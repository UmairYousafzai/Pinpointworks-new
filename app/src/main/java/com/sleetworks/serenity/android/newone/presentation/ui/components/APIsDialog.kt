package com.sleetworks.serenity.android.newone.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.sleetworks.serenity.android.newone.domain.models.APIsListItem
import com.sleetworks.serenity.android.newone.ui.theme.PinpointworksNewTheme

@Composable
fun APIsSelectionDialog(
    urls: List<APIsListItem> = listOf(
        APIsListItem(
            url = "https://www.google.com",
            isSelected = true
        ),
        APIsListItem(url = "https://www.google.com", isSelected = false)
    ),
    onContinue: (String) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    var selectedIndex by remember { mutableIntStateOf(urls.indexOfFirst { it.isSelected }) }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 20.dp, horizontal = 10.dp),
        ) {
            Column(

                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Choose your API instance",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                LazyColumn {
                    itemsIndexed(urls) { index, item ->
                        APIsListItem(
                            item = item.copy(
                                item.url,
                                index == selectedIndex
                            )
                        ) {
                            selectedIndex = index
                        }
                    }

                }

                Spacer(modifier = Modifier.height(40.dp))
            }
            Text(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .clickable(onClick = {
                        if (selectedIndex>=0) {
                            onContinue(urls[selectedIndex].url)
                        }
                    }),
                text = "CONTINUE",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium
            )

        }

    }
}

@Composable
fun APIsListItem(item: APIsListItem, onClick: () -> Unit) {

    Row(
        modifier = Modifier
            .padding(top = 12.dp, bottom = 5.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = item.isSelected, onClick = null)
        Spacer(modifier = Modifier.width(15.dp))
        Text(text = item.url, fontSize = 17.sp, color = Color.Black)
    }


}

@Preview(showBackground = true)
@Composable
fun APIsSelectionDialogPreview() {
    PinpointworksNewTheme {
        APIsSelectionDialog()
    }
}
