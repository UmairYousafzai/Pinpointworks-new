package com.sleetworks.serenity.android.newone.domain.reporitories.local

import com.sleetworks.serenity.android.newone.data.models.local.entities.customField.CustomFieldTemplateEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointAssigneeEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointTagEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.customfield.CustomFieldTemplate

interface PointAssigneeRepository {

    suspend fun insertAssignee(assignee: PointAssigneeEntity)
    suspend fun insertAssignees(assignees: List<PointAssigneeEntity>)
    suspend fun getAssigneesByPointID(pointID: String): List<PointAssigneeEntity>?
    suspend fun getAllAssignees(): List<PointAssigneeEntity?>
}