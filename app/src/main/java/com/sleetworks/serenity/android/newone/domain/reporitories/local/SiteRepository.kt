package com.sleetworks.serenity.android.newone.domain.reporitories.local

import com.sleetworks.serenity.android.newone.data.models.local.entities.SiteEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.site.Site
import kotlinx.coroutines.flow.Flow

interface SiteRepository {

    suspend fun insertSite(site: Site)
    suspend fun insertSites(sites: List<Site>)
    suspend fun getSiteByID(siteID: String): SiteEntity?
    fun getSiteByIDFlow(siteID: String): Flow<SiteEntity?>
    suspend fun getAllSites(): List<SiteEntity?>
    fun getAllSitesFlow(): Flow<List<SiteEntity?>>
}