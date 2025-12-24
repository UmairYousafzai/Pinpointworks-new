package com.sleetworks.serenity.android.newone.presentation.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sleetworks.serenity.android.newone.data.imageStore.UserImageStore
import com.sleetworks.serenity.android.newone.data.mappers.toDomain
import com.sleetworks.serenity.android.newone.data.models.local.entities.OfflineModifiedPointFields
import com.sleetworks.serenity.android.newone.data.network.NetworkUtil
import com.sleetworks.serenity.android.newone.data.network.Resource
import com.sleetworks.serenity.android.newone.domain.models.point.PointDomain
import com.sleetworks.serenity.android.newone.domain.reporitories.local.DataStoreRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.PointRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.ImageRemoteRepository
import com.sleetworks.serenity.android.newone.domain.usecase.SyncPointOriginalImageUseCase
import com.sleetworks.serenity.android.newone.presentation.model.LocalImage
import com.sleetworks.serenity.android.newone.presentation.ui.model.DefectFieldType
import com.sleetworks.serenity.android.newone.utils.convertToOfflineFieldValue
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val imageRepository: ImageRemoteRepository,
    private val pointRepository: PointRepository,
    private val userImageStore: UserImageStore,
    private val syncPointImageUseCase: SyncPointOriginalImageUseCase,
    private val pointLocalRepository: PointRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val TAG = "PhotoViewModel"

    private val _imageSyncLoader: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val imageSyncLoader: StateFlow<Boolean>
        get() = _imageSyncLoader

    private val _mainLoader: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val mainLoader: StateFlow<Boolean>
        get() = _mainLoader

    private val _deleteSuccess: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val deleteSuccess: StateFlow<Boolean>
        get() = _deleteSuccess

    private val _error: MutableStateFlow<String> = MutableStateFlow("")
    val error: StateFlow<String>
        get() = _error

    private val _currentIndex: MutableStateFlow<Int> = MutableStateFlow(0)
    val currentIndex: StateFlow<Int>
        get() = _currentIndex

    private val _workspaceID = dataStoreRepository.workspaceIDFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
    val workspaceID
        get() = _workspaceID

    private val _photos: MutableStateFlow<List<Pair<String, File>>> = MutableStateFlow(emptyList())
    val photos: StateFlow<List<Pair<String, File>>>
        get() = _photos

    private var point: PointDomain? = null

    init {

        viewModelScope.launch(Dispatchers.IO) {
            _currentIndex.value = (savedStateHandle.get<Int>("currentIndex") ?: 0)
            val pointId = (savedStateHandle.get<String>("pointId") ?: "")
            fetchPointAndImages(pointId)
        }
    }

    fun setDeleteSuccessStatus(flag: Boolean = false) {
        _deleteSuccess.value = flag
    }

    fun setErrorMessage(message: String = "") {
        _error.value = message
    }

    suspend fun fetchPointAndImages(pointId: String) {
        _imageSyncLoader.value = true

        val pointEntity = pointLocalRepository.getPointByID(pointId)
        pointEntity?.let {
            point = it.toDomain()
            checkAndSyncImages(point!!)
        }
    }

    suspend fun checkAndSyncImages(point: PointDomain) {
        _workspaceID.first { !it.isNullOrEmpty() }

        workspaceID.value?.let { workspaceId ->
            syncPointImageUseCase(workspaceId, point)
            _photos.value = point.images.map { image ->
                (image.id to userImageStore.checkImage(
                    workspaceId,
                    "$workspaceId/images/original/",
                    image.id
                ))
            }
        }

        _imageSyncLoader.value = false

    }

    fun deleteImage(imageId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _mainLoader.value = true

            try {
                if (NetworkUtil.isInternetAvailable(context)) {

                    point?.let {
                        val result = imageRepository.removeImage(it.id, imageId)
                        when (result) {
                            is Resource.Error -> {
                                Log.e(TAG, "deleteImage: ", result.apiException)
                                saveDeleteImageLocally(imageId)

                            }

                            Resource.Loading -> {

                            }

                            is Resource.Success<Unit> -> {
                                deleteImageFromPointLocally(imageId)
                                _photos.value =
                                    photos.value.filter { item -> item.first != imageId }

                            }
                        }
                    }
                } else {
                    saveDeleteImageLocally(imageId)

                }
            } catch (e: Exception) {
                saveDeleteImageLocally(imageId)
                Log.e(TAG, "deleteImage: ", e)
            }


            _mainLoader.value = false

        }

    }

    suspend fun saveDeleteImageLocally(imageId: String) {

        point?.let {


            val value = imageId.convertToOfflineFieldValue()
            pointRepository.insertModifiedField(
                OfflineModifiedPointFields(
                    workspaceId = _workspaceID.value ?: "",
                    pointId = it.id,
                    field = imageId,
                    type = DefectFieldType.DELETED_IMAGES + it.id,
                    value = value,
                )
            )
            deleteImageFromPointLocally(imageId)
            userImageStore.deleteThumbnail(workspaceID.value ?: "", imageId)
            userImageStore.deleteOriginal(workspaceID.value ?: "", imageId)
        }

    }

    suspend fun deleteImageFromPointLocally(imageId: String) {
        point = point?.copy(
            images = point!!.images.filter { item -> item.id != imageId } as ArrayList<LocalImage>
        )
        point?.let { it1 -> pointRepository.insertPointDetailsOnly(it1) }
        _photos.value =
            photos.value.filter { item -> item.first != imageId }
        _deleteSuccess.value = true

    }


}