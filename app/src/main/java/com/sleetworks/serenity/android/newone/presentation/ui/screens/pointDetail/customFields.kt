package com.sleetworks.serenity.android.newone.presentation.ui.screens.pointDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aghajari.compose.text.AnnotatedText
import com.aghajari.compose.text.fromHtml
import com.sleetworks.serenity.android.newone.R
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.customfield.SubListItem
import com.sleetworks.serenity.android.newone.domain.models.share.CustomFieldPermissionDomainModel
import com.sleetworks.serenity.android.newone.presentation.model.CustomFieldTempUI
import com.sleetworks.serenity.android.newone.presentation.ui.model.CustomFieldType
import com.sleetworks.serenity.android.newone.ui.theme.BrightBlue
import com.sleetworks.serenity.android.newone.ui.theme.Grey
import com.sleetworks.serenity.android.newone.ui.theme.TransparentBlack
import com.sleetworks.serenity.android.newone.utils.TextRichOptions
import com.sleetworks.serenity.android.newone.utils.convertTimeMilisToDisplayValue
import com.sleetworks.serenity.android.newone.utils.formatFormulaCfOutputNumber
import com.sleetworks.serenity.android.newone.utils.formulaCFOutputCostValueFormat
import com.sleetworks.serenity.android.newone.utils.formulaCFOutputPercentageValueFormat
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomFieldTextLayout(
    customField: CustomFieldTempUI,
    value: String = "",
    permission: CustomFieldPermissionDomainModel,
    isLoading: Boolean = false,
    isCompleted: Boolean = false,
    isOfflineModified: Boolean = false,
    onValueChange: (String) -> Unit,
    onNotEditClick: () -> Unit = {},
    onLockedClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showEditTextDialog by remember {
        mutableStateOf(false)
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    // Hide keyboard after dialog is dismissed with a small delay
    LaunchedEffect(showEditTextDialog) {
        if (!showEditTextDialog) {
            delay(100) // Small delay to ensure dialog is fully dismissed
            keyboardController?.hide()
        }
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .clickable {
                if (customField.lockedValue == true) {
                    onLockedClick()
                } else if (permission.permission.edit == true) {
                    showEditTextDialog = true
                } else {
                    onNotEditClick()
                }
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_font_solid),
                contentDescription = "Text field icon",
                modifier = Modifier.padding(horizontal = 5.dp)
            )

            // Label
            Text(
                text = customField.label,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 5.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Status indicators
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(22.dp)
                            .padding(end = 5.dp),
                        strokeWidth = 2.dp
                    )
                }

                isCompleted -> {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_green_check_solid),
                        contentDescription = "Completed",
                        tint = null,
                        modifier = Modifier
                            .size(22.dp)
                            .padding(end = 5.dp)
                    )
                }

                isOfflineModified -> {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_cloud_upload_solid),
                        contentDescription = "Offline modified",
                        modifier = Modifier
                            .size(30.dp)
                            .padding(end = 5.dp)
                    )
                }
            }
        }

        Text(
            text = value.ifEmpty { "Enter text..." },
            fontSize = 16.sp,
            color = if (value.isEmpty()) Color.Gray else Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 35.dp, bottom = 10.dp)
        )


        // Divider
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = TransparentBlack,
            thickness = 1.dp
        )
    }

    if (showEditTextDialog)
        PointTextEditDialog(
            customField.label,
            customField = customField,
            initialValue = value,
            onDismiss = {
                showEditTextDialog = false
//            keyboardController?.hide()
            },
            onConfirm = { value ->
                showEditTextDialog = false
                onValueChange(value)

            })
}


