package com.sleetworks.serenity.android.newone.presentation.ui.screens.pointDetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.sleetworks.serenity.android.newone.R
import com.sleetworks.serenity.android.newone.data.models.local.entities.AssigneeEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.customfield.SubListItem
import com.sleetworks.serenity.android.newone.presentation.model.CustomFieldTempUI
import com.sleetworks.serenity.android.newone.presentation.ui.model.CustomFieldType
import com.sleetworks.serenity.android.newone.presentation.ui.model.PointItemPriority
import com.sleetworks.serenity.android.newone.presentation.ui.model.PointItemStatus
import com.sleetworks.serenity.android.newone.ui.theme.CornflowerBlue
import com.sleetworks.serenity.android.newone.ui.theme.CornflowerBlueWith50Opacity
import com.sleetworks.serenity.android.newone.ui.theme.Goldenrod
import com.sleetworks.serenity.android.newone.ui.theme.GoldenrodWith50Opacity
import com.sleetworks.serenity.android.newone.ui.theme.Red
import com.sleetworks.serenity.android.newone.ui.theme.RedWith50Opacity
import com.sleetworks.serenity.android.newone.ui.theme.TransparentBlack
import com.sleetworks.serenity.android.newone.utils.removeLeadingZeros
import com.sleetworks.serenity.android.newone.utils.validateDecimalInput
import java.util.Calendar


@Composable
fun PointTextEditDialog(
    title: String,
    initialValue: String = "",
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    characterLimit: Int = 5000,
    customField: CustomFieldTempUI? = null,
) {
    var textValue by remember { mutableStateOf(initialValue) }
    var showCounter by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val dismissAndHideKeyboard = {
        focusManager.clearFocus()
        keyboardController?.hide()
        onDismiss()
    }
    var keyBoardType = KeyboardType.Text
    var hint = "Enter text here"
    var decimalAllowed = 0
    val textFieldColor = OutlinedTextFieldDefaults.colors(

        focusedBorderColor = Color.Black,
        unfocusedBorderColor = Color.Black,
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,

        )
    // Show counter when approaching character limit
    LaunchedEffect(textValue.length) {
        showCounter = textValue.length >= (characterLimit - 500)
    }
    DisposableEffect(Unit) {
        onDispose {
            focusManager.clearFocus(force = true)
            keyboardController?.hide()
        }
    }
    // Focus and show keyboard when dialog opens
    LaunchedEffect(Unit) {
        focusManager.clearFocus(force = true)
        focusRequester.requestFocus()
        keyboardController?.show()
    }
    if (customField != null) {
        when (CustomFieldType.fromValue(customField.type)) {
            CustomFieldType.COST -> {
                keyBoardType = KeyboardType.Number
                hint = "Enter number..."
                decimalAllowed = 2
            }

            CustomFieldType.NUMBERS -> {
                keyBoardType = KeyboardType.Number
                hint = "Enter number..."
                decimalAllowed = customField.decimalPlaces ?: 0
            }

            CustomFieldType.PERCENTAGE -> {
                keyBoardType = KeyboardType.Number
                hint = "Enter number..."
                decimalAllowed = 0
            }

            else -> {
                keyBoardType = KeyboardType.Text
                hint = "Enter text..."
            }
        }
    } else {
        hint = "Enter text here"
        keyBoardType = KeyboardType.Text

    }

    Dialog(

        onDismissRequest = {
            focusManager.clearFocus(force = true)
            keyboardController?.hide()
            onDismiss()
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        ),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)

                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Text Field
                TextField(
                    value = textValue,
                    onValueChange = { newValue ->
                        textValue = if (customField != null && CustomFieldType.fromValue(customField.type) != CustomFieldType.TEXT) {
                            newValue.validateDecimalInput(decimalAllowed)
                        } else {
                            newValue
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    placeholder = { Text(hint) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = keyBoardType,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            dismissAndHideKeyboard()
                            onConfirm(textValue.removeLeadingZeros())


                        }
                    ),
                    minLines = 1,
                    maxLines = 5,
                    colors = textFieldColor
                )

                // Character Counter
                if (showCounter) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "${textValue.length}/$characterLimit",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (textValue.length > characterLimit) {
                                Color(0xFFE45050) // Red color for error
                            } else {
                                Color(0xFF465058) // Gray color for normal
                            }
                        )
                    }
                }

                // Error message for character limit exceeded
                if (textValue.length > characterLimit) {
                    Text(
                        text = "Text limit exceeded",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFE45050)
                    )
                }

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            dismissAndHideKeyboard()
                        }
                    ) {
                        Text("Cancel", color = Color.Black)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(
                        onClick = {
                            if (textValue.length <= characterLimit) {
                                dismissAndHideKeyboard()

                                onConfirm(textValue.removeLeadingZeros())
                            }
                        },
                        enabled = textValue.length <= characterLimit
                    ) {
                        Text("Accept", color = Color.Black)
                    }
                }
            }
        }
    }
}

