package com.sleetworks.serenity.android.newone.data.models.local.datastore

import androidx.room.Entity


data class UserPreference(
    val id: String="",
    val username: String="",
    val isLogin: Boolean= false,
    val authToken: String="",
    val email: String="",
    val password: String="",
)
