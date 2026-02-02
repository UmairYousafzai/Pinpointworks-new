package com.sleetworks.serenity.android.newone.presentation.ui.screens.pointDetail

import CircularImage
import android.Manifest
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.aghajari.compose.text.AnnotatedText
import com.aghajari.compose.text.fromHtml
import com.sleetworks.serenity.android.newone.R
import com.sleetworks.serenity.android.newone.data.models.local.PointFieldType
import com.sleetworks.serenity.android.newone.data.models.local.entities.AssigneeEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.OfflineModifiedPointFields
import com.sleetworks.serenity.android.newone.data.storage.InternalWorkspaceStorageUtils
import com.sleetworks.serenity.android.newone.domain.models.CommentDomain
import com.sleetworks.serenity.android.newone.domain.models.point.PointDomain
import com.sleetworks.serenity.android.newone.domain.models.share.CustomFieldPermissionDomainModel
import com.sleetworks.serenity.android.newone.domain.models.share.PermissionDomainModel
import com.sleetworks.serenity.android.newone.presentation.common.toPointCustomField
import com.sleetworks.serenity.android.newone.presentation.navigation.Screen
import com.sleetworks.serenity.android.newone.presentation.ui.components.AppTopBar
import com.sleetworks.serenity.android.newone.presentation.ui.components.ExpandableFab
import com.sleetworks.serenity.android.newone.presentation.ui.components.LoaderDialog
import com.sleetworks.serenity.android.newone.presentation.ui.dialogs.AddPhotoDialog
import com.sleetworks.serenity.android.newone.presentation.ui.dialogs.MediaStorageSettingDialog
import com.sleetworks.serenity.android.newone.presentation.ui.model.CustomFieldType
import com.sleetworks.serenity.android.newone.presentation.ui.model.DefectFieldType
import com.sleetworks.serenity.android.newone.presentation.ui.model.PointItemPriority
import com.sleetworks.serenity.android.newone.presentation.ui.model.PointItemStatus
import com.sleetworks.serenity.android.newone.presentation.viewmodels.PointDetailViewModel
import com.sleetworks.serenity.android.newone.presentation.viewmodels.SharedViewModel
import com.sleetworks.serenity.android.newone.ui.theme.BrightBlue
import com.sleetworks.serenity.android.newone.ui.theme.Goldenrod
import com.sleetworks.serenity.android.newone.ui.theme.LightGrey
import com.sleetworks.serenity.android.newone.ui.theme.LightSalmon
import com.sleetworks.serenity.android.newone.ui.theme.PinpointworksNewTheme
import com.sleetworks.serenity.android.newone.ui.theme.TransparentBlack
import com.sleetworks.serenity.android.newone.ui.theme.gray
import com.sleetworks.serenity.android.newone.ui.theme.onyx
import com.sleetworks.serenity.android.newone.ui.theme.paleBlueGray
import com.sleetworks.serenity.android.newone.utils.CONSTANTS.IMAGE
import com.sleetworks.serenity.android.newone.utils.CONSTANTS.NONE
import com.sleetworks.serenity.android.newone.utils.CONSTANTS.VIDEO
import com.sleetworks.serenity.android.newone.utils.TextRichOptions
import com.sleetworks.serenity.android.newone.utils.clearResult
import com.sleetworks.serenity.android.newone.utils.createCameraImageFile
import com.sleetworks.serenity.android.newone.utils.createCameraVideoFile
import com.sleetworks.serenity.android.newone.utils.formatCommentDateTime
import com.sleetworks.serenity.android.newone.utils.requestCameraPermission
import com.sleetworks.serenity.android.newone.utils.requestStoragePermission
import com.sleetworks.serenity.android.newone.utils.requestVideoPermissions
import com.sleetworks.serenity.android.newone.utils.resultFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun DefectDetailScreen(
    navController: NavHostController,
    viewModel: PointDetailViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel
) {
    val TAG = "DefectDetailScreen"


    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    var addMediaType by remember { mutableStateOf(NONE) }

    var formulaLabel by remember { mutableStateOf("") }
    var editTextDialogLabel by remember { mutableStateOf("") }

    // Camera image state
    var cameraMediaUri by remember { mutableStateOf<Uri?>(null) }


    var showEditTextDialog by remember {
        mutableStateOf(false)
    }


    var showSettingDialog by remember {
        mutableStateOf(false)
    }

    var showAddPhotoDialog by remember {
        mutableStateOf(false)
    }

    var showPriorityDialog by remember {
        mutableStateOf(false)
    }
    var showPointStatusDialog by remember {
        mutableStateOf(false)
    }
    var showFlagDialog by remember {
        mutableStateOf(false)
    }
    var showAssigneeDialog by remember {
        mutableStateOf(false)
    }
    var showTagDialog by remember {
        mutableStateOf(false)
    }

    var showLockedDialog by remember {
        mutableStateOf(false)
    }

    var showFormulaDialog by remember {
        mutableStateOf(false)
    }
    val imageCameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success && cameraMediaUri != null) {
            cameraMediaUri?.let { uri ->
                viewModel.handleImageResult(listOf(uri))
            }
        }
    }
    val videoCameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo()
    ) { success: Boolean ->
        if (success && cameraMediaUri != null) {
            cameraMediaUri?.let { uri ->
                viewModel.handleVideoResult(listOf(uri))
            }
        } else {
            sharedViewModel.showSnackbar("Video recording failed or was cancelled")
        }
    }

    val coroutineScope = rememberCoroutineScope()

    fun launchCamera() {
        coroutineScope.launch {
            val saveToExternal = viewModel.shouldSaveImageToExternalStorage()
            val uri = context.createCameraImageFile(saveToExternal)
            cameraMediaUri = uri
            uri?.let {
                imageCameraLauncher.launch(it)  // Use image launcher
            } ?: run {
                sharedViewModel.showSnackbar("Failed to create image file")
            }
        }
    }

    fun launchVideoCamera() {
        coroutineScope.launch {
            val saveToExternal = viewModel.shouldSaveVideoToExternalStorage()
            val uri = context.createCameraVideoFile(saveToExternal)
            cameraMediaUri = uri
            uri?.let {
                videoCameraLauncher.launch(it)  // Use video launcher
            } ?: run {
                sharedViewModel.showSnackbar("Failed to create video file")
            }
        }
    }

    val videoPermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val cameraGranted = permissions[Manifest.permission.CAMERA] ?: false
        val audioGranted = permissions[Manifest.permission.RECORD_AUDIO] ?: false

        when {
            cameraGranted && audioGranted -> {
                showSettingDialog = true
            }

            !cameraGranted && !audioGranted -> {
                sharedViewModel.showSnackbar("Camera and audio permissions are required to record videos")
            }

            !cameraGranted -> {
                sharedViewModel.showSnackbar("Camera permission is required to record videos")
            }

            !audioGranted -> {
                sharedViewModel.showSnackbar("Audio permission is required to record videos")
            }
        }
    }
    // Camera permission launcher - only handles images
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showSettingDialog = true
        } else {
            sharedViewModel.showSnackbar("Camera permission is required to take pictures")
        }
    }

    // Multiple permissions launcher for videos (needs CAMERA + RECORD_AUDIO)

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris ->
        if (uris.isNotEmpty()) {
            if (addMediaType == IMAGE) {
                viewModel.handleImageResult(uris)
            } else {
                viewModel.handleVideoResult(uris)

            }
        }
    }

    val storagePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            imagePickerLauncher.launch(
                PickVisualMediaRequest(
                    if (addMediaType == IMAGE) ActivityResultContracts.PickVisualMedia.ImageOnly
                    else ActivityResultContracts.PickVisualMedia.VideoOnly
                )
            )
        } else {
            sharedViewModel.showSnackbar("Storage permission is required to select pictures")
        }
    }


    val plainText by navController.resultFlow("plainText", "").collectAsState()
    val base64Value by navController.resultFlow("base64Value", "").collectAsState()
    val richfieldType by navController.resultFlow("fieldType", "").collectAsState()
    val mentions by navController.resultFlow("mentions", emptyList<String>()).collectAsState()
    val richCustomFieldId by navController.resultFlow("customFieldId", "").collectAsState()
    val richCustomFieldTempId by navController.resultFlow("customFieldTempId", "").collectAsState()

    val isLoading by viewModel.mainLoader.collectAsState()
    val point by viewModel.point.collectAsState()
    val errorMessage by viewModel.error.collectAsState()
    val workspaceUsers by viewModel.workspaceUser.collectAsState()
    val site by viewModel.site.collectAsState()
    val workspaceId by viewModel.workspaceID.collectAsState()
    val share by viewModel.share.collectAsState()
    val offlineModifiedPointFields by viewModel.offlinePointFields.collectAsState()
    val customFieldTemplates by viewModel.customFieldTemplates.collectAsState()
    val customFieldTemplatesMapID by remember { mutableStateOf(customFieldTemplates.associateBy { it.id.toString() }) }
    val pointCustomFieldByTemplateId by viewModel.pointCustomFieldByTemplateId.collectAsState()
    val customFieldPermission by remember {
        mutableStateOf(share?.advancedAccessLevels?.customFields?.associateBy {
            it.templateId
        })
    }
    var offlineModifiedFields by remember {
        mutableStateOf(offlineModifiedPointFields.associateBy {
            it.field
        })
    }
    var offlineModifiedFieldsByType by remember {
        mutableStateOf(offlineModifiedPointFields.associateBy {
            it.type
        })
    }

    val fieldLoader by viewModel.fieldLoader.collectAsState()
    val fieldSuccess by viewModel.fieldSuccess.collectAsState()

    BackHandler {
        if (!navController.popBackStack()) {
            navController.navigate(Screen.DefectListScreen.route + "/" + false)
        }
    }

    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotEmpty()) {
            sharedViewModel.showSnackbar(errorMessage)
            viewModel.setError()
        }
    }

    LaunchedEffect(Unit) {

        snapshotFlow {
            Pair(site, workspaceId)
        }
            .first { (site, workspaceId) ->
                site != null && workspaceId != null
            }

//        viewModel.syncPoint()
    }

    LaunchedEffect(offlineModifiedPointFields) {
        offlineModifiedFields = offlineModifiedPointFields.associateBy {
            it.field
        }
        offlineModifiedFieldsByType = offlineModifiedPointFields.associateBy {
            it.type
        }

    }



    LaunchedEffect(showEditTextDialog) {
        if (!showEditTextDialog) {
            delay(100) // Small delay to ensure dialog is fully dismissed
            focusManager.clearFocus(force = true)
            keyboardController?.hide()
        }
    }

    LaunchedEffect(plainText) {

        if (plainText.isNotEmpty()) {

            if (richCustomFieldId.isNotEmpty()) {
                val cf = customFieldTemplatesMapID[richCustomFieldTempId]
                if (cf != null) {
                    val pointCf =
                        pointCustomFieldByTemplateId[richCustomFieldId] ?: cf.toPointCustomField()


                    pointCf.value = base64Value
                    viewModel.updateCustomField(cf, pointCf)
                }
            } else if (richfieldType == PointFieldType.COMMENT.value) {
                viewModel.addNewComment(plainText, base64Value, mentions)
            } else {
                viewModel.updatePoint(
                    mapOf(
                        DefectFieldType.DESCRIPTION to plainText,
                        DefectFieldType.DESCRIPTIONRICH to base64Value,
                        DefectFieldType.MENTIONS to mentions
                    )
                )
            }


            navController.clearResult("plainText")
        }
        if (base64Value.isNotEmpty()) {
            navController.clearResult("base64Value")
        }
        if (richfieldType.isNotEmpty()) {
            navController.clearResult("fieldType")
        }
        if (mentions.isNotEmpty()) {
            navController.clearResult("mentions")
        }
        if (richCustomFieldId.isNotEmpty()) {
            navController.clearResult("richCustomFieldId")
        }
        if (richCustomFieldTempId.isNotEmpty()) {
            navController.clearResult("customFieldTempId")
        }

    }


    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            AppTopBar(
                leadingIcon = R.drawable.ic_back_solid,
                actionIcons = listOf(
                    Pair("Done", Icons.Default.Sync)
                )
            ) { action ->

                when (action) {
                    "Done" -> {
                        viewModel.syncPointDetail()
                    }

                    "Back" -> {
                        navController.popBackStack()
                    }
                }
            }


            LazyColumn() {
                item {
                    PointDetails(
                        point, workspaceUsers,
                        isLoading = fieldLoader,
                        isSuccess = fieldSuccess,
                        offlineModifiedFields = offlineModifiedFields,
                        navController = navController,
                        showEditTextDialog = {
                            editTextDialogLabel = "Title"
                            showEditTextDialog = true
                        },
                        showPriorityDialog = {
                            showPriorityDialog = true
                        },
                        showPointStatusDialog = {
                            showPointStatusDialog = true
                        },
                        showFlagDialog = {
                            showFlagDialog = true
                        },
                        showAssigneeDialog = {
                            showAssigneeDialog = true
                        },
                        showTagDialog = {
                            showTagDialog = true
                        }
                    )

                }

                items(customFieldTemplates) { customField ->
                    if (customField.display == false) return@items
                    val permission =
                        customFieldPermission?.get(customField.id)
                            ?: CustomFieldPermissionDomainModel(
                                customField.id,
                                PermissionDomainModel(read = true, edit = true)
                            )
                    val pointCF = pointCustomFieldByTemplateId[customField.id.toString()]


                    when (CustomFieldType.fromValue(customField.type)) {

                        CustomFieldType.TEXT -> {
                            CustomFieldTextLayout(
                                customField = customField,
                                value = pointCF?.value ?: "",
                                permission,
                                isLoading = (pointCF != null && (pointCF.customFieldTemplateId == fieldLoader.first) && fieldLoader.second),
                                isCompleted = (pointCF != null && (pointCF.customFieldTemplateId == fieldSuccess.first) && fieldSuccess.second),
                                isOfflineModified = offlineModifiedFields.contains(customField.id.toString()),
                                onLockedClick = {
                                    showLockedDialog = true
                                },
                                onNotEditClick = {
                                    sharedViewModel.showSnackbar("Not editable field")
                                },
                                onValueChange = { value ->
                                    val newPointCustomField =
                                        pointCF ?: customField.toPointCustomField()
                                    newPointCustomField.value = value
                                    viewModel.updateCustomField(customField, newPointCustomField)

                                }
                            )

                        }

                        CustomFieldType.DATE -> {
                            CustomFieldDateLayout(
                                customField = customField,
                                value = pointCF?.value ?: "",
                                permission = permission,
                                isLoading = (pointCF != null && (pointCF.customFieldTemplateId == fieldLoader.first) && fieldLoader.second),
                                isCompleted = (pointCF != null && (pointCF.customFieldTemplateId == fieldSuccess.first) && fieldSuccess.second),
                                isOfflineModified = offlineModifiedFields.contains(customField.id.toString()),
                                onLockedClick = {
                                    showLockedDialog = true
                                },
                                onNotEditable = {
                                    sharedViewModel.showSnackbar("Not editable field")
                                },
                                onDateChange = { dataMilli ->
                                    val newPointCustomField =
                                        pointCF ?: customField.toPointCustomField()
                                    newPointCustomField.value = dataMilli.toString()
                                    viewModel.updateCustomField(customField, newPointCustomField)

                                }
                            )
                        }

                        CustomFieldType.COST -> {
                            CustomFieldCostLayout(
                                customField = customField,
                                value = pointCF?.value ?: "",
                                permission = permission,
                                isLoading = (pointCF != null && (pointCF.customFieldTemplateId == fieldLoader.first) && fieldLoader.second),
                                isCompleted = (pointCF != null && (pointCF.customFieldTemplateId == fieldSuccess.first) && fieldSuccess.second),
                                isOfflineModified = offlineModifiedFields.contains(customField.id.toString()),

                                onNotEditable = {
                                    sharedViewModel.showSnackbar("Not editable field")

                                },
                                onLockedClick = {
                                    showLockedDialog = true
                                },
                                onValueChanged = { value ->
                                    val newPointCustomField =
                                        pointCF ?: customField.toPointCustomField()
                                    newPointCustomField.value = value
                                    viewModel.updateCustomField(customField, newPointCustomField)
                                }
                            )
                        }

                        CustomFieldType.LIST -> {

                            CustomFieldListLayout(
                                customField = customField,
                                value = pointCF?.value ?: "",
                                selectedValueId = pointCF?.idOfChosenElement ?: "",
                                permission = permission,
                                isLoading = (pointCF != null && (pointCF.customFieldTemplateId == fieldLoader.first) && fieldLoader.second),
                                isCompleted = (pointCF != null && (pointCF.customFieldTemplateId == fieldSuccess.first) && fieldSuccess.second),
                                isOfflineModified = offlineModifiedFields.contains(customField.id.toString()),
                                onNotEditable = {
                                    sharedViewModel.showSnackbar("Not editable field")

                                },
                                onLockedClick = {
                                    showLockedDialog = true
                                },
                                onOptionSelected = { value ->
                                    val newPointCustomField =
                                        pointCF ?: customField.toPointCustomField()
                                    newPointCustomField.idOfChosenElement = value
                                    viewModel.updateCustomField(customField, newPointCustomField)

                                }

                            )
                        }

                        CustomFieldType.NUMBERS -> {
                            CustomFieldNumbersLayout(
                                customField = customField,
                                value = pointCF?.value ?: "",
                                permission = permission,
                                isLoading = (pointCF != null && (pointCF.customFieldTemplateId == fieldLoader.first) && fieldLoader.second),
                                isCompleted = (pointCF != null && (pointCF.customFieldTemplateId == fieldSuccess.first) && fieldSuccess.second),
                                isOfflineModified = offlineModifiedFields.contains(customField.id.toString()),
                                onNotEditable = {
                                    sharedViewModel.showSnackbar("Not editable field")
                                },
                                onLockedClick = {
                                    showLockedDialog = true
                                },
                                onValueChanged = { value ->
                                    val newPointCustomField =
                                        pointCF ?: customField.toPointCustomField()
                                    newPointCustomField.value = value
                                    viewModel.updateCustomField(customField, newPointCustomField)
                                }
                            )
                        }

                        CustomFieldType.RICHTEXT -> {

                            CustomFieldRichTextLayout(
                                customField = customField,
                                content = pointCF?.value ?: "",
                                permission = permission,
                                isLoading = (pointCF != null && (pointCF.customFieldTemplateId == fieldLoader.first) && fieldLoader.second),
                                isCompleted = (pointCF != null && (pointCF.customFieldTemplateId == fieldSuccess.first) && fieldSuccess.second),
                                isOfflineModified = offlineModifiedFields.contains(customField.id.toString()),
                                onNotEditable = {
                                    sharedViewModel.showSnackbar("Not editable field")
                                },
                                onLockedClick = {
                                    showLockedDialog = true
                                },
                                onEditClick = {
                                    navController.navigate(
                                        Screen.RichTextEditorScreen.route +
                                                "/${PointFieldType.DESCRIPTION.value}/${customField.id}/${pointCF?.value ?: ""}"
                                    )
                                }
                            )
                        }

                        CustomFieldType.TIME -> {

                            TimeCustomFieldLayout(
                                customField = customField,
                                value = pointCF?.value ?: "",
                                permission = permission,
                                isLoading = (pointCF != null && (pointCF.customFieldTemplateId == fieldLoader.first) && fieldLoader.second),
                                isCompleted = (pointCF != null && (pointCF.customFieldTemplateId == fieldSuccess.first) && fieldSuccess.second),
                                isOfflineModified = offlineModifiedFields.contains(customField.id.toString()),
                                onNotEditable = {
                                    sharedViewModel.showSnackbar("Not editable field")
                                },
                                onLockedClick = {
                                    showLockedDialog = true
                                },
                                onValueChanged = { value ->
                                    val newPointCustomField =
                                        pointCF ?: customField.toPointCustomField()
                                    newPointCustomField.value = value
                                    viewModel.updateCustomField(customField, newPointCustomField)

                                }
                            )
                        }

                        CustomFieldType.PERCENTAGE -> {

                            CustomFieldPercentageLayout(
                                customField = customField,
                                value = pointCF?.value ?: "",
                                permission = permission,
                                isLoading = (pointCF != null && (pointCF.customFieldTemplateId == fieldLoader.first) && fieldLoader.second),
                                isCompleted = (pointCF != null && (pointCF.customFieldTemplateId == fieldSuccess.first) && fieldSuccess.second),
                                isOfflineModified = offlineModifiedFields.contains(customField.id.toString()),
                                onAddClick = { /* Handle add click */ },
                                onViewClick = { /* Handle view click */ },
                                onLockedClick = {
                                    showLockedDialog = true
                                },
                                onNotEditable = {
                                    sharedViewModel.showSnackbar("Not editable field")

                                },
                                onValueChanged = { value ->
                                    val newPointCustomField =
                                        pointCF ?: customField.toPointCustomField()
                                    newPointCustomField.value = value
                                    viewModel.updateCustomField(customField, newPointCustomField)
                                }
                            )
                        }

                        CustomFieldType.CHECKBOX -> {
                            CustomFieldCheckboxLayout(
                                customField = customField,
                                checked = pointCF?.value == "true",
                                permission = permission,
                                isLoading = (pointCF != null && (pointCF.customFieldTemplateId == fieldLoader.first) && fieldLoader.second),
                                isCompleted = (pointCF != null && (pointCF.customFieldTemplateId == fieldSuccess.first) && fieldSuccess.second),
                                isOfflineModified = offlineModifiedFields.contains(customField.id.toString()),
                                onCheckedChange = { value ->
                                    val newPointCustomField =
                                        pointCF ?: customField.toPointCustomField()
                                    newPointCustomField.value = value.toString()
                                    viewModel.updateCustomField(customField, newPointCustomField)
                                },
                                onLockedClick = {
                                    showLockedDialog = true
                                },
                                onNotEditable = {
                                    sharedViewModel.showSnackbar("Not editable field")

                                },
                            )
                        }

                        CustomFieldType.TIMELINE -> {
                            CustomFieldTimelineLayout(
                                customField = customField,
                                value = pointCF?.value ?: "",
                                permission = permission,
                                isLoading = (pointCF != null && (pointCF.customFieldTemplateId == fieldLoader.first) && fieldLoader.second),
                                isCompleted = (pointCF != null && (pointCF.customFieldTemplateId == fieldSuccess.first) && fieldSuccess.second),
                                isOfflineModified = offlineModifiedFields.contains(customField.id.toString()),
                                onLockedClick = {
                                    showLockedDialog = true
                                },
                                onNotEditable = {
                                    sharedViewModel.showSnackbar("Not editable field")

                                },
                                onDateChange = { value ->
                                    val newPointCustomField =
                                        pointCF ?: customField.toPointCustomField()
                                    newPointCustomField.value = value
                                    viewModel.updateCustomField(customField, newPointCustomField)
                                }
                            )
                        }

                        CustomFieldType.FORMULA -> {
                            CustomFieldFormulaLayout(
                                customField = customField,
                                value = pointCF?.value ?: "",
                                onClicked = { label ->
                                    formulaLabel = label
                                    showFormulaDialog = true
                                }
                            )
                        }

                        CustomFieldType.MULTI_LIST -> {
                            CustomFieldMultiListLayout(
                                customField = customField,
                                value = pointCF?.value ?: "",
                                selectedIds = pointCF?.selectedItemIds ?: emptyList(),
                                permission = permission,
                                isLoading = false,
                                isCompleted = false,
                                isOfflineModified = offlineModifiedFields.contains(customField.id.toString()),
                                onLockedClick = {
                                    showLockedDialog = true
                                },
                                onNotEditable = {
                                    sharedViewModel.showSnackbar("Not editable field")
                                },
                                onValueChanged = { ids ->
                                    val newPointCustomField =
                                        pointCF ?: customField.toPointCustomField()
                                    newPointCustomField.selectedItemIds = ids
                                    viewModel.updateCustomField(customField, newPointCustomField)


                                }
                            )
                        }

                        null -> {}
                    }

                }

                item {
                    if (point.images.isNotEmpty())
                        PhotoSection(
                            point = point,
                            workspaceId = workspaceId ?: "",
                            offlineModifiedFields = offlineModifiedFields,
                            navController = navController,
                            showPhotoSelectionDialog = {

                            }
                        )
                    if (point.videos.isNotEmpty())
                        VideoSection(
                            point = point,
                            offlineModifiedFields = offlineModifiedFields,
                            navController = navController
                        )
                    if (point.comments.isNotEmpty())
                        CommentSection(
                            comments = point.comments,
                            viewModel = viewModel,
                            offlineModifiedFields = offlineModifiedFieldsByType,
                            onLikedClicked = { comment, isLiked ->


                                viewModel.updateReaction(comment, isLiked)
                            }
                        )
                }

            }

            if (showSettingDialog) {
                MediaStorageSettingDialog(
                    title = if (IMAGE == addMediaType) "Save images to device" else "Save videos to device",
                    body = if (IMAGE == addMediaType) "Photos taken in app will automatically save to the photo library on the device. " +
                            "(This can be changed later in the Settings panel.)" else "Videos taken in app will automatically save to the library on the device. " +
                            "(This can be changed later in the Settings panel.)",
                    onConfirm = {
                        showSettingDialog = false
                        if (addMediaType === IMAGE) {
                            viewModel.shouldSaveImageTOExternalStorage(true)
                        } else {
                            viewModel.shouldSaveVideoTOExternalStorage(true)
                        }
                        if (addMediaType == IMAGE) launchCamera() else launchVideoCamera()
                    },
                    onDismiss = {
                        showSettingDialog = false
                        if (addMediaType === IMAGE) {
                            viewModel.shouldSaveImageTOExternalStorage(false)
                        } else {
                            viewModel.shouldSaveVideoTOExternalStorage(false)
                        }
                        if (addMediaType == IMAGE) launchCamera() else launchVideoCamera()

                    }
                )
                addMediaType = NONE
            }

            if (showAddPhotoDialog)
                AddPhotoDialog(
                    mediaType = addMediaType,
                    onTakeMediaClick = { mediaType ->
                        if (mediaType == IMAGE) {
                            // For images, only need CAMERA permission
                            context.requestCameraPermission(
                                onRequestPermission = { permission ->
                                    cameraPermissionLauncher.launch(permission)
                                },
                                onPermissionGranted = {
                                    launchCamera()
                                }
                            )
                        } else {
                            // For videos, use the new requestVideoPermissions utility
                            context.requestVideoPermissions(
                                onPermissionsGranted = {
                                    // Both permissions granted, launch video camera
                                    launchVideoCamera()
                                },
                                onRequestPermissions = { permissions ->
                                    // Request missing permissions together
                                    videoPermissionsLauncher.launch(permissions)
                                }
                            )
                        }
                    },
                    onSelectMediaClick = {
                        context.requestStoragePermission(
                            onRequestPermission = { permission ->
                                storagePermissionLauncher.launch(permission)
                            },
                            onPermissionGranted = {
                                imagePickerLauncher.launch(
                                    PickVisualMediaRequest(
                                        if (addMediaType == IMAGE) ActivityResultContracts.PickVisualMedia.ImageOnly
                                        else ActivityResultContracts.PickVisualMedia.VideoOnly
                                    )
                                )
                            }
                        )
                    },
                    onDismiss = {
                        showAddPhotoDialog = false
                    }
                )

            if (showEditTextDialog)
                PointTextEditDialog(
                    editTextDialogLabel, initialValue = point.title,
                    onDismiss = {
                        showEditTextDialog = false
                    },
                    onConfirm = { value ->
                        showEditTextDialog = false
                        viewModel.updatePointFields(mapOf(DefectFieldType.TITLE to value))
                    },

                    )

            // Dialog
            if (showPriorityDialog) {
                PriorityPickerDialog(
                    selectedPriority = PointItemPriority.from(point.priority),
                    onPrioritySelected = { priority ->
                        showPriorityDialog = false
                        viewModel.updatePointFields(mapOf(DefectFieldType.PRIORITY to priority.label.uppercase()))
                    },
                    onDismiss = {
                        showPriorityDialog = false
                    }
                )
            }

            if (showPointStatusDialog) {
                StatusPickerDialog(
                    selectedStatus = PointItemStatus.from(point.status),
                    onStatusSelected = { status ->
                        showPointStatusDialog = false
                        viewModel.updatePointFields(
                            mapOf(
                                DefectFieldType.STATUS to
                                        PointItemStatus.toUpperValue(status.label)
                            )
                        )

                    },
                    onDismiss = {
                        showPointStatusDialog = false

                    }
                )

            }


            if (showFlagDialog) {
                FlaggedChoiceDialog(
                    selectedFlaggedState = point.flagged,
                    onFlaggedStateSelected = { state ->
                        viewModel.updatePointFields(
                            mapOf(
                                DefectFieldType.RED_FLAG to
                                        state
                            )
                        )
                        showFlagDialog = false
                    },
                    onDismiss = {
                        showFlagDialog = false

                    }

                )
            }

            if (showAssigneeDialog) {
                AssigneeDialog(
                    assignees = workspaceUsers,
                    selectedAssigneeIds = point.assigneeIds,
                    onAssigneeSelected = { ids ->
                        showAssigneeDialog = false
                        viewModel.updatePointFields(mapOf(DefectFieldType.ASSIGNEES to ids))
                    },
                    onDismiss = {
                        showAssigneeDialog = false
                    }
                )
            }

            if (showTagDialog) {
                TagsDialog(
                    tags = site?.tags ?: emptyList(),
                    selectedTags = point.tags,
                    onTagSelected = { ids ->
                        showTagDialog = false
                        viewModel.updatePointFields(mapOf(DefectFieldType.TAGS to ids))

                    },
                    onDismiss = {
                        showTagDialog = false
                    }
                )
            }

            if (showLockedDialog) {
                LockedDialog(onDismiss = {
                    showLockedDialog = false
                })
            }

            if (showFormulaDialog) {
                FormulaDialog(formulaLabel, onDismiss = {
                    showFormulaDialog = false
                })
            }

            if (isLoading) {
                LoaderDialog(text = "Syncing...")
            }


        }

        ExpandableFab(
            modifier = Modifier
                .align(Alignment.BottomEnd),
            onCameraClick = {
                addMediaType = IMAGE
                showAddPhotoDialog = true
            },
            onVideoClick = {
                addMediaType = VIDEO
                showAddPhotoDialog = true
            },
            onCommentClick = {
                navController.navigate(
                    Screen.RichTextEditorScreen.route +
                            "/${PointFieldType.COMMENT.value}/${""}/${""}"
                )
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PointDetails(
    point: PointDomain,
    workspaceUsers: List<AssigneeEntity>,
    isLoading: Pair<String, Boolean>,
    isSuccess: Pair<String, Boolean>,
    offlineModifiedFields: Map<String, OfflineModifiedPointFields>,
    navController: NavHostController,
    showEditTextDialog: () -> Unit,
    showPriorityDialog: () -> Unit,
    showPointStatusDialog: () -> Unit,
    showFlagDialog: () -> Unit,
    showAssigneeDialog: () -> Unit,
    showTagDialog: () -> Unit,

    ) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(top = 10.dp, start = 10.dp, end = 10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            Text(
                modifier = Modifier.fillMaxSize(0.1f),
                text = point.sequenceNumber.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = gray
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(

                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .weight(1f)
                    .clickable(onClick = showEditTextDialog),
                text = point.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
            if (isLoading.first == DefectFieldType.TITLE && isLoading.second) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(22.dp)
                        .padding(end = 5.dp),
                    strokeWidth = 2.dp
                )
            }
            if (isSuccess.first == DefectFieldType.TITLE && isSuccess.second) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_green_check_solid),
                    contentDescription = "Completed",
                    tint = null,
                    modifier = Modifier
                        .size(22.dp)
                        .padding(end = 5.dp)
                )
            }
            if (offlineModifiedFields.contains(DefectFieldType.TITLE)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cloud_upload_solid),
                    contentDescription = "Offline modified",
                    modifier = Modifier
                        .size(30.dp)
                        .padding(start = 5.dp)
                )
            }
        }


    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextChipWithIcon(
            text = PointItemPriority.from(point.priority).label,
            color = PointItemPriority.from(point.priority).color,
            onClick = showPriorityDialog
        )
        Spacer(modifier = Modifier.width(10.dp))
        TextChipWithIcon(
            text = PointItemStatus.from(point.status).label,
            icon = PointItemStatus.from(point.status).icon,
            color = gray,
            shouldShowIcon = true,
            onClick = showPointStatusDialog
        )
        Spacer(modifier = Modifier.width(15.dp))
        IconWithBg(
            if (point.flagged) R.drawable.ic_red_flag else R.drawable.ic_flagged_gray,
            bgColor = if (point.flagged) LightSalmon else paleBlueGray,
            onClick = showFlagDialog
        )
        Spacer(modifier = Modifier.width(15.dp))
        IconWithBg(icon = R.drawable.ic_reminder) {

        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )


        if ((isLoading.first == DefectFieldType.PRIORITY
                    || isLoading.first == DefectFieldType.STATUS
                    || isLoading.first == DefectFieldType.RED_FLAG)
            && isLoading.second
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(22.dp)
                    .padding(end = 5.dp),
                strokeWidth = 2.dp
            )
        }
        if ((isSuccess.first == DefectFieldType.PRIORITY
                    || isSuccess.first == DefectFieldType.STATUS
                    || isSuccess.first == DefectFieldType.RED_FLAG)
            && isSuccess.second
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_green_check_solid),
                contentDescription = "Completed",
                tint = null,
                modifier = Modifier
                    .size(22.dp)
                    .padding(end = 5.dp)
            )
        }
        if (offlineModifiedFields.contains(DefectFieldType.PRIORITY)
            || offlineModifiedFields.contains(DefectFieldType.STATUS)
            || offlineModifiedFields.contains(DefectFieldType.RED_FLAG)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_cloud_upload_solid),
                contentDescription = "Offline modified",
                modifier = Modifier
                    .size(30.dp)
                    .padding(start = 5.dp)
            )
        }

    }

    Box {
        Row(
            modifier = Modifier
                .padding(10.dp)

        ) {

            AnnotatedText(
                text = if (point.descriptionRich.isNotEmpty()) TextRichOptions
                    .convertBase64ToRichContentString(point.descriptionRich)
                    .fromHtml() else "Enter description...".fromHtml(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = if (point.descriptionRich.isNotEmpty()) onyx else LightGrey,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clickable(enabled = false) { }
            )
            if (isLoading.first == DefectFieldType.DESCRIPTION && isLoading.second) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(22.dp)
                        .padding(start = 5.dp, end = 5.dp),
                    strokeWidth = 2.dp
                )
            }
            if (isSuccess.first == DefectFieldType.DESCRIPTION && isSuccess.second) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_green_check_solid),
                    contentDescription = "Completed",
                    tint = null,
                    modifier = Modifier
                        .size(22.dp)
                        .padding(start = 5.dp, end = 5.dp)
                )
            }
            if (offlineModifiedFields.contains(DefectFieldType.DESCRIPTION)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cloud_upload_solid),
                    contentDescription = "Offline modified",
                    modifier = Modifier
                        .size(30.dp)
                        .padding(start = 5.dp)
                )
            }
        }



        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = LocalIndication.current
                ) {
                    navController.navigate(
                        Screen.RichTextEditorScreen.route +
                                "/${PointFieldType.DESCRIPTION.value}/${point.descriptionRich}"
                    )
                }
        )
    }

    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 10.dp),
        color = TransparentBlack
    )

    // Created by

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(R.drawable.ic_assignees),
            contentDescription = "Assignee Icon"
        )
        Spacer(modifier = Modifier.width(18.dp))
        Text(

            text = "Created by",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
        )
        val pointOwner = workspaceUsers.find { it.id == point.header?.createdBy?.id }
        val pointOwnerName = point.header?.createdBy?.caption ?: "user"
        Spacer(modifier = Modifier.width(15.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = if (pointOwner != null) pointOwnerName else ("$pointOwnerName (Deleted)"),
            fontSize = 16.sp,
            color = Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

    }
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 10.dp),
        color = TransparentBlack

    )

    // Assignees

    Column(
        modifier = Modifier.clickable(
            onClick = showAssigneeDialog
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(R.drawable.ic_assignees),
                contentDescription = "Assignee Icon"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(

                text = "Assignees",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )
            Spacer(modifier = Modifier.width(18.dp))
            Icon(

                painter = painterResource(R.drawable.ic_add_button_blue),
                contentDescription = "add Icon",
                tint = null
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            if (isLoading.first == DefectFieldType.ASSIGNEES && isLoading.second) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(22.dp)
                        .padding(start = 5.dp, end = 5.dp),
                    strokeWidth = 2.dp
                )
            }
            if (isSuccess.first == DefectFieldType.ASSIGNEES && isSuccess.second) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_green_check_solid),
                    contentDescription = "Completed",
                    tint = null,
                    modifier = Modifier
                        .size(22.dp)
                        .padding(start = 5.dp, end = 5.dp)
                )
            }
            if (offlineModifiedFields.contains(DefectFieldType.ASSIGNEES)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cloud_upload_solid),
                    contentDescription = "Offline modified",
                    modifier = Modifier
                        .size(30.dp)
                        .padding(start = 5.dp)
                )
            }


        }
        Spacer(modifier = Modifier.height(10.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 3
        ) {
            repeat(point.assignees.size) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 5.dp)
                        .background(
                            color = gray,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(vertical = 3.dp, horizontal = 10.dp),
                    text = point.assignees[it].caption, color = Color.White
                )
            }
        }

    }


    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 10.dp),
        color = TransparentBlack

    )

    // Tags

    Column(modifier = Modifier.clickable(onClick = showTagDialog)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(R.drawable.ic_tag),
                contentDescription = "Assignee Icon"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(

                text = "Tags",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )
            Spacer(modifier = Modifier.width(18.dp))
            Icon(
                painter = painterResource(R.drawable.ic_add_button_blue),
                contentDescription = "add Icon",
                tint = null
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            if (isLoading.first == DefectFieldType.TAGS && isLoading.second) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(22.dp)
                        .padding(start = 5.dp, end = 5.dp),
                    strokeWidth = 2.dp
                )
            }
            if (isSuccess.first == DefectFieldType.TAGS && isSuccess.second) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_green_check_solid),
                    contentDescription = "Completed",
                    tint = null,
                    modifier = Modifier
                        .size(22.dp)
                        .padding(start = 5.dp, end = 5.dp)
                )
            }

            if (offlineModifiedFields.contains(DefectFieldType.TAGS)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cloud_upload_solid),
                    contentDescription = "Offline modified",
                    modifier = Modifier
                        .size(30.dp)
                        .padding(start = 5.dp)
                )
            }

        }
        Spacer(modifier = Modifier.height(10.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 3
        ) {
            repeat(point.tags.size) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 5.dp)
                        .background(
                            color = gray,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(vertical = 3.dp, horizontal = 10.dp),
                    text = point.tags[it], color = Color.White
                )
            }
        }
    }

    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 10.dp),
        color = TransparentBlack

    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconWithBg(
    icon: Int = R.drawable.ic_flagged_gray,
    bgColor: Color = paleBlueGray,
    onClick: () -> Unit = {}
) {

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color = bgColor)
            .padding(horizontal = 17.dp, vertical = 5.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    )
    {
        Icon(
            modifier = Modifier.size(18.dp),
            painter = painterResource(icon),
            contentDescription = "Icon",
            tint = null,
        )
    }
}

