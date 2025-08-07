package com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share

import java.io.Serializable

data class Timeline(
    val permission: Permission,
    val comments: PermissionWrapper
): Serializable
