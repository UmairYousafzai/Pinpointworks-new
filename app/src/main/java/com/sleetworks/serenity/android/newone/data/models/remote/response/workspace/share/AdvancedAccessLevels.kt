package com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share

import com.sleetworks.serenity.android.newone.domain.models.share.CustomFieldPermissionDomainModel
import java.io.Serializable

data class AdvancedAccessLevels(
    val customFields: ArrayList<CustomFieldPermissionDomainModel>? = null,
    val tags: PermissionWrapper,
    val timeline: Timeline,
    val sitePlan: PermissionWrapper,
    val exports: PermissionWrapper
) : Serializable
