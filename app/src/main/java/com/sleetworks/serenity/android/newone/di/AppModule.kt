package com.sleetworks.serenity.android.newone.di

import android.content.Context
import androidx.room.Room
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.MainDao
import com.sleetworks.serenity.android.newone.data.datasource.local.PinpointDatabase
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.UserDao
import com.sleetworks.serenity.android.newone.utils.CONSTANTS.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PinpointDatabase {
        return Room.databaseBuilder(context, PinpointDatabase::class.java, DB_NAME).build()
    }

    @Provides
    fun provideUserDao(db: PinpointDatabase): UserDao = db.userDao()
//
//    @Provides
//    fun provideMainDao(db: PinpointDatabase): MainDao = db.mainDaa()
}