@Composable
fun CustomFieldCostLayout(
    customField: CustomFieldTempUI,
    value: String = "",
    permission: CustomFieldPermissionDomainModel,
    isLoading: Boolean = false,
    isCompleted: Boolean = false,
    isOfflineModified: Boolean = false,
    onAddClick: () -> Unit = {},
    onViewClick: () -> Unit = {},
    onLockedClick: () -> Unit = {},
    onNotEditable: () -> Unit = {},
    onValueChanged: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showEditTextDialog by remember {
        mutableStateOf(false)
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(showEditTextDialog) {
        if (!showEditTextDialog) {
            delay(100)
            keyboardController?.hide()
        }
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .clickable {
                if (customField.lockedValue == true) {
                    onLockedClick()
                } else if (permission.permission.edit == false) {
                    onNotEditable()
                } else {
                    if (customField.subValuesActive == false) {
                        showEditTextDialog = true
                    }
                }
            }
    ) {
        // Header row with icon, label, and status indicators
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Money bill icon
            val mainIcon = if (customField.volyIntegrationActive != null) {
                if (customField.volyIntegrationActive) {
                    R.drawable.voly_connection_status_active
                } else {
                    R.drawable.voly_connection_status_inactive
                }
            } else {
                R.drawable.ic_money_bill_gray
            }
            Icon(
                painter = painterResource(id = mainIcon),
                contentDescription = "Cost field icon",
                modifier = Modifier.padding(horizontal = 5.dp)
            )

            // Label
            Text(
                text = customField.label,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier.padding(horizontal = 5.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Status indicators
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(22.dp)
                            .padding(end = 5.dp),
                        strokeWidth = 2.dp
                    )
                }

                isCompleted -> {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_green_check_solid),
                        contentDescription = "Completed",
                        modifier = Modifier
                            .size(22.dp)
                            .padding(end = 5.dp)
                    )
                }

                isOfflineModified -> {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_cloud_upload_solid),
                        contentDescription = "Offline modified",
                        modifier = Modifier
                            .size(30.dp)
                            .padding(end = 5.dp)
                    )
                }
            }
        }

        // Cost input section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 35.dp, top = 5.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Currency code
            Text(
                text = customField.currencyCode ?: "",
                color = Color.Black,
                fontSize = 16.sp
            )

            // Currency symbol
            Text(
                text = customField.currencySymbol ?: "",
                color = Color.Black,
                fontSize = 16.sp
            )

            Text(
                text = if (value.isNotEmpty()) value.toDouble()
                    .formulaCFOutputCostValueFormat() else "---.--",
                color = if (value.isEmpty()) Color.Gray else Color.Black,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(end = 8.dp)
            )



            if (customField.subValuesActive == true) {
                Text(
                    text = "Total",
                    color = Grey,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 30.dp)
                )
                if (permission.permission.edit == true) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_add_button_blue),
                        contentDescription = "Add sub-fields",
                        tint = BrightBlue,
                        modifier = Modifier
                            .padding(start = 10.dp, end = 16.dp)
                            .clickable {
                                onAddClick()
                            }

                    )

                } else {

                    Icon(
                        painter = painterResource(id = R.drawable.icon_next_read_only),
                        contentDescription = "View details",
                        tint = BrightBlue,
                        modifier = Modifier
                            .padding(start = 10.dp, end = 16.dp)
                            .clickable {
                                onViewClick
                            }

                    )

                }

            }


        }

        // Divider
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = TransparentBlack,
            thickness = 1.dp
        )
    }
    if (showEditTextDialog)
        PointTextEditDialog(
            customField.label,
            initialValue = value,
            customField = customField,
            onDismiss = {
                showEditTextDialog = false
//            keyboardController?.hide()
            },
            onConfirm = { value ->
                showEditTextDialog = false
                onValueChanged(value)

            })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomFieldDateLayout(
    customField: CustomFieldTempUI,
    value: String = "",
    permission: CustomFieldPermissionDomainModel,
    isLoading: Boolean = false,
    isCompleted: Boolean = false,
    isOfflineModified: Boolean = false,
    onLockedClick: () -> Unit,
    onNotEditable: () -> Unit,
    onDateChange: (Long) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showDateDialog by remember {
        mutableStateOf(false)
    }
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = if (value.isNotEmpty() && value != "0") sdf.format(Date(value.toLong())) else ""
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .clickable {
                if (customField.lockedValue == true) {
                    onLockedClick()
                } else if (permission.permission.edit == false) {
                    onNotEditable()
                } else {
                    showDateDialog = true
                }
            }
    ) {
        // Header row with icon, label, and status indicators
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Calendar icon
            Icon(
                painter = painterResource(id = R.drawable.ic_calendar_gray),
                contentDescription = "Date field icon",
                modifier = Modifier.padding(horizontal = 5.dp)
            )

            // Label
            Text(
                text = customField.label,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF000000) // label_black color
                ),
                modifier = Modifier.padding(horizontal = 5.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Status indicators
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(22.dp)
                            .padding(end = 5.dp),
                        strokeWidth = 2.dp
                    )
                }

                isCompleted -> {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_green_check_solid),
                        contentDescription = "Completed",
                        tint = null,
                        modifier = Modifier
                            .size(22.dp)
                            .padding(end = 5.dp)
                    )
                }

                isOfflineModified -> {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_cloud_upload_solid),
                        contentDescription = "Offline modified",
                        modifier = Modifier
                            .size(30.dp)
                            .padding(end = 5.dp)
                    )
                }
            }
        }

        // Date input section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 35.dp, top = 5.dp, bottom = 10.dp)
        ) {

            Text(
                text = date.ifEmpty { "Select a date..." },
                color = if (value.isEmpty()) Color.Gray else Color.Black,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            )


            // Dropdown arrow
            Icon(
                painter = painterResource(id = R.drawable.ic_menu_down_black_24dp),
                contentDescription = "Date picker",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(30.dp)
            )
        }

        // Divider
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = TransparentBlack,
            thickness = 1.dp
        )
    }

    if (showDateDialog) {
        CustomDatePickerDialog(
            date = value,
            onDismiss = {
                showDateDialog = false
            },
            onValueChange = { dateMilli ->
                showDateDialog = false
                onDateChange(dateMilli)
            }

        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomFieldListLayout(
    customField: CustomFieldTempUI,
    value: String = "",
    selectedValueId: String = "",
    permission: CustomFieldPermissionDomainModel,
    isLoading: Boolean = false,
    isCompleted: Boolean = false,
    isOfflineModified: Boolean = false,
    onLockedClick: () -> Unit,
    onNotEditable: () -> Unit,
    onOptionSelected: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(showDialog) {
        if (!showDialog) {
            delay(100)
            keyboardController?.hide()
        }
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .clickable {
                if (customField.lockedValue == true) {
                    onLockedClick()
                } else if (permission.permission.edit == false) {
                    onNotEditable()
                } else {
                    showDialog = true
                }
            }
    ) {
        // Header row with icon, label, and status indicators
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (customField.volyIntegrationActive != null) {
                Icon(
                    painter = painterResource(
                        id = if (customField.volyIntegrationActive) {
                            R.drawable.voly_connection_status_active
                        } else {
                            R.drawable.voly_connection_status_inactive
                        }
                    ),
                    contentDescription = "List field icon",
                    modifier = Modifier.padding(horizontal = 5.dp)
                )
            } else {

                Icon(
                    painter = painterResource(id = R.drawable.ic_list_gray),
                    contentDescription = "List field icon",
                    modifier = Modifier.padding(horizontal = 5.dp)

                )
            }

            // Label
            Text(
                text = customField.label,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF000000)
                ),
                modifier = Modifier.padding(horizontal = 5.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Status indicators
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(22.dp)
                            .padding(end = 5.dp),
                        strokeWidth = 2.dp
                    )
                }

                isCompleted -> {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_green_check_solid),
                        contentDescription = "Completed",
                        tint = null,
                        modifier = Modifier
                            .size(22.dp)
                            .padding(end = 5.dp)
                    )
                }

                isOfflineModified -> {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_cloud_upload_solid),
                        contentDescription = "Offline modified",
                        modifier = Modifier
                            .size(30.dp)
                            .padding(end = 5.dp)
                    )
                }
            }
        }

        // List selection area - ALWAYS show regardless of volyIntegrationActive status
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 35.dp, top = 5.dp, bottom = 10.dp)
        ) {
//            // Find the selected option by ID and display the label
//            val selectedOption = findSubListItemById(customField.subList, selectedValueId)
//            val displayText = selectedOption?.label ?: selectedValueId.ifEmpty { "Select..." }

            val text = retrieveLabel(selectedValueId, customField.subList?:emptyList(), false, "")
            Text(
                text = text?.ifEmpty { "Select..." }?:"Select...",
                color = if (selectedValueId.isEmpty()) Color.Gray else Color.Black,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            )

            // Dropdown arrow
            Icon(
                painter = painterResource(id = R.drawable.ic_menu_down_black_24dp),
                contentDescription = "List picker",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(30.dp)
            )
        }

        // Divider
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = TransparentBlack,
            thickness = 1.dp
        )
    }

    if (showDialog) {
        ListDialogInCompose(
            onDismissRequest = { showDialog = false },
            customField = customField,
            initialSelectedId = selectedValueId,
            onAccept = { selectedId ->
                if (selectedId != null && selectedId != selectedValueId) {
                    showDialog = false
                    onOptionSelected(selectedId)

                }
            },
            onClear = {

            }
        )
    }
}

