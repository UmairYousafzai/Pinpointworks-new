package com.sleetworks.serenity.android.newone.data.models.remote.response.point

import java.io.Serializable

data class PointCustomField(
    val value: String,
    val customFieldTemplateId: String,
    val type: String,
    val label: String,
    val currency: String? = null,
    val currencyCode: String? = null,
    val currencySymbol: String? = null,
    val unit: String? = null,
    val decimalPlaces: Int? = null,
    val showCommas: Boolean? = null,
    val showHoursOnly: Boolean? = null,
    val idOfChosenElement: String? = null,
    val selectedItemIds: List<String>? = null
) : Serializable
