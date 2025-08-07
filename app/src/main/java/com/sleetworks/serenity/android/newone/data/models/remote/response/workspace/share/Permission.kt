package com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share

import java.io.Serializable

data class Permission(
    val read: Boolean,
    val edit: Boolean? = null
): Serializable