@Composable
fun PriorityPickerDialog(
    selectedPriority: PointItemPriority? = null,
    onPrioritySelected: (PointItemPriority) -> Unit,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {

    var priority by remember { mutableStateOf(selectedPriority) }
    Dialog(onDismissRequest = onDismiss) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .padding(top = 10.dp)
        ) {

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 24.dp, end = 24.dp, bottom = 15.dp),
                text = "Priority",
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )
            // Low Priority Option
            PriorityOptionCard(
                priority = "Low",
                backgroundColor = if (priority == PointItemPriority.Low) CornflowerBlue else CornflowerBlueWith50Opacity,
                isSelected = priority == PointItemPriority.Low,
                onClick = { priority = PointItemPriority.Low },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 5.dp)
            )

            // Medium Priority Option
            PriorityOptionCard(
                priority = "Medium",
                backgroundColor = if (priority == PointItemPriority.Medium) Goldenrod else GoldenrodWith50Opacity,
                isSelected = priority == PointItemPriority.Medium,
                onClick = {
                    priority = PointItemPriority.Medium
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 5.dp)
            )

            // High Priority Option
            PriorityOptionCard(
                priority = "High",
                backgroundColor = if (priority == PointItemPriority.High) Red else RedWith50Opacity,
                isSelected = priority == PointItemPriority.High,
                onClick = {
                    priority = PointItemPriority.High
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 5.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.End

            ) {
                Text(
                    modifier = Modifier
                        .clickable(onClick = onDismiss),
                    text = "Cancel",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(30.dp))
                Text(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clickable(onClick = {
                            priority?.let { onPrioritySelected(it) }
                        }),
                    text = "OK",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

}

@Composable
private fun PriorityOptionCard(
    priority: String,
    backgroundColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(40.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 20.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
//                .background( backgroundColor ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = priority,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}


@Composable
fun StatusPickerDialog(
    selectedStatus: PointItemStatus? = null,
    onStatusSelected: (PointItemStatus) -> Unit,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    var pointStatus by remember { mutableStateOf(selectedStatus) }
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .padding(top = 10.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 24.dp, end = 24.dp, bottom = 15.dp),
                text = "Status",
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )
            // Status options
            val statusOptions = listOf(

                PointItemStatus.Open,
                PointItemStatus.InProgress,
                PointItemStatus.OnHold,
                PointItemStatus.ToReview,
                PointItemStatus.Completed,
                PointItemStatus.Canceled,
            )

            statusOptions.forEach { statusOption ->
                StatusOptionCard(
                    status = statusOption.label,
                    iconRes = statusOption.icon,
                    isSelected = pointStatus == statusOption,
                    onClick = { pointStatus = statusOption },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 10.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.End

            ) {
                Text(
                    modifier = Modifier
                        .clickable(onClick = onDismiss),
                    text = "Cancel",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(30.dp))
                Text(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clickable(onClick = {
                            pointStatus?.let { onStatusSelected(it) }
                        }),
                    text = "OK",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

}


@Composable
private fun StatusOptionCard(
    status: String,
    iconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(40.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 20.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                if (isSelected) Color(0xFF6E7881) else Color(0xFF6E7881).copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Status icon
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = "$status icon",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(5.dp))

            // Status text
            Text(
                text = status,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun FlaggedChoiceDialog(
    selectedFlaggedState: Boolean = false,
    onFlaggedStateSelected: (Boolean) -> Unit = {},
    onDismiss: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    var isFlagSelected by remember { mutableStateOf(selectedFlaggedState) }
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(color = Color.White)
                .padding(horizontal = 32.dp) // 10% margin from each side
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                text = "Assignees",
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )
            // Unflagged option
            FlaggedOptionCard(
                title = "Unflagged",
                iconRes = R.drawable.ic_flagged_gray,
                backgroundColor = Color(0xFFE0E0E0), // grey_flagged_background color
                isSelected = !isFlagSelected,
                onClick = { isFlagSelected = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            )

            // Flagged option
            FlaggedOptionCard(
                title = "Flagged",
                iconRes = R.drawable.ic_red_flag,
                backgroundColor = Color(0xFFFFEBEE), // red flag background color
                isSelected = isFlagSelected,
                onClick = { isFlagSelected = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 15.dp),
                horizontalArrangement = Arrangement.End

            ) {
                Text(
                    modifier = Modifier
                        .clickable(onClick = onDismiss),
                    text = "Cancel",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(30.dp))
                Text(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clickable(onClick = {
                            onFlaggedStateSelected(isFlagSelected)
                        }),
                    text = "OK",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }

        }
    }

}


@Composable
private fun FlaggedOptionCard(
    title: String,
    iconRes: Int,
    backgroundColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        border = if (isSelected) BorderStroke(1.dp, CornflowerBlue) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 9.dp, horizontal = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = "$title icon",
                modifier = Modifier.size(19.dp),
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.width(13.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}


@Composable
fun AssigneeDialog(
    assignees: List<AssigneeEntity>,
    selectedAssigneeIds: ArrayList<String> = arrayListOf(),
    onAssigneeSelected: (List<String>) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val selectedIds = remember(selectedAssigneeIds) {
        mutableStateListOf<String>().apply { addAll(selectedAssigneeIds) }
    }
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
        ) {

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp),
                text = "Assignees",
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )
            // Search bar (only show if more than 5 assignees)
            if (assignees.size > 5) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search") },
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                    ),

                    )
            }

            // Assignees list
            val filteredAssignees = assignees.filter { assignee ->
                searchQuery.isEmpty() || assignee.caption.contains(searchQuery, ignoreCase = true)
            }.sortedBy { it.caption.lowercase() }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize(0.4f)
                    .padding(horizontal = 12.dp, vertical = 5.dp),
//            verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(filteredAssignees) { assignee ->
                    AssigneeCheckboxItem(
                        assignee = assignee,
                        isSelected = selectedIds.contains(assignee.id),
                        onSelectionChanged = { isSelected ->
                            if (isSelected) {
                                if (!selectedIds.contains(assignee.id)) selectedIds.add(assignee.id)
                            } else {
                                selectedIds.remove(assignee.id)
                            }
                        }
                    )
                }
            }

            // Divider
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = TransparentBlack,
                thickness = 1.dp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 15.dp),
                horizontalArrangement = Arrangement.End

            ) {
                Text(
                    modifier = Modifier
                        .clickable(onClick = onDismiss),
                    text = "Cancel",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(30.dp))
                Text(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clickable(onClick = {
                            onAssigneeSelected(selectedIds)
                        }),
                    text = "OK",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

}

@Composable
private fun AssigneeCheckboxItem(
    assignee: AssigneeEntity,
    isSelected: Boolean,
    onSelectionChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelectionChanged(!isSelected) }
            .background(Color.White),
//            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = onSelectionChanged,
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF0084F8),
                uncheckedColor = Color.Gray
            ),
            modifier = Modifier.padding(end = 8.dp)
        )

        Text(
            text = assignee.caption,
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun TagsDialog(
    tags: List<String>,
    selectedTags: ArrayList<String> = arrayListOf(),
    onTagSelected: (List<String>) -> Unit = { _ -> },
    onDismiss: () -> Unit = {},

    modifier: Modifier = Modifier
) {
    var searchQuery by remember {
        mutableStateOf("")
    }
    val selectedTags = remember(selectedTags) {
        mutableStateListOf<String>().apply { addAll(selectedTags) }
    }

    Dialog(onDismissRequest = {}) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .padding(bottom = 10.dp)
        ) {

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp),
                text = "Tags",
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )
            // Search bar (only show if more than 5 assignees)
            if (tags.size > 5) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search") },
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 5.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                    )
                )
            }

            // Assignees list
            val filteredTags = tags.filter { tag ->
                searchQuery.isEmpty() || tag.contains(searchQuery, ignoreCase = true)
            }.sortedBy { it.lowercase() }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize(0.4f)
                    .padding(horizontal = 12.dp, vertical = 5.dp),
