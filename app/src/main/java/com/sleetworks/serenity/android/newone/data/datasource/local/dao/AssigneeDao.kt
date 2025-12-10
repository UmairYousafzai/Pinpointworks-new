package com.sleetworks.serenity.android.newone.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.sleetworks.serenity.android.newone.data.models.local.entities.AssigneeEntity

@Dao
interface AssigneeDao {

    @Upsert
    suspend fun insertUser(user: AssigneeEntity)

    @Upsert
    suspend fun insertUsers(user: List<AssigneeEntity>)

    @Query("SELECT * FROM assignee WHERE id = :userId")
    suspend fun getUserById(userId: String): AssigneeEntity?

    @Query("SELECT * FROM assignee WHERE workspace_id = :workspaceID")
    suspend fun getUserByWorkspaceId(workspaceID: String): List<AssigneeEntity>

    @Query("SELECT * FROM assignee")
    suspend fun getAllUsers(): List<AssigneeEntity>
}