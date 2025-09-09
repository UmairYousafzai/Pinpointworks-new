package com.sleetworks.serenity.android.newone.data.repositories.local

import com.sleetworks.serenity.android.newone.data.datasource.local.dao.CustomFieldDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.SubListDao
import com.sleetworks.serenity.android.newone.data.mappers.toEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.CustomFieldEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.SubListItemEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.customfield.CustomField
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.customfield.SubListItem
import com.sleetworks.serenity.android.newone.domain.reporitories.local.CustomFieldRepository
import javax.inject.Inject

class CustomFieldRepositoryImpl @Inject constructor(
    val customFieldDao: CustomFieldDao,
    val subListDao: SubListDao
) :
    CustomFieldRepository {
    override suspend fun insertCustomField(customField: CustomField, workspaceID: String) {
        customFieldDao.insertCustomField(customField.toEntity(workspaceID));
    }

    override suspend fun insertCustomFields(customFields: List<CustomField>, workspaceID: String) {
        val subListItems = arrayListOf<SubListItemEntity>()

        val customFieldEntities = customFields.map { customField ->
            val entity = customField.toEntity(workspaceID)

            if (customField.subList?.isNotEmpty() == true) {
                val flatSublist = formSublistsList(
                    subslists = customField.subList,
                    parentId = customField.id.toString(),
                    fieldParentId = customField.id, // keep same fieldParentId for all nested
                    workspaceId = workspaceID
                )
                subListItems.addAll(flatSublist)
            }

            entity
        }
        customFieldDao.insertCustomFields(customFieldEntities)


        subListDao.insertSubLists(subListItems)
    }

    override suspend fun getCustomFieldByID(customFieldID: String): CustomFieldEntity? {
        return customFieldDao.getCustomFieldById(customFieldID)
    }

    override suspend fun getAllCustomFields(): List<CustomFieldEntity?> {
        return customFieldDao.getAllCustomFields()
    }

    private fun formSublistsList(
        subslists: List<SubListItem>,
        parentId: String,
        fieldParentId: Long,
        workspaceId: String
    ): MutableList<SubListItemEntity> {
        val allSublist: MutableList<SubListItemEntity> = arrayListOf()

        for (subListItem in subslists) {

            allSublist.add(subListItem.toEntity(parentId, fieldParentId, workspaceId))
            if (subListItem.subList?.isNotEmpty() == true) {
                allSublist.addAll(
                    formSublistsList(
                        subListItem.subList,
                        subListItem.id.toString(),
                        fieldParentId,
                        workspaceId
                    )
                )
            }
        }
        return allSublist
    }
}