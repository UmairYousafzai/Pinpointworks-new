package com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.site

import androidx.room.ColumnInfo

data class SiteImageRef(
    @ColumnInfo(name = "site_image_caption")
    val caption: String?,
    @ColumnInfo(name = "site_image_id")
    val id: String?,
    @ColumnInfo(name = "site_image_type")
    val type: String?
)