//            verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(filteredTags) { tag ->
                    TagCheckboxItem(
                        tag = tag,
                        isSelected = selectedTags.contains(tag),
                        onSelectionChanged = { isSelected ->
                            if (isSelected) {
                                if (!selectedTags.contains(tag)) selectedTags.add(tag)
                            } else {
                                selectedTags.remove(tag)
                            }
                        }
                    )
                }
            }

            // Divider
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = TransparentBlack,
                thickness = 1.dp
            )


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 15.dp),
                horizontalArrangement = Arrangement.End

            ) {
                Text(
                    modifier = Modifier
                        .clickable(onClick = onDismiss),
                    text = "Cancel",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(30.dp))
                Text(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clickable(onClick = {
                            onTagSelected(selectedTags)
                        }),
                    text = "OK",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

}

@Composable
private fun TagCheckboxItem(
    tag: String,
    isSelected: Boolean,
    onSelectionChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelectionChanged(!isSelected) }
            .background(Color.White),
//            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = onSelectionChanged,
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF0084F8),
                uncheckedColor = Color.Gray
            ),
            modifier = Modifier.padding(end = 8.dp)
        )

        Text(
            text = tag,
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
    }
}


@Composable
fun TimeEditDialog(
    title: String,
    isHoursOnly: Boolean = false,

    onDismiss: () -> Unit = {},
    onSave: (days: String, hours: String, minutes: String) -> Unit = { _, _, _ -> },
    modifier: Modifier = Modifier
) {
    var days by remember { mutableStateOf("") }
    var hours by remember { mutableStateOf("") }
    var minutes by remember { mutableStateOf("") }

    val textFieldColor = OutlinedTextFieldDefaults.colors(

        focusedBorderColor = Color.Black,
        unfocusedBorderColor = Color.Black,
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,

        )
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Input fields
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (!isHoursOnly) {
                        // Days input
                        TextField(
                            value = days,
                            onValueChange = { days = it },
                            placeholder = { Text("d") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = textFieldColor
                        )
                    }

                    // Hours input
                    TextField(
                        value = hours,
                        onValueChange = { hours = it },
                        placeholder = { Text(if (isHoursOnly) "h" else "h") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = textFieldColor

                    )

                    // Minutes input
                    TextField(
                        value = minutes,
                        onValueChange = { minutes = it },
                        placeholder = { Text("min") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = textFieldColor

                    )

                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }

                    TextButton(
                        onClick = {
                            if (isHoursOnly) {
                                onSave("0", hours.ifEmpty { "0" }, minutes.ifEmpty { "0" })
                            } else {
                                onSave(
                                    days.ifEmpty { "0" },
                                    hours.ifEmpty { "0" },
                                    minutes.ifEmpty { "0" })
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Accept")
                    }
                }
            }
        }
    }
}

