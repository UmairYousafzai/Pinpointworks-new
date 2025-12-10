package com.sleetworks.serenity.android.newone.data.models.local

import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.customfield.SubListOfTotal

data class NewCustomField(
    var customFieldTemplateId:String="",
    var value:String="",
    var type:String="",
    var subValues: List<SubListOfTotal>? = null,
    var isSubValueActive: Boolean?= null,
    var selectedItemIds: List<String>? = null,
)
