package com.sleetworks.serenity.android.newone.presentation.model

import com.sleetworks.serenity.android.newone.data.models.remote.response.Header
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.WorkspaceRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.site.SiteImageRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.site.SitePlan

data class SiteUiModel(
    val id: String,
    val name: String,
    val siteImageRef: SiteImageRef?,
    val sitePlan: SitePlan?,
    val workspaceRef: WorkspaceRef,
    val isSelected: Boolean= false
)
