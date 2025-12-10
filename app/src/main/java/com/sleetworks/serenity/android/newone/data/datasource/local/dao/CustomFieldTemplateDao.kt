package com.sleetworks.serenity.android.newone.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.sleetworks.serenity.android.newone.data.models.local.entities.customField.CustomFieldTemplateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomFieldTemplateDao {

    @Upsert
    suspend fun insertCustomField(customField: CustomFieldTemplateEntity)

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomFields(customFields: List<CustomFieldTemplateEntity>)

    @Query("SELECT * FROM custom_field_template WHERE id = :customFieldID")
    suspend fun getCustomFieldById(customFieldID: String): CustomFieldTemplateEntity?

    @Query("SELECT * FROM custom_field_template WHERE workspaceID = :workspaceId ")
    fun getCustomFieldByWorkspaceId(workspaceId: String): Flow<List<CustomFieldTemplateEntity>>

    @Query("SELECT * FROM custom_field_template")
    suspend fun getAllCustomFields(): List<CustomFieldTemplateEntity>
}