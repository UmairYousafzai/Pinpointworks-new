package com.sleetworks.serenity.android.newone.domain.models

import com.sleetworks.serenity.android.newone.data.models.remote.response.notification.ChangeBody
import com.sleetworks.serenity.android.newone.data.models.remote.response.notification.RefObject

data class NotificationDomain(
    val changeBody: ChangeBody,
    val creatorUserId: String,
    val creatorUserName: String,
    val id: String,
    val markedAsRead: Boolean,
    val notificationReason: String,
    val pushNotificationType: String,
    val refObject: RefObject,
    val siteName: String,
    val targetUserId: String,
    val timestampEpochMillis: Long,
    val workspaceId: String
)