package com.sleetworks.serenity.android.newone.data.repositories.local

import com.sleetworks.serenity.android.newone.data.datasource.local.dao.CustomFieldTemplateDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.SubListDao
import com.sleetworks.serenity.android.newone.data.mappers.toEntities
import com.sleetworks.serenity.android.newone.data.models.local.entities.customField.CustomFieldTemplateEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.customfield.CustomFieldTemplate
import com.sleetworks.serenity.android.newone.domain.reporitories.local.CustomFieldRepository
import kotlinx.coroutines.flow.Flow
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
            customField.toEntities(null, workspaceID)
        }
        customFieldDao.insertCustomFields(customFieldEntities)


    }

    override suspend fun getCustomFieldByID(customFieldID: String): CustomFieldTemplateEntity? {
        return customFieldDao.getCustomFieldById(customFieldID)
    }

    override suspend fun getCustomFieldByWorkspaceID(workspaceID: String): Flow<List<CustomFieldTemplateEntity>> {
        return customFieldDao.getCustomFieldByWorkspaceId(workspaceID)
    }

    override suspend fun getAllCustomFields(): List<CustomFieldTemplateEntity?> {
        return customFieldDao.getAllCustomFields()
    }


}