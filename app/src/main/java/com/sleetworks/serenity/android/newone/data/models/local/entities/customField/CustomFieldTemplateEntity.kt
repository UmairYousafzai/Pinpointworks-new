package com.sleetworks.serenity.android.newone.data.models.local.entities.customField

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.customfield.SubListItem

@Entity("custom_field_template",
    indices = [
        Index(
            value = ["id"],
            unique = true
        )
    ])
data class CustomFieldTemplateEntity(

    @PrimaryKey (autoGenerate = true)
    val localId: Long? =null,
    val id: Long,
    val workspaceID: String,
    val parentId: Long?,                 // null for root
    val label: String,
    val type: String,
    val description: String? = null,
    val currency: String? = null,
    val currencyCode: String? = null,
    val currencySymbol: String? = null,
    val decimalPlaces: Int? = null,
    val showTotal: Boolean? = null,
    val showCommas: Boolean? = null,
    val showHoursOnly: Boolean? = null,
    val formula: String? = null,
    val outputType: String? = null,
    val nestingLevel: Int? = null,
    val unit: String? = null,
    val display: Boolean? = null,
    val lockedValue: Boolean? = null,
    val lockedTemplate: Boolean? = null,
    val volyIntegrationActive: Boolean? = null,
    val subValuesActive: Boolean? = null,
    val maxListIndex: Int? = null,
    val subList: List<SubListItem>? = null,
    val saveAt:Long? = null
    )
