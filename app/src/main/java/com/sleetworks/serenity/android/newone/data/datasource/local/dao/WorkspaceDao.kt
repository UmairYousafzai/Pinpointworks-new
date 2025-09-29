package com.sleetworks.serenity.android.newone.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.sleetworks.serenity.android.newone.data.models.local.entities.WorkspaceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkspaceDao {

    @Upsert
    suspend fun insertWorkspace(workspace: WorkspaceEntity)

    @Upsert
    suspend fun insertWorkspaces(workspace: List<WorkspaceEntity>)

    @Query("SELECT * FROM workspace WHERE id = :workspaceID")
    suspend fun getWorkspaceById(workspaceID: String): WorkspaceEntity?

    @Query("SELECT * FROM workspace WHERE id = :workspaceID")
    fun getWorkspaceByIdFlow(workspaceID: String): Flow<WorkspaceEntity?>


    @Query("SELECT * FROM workspace")
    suspend fun getAllWorkspaces(): List<WorkspaceEntity>

    @Query("SELECT * FROM workspace")
    fun getAllWorkspacesFlow(): Flow<List<WorkspaceEntity>>

}