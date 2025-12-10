package com.sleetworks.serenity.android.newone.domain.reporitories.remote

import com.sleetworks.serenity.android.newone.data.models.remote.response.ApiResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.PointResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.updatedPoint.UpdatedPoint
import com.sleetworks.serenity.android.newone.data.network.Resource
import okhttp3.RequestBody

interface PointRemoteRepository {

    suspend fun getPoints(
        lastSyncTime: String,
        workspaceId: String
    ): Resource<ApiResponse<PointResponse>>

    suspend fun updatePoint(
        pointId: String,
        body: RequestBody
    ): Resource<ApiResponse<UpdatedPoint>>



}