package com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share

import java.io.Serializable

data class Permissions(
    val fullVisibility: Boolean,
    val defect: DefectPermissions,
    val site: SitePermissions
): Serializable
