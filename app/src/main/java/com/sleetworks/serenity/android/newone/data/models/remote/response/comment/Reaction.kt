package com.sleetworks.serenity.android.newone.data.models.remote.response.comment

import com.google.gson.annotations.SerializedName
import com.sleetworks.serenity.android.newone.data.models.remote.response.Header
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.TargetRef

data class Reaction(
    @SerializedName("_id")
    val id: String,
    val header: Header,
    var like: List<String>,
    val tags: List<Any>?,
    val targetRef: TargetRef?,
    val type: String,
    val commentId: String,
    )