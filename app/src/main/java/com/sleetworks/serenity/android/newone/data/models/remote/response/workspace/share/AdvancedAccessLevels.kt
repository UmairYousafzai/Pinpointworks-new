package com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share

import java.io.Serializable

data class AdvancedAccessLevels(
    val customFields: ArrayList<CustomFieldPermission>? = null,
    val tags: PermissionWrapper,
    val timeline: Timeline,
    val sitePlan: PermissionWrapper,
    val exports: PermissionWrapper
) : Serializable
