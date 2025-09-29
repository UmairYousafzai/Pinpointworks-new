package com.sleetworks.serenity.android.newone.data.mappers

import android.util.Log
import com.google.gson.Gson
import com.sleetworks.serenity.android.newone.data.models.local.InsertPoint
import com.sleetworks.serenity.android.newone.data.models.local.entities.AssigneeEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.CommentEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.ShareEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.SiteEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.SubListItemEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.WorkspaceEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.customField.CustomFieldTemplateEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.customField.PointCustomFieldEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointAssigneeEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointTagEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.Assignee
import com.sleetworks.serenity.android.newone.data.models.remote.response.comment.Comment
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Point
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.PointCustomField
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.Workspace
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.WorkspaceRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.customfield.CustomFieldTemplate
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.customfield.SubListItem
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.Share
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.site.Site

fun Assignee.toEntity(): AssigneeEntity {
    return AssigneeEntity(
        id = this.id,
        caption = this.caption,
        primaryImageId = this.primaryImageId,
        email = this.email,
        type = this.type

    )
}

fun AssigneeEntity.toDomain(): Assignee {
    return Assignee(
        id = this.id,
        caption = this.caption,
        primaryImageId = this.primaryImageId,
        email = this.email,
        type = this.type
    )
}


fun Workspace.toEntity(): WorkspaceEntity {
    return WorkspaceEntity(
        id = this.id,
        accountRef = this.accountRef,
        hidden = this.hidden,
        siteName = this.siteName,
        siteRef = this.siteRef
    )
}

fun Site.toEntity(): SiteEntity {
    return SiteEntity(
        id = this._id,
        header = this.header,
        name = this.name,
        siteImageRef = this.siteImageRef,
        sitePlan = this.sitePlan,
        tags = this.tags as ArrayList<String>,
        totalBytes = this.totalBytes,
        type = this.type,
        workspaceRef = this.workspaceRef
    )
}

fun CustomFieldTemplate.toEntities(
    parentId: Long? = null,
    workspaceId: String
): List<CustomFieldTemplateEntity> {
    val me = CustomFieldTemplateEntity(
        id = id,
        workspaceID = workspaceId,
        parentId = parentId,
        label = label,
        type = type ?: "",
        description = description,
        currency = currency,
        currencyCode = currencyCode,
        currencySymbol = currencySymbol,
        decimalPlaces = decimalPlaces,
        showTotal = showTotal,
        showCommas = showCommas,
        showHoursOnly = showHoursOnly,
        formula = formula,
        outputType = outputType,
        nestingLevel = nestingLevel,
        unit = unit,
        display = display,
        lockedValue = lockedValue,
        maxListIndex = maxListIndex
    )
    val children = subList.orEmpty().flatMap { it.toEntities(parentId = id, workspaceId) }
    return listOf(me) + children
}

fun Share.toEntity(): ShareEntity {
    return ShareEntity(
        id = this.id,
        userRef = this.userRef,
        targetRef = this.targetRef,
        type = this.type,
        label = this.label,
        permissions = this.permissions,
        advancedAccessLevels = this.advancedAccessLevels,
        defectTags = this.defectTags,
        tagLimited = this.tagLimited,
        hidden = this.hidden,
        shareOption = this.shareOption,
        currentShare = false,
        previousShare = true
    )
}

fun SubListItem.toEntity(
    parentId: String,
    fieldParentId: Long,
    workspaceId: String
): SubListItemEntity {

    return SubListItemEntity(
        id = this.id,
        label = this.label,
        maxListIndex = this.maxListIndex,
        parentId = parentId,
        fieldParentId = fieldParentId,
        workspaceId = workspaceId,
        isSelected = false,


        )
}

fun Point.toEntity(): PointEntity {
    Log.e("Point", "toEntity: ${Gson().toJson(this)}")


    return PointEntity(
        id = this.id ?: "",
        localID = this.id,
//        assignees = this.assignees?.map {
//            it.toAssigneeEntity(
//                pointId = this.id ?: ""
//            )
//        } as ArrayList<PointAssigneeEntity>
//            ?: arrayListOf(),
//        customFieldSimplyList = customFieldSimplyList.map { it.toEntity(id) } as ArrayList<PointCustomFieldEntity>,
        description = this.description ?: "",
        descriptionRich = this.descriptionRich ?: "",
        createdBy = this.header?.createdBy?.id ?: "",
        documents = this.documents ?: arrayListOf(),
        flagged = this.flagged,
        header = this.header, // nullable, no problem
        images = this.images ?: arrayListOf(),
        images360 = this.images360 ?: arrayListOf(),
        pins = this.pins ?: arrayListOf(),
        polygons = this.polygons ?: arrayListOf(),
        priority = this.priority ?: "",
        sequenceNumber = this.sequenceNumber ?: 0,
        status = this.status ?: "",
//        tags = this.tags?.map {
//            it.toTagEntity(
//                pointId = this.id ?: ""
//            )
//        } as ArrayList<PointTagEntity> ?: arrayListOf(),
        title = this.title ?: "",
        videos = this.videos ?: arrayListOf(),
        workspaceRef = if (!this.workspaceRef.caption.isNullOrEmpty()) this.workspaceRef else WorkspaceRef(
            "",
            this.workspaceRef.id
        ),
        isModified = false,
        edited = this.edited ?: true
    )
}


fun String.toTagEntity(id: Int = 0, pointId: String): PointTagEntity {
    return PointTagEntity(
        pointId = pointId,
        tag = this

    )
}

fun String.toAssigneeEntity(id: Int = 0, pointId: String): PointAssigneeEntity {
    return PointAssigneeEntity(
        id = id,
        pointId = pointId,
        assigneeId = this,

        )
}

// PointCustomFieldMappers.kt


fun PointCustomField.toEntity(
    pointID: String,
    localID: Int = 0
): PointCustomFieldEntity = PointCustomFieldEntity(
    localID = localID,
    pointID = pointID,
    value = value,
    customFieldTemplateId = customFieldTemplateId,
    type = type,
    label = label,
    currency = currency,
    currencyCode = currencyCode,
    currencySymbol = currencySymbol,
    unit = unit,
    decimalPlaces = decimalPlaces,
    showCommas = showCommas,
    showHoursOnly = showHoursOnly,
    idOfChosenElement = idOfChosenElement,
    selectedItemIds = selectedItemIds
)

fun Point.toInsertPoint(

): InsertPoint {


    return InsertPoint(
        point = this.toEntity(),
        tags = this.tags?.map {
            it.toTagEntity(
                pointId = this.id ?: ""
            )
        } as ArrayList<PointTagEntity> ?: arrayListOf(),
        assignees = assignees?.map {
            it.toAssigneeEntity(
                pointId = this.id ?: ""
            )
        } as ArrayList<PointAssigneeEntity>,
        customFields = customFieldSimplyList.map { it.toEntity(id) } as ArrayList<PointCustomFieldEntity>
    )
}

fun Comment.toEntity(): CommentEntity {
    return CommentEntity(
        id = this.id,
        comment = this.comment,
        commentRich = this.commentRich,
        defectRef = this.defectRef,
        header = this.header,
        tags = this.tags,
        totalBytes = this.totalBytes,
        type = this.type,
        workspaceRef = this.workspaceRef

    )
}

