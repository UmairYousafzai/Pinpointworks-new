package com.sleetworks.serenity.android.newone.data.models.local.entities.customField

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.customfield.CustomFieldTemplate

@Entity("custom_field_Template")
data class CustomFieldTemplateEntity(

    @PrimaryKey val id: Long,
    val workspaceID: String,
    val label: String,
    val type: String,
    val description: String?,
    val currency: String?,
    val currencyCode: String?,
    val currencySymbol: String?,
    val decimalPlaces: Int?,
    val showTotal: Boolean?,
    val showCommas: Boolean?,
    val showHoursOnly: Boolean?,
    val formula: String?,
    val outputType: String?,
    val nestingLevel: Int?,
    val unit: String?,
    val display: Boolean?,
    val lockedValue: Boolean?,
    val maxListIndex: Int?,
    val subListJson: List<CustomFieldTemplate>?
)