@Composable
fun TextChipWithIcon(
    text: String = "",
    icon: Int = R.drawable.ic_open_light,
    color: Color = Goldenrod,
    shouldShowIcon: Boolean = false,
    onClick: () -> Unit
) {

    val isIconVisible = remember { shouldShowIcon }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color = color)
            .padding(horizontal = 15.dp, vertical = 4.dp)
            .clickable(
                onClick = onClick
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (isIconVisible)
            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(icon),
                contentDescription = "Icon",
                tint = Color.White,
            )
        Spacer(modifier = Modifier.width(5.dp))
        Text(

            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }

}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PhotoSection(
    point: PointDomain,
    workspaceId: String,
    navController: NavHostController,
    offlineModifiedFields: Map<String, OfflineModifiedPointFields>,
    showPhotoSelectionDialog: () -> Unit,
) {
    Row(modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)) {
        Icon(
            painter = painterResource(id = R.drawable.ic_camera_solid),
            contentDescription = "Completed",
            tint = null,
            modifier = Modifier
                .size(22.dp)
                .padding(end = 5.dp)
        )

        Text(

            text = "Photos",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 5.dp)
        )

        if (offlineModifiedFields.contains(DefectFieldType.IMAGES + point.id)) {
            Icon(
                painter = painterResource(id = R.drawable.ic_cloud_upload_solid),
                contentDescription = "Offline modified",
                modifier = Modifier
                    .size(30.dp)
            )
        }
    }
    val context = LocalContext.current
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 5.dp),
        maxItemsInEachRow = 4,
    ) {
        repeat(point.images.size) {
            val thumbnailFile = InternalWorkspaceStorageUtils().getFileReference(
                workspaceId,
                "$workspaceId/images/thumb/",
                point.images[it].id + ".png",
                context
            )
            Card(
                modifier = Modifier
                    .size(100.dp)
                    .padding(horizontal = 2.dp, vertical = 5.dp)
                    .clickable {
                        navController.navigate(Screen.PhotoViewScreen.route + "/$it" + "/${point.id}")
                    },
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(thumbnailFile)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Thumbnail",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.ic_image_placeholder),
                    error = painterResource(R.drawable.ic_image_placeholder)
                )
            }
        }


    }


}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun VideoSection(
    point: PointDomain,
    offlineModifiedFields: Map<String, OfflineModifiedPointFields>,
    navController: NavHostController
) {
    Row(modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)) {
        Icon(
            painter = painterResource(id = R.drawable.ic_video_solid),
            contentDescription = "Video section icon",
            tint = null,
            modifier = Modifier
                .size(22.dp)
                .padding(end = 5.dp)
        )

        Text(

            text = "Videos",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 5.dp)
        )

        if (offlineModifiedFields.contains(DefectFieldType.VIDEOS + point.id)) {
            Icon(
                painter = painterResource(id = R.drawable.ic_cloud_upload_solid),
                contentDescription = "Offline modified",
                modifier = Modifier
                    .size(30.dp)
            )
        }
    }
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 15.dp),
        maxItemsInEachRow = 4,
    ) {
        repeat(point.videos.size) {

            Text(
                point.videos[it].caption ?: "",
                style = TextStyle(color = onyx, fontSize = 16.sp),
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .clickable {
                        navController.navigate(Screen.VideoPlayerScreen.route + "/${point.videos[it].id}/${point.id}")
                    }
            )

        }


    }


}

