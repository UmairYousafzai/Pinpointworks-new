package com.sleetworks.serenity.android.newone.domain.reporitories

import com.sleetworks.serenity.android.newone.data.models.local.entities.CustomFieldEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.customfield.CustomField

interface CustomFieldRepository {

    suspend fun insertCustomField(customField: CustomField,workspaceID: String)
    suspend fun insertCustomFields(customFields: List<CustomField>,workspaceID: String)
    suspend fun getCustomFieldByID(customFieldID: String): CustomFieldEntity?
    suspend fun getAllCustomFields(): List<CustomFieldEntity?>
}