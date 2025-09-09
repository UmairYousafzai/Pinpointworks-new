package com.sleetworks.serenity.android.newone.data.repositories.local

import com.sleetworks.serenity.android.newone.data.datasource.local.dao.UserDao
import com.sleetworks.serenity.android.newone.data.models.local.entities.UserEntity
import com.sleetworks.serenity.android.newone.domain.reporitories.local.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor (val userDao: UserDao) : UserRepository {
    override suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    override suspend fun getUserById(userId: String): UserEntity? {
        TODO("Not yet implemented")
    }

    override suspend fun getAllUsers(): List<UserEntity> {
        TODO("Not yet implemented")
    }
}