package com.sleetworks.serenity.android.newone.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sleetworks.serenity.android.newone.data.datasource.local.converters.PointConverters
import com.sleetworks.serenity.android.newone.data.datasource.local.converters.ShareConverters
import com.sleetworks.serenity.android.newone.data.datasource.local.converters.SiteConverters
import com.sleetworks.serenity.android.newone.data.datasource.local.converters.UserConverters
import com.sleetworks.serenity.android.newone.data.datasource.local.converters.WorkspaceConverters
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.CustomFieldDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.PointDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.ShareDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.SiteDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.SubListDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.SyncDetailDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.UserDao
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.WorkspaceDao
import com.sleetworks.serenity.android.newone.data.models.local.entities.CustomFieldEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.PointEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.ShareEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.SiteEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.SubListItemEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.SyncDetailEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.UserEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.WorkspaceEntity

@Database(
    entities = [
        UserEntity::class,
        WorkspaceEntity::class,
        SiteEntity::class,
        CustomFieldEntity::class,
        SubListItemEntity::class,
        ShareEntity::class,
        PointEntity::class,
        SyncDetailEntity::class
    ],
    version = 1
)
@TypeConverters(
    UserConverters::class,
    WorkspaceConverters::class,
    SiteConverters::class,
    PointConverters::class,
    ShareConverters::class
)
abstract class PinpointDatabase : RoomDatabase() {
    //    abstract fun mainDaa(): MainDao
    abstract fun userDao(): UserDao
    abstract fun workspaceDao(): WorkspaceDao
    abstract fun siteDao(): SiteDao
    abstract fun customFieldDao(): CustomFieldDao
    abstract fun sublistDao(): SubListDao
    abstract fun shareDao(): ShareDao
    abstract fun pointDao(): PointDao
    abstract fun synDetailDao(): SyncDetailDao
}