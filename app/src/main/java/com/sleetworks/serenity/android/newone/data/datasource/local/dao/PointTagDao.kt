package com.sleetworks.serenity.android.newone.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.sleetworks.serenity.android.newone.data.models.local.entities.customField.CustomFieldTemplateEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.customField.PointCustomFieldEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointTagEntity

@Dao
interface PointTagDao {

    @Upsert
    suspend fun insertPointTag(customField: PointTagEntity)

    @Upsert
    suspend fun insertPointTags(sites: List<PointTagEntity>)

    @Query("SELECT * FROM point_tags WHERE point_id = :pointID")
    suspend fun getTagByPointId(pointID: String): List<PointTagEntity>?

    @Query("SELECT * FROM point_tags")
    suspend fun getAllPointTags(): List<PointTagEntity>
}