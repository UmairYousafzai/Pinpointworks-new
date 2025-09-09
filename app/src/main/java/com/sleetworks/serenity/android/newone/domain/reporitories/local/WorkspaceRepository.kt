package com.sleetworks.serenity.android.newone.domain.reporitories.local

import com.sleetworks.serenity.android.newone.data.models.local.entities.WorkspaceEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.Workspace
import kotlinx.coroutines.flow.Flow

interface WorkspaceRepository {


    suspend fun insertWorkspace(workspace: Workspace)
    suspend fun insertWorkspaces(workspace: List<Workspace>)
    suspend fun getWorkspaceByID(workspaceID: String): WorkspaceEntity?
    suspend fun getAllWorkspaces(): List<WorkspaceEntity?>
    fun getAllWorkspacesFlow(): Flow<List<WorkspaceEntity?>>
    fun getCurrentWorkspaceFlow(workspaceID: String): Flow<WorkspaceEntity?>
}