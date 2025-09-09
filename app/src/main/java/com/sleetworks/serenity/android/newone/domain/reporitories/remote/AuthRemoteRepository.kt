package com.sleetworks.serenity.android.newone.domain.reporitories.remote

import com.sleetworks.serenity.android.newone.data.models.remote.response.ApiResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.LoginResponse
import com.sleetworks.serenity.android.newone.data.network.Resource

interface AuthRemoteRepository {

    suspend fun login(
        email: String,
        password: String
    ): Resource<ApiResponse<LoginResponse>>

    suspend fun loginWithCode(
        email: String,
        password: String,
        code: String,
    ): Resource<ApiResponse<LoginResponse>>

    suspend fun checkIfUserExists(email: String, baseURL: String): Resource<Unit>

}