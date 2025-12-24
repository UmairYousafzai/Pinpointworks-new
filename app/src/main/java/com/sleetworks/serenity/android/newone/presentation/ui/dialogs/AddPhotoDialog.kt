package com.sleetworks.serenity.android.newone.presentation.ui.dialogs

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sleetworks.serenity.android.newone.R
import com.sleetworks.serenity.android.newone.utils.CONSTANTS.IMAGE

@Composable
fun AddPhotoDialog(
    mediaType: String,
    onDismiss: () -> Unit,
    onTakeMediaClick: (String) -> Unit,
    onSelectMediaClick: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        // Use Surface for the rounded corners and background
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                // Title: "Add Photo"
                Text(
                    text = "Add ${if (mediaType == IMAGE) "Photo" else "Video"}",
                    style = MaterialTheme.typography.headlineSmall, // Matches Large TextAppearance
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 15.dp)
                )

                // Row: Take Picture
                PhotoOptionRow(
                    iconRes = R.drawable.ic_shutter,
                    label = if (mediaType == IMAGE) "Take Photo" else "Record Video",
                    onClick = {
                        onTakeMediaClick(mediaType)
                        onDismiss()
                    }
                )

                Spacer(modifier = Modifier.height(15.dp))

                // Row: Select Picture
                PhotoOptionRow(
                    iconRes = android.R.drawable.ic_menu_gallery,
                    label = if (mediaType == IMAGE) "Select Picture" else "Select Video",
                    onClick = {
                        onSelectMediaClick(mediaType)
                        onDismiss()
                    }
                )
            }
        }
    }
}

@Composable
fun PhotoOptionRow(
    @DrawableRes iconRes: Int,
    label: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = Color.Unspecified, // Keeps original icon colors
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = label,
            color = Color(0xFF4DA0E5), // Matches your XML #4da0e5
            style = MaterialTheme.typography.bodyLarge // Matches Medium TextAppearance
        )
    }
}
