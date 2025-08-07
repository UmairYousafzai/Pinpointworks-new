package com.sleetworks.serenity.android.newone.data.models.remote.response.workspace

import java.io.Serializable

data class WorkspaceResponse(
    val accountFeatures: AccountFeatures,
    val id: String,
    val name: String,
    val order: Int,
    val workspaces: List<Workspace>
): Serializable