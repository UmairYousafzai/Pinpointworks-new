package com.sleetworks.serenity.android.newone.domain.reporitories

import com.sleetworks.serenity.android.newone.data.models.local.entities.ShareEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.Share

interface ShareRepository {

    suspend fun insertShare(share: Share)
    suspend fun insertShares(shares: List<Share>, workspaceID: String)
    suspend fun getShareByID(shareID: String): ShareEntity?
    suspend fun getAllShares(): List<ShareEntity?>
}