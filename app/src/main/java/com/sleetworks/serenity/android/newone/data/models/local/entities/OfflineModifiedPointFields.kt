package com.sleetworks.serenity.android.newone.data.models.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.sleetworks.serenity.android.newone.data.models.local.OfflineFieldValue


@Entity(tableName = "offline_modified_point_fields"
, indices = [Index(
        value = [ "field"],
        unique = true
    )]
)
data class OfflineModifiedPointFields(
    @PrimaryKey(autoGenerate = true)
    val id: Int?=null,
    val workspaceId: String,
    val pointId: String,
    val field: String,
    val value: OfflineFieldValue
)
