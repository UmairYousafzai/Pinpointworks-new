package com.sleetworks.serenity.android.newone.domain.reporitories.local

import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointTagEntity

interface PointTagRepository {

    suspend fun insertTag(tag: PointTagEntity)
    suspend fun insertTags(tags: List<PointTagEntity>)
    suspend fun getTagsByPointID(pointID: String): List<PointTagEntity>?
    suspend fun getAllTags(): List<PointTagEntity?>
}