package com.sleetworks.serenity.android.data.models

data class ApiResponse<T>(
    val entity: T? = null,
    val error: String? = null,
    val errorMessage: String? = null
)
