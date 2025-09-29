package com.sleetworks.serenity.android.newone.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sleetworks.serenity.android.newone.data.network.Resource
import com.sleetworks.serenity.android.newone.domain.mapper.toDomain
import com.sleetworks.serenity.android.newone.domain.models.point.PointDomain
import com.sleetworks.serenity.android.newone.domain.reporitories.local.CommentLocalRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.DataStoreRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.PointRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.CommentRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PointDetailViewModel @Inject constructor(
    val commentRepository: CommentRemoteRepository,
    val commentLocalRepository: CommentLocalRepository,
    val dataStoreRepository: DataStoreRepository,
    val pointRepository: PointRepository,
    val savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    private val _error: MutableStateFlow<String> = MutableStateFlow("")
    val error: StateFlow<String>
        get() = _error

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

    private val _pointID: MutableStateFlow<String> = MutableStateFlow("")
    val pointID: StateFlow<String>
        get() = _pointID
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

    private val _point: StateFlow<PointDomain> =
        pointID.filter { !it.isEmpty() }
            .flatMapLatest { pointId ->
                pointRepository.getPointByIDFlow(
                    pointId
                )
            }
            .map {
                it.toDomain()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = PointDomain()
            )
    val point
        get() = _point

    init {

        _pointID.value = savedStateHandle.get<String>("pointId") ?: ""

    }


    fun getPointComments() {

        viewModelScope.launch(Dispatchers.IO) {
            val result = commentRepository.getPointComments(pointID.value)

            when (result) {
                is Resource.Success -> {

                    result.data.entity?.let {
                        commentLocalRepository.insertComments(it)
                    }

                }

                is Resource.Error -> {
                    _error.emit(result.apiException.message ?: "Unknown Error")
                }

                else -> {}

            }

        }


    }

}