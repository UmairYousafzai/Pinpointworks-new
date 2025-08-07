package com.sleetworks.serenity.android.newone.data.models.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.AdvancedAccessLevels
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.Permissions
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.TargetRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.UserRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.WorkspacePreferenceDocRef

@Entity(tableName = "share")
data class ShareEntity(
    @PrimaryKey
    val id: String,
    val type: String,
    val label: String,
    val userRef: UserRef,
    val defectTags: ArrayList<String>?,
    val tagLimited: Boolean?,
    val hidden: Boolean?,
    var currentShare: Boolean ,
    var previousShare: Boolean,
    @Embedded
    val targetRef: TargetRef,
    val shareOption: String?,
    val permissions: Permissions?,
    val advancedAccessLevels: AdvancedAccessLevels? = null,
)

