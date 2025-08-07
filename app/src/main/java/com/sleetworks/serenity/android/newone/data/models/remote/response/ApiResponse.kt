package com.sleetworks.serenity.android.newone.data.models.remote.response

import java.io.Serializable


data class ApiResponse<T>(
    val entity: T? = null,
    val error: String? = null,
    val errorMessage: String? = null
): Serializable