@Composable
fun CommentSection(
    comments: List<CommentDomain>,
    viewModel: PointDetailViewModel,
    offlineModifiedFields: Map<String, OfflineModifiedPointFields>,
    onLikedClicked: (CommentDomain, Boolean) -> Unit,
) {

    Row(
        modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_comment_alt_solid),
            contentDescription = "Comment"
        )
        Text(

            text = "Comments",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 5.dp)
        )

        if (offlineModifiedFields.contains(DefectFieldType.REACTION) || offlineModifiedFields.contains(
                DefectFieldType.COMMENT
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_cloud_upload_solid),
                contentDescription = "Offline modified",
                modifier = Modifier
                    .size(30.dp)
            )
        }

    }
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 15.dp),
        maxItemsInEachRow = 1,
    ) {
        repeat(comments.size) {


            CommentItem(comments[it], viewModel, onLikedClicked)
        }


    }


}

@Composable
fun CommentItem(
    comment: CommentDomain,
    viewModel: PointDetailViewModel,
    onLikeClick: (CommentDomain, Boolean) -> Unit
) {
    val user by viewModel.user.collectAsState()
    val avatarSignal by viewModel.avatarUpdateSignal.collectAsState(initial = "")
    var isLikedByMe by remember { mutableStateOf(comment.reactions?.like?.contains(user.id) == true) }
    val avatarFile = remember(avatarSignal) {
        viewModel.getUserAvatar(
            comment.header?.createdBy?.primaryImageId ?: comment.author?.images[0]?.id ?: ""
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        val authorAndDate =
            "${comment.header?.updatedBy?.caption ?: comment.author?.name} - ${comment.header?.updatedEpochMillis?.formatCommentDateTime() ?: ""}"

        Row(
            modifier = Modifier
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {

            CircularImage(
                comment.header?.createdBy?.caption ?: comment.author?.name,
                avatarFile,
                45
            )
            Spacer(modifier = Modifier.width(20.dp))

            var commentText = TextRichOptions
                .convertBase64ToRichContentString(comment.commentRich)
            if (commentText.endsWith("<br>")) {
                commentText = commentText.replace("<br>", "")
            }
            Column {

                // Comment Text
                AnnotatedText(
                    text = commentText
                        .fromHtml(),
                    fontSize = 16.sp,
                    color = Color.Black
                )

//                // Updated date (optional)
//                if (showUpdatedTime && updatedDateTime != null) {
//                    Text(
//                        text = updatedDateTime,
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = colorResource(id = R.color.comment_date_color)
//                    )
//                }

                // Author + date
                Text(
                    text = authorAndDate,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = gray
                )

                // Likes row
                if (comment.reactions?.like?.isNotEmpty() == true) {
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (isLikedByMe)
                                    R.drawable.thumb_up_blue
                                else
                                    R.drawable.thumb_up_blue_empty
                            ),
                            contentDescription = "Like Icon",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(15.dp)
                        )

                        Spacer(modifier = Modifier.width(5.dp))
                        val text = if (isLikedByMe) {
                            if (comment.reactions.like.size == 1) {
                                " You liked this"
                            } else {
                                "You &" + comment.reactions.like.size.toString() + " other liked this"
                            }
                        } else {
                            " ${comment.reactions.like.size} other liked this"
                        }
                        Text(
                            text = text,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrightBlue
                        )
                    }
                }
            }
        }

        // Like button (right side)
        val icon = if (isLikedByMe) painterResource(id = R.drawable.thumb_up_blue) else {
            if (comment.reactions?.like?.isNotEmpty() == true) painterResource(id = R.drawable.thumb_up_blue_empty)
            else painterResource(
                id =
                    R.drawable.thumb_up
            )
        }
        Icon(
            painter = icon,
            contentDescription = "Like Button",
            modifier = Modifier
                .size(30.dp)
                .padding(horizontal = 5.dp)
                .clickable {

                    onLikeClick(comment, isLikedByMe)
                    isLikedByMe = !isLikedByMe
                },
            tint = Color.Unspecified
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefectDetailScreenPreview() {
    PinpointworksNewTheme {
//        DefectDetailScreen()
    }
}