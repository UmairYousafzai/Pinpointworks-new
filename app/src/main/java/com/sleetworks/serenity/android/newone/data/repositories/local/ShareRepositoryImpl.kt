package com.sleetworks.serenity.android.newone.data.repositories.local

import com.sleetworks.serenity.android.newone.data.datasource.local.dao.ShareDao
import com.sleetworks.serenity.android.newone.data.mappers.toEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.ShareEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.Share
import com.sleetworks.serenity.android.newone.domain.mapper.toDomainModel
import com.sleetworks.serenity.android.newone.domain.models.share.ShareDomainModel
import com.sleetworks.serenity.android.newone.domain.reporitories.local.ShareRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ShareRepositoryImpl @Inject constructor(val shareDao: ShareDao) :
    ShareRepository {
    override suspend fun insertShare(share: Share) {
        shareDao.insertShare(share.toEntity())
    }

    override suspend fun insertShares(shares: List<Share>, workspaceID: String) {

        shareDao.insertShares(shares.map {
            val shareEntity = it.toEntity()
            shareEntity.currentShare = shareEntity.targetRef.id == workspaceID
            shareEntity

        }
        )
    }

    override suspend fun getShareByID(shareID: String) = shareDao.getShareById(shareID)

    override suspend fun getShareByWorkspaceID(workspaceId: String) =
        shareDao.getShareByWorkspaceId(workspaceId)?.toDomainModel()

    override suspend fun getShareByWorkspaceIDFlow(workspaceId: String): Flow<ShareDomainModel?> {
        return shareDao.getShareByWorkspaceIdFlow(workspaceId).map { it?.toDomainModel() }
    }


    override suspend fun getAllShares() = shareDao.getAllShares()
}