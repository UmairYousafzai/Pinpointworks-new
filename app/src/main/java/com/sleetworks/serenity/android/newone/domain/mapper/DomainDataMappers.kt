package com.sleetworks.serenity.android.newone.domain.mapper

import com.sleetworks.serenity.android.newone.data.models.local.PointWithRelations
import com.sleetworks.serenity.android.newone.data.models.local.entities.customField.PointCustomFieldEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointAssigneeEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointTagEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Point
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.PointCustomField

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


fun PointWithRelations.toDomain(): Point {
    return Point(
        id = point.id,
        assignees = ArrayList(assignees.map { it.assigneeId }),
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
        tags = ArrayList(tags.map { it.tag })
    )
}