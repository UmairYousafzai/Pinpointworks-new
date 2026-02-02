package com.sleetworks.serenity.android.newone.data.models.remote.response.notification

data class ChangeBody(
    val label: String,
    val cfFieldType: String,
    val comment: String,
    val commentId: String,
    val newValue: Any,
    val oldValue: Any,

)