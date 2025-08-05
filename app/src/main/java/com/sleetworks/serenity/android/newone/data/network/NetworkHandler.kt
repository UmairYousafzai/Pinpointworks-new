package com.sleetworks.serenity.android.newone.data.network

import android.content.Context
import com.sleetworks.serenity.android.newone.data.models.remote.response.ApiResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.LoginResponse
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val apiException: ApiException) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
}

sealed class ApiException(message: String) : Exception(message) {
    class NetworkException : ApiException("Please check your internet connection.")
    class TimeoutException : ApiException("Request timed out.")
    class UnauthorizedException : ApiException("Unauthorized access.")
    class UnknownException(message: String) : ApiException(message)
    class DnsResolutionException :
        ApiException("Unable to resolve server address. Please check the URL or try again later.")
}

suspend fun <T> safeApiCall(
    context: Context,
    overrideSuccessCodes: Set<Int> = emptySet(),
    apiCall: suspend () -> Response<T>
): Resource<T> {
    return try {
        if (!NetworkUtil.isInternetAvailable(context)) {
            return Resource.Error(ApiException.NetworkException())
        }
        val response = apiCall()
        val code= response.code()

        if (response.isSuccessful) {

            Resource.Success(response.body() ?: Unit as T)
        } else if (overrideSuccessCodes.contains(code)){
            val errorString = JSONObject(response.errorBody()?.string()).getString("error")
            Resource.Success(response.body() ?: ApiResponse<LoginResponse>(error = errorString) as T)
        }
        else {
            when (response.code()) {
                401 -> Resource.Error(ApiException.UnauthorizedException())
                404 -> Resource.Error(ApiException.DnsResolutionException())
                else -> {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown API error"
                    Resource.Error(ApiException.UnknownException(errorMessage))
                }
            }
        }

    } catch (e: Exception) {
        val exception = when (e) {
            is UnknownHostException -> ApiException.DnsResolutionException()
            is IOException -> {
                if (e.message?.contains("No address associated with hostname") == true) {
                    ApiException.DnsResolutionException()
                } else {
                    ApiException.NetworkException()
                }
            }

            is SocketTimeoutException -> ApiException.TimeoutException()
            else -> ApiException.UnknownException(e.localizedMessage ?: "Unexpected error")
        }
        Resource.Error(exception)
    }
}
