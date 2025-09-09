package com.sleetworks.serenity.android.newone.presentation.common

import com.sleetworks.serenity.android.newone.data.models.local.entities.SiteEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.SubListItemEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.WorkspaceEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.customfield.SubListItem
import com.sleetworks.serenity.android.newone.presentation.model.SiteUiModel
import com.sleetworks.serenity.android.newone.presentation.model.WorkspaceUiModel


fun SiteEntity.toUiModel(
): SiteUiModel {

    return SiteUiModel(
        id = this.id,
        name = this.name,
        siteImageRef = this.siteImageRef,
        sitePlan = this.sitePlan,
        workspaceRef = this.workspaceRef,
    )
}

fun WorkspaceEntity.toUiModel(
    isSelected: Boolean
): WorkspaceUiModel {

    return WorkspaceUiModel(
        id = this.id,
        siteRef = this.siteRef,
        siteName = this.siteName,
        accountRef = this.accountRef,
        hidden = this.hidden,
        isSelected = isSelected
        )
}