private fun retrieveLabel(
    idOfChosenElement: String,
    sublists: List<SubListItem>?,
    found: Boolean,
    value: String?
): String? {
    if (sublists == null) return null
    for (sublist in sublists) {
        if (found) break
        if (sublist.id.toString() == idOfChosenElement) {
            return retrieveLabel(
                idOfChosenElement,
                sublist.subList?:emptyList(),
                true,
                value + sublist.label
            )
        } else {
            if (sublist
                    ?.subList?.isNotEmpty() == true
            ) {
                val `val` = retrieveLabel(
                    idOfChosenElement,
                    sublist.subList?:emptyList(),
                    false,
                    value + sublist.label + "/"
                )
                if (`val` != null) return `val`
            }
        }
    }
    if (found) return value
    return null
}

fun findSubListItemById(subList: List<SubListItem>?, id: String): SubListItem? {
    subList ?: return null // Return null if subList is null
    for (item in subList) {
        if (item.id.toString() == id) {
            return item
        }
        // Recursively search in nested subList
        val found = findSubListItemById(item.subList, id)
        if (found != null) {
            return found
        }
    }
    return null
}

@Composable
fun CustomFieldNumbersLayout(
    customField: CustomFieldTempUI,
    value: String = "",
    permission: CustomFieldPermissionDomainModel,
    isLoading: Boolean = false,
    isCompleted: Boolean = false,
    isOfflineModified: Boolean = false,
    onAddClick: () -> Unit = {},
    onViewClick: () -> Unit = {},
    onLockedClick: () -> Unit = {},
    onNotEditable: () -> Unit = {},
    onValueChanged: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {

    var showEditTextDialog by remember {
        mutableStateOf(false)
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(showEditTextDialog) {
        if (!showEditTextDialog) {
            delay(100)
            keyboardController?.hide()
        }
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .clickable {
                if (customField.lockedValue == true) {
                    onLockedClick()
                } else if (permission.permission.edit == false) {
                    onNotEditable()
                } else {
                    if (customField.subValuesActive == false) {
                        showEditTextDialog = true
                    }
                }
            }
    ) {
        // Header row with icon, label, and status indicators
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Numbers icon
            Icon(
                painter = painterResource(id = R.drawable.ic_numbers),
                contentDescription = "Numbers field icon",
                modifier = Modifier.padding(horizontal = 5.dp)
            )

            // Label
            Text(
                text = customField.label,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier.padding(horizontal = 5.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Status indicators
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(22.dp)
                            .padding(end = 5.dp),
                        strokeWidth = 2.dp
                    )
                }

                isCompleted -> {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_green_check_solid),
                        contentDescription = "Completed",
                        modifier = Modifier
                            .size(22.dp)
                            .padding(end = 5.dp)
                    )
                }

                isOfflineModified -> {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_cloud_upload_solid),
                        contentDescription = "Offline modified",
                        modifier = Modifier
                            .size(30.dp)
                            .padding(end = 5.dp)
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 35.dp, top = 5.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Unit prefix
            if (customField.unit?.isNotEmpty() == true) {
                Text(
                    text = "${customField.unit} ",
                    color = Color.Black,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(end = 5.dp)
                )
            }


            Text(
                text = if (value.isNotEmpty()) value.replace(",", "").toDouble()
                    .formatFormulaCfOutputNumber(
                        customField.decimalPlaces ?: 0,
                        customField.showCommas == true
                    ) else "---.--",
                color = if (value.isEmpty()) Color.Gray else Color.Black,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(end = 8.dp)
            )


            // Action buttons
            if (customField.subValuesActive == true) {
                Text(
                    text = "Total",
                    color = Grey,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 30.dp)
                )
                if (permission.permission.edit == true) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_add_button_blue),
                        contentDescription = "Add sub-fields",
                        tint = BrightBlue,
                        modifier = Modifier
                            .padding(start = 10.dp, end = 16.dp)
                            .clickable {
                                onAddClick()
                            }

                    )

                } else {

                    Icon(
                        painter = painterResource(id = R.drawable.icon_next_read_only),
                        contentDescription = "View details",
                        tint = BrightBlue,
                        modifier = Modifier
                            .padding(start = 10.dp, end = 16.dp)
                            .clickable {
                                onViewClick
                            }

                    )

                }

            }
        }

        // Divider
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = TransparentBlack,
            thickness = 1.dp
        )
    }

    if (showEditTextDialog)
        PointTextEditDialog(
            customField.label,
            initialValue = value,
            customField = customField,
            onDismiss = {
                showEditTextDialog = false
//            keyboardController?.hide()
            },
            onConfirm = { value ->
                showEditTextDialog = false
                onValueChanged(value)

            })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomFieldRichTextLayout(
    customField: CustomFieldTempUI,
    content: String = "",
    permission: CustomFieldPermissionDomainModel,
    isLoading: Boolean = false,
    isCompleted: Boolean = false,
    isOfflineModified: Boolean = false,
    onEditClick: () -> Unit = {},
    onLockedClick: () -> Unit = {},
    onNotEditable: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val richValue = TextRichOptions.convertBase64ToRichContentString(content)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .clickable {
                if (customField.lockedValue == true) {
                    onLockedClick()
                } else if (permission.permission.edit == false) {
                    onNotEditable()
                } else {
                    onEditClick()
                }
            }
    ) {
        // Header row with icon, label, and status indicators
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rich text icon
            Icon(
                painter = painterResource(id = R.drawable.ic_rich_text),
                contentDescription = "Rich text field icon",
                modifier = Modifier.padding(horizontal = 5.dp)
            )

            // Label
            Text(
                text = customField.label,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF000000) // label_black color
                ),
                modifier = Modifier.padding(horizontal = 5.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Status indicators
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(22.dp)
                            .padding(end = 5.dp),
                        strokeWidth = 2.dp
                    )
                }

                isCompleted -> {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_green_check_solid),
                        contentDescription = "Completed",
                        tint = null,
                        modifier = Modifier
                            .size(22.dp)
                            .padding(end = 5.dp)
                    )
                }

                isOfflineModified -> {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_cloud_upload_solid),
                        contentDescription = "Offline modified",
                        modifier = Modifier
                            .size(30.dp)
                            .padding(end = 5.dp)
                    )
                }
            }
        }

        RichTextDisplay(
            content = richValue,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 35.dp, bottom = 10.dp)
        )


        // Divider
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = TransparentBlack,
            thickness = 1.dp
        )
    }
}

