package com.sleetworks.serenity.android.newone.di

import android.content.Context
import com.sleetworks.serenity.android.newone.data.datasource.local.PinpointDatabase
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.AssigneeDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.CommentDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.CustomFieldTemplateDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.PointAssigneeDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.PointCustomFieldDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.PointDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.PointTagDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.ReactionDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.ShareDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.SiteDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.SubListDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.SyncDetailDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.WorkspaceDao
import com.sleetworks.serenity.android.newone.data.network.RetrofitProvider
import com.sleetworks.serenity.android.newone.data.repositories.local.CommentLocalRepositoryImpl
import com.sleetworks.serenity.android.newone.data.repositories.local.CustomFieldRepositoryImpl
import com.sleetworks.serenity.android.newone.data.repositories.local.DataStoreRepositoryImpl
import com.sleetworks.serenity.android.newone.data.repositories.local.PointAssigneeRepositoryImpl
import com.sleetworks.serenity.android.newone.data.repositories.local.PointCustomFieldRepositoryImpl
import com.sleetworks.serenity.android.newone.data.repositories.local.PointRepositoryImpl
import com.sleetworks.serenity.android.newone.data.repositories.local.PointTagRepositoryImpl
import com.sleetworks.serenity.android.newone.data.repositories.local.ReactionLocalRepositoryImpl
import com.sleetworks.serenity.android.newone.data.repositories.local.ShareRepositoryImpl
import com.sleetworks.serenity.android.newone.data.repositories.local.SiteRepositoryImpl
import com.sleetworks.serenity.android.newone.data.repositories.local.SyncDetailRepositoryImpl
import com.sleetworks.serenity.android.newone.data.repositories.local.UserRepositoryImpl
import com.sleetworks.serenity.android.newone.data.repositories.local.WorkspaceRepositoryImpl
import com.sleetworks.serenity.android.newone.data.repositories.remote.AuthRemoteRepositoryImpl
import com.sleetworks.serenity.android.newone.data.repositories.remote.CommentRemoteRepositoryImpl
import com.sleetworks.serenity.android.newone.data.repositories.remote.FirebaseRepositoryImpl
import com.sleetworks.serenity.android.newone.data.repositories.remote.ImageRemoteRepositoryImpl
import com.sleetworks.serenity.android.newone.data.repositories.remote.NotificationRepositoryImpl
import com.sleetworks.serenity.android.newone.data.repositories.remote.PointRemoteRepositoryImpl
import com.sleetworks.serenity.android.newone.data.repositories.remote.UserRemoteRepositoryImpl
import com.sleetworks.serenity.android.newone.data.repositories.remote.VideoRemoteRepositoryImpl
import com.sleetworks.serenity.android.newone.data.repositories.remote.WorkspaceRemoteRepositoryImpl
import com.sleetworks.serenity.android.newone.domain.reporitories.local.CommentLocalRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.CustomFieldRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.DataStoreRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.FirebaseRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.PointAssigneeRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.PointCustomFieldRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.PointRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.PointTagRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.ReactionLocalRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.ShareRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.SiteRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.SyncDetailRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.UserRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.WorkspaceRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.AuthRemoteRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.CommentRemoteRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.ImageRemoteRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.NotificationRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.PointRemoteRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.UserRemoteRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.VideoRemoteRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.WorkspaceRemoteRepository
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
    fun providePointRemoteRepository(
        @ApplicationContext context: Context,
        retrofitProvider: RetrofitProvider
    ): PointRemoteRepository {
        return PointRemoteRepositoryImpl(context, retrofitProvider)
    }

    @Provides
    @Singleton
    fun provideUserRemoteRepository(
        @ApplicationContext context: Context,
        retrofitProvider: RetrofitProvider
    ): UserRemoteRepository {
        return UserRemoteRepositoryImpl(context, retrofitProvider)
    }

    @Provides
    @Singleton
    fun provideCommentRemoteRepository(
        @ApplicationContext context: Context,
        retrofitProvider: RetrofitProvider
    ): CommentRemoteRepository {
        return CommentRemoteRepositoryImpl(context, retrofitProvider)
    }

    @Provides
    @Singleton
    fun provideImageRemoteRepository(
        @ApplicationContext context: Context,
        retrofitProvider: RetrofitProvider
    ): ImageRemoteRepository {
        return ImageRemoteRepositoryImpl(context, retrofitProvider)
    }

    @Provides
    @Singleton
    fun provideDataStoreRepository(@ApplicationContext context: Context): DataStoreRepository {
        return DataStoreRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideFirebaseRepository(dataStoreRepository: DataStoreRepository): FirebaseRepository {
        return FirebaseRepositoryImpl(dataStoreRepository)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        assigneeDao: AssigneeDao,
        database: PinpointDatabase
    ): UserRepository {
        return UserRepositoryImpl(assigneeDao, database)
    }

    @Provides
    @Singleton
    fun provideWorkspaceRepository(workspaceDao: WorkspaceDao): WorkspaceRepository {
        return WorkspaceRepositoryImpl(workspaceDao);
    }

    @Provides
    @Singleton
    fun provideSiteRepository(siteDao: SiteDao): SiteRepository {
        return SiteRepositoryImpl(siteDao);
    }

    @Provides
    @Singleton
    fun provideCustomFieldRepository(
        customFieldDao: CustomFieldTemplateDao,
        subListDao: SubListDao
    ): CustomFieldRepository {
        return CustomFieldRepositoryImpl(customFieldDao, subListDao)
    }

    @Provides
    @Singleton
    fun provideShareRepository(shareDao: ShareDao): ShareRepository {
        return ShareRepositoryImpl(shareDao)
    }

    @Provides
    @Singleton
    fun providePointRepository(pointDao: PointDao): PointRepository {
        return PointRepositoryImpl(pointDao)
    }

    @Provides
    @Singleton
    fun provideSyncDetailRepository(syncDetailDao: SyncDetailDao): SyncDetailRepository {
        return SyncDetailRepositoryImpl(syncDetailDao)
    }

    @Provides
    @Singleton
    fun providePointTagRepository(tagPointTagDao: PointTagDao): PointTagRepository {
        return PointTagRepositoryImpl(tagPointTagDao)
    }

    @Provides
    @Singleton
    fun providePointAssigneeRepository(assigneeDao: PointAssigneeDao): PointAssigneeRepository {
        return PointAssigneeRepositoryImpl(assigneeDao)
    }

    @Provides
    @Singleton
    fun providePointCustomFieldRepository(pointCustomFieldDao: PointCustomFieldDao): PointCustomFieldRepository {
        return PointCustomFieldRepositoryImpl(pointCustomFieldDao)
    }

    @Provides
    @Singleton
    fun providePointCommentRepository(commentDao: CommentDao): CommentLocalRepository {
        return CommentLocalRepositoryImpl(commentDao)
    }

    @Provides
    @Singleton
    fun provideReactionRepository(reactionDao: ReactionDao): ReactionLocalRepository {
        return ReactionLocalRepositoryImpl(reactionDao)
    }

    @Provides
    @Singleton
    fun provideVideoRemoteRepository(
        @ApplicationContext context: Context,
        retrofitProvider: RetrofitProvider
    ): VideoRemoteRepository {
        return VideoRemoteRepositoryImpl(context, retrofitProvider)
    }

    @Provides
    @Singleton
    fun provideNotificationRepository(
        @ApplicationContext context: Context,
        retrofitProvider: RetrofitProvider
    ): NotificationRepository {
        return NotificationRepositoryImpl(context, retrofitProvider)
    }

}