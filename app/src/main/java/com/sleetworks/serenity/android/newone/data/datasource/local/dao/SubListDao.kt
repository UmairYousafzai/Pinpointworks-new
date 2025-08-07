package com.sleetworks.serenity.android.newone.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sleetworks.serenity.android.newone.data.models.local.entities.SiteEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.SubListItemEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.UserEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.WorkspaceEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.Workspace

@Dao
interface SubListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubList(subList: SubListItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubLists(subLists: List<SubListItemEntity>)

    @Query("SELECT * FROM sublist WHERE id = :sublistID")
    suspend fun getSubListById(sublistID: String): SubListItemEntity?

    @Query("SELECT * FROM sublist")
    suspend fun getAllSubLists(): List<SubListItemEntity>
}