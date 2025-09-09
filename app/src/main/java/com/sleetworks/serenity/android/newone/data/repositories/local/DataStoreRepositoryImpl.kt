package com.sleetworks.serenity.android.newone.data.repositories.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.sleetworks.serenity.android.newone.data.models.local.datastore.UserPreference
import com.sleetworks.serenity.android.newone.domain.reporitories.local.DataStoreRepository
import com.sleetworks.serenity.android.newone.utils.PREF_AUTH_TOKEN
import com.sleetworks.serenity.android.newone.utils.PREF_EMAIL
import com.sleetworks.serenity.android.newone.utils.PREF_FIRST_SYNC
import com.sleetworks.serenity.android.newone.utils.PREF_IS_LOGIN
import com.sleetworks.serenity.android.newone.utils.PREF_PASSWORD
import com.sleetworks.serenity.android.newone.utils.PREF_POINT_SYNC_TIMESTAMP
import com.sleetworks.serenity.android.newone.utils.PREF_USER_ID
import com.sleetworks.serenity.android.newone.utils.PREF_USER_IMAGE_ID
import com.sleetworks.serenity.android.newone.utils.PREF_USER_NAME
import com.sleetworks.serenity.android.newone.utils.PREF_WORKSPACE_ID
import com.sleetworks.serenity.android.newone.utils.PREF_WORKSPACE_SYNC_TIMESTAMP
import com.sleetworks.serenity.android.newone.utils.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(val context: Context) : DataStoreRepository {

    val externalScope = CoroutineScope(Dispatchers.Default)
    override val authTokenFlow: StateFlow<String?> = context.dataStore.data.map {
        it[PREF_AUTH_TOKEN]
    }.stateIn(
        scope = externalScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
    override val syncTime: StateFlow<String?> = context.dataStore.data.map {
        val timeStamp = it[PREF_WORKSPACE_SYNC_TIMESTAMP] ?: 0
        if (timeStamp != 0L) {
            val sdf = SimpleDateFormat("MMM/dd/yyyy, HH:mm", Locale.getDefault())
            sdf.format(Date(timeStamp))
        } else ""
    }.stateIn(
        scope = externalScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    override val pointSyncTime: StateFlow<String?> = context.dataStore.data.map {
        val timeStamp = it[PREF_POINT_SYNC_TIMESTAMP] ?: 0
        if (timeStamp != 0L) {
            timeStamp.toString()
        } else "1111111111111"
    }.stateIn(
        scope = externalScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    override val user: StateFlow<UserPreference?> = context.dataStore.data.map {
        UserPreference(
            id = it[PREF_USER_ID] ?: "",
            username = it[PREF_USER_NAME] ?: "",
            isLogin = it[PREF_IS_LOGIN] ?: false,
            email = it[PREF_EMAIL] ?: "",
            password = it[PREF_PASSWORD] ?: "",
            imageID = it[PREF_USER_IMAGE_ID] ?: "",
        )
    }.stateIn(
        scope = externalScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserPreference()
    )
    override val emailFlow: StateFlow<String?> = context.dataStore.data.map {
        it[PREF_EMAIL]
    }.stateIn(
        scope = externalScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    override val isLoggedInFlow: StateFlow<Boolean?> = context.dataStore.data.map {
        it[PREF_IS_LOGIN] ?: false


    }.stateIn(
        scope = externalScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    override val isFirstSyncFlow: StateFlow<Boolean?> = context.dataStore.data.map {
        it[PREF_FIRST_SYNC] ?: false

    }.stateIn(
        scope = externalScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    override val workspaceIDFlow: Flow<String?> = context.dataStore.data.map {
        it[PREF_WORKSPACE_ID]
    }

    override suspend fun putString(key: String, value: String) {
        val preferencesKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    override suspend fun putLong(key: String, value: Long) {
        val preferencesKey = longPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    override suspend fun putInt(key: String, value: Int) {
        val preferencesKey = intPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    override suspend fun putBoolean(key: String, value: Boolean) {
        val preferencesKey = booleanPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    override suspend fun getBoolean(key: String): Boolean {
        val preferencesKey = booleanPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        return preferences[preferencesKey] ?: false
    }

    override suspend fun getString(key: String): String {
        val preferencesKey = stringPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        return preferences[preferencesKey] ?: ""
    }


    override suspend fun getInt(key: String): Int? {
        val preferencesKey = intPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        return preferences[preferencesKey]
    }

    override suspend fun getLong(key: String): Long? {
        val preferencesKey = longPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        return preferences[preferencesKey]
    }

    override suspend fun clearData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    override suspend fun saveUserInfo(user: UserPreference) {
        context.dataStore.edit { preferences ->
            preferences[PREF_USER_ID] = user.id
            preferences[PREF_USER_NAME] = user.username
            preferences[PREF_EMAIL] = user.email
            preferences[PREF_PASSWORD] = user.password
            preferences[PREF_IS_LOGIN] = user.isLogin
            preferences[PREF_USER_IMAGE_ID] = user.imageID

        }
    }

}