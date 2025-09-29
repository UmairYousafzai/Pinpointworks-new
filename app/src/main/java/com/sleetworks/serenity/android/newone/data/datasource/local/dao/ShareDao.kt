package com.sleetworks.serenity.android.newone.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.sleetworks.serenity.android.newone.data.models.local.entities.ShareEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShareDao {

    @Upsert
    suspend fun insertShare(share: ShareEntity)

    @Upsert
    suspend fun insertShares(shares: List<ShareEntity>)

    @Query("SELECT * FROM share WHERE id = :shareID")
    suspend fun getShareById(shareID: String): ShareEntity?

    @Query("SELECT * FROM share WHERE workspace_id = :workspaceID")
    suspend fun getShareByWorkspaceId(workspaceID: String): ShareEntity?

    @Query("SELECT * FROM share WHERE workspace_id = :workspaceID")
    fun getShareByWorkspaceIdFlow(workspaceID: String): Flow<ShareEntity?>

    @Query("SELECT * FROM share")
    suspend fun getAllShares(): List<ShareEntity>
}