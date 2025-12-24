package com.sleetworks.serenity.android.newone.data.models.remote.request

import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.User
import com.sleetworks.serenity.android.newone.data.models.remote.response.comment.DefectRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.WorkspaceRef
import java.io.Serializable

class AddCommentRequest(
    var comment: String,
    var commentRich: String,
    var mentions: List<String>,
    var author: User,
    var defectRef: DefectRef,
    var workspaceRef: WorkspaceRef,

    ) : Serializable