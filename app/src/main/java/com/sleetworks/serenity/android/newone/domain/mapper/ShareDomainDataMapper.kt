package com.sleetworks.serenity.android.newone.domain.mapper

import com.sleetworks.serenity.android.newone.data.models.local.entities.ShareEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.AdvancedAccessLevels
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.DefectPermissions
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.Permission
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.PermissionWrapper
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.Permissions
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.Share
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.SitePermissions
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.TargetRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.Timeline
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.UserRef
import com.sleetworks.serenity.android.newone.domain.models.share.AdvancedAccessLevelsDomainModel
import com.sleetworks.serenity.android.newone.domain.models.share.DefectPermissionsDomainModel
import com.sleetworks.serenity.android.newone.domain.models.share.PermissionDomainModel
import com.sleetworks.serenity.android.newone.domain.models.share.PermissionWrapperDomainModel
import com.sleetworks.serenity.android.newone.domain.models.share.PermissionsDomainModel
import com.sleetworks.serenity.android.newone.domain.models.share.ShareDomainModel
import com.sleetworks.serenity.android.newone.domain.models.share.SitePermissionsDomainModel
import com.sleetworks.serenity.android.newone.domain.models.share.TargetRefDomainModel
import com.sleetworks.serenity.android.newone.domain.models.share.TimelineDomainModel
import com.sleetworks.serenity.android.newone.domain.models.share.UserRefDomainModel


fun Share.toDomainModel(): ShareDomainModel {
    return ShareDomainModel(
        id = this.id,
        userRef = this.userRef.toDomainModel(),
        targetRef = this.targetRef.toDomainModel(),
        type = this.type,
        label = this.label,
        permissions = this.permissions.toDomainModel(),
        advancedAccessLevels = this.advancedAccessLevels?.toDomainModel(),
        defectTags = this.defectTags,
        tagLimited = this.tagLimited,
        hidden = this.hidden,
        shareOption = this.shareOption,
        currentShare = false,
        previousShare = false
    )
}

fun ShareEntity.toDomainModel(): ShareDomainModel {
    return ShareDomainModel(
        id = this.id,
        userRef = this.userRef.toDomainModel(),
        targetRef = this.targetRef.toDomainModel(),
        type = this.type,
        label = this.label,
        permissions = this.permissions?.toDomainModel(),
        advancedAccessLevels = this.advancedAccessLevels?.toDomainModel(),
        defectTags = this.defectTags,
        tagLimited = this.tagLimited,
        hidden = this.hidden,
        shareOption = this.shareOption,
        currentShare = this.currentShare,
        previousShare = this.previousShare
    )
}

fun AdvancedAccessLevels.toDomainModel(): AdvancedAccessLevelsDomainModel {
    return AdvancedAccessLevelsDomainModel(
        customFields = this.customFields,
        tags = this.tags.toDomainModel(),
        timeline = this.timeline.toDomainModel(),
        sitePlan = this.sitePlan.toDomainModel(),
        exports = this.exports.toDomainModel()
    )
}

fun Timeline.toDomainModel(): TimelineDomainModel {
    return TimelineDomainModel(
        permission = this.permission.toDomainModel(),
        comments = this.comments.toDomainModel()
    )
}

fun PermissionWrapper.toDomainModel(): PermissionWrapperDomainModel {
    return PermissionWrapperDomainModel(
        permission = this.permission.toDomainModel()
    )
}

fun Permission.toDomainModel(): PermissionDomainModel {
    return PermissionDomainModel(
        read = this.read,
        edit = this.edit,
    )
}

fun UserRef.toDomainModel(): UserRefDomainModel {
    return UserRefDomainModel(
        type = this.type,
        caption = this.caption,
        primaryImageId = this.primaryImageId,
        id = this.id
    )
}

fun TargetRef.toDomainModel(): TargetRefDomainModel {
    return TargetRefDomainModel(
        type = this.type,
        caption = this.caption,
        id = this.id
    )
}

fun Permissions.toDomainModel(): PermissionsDomainModel {
    return PermissionsDomainModel(
        fullVisibility = this.fullVisibility,
        defect = this.defect.toDomainModel(),
        site = this.site.toDomainModel()
    )
}

fun DefectPermissions.toDomainModel(): DefectPermissionsDomainModel {
    return DefectPermissionsDomainModel(
        read = this.read,
        edit = this.edit,
        create = this.create,
        contribute = this.contribute,
        delete = this.delete
    )
}

fun SitePermissions.toDomainModel(): SitePermissionsDomainModel {
    return SitePermissionsDomainModel(
        read = this.read,
        share = this.share
    )
}