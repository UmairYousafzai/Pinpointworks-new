package com.sleetworks.serenity.android.newone.domain.reporitories.local

import com.sleetworks.serenity.android.newone.data.models.local.entities.customField.CustomFieldTemplateEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.customfield.CustomFieldTemplate
import kotlinx.coroutines.flow.Flow

interface CustomFieldRepository {

    suspend fun insertCustomFields(customFields: List<CustomFieldTemplate>,workspaceID: String)
    suspend fun getCustomFieldByID(customFieldID: String): CustomFieldTemplateEntity?
    suspend fun getCustomFieldByWorkspaceID(workspaceID: String): Flow<List<CustomFieldTemplateEntity>>
    suspend fun getAllCustomFields(): List<CustomFieldTemplateEntity?>
}