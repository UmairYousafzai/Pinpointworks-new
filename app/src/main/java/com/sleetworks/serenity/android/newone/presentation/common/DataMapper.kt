package com.sleetworks.serenity.android.newone.presentation.common

import com.sleetworks.serenity.android.newone.data.models.local.entities.SiteEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.WorkspaceEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.customField.CustomFieldTemplateEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Point
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.PointCustomField
import com.sleetworks.serenity.android.newone.domain.models.point.PointDomain
import com.sleetworks.serenity.android.newone.presentation.model.CustomFieldTempUI
import com.sleetworks.serenity.android.newone.presentation.model.SiteUiModel
import com.sleetworks.serenity.android.newone.presentation.model.WorkspaceUiModel


fun SiteEntity.toUiModel(
): SiteUiModel {

    return SiteUiModel(
        id = this.id,
        name = this.name,
        siteImageRef = this.siteImageRef,
        sitePlan = this.sitePlan,
        workspaceRef = this.workspaceRef,
    )
}

fun WorkspaceEntity.toUiModel(
    isSelected: Boolean
): WorkspaceUiModel {

    return WorkspaceUiModel(
        id = this.id,
        siteRef = this.siteRef,
        siteName = this.siteName,
        accountRef = this.accountRef,
        hidden = this.hidden,
        isSelected = isSelected
    )
}

fun CustomFieldTemplateEntity.toUiModel() : CustomFieldTempUI
{
    return CustomFieldTempUI(
        id = this.id,
        workspaceID = this.workspaceID,
        parentId = this.parentId,
        label = this.label,
        type = this.type,
        description = this.description,
        currency = this.currency,
        currencyCode = this.currencyCode,
        currencySymbol = this.currencySymbol,
        decimalPlaces = this.decimalPlaces,
        showTotal = this.showTotal,
        showCommas = this.showCommas,
        showHoursOnly = this.showHoursOnly,
        formula = this.formula,
        outputType = this.outputType,
        nestingLevel = this.nestingLevel,
        unit = this.unit,
        display = this.display,
        lockedValue = this.lockedValue,
        lockedTemplate = this.lockedTemplate,
        subList= subList,
        subValuesActive = subValuesActive,
        maxListIndex = maxListIndex,
        volyIntegrationActive = volyIntegrationActive,

    )
}


fun PointDomain.toPoint(): Point {
    return Point(
        id = this.id.ifEmpty { this.localId }, // Use localId as fallback if id is empty
        assignees = if (this.assigneeIds.isNotEmpty()) {
            ArrayList(this.assigneeIds)
        } else {
            null
        },
        customFieldSimplyList = this.customFieldSimplyList,
        description = this.description.ifEmpty { null },
        descriptionRich = this.descriptionRich.ifEmpty { null },
        documents = this.documents,
        flagged = this.flagged,
        images = this.images,
        images360 = this.images360,
        pins = if (this.pins.isNotEmpty()) {
            ArrayList(this.pins)
        } else {
            null
        },
        polygons = if (this.polygons.isNotEmpty()) {
            ArrayList(this.polygons)
        } else {
            null
        },
        priority = this.priority,
        sequenceNumber = this.sequenceNumber,
        status = this.status,
        title = this.title,
        videos = this.videos,
        workspaceRef = this.workspaceRef
            ?: throw IllegalArgumentException("workspaceRef cannot be null for Point"),
        edited = this.edited,
        tags = if (this.tags.isNotEmpty()) {
            ArrayList(this.tags)
        } else {
            null
        },
        header = this.header
    )
}

fun CustomFieldTempUI.toPointCustomField(): PointCustomField
{
    return PointCustomField(
        value = "",
        customFieldTemplateId = this.id.toString(),
        type = this.type,
        label = this.label,
        currency = this.currency,
        currencyCode = this.currencyCode,
        currencySymbol = this.currencySymbol,
        unit = this.unit,
        decimalPlaces = this.decimalPlaces,
        showCommas = this.showCommas,
        showHoursOnly = this.showHoursOnly,
    )
}