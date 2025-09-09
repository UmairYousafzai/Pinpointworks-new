package com.sleetworks.serenity.android.newone.data.datasource.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sleetworks.serenity.android.newone.data.models.local.entities.CustomFieldEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.Image
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Document
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Images360
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Pin
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Polygon
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Video
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.customfield.CustomField

class PointConverters {
    private val gson = Gson()


    @TypeConverter
    fun fromCustomFieldList(value: ArrayList<CustomFieldEntity>?): String? = gson.toJson(value)

    @TypeConverter
    fun toCustomFieldList(value: String?): ArrayList<CustomFieldEntity>? =
        value?.let { gson.fromJson(it, object : TypeToken<ArrayList<CustomFieldEntity>>() {}.type) }

    @TypeConverter
    fun fromDocumentList(value: ArrayList<Document>?): String? = gson.toJson(value)

    @TypeConverter
    fun toDocumentList(value: String?): ArrayList<Document>? =
        value?.let { gson.fromJson(it, object : TypeToken<ArrayList<Document>>() {}.type) }

    @TypeConverter
    fun fromImages360List(value: ArrayList<Images360>?): String? = gson.toJson(value)

    @TypeConverter
    fun toImages360List(value: String?): ArrayList<Images360>? =
        value?.let { gson.fromJson(it, object : TypeToken<ArrayList<Images360>>() {}.type) }


    @TypeConverter
    fun fromPinList(value: ArrayList<Pin>?): String? = gson.toJson(value)

    @TypeConverter
    fun toPinList(value: String?): ArrayList<Pin>? =
        value?.let { gson.fromJson(it, object : TypeToken<ArrayList<Pin>>() {}.type) }

    @TypeConverter
    fun fromPolygonList(value: ArrayList<Polygon>?): String? = gson.toJson(value)

    @TypeConverter
    fun toPolygonList(value: String?): ArrayList<Polygon>? =
        value?.let { gson.fromJson(it, object : TypeToken<ArrayList<Polygon>>() {}.type) }

    @TypeConverter
    fun fromVideoList(value: ArrayList<Video>?): String? = gson.toJson(value)

    @TypeConverter
    fun toVideoList(value: String?): ArrayList<Video>? =
        value?.let { gson.fromJson(it, object : TypeToken<ArrayList<Video>>() {}.type) }

}