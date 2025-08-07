package com.sleetworks.serenity.android.newone.domain.reporitories

import com.sleetworks.serenity.android.newone.data.models.local.datastore.UserPreference
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface DataStoreRepository {
    val authTokenFlow: StateFlow<String?>
    val emailFlow: StateFlow<String?>
    val isLoggedInFlow: StateFlow<Boolean?>
    suspend fun putString(key: String, value: String)
    suspend fun putLong(key: String, value: Long)
    suspend fun putInt(key: String, value: Int)
    suspend fun putBoolean(key: String, value: Boolean)
    suspend fun getBoolean(key: String): Boolean
    suspend fun getString(key: String): String
    suspend fun getInt(key: String): Int?
    suspend fun clearData()
    suspend fun saveUserInfo(user: UserPreference)}