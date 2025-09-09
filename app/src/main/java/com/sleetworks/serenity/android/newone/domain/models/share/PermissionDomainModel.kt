package com.sleetworks.serenity.android.newone.domain.models.share

import java.io.Serializable

data class PermissionDomainModel(
    val read: Boolean,
    val edit: Boolean? = null
): Serializable
