package com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share

import java.io.Serializable

data class WorkspacePreferenceDocRef(
    val type: String,
    val caption: String,
    val id: String
): Serializable
