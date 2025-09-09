package com.sleetworks.serenity.android.newone.domain.models.share

import java.io.Serializable

data class TimelineDomainModel(
    val permission: PermissionDomainModel,
    val comments: PermissionWrapperDomainModel
): Serializable
