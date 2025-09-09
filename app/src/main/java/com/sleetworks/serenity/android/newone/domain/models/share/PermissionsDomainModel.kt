package com.sleetworks.serenity.android.newone.domain.models.share

import java.io.Serializable

data class PermissionsDomainModel(
    val fullVisibility: Boolean,
    val defect: DefectPermissionsDomainModel,
    val site: SitePermissionsDomainModel
): Serializable
