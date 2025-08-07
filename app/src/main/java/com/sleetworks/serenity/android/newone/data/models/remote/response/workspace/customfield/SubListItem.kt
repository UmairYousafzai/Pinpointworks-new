package com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.customfield

data class SubListItem(
    val id: Long,
    val label: String,
    val subList: List<SubListItem>,
    val maxListIndex: Int
)