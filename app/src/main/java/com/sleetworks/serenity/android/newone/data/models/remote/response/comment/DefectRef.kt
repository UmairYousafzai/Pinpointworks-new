package com.sleetworks.serenity.android.newone.data.models.remote.response.comment

import androidx.room.ColumnInfo

data class DefectRef(
    @ColumnInfo(name = "point_caption")
    val caption: String,
    @ColumnInfo(name = "point_id")
    val id: String,
    @ColumnInfo(name = "point_type")
    val type: String
)