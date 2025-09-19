package com.sleetworks.serenity.android.newone.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.sleetworks.serenity.android.newone.data.models.local.entities.customField.CustomFieldTemplateEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.customField.PointCustomFieldEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointAssigneeEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointTagEntity

@Dao
interface PointAssigneeDao {

    @Upsert
    suspend fun insertPointAssignee(customField: PointAssigneeEntity)

    @Upsert
    suspend fun insertPointAssignees(sites: List<PointAssigneeEntity>)

    @Query("SELECT * FROM point_assignees WHERE point_id = :pointID")
    suspend fun getAssigneeByPointId(pointID: String): List<PointAssigneeEntity>?

    @Query("SELECT * FROM point_assignees")
    suspend fun getAllPointAssignees(): List<PointAssigneeEntity>
}