@Composable
fun LockedDialog(onDismiss: () -> Unit) {
    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = onDismiss,
//        title = {
//            Text("Locked by Admin",)
//        },

        text = {
            Text(
                "Locked by Admin",
                style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 18.sp)
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

@Composable
fun FormulaDialog(label: String, onDismiss: () -> Unit) {
    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = onDismiss,
        title = { Text(label) },
        text = { Text("Formula field values cannot be manually edited. To see the latest calculated value in Formula field, sync this point.") },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerDialog(
    title: String = "Select Date",
    date: String,
    minDateMilli: Long = 0,
    shouldSetMinDate: Boolean = false,
    onDismiss: () -> Unit,
    onValueChange: (Long) -> Unit
) {

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = date.toLong(),
        yearRange = (2000..Calendar.getInstance().get(Calendar.YEAR) + 10),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {

                return if (shouldSetMinDate) {
                    utcTimeMillis >= minDateMilli
                } else {
                    true
                }
            }
        }
    )




    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        colors = DatePickerDefaults.colors(
            containerColor = Color.White
        ),

        confirmButton = {
            TextButton(onClick = {
                onValueChange(datePickerState.selectedDateMillis ?: 0)

            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    ) {
        Column {

            DatePicker(
                title = { Text(title) },

                state = datePickerState, colors = DatePickerDefaults.colors(
                    containerColor = Color.White
                )
            )

//            // Add "Clear" Button (like DATEPICKER neutral button)
//            TextButton(
//                onClick = {
//                    onValueChange(0)
//                },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Clear", color = MaterialTheme.colorScheme.error)
//            }
        }
    }

}

@Composable
fun ListDialogInCompose(
    onDismissRequest: () -> Unit,
    customField: CustomFieldTempUI,
    initialSelectedId: String?,
    onAccept: (selectedId: String?) -> Unit,
    onClear: () -> Unit
) {
    // State for the search query
    var searchQuery by remember { mutableStateOf("") }
    val isSearching = searchQuery.isNotEmpty()

    // --- State for Hierarchical Navigation ---
    // A stack to manage the path of nested lists. Each element is a list of Sublist items.
    val navStack = remember { mutableStateListOf(customField.subList) }
    // The currently displayed list is the last one on the stack.
    val currentList = navStack.last()
    // The current path as a breadcrumb string.
    var currentPath by remember { mutableStateOf("") }

    // --- State for Selection ---
    var selectedId by remember { mutableStateOf(initialSelectedId) }

    // --- State for Search ---
    // Flatten the entire nested structure once for efficient searching.
    val flatSearchList = remember(customField.subList) {
        flattenForSearch(customField.subList ?: emptyList(), "")
    }
    // The list of items matching the search query.
    val searchResults = if (isSearching) {
        flatSearchList.filter { it.displayLabel.contains(searchQuery, ignoreCase = true) }
    } else {
        emptyList()
    }
    val textFieldColor = OutlinedTextFieldDefaults.colors(

        focusedBorderColor = Color.Black,
        unfocusedBorderColor = Color.Black,
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,

        )
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.fillMaxHeight(0.5f), // Use a fraction of screen height
            shape = MaterialTheme.shapes.large,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
            ) {
                // --- Dialog Header ---
                Column(
                    modifier = Modifier.padding(
                        start = 24.dp,
                        end = 24.dp,
                        top = 24.dp,
                        bottom = 16.dp
                    )
                ) {
                    Text(
                        text = customField.label,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(Modifier.height(16.dp))
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Search") },
                        colors = textFieldColor,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // --- List Content Area ---
                Box(modifier = Modifier.weight(1f)) {
                    // Show search results if user is searching
                    if (isSearching) {
                        SearchListView(
                            items = searchResults,
                            currentSelectedId = selectedId,
                            onItemSelected = { id -> selectedId = id }
                        )
                    } else {
                        // Otherwise, show the hierarchical browser
                        HierarchicalListView(
                            path = currentPath,
                            items = currentList ?: emptyList(),
                            currentSelectedId = selectedId,
                            onItemSelected = { id -> selectedId = id },
                            onNavigateForward = { sublist, newPath ->
                                navStack.add(sublist)
                                currentPath = newPath
                            },
                            onNavigateBack = {
                                if (navStack.size > 1) {
                                    navStack.removeAt(navStack.lastIndex)
                                    // Reconstruct path after going back
                                    currentPath = navStack.drop(1).joinToString(" / ") { list ->
                                        // This part is tricky, we need to find the parent label.
                                        // A better data structure would help. For now, we simplify.
                                        currentPath.substringBeforeLast(
                                            " /",
                                            missingDelimiterValue = ""
                                        )
                                    }
                                }
                            }
                        )
                    }
                }

                // --- Action Buttons ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        onClear()
                        onDismissRequest()
                    }) {
                        Text("Clear")
                    }
                    Spacer(Modifier.width(8.dp))
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancel")
                    }
                    Spacer(Modifier.width(8.dp))
                    TextButton(onClick = {
                        onAccept(selectedId,)
                        onDismissRequest()
                    }) {
                        Text("Accept")
                    }
                }
            }
        }
    }
}

