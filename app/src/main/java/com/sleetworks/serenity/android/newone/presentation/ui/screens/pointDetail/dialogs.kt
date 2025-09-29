package com.sleetworks.serenity.android.newone.presentation.ui.screens.pointDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.PointCustomField


@Composable
fun PointTextEditDialog(
    title: String,
    initialValue: String = "",
    onDismiss: () -> Unit,
    onConfirm: (String, PointCustomField?) -> Unit,
    characterLimit: Int = 5000,
    customField: PointCustomField? = null,
) {
    var textValue by remember { mutableStateOf(TextFieldValue(initialValue)) }
    var showCounter by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val textFieldColor = OutlinedTextFieldDefaults.colors(

        focusedBorderColor = Color.Black,
        unfocusedBorderColor = Color.Black,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,

        )
    // Show counter when approaching character limit
    LaunchedEffect(textValue.text.length) {
        showCounter = textValue.text.length >= (characterLimit - 500)
    }

    // Focus and show keyboard when dialog opens
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
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
                OutlinedTextField(
                    value = textValue,
                    onValueChange = { newValue ->
                        if (newValue.text.length <= characterLimit) {
                            textValue = newValue
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    placeholder = { Text("Enter Text...") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (textValue.text.length <= characterLimit) {
                                onConfirm(textValue.text,customField)
                                onDismiss()
                            }
                        }
                    ),
                    minLines = 1,
                    maxLines = 5,
                    isError = textValue.text.length > characterLimit,
                    colors = textFieldColor
                )

                // Character Counter
                if (showCounter) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "${textValue.text.length}/$characterLimit",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (textValue.text.length > characterLimit) {
                                Color(0xFFE45050) // Red color for error
                            } else {
                                Color(0xFF465058) // Gray color for normal
                            }
                        )
                    }
                }

                // Error message for character limit exceeded
                if (textValue.text.length > characterLimit) {
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
                        onClick = onDismiss
                    ) {
                        Text("Cancel", color = Color.Black)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(
                        onClick = {
                            if (textValue.text.length <= characterLimit) {
                                onConfirm(textValue.text,customField)
                            }
                        },
                        enabled = textValue.text.length <= characterLimit
                    ) {
                        Text("Accept", color = Color.Black)
                    }
                }
            }
        }
    }
}