package com.sleetworks.serenity.android.newone.data.datasource.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.customfield.SubListItem

class CustomFieldConverters {
    private val gson = Gson()


    @TypeConverter
    fun fromSubList(value: List<SubListItem>?): String? = gson.toJson(value)

    @TypeConverter
    fun toSubList(value: String?): List<SubListItem>? =
        value?.let { gson.fromJson(it, object : TypeToken<List<SubListItem>>() {}.type) }


}