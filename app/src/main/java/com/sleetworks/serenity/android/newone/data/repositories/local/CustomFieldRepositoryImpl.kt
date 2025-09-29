package com.sleetworks.serenity.android.newone.data.repositories.local

import com.sleetworks.serenity.android.newone.data.datasource.local.dao.CustomFieldTemplateDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.SubListDao
import com.sleetworks.serenity.android.newone.data.mappers.toEntities
import com.sleetworks.serenity.android.newone.data.mappers.toEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.customField.CustomFieldTemplateEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.SubListItemEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.customfield.CustomFieldTemplate
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.customfield.SubListItem
import com.sleetworks.serenity.android.newone.domain.reporitories.local.CustomFieldRepository
import javax.inject.Inject

class CustomFieldRepositoryImpl @Inject constructor(
    val customFieldDao: CustomFieldTemplateDao,
    val subListDao: SubListDao
) :
    CustomFieldRepository {

    override suspend fun insertCustomFields(
        customFields: List<CustomFieldTemplate>,
        workspaceID: String
    ) {

        val customFieldEntities = customFields.map { customField ->
            val entity = customField.toEntities(null, workspaceID)
            entity
        }.flatten()
        customFieldDao.insertCustomFields(customFieldEntities)


    }

    override suspend fun getCustomFieldByID(customFieldID: String): CustomFieldTemplateEntity? {
        return customFieldDao.getCustomFieldById(customFieldID)
    }

    override suspend fun getAllCustomFields(): List<CustomFieldTemplateEntity?> {
        return customFieldDao.getAllCustomFields()
    }


}