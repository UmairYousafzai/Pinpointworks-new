package com.sleetworks.serenity.android.newone.data.repositories.local

import com.sleetworks.serenity.android.newone.data.datasource.local.dao.PointCustomFieldDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.PointTagDao
import com.sleetworks.serenity.android.newone.data.mappers.toEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.customField.PointCustomFieldEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointTagEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.PointCustomField
import com.sleetworks.serenity.android.newone.domain.reporitories.local.PointCustomFieldRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.PointTagRepository
import javax.inject.Inject

class PointCustomFieldRepositoryImpl @Inject constructor(
    val pointCustomFieldDao: PointCustomFieldDao,
) :
    PointCustomFieldRepository {


    override suspend fun insertCustomField(
        pointCustomField: PointCustomField,
        pointID: String
    ) {
        pointCustomFieldDao.insertPointCustomField(pointCustomField.toEntity(pointID));
    }

    override suspend fun insertCustomFields(
        pointCustomFields: List<PointCustomField>,
        pointID: String
    ) {
        pointCustomFieldDao.insertPointCustomFields(pointCustomFields.map {
            it.toEntity(pointID)
        })
    }

    override suspend fun getCustomFieldsByPointID(pointID: String): List<PointCustomFieldEntity>? {
        return pointCustomFieldDao.getCustomFieldByPointId(pointID)
    }

    override suspend fun getAllCustomFields(): List<PointCustomFieldEntity?> {
        return pointCustomFieldDao.getAllCustomFields()
    }
}