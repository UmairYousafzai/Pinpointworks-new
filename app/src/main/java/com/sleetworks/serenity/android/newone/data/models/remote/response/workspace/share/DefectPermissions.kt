package com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share

import java.io.Serializable

data class DefectPermissions(
    val read: Boolean,
    val edit: Boolean,
    val create: Boolean,
    val contribute: Boolean,
    val delete: Boolean
): Serializable
