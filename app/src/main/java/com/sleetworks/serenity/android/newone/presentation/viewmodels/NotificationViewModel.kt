package com.sleetworks.serenity.android.newone.presentation.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sleetworks.serenity.android.newone.data.imageStore.UserImageStore
import com.sleetworks.serenity.android.newone.data.models.remote.response.ApiResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.notification.Notification
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Point
import com.sleetworks.serenity.android.newone.data.network.NetworkUtil
import com.sleetworks.serenity.android.newone.data.network.Resource
import com.sleetworks.serenity.android.newone.domain.mapper.toDomain
import com.sleetworks.serenity.android.newone.domain.models.AssigneeDomain
import com.sleetworks.serenity.android.newone.domain.models.NotificationDomain
import com.sleetworks.serenity.android.newone.domain.reporitories.local.PointRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.UserRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.WorkspaceRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.NotificationRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.PointRemoteRepository
import com.sleetworks.serenity.android.newone.domain.usecase.DownloadUserAvatarUseCase
import com.sleetworks.serenity.android.newone.presentation.common.UIEvent
import com.sleetworks.serenity.android.newone.presentation.common.toUiModel
import com.sleetworks.serenity.android.newone.presentation.model.PushNotificationFieldType
import com.sleetworks.serenity.android.newone.presentation.navigation.Screen
import com.sleetworks.serenity.android.newone.presentation.ui.screens.notification.NotificationItem
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.util.Calendar
import java.util.GregorianCalendar
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val notificationRepository: NotificationRepository,
    private val pointRemoteRepository: PointRemoteRepository,
    private val pointLocalRepository: PointRepository,
    private val workspaceRepository: WorkspaceRepository,
    private val userImageStore: UserImageStore,
    private val userRepository: UserRepository,
    private val downloadUserAvatarUseCase: DownloadUserAvatarUseCase,

    ) : ViewModel() {
    val TAG = "NotificationViewModel"

    private val _uiEvent = MutableSharedFlow<UIEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _error: MutableStateFlow<String> = MutableStateFlow("")
    val error: StateFlow<String>
        get() = _error

    private val _mainLoader: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val mainLoader: StateFlow<Boolean>
        get() = _mainLoader

    private val _avatarUpdateSignal = MutableSharedFlow<String>()
    val avatarUpdateSignal = _avatarUpdateSignal.asSharedFlow()


    private val _notifications: MutableStateFlow<List<NotificationItem>> =
        MutableStateFlow(listOf())
    val notifications: StateFlow<List<NotificationItem>>
        get() = _notifications

    private val _workspaceUser: StateFlow<Map<String, AssigneeDomain>> =
        userRepository.getAllUsersFlow()
            .map {
                val list = it.map { item -> item.toDomain() }
                list.associateBy { item -> item.id }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = mapOf()
            )
    val workspaceUser
        get() = _workspaceUser

    private val _workspaces = workspaceRepository.getAllWorkspacesFlow().map {
        val workspacesUi = it.map { workspace -> workspace?.toUiModel(false) }
        workspacesUi.associateBy { item -> item?.id }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = mapOf()
    )
    val workspaces
        get() = _workspaces


    private var currentOffset = 0
    private var hasMoreNotifications = true
    private var isLoadingMore = false

    private val todayDate: Long
    private val yesterdayDate: Long

    init {
        val calendar = GregorianCalendar()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        todayDate = calendar.timeInMillis

        calendar.add(Calendar.DAY_OF_MONTH, -1)
        yesterdayDate = calendar.timeInMillis
    }

    fun setError(message: String = "") {

        _error.value = message
    }

    fun getUserAvatar(imageID: String): File? {
        val file = userImageStore.getAvatarFile(imageID)
        if (file == null) {
            downloadUserAvatar(imageID)
        }
        return file
    }

    fun downloadUserAvatar(imageId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                downloadUserAvatarUseCase(imageId)
                _avatarUpdateSignal.emit(imageId)
            } catch (e: Exception) {
                Log.e(TAG, "downloadUserAvatar: ", e)
            }

        }
    }

    fun loadFirstTimeNotifications() {
        viewModelScope.launch {
            markedNotificationsAsRead()
            workspaces.first { it.isNotEmpty() }

            loadMoreNotifications()

        }

    }

    fun loadMoreNotifications() {
        if (!NetworkUtil.isInternetAvailable(context)) return
        if (isLoadingMore || !hasMoreNotifications) return

        viewModelScope.launch {
            _mainLoader.value = true
            isLoadingMore = true

            currentOffset = _notifications.value.size

            try {
                val newNotifications = fetchNotifications(currentOffset)
                val notificationItems = newNotifications.map { notification ->
                    NotificationItem(
                        notification = notification.toDomain(),
                        dateLabel = getDateLabel(notification.timestampEpochMillis)
                    )
                }

                _notifications.value = (_notifications.value + notificationItems)
                hasMoreNotifications = newNotifications.isNotEmpty()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoadingMore = false
                _mainLoader.value = false
            }
        }
    }

    suspend fun fetchNotifications(offset: Int): List<Notification> {

        var notifications = emptyList<Notification>()
        val result = notificationRepository.getNotifications(offset)

        when (result) {
            is Resource.Error -> {
                notifications = emptyList()
                Log.e(TAG, "fetchNotifications: ", result.apiException)
            }

            Resource.Loading -> {

            }

            is Resource.Success<ApiResponse<List<Notification>>> -> {
                notifications = result.data.entity ?: emptyList()

            }
        }

        return notifications

    }

    private fun getDateLabel(timestamp: Long): String? {
        return when {
            timestamp >= todayDate -> "Today"
            timestamp >= yesterdayDate -> "Yesterday"
            else -> "Older"
        }
    }

    fun markedNotificationsAsRead() {
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtil.isInternetAvailable(context)) {
                try {
                    _mainLoader.value = true

                    val result = notificationRepository.markNotificationsOpened()
                    when (result) {
                        is Resource.Error -> {
                            Log.e(TAG, "markedNotificationAsRead: ", result.apiException)
                        }

                        Resource.Loading -> {}
                        is Resource.Success<*> -> {

                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "markedNotificationAsRead: ", e)
                } finally {
                    _mainLoader.value = false
                }
            }


        }
    }

    fun openNotificationPoint(notification: NotificationDomain) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val notificationType =
                    PushNotificationFieldType.fromString(notification.pushNotificationType)

                val pointId =
                    if (notificationType == PushNotificationFieldType.REMINDER_EDITED
                        || notificationType == PushNotificationFieldType.REMINDER_CREATED
                        || notificationType == PushNotificationFieldType.REMINDER_DELETED
                    )
                        notification.reminderDetails?.pointId ?: "-1"
                    else notification.refObject.id

                _mainLoader.value = true
                val isMarked = markNotificationAsRead(notificationId = notification.id)
                if (isMarked && pointId != "-1") {
                    getPointDetail(pointId, notification.id)
                } else {
                    _error.value = "Failed to mark notification as read"
                }

            } catch (e: Exception) {
                Log.e(TAG, "openNotificationPoint: ", e)
            } finally {
                _mainLoader.value = false
            }

        }

    }

    suspend fun markNotificationAsRead(notificationId: String): Boolean {
        var isSuccessful = false

        val result =
            notificationRepository.markNotificationOpened(notificationId = notificationId, true)

        when (result) {
            is Resource.Error -> {
                Log.e(TAG, "markNotificationAsRead: ", result.apiException)
                _error.value = result.apiException.message ?: "unknown error"
                isSuccessful = false
            }

            Resource.Loading -> {}
            is Resource.Success<*> -> {
                isSuccessful = true

            }
        }
        return isSuccessful
    }

    suspend fun getPointDetail(pointId: String, workspaceId: String) {
        val result = pointRemoteRepository.getPointDetail(pointId, workspaceId)
        when (result) {
            is Resource.Error -> {
                Log.e(TAG, "getPointDetail: ", result.apiException)
                _error.value = result.apiException.message ?: "unknown error"

            }

            Resource.Loading -> {}

            is Resource.Success<ApiResponse<Point>> -> {
                val point = result.data.entity
                if (point != null) {
                    pointLocalRepository.insertPoint(point)
                    _uiEvent.emit(UIEvent.Navigate("${Screen.DefectDetailScreen.route}/$pointId"))
                } else {
                    _error.value = "Point Fetch Failed"
                }
            }
        }
    }
}

