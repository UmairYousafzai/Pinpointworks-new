package com.sleetworks.serenity.android.newone.data.models.local.entities.point

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

//@Entity(
//    tableName = "point_media",
//    indices = [
//        Index(value = ["point_id"]),
//        Index(value = ["type"]),
//        Index(value = ["order"])
//    ]
//)
//data class PointMediaEntity(
//    @PrimaryKey
//    val id: String,
//    @ColumnInfo(name = "point_id")
//    val pointId: String,
//    val type: String, // "image", "video", "document", "360_image"
//    val url: String,
//    @ColumnInfo(name = "thumbnail_url")
//    val thumbnailUrl: String?,
//    val filename: String,
//    val size: Long,
//    val metadata: String, // JSON for additional data
//    val order: Int,
//    @ColumnInfo(name = "uploaded_at")
//    val uploadedAt: Long
//)