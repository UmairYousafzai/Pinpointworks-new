package com.sleetworks.serenity.android.newone.data.repositories.local

import com.sleetworks.serenity.android.newone.data.datasource.local.dao.SiteDao
import com.sleetworks.serenity.android.newone.data.mappers.toEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.SiteEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.site.Site
import com.sleetworks.serenity.android.newone.domain.reporitories.SiteRepository
import javax.inject.Inject

class SiteRepositoryImpl @Inject constructor(val siteDao: SiteDao) :
    SiteRepository {
    override suspend fun insertSite(site: Site) {
        siteDao.insertSite(site.toEntity())
    }

    override suspend fun insertSites(sites: List<Site>) {
        siteDao.insertSites(sites.map { it.toEntity() })
    }

    override suspend fun getSiteByID(siteID: String): SiteEntity? {
        return siteDao.getSiteById(siteID)
    }

    override suspend fun getAllSites(): List<SiteEntity?> {
        return siteDao.getAllSites()
    }
}