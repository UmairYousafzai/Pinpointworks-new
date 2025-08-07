package com.sleetworks.serenity.android.newone.data.datasource.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.AccountRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.SiteRef

class WorkspaceConverters {
    private val gson = Gson()
    @TypeConverter
    fun fromAccountRef(accountRef: AccountRef):
            String? = gson.toJson(accountRef)

    @TypeConverter
    fun toAccountRef(value: String?): AccountRef? =
        value?.let { gson.fromJson(it, AccountRef::class.java) }

    @TypeConverter
    fun fromSiteRef(siteRef: SiteRef):
            String? = gson.toJson(siteRef)

    @TypeConverter
    fun toSiteRef(value: String?): SiteRef? =
        value?.let { gson.fromJson(it, SiteRef::class.java) }


}