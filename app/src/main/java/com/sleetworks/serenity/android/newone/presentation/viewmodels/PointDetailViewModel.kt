package com.sleetworks.serenity.android.newone.presentation.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.sleetworks.serenity.android.newone.data.imageStore.UserImageStore
import com.sleetworks.serenity.android.newone.data.mappers.toEntity
import com.sleetworks.serenity.android.newone.data.models.local.NewCustomField
import com.sleetworks.serenity.android.newone.data.models.local.OfflineFieldValue
import com.sleetworks.serenity.android.newone.data.models.local.entities.AssigneeEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.OfflineModifiedPointFields
import com.sleetworks.serenity.android.newone.data.models.local.entities.SiteEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.ApiResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.comment.Reaction
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.PointCustomField
import com.sleetworks.serenity.android.newone.data.network.NetworkUtil
import com.sleetworks.serenity.android.newone.data.network.Resource
import com.sleetworks.serenity.android.newone.domain.mapper.toDomain
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
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.PointRemoteRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.WorkspaceRemoteRepository
import com.sleetworks.serenity.android.newone.domain.usecase.SyncPointImageUseCase
import com.sleetworks.serenity.android.newone.presentation.common.toUiModel
import com.sleetworks.serenity.android.newone.presentation.model.CustomFieldTempUI
import com.sleetworks.serenity.android.newone.presentation.model.UserUiModel
import com.sleetworks.serenity.android.newone.presentation.ui.model.CustomFieldType
import com.sleetworks.serenity.android.newone.presentation.ui.model.DefectFieldType
import com.sleetworks.serenity.android.newone.presentation.ui.model.PointItemPriority
import com.sleetworks.serenity.android.newone.presentation.ui.model.PointItemStatus
import com.sleetworks.serenity.android.newone.utils.CONSTANTS
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    private val syncPointImageUseCase: SyncPointImageUseCase,
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
                    pointDomain.comments.sortedByDescending { it.header.updatedEpochMillis }
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

    fun getPointComments() {

        viewModelScope.launch(Dispatchers.IO) {
            val result = commentRepository.getPointComments(pointID.value)

            when (result) {
                is Resource.Success -> {

                    result.data.entity?.let {
                        commentLocalRepository.insertComments(it)
                    }

                }

                is Resource.Error -> {
                    _error.emit(result.apiException.message ?: "Unknown Error")
                }

                else -> {}

            }

        }


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

    private fun convertToOfflineFieldValue(value: Any): OfflineFieldValue {
        return when (value) {
            is Reaction -> OfflineFieldValue.CommentReactionValue(value)
            is NewCustomField -> OfflineFieldValue.NewCustomFieldValue(value as List<NewCustomField>)
            is String -> OfflineFieldValue.StringValue(value)
            is Boolean -> OfflineFieldValue.BooleanValue(value)
            is List<*> -> {
                if (value.isNotEmpty() && value.first() is String) {
                    OfflineFieldValue.StringListValue(value as List<String>)
                } else if (value.isNotEmpty() && value.first() is NewCustomField) {
                    OfflineFieldValue.NewCustomFieldValue(value as List<NewCustomField>)
                } else {
                    OfflineFieldValue.StringListValue(value.map { it.toString() })
                }
            }

            is Set<*> -> {
                if (value.isNotEmpty() && value.first() is String) {
                    OfflineFieldValue.StringListValue(value.toList() as List<String>)
                } else {
                    OfflineFieldValue.StringListValue(value.map { it.toString() })
                }
            }

            is Int -> OfflineFieldValue.IntValue(value)
            is Double -> OfflineFieldValue.DoubleValue(value)
            else -> OfflineFieldValue.StringValue(value.toString())
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
                        if (CustomFieldType.fromValue(oldField.type) != CustomFieldType.LIST) {
                            updatedList[existingFieldIndex] = oldField.copy(
                                value = fields[DefectFieldType.CUSTOM_FIELDS] as? String
                                    ?: oldField.value
                            )
                        } else {
                            updatedList[existingFieldIndex] = oldField.copy(
                                idOfChosenElement = fields[DefectFieldType.CUSTOM_FIELDS] as? String
                                    ?: oldField.idOfChosenElement
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

            val convertedValue = convertToOfflineFieldValue(item.value)
            pointRepository.insertModifiedField(
                OfflineModifiedPointFields(
                    workspaceId = _workspaceID.value ?: "",
                    pointId = _pointID.value,
                    field = if (DefectFieldType.MENTIONS != item.key) customFieldKey.ifEmpty { item.key } else item.key,
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

    fun syncPoint() {
        viewModelScope.launch(Dispatchers.IO) {
            _mainLoader.value = true
            if (NetworkUtil.isInternetAvailable(context)) {

                syncOfflineData()

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


            } else {
//                _error.value = "No Internet Connection"
            }
            _mainLoader.value = false

        }

    }


    suspend fun syncOfflineData() {
        if (_offlinePointFields.value.isNotEmpty()) {
            val cfList = arrayListOf<NewCustomField>()
            val pointDetailsMap = mutableMapOf<String, Any>()
            val commentReactions = mutableListOf<Reaction>()
            _offlinePointFields.value.forEach { item ->
                if (item.value is OfflineFieldValue.NewCustomFieldValue) {
                    cfList.addAll(item.value.getValue() as List<NewCustomField>)
                } else if (item.value is OfflineFieldValue.CommentReactionValue) {
                    commentReactions.add(item.value.getValue() as Reaction)
                } else {
                    pointDetailsMap.put(
                        item.field,
                        item.value.getValue()
                    )
                }

            }
            if (pointDetailsMap.isNotEmpty()) {
                if (cfList.isNotEmpty()) {
                    pointDetailsMap[DefectFieldType.CUSTOM_FIELDS] = cfList
                }
                updatePoint(pointDetailsMap, isManualSync = true)
            }
            if (commentReactions.isNotEmpty()) {
                updateLocalReactions(commentReactions)
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
//            if (filledCustomField.getSelectedItemIds() != null) {
//                customField.setSelectedItemIds(filledCustomField.getSelectedItemIds())
//                customField.setType(CustomField.TYPE_MULTI_LIST)
//            }
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

    suspend fun updateLocalReactions(reactions: List<Reaction>) {

        coroutineScope {

            reactions.map { reaction ->
                async {
                    val id = reaction.like.find {
                        it == _user.value.id
                    }
                    addReaction(reaction, id == null)
                }
            }.awaitAll()

        }
    }

    fun updateReaction(reaction: Reaction, shouldAdd: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            addReaction(reaction, shouldAdd)
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
        val convertedValue = convertToOfflineFieldValue(reaction)
        pointRepository.insertModifiedField(
            OfflineModifiedPointFields(
                workspaceId = _workspaceID.value ?: "",
                pointId = _pointID.value,
                field = DefectFieldType.REACTION,
                value = convertedValue
            )
        )

        reactionLocalRepository.insertReaction(reaction)
        refreshPoint()


    }

}