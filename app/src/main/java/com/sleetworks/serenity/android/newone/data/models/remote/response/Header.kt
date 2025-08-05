package com.sleetworks.serenity.android.newone.data.models.remote.response

import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.CreatedBy
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.UpdatedBy
import java.io.Serializable

data class Header(
    val createdBy: CreatedBy,
    val createdEpochMillis: Long,
    val createdUserAgent: String,
    val updatedBy: UpdatedBy,
    val updatedEpochMillis: Long,
    val updatedUserAgent: String
): Serializable