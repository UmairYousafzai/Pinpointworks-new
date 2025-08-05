package com.sleetworks.serenity.android.newone.di

import android.content.Context
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.UserDao
import com.sleetworks.serenity.android.newone.data.network.RetrofitProvider
import com.sleetworks.serenity.android.newone.data.repositories.remote.AuthRemoteRepositoryImpl
import com.sleetworks.serenity.android.newone.data.repositories.local.DataStoreRepositoryImpl
import com.sleetworks.serenity.android.newone.data.repositories.local.UserRepositoryImpl
import com.sleetworks.serenity.android.newone.data.repositories.remote.FirebaseRepositoryImpl
import com.sleetworks.serenity.android.newone.data.repositories.remote.WorkspaceRemoteRepositoryImpl
import com.sleetworks.serenity.android.newone.domain.reporitories.AuthRemoteRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.DataStoreRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.FirebaseRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.UserRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.WorkspaceRemoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {


    @Provides
    @Singleton
    fun provideAuthRepository(
        @ApplicationContext context: Context,
        retrofitProvider: RetrofitProvider
    ): AuthRemoteRepository {
        return AuthRemoteRepositoryImpl(context, retrofitProvider)
    }

    @Provides
    @Singleton
    fun provideWorkspaceRemoteRepository(
        @ApplicationContext context: Context,
        retrofitProvider: RetrofitProvider
    ): WorkspaceRemoteRepository {
        return WorkspaceRemoteRepositoryImpl(context, retrofitProvider)
    }

    @Provides
    @Singleton
    fun provideDataStoreRepository(@ApplicationContext context: Context): DataStoreRepository {
        return DataStoreRepositoryImpl(context);
    }

    @Provides
    @Singleton
    fun provideFirebaseRepository(dataStoreRepository: DataStoreRepository): FirebaseRepository {
        return FirebaseRepositoryImpl(dataStoreRepository);
    }

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepositoryImpl(userDao);
    }

}