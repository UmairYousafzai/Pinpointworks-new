package com.sleetworks.serenity.android.newone.data.storage

import android.content.Context
import java.io.File

class InternalWorkspaceStorageUtils : AbstractWorkspaceFileStore() {


    override fun getTag(): String {
        return InternalWorkspaceStorageUtils::class.simpleName ?: "InternalWorkspaceStorageUtils"
    }

    override fun getRootDir(context: Context): File {
        return context.filesDir
    }
}