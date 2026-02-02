package com.sleetworks.serenity.android.newone.data.repositories.remote

import android.content.Context
import com.sleetworks.serenity.android.newone.data.datasource.remote.ApiService
import com.sleetworks.serenity.android.newone.data.models.remote.response.ApiResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.notification.Notification
import com.sleetworks.serenity.android.newone.data.network.Resource
import com.sleetworks.serenity.android.newone.data.network.RetrofitProvider
import com.sleetworks.serenity.android.newone.data.network.safeApiCall
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val context: Context,
    private val retrofitProvider: RetrofitProvider
) :
    NotificationRepository {
    override suspend fun getNotifications(offset: Int): Resource<ApiResponse<List<Notification>>> {
        return safeApiCall(context) {
            retrofitProvider.getRetrofit().create(ApiService::class.java)
                .getPushNotifications(offset)
        }
    }

    override suspend fun markNotificationsOpened(): Resource<ApiResponse<Unit>> {
        return safeApiCall(context) {
            retrofitProvider.getRetrofit().create(ApiService::class.java).markNotificationsOpened()
        }
    }

    override suspend fun markNotificationOpened(
        notificationId: String,
        markAsRead: Boolean
    ): Resource<ApiResponse<Unit>> {
        return safeApiCall(context){
            retrofitProvider.getRetrofit().create(ApiService::class.java)
                .markNotificationAsRead(notificationId, markAsRead)
        }
    }

}