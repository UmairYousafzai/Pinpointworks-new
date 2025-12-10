package com.sleetworks.serenity.android.newone.data.models.remote.response.point

import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.customfield.SubListOfTotal
import java.io.Serializable

data class  PointCustomField(
    var value: String,
    val customFieldTemplateId: String,
    val type: String,
    val label: String,
    val currency: String? = null,
    val currencyCode: String? = null,
    val currencySymbol: String? = null,
    val addedTimeValue: String? = null,
    val unit: String? = null,
    val decimalPlaces: Int? = null,
    val showCommas: Boolean? = null,
    val showHoursOnly: Boolean? = null,
    val idOfChosenElement: String? = null,
    val selectedItemIds: List<String>? = null,
    val subValues: List<SubListOfTotal>? = null
) : Serializable
