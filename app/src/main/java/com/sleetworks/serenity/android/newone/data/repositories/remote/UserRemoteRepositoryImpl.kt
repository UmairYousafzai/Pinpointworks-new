package com.sleetworks.serenity.android.newone.data.repositories.remote

import android.content.Context
import com.sleetworks.serenity.android.newone.data.datasource.remote.ApiService
import com.sleetworks.serenity.android.newone.data.models.remote.request.LoggedRequest
import com.sleetworks.serenity.android.newone.data.models.remote.response.ApiResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.User
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.UserResponse
import com.sleetworks.serenity.android.newone.data.network.Resource
import com.sleetworks.serenity.android.newone.data.network.RetrofitProvider
import com.sleetworks.serenity.android.newone.data.network.safeApiCall
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.UserRemoteRepository
import javax.inject.Inject

class UserRemoteRepositoryImpl @Inject constructor(
    private val context: Context,
    private val retrofitProvider: RetrofitProvider
) : UserRemoteRepository {


    override suspend fun getAuthUser(
        loggedRequest: LoggedRequest
    ): Resource<ApiResponse<UserResponse>> {

        return safeApiCall(context) {
            retrofitProvider.getRetrofit().create(ApiService::class.java)
                .getLogged(loggedRequest.token, loggedRequest.email)
        }


    }

    override suspend fun getLoginUser(): Resource<ApiResponse<User>> {
        return safeApiCall(context) {
            retrofitProvider.getRetrofit().create(ApiService::class.java).getSelfAccount()
        }
    }
}