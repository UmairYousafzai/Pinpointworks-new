package com.sleetworks.serenity.android.newone.presentation.model

import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.AccountRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.SiteRef

data class WorkspaceUiModel(
    val id: String,
    val accountRef: AccountRef,
    val hidden: Boolean,
    val siteName: String,
    val siteRef: SiteRef,
    val isSelected: Boolean = false,
)
