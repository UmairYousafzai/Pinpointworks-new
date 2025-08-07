package com.sleetworks.serenity.android.newone.data.repositories.local

import com.sleetworks.serenity.android.newone.data.datasource.local.dao.ShareDao
import com.sleetworks.serenity.android.newone.data.mappers.toEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.ShareEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.Share
import com.sleetworks.serenity.android.newone.domain.reporitories.ShareRepository
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

    override suspend fun getShareByID(shareID: String): ShareEntity? {
        return shareDao.getShareById(shareID)
    }

    override suspend fun getAllShares(): List<ShareEntity?> {
        return shareDao.getAllShares()
    }
}