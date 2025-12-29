package com.sleetworks.serenity.android.newone.presentation.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.primaverahq.videocompressor.VideoCompressor
import com.primaverahq.videocompressor.settings.CompressionSettings
import com.primaverahq.videocompressor.settings.EncoderSelectionMode
import com.sleetworks.serenity.android.newone.data.datasource.VideoStore
import com.sleetworks.serenity.android.newone.data.imageStore.UserImageStore
import com.sleetworks.serenity.android.newone.data.mappers.toDomain
import com.sleetworks.serenity.android.newone.data.mappers.toEntity
import com.sleetworks.serenity.android.newone.data.models.local.NewCustomField
import com.sleetworks.serenity.android.newone.data.models.local.OfflineFieldValue
import com.sleetworks.serenity.android.newone.data.models.local.entities.AssigneeEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.OfflineModifiedPointFields
import com.sleetworks.serenity.android.newone.data.models.local.entities.SiteEntity
import com.sleetworks.serenity.android.newone.data.models.remote.request.AddCommentRequest
import com.sleetworks.serenity.android.newone.data.models.remote.response.ApiResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.Header
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.CreatedBy
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.UpdatedBy
import com.sleetworks.serenity.android.newone.data.models.remote.response.comment.Comment
import com.sleetworks.serenity.android.newone.data.models.remote.response.comment.DefectRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.comment.Reaction
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.PointCustomField
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Video
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.WorkspaceRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.TargetRef
import com.sleetworks.serenity.android.newone.data.network.NetworkUtil
import com.sleetworks.serenity.android.newone.data.network.Resource
import com.sleetworks.serenity.android.newone.data.storage.InternalWorkspaceStorageUtils
import com.sleetworks.serenity.android.newone.domain.mapper.toDomain
import com.sleetworks.serenity.android.newone.domain.models.CommentDomain
import com.sleetworks.serenity.android.newone.domain.models.point.PointDomain
import com.sleetworks.serenity.android.newone.domain.models.share.ShareDomainModel
import com.sleetworks.serenity.android.newone.domain.reporitories.local.CommentLocalRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.CustomFieldRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.DataStoreRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.PointRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.ReactionLocalRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.ShareRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.SiteRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.UserRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.WorkspaceRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.CommentRemoteRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.ImageRemoteRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.PointRemoteRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.VideoRemoteRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.WorkspaceRemoteRepository
import com.sleetworks.serenity.android.newone.domain.usecase.SyncPointImageUseCase
import com.sleetworks.serenity.android.newone.presentation.common.toUiModel
import com.sleetworks.serenity.android.newone.presentation.model.CustomFieldTempUI
import com.sleetworks.serenity.android.newone.presentation.model.LocalImage
import com.sleetworks.serenity.android.newone.presentation.model.UserUiModel
import com.sleetworks.serenity.android.newone.presentation.ui.model.CustomFieldType
import com.sleetworks.serenity.android.newone.presentation.ui.model.DefectFieldType
import com.sleetworks.serenity.android.newone.presentation.ui.model.PointItemPriority
import com.sleetworks.serenity.android.newone.presentation.ui.model.PointItemStatus
import com.sleetworks.serenity.android.newone.utils.CONSTANTS
import com.sleetworks.serenity.android.newone.utils.SAVE_IMAGE_TO_EXTERNAL_STORAGE
import com.sleetworks.serenity.android.newone.utils.SAVE_VIDEO_TO_EXTERNAL_STORAGE
import com.sleetworks.serenity.android.newone.utils.convertToOfflineFieldValue
import com.sleetworks.serenity.android.newone.utils.readExifDataAsJson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.destination
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PointDetailViewModel @Inject constructor(
    private val commentRepository: CommentRemoteRepository,
    private val commentLocalRepository: CommentLocalRepository,
    private val reactionLocalRepository: ReactionLocalRepository,
    private val workspaceRepository: WorkspaceRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val pointRepository: PointRepository,
    private val pointRemoteRepository: PointRemoteRepository,
    private val userRepository: UserRepository,
    private val siteRepository: SiteRepository,
    private val customFieldTemplateRepo: CustomFieldRepository,
    private val workspaceRemoteRepository: WorkspaceRemoteRepository,
    private val shareRepo: ShareRepository,
    private val userImageStore: UserImageStore,
    private val videoStore: VideoStore,
    private val syncPointImageUseCase: SyncPointImageUseCase,
    private val imageRemoteRepository: ImageRemoteRepository,
    private val videoRemoteRepository: VideoRemoteRepository,
    private val savedStateHandle: SavedStateHandle,
    @ApplicationContext
    val context: Context,
) :
    ViewModel() {
    val TAG = "PointDetailViewModel"
    private val refreshPoint = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    private val _error: MutableStateFlow<String> = MutableStateFlow("")
    val error: StateFlow<String>
        get() = _error

    private val _mainLoader: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val mainLoader: StateFlow<Boolean>
        get() = _mainLoader

    private val _fieldLoader: MutableStateFlow<Pair<String, Boolean>> =
        MutableStateFlow(("" to false))
    val fieldLoader: StateFlow<Pair<String, Boolean>>
        get() = _fieldLoader


    private val _fieldSuccess: MutableStateFlow<Pair<String, Boolean>> =
        MutableStateFlow(("" to false))
    val fieldSuccess: StateFlow<Pair<String, Boolean>>
        get() = _fieldSuccess


    private val _pointID: MutableStateFlow<String> = MutableStateFlow("")
    val pointID: StateFlow<String>
        get() = _pointID
    private val _message: MutableStateFlow<String> = MutableStateFlow("")
    val message: StateFlow<String>
        get() = _message

    private val _workspaceID = dataStoreRepository.workspaceIDFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
    val workspaceID
        get() = _workspaceID


    private val _workspaceUser: StateFlow<List<AssigneeEntity>> =
        _workspaceID.filterNot {
            it.isNullOrEmpty()
        }
            .distinctUntilChanged()
            .map {
                userRepository.getUserByWorkspaceId(it ?: "")
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    val workspaceUser
        get() = _workspaceUser

    private val _user = dataStoreRepository.user.filterNotNull().map { user ->
        UserUiModel(
            id = user.id,
            user.username,
            user.isLogin,
            user.email,
            user.password,
            user.imageID
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserUiModel()
    )

    val user: StateFlow<UserUiModel>
        get() = _user
    private val _point: StateFlow<PointDomain> =
        combine(
            _pointID.filter { it.isNotBlank() },
            refreshPoint.onStart { emit(Unit) } // initial load
        ) { pointId, _ ->
            pointId
        }
            .flatMapLatest { pointId ->
                pointRepository.getPointByIDFlow(
                    pointId
                )
            }
            .map { point ->

                val pointDomain = point.toDomain()
                _pointCustomFieldByTemplateId.value =
                    pointDomain.customFieldSimplyList.associateBy { it.customFieldTemplateId }
                val workSpaceUser = userRepository.getUserByWorkspaceId(_workspaceID.value ?: "")


                val pointUser = workSpaceUser.filter { it.id in pointDomain.assigneeIds }
                pointDomain.assignees.clear()
                pointDomain.assignees.addAll(pointUser)
                val sortComments =
                    pointDomain.comments.sortedByDescending {
                        it.header?.updatedEpochMillis ?: it.addedTime
                    }
                pointDomain.comments = ArrayList(sortComments)
                pointDomain
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = PointDomain()
            )
    val point
        get() = _point

    private val _pointCustomFieldByTemplateId: MutableStateFlow<Map<String, PointCustomField>> =
        MutableStateFlow(emptyMap())

    val pointCustomFieldByTemplateId: StateFlow<Map<String, PointCustomField>>
        get() = _pointCustomFieldByTemplateId

    private val _offlinePointFields: StateFlow<List<OfflineModifiedPointFields>> =
        _pointID.filterNot {
            it.isBlank()
        }
            .distinctUntilChanged()
            .flatMapLatest { pointId ->
                pointRepository.getAllModifiedFieldsFlow(
                    pointId
                )
            }
            .map {
                it
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    val offlinePointFields
        get() = _offlinePointFields

    private val _site: StateFlow<SiteEntity?> =
        workspaceID.filterNot {
            it?.isBlank() == true
        }
            .distinctUntilChanged()
            .flatMapLatest { workspaceID ->
                siteRepository.getSiteByIDFlow(workspaceID ?: "")
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )
    val site
        get() = _site

    private val _share: StateFlow<ShareDomainModel?> =
        workspaceID.filterNot {
            it?.isBlank() == true
        }
            .distinctUntilChanged()
            .flatMapLatest { workspaceID ->
                shareRepo.getShareByWorkspaceIDFlow(workspaceID ?: "")
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )
    val share
        get() = _share


    private val _customFieldTemplates: StateFlow<List<CustomFieldTempUI>> =
        workspaceID.filterNot {
            it?.isBlank() == true
        }
            .distinctUntilChanged()
            .flatMapLatest { workspaceID ->
                customFieldTemplateRepo.getCustomFieldByWorkspaceID(workspaceID ?: "")
            }
            .map {

                it.map { item -> item.toUiModel() }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    val customFieldTemplates
        get() = _customFieldTemplates

    init {

        viewModelScope.launch(Dispatchers.IO) {
            _pointID.value = savedStateHandle.get<String>("pointId") ?: ""

        }
        _point.onEach {
            if (it.id.isNotEmpty()) {
                checkAndSyncImages()
            }
        }.launchIn(viewModelScope)


    }

    fun refreshPoint() {
        refreshPoint.tryEmit(Unit)
    }

    fun setError(message: String = "") {

        _error.value = message
    }


    fun updatePointFields(
        fields: Map<String, Any>,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            updatePoint(fields)
        }
    }

    suspend fun updatePoint(
        fields: Map<String, Any>,
        customField: PointCustomField? = null,
        isManualSync: Boolean = false
    ) {
        val firstKey = fields.entries.first().key
        try {
            if (customField != null) {
                _fieldLoader.value = customField.customFieldTemplateId to true
            } else if (!isManualSync) {
                _fieldLoader.value = firstKey to true
            }
            val jsonObject = JSONObject()
            fields.forEach { item ->


                //custom Fields
                if (item.key == DefectFieldType.CUSTOM_FIELDS) {
                    val cfList: ArrayList<NewCustomField> =
                        item.value as ArrayList<NewCustomField>
                    val parentArray = JSONArray()

                    for (customField in cfList) {
                        //single object
                        val jsonObjectsContainer = JSONObject()
                        jsonObjectsContainer.put(
                            CONSTANTS.CF_TEMPLATE,
                            customField.customFieldTemplateId
                        )


                        //array of sub values
                        if (customField.subValues != null && !customField.subValues!!.isEmpty()) {
                            val subFieldsArray = JSONArray()
                            for (subListOfTotal in customField.subValues) {
                                val childObject = JSONObject()
                                childObject.put("id", subListOfTotal.id)
                                childObject.put("value", subListOfTotal.value)
                                childObject.put("description", subListOfTotal.description)
                                subFieldsArray.put(childObject)
                            }

                            jsonObjectsContainer.put("subValues", subFieldsArray)
                        } else {
                            if (customField.isSubValueActive == true) {
                                //if subfield with subList Length 0
                                jsonObjectsContainer.put("subValues", JSONArray())
                            } else if (customField.selectedItemIds != null && customField.type == CustomFieldType.MULTI_LIST.value) {
                                //initialing json array
                                val selectedItemArray = JSONArray()
                                if (customField.selectedItemIds?.isNotEmpty() == true) {
                                    for (value in customField.selectedItemIds) {
                                        selectedItemArray.put(value)
                                    }
                                }

                                jsonObjectsContainer.put("selectedItemIds", selectedItemArray)
                            } else {
                                //simple field
                                jsonObjectsContainer.put(
                                    CONSTANTS.VALUE,
                                    customField.value
                                )
                            }
                        }

                        //parent field Object
                        parentArray.put(jsonObjectsContainer)
                    }


                    // Add the JSON object to the main object or array as needed
                    jsonObject.put(item.key, parentArray)
                } else if (item.value is Set<*> ||
                    item.value is List<*> ||
                    item.value is MutableCollection<*> ||
                    item.value is LinkedHashSet<*>
                ) {
                    val entityArray: JSONArray = JSONArray(
                        Gson().toJson(item.value)
                    )
                    jsonObject.put(item.key, entityArray)
                    // For title, description, priority and status
                } else if (item.value is String
                    || item.value is PointItemStatus
                    || item.value is PointItemPriority
                    || item.key == DefectFieldType.RED_FLAG
                ) {
                    jsonObject.put(item.key, item.value)

                    // Generic fallback for unhandled types
                } else {
                    jsonObject.put(item.key, item.value)
                }


            }

            if (NetworkUtil.isInternetAvailable(context)) {


                val jsonMediaType = "application/json; charset=utf-8".toMediaType()
                val body = RequestBody.create(
                    jsonMediaType,
                    jsonObject.toString()
                )

                val result = pointRemoteRepository.updatePoint(_pointID.value, body)

                when (result) {
                    is Resource.Success -> {
                        val updatedPoint = result.data.entity
                        if (updatedPoint != null && updatedPoint.updatedPoint != null) {
                            pointRepository.insertPoint(updatedPoint.updatedPoint!!)
                            if (customField != null) {
                                showSuccess(customField.customFieldTemplateId)
                            } else if (!isManualSync) {
                                showSuccess(firstKey)
                            }
                            deleteModifiedFields()
                        } else {
                            updatePointLocally(_point.value, fields, customField)
                            _error.emit("Unknown Error")
                        }

                    }

                    is Resource.Error -> {
                        updatePointLocally(_point.value, fields, customField)
                        _error.emit(result.apiException.message ?: "Unknown Error")

                    }

                    Resource.Loading -> {

                    }
                }

            } else {
                updatePointLocally(_point.value, fields, customField)
            }


        } catch (e: Exception) {
            Log.e(TAG, "updatePoint: ", e)
        }
        if (customField != null) {
            _fieldLoader.value = customField.customFieldTemplateId to false
        } else if (!isManualSync) {
            _fieldLoader.value = firstKey to false
        }


    }


    suspend fun updatePointLocally(
        currentPoint: PointDomain,
        fields: Map<String, Any>,
        customField: PointCustomField? = null
    ) {

        val newCustomFields = fields[DefectFieldType.CUSTOM_FIELDS] as? List<NewCustomField>
        val customFieldKey = customField?.customFieldTemplateId ?: ""
        val updatedPoint = currentPoint.copy(

            customFieldSimplyList = if (!newCustomFields.isNullOrEmpty()) {
                val updatedList = currentPoint.customFieldSimplyList

                newCustomFields.forEach { newField ->
                    val existingFieldIndex = updatedList.indexOfFirst {
                        it.customFieldTemplateId == newField.customFieldTemplateId
                    }

                    if (existingFieldIndex != -1) {

                        val oldField = updatedList[existingFieldIndex]
                        if (CustomFieldType.fromValue(oldField.type) == CustomFieldType.LIST) {


                            updatedList[existingFieldIndex] = oldField.copy(
                                idOfChosenElement = fields[DefectFieldType.CUSTOM_FIELDS] as? String
                                    ?: oldField.idOfChosenElement
                            )
                        } else if (CustomFieldType.fromValue(oldField.type) == CustomFieldType.MULTI_LIST) {
                            updatedList[existingFieldIndex] = oldField.copy(
                                selectedItemIds = fields[DefectFieldType.CUSTOM_FIELDS] as? List<String>
                                    ?: oldField.selectedItemIds
                            )
                        } else {
                            updatedList[existingFieldIndex] = oldField.copy(
                                value = fields[DefectFieldType.CUSTOM_FIELDS] as? String
                                    ?: oldField.value
                            )
                        }
                    } else {

                        updatedList.add(customField!!)
                    }
                }
                updatedList
            } else {
                currentPoint.customFieldSimplyList
            },
            title = if (fields.containsKey(DefectFieldType.TITLE))
                fields[DefectFieldType.TITLE] as? String ?: currentPoint.title
            else currentPoint.title,

            description = if (fields.containsKey(DefectFieldType.DESCRIPTION))
                fields[DefectFieldType.DESCRIPTION] as? String ?: currentPoint.description
            else currentPoint.description,

            descriptionRich = if (fields.containsKey(DefectFieldType.DESCRIPTIONRICH))
                fields[DefectFieldType.DESCRIPTIONRICH] as? String ?: currentPoint.descriptionRich
            else currentPoint.descriptionRich,

            priority = if (fields.containsKey(DefectFieldType.PRIORITY))
                fields[DefectFieldType.PRIORITY] as? String ?: currentPoint.priority
            else currentPoint.priority,

            status = if (fields.containsKey(DefectFieldType.STATUS))
                fields[DefectFieldType.STATUS] as? String ?: currentPoint.status
            else currentPoint.status,

            flagged = if (fields.containsKey(DefectFieldType.RED_FLAG))
                fields[DefectFieldType.RED_FLAG] as? Boolean ?: currentPoint.flagged
            else currentPoint.flagged,

            isModified = true, // Mark as modified for offline sync
            edited = true,
            updatedAt = System.currentTimeMillis(),
            assigneeIds = if (fields.containsKey(DefectFieldType.ASSIGNEES)) ArrayList(fields[DefectFieldType.ASSIGNEES] as List<String>)
                ?: currentPoint.assigneeIds else currentPoint.assigneeIds,
            tags = if (fields.containsKey(DefectFieldType.TAGS)) ArrayList(fields[DefectFieldType.TAGS] as List<String>)
                ?: currentPoint.tags else currentPoint.tags,

            )
        fields.forEach { item ->

            val convertedValue = item.value.convertToOfflineFieldValue()
            pointRepository.insertModifiedField(
                OfflineModifiedPointFields(
                    workspaceId = _workspaceID.value ?: "",
                    pointId = _pointID.value,
                    field = if (DefectFieldType.MENTIONS != item.key) customFieldKey.ifEmpty { item.key } else item.key,
                    type = "",
                    value = convertedValue
                )
            )
        }

        pointRepository.insertPoint(updatedPoint)

    }

    fun showSuccess(key: String) {
        _fieldSuccess.value = key to true

        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            _fieldSuccess.value = key to false

        }

    }

    fun deleteModifiedFields() {
        viewModelScope.launch(Dispatchers.IO) {
            pointRepository.deleteModifiedFieldByPointId(_pointID.value)
        }
    }

    fun syncPointDetail() {
        viewModelScope.launch(Dispatchers.IO) {
               Log.e("Point Sync", "Sync Start" )
            syncPoint()
            Log.e("Point Sync", "Sync complete" )

        }
    }

    suspend fun syncPoint() {
        _mainLoader.value = true
        if (NetworkUtil.isInternetAvailable(context)) {

            syncOfflineData()

            coroutineScope {
                val site = _site.value
                val workspaceID = workspaceID.value
                val pointID = pointID.value

                if (site != null && workspaceID != null) {

                    val siteDeferred = async {
                        workspaceRemoteRepository.getSiteByID(site.id)
                    }

                    val pointDeferred = async {
                        pointRemoteRepository.getPointDetail(pointID, workspaceID)
                    }

                    val commentDeferred = async {
                        commentRepository.getPointComments(pointID)
                    }
                    val commentReactionDeferred = async {
                        commentRepository.getPointCommentsReaction(pointID)
                    }
                    val workspaceUserDeferred = async {
                        workspaceRemoteRepository.getWorkSpaceUsers(workspaceID)
                    }

                    val siteResult = siteDeferred.await()
                    val pointResult = pointDeferred.await()
                    val commentResult = commentDeferred.await()
                    val commentReactionResult = commentReactionDeferred.await()
                    val workspaceUserResult = workspaceUserDeferred.await()

                    if (
                        siteResult is Resource.Success
                        && pointResult is Resource.Success
                        && commentResult is Resource.Success
                        && commentReactionResult is Resource.Success
                        && workspaceUserResult is Resource.Success
                    ) {

                        val site = siteResult.data.entity
                        val point = pointResult.data.entity
                        val comments = commentResult.data.entity
                        val commentReactions = commentReactionResult.data.entity
                        val workspaceUsers = workspaceUserResult.data.entity
                        if (site != null && point != null && comments != null && commentReactions != null && workspaceUsers != null) {
                            siteRepository.insertSite(site)
                            pointRepository.insertPoint(point)
                            commentLocalRepository.insertComments(comments)
                            reactionLocalRepository.insertReactions(commentReactions)
                            userRepository.insertUsers(workspaceUsers.map { item ->
                                item.toEntity(
                                    workspaceID
                                )
                            })

                        } else {
                            _error.value =
                                "Unfortunately a problem occurred and the sync could not be completed. Please check your network connection and try again."
                        }


                    } else if (siteResult is Resource.Error
                        || pointResult is Resource.Error
                        || commentResult is Resource.Error
                        || commentReactionResult is Resource.Error
                        || workspaceUserResult is Resource.Error
                    ) {
                        _error.value =
                            "Unfortunately a problem occurred and the sync could not be completed. Please check your network connection and try again."
                    }

                }
            }


        } else {
//                _error.value = "No Internet Connection"
        }
        _mainLoader.value = false


    }


    suspend fun syncOfflineData() {
        if (_offlinePointFields.value.isNotEmpty()) {
            val cfList = arrayListOf<NewCustomField>()
            val pointDetailsMap = mutableMapOf<String, Any>()
            val commentReactions = mutableListOf<Pair<Int?, Reaction>>()
            val comments = mutableListOf<Pair<Int?, CommentDomain>>()
            val images = mutableListOf<LocalImage>()
            val videos = mutableListOf<Pair<String, File>>()
            val deleteImageIds = mutableListOf<String>()
            val deleteVideoIds = mutableListOf<String>()
            _offlinePointFields.value.forEach { item ->
                when (item.value) {
                    is OfflineFieldValue.NewCustomFieldValue -> {
                        Log.d(TAG, "syncOfflineData: Custom Field")
                        cfList.addAll(item.value.getValue() as List<NewCustomField>)
                    }

                    is OfflineFieldValue.CommentReactionValue -> {
                        Log.d(TAG, "syncOfflineData: Comment Reaction")

                        commentReactions.add(item.id to item.value.getValue() as Reaction)
                    }

                    is OfflineFieldValue.CommentValue -> {

                        comments.add(item.id to item.value.getValue() as CommentDomain)
                    }

                    is OfflineFieldValue.ImageListValue -> {
                        images.addAll(item.value.getValue() as List<LocalImage>)
                    }

                    is OfflineFieldValue.VideoListValue -> {
                        val videoList = item.value.getValue() as List<Video>
                        videoList.forEach { video ->
                            val videoFile = videoStore.getOriginalFileReference(
                                workspaceID.value!!,
                                video.id?:"",
                            )
                            if (videoFile.exists()) {
                                videos.add(video.id!! to videoFile)
                            }
                        }
                    }

                    else -> {
                        Log.d(TAG, "syncOfflineData: Point Detail")
                        when (item.type) {
                            (DefectFieldType.DELETED_IMAGES + _pointID.value) -> {
                                deleteImageIds.add(item.field)
                            }

                            (DefectFieldType.DELETED_VIDEOS + _pointID.value) -> {
                                deleteVideoIds.add(item.field)
                            }

                            else -> {
                                pointDetailsMap.put(
                                    item.field,
                                    item.value.getValue()
                                )
                            }
                        }

                    }
                }

            }
            if (deleteImageIds.isNotEmpty()) {
                deleteImages(deleteImageIds)
            }
            if (deleteVideoIds.isNotEmpty()) {
                deleteVideos(deleteVideoIds)
            }
            if (commentReactions.isNotEmpty()) {
                updateLocalReactions(commentReactions)
            }
            if (comments.isNotEmpty()) {
                updateLocalComments(comments)
            }
            if (images.isNotEmpty()) {
                val storageUtils = InternalWorkspaceStorageUtils()

                uploadImages(
                    images.map {
                        val originalFile = storageUtils.getFileReference(
                            workspaceID.value!!,
                            "${workspaceID.value}/images/original/",
                            "${it.id}.jpg",
                            context
                        )
                        it.id to originalFile
                    },
                    false
                )
            }

            if (videos.isNotEmpty()) {
                uploadVideos(videos, false)
            }

            if (cfList.isNotEmpty()) {
                pointDetailsMap[DefectFieldType.CUSTOM_FIELDS] = cfList
            }
            if (pointDetailsMap.isNotEmpty()) {
                updatePoint(pointDetailsMap, isManualSync = true)
            }
        }

    }

    suspend fun deleteImages(ids: List<String>) {
        coroutineScope {

            ids.chunked(10).forEach { chunk ->

                chunk.map { imageId ->
                    async {
                        imageRemoteRepository.removeImage(_pointID.value, imageId)
                        pointRepository.deleteModifiedFieldByFieldValue(imageId)

                    }
                }.awaitAll()

            }

        }
    }

    suspend fun deleteVideos(ids: List<String>) {
        coroutineScope {

            ids.chunked(10).forEach { chunk ->

                chunk.map { videoId ->
                    async {
                        videoRemoteRepository.deleteVideo(_pointID.value, videoId)
                        pointRepository.deleteModifiedFieldByFieldValue(videoId)
                    }
                }.awaitAll()

            }

        }
    }

    fun updateCustomField(
        customFieldTempUI: CustomFieldTempUI,
        pointCustomField: PointCustomField,
        mentions: List<String> = emptyList()
    ) {


        val cfArray = arrayListOf<NewCustomField>()
        val customField = NewCustomField()

        val customFieldType = CustomFieldType.fromValue(pointCustomField.type)
        customField.customFieldTemplateId = customFieldTempUI.id.toString()
        customField.type = customFieldTempUI.type

//        if (customFieldType == CustomFieldType.TIME) {
//            customField.value = pointCustomField.addedTimeValue ?: ""
//
////            if (NetworkUtil.isInternetAvailable(context)) {
////                val oldValue = filledCustomField.getValue().toLong()
////                val addedValue = filledCustomField.getAddedTimeValue().toLong()
////                filledCustomField.setValue((oldValue + addedValue).toString())
////                filledCustomField.setAddedTimeValue("0")
////            }
//        }
        if (customFieldType == CustomFieldType.LIST) {
            customField.value = pointCustomField.idOfChosenElement ?: ""
        } else if (customFieldType == CustomFieldType.MULTI_LIST) {
            customField.selectedItemIds = pointCustomField.selectedItemIds
            customField.type = pointCustomField.type
        } else {
            customField.value = pointCustomField.value
        }


        if (customField.isSubValueActive == true) {

            if (pointCustomField.subValues != null && !pointCustomField.subValues.isEmpty()) {
                customField.subValues = pointCustomField.subValues
            } else {
                customField.subValues = emptyList()
            }

            customField.isSubValueActive = true

        }

        cfArray.add(customField)
        viewModelScope.launch(Dispatchers.IO) {
            updatePoint(
                if (customFieldType == CustomFieldType.RICHTEXT) mapOf(
                    DefectFieldType.CUSTOM_FIELDS to cfArray, DefectFieldType.MENTIONS to mentions
                )
                else mapOf(DefectFieldType.CUSTOM_FIELDS to cfArray),
                customField = pointCustomField
            )
        }


    }

    fun checkAndSyncImages() {
        viewModelScope.launch(Dispatchers.IO) {
            _workspaceID.first { !it.isNullOrEmpty() }

            workspaceID.value?.let { syncPointImageUseCase(it, point.value, _mainLoader) }
        }
    }

    fun getUserAvatar(imageID: String): File? {
        return userImageStore.getAvatarFile(imageID)
    }

    suspend fun updateLocalReactions(reactions: MutableList<Pair<Int?, Reaction>>) {

        coroutineScope {

            reactions.map { reactionPair ->
                async {
                    val id = reactionPair.second.like.find {
                        it == _user.value.id
                    }
                    addReaction(reactionPair.second, id == null)
                    reactionPair.first?.let { pointRepository.deleteModifiedFieldById(it) }
                }
            }.awaitAll()

        }
    }

    suspend fun updateLocalComments(comments: MutableList<Pair<Int?, CommentDomain>>) {

        coroutineScope {

            comments.map { commentPair ->
                async {

                    addComment(
                        commentPair.second.comment,
                        commentPair.second.commentRich,
                        commentPair.second.mentions ?: emptyList(),
                        false
                    )
                    commentPair.first?.let { pointRepository.deleteModifiedFieldById(it) }

                }
            }.awaitAll()

        }
    }

    fun updateReaction(comment: CommentDomain, shouldAdd: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val uuid: String = UUID.randomUUID().toString()
            val oldReaction = comment.reactions
            val newReaction =
                if (comment.reactions != null) {
                    val oldLikes = ArrayList(comment.reactions.like)

                    if (!shouldAdd) {
                        oldLikes.add(_user.value.id)
                    } else {
                        oldLikes.remove(_user.value.id)

                    }
                    oldReaction.like = oldLikes
                    oldReaction
                } else Reaction(
                    id = uuid,
                    header = Header(
                        createdBy = CreatedBy(
                            _user.value.username,
                            _user.value.id,
                            "",
                            _user.value.imageID
                        ),
                        createdEpochMillis = System.currentTimeMillis(),
                        createdUserAgent = "",
                        updatedBy = UpdatedBy(
                            _user.value.username,
                            _user.value.id,
                            _user.value.imageID,
                            ""
                        ),
                        updatedEpochMillis = System.currentTimeMillis(),
                        updatedUserAgent = ""
                    ),
                    like = if (!shouldAdd) listOf(_user.value.id) else emptyList(),
                    tags = emptyList(),
                    targetRef = TargetRef(
                        type = "",
                        caption = "",
                        id = comment.id
                    ),
                    type = "",
                    commentId = comment.id

                )
            addReaction(newReaction, shouldAdd)
        }
    }

    suspend fun addReaction(reaction: Reaction, shouldAdd: Boolean) {

        if (NetworkUtil.isInternetAvailable(context)) {
            try {
                val result = commentRepository.updateCommentReaction(
                    reaction.targetRef?.id ?: reaction.commentId,
                    shouldAdd
                )

                when (result) {
                    is Resource.Error -> {
                        _error.value = result.apiException.message ?: "Unknown Error"
                    }

                    Resource.Loading -> {

                    }

                    is Resource.Success<ApiResponse<Reaction>> -> {

                        val newReaction = result.data.entity


                        if (newReaction != null) {
                            reactionLocalRepository.insertReaction(newReaction)
                        } else {
                            _error.value = "Unknown Error"
                            saveOfflineReaction(reaction)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "addReaction: ", e)
            }

        } else {

            saveOfflineReaction(reaction)
        }


    }

    suspend fun saveOfflineReaction(reaction: Reaction) {
        val convertedValue = reaction.convertToOfflineFieldValue()
        pointRepository.insertModifiedField(
            OfflineModifiedPointFields(
                workspaceId = _workspaceID.value ?: "",
                pointId = _pointID.value,
                field = reaction.id,
                type = DefectFieldType.REACTION,
                value = convertedValue
            )
        )

        reactionLocalRepository.insertReaction(reaction)
        refreshPoint()


    }

    fun addNewComment(
        comment: String,
        richComment: String,
        mentions: List<String>,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _mainLoader.value = true
            addComment(comment, richComment, mentions)
            _mainLoader.value = false

        }

    }

    suspend fun addComment(
        comment: String,
        richComment: String,
        mentions: List<String>,
        shouldSaveLocally: Boolean = true
    ) {


        try {
            val user = dataStoreRepository.getUser()
            val point = _point.value

            user?.let {
                val commentRequest = AddCommentRequest(
                    comment = comment,
                    commentRich = richComment,
                    mentions = mentions,
                    author = it,
                    defectRef = DefectRef(
                        caption = point.title,
                        id = point.id,
                        type = "DefectType",
                    ),
                    workspaceRef = WorkspaceRef(
                        id = _workspaceID.value ?: "",
                        type = "WorkspaceRef",
                        caption = ""
                    )
                )

                if (NetworkUtil.isInternetAvailable(context)) {
                    hitCommentEndPoint(point.id, commentRequest, shouldSaveLocally)
                } else {
                    if (shouldSaveLocally) {
                        saveOfflineComment(commentRequest.toDomain())
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "addComment: ", e)
        }


    }

    suspend fun hitCommentEndPoint(
        pointId: String,
        commentRequest: AddCommentRequest,
        shouldSaveLocally: Boolean = true
    ) {
        val result = commentRepository.addComment(pointId, commentRequest)

        when (result) {
            is Resource.Error -> {
                if (shouldSaveLocally) {
                    saveOfflineComment(commentRequest.toDomain())
                }
                _error.value = result.apiException.message ?: "Unknown Error"
            }

            Resource.Loading -> {

            }

            is Resource.Success<ApiResponse<Comment>> -> {

                val comment = result.data.entity
                if (comment != null) {
                    commentLocalRepository.insertComment(comment.toEntity())
                    refreshPoint()
                } else {
                    if (shouldSaveLocally) {
                        saveOfflineComment(commentRequest.toDomain())
                    }
                    _error.value = "Unknown Error"

                }
            }
        }
    }

    suspend fun saveOfflineComment(comment: CommentDomain) {
        val convertedValue = comment.convertToOfflineFieldValue()
        pointRepository.insertModifiedField(
            OfflineModifiedPointFields(
                workspaceId = _workspaceID.value ?: "",
                pointId = _pointID.value,
                field = comment.id,
                type = DefectFieldType.COMMENT,
                value = convertedValue,
            )
        )
        commentLocalRepository.insertComment(comment.toEntity())
        refreshPoint()


    }

    fun shouldSaveImageTOExternalStorage(flag: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.putBoolean(SAVE_IMAGE_TO_EXTERNAL_STORAGE, flag)
        }
    }

    fun shouldSaveVideoTOExternalStorage(flag: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.putBoolean(SAVE_VIDEO_TO_EXTERNAL_STORAGE, flag)
        }
    }

    suspend fun shouldSaveImageToExternalStorage(): Boolean {
        return dataStoreRepository.getBoolean(SAVE_IMAGE_TO_EXTERNAL_STORAGE)
    }

    suspend fun shouldSaveVideoToExternalStorage(): Boolean {
        return dataStoreRepository.getBoolean(SAVE_VIDEO_TO_EXTERNAL_STORAGE)
    }

    fun handleImageResult(uris: List<Uri>) {
        if (uris.isEmpty() || workspaceID.value == null || point.value.id.isEmpty()) {
            _error.value = "Unable to process images"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _mainLoader.value = true
            try {
                val storageUtils = InternalWorkspaceStorageUtils()
                val newImages = mutableListOf<Pair<String, File>>()
                var firstImageId: String? = null

                uris.forEachIndexed { index, uri ->
                    try {
                        val imageId = UUID.randomUUID().toString()
                        if (firstImageId == null) {
                            firstImageId = imageId
                        }

                        // Create temporary file from URI for compressor
                        val tempFile = File.createTempFile("temp_image_", ".jpg", context.cacheDir)
                        context.contentResolver.openInputStream(uri)?.use { inputStream ->
                            FileOutputStream(tempFile).use { outputStream ->
                                inputStream.copyTo(outputStream)
                            }
                        } ?: throw Exception("Unable to read image file")

                        // Compress image using zelory compressor (preserves EXIF automatically)
                        val originalFile = storageUtils.getFileReference(
                            workspaceID.value!!,
                            "${workspaceID.value}/images/original/",
                            "$imageId.jpg",
                            context
                        )

                        // Compress with max 2048px resolution and 85% quality
                        Compressor.compress(context, tempFile) {
                            resolution(2048, 2048)
                            quality(85)
                            format(Bitmap.CompressFormat.JPEG)
                            destination(originalFile)
                        }

                        // Create thumbnail using compressor (max 400px)
                        val thumbnailFile = storageUtils.getFileReference(
                            workspaceID.value!!,
                            "${workspaceID.value}/images/thumb/",
                            "$imageId.jpg",
                            context
                        )

                        Compressor.compress(context, tempFile) {
                            resolution(400, 400)
                            quality(90)
                            format(Bitmap.CompressFormat.JPEG)
                            destination(thumbnailFile)
                        }

                        // Clean up temporary file
                        tempFile.delete()

                        val localImage = LocalImage(
                            id = imageId,
                            caption = "Image ${index + 1}",
                            type = "image",
                            imageLocalPath = originalFile.absolutePath
                        )
                        newImages.add(imageId to originalFile)

                    } catch (e: Exception) {
                        Log.e(TAG, "Error processing image: ${e.message}", e)
                    }
                }

                uploadImages(newImages, true)


            } catch (e: Exception) {
                Log.e(TAG, "Error handling images: ${e.message}", e)

            }
            _mainLoader.value = false
        }
    }

    suspend fun uploadImages(newImages: List<Pair<String, File>>, shouldSaveLocally: Boolean) {

        val notUploadedImageIds = arrayListOf<Pair<String, File>>()
        coroutineScope {
            if (NetworkUtil.isInternetAvailable(context)) {
                val results = newImages.map { image ->
                    async {
                        val exifData = image.second.readExifDataAsJson()

                        imageRemoteRepository.uploadPointImagesQuick(
                            workspaceId = workspaceID.value!!,
                            pointId = point.value.id,
                            pointCaption = _point.value.title,
                            caption = "Image ${image.first + 1}",
                            exifData = exifData,
                            imageFile = image.second
                        )

                    }
                }.awaitAll()

                results.forEachIndexed { index, result ->

                    when (result) {
                        is Resource.Error -> {
                            notUploadedImageIds.add(newImages[index])


                        }

                        Resource.Loading -> {
                        }

                        is Resource.Success<ApiResponse<List<String>>> -> {
                            pointRepository.deleteModifiedFieldByFieldValue(DefectFieldType.IMAGES + _point.value.id)
                        }
                    }

                }
                if (shouldSaveLocally) {
                    syncPoint()
                }

                if (notUploadedImageIds.isNotEmpty() && shouldSaveLocally) {
                    saveImageLocally(notUploadedImageIds)
                }
            } else {
                if (shouldSaveLocally) {
                    saveImageLocally(newImages)
                }
            }

        }


    }

    suspend fun saveImageLocally(newImages: List<Pair<String, File>>) {
        val point = _point.value

        val offlineImages = arrayListOf<LocalImage>()

        newImages.forEach { image ->
            val localImage = LocalImage(
                caption = "Image ${image.first + 1}",
                id = image.first,
                type = "image",
                imageLocalPath = image.second.absolutePath
            )
            point.images.add(localImage)
            offlineImages.add(localImage)
        }
        val updatedPoint = point.copy(
            images = point.images,
            isModified = true
        )
        val convertedValue = offlineImages.convertToOfflineFieldValue()
        pointRepository.insertModifiedField(
            OfflineModifiedPointFields(
                workspaceId = _workspaceID.value ?: "",
                pointId = _pointID.value,
                field = DefectFieldType.IMAGES + _pointID.value,
                type = DefectFieldType.IMAGES,
                value = convertedValue,
            )
        )
        pointRepository.insertPoint(updatedPoint)
        refreshPoint()
    }

    fun handleVideoResult(uris: List<Uri>) {
        if (uris.isEmpty() || workspaceID.value == null || point.value.id.isEmpty()) {
            _error.value = "Unable to process videos"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _mainLoader.value = true
            try {
                val storageUtils = InternalWorkspaceStorageUtils()
                val newVideos = mutableListOf<Pair<String, File>>()

                uris.forEachIndexed { index, uri ->
                    try {
                        val videoId = UUID.randomUUID().toString()

                        // Copy URI to temp file for VideoCompressor (library needs File, not URI)
                        // Use external files directory which is more accessible to native code
                        val tempDir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
                            ?: context.filesDir
                        val tempVideoFile = File.createTempFile("temp_video_", ".mp4", tempDir)
                        Log.d(TAG, "Created temp file: ${tempVideoFile.absolutePath}")

                        context.contentResolver.openInputStream(uri)?.use { inputStream ->
                            FileOutputStream(tempVideoFile).use { outputStream ->
                                inputStream.copyTo(outputStream)
                                outputStream.flush()
                            }
                        } ?: throw Exception("Unable to read video file")

                        // Verify file was written correctly
                        if (!tempVideoFile.exists() || tempVideoFile.length() == 0L) {
                            throw Exception("Video file is invalid after copying")
                        }

                        Log.d(TAG, "Temp file created. Size: ${tempVideoFile.length()} bytes, Path: ${tempVideoFile.absolutePath}")

                        // Ensure file is fully written and accessible
                        tempVideoFile.setReadable(true, false)
                        tempVideoFile.setWritable(true, false)

                        // Small delay to ensure file system has synced
                        delay(100)

                        // Double-check file is still accessible
                        if (!tempVideoFile.exists() || !tempVideoFile.canRead()) {
                            throw Exception("Video file became inaccessible after creation")
                        }

                        // Create output file for compressed video (use same directory as input)
                        val compressedFile = File.createTempFile("compressed_$videoId", ".mp4", tempDir)
                        compressedFile.setWritable(true, false)

                        // Compress video using VideoCompressor
                        try {
                            Log.d(TAG, "Starting video compression. Input file: ${tempVideoFile.absolutePath}, exists: ${tempVideoFile.exists()}, readable: ${tempVideoFile.canRead()}")

                            // Final verification before compression
                            if (!tempVideoFile.exists()) {
                                throw Exception("Input video file does not exist")
                            }

                            if (!tempVideoFile.canRead()) {
                                throw Exception("Input video file is not readable")
                            }

                            if (tempVideoFile.length() == 0L) {
                                throw Exception("Input video file is empty")
                            }

                            // Use VideoCompressor with File (library requirement)
                            VideoCompressor.compress(
                                context = context,
                                input = tempVideoFile,  // Use File as library requires
                                output = compressedFile,
                                onMetadataDecoded = { compressor, metadata ->
                                    try {
                                        Log.d(TAG, "Metadata decoded. Original dimensions: ${metadata.actualWidth}x${metadata.actualHeight}")

                                        // Calculate target size (half of original, but ensure even numbers)
                                        // MediaCodec requires even dimensions
                                        var targetWidth = (metadata.actualWidth / 2)
                                        var targetHeight = (metadata.actualHeight / 2)

                                        // Ensure dimensions are even (required by MediaCodec)
                                        if (targetWidth % 2 != 0) targetWidth--
                                        if (targetHeight % 2 != 0) targetHeight--

                                        // Constrain to reasonable limits
                                        targetWidth = targetWidth.coerceIn(320, 1920)
                                        targetHeight = targetHeight.coerceIn(240, 1080)

                                        // Ensure still even after constraining
                                        if (targetWidth % 2 != 0) targetWidth--
                                        if (targetHeight % 2 != 0) targetHeight--

                                        Log.d(TAG, "Target compression dimensions: ${targetWidth}x${targetHeight}")

                                        // Build compression settings
                                        CompressionSettings.Builder()
                                            .setTargetSize(
                                                width = targetWidth,
                                                height = targetHeight
                                            )
                                            .setBitrate(2_000_000)
                                            .setStreamable(true)
                                            .allowSizeAdjustments(true)
                                            .setEncoderSelectionMode(EncoderSelectionMode.TRY_ALL)
                                            .build()
                                    } catch (e: Exception) {
                                        Log.e(TAG, "Error in onMetadataDecoded: ${e.message}", e)
                                        // Fallback to safe default settings
                                        CompressionSettings.Builder()
                                            .setTargetSize(width = 1280, height = 720)
                                            .setBitrate(2_000_000)
                                            .setStreamable(true)
                                            .allowSizeAdjustments(true)
                                            .setEncoderSelectionMode(EncoderSelectionMode.TRY_ALL)
                                            .build()
                                    }
                                }
                            )

                            // Wait longer for compression to complete (compression can take time)
                            var attempts = 0
                            while (attempts < 40 && (!compressedFile.exists() || compressedFile.length() == 0L)) {
                                delay(500)
                                attempts++
                            }

                            // Verify the compressed file was created and has content
                            if (!compressedFile.exists()) {
                                throw Exception("Video compression failed: Output file was not created after ${attempts * 500}ms")
                            }

                            if (compressedFile.length() == 0L) {
                                throw Exception("Video compression failed: Output file is empty")
                            }

                            Log.d(TAG, "Video compression successful. Compressed: ${compressedFile.length()} bytes")

                        } catch (e: Exception) {
                            Log.e(TAG, "Video compression error: ${e.message}", e)
                            e.printStackTrace()
                            // Clean up failed output file
                            if (compressedFile.exists()) {
                                compressedFile.delete()
                            }
                            throw Exception("Video compression failed: ${e.message ?: "Unknown error"}")
                        }

                        // Save compressed video to storage
                        val originalFile = videoStore.getOriginalFileReference(_workspaceID.value!!,videoId)
                        originalFile.parentFile?.mkdirs()
                        compressedFile.copyTo(originalFile, overwrite = true)

                        // Clean up temporary files
                        tempVideoFile.delete()
                        compressedFile.delete()

                        newVideos.add(videoId to originalFile)

                    } catch (e: Exception) {
                        Log.e(TAG, "Error processing video ${index + 1}: ${e.message}", e)
                    }
                }

                if (newVideos.isNotEmpty()) {
                    uploadVideos(newVideos, true)
                } else {
                    _error.value = "No videos were successfully processed"
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error handling videos: ${e.message}", e)
                _error.value = "Error processing videos: ${e.message}"
            }
            _mainLoader.value = false
        }
    }

    private fun getRealPathFromURI(uri: Uri): String? {
        // On modern Android versions, _data column is not available due to scoped storage
        // This method is kept for backward compatibility but may return null
        var result: String? = null
        when {
            uri.scheme == "file" -> {
                result = uri.path
            }
            uri.scheme == "content" -> {
                // Try to get path, but don't throw if column doesn't exist
                try {
                    val projection = arrayOf(MediaStore.Video.Media.DATA)
                    val cursor = context.contentResolver.query(uri, projection, null, null, null)
                    cursor?.use {
                        if (it.moveToFirst()) {
                            val columnIndex = it.getColumnIndex(MediaStore.Video.Media.DATA)
                            if (columnIndex >= 0) {
                                result = it.getString(columnIndex)
                            }
                        }
                    }
                } catch (e: Exception) {
                    // Column doesn't exist or other error - return null
                    Log.d(TAG, "Could not get path from URI: ${e.message}")
                }
            }
        }
        return result
    }

    suspend fun uploadVideos(newVideos: List<Pair<String, File>>, shouldSaveLocally: Boolean) {
        val notUploadedVideoIds = arrayListOf<Pair<String, File>>()
        coroutineScope {
            if (NetworkUtil.isInternetAvailable(context)) {
                val results = newVideos.map { video ->
                    async {
                        videoRemoteRepository.uploadVideo(
                            workspaceId = workspaceID.value!!,
                            pointId = point.value.id,
                            pointCaption = _point.value.title,
                            videoFile = video.second
                        )
                    }
                }.awaitAll()

                results.forEachIndexed { index, result ->
                    when (result) {
                        is Resource.Error -> {
                            notUploadedVideoIds.add(newVideos[index])
                            Log.e(TAG, "Error uploading video: ${result.apiException.message}")
                        }

                        Resource.Loading -> {
                            // Loading state
                        }

                        is Resource.Success<ApiResponse<Video>> -> {
                            val uploadedVideo = result.data.entity
                            if (uploadedVideo != null) {
                                // Video uploaded successfully
                                Log.d(TAG, "Video uploaded successfully: ${uploadedVideo.id}")
                                // Delete the modified field if it exists
                                pointRepository.deleteModifiedFieldByFieldValue(
                                    DefectFieldType.VIDEOS +_pointID.value
                                )
                            } else {
                                notUploadedVideoIds.add(newVideos[index])
                            }
                        }
                    }
                }
                if (shouldSaveLocally) {
                    syncPoint()
                }

                if (notUploadedVideoIds.isNotEmpty() && shouldSaveLocally) {
                    saveVideoLocally(notUploadedVideoIds)
                }
            } else {
                if (shouldSaveLocally) {
                    saveVideoLocally(newVideos)
                }
            }
        }
    }

    suspend fun saveVideoLocally(newVideos: List<Pair<String, File>>) {
        val point = _point.value
        val offlineVideos = arrayListOf<Video>()

        newVideos.forEachIndexed { index, video ->
            val localVideo = Video(
                caption = "Video ${index + 1}",
                id = video.first,
                type = "video"
            )
            point.videos.add(localVideo)
            offlineVideos.add(localVideo)
        }

        val updatedPoint = point.copy(
            videos = point.videos,
            isModified = true
        )

        // Convert videos to offline field value
        val convertedValue = offlineVideos.convertToOfflineFieldValue()
        pointRepository.insertModifiedField(
            OfflineModifiedPointFields(
                workspaceId = _workspaceID.value ?: "",
                pointId = _pointID.value,
                field = DefectFieldType.VIDEOS + _pointID.value,
                type = DefectFieldType.VIDEOS+ _pointID.value,
                value = convertedValue,
            )
        )
        pointRepository.insertPoint(updatedPoint)
        refreshPoint()
    }


}