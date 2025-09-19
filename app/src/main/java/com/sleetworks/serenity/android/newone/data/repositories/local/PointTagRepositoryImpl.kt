package com.sleetworks.serenity.android.newone.data.repositories.local

import com.sleetworks.serenity.android.newone.data.datasource.local.dao.PointTagDao
import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointTagEntity
import com.sleetworks.serenity.android.newone.domain.reporitories.local.PointTagRepository
import javax.inject.Inject

class PointTagRepositoryImpl @Inject constructor(
    val tagPointTagDao: PointTagDao,
) :
    PointTagRepository {

    override suspend fun insertTag(tag: PointTagEntity) {
        tagPointTagDao.insertPointTag(tag);
    }

    override suspend fun insertTags(tags: List<PointTagEntity>) {
        tagPointTagDao.insertPointTags(tags)
    }

    override suspend fun getTagsByPointID(pointID: String): List<PointTagEntity>? {
        return tagPointTagDao.getTagByPointId(pointID)
    }

    override suspend fun getAllTags(): List<PointTagEntity?> {
        return tagPointTagDao.getAllPointTags()
    }
}