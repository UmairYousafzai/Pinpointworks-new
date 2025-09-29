package com.sleetworks.serenity.android.newone.data.models.remote.response

import com.sleetworks.serenity.android.newone.utils.TextRichOptions
import java.io.Serializable


data class ApiResponse<T>(
    val entity: T? = null,
    val error: String? = null,
    val errorMessage: String? = null,
    val ops: List<TextRichOptions>? =null

): Serializable
