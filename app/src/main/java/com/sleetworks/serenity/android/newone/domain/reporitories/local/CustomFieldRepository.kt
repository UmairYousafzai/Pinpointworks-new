package com.sleetworks.serenity.android.newone.domain.reporitories.local

import com.sleetworks.serenity.android.newone.data.models.local.entities.customField.CustomFieldTemplateEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.customfield.CustomFieldTemplate

interface CustomFieldRepository {

    suspend fun insertCustomFields(customFields: List<CustomFieldTemplate>,workspaceID: String)
    suspend fun getCustomFieldByID(customFieldID: String): CustomFieldTemplateEntity?
    suspend fun getAllCustomFields(): List<CustomFieldTemplateEntity?>
}