// Composable for the hierarchical browser view
@Composable
private fun HierarchicalListView(
    path: String,
    items: List<SubListItem>,
    currentSelectedId: String?,
    onItemSelected: (String) -> Unit,
    onNavigateForward: (List<SubListItem>, String) -> Unit,
    onNavigateBack: () -> Unit
) {
    Column(modifier = Modifier.background(color = Color.White)) {
        // --- Header with Back Button and Breadcrumbs ---
        if (path.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(text = path, style = MaterialTheme.typography.bodyMedium)
            }
            Divider()
        }

        // --- List of Items ---
        LazyColumn(modifier = Modifier.background(color = Color.White)) {
            items(items, key = { it.id }) { item ->
                ListItem(
                    headlineContent = { Text(item.label) },
                    modifier = Modifier
                        .background(color = Color.White)
                        .clickable {
                            if (item.subList.isNullOrEmpty()) {
                                onItemSelected(item.id.toString())
                            } else {
                                val newPath =
                                    if (path.isEmpty()) item.label else "$path / ${item.label}"
                                onNavigateForward(item.subList, newPath)
                            }
                        },
                    leadingContent = {
                        RadioButton(
                            selected = (item.id.toString() == currentSelectedId),
                            onClick = { onItemSelected(item.id.toString()) }
                        )
                    },
                    colors = ListItemDefaults.colors(
                        containerColor = Color.White  // background color
                    ),                    // Show arrow if it's a parent node
                    trailingContent = {
                        if (!item.subList.isNullOrEmpty()) {
                            Icon(
                                Icons.Default.ArrowForwardIos,
                                contentDescription = "Navigate to sublist"
                            )
                        }
                    }
                )
            }
        }
    }
}


