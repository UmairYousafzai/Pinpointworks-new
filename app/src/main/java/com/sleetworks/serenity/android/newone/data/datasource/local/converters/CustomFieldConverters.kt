package com.sleetworks.serenity.android.newone.data.datasource.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sleetworks.serenity.android.newone.data.models.local.entities.customField.CustomFieldTemplateEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.AccountRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.SiteRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.site.SiteImageRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.site.SitePlan
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.WorkspaceRef

class CustomFieldConverters {
    private val gson = Gson()


    @TypeConverter
    fun fromCustomFieldList(value: ArrayList<CustomFieldTemplateEntity>?): String? = gson.toJson(value)

    @TypeConverter
    fun toCustomFieldList(value: String?): ArrayList<CustomFieldTemplateEntity>? =
        value?.let { gson.fromJson(it, object : TypeToken<ArrayList<CustomFieldTemplateEntity>>() {}.type) }


}