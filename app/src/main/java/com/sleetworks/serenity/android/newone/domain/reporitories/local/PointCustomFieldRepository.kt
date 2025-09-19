package com.sleetworks.serenity.android.newone.domain.reporitories.local

import com.sleetworks.serenity.android.newone.data.models.local.entities.customField.PointCustomFieldEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.PointCustomField

interface PointCustomFieldRepository {

    suspend fun insertCustomField(pointCustomField: PointCustomField,pointID: String)
    suspend fun insertCustomFields(pointCustomFields: List<PointCustomField>,pointID: String)
    suspend fun getCustomFieldsByPointID(pointID: String): List<PointCustomFieldEntity>?
    suspend fun getAllCustomFields(): List<PointCustomFieldEntity?>
}