package com.sleetworks.serenity.android.newone.domain.reporitories

import com.sleetworks.serenity.android.newone.data.models.remote.response.ApiResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.Workspace
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.WorkspaceResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.Share
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.site.Site
import com.sleetworks.serenity.android.newone.data.network.Resource

interface WorkspaceRemoteRepository {

    suspend fun getAllWorkspaces(): Resource<ApiResponse<List<WorkspaceResponse>>>
    suspend fun getAllSites(): Resource<ApiResponse<List<Site>>>
    suspend fun getAllShares(): Resource<ApiResponse<List<Share>>>
}