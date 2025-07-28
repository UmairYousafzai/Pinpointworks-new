package com.sleetworks.serenity.android.domain.reporitories

import com.sleetworks.serenity.android.data.models.ApiResponse
import com.sleetworks.serenity.android.data.models.auth.LoginResponse

interface AuthRepository {

    fun login(email: String, password: String): ApiResponse<LoginResponse>
    fun loginWithCode(email: String, password: String, code: String): ApiResponse<LoginResponse>
    fun checkIfUserExists(email: String): ApiResponse<Void>
}