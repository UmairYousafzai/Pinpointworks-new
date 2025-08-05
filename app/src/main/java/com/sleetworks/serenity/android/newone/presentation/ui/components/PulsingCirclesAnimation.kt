package com.sleetworks.serenity.android.newone.presentation.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
@Composable
fun SeamlessPingPongCircles() {
    val circleCount = 4
    val duration = 800

    var direction by remember { mutableStateOf(1) } // 1 for forward, -1 for backward
    var currentIndex by remember { mutableStateOf(0) }

    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            progress.snapTo(0f)
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = duration)
            )

            val nextIndex = currentIndex + direction

            // If we reach the edge, reverse direction
            if (nextIndex < 0 || nextIndex >= circleCount) {
                direction *= -1
                currentIndex += direction // start moving back right away
            } else {
                currentIndex = nextIndex
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(circleCount) { index ->
            val nextIndex = currentIndex + direction

            val scale = when (index) {
                currentIndex -> 1.5f * (1f - progress.value)
                nextIndex -> 1.5f * progress.value
                else -> 0f
            }

            Box(
                modifier = Modifier
                    .size(30.dp)
                    .graphicsLayer(
                        scaleX = scale.coerceAtLeast(0f),
                        scaleY = scale.coerceAtLeast(0f)
                    )
                    .background(Color(0xFFFFCC66), CircleShape)
            )
        }
    }
}
