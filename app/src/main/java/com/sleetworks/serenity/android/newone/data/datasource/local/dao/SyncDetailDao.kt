package com.sleetworks.serenity.android.newone.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.sleetworks.serenity.android.newone.data.models.local.entities.SyncDetailEntity

@Dao
interface SyncDetailDao {

    @Upsert
    suspend fun insertSynDetail(synDetail: SyncDetailEntity)

    @Upsert
    suspend fun insertSynDetails(synDetails: List<SyncDetailEntity>)

    @Query("SELECT * FROM syn_detail WHERE workspace_id = :workspaceID AND data_type = :type")
    fun getSynDetailByWorkspaceId(workspaceID: String, type: String): SyncDetailEntity?


    @Query("DELETE FROM point WHERE workspace_id IN (:workspaceID)")
    suspend fun deleteSynDetailByWorkspaceId(workspaceID: String): Int
}