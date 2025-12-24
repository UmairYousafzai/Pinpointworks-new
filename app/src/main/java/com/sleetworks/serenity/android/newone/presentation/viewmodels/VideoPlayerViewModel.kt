package com.sleetworks.serenity.android.newone.presentation.viewmodels

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.sleetworks.serenity.android.newone.data.datasource.VideoStore
import com.sleetworks.serenity.android.newone.data.mappers.toDomain
import com.sleetworks.serenity.android.newone.data.models.local.entities.OfflineModifiedPointFields
import com.sleetworks.serenity.android.newone.data.models.remote.response.ApiResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Video
import com.sleetworks.serenity.android.newone.data.network.NetworkUtil
import com.sleetworks.serenity.android.newone.data.network.Resource
import com.sleetworks.serenity.android.newone.domain.models.point.PointDomain
import com.sleetworks.serenity.android.newone.domain.reporitories.local.DataStoreRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.PointRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.VideoRemoteRepository
import com.sleetworks.serenity.android.newone.presentation.common.UIEvent
import com.sleetworks.serenity.android.newone.presentation.ui.model.DefectFieldType
import com.sleetworks.serenity.android.newone.utils.convertToOfflineFieldValue
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.BufferedInputStream
import java.io.File
import javax.inject.Inject

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val videoRemoteRepository: VideoRemoteRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val videoStore: VideoStore,
    private val pointRepository: PointRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val TAG = "VideoPlayerViewModel"

    private val _mainLoader: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val mainLoader: StateFlow<Boolean>
        get() = _mainLoader

    private val _downloadLoader: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val downloadLoader: StateFlow<Boolean>
        get() = _downloadLoader


    private val _error: MutableStateFlow<String> = MutableStateFlow("")
    val error: StateFlow<String>
        get() = _error

    private val _videoFile: MutableStateFlow<File?> = MutableStateFlow(null)
    val videoFile: StateFlow<File?>
        get() = _videoFile

    private val _workspaceID = dataStoreRepository.workspaceIDFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
    val workspaceID
        get() = _workspaceID

    private val _player = MutableStateFlow<ExoPlayer?>(null)
    val player: StateFlow<ExoPlayer?> = _player

    private val _uiEvent = MutableSharedFlow<UIEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    var videoId = ""
    var pointId = ""
    private var point: PointDomain? = null

    init {

        viewModelScope.launch(Dispatchers.IO) {
            videoId = (savedStateHandle.get<String>("videoId") ?: "")
            pointId = (savedStateHandle.get<String>("pointId") ?: "")
            fetchPointAndImages(pointId)

            downLoadVideo(videoId)
        }
    }

    fun setErrorMessage(message: String = "") {
        _error.value = message
    }


    suspend fun fetchPointAndImages(pointId: String) {

        val pointEntity = pointRepository.getPointByID(pointId)
        pointEntity?.let {
            point = it.toDomain()
        }
    }

    suspend fun downLoadVideo(videoId: String) {
        _workspaceID.first { !it.isNullOrEmpty() }

        _workspaceID.value?.let {workspaceId->
            try {
                _downloadLoader.value = true

                val file = videoStore.getOriginalFileReference(workspaceId,  videoId)
                if (file.exists()) {
                    _videoFile.value = file
                    setupPlayer(_videoFile.value!!)
                } else {
                    val result = videoRemoteRepository.downloadVideo(videoId)

                    when (result) {
                        is Resource.Error -> {

                        }

                        Resource.Loading -> {
                        }

                        is Resource.Success<ResponseBody> -> {

                            val outputStream = videoStore.getOriginalOutputStream(workspaceId, videoId)
                            val inputStream = BufferedInputStream(result.data.byteStream())

                            // It is better to use .use { } in Kotlin to automatically close streams
                            outputStream.use { out ->
                                inputStream.use { input ->
                                    val buffer = ByteArray(4096)
                                    var bytesRead: Int
                                    while (input.read(buffer).also { bytesRead = it } != -1) {
                                        out.write(buffer, 0, bytesRead)
                                    }
                                }
                            }
                            // Now the file is fully written and closed.
                            val savedFile = videoStore.getOriginalFileReference(workspaceId, videoId)
                            _videoFile.value = savedFile
                            setupPlayer(savedFile)

                            Log.d(TAG, "Video saved at: ${savedFile.absolutePath}")
                        }


                    }
                }

            } catch (e: Exception) {
                Log.e(TAG, "downLoadVideo: ", e)
            }
            _downloadLoader.value = false
        }

    }

    suspend fun setupPlayer(file: File) {
        withContext(Dispatchers.Main) {
            val exoPlayer = ExoPlayer.Builder(context).build()
            val mediaItem = MediaItem.fromUri(file.toUri())
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
            _player.value = exoPlayer

        }
    }

    fun deleteVideo() {
        if (videoId.isNotEmpty() && pointId.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                if (NetworkUtil.isInternetAvailable(context)) {
                    try {
                        _mainLoader.value = true

                        val result = videoRemoteRepository.deleteVideo(pointId, videoId)
                        when (result) {
                            is Resource.Error -> {
                                _mainLoader.value = false
                                saveDeleteVideoLocally(videoId)

                            }

                            Resource.Loading -> {

                            }

                            is Resource.Success<ApiResponse<String>> -> {
                                _mainLoader.value = false
                                val response = result.data.entity
                                if (response != null) {

                                    deleteVideoFromPointLocally(videoId)
                                } else {
                                    saveDeleteVideoLocally(videoId)
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "deleteVideo: ", e)
                        saveDeleteVideoLocally(videoId)
                        _mainLoader.value = false

                    }
                } else {
                    saveDeleteVideoLocally(videoId)
                }
            }
        }
    }

    suspend fun saveDeleteVideoLocally(videoId: String) {

        point?.let {


            val value = videoId.convertToOfflineFieldValue()
            pointRepository.insertModifiedField(
                OfflineModifiedPointFields(
                    workspaceId = _workspaceID.value ?: "",
                    pointId = it.id,
                    field = videoId,
                    type = DefectFieldType.DELETED_VIDEOS + pointId,
                    value = value,
                )
            )

            deleteVideoFromPointLocally(videoId)
            videoStore.deleteOriginal(workspaceID.value ?: "", videoId)
        }

    }

    suspend fun deleteVideoFromPointLocally(videoId: String) {
        point = point?.copy(
            videos = point!!.videos.filter { item -> item.id != videoId } as ArrayList<Video>
        )
        point?.let { it1 -> pointRepository.insertPointDetailsOnly(it1) }
        _uiEvent.emit(UIEvent.PopBackStack)


    }

    override fun onCleared() {
        _player.value?.release()
    }

}