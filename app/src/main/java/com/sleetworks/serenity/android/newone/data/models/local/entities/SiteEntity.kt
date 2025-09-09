package com.sleetworks.serenity.android.newone.data.models.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sleetworks.serenity.android.newone.data.models.remote.response.Header
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.site.SiteImageRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.site.SitePlan
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.site.SitePreferenceRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.WorkspaceRef

@Entity(tableName = "site")
data class SiteEntity(
    @PrimaryKey
    val id: String,
    val header: Header,
    val name: String,
    @Embedded
    val siteImageRef: SiteImageRef?,
    val sitePlan: SitePlan?,
    val tags: ArrayList<String>?,
    val totalBytes: Int,
    val type: String,
    @Embedded
    val workspaceRef: WorkspaceRef
)