package com.sleetworks.serenity.android.newone.presentation.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.sleetworks.serenity.android.newone.utils.CONSTANTS.LOGOUT

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    dialogType: String = LOGOUT,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = message) },
        containerColor = Color.White,

        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL")
            }        }
    )

}