package com.sleetworks.serenity.android.newone.data.repositories.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.sleetworks.serenity.android.newone.data.models.local.datastore.UserPreference
import com.sleetworks.serenity.android.newone.domain.reporitories.DataStoreRepository
import com.sleetworks.serenity.android.newone.utils.PREF_AUTH_TOKEN
import com.sleetworks.serenity.android.newone.utils.PREF_EMAIL
import com.sleetworks.serenity.android.newone.utils.PREF_IS_LOGIN
import com.sleetworks.serenity.android.newone.utils.PREF_PASSWORD
import com.sleetworks.serenity.android.newone.utils.PREF_USER_ID
import com.sleetworks.serenity.android.newone.utils.PREF_USER_NAME
import com.sleetworks.serenity.android.newone.utils.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
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
    override val emailFlow: StateFlow<String?> = context.dataStore.data.map {
        it[PREF_EMAIL]
    }.stateIn(
        scope = externalScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    override val isLoggedInFlow: StateFlow<Boolean?> = context.dataStore.data.map {
        it[PREF_IS_LOGIN] != null

    }.stateIn(
        scope = externalScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

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

    override suspend fun clearData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    override suspend fun saveUserInfo(user: UserPreference) {
        context.dataStore.edit { preferences ->
            preferences[PREF_USER_ID] = user.email
            preferences[PREF_USER_NAME] = user.email
            preferences[PREF_EMAIL] = user.email
            preferences[PREF_AUTH_TOKEN] = user.authToken
            preferences[PREF_PASSWORD] = user.password
            preferences[PREF_IS_LOGIN] = user.password

        }
    }

}