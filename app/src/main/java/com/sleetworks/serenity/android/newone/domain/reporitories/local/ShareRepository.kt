package com.sleetworks.serenity.android.newone.domain.reporitories.local

import com.sleetworks.serenity.android.newone.data.models.local.entities.ShareEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.Share
import com.sleetworks.serenity.android.newone.domain.models.share.ShareDomainModel
import kotlinx.coroutines.flow.Flow

interface ShareRepository {

    suspend fun insertShare(share: Share)
    suspend fun insertShares(shares: List<Share>, workspaceID: String)
    suspend fun getShareByID(shareID: String): ShareEntity?
    suspend fun getShareByWorkspaceID(workspaceId: String): ShareDomainModel?
    suspend fun getShareByWorkspaceIDFlow(workspaceId: String): Flow<ShareDomainModel?>
    suspend fun getAllShares(): List<ShareEntity?>
}