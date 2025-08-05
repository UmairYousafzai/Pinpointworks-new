package com.sleetworks.serenity.android.newone.data.models.local.datastore

import androidx.room.Entity


data class UserPreference(
    val authToken: String="",
    val email: String="",
    val password: String="",
)
