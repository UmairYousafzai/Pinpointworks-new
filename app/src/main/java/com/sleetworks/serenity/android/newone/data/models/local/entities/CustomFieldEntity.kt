package com.sleetworks.serenity.android.newone.data.models.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("custom_field")
data class CustomFieldEntity(

    @PrimaryKey
    val id: Long ,
    val currency: String?? = "",
    val currencyCode: String? = "",
    val currencySymbol: String? = "",
    val decimalPlaces: Int?,
    val display: Boolean? = false,
    val formula: String? = "",
    val label: String? = "",
    val lockedValue: Boolean? = false,
    val maxListIndex: Int?=0,
    val nestingLevel: Int?=0,
    val outputType: String? = "",
    val showCommas: Boolean? = false,
    val showHoursOnly: Boolean? = false,
    val showTotal: Boolean? = false,
//    val subList: List<SubListItem>,
    val selectedItemIds: ArrayList<String>? = arrayListOf(),
//    val subValues: List<SubListOfTotal>,
    val subValuesActive: Boolean? = false,
    val type: String? = "",
    val unit: String? = "",
    val workspaceId: String? = "",
    val modified: Boolean? = false,
    val volyIntegrationActive: Boolean? = false,
    val lockedTemplate: Boolean? = false,
)