// Composable for the flat search results view
@Composable
private fun SearchListView(
    items: List<FlatSearchItem>,
    currentSelectedId: String?,
    onItemSelected: (String) -> Unit
) {
    LazyColumn {
        items(items, key = { it.id }) { item ->
            ListItem(
                headlineContent = { Text(item.displayLabel) },
                modifier = Modifier.clickable { onItemSelected(item.id) },
                leadingContent = {
                    RadioButton(
                        selected = (item.id == currentSelectedId),
                        onClick = { onItemSelected(item.id) }
                    )
                }
            )
        }
    }
}


// --- Helper data class and function for search ---
private data class FlatSearchItem(val id: String, val displayLabel: String)

private fun flattenForSearch(sublists: List<SubListItem>, prefix: String): List<FlatSearchItem> {
    val result = mutableListOf<FlatSearchItem>()
    for (item in sublists) {
        val currentLabel = if (prefix.isEmpty()) item.label else "$prefix / ${item.label}"
        // Only leaf nodes (items without a sublist) are selectable in the original logic.
        // We add all items to search, but you could filter here if needed.
        result.add(FlatSearchItem(id = item.id.toString(), displayLabel = currentLabel))
        item.subList?.let {
            result.addAll(flattenForSearch(it, currentLabel))
        }
    }
    return result
}

