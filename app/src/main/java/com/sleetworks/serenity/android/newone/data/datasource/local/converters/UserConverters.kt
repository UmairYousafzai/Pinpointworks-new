package com.sleetworks.serenity.android.newone.data.datasource.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sleetworks.serenity.android.newone.data.models.remote.response.Header
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.ActiveWorkspaceRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.Image
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.PreferenceDocRef

class UserConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromImageList(value: List<Image>?): String? = gson.toJson(value)

    @TypeConverter
    fun toImageList(value: String?): List<Image>? =
        value?.let { gson.fromJson(it, object : TypeToken<List<Image>>() {}.type) }

    @TypeConverter
    fun fromStringList(value: ArrayList<String>?): String? = gson.toJson(value)

    @TypeConverter
    fun toStringList(value: String?): ArrayList<String>? =
        value?.let { gson.fromJson(it, object : TypeToken<ArrayList<String>>() {}.type) }

    @TypeConverter
    fun fromHeader(header: Header?): String? = gson.toJson(header)

    @TypeConverter
    fun toHeader(value: String?): Header? =
        value?.let { gson.fromJson(it, Header::class.java) }

    @TypeConverter
    fun fromPreferenceDocRef(preferenceDocRef: PreferenceDocRef?):
            String? = gson.toJson(preferenceDocRef)

    @TypeConverter
    fun toPreferenceDocRef(value: String?): PreferenceDocRef? =
        value?.let { gson.fromJson(it, PreferenceDocRef::class.java) }

    @TypeConverter
    fun fromActiveWorkspaceRef(activeWorkspaceRef: ActiveWorkspaceRef):
            String? = gson.toJson(activeWorkspaceRef)

    @TypeConverter
    fun toActiveWorkspaceRef(value: String?): ActiveWorkspaceRef? =
        value?.let { gson.fromJson(it, ActiveWorkspaceRef::class.java) }


}
