package com.sleetworks.serenity.android.newone.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sleetworks.serenity.android.newone.data.mappers.toEntity
import com.sleetworks.serenity.android.newone.data.network.ApiException
import com.sleetworks.serenity.android.newone.data.network.Resource
import com.sleetworks.serenity.android.newone.domain.reporitories.local.CustomFieldRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.DataStoreRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.ShareRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.SiteRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.UserRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.WorkspaceRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.WorkspaceRemoteRepository
import com.sleetworks.serenity.android.newone.presentation.common.UIEvent
import com.sleetworks.serenity.android.newone.utils.FIRST_SYNC
import com.sleetworks.serenity.android.newone.utils.SITE_ID
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
class FirstSyncViewModel @Inject constructor(
    val workspaceRemoteRepository: WorkspaceRemoteRepository,
    val workspaceRepository: WorkspaceRepository,
    val siteRepository: SiteRepository,
    val customFieldRepository: CustomFieldRepository,
    val shareRepository: ShareRepository,
    val userRepository: UserRepository,
    val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val TAG: String = "SyncViewModel"
    private val _uiEvent = MutableSharedFlow<UIEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    private val _loader: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loader: StateFlow<Boolean>
        get() = _loader
    private val _error: MutableStateFlow<String> = MutableStateFlow("")
    val error: StateFlow<String>
        get() = _error
    private val _message: MutableStateFlow<String> = MutableStateFlow("")
    val message: StateFlow<String>
        get() = _message
    private val _success: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val success: StateFlow<Boolean>
        get() = _success


    private suspend fun setErrorToEmpty() {
        _error.emit("")


    }

    fun navigateToPointListScreen() {
        viewModelScope.launch {
            _uiEvent.emit(UIEvent.Navigate("defect_list"))
        }
    }


    fun syncWorkspacesData() {


        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loader.emit(true)
                setErrorToEmpty()
                var workspaceID = ""
                _message.emit("Syncing data...")
                Log.d(TAG, "syncData: start")

                val sitesDeferred =
                    async {
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


                if (sites is Resource.Success &&
                    workspaces is Resource.Success &&
                    shares is Resource.Success
                ) {
                    _message.emit("Storing workspaces...")
                    workspaces.data.entity?.let {
                        it.forEachIndexed { index, workspaceResponse ->
                            workspaceRepository.insertWorkspaces(workspaceResponse.workspaces)
                        }
                        workspaceID = it.first().workspaces.first().id
                        storeWorkspaceIDAndSiteID(
                            workspaceID,
                            it.first().workspaces.first().siteRef.id
                        )

                    }



                    if (workspaceID.isEmpty()) {
                        _message.emit("Sync Failed")
                        _error.emit("Unexpected error")
                        _success.emit(false)
                        return@launch
                    }

                    val result = getAndStoreWorkSpaceUser(workspaceID)
                    if (!result) {
                        _message.emit("Sync Failed")
                        _error.emit("Unexpected error")
                        _success.emit(false)
                        return@launch
                    }

                    _message.emit("Storing sites...")
                    sites.data.entity?.let {
                        siteRepository.insertSites(it)

                    }

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
                        shareRepository.insertShares(it, workspaceID)
                    }

                    val workSpaceUsersDeferred = async {
                        workspaceRemoteRepository.getWorkSpaceUsers(workspaceID)
                    }

                    val workSpaceUsers = workSpaceUsersDeferred.await()
                    if (workSpaceUsers is Resource.Success) {
                        workSpaceUsers.data.entity?.let { users ->
                            users.map { it.toEntity(workspaceID) }

                        }

                    } else if (workSpaceUsers is Resource.Error) {

                        _message.emit("Sync Failed")
                        _error.emit("Unexpected error")
                        _success.emit(false)
                        return@launch
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
                _error.emit("Sync Failed, please try again.")
                _success.emit(false)
                Log.e(TAG, "syncData: unexpected error", e)
            }

            _loader.emit(false)

        }
    }

    suspend fun getAndStoreWorkSpaceUser(workspaceID: String): Boolean {

        val result = workspaceRemoteRepository.getWorkSpaceUsers(workspaceID)

        if (result is Resource.Success) {
            val users = result.data.entity
            users?.let {
                userRepository.insertUsers(it.map { item ->
                    item.toEntity(
                        workspaceID
                    )
                })
            }
            return true
        } else {
            return false
        }


    }

    suspend fun storeWorkspaceIDAndSiteID(workspaceID: String, siteID: String) {

        if (workspaceID.isNotEmpty()) {
            dataStoreRepository.putString(WORKSPACE_ID, workspaceID)
            dataStoreRepository.putString(SITE_ID, siteID)
        }

    }

    fun saveFirstSync() {
        viewModelScope.launch {
            dataStoreRepository.putBoolean(FIRST_SYNC, true)
        }

    }
}