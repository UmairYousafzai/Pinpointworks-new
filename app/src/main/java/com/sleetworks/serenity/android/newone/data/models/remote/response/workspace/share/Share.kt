package com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share

import com.google.gson.annotations.SerializedName
import com.sleetworks.serenity.android.newone.data.models.remote.response.BaseResponse
import java.io.Serializable

data class Share(
    @SerializedName("_id")
    val id: String,
    val type: String,
    val label: String,
    val userRef: UserRef,
    val defectTags: ArrayList<String>,
    val tagLimited: Boolean,
    val hidden: Boolean,
    val targetRef: TargetRef,
    val shareOption: String,
    val permissions: Permissions,
    val advancedAccessLevels: AdvancedAccessLevels? = null,
    val workspacePreferenceDocRef: WorkspacePreferenceDocRef? = null,
): Serializable, BaseResponse()
