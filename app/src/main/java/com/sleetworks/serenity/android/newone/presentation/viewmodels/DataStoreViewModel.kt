package com.sleetworks.serenity.android.newone.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sleetworks.serenity.android.newone.domain.reporitories.local.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataStoreViewModel @Inject constructor(private val repository: DataStoreRepository) : ViewModel() {

    val isLoggedIn = repository.isLoggedInFlow
    val isFirstSync = repository.isFirstSyncFlow


    fun put(key: String, value: String) {
        viewModelScope.launch {
            repository.putString(key, value)
        }
    }

    fun put(key: String, value: Long) {
        viewModelScope.launch {
            repository.putLong(key, value)
        }
    }


    fun put(key: String, value: Int) {
        viewModelScope.launch {
            repository.putInt(key, value)
        }
    }


    fun put(key: String, value: Boolean) {
        viewModelScope.launch {
            repository.putBoolean(key, value)
        }
    }






    fun clearData() = viewModelScope.launch {
        repository.clearData()
    }
}