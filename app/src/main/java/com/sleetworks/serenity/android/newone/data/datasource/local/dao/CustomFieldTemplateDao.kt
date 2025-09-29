package com.sleetworks.serenity.android.newone.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.sleetworks.serenity.android.newone.data.models.local.entities.customField.CustomFieldTemplateEntity

@Dao
interface CustomFieldTemplateDao {

    @Upsert
    suspend fun insertCustomField(customField: CustomFieldTemplateEntity)

    @Upsert
    suspend fun insertCustomFields(customFields: List<CustomFieldTemplateEntity>)

    @Query("SELECT * FROM custom_field_template WHERE id = :customFieldID")
    suspend fun getCustomFieldById(customFieldID: String): CustomFieldTemplateEntity?

    @Query("SELECT * FROM custom_field_template")
    suspend fun getAllCustomFields(): List<CustomFieldTemplateEntity>
}