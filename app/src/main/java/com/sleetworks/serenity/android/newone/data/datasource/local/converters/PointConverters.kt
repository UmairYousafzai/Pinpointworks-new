package com.sleetworks.serenity.android.newone.data.datasource.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.sleetworks.serenity.android.newone.data.models.local.NewCustomField
import com.sleetworks.serenity.android.newone.data.models.local.OfflineFieldValue
import com.sleetworks.serenity.android.newone.data.models.local.entities.customField.CustomFieldTemplateEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.customField.PointCustomFieldEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.CreatedBy
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.UpdatedBy
import com.sleetworks.serenity.android.newone.data.models.remote.response.comment.Comment
import com.sleetworks.serenity.android.newone.data.models.remote.response.comment.Reaction
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Document
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Images360
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Pin
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Polygon
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Video
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.WorkspaceRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.customfield.SubListOfTotal
import com.sleetworks.serenity.android.newone.presentation.model.LocalImage

class PointConverters {
    private val gson = Gson()


    @TypeConverter
    fun fromCustomFieldList(value: ArrayList<CustomFieldTemplateEntity>?): String? =
        gson.toJson(value)

    @TypeConverter
    fun toCustomFieldList(value: String?): ArrayList<PointCustomFieldEntity>? =
        value?.let {
            gson.fromJson(
                it,
                object : TypeToken<ArrayList<PointCustomFieldEntity>>() {}.type
            )
        }

    @TypeConverter
    fun fromDocumentList(value: ArrayList<Document>?): String? = gson.toJson(value)

    @TypeConverter
    fun toDocumentList(value: String?): ArrayList<Document>? =
        value?.let { gson.fromJson(it, object : TypeToken<ArrayList<Document>>() {}.type) }

    @TypeConverter
    fun fromCommentList(value: ArrayList<Comment>?): String? = gson.toJson(value)

    @TypeConverter
    fun toCommentList(value: String?): ArrayList<Comment>? =
        value?.let { gson.fromJson(it, object : TypeToken<ArrayList<Comment>>() {}.type) }

    @TypeConverter
    fun fromImages360List(value: ArrayList<Images360>?): String? = gson.toJson(value)

    @TypeConverter
    fun toImages360List(value: String?): ArrayList<Images360>? =
        value?.let { gson.fromJson(it, object : TypeToken<ArrayList<Images360>>() {}.type) }


    @TypeConverter
    fun fromLocalImageList(value: ArrayList<LocalImage>?): String? = gson.toJson(value)

    @TypeConverter
    fun toLocalImageList(value: String?): ArrayList<LocalImage>? =
        value?.let { gson.fromJson(it, object : TypeToken<ArrayList<LocalImage>>() {}.type) }


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
    fun fromUpdatedBy(updatedBy: UpdatedBy): String? = gson.toJson(updatedBy)

    @TypeConverter
    fun toUpdatedBy(value: String?): UpdatedBy? =
        value?.let { gson.fromJson(it, UpdatedBy::class.java) }

    @TypeConverter
    fun fromCreatedBy(createdBy: CreatedBy): String? = gson.toJson(createdBy)

    @TypeConverter
    fun toCreatedBy(value: String?): CreatedBy? =
        value?.let { gson.fromJson(it, CreatedBy::class.java) }

    @TypeConverter
    fun fromOfflineFieldValue(value: OfflineFieldValue?): String? = value?.let {
        val typeName = when (it) {
            is OfflineFieldValue.StringValue -> "StringValue"
            is OfflineFieldValue.BooleanValue -> "BooleanValue"
            is OfflineFieldValue.StringListValue -> "StringListValue"
            is OfflineFieldValue.IntValue -> "IntValue"
            is OfflineFieldValue.DoubleValue -> "DoubleValue"
            is OfflineFieldValue.NewCustomFieldValue -> "NewCustomFieldValue"
            is OfflineFieldValue.CommentReactionValue -> "CommentReactionValue"
        }

        val wrapper = mapOf(
            "type" to typeName,
            "value" to it.getValue() // Use the getValue() function from sealed class
        )
        gson.toJson(wrapper)
    } ?: gson.toJson(mapOf("type" to "StringValue", "value" to ""))

    @TypeConverter
    fun toOfflineFieldValue(value: String?): OfflineFieldValue? = value?.let { jsonString ->
        try {
            val jsonElement = gson.fromJson(jsonString, JsonElement::class.java)
            val type = jsonElement.asJsonObject["type"]?.asString
            val actualValue = jsonElement.asJsonObject["value"]

            when (type) {
                "StringValue" -> OfflineFieldValue.StringValue(actualValue?.asString ?: "")
                "BooleanValue" -> OfflineFieldValue.BooleanValue(actualValue?.asBoolean ?: false)
                "StringListValue" -> {
                    // Parse JsonArray directly
                    val list = if (actualValue?.isJsonArray == true) {
                        actualValue.asJsonArray.mapNotNull { it.asString }
                    } else {
                        // Fallback: try parsing as string
                        val listJson = actualValue?.toString() ?: "[]"
                        gson.fromJson<List<String>>(
                            listJson,
                            object : TypeToken<List<String>>() {}.type
                        ) ?: emptyList()
                    }
                    OfflineFieldValue.StringListValue(list)
                }

                "IntValue" -> OfflineFieldValue.IntValue(actualValue?.asInt ?: 0)
                "CommentReactionValue" -> OfflineFieldValue.CommentReactionValue(
                    gson.fromJson(
                        actualValue ,
                        Reaction::class.java
                    )
                )

                "NewCustomFieldValue" -> {
                    val list = if (actualValue?.isJsonArray == true) {
                        actualValue.asJsonArray.mapNotNull {
                            gson.fromJson(
                                it,
                                NewCustomField::class.java
                            )
                        }
                    } else {
                        // Fallback: try parsing as string
                        val listJson = actualValue?.toString() ?: "[]"
                        gson.fromJson<List<NewCustomField>>(
                            listJson,
                            object : TypeToken<List<NewCustomField>>() {}.type
                        ) ?: emptyList()
                    }
                    OfflineFieldValue.NewCustomFieldValue(list)

                }

                "DoubleValue" -> OfflineFieldValue.DoubleValue(actualValue?.asDouble ?: 0.0)
                else -> null
            }
        } catch (e: Exception) {
            null
        }
    }

    @TypeConverter
    fun fromStringListForCustomField(value: List<String>?): String? = gson.toJson(value)

    @TypeConverter
    fun toStringListForCustomField(value: String?): List<String>? =
        value?.let { gson.fromJson(it, object : TypeToken<List<String>>() {}.type) }

    @TypeConverter
    fun fromSubListOfTotal(value: List<SubListOfTotal>?): String? = gson.toJson(value)

    @TypeConverter
    fun toSubListOfTotal(value: String?): List<SubListOfTotal>? =
        value?.let { gson.fromJson(it, object : TypeToken<List<SubListOfTotal>>() {}.type) }

}