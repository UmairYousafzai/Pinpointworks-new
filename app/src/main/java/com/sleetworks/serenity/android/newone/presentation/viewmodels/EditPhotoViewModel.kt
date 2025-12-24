package com.sleetworks.serenity.android.newone.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sleetworks.serenity.android.newone.data.imageStore.UserImageStore
import com.sleetworks.serenity.android.newone.domain.reporitories.local.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
class EditPhotoViewModel @Inject constructor(
    private val userImageStore: UserImageStore,
    private val dataStoreRepository: DataStoreRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _photo: MutableStateFlow<Pair<String, File>> = MutableStateFlow("" to File(""))
    val photo: StateFlow<Pair<String, File>>
        get() = _photo

    private val _workspaceID = dataStoreRepository.workspaceIDFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
    val workspaceID
        get() = _workspaceID


    init {
        viewModelScope.launch(Dispatchers.IO) {
            val imageId = (savedStateHandle.get<String>("imageId") ?: "")

            getImage(imageId)
        }
    }

    suspend fun getImage(imageId: String) {
        _workspaceID.first { !it.isNullOrEmpty() }

        workspaceID.value?.let {
            val image = userImageStore.checkImage(
                it,
                "$it/images/original/",
                imageId
            )
            _photo.value = imageId to image
        }


    }

}