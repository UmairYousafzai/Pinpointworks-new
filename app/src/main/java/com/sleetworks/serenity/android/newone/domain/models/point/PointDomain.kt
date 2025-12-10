package com.sleetworks.serenity.android.newone.domain.models.point

import com.sleetworks.serenity.android.newone.data.models.local.entities.AssigneeEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.Header
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.Image
import com.sleetworks.serenity.android.newone.data.models.remote.response.comment.Comment
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Document
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Images360
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Pin
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.PointCustomField
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Polygon
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Video
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.WorkspaceRef
import java.io.Serializable

data class PointDomain(
    var id: String = "",
    var localId: String = "",
    var assigneeIds: ArrayList<String> = arrayListOf(),
    var assignees: ArrayList<AssigneeEntity> = arrayListOf(),
    var customFieldSimplyList: ArrayList<PointCustomField> = arrayListOf(),
    var description: String = "",
    var descriptionRich: String = "",
    var documents: ArrayList<Document> = arrayListOf(),
    var flagged: Boolean = false,
    var images: ArrayList<Image> = arrayListOf(),
    var images360: ArrayList<Images360> = arrayListOf(),
    var pins: ArrayList<Pin> = arrayListOf(),
    var polygons: ArrayList<Polygon> = arrayListOf(),
    var priority: String = "Low",
    var sequenceNumber: Int = 0,
    var status: String = "",
    var title: String = "",
    var videos: ArrayList<Video> = arrayListOf(),
    var workspaceRef: WorkspaceRef? = null,
    var edited: Boolean = false,
    var tags: ArrayList<String> = arrayListOf(),
    var isModified: Boolean = false,
    var header: Header? = null,
    var comments: ArrayList<Comment> = arrayListOf(),
    val updatedAt:Long=0


) : Serializable