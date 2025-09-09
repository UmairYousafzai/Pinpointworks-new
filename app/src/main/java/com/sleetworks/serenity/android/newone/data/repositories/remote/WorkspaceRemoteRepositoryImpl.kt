package com.sleetworks.serenity.android.newone.data.repositories.remote

import android.content.Context
import com.sleetworks.serenity.android.newone.data.datasource.remote.ApiService
import com.sleetworks.serenity.android.newone.data.models.remote.response.ApiResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.WorkspaceResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.Share
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.site.Site
import com.sleetworks.serenity.android.newone.data.network.Resource
import com.sleetworks.serenity.android.newone.data.network.RetrofitProvider
import com.sleetworks.serenity.android.newone.data.network.safeApiCall
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.WorkspaceRemoteRepository
import javax.inject.Inject

class WorkspaceRemoteRepositoryImpl @Inject constructor(
    val context: Context,
    val retrofitProvider: RetrofitProvider
) : WorkspaceRemoteRepository {


    override suspend fun getAllWorkspaces(): Resource<ApiResponse<List<WorkspaceResponse>>> {
        return safeApiCall(context) {
            retrofitProvider.getRetrofit().create(ApiService::class.java).getAllWorkspaces()
        }
    }

    override suspend fun getAllSites(): Resource<ApiResponse<List<Site>>> {
        return safeApiCall(context) {
            retrofitProvider.getRetrofit().create(ApiService::class.java).getAllSites()
        }
    }

    override suspend fun getAllShares(): Resource<ApiResponse<List<Share>>> {
        return safeApiCall(context) {
            retrofitProvider.getRetrofit().create(ApiService::class.java).getAllShares()
        }

    }

    override suspend fun getSiteByID(siteID: String): Resource<ApiResponse<Site>> {
        return safeApiCall(context) {
            retrofitProvider.getRetrofit().create(ApiService::class.java).getSite(siteID)
        }
    }
}