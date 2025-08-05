package com.sleetworks.serenity.android.newone.data.mappers

import com.sleetworks.serenity.android.newone.data.models.local.entities.UserEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.User

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        activeWorkspaceRef = this.activeWorkspaceRef,
        notificationStatus = this.notificationStatus,
        preferenceDocRef = this.preferenceDocRef,
        revision = this.revision ?: "",
        header = this.header,
        tags = this.tags ?: arrayListOf(),
        email = this.email,
        enabled = this.enabled,
        enabled2fa = this.enabled2fa,
        images = this.images,
        lastActivityEpochMillis = this.lastActivityEpochMillis,
        name = this.name,
        setup2faAfter = this.setup2faAfter,
        timeZoneId = this.timeZoneId,
        type = this.type,
        userType = this.userType,
        verified = this.verified,
        passwordHash = this.passwordHash
    )
}

fun UserEntity.toDomain(): User {
    return User(
        activeWorkspaceRef = this.activeWorkspaceRef,
        email = this.email,
        enabled = this.enabled,
        enabled2fa = this.enabled2fa,
        images = this.images,
        lastActivityEpochMillis = this.lastActivityEpochMillis,
        name = this.name,
        notificationStatus = this.notificationStatus,
        preferenceDocRef = this.preferenceDocRef,
        setup2faAfter = this.setup2faAfter,
        timeZoneId = this.timeZoneId,
        type = this.type,
        userType = this.userType,
        verified = this.verified,
        passwordHash = this.passwordHash,
        id = this.id

    ).apply {
        this.revision = this@toDomain.revision
        this.header = this@toDomain.header
        this.tags?.addAll(this@toDomain.tags)
    }
}