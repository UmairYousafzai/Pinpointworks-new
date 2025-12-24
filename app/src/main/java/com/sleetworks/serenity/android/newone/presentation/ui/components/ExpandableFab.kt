package com.sleetworks.serenity.android.newone.presentation.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ModeComment
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sleetworks.serenity.android.newone.ui.theme.FreshGreen
import com.sleetworks.serenity.android.newone.ui.theme.OuterSpace
import com.sleetworks.serenity.android.newone.ui.theme.Sunglow
import com.sleetworks.serenity.android.newone.ui.theme.TealBlue
import com.sleetworks.serenity.android.newone.ui.theme.WarmRed

@Composable
fun ExpandableFab(
    modifier: Modifier = Modifier,
    onCameraClick: () -> Unit,
    onVideoClick: () -> Unit,
    onCommentClick: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        // Sub FABs (appear upward with animation)
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sub FAB 1

            AnimatedVisibility(
                visible = expanded,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = OuterSpace,
                        shape = RoundedCornerShape(3.dp),
                        shadowElevation = 6.dp
                    ) {
                        Text(
                            text = "Add Photo",
                            color = Color.White,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    FloatingActionButton(
                        shape = CircleShape,
                        onClick = {
                            expanded = false
                            onCameraClick()
                        },
                        containerColor = WarmRed
                    ) {
                        Icon(
                            Icons.Default.CameraAlt,
                            contentDescription = "add photo",
                            tint = Color.White
                        )
                    }
                }
            }

            // Sub FAB 2
            AnimatedVisibility(
                visible = expanded,
                enter = scaleIn(animationSpec = tween(delayMillis = 50)) + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = OuterSpace,
                        shape = RoundedCornerShape(3.dp),
                        shadowElevation = 6.dp
                    ) {
                        Text(
                            text = "Add Video",
                            color = Color.White,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    FloatingActionButton(
                        shape = CircleShape,
                        onClick = {
                            expanded = false
                            onVideoClick()
                        },
                        containerColor = Sunglow
                    ) {
                        Icon(
                            Icons.Default.Videocam,
                            contentDescription = "add video",
                            tint = Color.White
                        )
                    }
                }
            }

            // Sub FAB 3
            AnimatedVisibility(
                visible = expanded,
                enter = scaleIn(animationSpec = tween(delayMillis = 100)) + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = OuterSpace,
                        shape = RoundedCornerShape(3.dp),
                        shadowElevation = 6.dp
                    ) {
                        Text(
                            text = "Add Comment",
                            color = Color.White,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    FloatingActionButton(
                        shape = CircleShape,
                        onClick = {
                            expanded = false
                            onCommentClick()
                        },
                        containerColor = FreshGreen
                    ) {
                        Icon(
                            Icons.Default.ModeComment,
                            contentDescription = "add comment",
                            tint = Color.White
                        )
                    }
                }

            }

            // Main FAB (toggles expansion, rotates icon)
            FloatingActionButton(
                onClick = { expanded = !expanded },
                shape = CircleShape,
                containerColor = TealBlue
            ) {
                AnimatedContent(targetState = expanded) { isExpanded ->
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.Close else Icons.Default.Add,
                        contentDescription = "Toggle",
                        tint = Color.White
                    )
                }
            }
        }
    }
}