@Composable
private fun RichTextDisplay(
    content: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        if (content.isEmpty()) {
            Text(
                text = "Enter text...",
                color = Color.Gray,
                fontSize = 16.sp
            )
        } else {
            RichTextContent(
                content = content,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}


@Composable
private fun RichTextContent(
    content: String,
    fontSize: TextUnit,
    color: Color,
    modifier: Modifier = Modifier
) {

    AnnotatedText(
        text = content.fromHtml(),
        fontSize = fontSize,
        color = color,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeCustomFieldLayout(
    customField: CustomFieldTempUI,
    value: String = "Total –",
    permission: CustomFieldPermissionDomainModel,
    isLoading: Boolean = false,
    isCompleted: Boolean = false,
    isOfflineModified: Boolean = false,
    onValueChanged: (String) -> Unit = {},
    onLockedClick: () -> Unit = {},
    onNotEditable: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showTimeDialog by remember {
        mutableStateOf(false)
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(showTimeDialog) {
        if (!showTimeDialog) {
            delay(100)
            keyboardController?.hide()
        }
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable {
                if (customField.lockedValue == true) {
                    onLockedClick()
                } else if (permission.permission.edit == false) {
                    onNotEditable()
                } else {
                    showTimeDialog = true
                }
            }
    ) {
        // Header section with icon, label, and status indicators
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Time icon using ic_time drawable
            Image(
                painter = painterResource(id = R.drawable.ic_time),
                contentDescription = "Time",
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Label
            Text(
                text = customField.label,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )

            // Status indicators
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.dp
                    )
                } else if (isCompleted) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_green_check_solid),
                        contentDescription = "Synced",
                        tint = null,
                        modifier = Modifier.size(22.dp)
                    )
                } else if (isOfflineModified) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_cloud_upload_solid),
                        contentDescription = "Offline Modified",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }

        // Time value section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 56.dp, end = 16.dp, top = 10.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Add time button using ic_add_button_blue drawable
            Image(
                painter = painterResource(id = R.drawable.ic_add_button_blue),
                contentDescription = "Add Time",
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Time value text
            Text(
                text = if (value.isNotEmpty()) value.convertTimeMilisToDisplayValue(customField.showHoursOnly == true)
                else "Total \uFEFF–",
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }

        // Divider
        HorizontalDivider(
            color = TransparentBlack,
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )
    }

    if (showTimeDialog) {
        TimeEditDialog(
            title = customField.label,
            isHoursOnly = customField.showHoursOnly == true,
            onDismiss = { showTimeDialog = false },
            onSave = { days, hours, minutes ->

                val editTime =
                    60000 * (minutes.toLong() + 60 * (hours.toLong() + 24 * days.toLong()))
                onValueChanged(editTime.toString())
                showTimeDialog = false
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomFieldPercentageLayout(
    customField: CustomFieldTempUI,
    value: String = "",
    permission: CustomFieldPermissionDomainModel,
    isLoading: Boolean = false,
    isCompleted: Boolean = false,
    isOfflineModified: Boolean = false,
    onAddClick: () -> Unit = {},
    onViewClick: () -> Unit = {},
    onLockedClick: () -> Unit = {},
    onNotEditable: () -> Unit = {},
    onValueChanged: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showEditTextDialog by remember {
        mutableStateOf(false)
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(showEditTextDialog) {
        if (!showEditTextDialog) {
            delay(100)
            keyboardController?.hide()
        }
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                if (customField.lockedValue == true) {
                    onLockedClick()
                } else if (permission.permission.edit == false) {
                    onNotEditable()
                } else {
                    if (customField.subValuesActive == false) {
                        showEditTextDialog = true
                    }
                }
            }
    ) {
        // Header row with icon, label, and status indicators
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Percentage icon
            Icon(
                painter = painterResource(id = R.drawable.ic_percentage),
                contentDescription = "Percentage field icon",
                modifier = Modifier.padding(horizontal = 5.dp)
            )

            // Label
            Text(
                text = customField.label,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF000000) // label_black color
                ),
                modifier = Modifier.padding(horizontal = 5.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Status indicators
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(22.dp)
                            .padding(end = 5.dp),
                        strokeWidth = 2.dp
                    )
                }

                isCompleted -> {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_green_check_solid),
                        contentDescription = "Completed",
                        tint = null,
                        modifier = Modifier
                            .size(22.dp)
                            .padding(end = 5.dp)
                    )
                }

                isOfflineModified -> {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_cloud_upload_solid),
                        contentDescription = "Offline modified",
                        modifier = Modifier
                            .size(30.dp)
                            .padding(end = 5.dp)
                    )
                }
            }
        }

        // Percentage input section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 35.dp, top = 5.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Percentage symbol
            Text(
                text = "%",
                color = Color.Black,
                fontSize = 16.sp,
                modifier = Modifier.padding(end = 5.dp)
            )

            // Value input/display
            if (permission.permission.edit == true) {
                Text(
                    text = if (value.isNotEmpty()) value.toDouble()
                        .formulaCFOutputPercentageValueFormat() else "---.--",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                    modifier = Modifier
//                        .weight(1f)
                        .padding(end = 8.dp)
                )
            } else {
                Text(
                    text = value.ifEmpty { "---.--" },
                    color = if (value.isEmpty()) Color.Gray else Color.Black,
                    fontSize = 16.sp,

                    )
            }

            // Total tag
            if (customField.subValuesActive == true) {
                Text(
                    text = "Total",
                    color = Grey,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 30.dp)
                )
                if (permission.permission.edit == true) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_add_button_blue),
                        contentDescription = "Add sub-fields",
                        tint = BrightBlue,
                        modifier = Modifier
                            .padding(start = 10.dp, end = 16.dp)
                            .clickable {
                                onAddClick()
                            }

                    )

                } else {

                    Icon(
                        painter = painterResource(id = R.drawable.icon_next_read_only),
                        contentDescription = "View details",
                        tint = BrightBlue,
                        modifier = Modifier
                            .padding(start = 10.dp, end = 16.dp)
                            .clickable {
                                onViewClick()
                            }

                    )

                }

            }

        }

        // Divider
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = TransparentBlack,
            thickness = 1.dp
        )
    }


    if (showEditTextDialog)
        PointTextEditDialog(
            customField.label,
            initialValue = value,
            customField = customField,
            onDismiss = {
                showEditTextDialog = false
            },
            onConfirm = { value ->
                showEditTextDialog = false
                onValueChanged(value)

            })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomFieldCheckboxLayout(
    customField: CustomFieldTempUI,
    checked: Boolean,
    permission: CustomFieldPermissionDomainModel,
    isLoading: Boolean = false,
    isCompleted: Boolean = false,
    isOfflineModified: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
    onLockedClick: () -> Unit = {},
    onNotEditable: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isChecked by remember { mutableStateOf(checked) }
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {

        // Header row: icon, label, status indicators
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_checkbox), // header icon
                contentDescription = "Checkbox field icon",
                modifier = Modifier
                    .size(35.dp)
                    .padding(horizontal = 5.dp)
            )
            Text(
                text = customField.label,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier.padding(horizontal = 5.dp)
            )
            Spacer(modifier = Modifier.weight(1f))

            when {
                isLoading -> CircularProgressIndicator(
                    modifier = Modifier
                        .size(22.dp)
                        .padding(end = 5.dp),
                    strokeWidth = 2.dp
                )

                isCompleted -> Icon(
                    painter = painterResource(id = R.drawable.ic_green_check_solid),
                    contentDescription = "Completed",
                    tint = null,
                    modifier = Modifier
                        .size(22.dp)
                        .padding(end = 5.dp)
                )

                isOfflineModified -> Icon(
                    painter = painterResource(id = R.drawable.ic_cloud_upload_solid),
                    contentDescription = "Offline modified",
                    modifier = Modifier
                        .size(30.dp)
                        .padding(end = 5.dp)
                )
            }
        }

        // Checkbox row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 35.dp, top = 5.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(
                    id = if (isChecked) R.drawable.ic_check_square else R.drawable.ic_uncheck_square
                ),
                contentDescription = if (isChecked) "Checked" else "Unchecked",
                modifier = Modifier
                    .size(35.dp)
                    .padding(start = 10.dp)
                    .clickable {
                        if (customField.lockedValue == true) {
                            onLockedClick()
                        } else if (permission.permission.edit == false) {
                            onNotEditable()
                        } else {
                            isChecked = !isChecked
                            onCheckedChange(isChecked)
                        }
                    }
            )
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = TransparentBlack,
            thickness = 1.dp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomFieldTimelineLayout(
    customField: CustomFieldTempUI,
    value: String = "",
    permission: CustomFieldPermissionDomainModel,
    isLoading: Boolean = false,
    isCompleted: Boolean = false,
    isOfflineModified: Boolean = false,
    onLockedClick: () -> Unit = {},
    onNotEditable: () -> Unit = {},
    onDateChange: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showFromDateDialog by remember {
        mutableStateOf(false)
    }
    var showToDateDialog by remember {
        mutableStateOf(false)
    }
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    var isToDateDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("") }
    val dates = value.split("~")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                if (customField.lockedValue == true) {
                    onLockedClick()
                } else if (permission.permission.edit == false) {
                    onNotEditable()
                } else {
                    showFromDateDialog = true
                }
            }) {

        // Header: icon + label + status indicators
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_timeline),
                contentDescription = "Timeline field icon",
                modifier = Modifier.padding(horizontal = 5.dp)
            )
            Text(
                text = customField.label,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black// label_black
                ),
                modifier = Modifier.padding(horizontal = 5.dp)
            )
            Spacer(modifier = Modifier.weight(1f))

            when {
                isLoading -> CircularProgressIndicator(
                    modifier = Modifier
                        .size(22.dp)
                        .padding(end = 5.dp),
                    strokeWidth = 2.dp
                )

                isCompleted -> Icon(
                    painter = painterResource(id = R.drawable.ic_green_check_solid),
                    contentDescription = "Completed",
                    tint = null,
                    modifier = Modifier
                        .size(22.dp)
                        .padding(end = 5.dp)
                )

                isOfflineModified -> Icon(
                    painter = painterResource(id = R.drawable.ic_cloud_upload_solid),
                    contentDescription = "Offline modified",
                    modifier = Modifier
                        .size(30.dp)
                        .padding(end = 5.dp)
                )
            }
        }

        // Body: placeholder or timeline values
        val hasDates = value.isNotBlank()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 35.dp, top = 5.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!hasDates) {
                // Placeholder: From/To
                Text(
                    text = "From/To",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            } else {
                var dateFrom = ""
                var dateTo = ""
                if (dates.size == 2) {
                    var dateVal = Date(dates[0].toLong())
                    dateFrom = sdf.format(dateVal)
                    dateVal = Date(dates[1].toLong())
                    dateTo = sdf.format(dateVal)
                }
                Text(
                    text = "From ",
                    color = Color.Black,
                    fontSize = 16.sp
                )
                Text(
                    text = dateFrom.ifEmpty { "—" },
                    color = Color.Black,
                    fontSize = 16.sp
                )
                Text(
                    text = "  To ",
                    color = Color.Black,
                    fontSize = 16.sp
                )
                Text(
                    text = dateTo.ifEmpty { "—" },
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = TransparentBlack,
            thickness = 1.dp
        )
    }


    if (showFromDateDialog) {
        CustomDatePickerDialog(
            title = "Select date from",
            date = dates[0],
            onDismiss = {
                showFromDateDialog = false
            },
            onValueChange = { dateMilli ->
                selectedDate = dateMilli.toString()
                showFromDateDialog = false
                showToDateDialog = true

            }

        )
    }

    if (showToDateDialog) {
        CustomDatePickerDialog(
            title = "Select date to",
            date = dates[1],
            minDateMilli = selectedDate.toLong(),
            onDismiss = {
                showToDateDialog = false
                selectedDate = ""
            },
            onValueChange = { dateMilli ->
                showToDateDialog = false
                selectedDate = "$selectedDate~$dateMilli"
                onDateChange(selectedDate)
            }

        )
    }
}

