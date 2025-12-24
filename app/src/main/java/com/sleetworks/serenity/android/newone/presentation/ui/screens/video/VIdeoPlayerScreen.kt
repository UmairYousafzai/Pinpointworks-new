package com.sleetworks.serenity.android.newone.presentation.ui.screens.video

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sleetworks.serenity.android.newone.R
import com.sleetworks.serenity.android.newone.presentation.common.UIEvent
import com.sleetworks.serenity.android.newone.presentation.ui.components.ConfirmationDialog
import com.sleetworks.serenity.android.newone.presentation.ui.components.LoaderDialog
import com.sleetworks.serenity.android.newone.presentation.viewmodels.SharedViewModel
import com.sleetworks.serenity.android.newone.presentation.viewmodels.VideoPlayerViewModel
import com.sleetworks.serenity.android.newone.ui.theme.OuterSpace
import com.sleetworks.serenity.android.newone.utils.CONSTANTS.DELETE


@Composable
fun VideoPlayerScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    viewModel: VideoPlayerViewModel = hiltViewModel()

) {

    val errorMessage by viewModel.error.collectAsState()
    val mainLoader by viewModel.mainLoader.collectAsState()
    val uiEvent = viewModel.uiEvent
    var showConfirmationDialog by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        uiEvent.collect { event ->
            when (event) {
                is UIEvent.Navigate -> {}
                is UIEvent.PopBackStack -> navController.popBackStack()
                UIEvent.Logout -> {}
            }
        }
    }
    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotEmpty()) {
            sharedViewModel.showSnackbar(errorMessage)
            viewModel.setErrorMessage()
        }
    }

    Column(verticalArrangement = Arrangement.SpaceBetween) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(OuterSpace)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                "Pinpoint Works", color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,

                )
        }

        VideoSection(
            viewModel, Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(color = Color.Black)
        )


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .background(OuterSpace)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .padding(5.dp)
                    .clickable {
                        showConfirmationDialog = true
                    },
                painter = painterResource(R.drawable.ic_trash),
                contentDescription = "error",
                tint = Color.White
            )
        }
    }

    if (showConfirmationDialog) {
        ConfirmationDialog(
            title = "Delete video",
            message = "Are you sure you want to delete this video?",
            dialogType = DELETE,
            onDismiss = {
                showConfirmationDialog = false
            },
            onConfirm = {
                showConfirmationDialog = false

                viewModel.deleteVideo()
            })
    }
    if (mainLoader) {
        LoaderDialog(text = "Deleting...")
    }
}

@Composable
fun VideoSection(viewModel: VideoPlayerViewModel, modifier: Modifier) {

    val loader by viewModel.downloadLoader.collectAsState()
    val player by viewModel.player.collectAsState()
    val video by viewModel.videoFile.collectAsState()

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {


        if (player != null) {
            androidx.compose.ui.viewinterop.AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    androidx.media3.ui.PlayerView(ctx).apply {
                        this.player = player
                        useController = true  // This enables the controls (buttons and seekbar)
                    }
                },
                update = { playerView ->
                    playerView.player = player
                }
            )
        }

        if (video?.exists() == false && !loader)
            Icon(
                modifier = Modifier.size(35.dp),
                imageVector = Icons.Default.Error,
                contentDescription = "error",
                tint = Color.White
            )

        if (loader)
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier.size(35.dp),
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "error",
                    tint = Color.White
                )
                Text("Downloading", color = Color.White, fontSize = 18.sp)
                LinearProgressIndicator(
                    modifier = Modifier
                        .width(120.dp)
                        .height(3.dp),
                    color = Color(0xFF33B5E5), // Holo blue
                    trackColor = Color.Transparent
                )

            }


    }


}