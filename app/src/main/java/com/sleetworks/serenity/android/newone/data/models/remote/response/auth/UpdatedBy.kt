package com.sleetworks.serenity.android.newone.data.models.remote.response.auth

import java.io.Serializable

data class UpdatedBy(
    val caption: String,
    val id: String,
    val primaryImageId: String,
    val type: String
): Serializable