@Composable
fun CustomFieldFormulaLayout(
    customField: CustomFieldTempUI,
    value: String = "",
    onClicked: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClicked(customField.label)
            }
    ) {

        // Header: icon + label + status
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.formula_symbol),
                contentDescription = "Formula field icon",
                modifier = Modifier.padding(horizontal = 5.dp)
            )
            Text(
                text = customField.label,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier.padding(horizontal = 5.dp)
            )
            Spacer(modifier = Modifier.weight(1f))

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, top = 5.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var symbol = ""
            var currencySymbol = ""
            var newValue = ""
            if (value.isNotEmpty()) {
                if (CustomFieldType.fromValue(
                        customField.outputType ?: ""
                    ) == CustomFieldType.PERCENTAGE
                ) {
                    symbol = "%"
                    newValue = value.toDouble().formulaCFOutputPercentageValueFormat()
                } else if (CustomFieldType.fromValue(
                        customField.outputType ?: ""
                    ) == CustomFieldType.COST
                ) {
                    symbol = customField.currencyCode ?: ""
                    currencySymbol = customField.currencySymbol ?: ""
                    newValue = value.toDouble().formulaCFOutputCostValueFormat()
                } else if (CustomFieldType.fromValue(
                        customField.outputType ?: ""
                    ) == CustomFieldType.NUMBERS
                ) {
                    if (customField.unit != null) {
                        symbol = customField.unit
                    }
                    newValue = value.toDouble().formatFormulaCfOutputNumber(
                        customField.decimalPlaces ?: 0,
                        customField.showCommas ?: false
                    )
                }
            } else {
                newValue = "Unable to calculate result of formula"
            }
            Text(
                text = symbol,
                color = Color.Black,
                fontSize = 16.sp,
                modifier = Modifier.padding(end = 5.dp)
            )

            Text(
                text = currencySymbol,
                color = Color.Black,
                fontSize = 16.sp,
                modifier = Modifier.padding(end = 5.dp)
            )

            Text(
                text = newValue,
                color = Color.Black,
                fontSize = 16.sp,
                modifier = Modifier.padding(end = 5.dp)
            )


        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = TransparentBlack,
            thickness = 1.dp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomFieldMultiListLayout(
    customField: CustomFieldTempUI,
    value: String,
    selectedIds: List<String> = emptyList(),
    permission: CustomFieldPermissionDomainModel,
    isLoading: Boolean = false,
    isCompleted: Boolean = false,
    isOfflineModified: Boolean = false,
    onNotEditable: () -> Unit = {},
    onLockedClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
//    var selectedItems = emptyList<Int?>()
//    var selectedLabel = "Select..."
//    if (selectedIds.isNotEmpty()) {
//        selectedItems =
//            filterSelectedItems(selectedIds.map { it.toInt() }, customField.subList ?: emptyList())
//        selectedLabel =
//            createSelectedItemsTitle(selectedItems, customField.subList ?: emptyList())
//    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                if (customField.lockedValue == true) {
                    onLockedClick()
                } else if (permission.permission.edit == false) {
                    onNotEditable()
                } else {
                    showDialog = true
                }
            }) {

        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.multi_select),
                contentDescription = "Multi list icon",
                modifier = Modifier.padding(horizontal = 5.dp)
            )
            Text(
                text = customField.label,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF000000) // label_black
                ),
                modifier = Modifier.padding(horizontal = 5.dp)
            )
            Spacer(modifier = Modifier.weight(1f))

            when {
                isLoading -> CircularProgressIndicator(
                    modifier = Modifier
                        .size(22.dp)
                        .padding(end = 5.dp),
                    strokeWidth = 2.dp
                )

                isCompleted -> Icon(
                    painter = painterResource(id = R.drawable.ic_green_check_solid),
                    contentDescription = "Completed",
                    tint = null,
                    modifier = Modifier
                        .size(22.dp)
                        .padding(end = 5.dp)
                )

                isOfflineModified -> Icon(
                    painter = painterResource(id = R.drawable.ic_cloud_upload_solid),
                    contentDescription = "Offline modified",
                    modifier = Modifier
                        .size(30.dp)
                        .padding(end = 5.dp)
                )
            }
        }

        // Value row
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 35.dp, top = 5.dp, bottom = 10.dp)
        ) {
            Text(
                text = value.ifEmpty { "Select..." },
                color = if (value.isEmpty() ) Color.Gray else Color.Black,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 30.dp, top = 12.dp, bottom = 12.dp)
            )

            if (permission.permission.edit == true) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu_down_black_24dp),
                    contentDescription = "Open multi select",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(30.dp)
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = TransparentBlack,
            thickness = 1.dp
        )
    }

    if (showDialog) {
        MultiSelectListDialog(
            title = customField.label,
            items = customField.subList?:emptyList(),
            initialSelectedIds = selectedIds, // Pass the initial List<String>
            onDismissRequest = { showDialog = false },
            // The callback now correctly gives a List<Integer>
            onSelectionConfirmed = { selectedItems -> // <--- This is now a List<Integer>

                // --- Your existing logic from the callback can be pasted here directly ---

                // This is the logic from your DefectViewFragment.java
//                val title = createSelectedItemsTitle(selectedItems, customField.subList)

                // To filter the list (assuming this function exists)
//                val filteredSelectedItems = filterSelectedItems(selectedItems, customField.subList)

                // Now update your ViewModel state
                // ... (Update tvValue equivalent state) ...

                // Converting list of Integer to List of String (just like in the original code)
//                val stringList = filteredSelectedItems.map { it.toString() }

                // Now call your viewmodel update function
                // finalFilledCustomField1.setSelectedItemIds(stringList)
                // updateField(DefectType.CUSTOM_FIELDS, finalFilledCustomField1)
            }
            , onClear = {

            }
        )
    }
}

