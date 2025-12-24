package com.sleetworks.serenity.android.newone.domain.models

import com.sleetworks.serenity.android.newone.data.models.remote.response.Header
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.User
import com.sleetworks.serenity.android.newone.data.models.remote.response.comment.DefectRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.comment.Reaction
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.WorkspaceRef

data class CommentDomain(
    val id: String,
    val comment: String,
    val commentRich: String,
    val defectRef: DefectRef,
    val header: Header?,
    val tags: List<String>,
    val totalBytes: Int,
    val type: String,
    val workspaceRef: WorkspaceRef,
    var mentions: List<String>?,
    var addedTime: Long,
    var author: User?,
    val reactions: Reaction?
)
