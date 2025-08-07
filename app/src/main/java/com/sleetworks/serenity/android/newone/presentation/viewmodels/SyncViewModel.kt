package com.sleetworks.serenity.android.newone.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sleetworks.serenity.android.newone.data.models.remote.response.ApiResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.WorkspaceResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.Share
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.site.Site
import com.sleetworks.serenity.android.newone.data.network.ApiException
import com.sleetworks.serenity.android.newone.data.network.Resource
import com.sleetworks.serenity.android.newone.domain.reporitories.CustomFieldRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.DataStoreRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.ShareRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.SiteRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.WorkspaceRemoteRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.WorkspaceRepository
import com.sleetworks.serenity.android.newone.presentation.common.UIEvent
import com.sleetworks.serenity.android.newone.utils.WORKSPACE_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SyncViewModel @Inject constructor(
    val workspaceRemoteRepository: WorkspaceRemoteRepository,
    val workspaceRepository: WorkspaceRepository,
    val siteRepository: SiteRepository,
    val customFieldRepository: CustomFieldRepository,
    val shareRepository: ShareRepository,
    val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val TAG: String = "SyncViewModel"
    private val _uiEvent = MutableSharedFlow<UIEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    private val _error: MutableStateFlow<String> = MutableStateFlow("")
    val error: StateFlow<String>
        get() = _error
    private val _message: MutableStateFlow<String> = MutableStateFlow("")
    val message: StateFlow<String>
        get() = _message
    private val _success: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val success: StateFlow<Boolean>
        get() = _success
    val syncList = arrayListOf(
        "workspaces" to suspend { workspaceRemoteRepository.getAllWorkspaces() },
        "sites" to suspend { workspaceRemoteRepository.getAllSites() },
        "share" to suspend { workspaceRemoteRepository.getAllShares() })

    init {
        Log.e(TAG, ": syncData")
        syncData()
    }

    fun syncData() {


        viewModelScope.launch(Dispatchers.IO) {
            try {
                var workspaceID = ""
                _message.emit("Syncing data...")
                Log.e(TAG, "syncData: start")

                val sitesDeferred = async {
                    workspaceRemoteRepository.getAllSites()
                }

                val workspacesDeferred = async {
                    workspaceRemoteRepository.getAllWorkspaces()
                }

                val sharesDeferred = async {
                    workspaceRemoteRepository.getAllShares()
                }

                _message.emit("Fetching workspaces...")
                val workspaces = workspacesDeferred.await()
                _message.emit("Fetching sites...")
                val sites = sitesDeferred.await()
                _message.emit("Fetching shares...")
                val shares = sharesDeferred.await()


                if (sites is Resource.Success<ApiResponse<List<Site>>> &&
                    workspaces is Resource.Success<ApiResponse<List<WorkspaceResponse>>> &&
                    shares is Resource.Success<ApiResponse<List<Share>>>
                ) {
                    _message.emit("Storing workspaces...")
                    workspaces.data.entity?.let {
                        it.forEachIndexed { index, workspaceResponse ->
                            workspaceRepository.insertWorkspaces(workspaceResponse.workspaces)
                        }
                        workspaceID = it.first().workspaces.first().id
                        storeWorkspaceID(workspaceID)
                    }

                    if (workspaceID.isEmpty()) {
                        _message.emit("Sync Failed")
                        _error.emit("Unexpected error")
                        _success.emit(false)
                        return@launch
                    }


                    _message.emit("Storing sites...")
                    sites.data.entity?.let { siteRepository.insertSites(it) }

                    _message.emit("Storing custom fields...")
                    sites.data.entity?.let {
                        it.forEachIndexed { index, site ->
                            site.customFields.let { customFields ->
                                customFieldRepository.insertCustomFields(
                                    customFields,
                                    site.workspaceRef.id
                                )
                            }
                        }
                    }


                    _message.emit("Storing shares...")
                    shares.data.entity?.let {
                        shareRepository.insertShares(it,workspaceID)
                    }

                    _message.emit("Sync complete")
                    _success.emit(true)


                } else if (sites is Resource.Error || workspaces is Resource.Error || shares is Resource.Error) {

                    val error = when {
                        sites is Resource.Error -> sites.apiException
                        workspaces is Resource.Error -> workspaces.apiException
                        shares is Resource.Error -> shares.apiException
                        else -> ApiException.UnknownException("Unexpected error")

                    }

                    _message.emit("Sync Failed")
                    if (error is ApiException.NetworkException) {
                        _error.emit(error.message ?: "Unexpected error")
                    } else {
                        _error.emit("Sync failed,Unexpected error")
                    }
                    _success.emit(false)
                    Log.e(TAG, "syncData: ", error)
                }

            } catch (e: Exception) {
                _message.emit("Sync Failed")
                _error.emit("Unexpected error: ${e.localizedMessage}")
                _success.emit(false)
                Log.e(TAG, "syncData: unexpected error", e)
            }


        }
    }


    suspend fun storeWorkspaceID(workspaceID: String) {

        if (workspaceID.isNotEmpty()) {
            dataStoreRepository.putString(WORKSPACE_ID, workspaceID)
        }

    }
}