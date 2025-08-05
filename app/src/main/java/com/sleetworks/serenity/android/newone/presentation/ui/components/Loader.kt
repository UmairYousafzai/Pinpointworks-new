package com.sleetworks.serenity.android.newone.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sleetworks.serenity.android.newone.ui.theme.BlackOpacity70

@Composable
fun CustomLoader(message: String = "Loading...") {
    Box(
        modifier = Modifier
            .fillMaxSize(1f)
            .background(color = BlackOpacity70),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.wrapContentSize().clip(shape = RoundedCornerShape(10)).background(Color.White).padding(20.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = message, color = Color.Black, style = MaterialTheme.typography.titleMedium)
            }
        }

    }
}
