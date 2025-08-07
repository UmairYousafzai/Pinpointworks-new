package com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share

import java.io.Serializable

data class CustomFieldPermission(
    val templateId: Long,
    val permission: Permission
): Serializable
