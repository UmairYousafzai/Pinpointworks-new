package com.sleetworks.serenity.android.newone.presentation.ui.screens.photo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.sleetworks.serenity.android.newone.presentation.viewmodels.EditPhotoViewModel
import com.sleetworks.serenity.android.newone.presentation.viewmodels.SharedViewModel
import io.getstream.sketchbook.Sketchbook
import io.getstream.sketchbook.rememberSketchbookController

@Composable
fun EditPhotoScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    viewMode: EditPhotoViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val photo by viewMode.photo.collectAsState()
     val sketchbookController = rememberSketchbookController()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Black)) {

        if (photo.second.exists())
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(photo.second)
                    .crossfade(true)
                    .build(),
                contentDescription = "image",
                modifier = Modifier.fillMaxSize(),
//                contentScale = ContentScale.Crop,
//            error = painterResource(R.drawable.ic_image_placeholder)
            )


        Sketchbook(
            modifier = Modifier.fillMaxSize(),
            controller = sketchbookController,
//            backgroundColor = Color.Transparent   // 🔴 THIS FIXES IT

        )
    }

}



