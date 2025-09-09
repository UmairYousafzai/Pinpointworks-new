package com.sleetworks.serenity.android.newone.data.repositories.local

import com.sleetworks.serenity.android.newone.data.datasource.local.dao.SyncDetailDao
import com.sleetworks.serenity.android.newone.data.models.local.entities.SyncDetailEntity
import com.sleetworks.serenity.android.newone.domain.reporitories.local.SyncDetailRepository
import javax.inject.Inject

class SyncDetailRepositoryImpl @Inject constructor(val syncDetailDao: SyncDetailDao) :
    SyncDetailRepository {
    override suspend fun insertSyncDetail(syncDetail: SyncDetailEntity) {

        syncDetailDao.insertSynDetail(syncDetail)
    }

    override suspend fun getSyncDetailByWorkspaceID(
        workspaceID: String,
        type: String
    ): SyncDetailEntity? {
        return syncDetailDao.getSynDetailByWorkspaceId(workspaceID, type)
    }

    override suspend fun deleteSyncDetailByWorkspaceID(workspaceID: String): Int {
        return syncDetailDao.deleteSynDetailByWorkspaceId(workspaceID)
    }
}