package com.sleetworks.serenity.android.newone.data.repositories.remote

import android.content.Context
import com.sleetworks.serenity.android.newone.data.datasource.remote.ApiService
import com.sleetworks.serenity.android.newone.data.models.remote.response.ApiResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.PointResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.updatedPoint.UpdatedPoint
import com.sleetworks.serenity.android.newone.data.network.Resource
import com.sleetworks.serenity.android.newone.data.network.RetrofitProvider
import com.sleetworks.serenity.android.newone.data.network.safeApiCall
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.PointRemoteRepository
import okhttp3.RequestBody
import javax.inject.Inject

class PointRemoteRepositoryImpl @Inject constructor(
    val context: Context,
    val retrofitProvider: RetrofitProvider
) :
    PointRemoteRepository {
    override suspend fun getPoints(
        lastSyncTime: String,
        workspaceId: String
    ): Resource<ApiResponse<PointResponse>> {
        return safeApiCall(context) {
            retrofitProvider.getRetrofit().create(ApiService::class.java)
                .getDefectByWorkspace(lastSyncTime, workspaceId)
        }
    }

    override suspend fun updatePoint(
        pointId: String,
        body: RequestBody
    ): Resource<ApiResponse<UpdatedPoint>> {
        return safeApiCall(context){
            retrofitProvider.getRetrofit().create(ApiService::class.java)
                .updatePointFields(pointId,body)
        }
    }

}