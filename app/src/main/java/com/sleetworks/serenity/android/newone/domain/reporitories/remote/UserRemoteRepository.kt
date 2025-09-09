package com.sleetworks.serenity.android.newone.domain.reporitories.remote

import com.sleetworks.serenity.android.newone.data.models.remote.request.LoggedRequest
import com.sleetworks.serenity.android.newone.data.models.remote.response.ApiResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.User
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.UserResponse
import com.sleetworks.serenity.android.newone.data.network.Resource

interface UserRemoteRepository {
    suspend fun getAuthUser(
        loggedRequest: LoggedRequest
    ): Resource<ApiResponse<UserResponse>>

    suspend fun getLoginUser(
    ): Resource<ApiResponse<User>>
}