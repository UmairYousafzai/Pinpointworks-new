package com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.site

import com.sleetworks.serenity.android.newone.data.models.remote.response.Header
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.customfield.CustomField

data class Site(
    val _id: String,
    val customFields: List<CustomField>,
    val header: Header,
    val logoRef: LogoRef,
    val name: String,
    val siteImageRef: SiteImageRef,
    val sitePlan: SitePlan,
    val sitePreferenceRef: SitePreferenceRef,
    val tags: List<String>,
    val totalBytes: Int,
    val type: String,
    val workspaceRef: WorkspaceRef
)