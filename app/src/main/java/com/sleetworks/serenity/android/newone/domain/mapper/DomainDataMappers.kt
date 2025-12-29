package com.sleetworks.serenity.android.newone.domain.mapper

import com.sleetworks.serenity.android.newone.data.mappers.toModel
import com.sleetworks.serenity.android.newone.data.models.local.CommentWithReactions
import com.sleetworks.serenity.android.newone.data.models.local.PointWithRelations
import com.sleetworks.serenity.android.newone.data.models.local.entities.AssigneeEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.CommentEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.customField.PointCustomFieldEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointAssigneeEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointTagEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.Image
import com.sleetworks.serenity.android.newone.data.models.remote.response.comment.Comment
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.PointCustomField
import com.sleetworks.serenity.android.newone.domain.models.AssigneeDomain
import com.sleetworks.serenity.android.newone.domain.models.CommentDomain
import com.sleetworks.serenity.android.newone.domain.models.point.PointDomain
import com.sleetworks.serenity.android.newone.presentation.model.LocalImage

fun PointCustomFieldEntity.toDomain(): PointCustomField = PointCustomField(
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

fun PointTagEntity.toDomain(): String {
    return tag;
}

fun PointAssigneeEntity.toDomain(): String {
    return assigneeId;
}

fun CommentEntity.toDomain(): Comment {
    return Comment(
        id = id,
        comment = comment,
        commentRich = commentRich,
        defectRef = defectRef,
        header = header,
        tags = tags,
        totalBytes = totalBytes,
        type = type,
        workspaceRef = workspaceRef,
    )
}

fun CommentWithReactions.toDomain(): CommentDomain {
    return CommentDomain(
        id = comment.id,
        comment = comment.comment,
        commentRich = comment.commentRich,
        defectRef = comment.defectRef,
        header = comment.header,
        tags = comment.tags,
        totalBytes = comment.totalBytes,
        type = comment.type,
        workspaceRef = comment.workspaceRef,
        reactions = reactions?.toModel(),
        addedTime = comment.addedTime,
        mentions = emptyList(),
        author = comment.author
    )
}

fun PointWithRelations.toDomain(): PointDomain {
    return PointDomain(
        id = point.id,
        localId = point.localID,
        isModified = point.isModified,
        assigneeIds = ArrayList(assignees.map { it.assigneeId }),
        customFieldSimplyList = customFields.map { it.toDomain() } as ArrayList<PointCustomField>,
        description = point.description,
        descriptionRich = point.descriptionRich,
        documents = point.documents,
        flagged = point.flagged,
        images = point.images,
        images360 = point.images360,
        pins = point.pins,
        polygons = point.polygons,
        priority = point.priority,
        sequenceNumber = point.sequenceNumber,
        status = point.status,
        title = point.title,
        videos = point.videos,
        workspaceRef = point.workspaceRef,
        edited = point.edited,
        tags = ArrayList(tags.map { it.tag }),
        header = point.header,
        comments = ArrayList(comments.map { it.toDomain() }),
        updatedAt = point.updatedAt

    )
}

fun Image.toDomain(): LocalImage {
    return LocalImage(
        caption = caption ?: "",
        id = id,
        type = type ?: "",
        imageLocalPath = ""
    )
}

fun AssigneeEntity.toDomain(): AssigneeDomain {
    return AssigneeDomain(
        id = id,
        caption = caption,
        primaryImageId = primaryImageId,
        email = email,
        type = type,
        workspaceId = workspaceId
    )
}
