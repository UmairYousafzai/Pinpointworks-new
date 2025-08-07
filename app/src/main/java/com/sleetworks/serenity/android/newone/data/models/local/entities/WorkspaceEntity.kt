package com.sleetworks.serenity.android.newone.data.models.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.AccountRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.SiteRef


@Entity(tableName = "workspace")
data class WorkspaceEntity(
    @PrimaryKey
    val id: String,
    val accountRef: AccountRef,
    val hidden: Boolean,
    val siteName: String,
    val siteRef: SiteRef,
)
