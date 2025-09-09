package com.sleetworks.serenity.android.newone.data.storage

import android.content.Context
import java.io.File

class ExternalStorageUtils : AbstractRootFileStore() {

    override fun getRootDir(context: Context): File? {
        return context.getExternalFilesDir(null)
    }

    override fun getTag(): String {
        return ""
    }
}
