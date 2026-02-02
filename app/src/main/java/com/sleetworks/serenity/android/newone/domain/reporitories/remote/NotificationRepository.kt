package com.sleetworks.serenity.android.newone.domain.reporitories.remote

import com.sleetworks.serenity.android.newone.data.models.remote.response.ApiResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.notification.Notification
import com.sleetworks.serenity.android.newone.data.network.Resource

interface NotificationRepository {

    suspend fun getNotifications(offset: Int): Resource<ApiResponse<List<Notification>>>

    suspend fun markNotificationsOpened(): Resource<ApiResponse<Unit>>
    suspend fun markNotificationOpened(notificationId: String, markAsRead: Boolean): Resource<ApiResponse<Unit>>

}