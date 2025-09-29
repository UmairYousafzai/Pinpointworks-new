package com.sleetworks.serenity.android.newone.data.repositories.local

import com.sleetworks.serenity.android.newone.data.datasource.local.dao.AssigneeDao
import com.sleetworks.serenity.android.newone.data.models.local.entities.AssigneeEntity
import com.sleetworks.serenity.android.newone.domain.reporitories.local.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(val userDao: AssigneeDao) : UserRepository {
    override suspend fun insertUser(user: AssigneeEntity) {
        userDao.insertUser(user)
    }


    override suspend fun insertUsers(users: List<AssigneeEntity>) {
        userDao.insertUsers(users)
    }

    override suspend fun getUserById(userId: String): AssigneeEntity? {
        return userDao.getUserById(userId)
    }

    override suspend fun getAllUsers(): List<AssigneeEntity> {
        return userDao.getAllUsers()
    }
}