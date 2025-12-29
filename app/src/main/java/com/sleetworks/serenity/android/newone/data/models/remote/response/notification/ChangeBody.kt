package com.sleetworks.serenity.android.newone.data.models.remote.response.notification

data class ChangeBody(
    val label: String,
    val newValue: List<NewValue>,
    val oldValue: List<OldValue>
)