package com.sleetworks.serenity.android.newone.domain.reporitories.local

import com.sleetworks.serenity.android.newone.data.models.local.entities.UserEntity

interface UserRepository {

    suspend fun insertUser(user: UserEntity)
    suspend fun getUserById(userId: String): UserEntity?
    suspend fun getAllUsers(): List<UserEntity>

}