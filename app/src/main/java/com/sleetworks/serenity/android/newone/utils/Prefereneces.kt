package com.sleetworks.serenity.android.newone.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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

//Pref keys
val PREF_AUTH_TOKEN = stringPreferencesKey(AUTH_TOKEN)
val PREF_EMAIL = stringPreferencesKey(EMAIL)
val PREF_BASE_URL = stringPreferencesKey(BASE_URL)
val PREF_PASSWORD = stringPreferencesKey(PASSWORD)
val PREF_IS_LOGIN = stringPreferencesKey(IS_LOGIN)
val PREF_USER_ID = stringPreferencesKey(USER_ID)
val PREF_USER_NAME = stringPreferencesKey(USER_NAME)
val PREF_WORKSPACE_ID = stringPreferencesKey(WORKSPACE_ID)