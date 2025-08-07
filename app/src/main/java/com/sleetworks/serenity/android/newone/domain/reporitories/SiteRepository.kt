package com.sleetworks.serenity.android.newone.domain.reporitories

import com.sleetworks.serenity.android.newone.data.models.local.entities.SiteEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.site.Site

interface SiteRepository {

    suspend fun insertSite(site: Site)
    suspend fun insertSites(sites: List<Site>)
    suspend fun getSiteByID(siteID: String): SiteEntity?
    suspend fun getAllSites(): List<SiteEntity?>
}