package com.sleetworks.serenity.android.newone.domain.models.share

import java.io.Serializable

data class DefectPermissionsDomainModel(
    val read: Boolean,
    val edit: Boolean,
    val create: Boolean,
    val contribute: Boolean,
    val delete: Boolean
): Serializable
