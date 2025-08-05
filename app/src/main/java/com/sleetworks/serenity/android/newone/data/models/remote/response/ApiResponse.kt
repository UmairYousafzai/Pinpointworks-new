package com.sleetworks.serenity.android.newone.data.models.remote.response


data class ApiResponse<T>(
    val entity: T? = null,
    val error: String? = null,
    val errorMessage: String? = null
)
