package com.sleetworks.serenity.android.newone.data.models.remote.response.notification

data class Notification(
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