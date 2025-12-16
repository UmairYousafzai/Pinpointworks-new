package com.sleetworks.serenity.android.newone.domain.reporitories.local

import com.sleetworks.serenity.android.newone.data.models.local.entities.AssigneeEntity

interface UserRepository {

    suspend fun insertUser(user: AssigneeEntity)
    suspend fun insertUsers(users: List<AssigneeEntity>)
    suspend fun getUserById(userId: String): AssigneeEntity?
    suspend fun getUserByWorkspaceId(workspaceId: String): List<AssigneeEntity>
    suspend fun getAllUsers(): List<AssigneeEntity>
    suspend fun clearDb()

}