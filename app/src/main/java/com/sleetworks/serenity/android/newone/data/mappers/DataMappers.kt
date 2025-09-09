package com.sleetworks.serenity.android.newone.data.mappers

import android.util.Log
import com.google.gson.Gson
import com.sleetworks.serenity.android.newone.data.models.local.entities.CustomFieldEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.PointEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.ShareEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.SiteEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.SubListItemEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.UserEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.WorkspaceEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.User
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Point
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.Workspace
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.WorkspaceRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.customfield.CustomField
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.customfield.SubListItem
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.Share
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.site.Site
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.site.SiteImageRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.site.SitePlan

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        activeWorkspaceRef = this.activeWorkspaceRef,
        notificationStatus = this.notificationStatus,
        preferenceDocRef = this.preferenceDocRef,
        header = this.header,
        tags = this.tags ?: arrayListOf(),
        email = this.email,
        enabled = this.enabled,
        enabled2fa = this.enabled2fa,
        images = this.images,
        lastActivityEpochMillis = this.lastActivityEpochMillis,
        name = this.name,
        setup2faAfter = this.setup2faAfter,
        timeZoneId = this.timeZoneId,
        type = this.type,
        userType = this.userType,
        verified = this.verified,
        passwordHash = this.passwordHash
    )
}

fun UserEntity.toDomain(): User {
    return User(
        activeWorkspaceRef = this.activeWorkspaceRef,
        email = this.email,
        enabled = this.enabled,
        enabled2fa = this.enabled2fa,
        images = this.images,
        lastActivityEpochMillis = this.lastActivityEpochMillis,
        name = this.name,
        notificationStatus = this.notificationStatus,
        preferenceDocRef = this.preferenceDocRef,
        setup2faAfter = this.setup2faAfter,
        timeZoneId = this.timeZoneId,
        type = this.type,
        userType = this.userType,
        verified = this.verified,
        passwordHash = this.passwordHash,
        id = this.id

    ).apply {
        this.header = this@toDomain.header
        this.tags?.addAll(this@toDomain.tags)
    }

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

fun CustomField.toEntity(workspaceID: String): CustomFieldEntity {
    return CustomFieldEntity(
        id = this.id,
        currency = this.currency,
        currencyCode = this.currencyCode,
        currencySymbol = this.currencySymbol,
        decimalPlaces = this.decimalPlaces,
        display = this.display,
        formula = this.formula,
        label = this.label,
        lockedValue = this.lockedValue,
        maxListIndex = this.maxListIndex,
        nestingLevel = this.nestingLevel,
        outputType = this.outputType,
        showCommas = this.showCommas,
        showHoursOnly = this.showHoursOnly,
        showTotal = this.showTotal,
        selectedItemIds = this.selectedItemIds,
        subValuesActive = this.subValuesActive,
        type = this.type,
        unit = this.unit,
        workspaceId = workspaceId,
        modified = this.modified,
        volyIntegrationActive = this.volyIntegrationActive,
        lockedTemplate = this.lockedTemplate,
    )
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

    val customFieldEntitys = this.customFieldSimplyList.map {
        it.toEntity(this.workspaceRef.id)
    } as ArrayList<CustomFieldEntity>


    return PointEntity(
        id = this.id ?: "",
        localID = this.id,
        assignees = this.assignees ?: arrayListOf(),
        customFieldSimplyList = customFieldEntitys,
        description = this.description ?: "",
        descriptionRich = this.descriptionRich ?: "",
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
        tags = this.tags ?: arrayListOf(),
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
