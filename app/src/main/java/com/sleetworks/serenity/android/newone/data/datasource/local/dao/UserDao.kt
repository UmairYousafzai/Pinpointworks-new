package com.sleetworks.serenity.android.newone.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.sleetworks.serenity.android.newone.data.models.local.entities.UserEntity

@Dao
interface UserDao {

    @Upsert
    suspend fun insertUser(user: UserEntity)

    @Upsert
    suspend fun insertUsers(user: List<UserEntity>)

    @Query("SELECT * FROM user WHERE id = :userId")
    suspend fun getUserById(userId: String): UserEntity?

    @Query("SELECT * FROM user")
    suspend fun getAllUsers(): List<UserEntity>
}