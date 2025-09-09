package com.sleetworks.serenity.android.newone.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

const val APP_PREFERENCES = "pinpoint_works_preferences"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = APP_PREFERENCES)

//keys
const val AUTH_TOKEN ="auth_token"
const val EMAIL ="email"
const val BASE_URL ="base_url"
const val PASSWORD ="password"
const val IS_LOGIN ="is_login"
const val USER_ID ="user_id"
const val USER_NAME ="user_name"
const val WORKSPACE_ID ="workspace_id"
const val SITE_ID ="site_id"
const val FIRST_SYNC ="first_sync"
const val USER_IMAGE_ID ="user_image_id"
const val WORKSPACE_SYNC_TIMESTAMP ="workspace_sync_timestamp"
const val POINT_SYNC_TIMESTAMP ="point_sync_timestamp"

//Pref keys
val PREF_AUTH_TOKEN = stringPreferencesKey(AUTH_TOKEN)
val PREF_EMAIL = stringPreferencesKey(EMAIL)
val PREF_BASE_URL = stringPreferencesKey(BASE_URL)
val PREF_PASSWORD = stringPreferencesKey(PASSWORD)
val PREF_IS_LOGIN = booleanPreferencesKey(IS_LOGIN)
val PREF_USER_ID = stringPreferencesKey(USER_ID)
val PREF_USER_NAME = stringPreferencesKey(USER_NAME)
val PREF_WORKSPACE_ID = stringPreferencesKey(WORKSPACE_ID)
val PREF_SITE_ID = stringPreferencesKey(SITE_ID)
val PREF_FIRST_SYNC= booleanPreferencesKey(FIRST_SYNC)
val PREF_USER_IMAGE_ID= stringPreferencesKey(USER_IMAGE_ID)
val PREF_WORKSPACE_SYNC_TIMESTAMP= longPreferencesKey(WORKSPACE_SYNC_TIMESTAMP)
val PREF_POINT_SYNC_TIMESTAMP= longPreferencesKey(POINT_SYNC_TIMESTAMP)