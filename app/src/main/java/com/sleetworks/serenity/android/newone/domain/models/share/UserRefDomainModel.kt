package com.sleetworks.serenity.android.newone.domain.models.share

import java.io.Serializable

data class UserRefDomainModel(
    val type: String,
    val caption: String,
    val primaryImageId: String,
    val id: String
): Serializable
