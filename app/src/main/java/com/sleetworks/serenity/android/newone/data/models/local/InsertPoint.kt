package com.sleetworks.serenity.android.newone.data.models.local

import com.sleetworks.serenity.android.newone.data.models.local.entities.customField.PointCustomFieldEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointAssigneeEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointTagEntity

data class InsertPoint(
    val point: PointEntity,
    val tags: List<PointTagEntity>,
    val assignees: List<PointAssigneeEntity>,
    val customFields: List<PointCustomFieldEntity>,
)
