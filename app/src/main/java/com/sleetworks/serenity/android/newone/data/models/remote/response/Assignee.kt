package com.sleetworks.serenity.android.newone.data.models.remote.response

import java.io.Serializable

data class Assignee(
    val id: String,
    val caption: String,
    val primaryImageId: String,
    val email: String,
    val type: String
) : Serializable