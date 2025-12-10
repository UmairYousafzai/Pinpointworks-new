package com.sleetworks.serenity.android.newone.data.models.local.entities.point

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.sleetworks.serenity.android.newone.data.models.remote.response.Header
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.Image
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Document
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Images360
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Pin
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Polygon
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Video
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.WorkspaceRef

@Entity(
    tableName = "point", indices = [Index(value = ["local_id"], unique = true)]
)
data class PointEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "local_id")
    val localID: String,
//    val assignees: ArrayList<PointAssigneeEntity>,
//    val customFieldSimplyList: ArrayList<PointCustomFieldEntity>,
    val description: String,
    val descriptionRich: String,
//    @ColumnInfo(name = "created_by")
    val createdBy: String,
    val documents: ArrayList<Document>,
    val flagged: Boolean,
//    @Embedded
    val header: Header?,
    val images: ArrayList<Image>,
    val images360: ArrayList<Images360>,
    val pins: ArrayList<Pin>,
    val polygons: ArrayList<Polygon>,
    val priority: String,
    val sequenceNumber: Int,
    val status: String,
//    val tags: ArrayList<PointTagEntity>,
    val title: String,
    val videos: ArrayList<Video>,
    @Embedded
    val workspaceRef: WorkspaceRef,
    val isModified: Boolean,
    val edited: Boolean,
    val updatedAt:Long

    )