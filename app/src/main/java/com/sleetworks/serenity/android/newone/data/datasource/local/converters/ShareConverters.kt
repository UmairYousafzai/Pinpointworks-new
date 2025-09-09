package com.sleetworks.serenity.android.newone.data.datasource.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.AccountRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.SiteRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.AdvancedAccessLevels
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.Permissions
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.UserRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.site.SiteImageRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.site.SitePlan
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.WorkspaceRef

class ShareConverters {
    private val gson = Gson()
    @TypeConverter
    fun fromUserRef(userRef: UserRef):
            String? = gson.toJson(userRef)

    @TypeConverter
    fun toUserRef(value: String?): UserRef? =
        value?.let { gson.fromJson(it, UserRef::class.java) }


    @TypeConverter
    fun fromPermissions(permission: Permissions):
            String? = gson.toJson(permission)

    @TypeConverter
    fun toPermissions(value: String?): Permissions? =
        value?.let { gson.fromJson(it, Permissions::class.java) }

    @TypeConverter
    fun fromAdvancedAccessLevels(advancedAccessLevels: AdvancedAccessLevels):
            String? = gson.toJson(advancedAccessLevels)

    @TypeConverter
    fun toAdvancedAccessLevels(value: String?): AdvancedAccessLevels? =
        value?.let { gson.fromJson(it, AdvancedAccessLevels::class.java) }

}