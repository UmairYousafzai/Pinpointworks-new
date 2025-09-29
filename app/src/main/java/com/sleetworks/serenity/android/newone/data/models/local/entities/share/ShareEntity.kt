package com.sleetworks.serenity.android.newone.data.models.local.entities.share
//
//import androidx.room.ColumnInfo
//import androidx.room.Embedded
//import androidx.room.Entity
//import androidx.room.Index
//import androidx.room.PrimaryKey
//import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.AdvancedAccessLevels
//import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.Permissions
//import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.TargetRef
//import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.UserRef
//
//@Entity(
//    tableName = "shares",
//    indices = [
//        Index(value = ["workspace_id"]),
//        Index(value = ["user_id"]),
//        Index(value = ["is_current"]),
//        Index(value = ["is_previous"])
//    ]
//)
//data class ShareEntity(
//    @PrimaryKey
//    val id: String,
//    @ColumnInfo(name = "workspace_id")
//    val workspaceId: String,
//    @ColumnInfo(name = "user_id")
//    val userId: String,
//    val label: String,
//    @ColumnInfo(name = "share_option")
//    val shareOption: String, // "admin", "owner", "normal", "limit"
//    val permissions: String, // JSON string for permissions
//    @ColumnInfo(name = "advanced_access_levels")
//    val advancedAccessLevels: String, // JSON string
//    @ColumnInfo(name = "defect_assignees")
//    val defectAssignees: String?, // JSON array
//    @ColumnInfo(name = "defect_tags")
//    val defectTags: String?, // JSON array
//    @ColumnInfo(name = "tag_limited")
//    val tagLimited: Boolean = false,
//    @ColumnInfo(name = "is_current")
//    val isCurrent: Boolean = false,
//    @ColumnInfo(name = "is_previous")
//    val isPrevious: Boolean = false,
//    @ColumnInfo(name = "created_at")
//    val createdAt: Long,
//    @ColumnInfo(name = "updated_at")
//    val updatedAt: Long
//)