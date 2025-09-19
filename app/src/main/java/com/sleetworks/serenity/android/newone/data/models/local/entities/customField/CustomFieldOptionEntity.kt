package com.sleetworks.serenity.android.newone.data.models.local.entities.customField

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "custom_field_options",
    indices = [
        Index(value = ["custom_field_id"]),
        Index(value = ["parent_id"])
    ]
)
data class CustomFieldOptionEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "custom_field_id")
    val customFieldId: Long,
    val label: String,
    val value: String,
    val order: Int,
    @ColumnInfo(name = "parent_id")
    val parentId: String? = null, // For hierarchical lists
    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true
)