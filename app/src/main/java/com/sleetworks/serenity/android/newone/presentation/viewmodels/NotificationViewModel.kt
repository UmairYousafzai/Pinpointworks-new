package com.sleetworks.serenity.android.newone.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sleetworks.serenity.android.newone.data.imageStore.UserImageStore
import com.sleetworks.serenity.android.newone.domain.mapper.toDomain
import com.sleetworks.serenity.android.newone.domain.models.AssigneeDomain
import com.sleetworks.serenity.android.newone.domain.reporitories.local.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.io.File
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val userImageStore: UserImageStore,
    private val userRepository: UserRepository,


    ) : ViewModel() {
    private val _error: MutableStateFlow<String> = MutableStateFlow("")
    val error: StateFlow<String>
        get() = _error

    private val _mainLoader: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val mainLoader: StateFlow<Boolean>
        get() = _mainLoader

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


    fun setError(message: String = "") {

        _error.value = message
    }


    fun getUserAvatar(imageID: String): File? {
        return userImageStore.getAvatarFile(imageID)
    }

}