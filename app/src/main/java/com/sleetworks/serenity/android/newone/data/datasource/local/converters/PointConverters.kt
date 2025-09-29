package com.sleetworks.serenity.android.newone.data.datasource.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sleetworks.serenity.android.newone.data.models.local.entities.customField.CustomFieldTemplateEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.customField.PointCustomFieldEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.comment.Comment
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Document
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Images360
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Pin
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Polygon
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Video
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.WorkspaceRef

class PointConverters {
    private val gson = Gson()


    @TypeConverter
    fun fromCustomFieldList(value: ArrayList<CustomFieldTemplateEntity>?): String? = gson.toJson(value)

    @TypeConverter
    fun toCustomFieldList(value: String?): ArrayList<PointCustomFieldEntity>? =
        value?.let { gson.fromJson(it, object : TypeToken<ArrayList<PointCustomFieldEntity>>() {}.type) }

    @TypeConverter
    fun fromDocumentList(value: ArrayList<Document>?): String? = gson.toJson(value)

    @TypeConverter
    fun toDocumentList(value: String?): ArrayList<Document>? =
        value?.let { gson.fromJson(it, object : TypeToken<ArrayList<Document>>() {}.type) }

    @TypeConverter
    fun fromCommentList(value: List<Comment>?): String? = gson.toJson(value)

    @TypeConverter
    fun toCommentList(value: String?): List<Comment>? =
        value?.let { gson.fromJson(it, object : TypeToken<List<Comment>>() {}.type) }

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

    @TypeConverter
    fun fromMap(value: Map<String, Any>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toMap(value: String): Map<String, Any> {
        return try {
            Gson().fromJson(value, object : TypeToken<Map<String, Any>>() {}.type)
        } catch (e: Exception) {
            emptyMap()
        }
    }


    @TypeConverter
    fun fromWorkspaceRef(workspaceRef: WorkspaceRef): String? = gson.toJson(workspaceRef)

    @TypeConverter
    fun toWorkspaceRef(value: String?): WorkspaceRef? =
        value?.let { gson.fromJson(it, WorkspaceRef::class.java) }

    @TypeConverter
    fun fromStringListForCustomField(value: List<String>?): String? = gson.toJson(value)

    @TypeConverter
    fun toStringListForCustomField(value: String?): List<String>? =
        value?.let { gson.fromJson(it, object : TypeToken<List<String>>() {}.type) }

}