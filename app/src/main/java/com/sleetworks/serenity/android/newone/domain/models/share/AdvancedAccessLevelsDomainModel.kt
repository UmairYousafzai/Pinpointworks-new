package com.sleetworks.serenity.android.newone.domain.models.share

import java.io.Serializable

data class AdvancedAccessLevelsDomainModel(
    val customFields: ArrayList<CustomFieldPermissionDomainModel>? = null,
    val tags: PermissionWrapperDomainModel,
    val timeline: TimelineDomainModel,
    val sitePlan: PermissionWrapperDomainModel,
    val exports: PermissionWrapperDomainModel
) : Serializable
