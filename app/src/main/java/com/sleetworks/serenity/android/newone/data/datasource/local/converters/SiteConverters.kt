package com.sleetworks.serenity.android.newone.data.datasource.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.AccountRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.SiteRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.site.SiteImageRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.site.SitePlan
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.WorkspaceRef

class SiteConverters {
    private val gson = Gson()
//    @TypeConverter
//    fun fromSiteImageRef(siteImageRef: SiteImageRef):
//            String? = gson.toJson(siteImageRef)
//
//    @TypeConverter
//    fun toSiteImageRef(value: String?): SiteImageRef? =
//        value?.let { gson.fromJson(it, SiteImageRef::class.java) }

    @TypeConverter
    fun fromSSitePlan(sitePlan: SitePlan):
            String? = gson.toJson(sitePlan)

    @TypeConverter
    fun toSitePlan(value: String?): SitePlan? =
        value?.let { gson.fromJson(it, SitePlan::class.java) }


//    @TypeConverter
//    fun fromWorkspaceRef(workspaceRef: WorkspaceRef):
//            String? = gson.toJson(workspaceRef)
//
//    @TypeConverter
//    fun toWorkspaceRef(value: String?): WorkspaceRef? =
//        value?.let { gson.fromJson(it, WorkspaceRef::class.java) }
//

}