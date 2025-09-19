package com.sleetworks.serenity.android.newone.data.models.local.entities.point

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "point_locations",
    indices = [
        Index(value = ["point_id"]),
        Index(value = ["type"])
    ]
)
data class PointLocationEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "point_id")
    val pointId: String,
    val type: String, // "pin", "polygon", "line"
    val coordinates: String, // JSON array of coordinates
    val metadata: String?, // Additional location data
    @ColumnInfo(name = "created_at")
    val createdAt: Long
)