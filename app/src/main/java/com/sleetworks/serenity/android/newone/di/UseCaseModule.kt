package com.sleetworks.serenity.android.newone.di

import com.sleetworks.serenity.android.newone.data.imageStore.UserImageStore
import com.sleetworks.serenity.android.newone.domain.reporitories.local.PointRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.ShareRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.ImageRemoteRepository
import com.sleetworks.serenity.android.newone.domain.usecase.SyncImageUseCase
import com.sleetworks.serenity.android.newone.domain.usecase.SyncPointImageUseCase
import com.sleetworks.serenity.android.newone.domain.usecase.SyncPointOriginalImageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideSyncImageUseCase(
        pointRepository: PointRepository,
        shareRepository: ShareRepository,
        imageRepository: ImageRemoteRepository,
        userImageStore: UserImageStore
    ): SyncImageUseCase {
        return SyncImageUseCase(
            pointRepository = pointRepository,
            shareRepository = shareRepository,
            imageRepository = imageRepository,
            userImageStore = userImageStore
        )
    }

  @Provides
    @ViewModelScoped
    fun provideSyncPointImageUseCase(
        imageRepository: ImageRemoteRepository,
        userImageStore: UserImageStore
    ): SyncPointImageUseCase {
        return SyncPointImageUseCase(
            imageRepository = imageRepository,
            userImageStore = userImageStore
        )
    }

  @Provides
    @ViewModelScoped
    fun provideSyncPointOriginalImageUseCase(
        imageRepository: ImageRemoteRepository,
        userImageStore: UserImageStore
    ): SyncPointOriginalImageUseCase {
        return SyncPointOriginalImageUseCase(
            imageRepository = imageRepository,
            userImageStore = userImageStore
        )
    }



}