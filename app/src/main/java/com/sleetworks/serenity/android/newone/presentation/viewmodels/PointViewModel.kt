package com.sleetworks.serenity.android.newone.presentation.viewmodels

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sleetworks.serenity.android.newone.data.imageStore.UserImageStore
import com.sleetworks.serenity.android.newone.data.mappers.toEntity
import com.sleetworks.serenity.android.newone.data.models.local.datastore.UserPreference
import com.sleetworks.serenity.android.newone.data.models.local.entities.SyncDetailEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.SyncType
import com.sleetworks.serenity.android.newone.data.models.remote.response.ApiResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.User
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.PointResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.Workspace
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.Share
import com.sleetworks.serenity.android.newone.data.network.ApiException
import com.sleetworks.serenity.android.newone.data.network.Resource
import com.sleetworks.serenity.android.newone.domain.mapper.toDomain
import com.sleetworks.serenity.android.newone.domain.mapper.toDomainModel
import com.sleetworks.serenity.android.newone.domain.models.point.PointDomain
import com.sleetworks.serenity.android.newone.domain.models.share.ShareDomainModel
import com.sleetworks.serenity.android.newone.domain.reporitories.local.CustomFieldRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.DataStoreRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.PointRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.ShareRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.SiteRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.SyncDetailRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.UserRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.WorkspaceRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.ImageRemoteRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.PointRemoteRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.UserRemoteRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.WorkspaceRemoteRepository
import com.sleetworks.serenity.android.newone.domain.usecase.DownloadUsersAvatarUseCase
import com.sleetworks.serenity.android.newone.domain.usecase.SyncImageUseCase
import com.sleetworks.serenity.android.newone.presentation.common.UIEvent
import com.sleetworks.serenity.android.newone.presentation.common.toUiModel
import com.sleetworks.serenity.android.newone.presentation.model.UserUiModel
import com.sleetworks.serenity.android.newone.presentation.model.WorkspaceUiModel
import com.sleetworks.serenity.android.newone.utils.CONSTANTS.PERMISSION_LIMIT
import com.sleetworks.serenity.android.newone.utils.CONSTANTS.PERMISSION_NORMAL
import com.sleetworks.serenity.android.newone.utils.SITE_ID
import com.sleetworks.serenity.android.newone.utils.WORKSPACE_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class PointViewModel @Inject constructor(
    private val syncDetailRepository: SyncDetailRepository,
    private val pointRepository: PointRepository,
    private val pointRemoteRepository: PointRemoteRepository,
    private val workspaceRemoteRepository: WorkspaceRemoteRepository,
    private val workspaceRepository: WorkspaceRepository,
    private val siteRepository: SiteRepository,
    private val customFieldRepository: CustomFieldRepository,
    private val shareRepository: ShareRepository,
    private val userRemoteRepository: UserRemoteRepository,
    private val userRepository: UserRepository,
    private val userImageStore: UserImageStore,
    private val imageRepository: ImageRemoteRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val syncImagesUseCase: SyncImageUseCase,
    private val downloadUsersAvatarUseCase: DownloadUsersAvatarUseCase,
    val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val TAG = "PointViewModel"
    private var syncImagesJob: Job? = null
    private var usersAvatarSync: Job? = null
    private val _uiEvent = MutableSharedFlow<UIEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    private val _loader: MutableStateFlow<Pair<String, Boolean>> = MutableStateFlow(Pair("", false))
    val loader: StateFlow<Pair<String, Boolean>>
        get() = _loader

    private val _error: MutableStateFlow<String> = MutableStateFlow("")
    val error: StateFlow<String>
        get() = _error

    private val _message: MutableStateFlow<String> = MutableStateFlow("")
    val message: StateFlow<String>
        get() = _message

    private val _workspaceID = dataStoreRepository.workspaceIDFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
    val workspaceID
        get() = _workspaceID

    private val _workspace = _workspaceID
        .filter { it?.isEmpty() == false }
        .flatMapLatest {
            workspaceRepository.getCurrentWorkspaceFlow(it ?: "")
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    val workspace
        get() = _workspace

    private val _share = _workspace
        .filter { it?.id?.isEmpty() == false }
        .flatMapLatest {
            shareRepository.getShareByWorkspaceIDFlow(it?.id.toString())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    val share
        get() = _share

    private val _points: StateFlow<List<PointDomain>> =
        _workspaceID.filter { it?.isEmpty() == false }
            .flatMapLatest { workspaceID ->
                pointRepository.getPointByWorkspaceIDFlow(
                    workspaceID ?: ""
                )
            }
            .map {
                it.map { point -> point.toDomain() }
            }
            .map {

                filterDefectsByPermission(it)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val points: StateFlow<List<PointDomain>>
        get() = _points

    private val _workspaces = workspaceRepository.getAllWorkspacesFlow().map { it ->
        it.map {
            it?.toUiModel(it.id == _workspaceID.value)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val workspaces: StateFlow<List<WorkspaceUiModel?>>
        get() = _workspaces


    private val _user = dataStoreRepository.user.filterNotNull().map { user ->
        UserUiModel(
            user.id,
            user.username,
            user.isLogin,
            user.email,
            user.password,
            user.imageID
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserUiModel()
    )

    val user: StateFlow<UserUiModel>
        get() = _user

    private val _imageFile: MutableStateFlow<File?> = MutableStateFlow(null)
    val imageFile
        get() = _imageFile

    private val _syncTime: StateFlow<String> =
        _workspaceID.filter { it?.isEmpty() == false }
            .flatMapLatest { workspaceID ->
                syncDetailRepository.getSyncDetailByWorkspaceIDFlow(
                    workspaceID ?: "",
                    SyncType.POINT.name
                )
            }
            .map { syncDetail ->
                if (syncDetail != null && syncDetail?.time != 0L) {
                    val sdf = SimpleDateFormat("MMM/dd/yyyy, HH:mm", Locale.getDefault())
                    sdf.format(Date(syncDetail.time))
                } else ""
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = ""
            )
    val syncTime
        get() = _syncTime

    fun getUserImage(imageID: String) {
        viewModelScope.launch(Dispatchers.IO) {

            _imageFile.emit(userImageStore.getAvatarFile(imageID))
        }
    }

    init {

        Handler(Looper.getMainLooper()).postDelayed({
            val shouldSyncPoint = savedStateHandle.get<Boolean>("shouldSyncPoint") ?: false
            if (shouldSyncPoint) {
                getPoints()
            } else {
                syncImages()
                downloadUsersAvatar()
            }
        }, 500)

    }

    fun cancelImageSync() {
        syncImagesJob?.cancel()
        usersAvatarSync?.cancel()
    }

    fun getPoints() {
        viewModelScope.launch(Dispatchers.IO) {

            _loader.emit(Pair("Syncing points.....", true))
            val pointSyncTime = getPointSyncTime()

            val result =
                pointRemoteRepository.getPoints(pointSyncTime, _workspaceID.value ?: "")

            when (result) {
                is Resource.Success<ApiResponse<PointResponse>> -> {

                    result.data.entity?.let {
                        pointRepository.deletePointByID(it.removedPointsIds)
                        pointRepository.insertPoints(it.points)
                    }
                    updatePointSyncTime(System.currentTimeMillis())
                    Log.e("Site Sync", "Sync complete")
                    downloadUsersAvatar()

                    syncImages()

                }

                is Resource.Error -> {
                    var message = ""
                    message = if (result.apiException is ApiException.NetworkException) {
                        result.apiException.message ?: "Unexpected error"
                    } else {
                        "Sync failed,unexpected error"
                    }

                    _error.emit(message)
                    Log.e(TAG, "getPoints: ", result.apiException)
                }

                is Resource.Loading -> {

                }
            }

            _loader.emit(Pair("", false))

        }
    }


    fun syncWorkspacesData() {


        viewModelScope.launch(Dispatchers.IO) {
            Log.e("Site Sync", "Sync Start")

            try {
                _loader.emit(Pair("Syncing Workspace.....", true))
                Log.d(TAG, "syncData: start")

                val userDeferred = async {
                    userRemoteRepository.getLoginUser()
                }

                val sitesDeferred =
                    async {
                        workspaceRemoteRepository.getAllSites()
                    }


                val workspacesDeferred = async {
                    workspaceRemoteRepository.getAllWorkspaces()
                }

                val sharesDeferred = async {
                    workspaceRemoteRepository.getAllShares()
                }

                val workspaceUsersDeferred = async {
                    workspaceRemoteRepository.getWorkSpaceUsers(_workspaceID.value.toString())
                }

                _loader.emit(Pair("Fetching user data.....", true))
                val user = userDeferred.await()
                _loader.emit(Pair("Fetching workspaces.....", true))
                val workspaces = workspacesDeferred.await()
                _loader.emit(Pair("Fetching sites.....", true))
                val sites = sitesDeferred.await()
                _loader.emit(Pair("Fetching shares.....", true))
                val shares = sharesDeferred.await()
                _loader.emit(Pair("Fetching workspace users.....", true))
                val users = workspaceUsersDeferred.await()

                delay(4000)
                if (user is Resource.Success &&
                    sites is Resource.Success &&
                    workspaces is Resource.Success &&
                    shares is Resource.Success &&
                    users is Resource.Success
                ) {

                    _loader.emit(Pair("Storing user data.....", true))
                    user.data.entity?.let { storeUserData(it) }

                    _loader.emit(Pair("Storing workspaces.....", true))
                    val workspaceList = arrayListOf<Workspace>()
                    workspaces.data.entity?.let {
                        it.forEachIndexed { index, workspaceResponse ->
                            workspaceList.addAll(workspaceResponse.workspaces)
                        }
                    }
                    workspaceRepository.insertWorkspaces(workspaceList)
                    validateCurrentWorkspaceAccess(workspaceList)

                    _loader.emit(Pair("Storing sites.....", true))
                    sites.data.entity?.let {
                        siteRepository.insertSites(it)

                    }

                    _loader.emit(Pair("Storing custom fields.....", true))
                    sites.data.entity?.let {
                        it.forEachIndexed { index, site ->
                            site.customFields.let { customFields ->
                                customFieldRepository.insertCustomFields(
                                    customFields,
                                    site.workspaceRef.id
                                )
                            }
                        }
                    }

                    _loader.emit(Pair("Storing shares.....", true))
                    shares.data.entity?.let {
                        isCurrentPermissionChanged(it)
                        shareRepository.insertShares(it, _workspaceID.value ?: "")
                        validatePermission(it)
                    }

                    users.data.entity?.let {
                        userRepository.insertUsers(it.map { item ->
                            item.toEntity(
                                workspaceID.value ?: ""
                            )
                        })
                    }
                    updateSyncTime()

                    getPoints()

                } else if (user is Resource.Error || sites is Resource.Error || workspaces is Resource.Error || shares is Resource.Error) {
                    _loader.emit(Pair("", false))

                    val error = when {
                        user is Resource.Error -> user.apiException
                        users is Resource.Error -> users.apiException
                        sites is Resource.Error -> sites.apiException
                        workspaces is Resource.Error -> workspaces.apiException
                        shares is Resource.Error -> shares.apiException
                        else -> ApiException.UnknownException("Unexpected error")

                    }

                    _message.emit("Sync Failed")

                    if (error is ApiException.NetworkException) {
                        _error.emit(error.message ?: "Unexpected error")
                    } else if (error is ApiException.UnauthorizedException) {

                    } else {
                        _error.emit("Sync failed,Unexpected error")
                    }
                    Log.e(TAG, "syncData: ", error)
                }

            } catch (e: Exception) {
                _message.emit("Sync Failed")
                _error.emit("Sync Failed, please try again.")
                _loader.emit(Pair("", false))
                Log.e(TAG, "syncData: unexpected error", e)
            }


        }
    }

    suspend fun isCurrentPermissionChanged(shares: List<Share>) {
//
//        val share =
//            shares.find { it.id == _share.value?.id && it.advancedAccessLevels == _share.value?.advancedAccessLevels }
//        if (share != null) {
//            updatePointSyncTime(0)
//        }

    }

    suspend fun updatePointSyncTime(time: Long) {
        _workspaceID.value?.let { it ->
            syncDetailRepository.insertSyncDetail(
                SyncDetailEntity(
                    workspaceId = it,
                    dataType = SyncType.POINT.name,
                    time = time
                )
            )
        }
    }

    suspend fun updateSyncTime() {
        _workspaceID.value?.let { it ->
            syncDetailRepository.insertSyncDetail(
                SyncDetailEntity(
                    workspaceId = it,
                    dataType = SyncType.WORKSPACE.name,
                    time = System.currentTimeMillis()
                )
            )
        }

    }

    suspend fun storeUserData(user: User) {
        dataStoreRepository.saveUserInfo(
            UserPreference(
                user.id,
                user.name,
                true,
                user.email,
                user.images[0].id
            )
        )
        dataStoreRepository.saveUser(user)

    }

    suspend fun getPointSyncTime(): String {
        _workspaceID.value?.let { workspaceID ->

            try {
                val syncDetail = syncDetailRepository.getSyncDetailByWorkspaceID(
                    workspaceID,
                    SyncType.POINT.name
                )
                return if (syncDetail == null || syncDetail.time == 0L) {
                    "1111111111111"
                } else {
                    syncDetail.time.toString()
                }
            } catch (e: Exception) {
                Log.e(TAG, "getPointSyncTime: ", e)
                return "1111111111111"
            }
        }
        return "1111111111111"
    }


    suspend fun storeWorkspaceIDAndSiteID(workspaceID: String, siteID: String) {

        if (workspaceID.isNotEmpty()) {

            dataStoreRepository.putString(WORKSPACE_ID, workspaceID)
            dataStoreRepository.putString(SITE_ID, siteID)
        }

    }

    suspend fun validateCurrentWorkspaceAccess(workspaces: List<Workspace>) {
        if (_workspaceID.value?.isNotEmpty() == true) {
            val workspaceExists = workspaces.any { it.id == _workspaceID.value }
            if (!workspaceExists) {
                storeWorkspaceIDAndSiteID(
                    workspaces.firstOrNull()?.id ?: return,
                    workspaces.firstOrNull()?.siteRef?.id ?: return
                )
            }
        }
    }

    suspend fun validatePermission(shares: List<Share>) {
        val newShares = shares.map { it.toDomainModel() }
        newShares.forEach { newShare ->
            val workspaceID = newShare.targetRef.id
            val currentShare = shareRepository.getShareByWorkspaceID(workspaceID)
            currentShare?.let { currentShare ->
                if (hasPermissionChanged(currentShare, newShare)) {
                    pointRepository.deletePointByWorkspaceID(workspaceID)
                }
            }
        }
    }

    fun hasPermissionChanged(
        currentShare: ShareDomainModel,
        newShare: ShareDomainModel
    ): Boolean {
        return currentShare.canReadTags() != newShare.canReadTags() ||
                currentShare.canEditTags() != newShare.canEditTags() ||
                currentShare.canReadComments() != newShare.canReadComments() ||
                currentShare.canEditComments() != newShare.canEditComments() ||
                currentShare.defectTags != newShare.defectTags
    }


    private fun filterDefectsByPermission(
        list: List<PointDomain>,
    ): List<PointDomain> {
        val defectTags = share.value?.defectTags
        if (_share.value?.shareOption !in listOf(PERMISSION_LIMIT, PERMISSION_NORMAL)) {
            return list
        }

        share.value ?: return list
        if (defectTags == null || share.value?.tagLimited == false || (share.value?.canReadTags() == false && share.value?.canEditTags() == false)) {
            return list
        }

        val filteredList = when {
            defectTags.isEmpty() -> list.filter { it?.tags?.isEmpty() == true }
            else -> list.filter { defect -> defectTags.any { defect?.tags?.contains(it) == true } }
        }
        return filteredList
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
//            _loader.emit(Pair("Logging out.....", true))
            try {
                userRepository.clearDb()
                dataStoreRepository.clearData()
                _uiEvent.emit(UIEvent.Logout)
            } catch (e: Exception) {

            }
        }

    }

    fun syncImages() {
        syncImagesJob = viewModelScope.launch(Dispatchers.IO) {
            Log.e("Images Sync", "Sync Start")

            try {
                val workspaceId = workspaceID.value ?: return@launch

                syncImagesUseCase(workspaceId, _loader)

            } catch (e: Exception) {
                Log.e(TAG, "syncImages job failed or was cancelled", e)
            } finally {
                Log.e("Images Sync", "Sync complete")

                _loader.emit(Pair("", false))
            }
        }
    }


    fun downloadUsersAvatar() {

        usersAvatarSync = viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentWorkspaceId = _workspaceID.value
                if (!currentWorkspaceId.isNullOrEmpty()) {
                    downloadUsersAvatarUseCase(currentWorkspaceId,user.value.id)
                }
            } catch (e: CancellationException) {
                Log.d(TAG, "Avatar sync was cancelled successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Download failed", e)
            }
        }

    }

}