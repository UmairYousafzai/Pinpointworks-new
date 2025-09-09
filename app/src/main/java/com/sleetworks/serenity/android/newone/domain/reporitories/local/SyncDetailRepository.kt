package com.sleetworks.serenity.android.newone.domain.reporitories.local

import com.sleetworks.serenity.android.newone.data.models.local.entities.SyncDetailEntity

interface SyncDetailRepository {

    suspend fun insertSyncDetail(syncDetail: SyncDetailEntity)
    suspend fun getSyncDetailByWorkspaceID(workspaceID: String, type: String): SyncDetailEntity?
    suspend fun deleteSyncDetailByWorkspaceID(workspaceID: String): Int
}