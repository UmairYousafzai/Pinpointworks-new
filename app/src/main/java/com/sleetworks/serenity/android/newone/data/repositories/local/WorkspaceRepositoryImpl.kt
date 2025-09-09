package com.sleetworks.serenity.android.newone.data.repositories.local

import com.sleetworks.serenity.android.newone.data.datasource.local.dao.WorkspaceDao
import com.sleetworks.serenity.android.newone.data.mappers.toEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.WorkspaceEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.Workspace
import com.sleetworks.serenity.android.newone.domain.reporitories.local.WorkspaceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WorkspaceRepositoryImpl @Inject constructor(val workspaceDao: WorkspaceDao) :
    WorkspaceRepository {


    override suspend fun insertWorkspace(workspace: Workspace) {
        workspaceDao.insertWorkspace(workspace.toEntity())
    }

    override suspend fun insertWorkspaces(workspace: List<Workspace>) {
        workspaceDao.insertWorkspaces(workspace.map { it.toEntity() })
    }

    override suspend fun getWorkspaceByID(workspaceID: String): WorkspaceEntity? {
        return workspaceDao.getWorkspaceById(workspaceID)
    }

    override suspend fun getAllWorkspaces(): List<WorkspaceEntity?> {
        return workspaceDao.getAllWorkspaces()
    }

    override  fun getAllWorkspacesFlow(): Flow<List<WorkspaceEntity?>> {
        return workspaceDao.getAllWorkspacesFlow()
    }

    override fun getCurrentWorkspaceFlow(workspaceID: String): Flow<WorkspaceEntity?> {
        return workspaceDao.getWorkspaceByIdFlow(workspaceID)
    }
}