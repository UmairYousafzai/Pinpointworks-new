package com.sleetworks.serenity.android.newone.domain.models.share

import java.io.Serializable

data class CustomFieldPermissionDomainModel(
    val templateId: Long,
    val permission: PermissionDomainModel
): Serializable
