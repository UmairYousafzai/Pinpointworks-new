package com.sleetworks.serenity.android.newone.data.repositories.remote

import android.content.Context
import com.google.gson.Gson
import com.sleetworks.serenity.android.newone.data.datasource.remote.ApiService
import com.sleetworks.serenity.android.newone.data.models.remote.response.ApiResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.LoginResponse
import com.sleetworks.serenity.android.newone.data.network.ApiException
import com.sleetworks.serenity.android.newone.data.network.NetworkUtil
import com.sleetworks.serenity.android.newone.data.network.Resource
import com.sleetworks.serenity.android.newone.data.network.RetrofitProvider
import com.sleetworks.serenity.android.newone.data.network.safeApiCall
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.AuthRemoteRepository
import javax.inject.Inject

class AuthRemoteRepositoryImpl @Inject constructor(
    val context: Context,
    val retrofitProvider: RetrofitProvider
) :
    AuthRemoteRepository {
    override suspend fun login(
        email: String,
        password: String,
    ): Resource<ApiResponse<LoginResponse>> {

        val result = safeApiCall(context, overrideSuccessCodes = setOf(403)) {
            retrofitProvider.getRetrofit().create(ApiService::class.java)
                .login(email, password)

        }

        when (result) {
            is Resource.Success -> {
                val entity = result.data.entity
                val loginResponse = Gson().fromJson(
                    Gson().toJson(entity), // Convert LinkedTreeMap to JSON string
                    LoginResponse::class.java
                )
                return Resource.Success(ApiResponse(loginResponse, null, null))

            }

            is Resource.Error -> {

                return result
            }

            Resource.Loading -> {
                // optional: handle loading state per call if needed
            }
        }
        return Resource.Error(ApiException.UnknownException("Unknown error"))

    }

    override suspend fun loginWithCode(
        email: String,
        password: String,
        code: String,
    ): Resource<ApiResponse<LoginResponse>> {
        return safeApiCall(context) {
            retrofitProvider.getRetrofit().create(ApiService::class.java)
                .loginWithCode(email, password, code)

        }

    }

    override suspend fun checkIfUserExists(
        email: String,
        baseURL: String
    ): Resource<Unit> {
        return safeApiCall(context) {
            NetworkUtil.createRetrofitForUrl("$baseURL/api/v1/").create(ApiService::class.java)
                .checkIfUserExists(email)
        }
    }


}