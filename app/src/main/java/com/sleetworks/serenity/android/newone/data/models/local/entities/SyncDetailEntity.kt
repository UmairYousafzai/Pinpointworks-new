package com.sleetworks.serenity.android.newone.data.models.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "syn_detail")
data class SyncDetailEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    @ColumnInfo(name = "workspace_id")
    val workspaceId: String,
    @ColumnInfo(name = "data_type")
    val dataType: String,
    val time: Long,

    )

enum class  SyncType{
    POINT,
    WORKSPACE
}