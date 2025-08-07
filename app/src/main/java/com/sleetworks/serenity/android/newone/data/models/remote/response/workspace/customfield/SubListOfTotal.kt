package com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.customfield

data class SubListOfTotal(
    var id: Int,
    var value: Double,
    var description: String,
    var filledCustomFieldId: String,
    var workspaceId: String,
    var defectID: String,
)