@Composable
fun MultiSelectListDialog(
    title: String,
    items: List<SubListItem>,
    initialSelectedIds: List<String>,
    onDismissRequest: () -> Unit,
    onSelectionConfirmed: (List<Int>) -> Unit,
    onClear: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val selectedIds = remember { mutableStateOf(initialSelectedIds.toSet()) }

    // This is the key change: We flatten the list for search but keep the hierarchy for display.
    val flatSearchList = remember(items) { flattenForSearch(items, "") }
    val searchResults = if (searchQuery.isNotEmpty()) {
        flatSearchList.filter { it.displayLabel.contains(searchQuery, ignoreCase = true) }
    } else {
        emptyList()
    }
    val isSearching = searchQuery.isNotEmpty()

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(modifier = Modifier.fillMaxHeight(0.8f)) {
            Column(modifier = Modifier.padding(vertical = 24.dp)) {
                // --- Header ---
                Text(
                    title,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(Modifier.height(16.dp))
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
                Spacer(Modifier.height(8.dp))

                // --- Content Area ---
                Box(modifier = Modifier.weight(1f)) {
                    if (isSearching) {
                        // Flat list for search results
                        SearchListView(
                            items = searchResults,
                            selectedIds = selectedIds.value,
                            onItemClick = { itemId ->
                                val currentIds = selectedIds.value.toMutableSet()
                                if (currentIds.contains(itemId)) currentIds.remove(itemId) else currentIds.add(
                                    itemId
                                )
                                selectedIds.value = currentIds
                            }
                        )
                    } else {
                        // **NEW: Hierarchical list for browsing**
                        HierarchicalListView(
                            items = items,
                            selectedIds = selectedIds.value,
                            onItemClick = { itemId ->
                                val currentIds = selectedIds.value.toMutableSet()
                                if (currentIds.contains(itemId)) currentIds.remove(itemId) else currentIds.add(
                                    itemId
                                )
                                selectedIds.value = currentIds
                            }
                        )
                    }
                }

                // --- Action Buttons ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        onClear()
                        onDismissRequest()
                    }) { Text("Clear") }
                    Spacer(Modifier.width(8.dp))
                    TextButton(onClick = onDismissRequest) { Text("Cancel") }
                    Spacer(Modifier.width(8.dp))
                    TextButton(onClick = {
                        val finalSelectedInts = selectedIds.value.mapNotNull { it.toIntOrNull() }
                        onSelectionConfirmed(finalSelectedInts)
                        onDismissRequest()
                    }) { Text("OK") }
                }
            }
        }
    }
}

// --- NEW: Composable to render the nested list ---
@Composable
private fun HierarchicalListView(
    items: List<SubListItem>,
    selectedIds: Set<String>,
    onItemClick: (String) -> Unit
) {
    LazyColumn {
        items.forEach { item ->
            // Render the top-level item
            renderHierarchicalItem(
                item = item,
                selectedIds = selectedIds,
                onItemClick = onItemClick,
                level = 0 // Starting at level 0
            )
        }
    }
}

// Recursive function to add items to the LazyColumn's scope
private fun LazyListScope.renderHierarchicalItem(
    item: SubListItem,
    selectedIds: Set<String>,
    onItemClick: (String) -> Unit,
    level: Int
) {
    // Add the current item to the list
    item {
        val isSelected = selectedIds.contains(item.id.toString())
        ListItem(
            headlineContent = { Text(item.label) },
            modifier = Modifier
                .clickable { onItemClick(item.id.toString()) }
                // Indent based on the nesting level
                .padding(start = (16 * level).dp),
            leadingContent = {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = null // Click is handled by the row
                )
            }
        )
    }

    // If this item has a sublist, recursively call this function for its children
    item.subList.let { subItems ->
        subItems.forEach { subItem ->
            renderHierarchicalItem(
                item = subItem,
                selectedIds = selectedIds,
                onItemClick = onItemClick,
                level = level + 1 // Increase indent level for children
            )
        }
    }
}


// --- Search-related composables and helpers (remain the same) ---
@Composable
private fun SearchListView(
    items: List<FlatSearchItem>,
    selectedIds: Set<String>,
    onItemClick: (String) -> Unit
) {
    LazyColumn {
        items(items, key = { it.id }) { item ->
            val isSelected = selectedIds.contains(item.id)
            ListItem(
                headlineContent = { Text(item.displayLabel) },
                modifier = Modifier.clickable { onItemClick(item.id) },
                leadingContent = {
                    Checkbox(checked = isSelected, onCheckedChange = null)
                }
            )
        }
    }
}









