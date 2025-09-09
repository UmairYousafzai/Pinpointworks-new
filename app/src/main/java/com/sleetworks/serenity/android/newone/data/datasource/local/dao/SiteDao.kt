package com.sleetworks.serenity.android.newone.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.sleetworks.serenity.android.newone.data.models.local.entities.SiteEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.UserEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.WorkspaceEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.Workspace
import kotlinx.coroutines.flow.Flow

@Dao
interface SiteDao {

    @Upsert
    suspend fun insertSite(site: SiteEntity)

    @Upsert
    suspend fun insertSites(sites: List<SiteEntity>)

    @Query("SELECT * FROM site WHERE id = :siteID")
    suspend fun getSiteById(siteID: String): SiteEntity?

    @Query("SELECT * FROM site WHERE workspace_id = :siteID")
    fun getSiteByIdFlow(siteID: String): Flow<SiteEntity?>

    @Query("SELECT * FROM site")
    suspend fun getAllSites(): List<SiteEntity>

    @Query("SELECT * FROM site")
    fun getAllSitesFlow(): Flow<List<SiteEntity>>
}