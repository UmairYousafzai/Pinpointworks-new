package com.sleetworks.serenity.android.newone

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PinpointWorks: Application() {


    override fun onCreate() {
        super.onCreate()
        // Force light mode - prevent dark mode regardless of system settings
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}