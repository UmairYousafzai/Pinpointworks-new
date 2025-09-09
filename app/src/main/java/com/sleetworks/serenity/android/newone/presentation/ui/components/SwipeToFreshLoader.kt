package com.sleetworks.serenity.android.newone.presentation.ui.components


import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MultiSegmentAppBarLoader(
    color: Color = Color(0xFF33B5E5),
    lineHeight: Dp = 5.dp,
    segmentCount: Int = 4,
    durationMs: Int = 1200
) {
    val infiniteTransition = rememberInfiniteTransition()

    // Animate a progress value from 0f to 1f infinitely
    val globalProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMs, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(lineHeight)
    ) {
        val canvasWidth = size.width
        val centerY = size.height / 2

        val baseLineWidth = canvasWidth / 8f
        val maxLineWidth = baseLineWidth * 2

        for (i in 0 until segmentCount) {
            // Calculate per-line phase shift (even time spacing)
            val phaseOffset = i.toFloat() / segmentCount
            val progress = (globalProgress + phaseOffset) % 1f

            // Grow and shrink line size over time
            val growth = if (progress <= 0.5f) {
                progress * 2
            } else {
                (1f - progress) * 2
            }

            val currentLineWidth = baseLineWidth + (growth * baseLineWidth)
            val startX = progress * canvasWidth
            val endX = (startX + currentLineWidth).coerceAtMost(canvasWidth)

            // Handle wrap-around if line overflows right edge
            if (endX <= canvasWidth) {
                drawLine(
                    color = color,
                    start = Offset(startX, centerY),
                    end = Offset(endX, centerY),
                    strokeWidth = size.height
                )
            } else {
                // Split draw if line wraps
                drawLine(
                    color = color,
                    start = Offset(startX, centerY),
                    end = Offset(canvasWidth, centerY),
                    strokeWidth = size.height
                )
                drawLine(
                    color = color,
                    start = Offset(0f, centerY),
                    end = Offset(endX - canvasWidth, centerY),
                    strokeWidth = size.height
                )
            }
        }
    }
}
