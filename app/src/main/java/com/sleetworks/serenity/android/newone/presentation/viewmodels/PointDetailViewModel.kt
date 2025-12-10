package com.sleetworks.serenity.android.newone.presentation.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.sleetworks.serenity.android.newone.data.models.local.NewCustomField
import com.sleetworks.serenity.android.newone.data.models.local.OfflineFieldValue
import com.sleetworks.serenity.android.newone.data.models.local.entities.AssigneeEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.OfflineModifiedPointFields
import com.sleetworks.serenity.android.newone.data.models.local.entities.SiteEntity
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
import com.sleetworks.serenity.android.newone.domain.reporitories.local.ShareRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.SiteRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.UserRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.CommentRemoteRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.PointRemoteRepository
import com.sleetworks.serenity.android.newone.presentation.common.toPoint
import com.sleetworks.serenity.android.newone.presentation.common.toUiModel
import com.sleetworks.serenity.android.newone.presentation.model.CustomFieldTempUI
import com.sleetworks.serenity.android.newone.presentation.ui.model.CustomFieldType
import com.sleetworks.serenity.android.newone.presentation.ui.model.DefectFieldType
import com.sleetworks.serenity.android.newone.presentation.ui.model.PointItemPriority
import com.sleetworks.serenity.android.newone.presentation.ui.model.PointItemStatus
import com.sleetworks.serenity.android.newone.utils.CONSTANTS
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class PointDetailViewModel @Inject constructor(
    private val commentRepository: CommentRemoteRepository,
    private val commentLocalRepository: CommentLocalRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val pointRepository: PointRepository,
    private val pointRemoteRepository: PointRemoteRepository,
    private val userRepository: UserRepository,
    private val siteRepository: SiteRepository,
    private val customFieldTemplateRepo: CustomFieldRepository,
    private val shareRepo: ShareRepository,
    private val savedStateHandle: SavedStateHandle,
    @ApplicationContext
    val context: Context,
) :
    ViewModel() {
    val TAG = "PointDetailViewModel"

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

    private val _point: StateFlow<PointDomain> =
        _pointID.filterNot {
            it.isBlank()
        }
            .distinctUntilChanged()
            .flatMapLatest { pointId ->
                pointRepository.getPointByIDFlow(
                    pointId
                )
            }
            .map { point ->

                val pointDomain = point.toDomain()
                val workSpaceUser = userRepository.getUserByWorkspaceId(_workspaceID.value ?: "")

                val pointUser = workSpaceUser.filter { it.id in pointDomain.assigneeIds }
                pointDomain.assignees.clear()
                pointDomain.assignees.addAll(pointUser)
                pointDomain

            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = PointDomain()
            )
    val point
        get() = _point

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

    fun updatePoint(
        fields: Map<String, Any>,
        customFieldKey: String = "",
        isManualSync: Boolean = true
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val firstKey = fields.entries.first().key
            try {
                if (customFieldKey.isNotEmpty()) {
                    _fieldLoader.value = customFieldKey to true
                } else if (!isManualSync) {
                    _fieldLoader.value = firstKey to true
                } else {
                    _mainLoader.value = true
                }
                val jsonObject = JSONObject()
                val parentArray = JSONArray()
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
                                if (customFieldKey.isNotEmpty()) {
                                    showSuccess(customFieldKey)
                                } else if (!isManualSync) {
                                    showSuccess(firstKey)
                                }
                                deleteModifiedFields()
                            } else {
                                updatePointLocally(_point.value, fields)
                                _error.emit("Unknown Error")
                            }

                        }

                        is Resource.Error -> {
                            updatePointLocally(_point.value, fields)
                            _error.emit(result.apiException.message ?: "Unknown Error")

                        }

                        Resource.Loading -> {

                        }
                    }

                } else {
                    updatePointLocally(_point.value, fields)
                    fields.forEach { item ->

                        val convertedValue = convertToOfflineFieldValue(item.value)
                        pointRepository.insertModifiedField(
                            OfflineModifiedPointFields(
                                workspaceId = _workspaceID.value ?: "",
                                pointId = _pointID.value,
                                field = item.key,
                                value = convertedValue
                            )
                        )
                    }

                }


            } catch (e: Exception) {
                Log.e(TAG, "updatePoint: ", e)
            }
            if (customFieldKey.isNotEmpty()) {
                _fieldLoader.value = customFieldKey to false
            } else if (!isManualSync) {
                _fieldLoader.value = firstKey to false
            } else {
                _mainLoader.value = false
            }
        }


    }

    private fun convertToOfflineFieldValue(value: Any): OfflineFieldValue {
        return when (value) {
            is String -> OfflineFieldValue.StringValue(value)
            is Boolean -> OfflineFieldValue.BooleanValue(value)
            is List<*> -> {
                if (value.isNotEmpty() && value.first() is String) {
                    OfflineFieldValue.StringListValue(value as List<String>)
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

    suspend fun updatePointLocally(currentPoint: PointDomain, fields: Map<String, Any>) {
        val updatedPoint = currentPoint.copy(
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
                ?: currentPoint.tags else currentPoint.tags
        )
        pointRepository.insertPoint(updatedPoint.toPoint())

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
            if (NetworkUtil.isInternetAvailable(context)) {
                if (_offlinePointFields.value.isNotEmpty()) {
                    val map = mutableMapOf<String, Any>()
                    _offlinePointFields.value.forEach { item ->
                        map.put(item.field, item.value.getValue())
                    }
                    updatePoint(map, isManualSync = true)
                }


            } else {
                _error.value = "No Internet Connection"
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

        customField.customFieldTemplateId = pointCustomField.customFieldTemplateId
        val customFieldType = CustomFieldType.fromValue(pointCustomField.type)


//        if (customFieldType == CustomFieldType.TIME) {
//            customField.value = pointCustomField.addedTimeValue ?: ""
//
////            if (NetworkUtil.isInternetAvailable(context)) {
////                val oldValue = filledCustomField.getValue().toLong()
////                val addedValue = filledCustomField.getAddedTimeValue().toLong()
////                filledCustomField.setValue((oldValue + addedValue).toString())
////                filledCustomField.setAddedTimeValue("0")
////            }
//        } else
//            if (customFieldType == CustomFieldType.LIST) {
//            if (filledCustomField.getIdOfChosenElement() != null && !filledCustomField.getIdOfChosenElement()
//                    .isEmpty()
//            ) {
//                customField.value(filledCustomField.getIdOfChosenElement())
//                customField.setType(CustomField.TYPE_LIST)
//            }
//        } else
            if (customFieldType == CustomFieldType.MULTI_LIST) {
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
        updatePoint(
            if (customFieldType == CustomFieldType.RICHTEXT) mapOf(
                DefectFieldType.CUSTOM_FIELDS to cfArray, DefectFieldType.MENTIONS to mentions
            )
            else mapOf(DefectFieldType.CUSTOM_FIELDS to cfArray),
            customFieldKey = pointCustomField.customFieldTemplateId
        )

    }


}