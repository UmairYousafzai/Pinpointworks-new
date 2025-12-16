package com.sleetworks.serenity.android.newone.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sleetworks.serenity.android.newone.domain.reporitories.local.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    val dataStoreRepository: DataStoreRepository

) : ViewModel() {

    val TAG = "SharedViewModel"
    private val _snackbarChannel = Channel<String>()
    val snackbarFlow = _snackbarChannel.receiveAsFlow()
    private val _workspaceID = dataStoreRepository.workspaceIDFlow
    val workspaceID
        get() = _workspaceID

    fun showSnackbar(message: String) {


        viewModelScope.launch {
            Log.d(TAG, "showSnackbar: ")
            _snackbarChannel.send(message)
            delay(1000)
            _snackbarChannel.send("")

        }
    }
}
