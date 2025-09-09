package com.sleetworks.serenity.android.newone.domain.models.share

import java.io.Serializable

data class SitePermissionsDomainModel(
    val read: Boolean,
    val share: Boolean
): Serializable
