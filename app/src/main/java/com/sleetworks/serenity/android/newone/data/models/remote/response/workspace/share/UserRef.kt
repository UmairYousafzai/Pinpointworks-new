package com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share

import java.io.Serializable

data class UserRef(
    val type: String,
    val caption: String,
    val primaryImageId: String,
    val id: String
): Serializable
