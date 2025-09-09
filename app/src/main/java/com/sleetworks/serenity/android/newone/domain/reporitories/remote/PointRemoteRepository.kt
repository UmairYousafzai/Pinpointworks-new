package com.sleetworks.serenity.android.newone.domain.reporitories.remote

import com.sleetworks.serenity.android.newone.data.models.remote.response.ApiResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.PointResponse
import com.sleetworks.serenity.android.newone.data.network.Resource

interface PointRemoteRepository {

    suspend fun getPoints(
        lastSyncTime: String,
        workspaceId: String
    ): Resource<ApiResponse<PointResponse>>


}