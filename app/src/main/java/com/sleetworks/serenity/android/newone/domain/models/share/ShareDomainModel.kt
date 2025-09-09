package com.sleetworks.serenity.android.newone.domain.models.share

import com.sleetworks.serenity.android.newone.data.models.remote.response.BaseResponse
import java.io.Serializable

data class ShareDomainModel(
    val id: String,
    val type: String,
    val label: String,
    val userRef: UserRefDomainModel,
    val defectTags: ArrayList<String>?,
    val tagLimited: Boolean?,
    val hidden: Boolean?,
    val targetRef: TargetRefDomainModel,
    val shareOption: String?,
    val permissions: PermissionsDomainModel?,
    val advancedAccessLevels: AdvancedAccessLevelsDomainModel? = null,
    val workspacePreferenceDocRef: WorkspacePreferenceDocRefDomainModel? = null,
    var currentShare: Boolean ,
    var previousShare: Boolean,
) : Serializable, BaseResponse() {
    fun canReadTags(): Boolean {
        return advancedAccessLevels?.tags?.permission?.read ?: true
    }

    fun canEditTags(): Boolean {
        return advancedAccessLevels?.tags?.permission?.edit ?: true
    }

    fun canReadComments(): Boolean {
        return advancedAccessLevels?.timeline?.comments?.permission?.read ?: true
    }

    fun canEditComments(): Boolean {
        return advancedAccessLevels?.timeline?.comments?.permission?.edit ?: true
    }
}
