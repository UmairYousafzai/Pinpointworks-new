package com.sleetworks.serenity.android.newone.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sleetworks.serenity.android.newone.data.models.local.entities.CustomFieldEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.SiteEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.UserEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.WorkspaceEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.Workspace
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.customfield.CustomField

@Dao
interface CustomFieldDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomField(customField: CustomFieldEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomFields(sites: List<CustomFieldEntity>)

    @Query("SELECT * FROM custom_field WHERE id = :customFieldID")
    suspend fun getCustomFieldById(customFieldID: String): CustomFieldEntity?

    @Query("SELECT * FROM custom_field")
    suspend fun getAllCustomFields(): List<CustomFieldEntity>
}