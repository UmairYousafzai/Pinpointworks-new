package com.sleetworks.serenity.android.newone.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.sleetworks.serenity.android.newone.data.models.local.entities.customField.CustomFieldTemplateEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.customField.PointCustomFieldEntity

@Dao
interface PointCustomFieldDao {

    @Upsert
    suspend fun insertPointCustomField(customField: PointCustomFieldEntity)

    @Upsert
    suspend fun insertPointCustomFields(sites: List<PointCustomFieldEntity>)

    @Query("SELECT * FROM point_custom_fields WHERE point_id = :pointID")
    suspend fun getCustomFieldByPointId(pointID: String): List<PointCustomFieldEntity>?

    @Query("SELECT * FROM point_custom_fields")
    suspend fun getAllCustomFields(): List<PointCustomFieldEntity>
}