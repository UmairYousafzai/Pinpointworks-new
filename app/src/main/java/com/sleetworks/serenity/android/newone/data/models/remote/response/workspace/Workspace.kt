package com.sleetworks.serenity.android.newone.data.models.remote.response.workspace

import com.google.gson.annotations.SerializedName

data class Workspace(
    @SerializedName("_id")
    val id: String,
    val accountRef: AccountRef,
    val customFields: List<Any>,
    val hidden: Boolean,
    val siteImageRef: SiteImageRef,
    val siteName: String,
    val sitePlan: SitePlan,
    val siteRef: SiteRef,
    val tags: List<Any>
)