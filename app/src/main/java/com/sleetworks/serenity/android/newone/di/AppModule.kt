package com.sleetworks.serenity.android.newone.di

import android.content.Context
import androidx.room.Room
import com.sleetworks.serenity.android.newone.data.datasource.local.PinpointDatabase
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.AssigneeDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.CommentDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.CustomFieldTemplateDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.PointAssigneeDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.PointCustomFieldDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.PointDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.PointTagDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.ShareDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.SiteDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.SubListDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.SyncDetailDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.WorkspaceDao
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
    fun provideUserDao(db: PinpointDatabase): AssigneeDao = db.userDao()

    @Provides
    fun provideWorkspaceDao(db: PinpointDatabase): WorkspaceDao = db.workspaceDao()

    @Provides
    fun provideSiteDao(db: PinpointDatabase): SiteDao = db.siteDao()

    @Provides
    fun provideCustomFieldTemplateDao(db: PinpointDatabase): CustomFieldTemplateDao =
        db.customFieldTemplateDao()

    @Provides
    fun providePointCustomFieldDao(db: PinpointDatabase): PointCustomFieldDao =
        db.pointCustomFieldDao()

    @Provides
    fun providePointTagDao(db: PinpointDatabase): PointTagDao = db.tagPointDao()

    @Provides
    fun providePointAssigneeDao(db: PinpointDatabase): PointAssigneeDao = db.assigneePointDao()


    @Provides
    fun provideSublistDao(db: PinpointDatabase): SubListDao = db.sublistDao()

    @Provides
    fun provideShareDao(db: PinpointDatabase): ShareDao = db.shareDao()

    @Provides
    fun providePointDao(db: PinpointDatabase): PointDao = db.pointDao()

    @Provides
    fun provideSynDetailDao(db: PinpointDatabase): SyncDetailDao = db.synDetailDao()

    @Provides
    fun provideCommentDao(db: PinpointDatabase): CommentDao = db.commentDao()

}