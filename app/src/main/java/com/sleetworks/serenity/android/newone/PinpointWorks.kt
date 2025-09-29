package com.sleetworks.serenity.android.newone

import android.app.Application
import com.sleetworks.serenity.android.newone.utils.TextRichOptions
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PinpointWorks: Application() {


    override fun onCreate() {
        super.onCreate()

        TextRichOptions.convertBase64ToRichContentString()
    }
}