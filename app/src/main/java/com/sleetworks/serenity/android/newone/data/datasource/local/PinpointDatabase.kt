package com.sleetworks.serenity.android.newone.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sleetworks.serenity.android.newone.data.datasource.local.converters.Converters
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.MainDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.UserDao
import com.sleetworks.serenity.android.newone.data.models.local.datastore.UserPreference
import com.sleetworks.serenity.android.newone.data.models.local.entities.UserEntity

@Database(entities = [UserEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class PinpointDatabase : RoomDatabase() {
//    abstract fun mainDaa(): MainDao
    abstract fun userDao(): UserDao
}