package com.sleetworks.serenity.android.newone.data.models.remote.response.point

import com.google.gson.annotations.SerializedName
import com.sleetworks.serenity.android.newone.data.models.remote.response.Header
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.Image
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.WorkspaceRef
import java.io.Serializable

data class Point(
    @SerializedName("_id")
    val id: String,
    val assignees: ArrayList<String>?,
    val customFieldSimplyList: ArrayList<PointCustomField>,
    val description: String?,
    val descriptionRich: String?,
    val documents: ArrayList<Document>,
    val flagged: Boolean,
    val images: ArrayList<Image>,
    val images360: ArrayList<Images360>,
    val pins: ArrayList<Pin>?,
    val polygons: ArrayList<Polygon>?,
    val priority: String,
    val sequenceNumber: Int,
    val status: String,
    val title: String,
    val videos: ArrayList<Video>,
    val workspaceRef: WorkspaceRef,
    val edited: Boolean,
    val tags: ArrayList<String>?,
    var header: Header?

) : Serializable