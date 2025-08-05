package com.sleetworks.serenity.android.newone.domain.reporitories

import com.sleetworks.serenity.android.newone.data.models.remote.response.ApiResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.WorkspaceResponse
import com.sleetworks.serenity.android.newone.data.network.Resource

interface WorkspaceRemoteRepository {

    suspend fun getAllWorkspaces(): Resource<ApiResponse<WorkspaceResponse>>
}