package com.sleetworks.serenity.android.newone.di

import android.content.Context
import com.sleetworks.serenity.android.newone.data.imageStore.UserImageStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object StoreModule {

    @Provides
     fun provideUserImageStore(@ApplicationContext context: Context): UserImageStore {
       return UserImageStore(context)
    }


}
