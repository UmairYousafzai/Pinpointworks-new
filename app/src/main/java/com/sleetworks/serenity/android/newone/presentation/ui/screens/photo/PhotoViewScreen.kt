package com.sleetworks.serenity.android.newone.presentation.ui.screens.photo

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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.sleetworks.serenity.android.newone.R
import com.sleetworks.serenity.android.newone.presentation.navigation.Screen
import com.sleetworks.serenity.android.newone.presentation.ui.components.ConfirmationDialog
import com.sleetworks.serenity.android.newone.presentation.ui.components.LoaderDialog
import com.sleetworks.serenity.android.newone.presentation.viewmodels.PhotoViewModel
import com.sleetworks.serenity.android.newone.presentation.viewmodels.SharedViewModel
import com.sleetworks.serenity.android.newone.ui.theme.OuterSpace
import com.sleetworks.serenity.android.newone.utils.CONSTANTS.DELETE
import kotlinx.coroutines.flow.first
import java.io.File


@Composable
fun PhotoViewScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    viewModel: PhotoViewModel = hiltViewModel()
) {
    val mainLoader by viewModel.mainLoader.collectAsState()
    val photos by viewModel.photos.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()
    val pagerState = rememberPagerState(pageCount = { photos.size }, initialPage = currentIndex)
    val deleteSuccess by viewModel.deleteSuccess.collectAsState()
    val errorMessage by viewModel.error.collectAsState()
    var showConfirmationDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(deleteSuccess) {

        if (deleteSuccess) {
            if (photos.isNotEmpty()) {
//                if (pagerState.currentPage>) {
//                    pagerState.scrollToPage(pagerState.currentPage)
//                }
            } else {
                navController.popBackStack()
            }
            viewModel.setDeleteSuccessStatus()
        }
    }
    LaunchedEffect(Unit) {
        snapshotFlow {
            photos
        }.first {
            it.isNotEmpty()
        }
        if (currentIndex in photos.indices) {
            pagerState.scrollToPage(currentIndex)
        }
    }
    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotEmpty()) {
            sharedViewModel.showSnackbar(errorMessage)
            viewModel.setErrorMessage()
        }
    }


    Column {

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




        HorizontalPager(
            modifier = Modifier.weight(1f),
            state = pagerState
        ) { page ->

            val imagePair = photos[page]
            PhotoItem(viewModel, imagePair.second)

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .background(OuterSpace)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .padding(5.dp)
                    .clickable {
                        navController.navigate(Screen.EditPhotScreen.route + "/${photos[pagerState.currentPage].first}")
                    },
                imageVector = Icons.Default.Edit,
                contentDescription = "error",
                tint = Color.White
            )

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
            title = "Delete image",
            message = "Are you sure you want to delete this picture?",
            dialogType = DELETE,
            onDismiss = {
                showConfirmationDialog = false
            },
            onConfirm = {
                showConfirmationDialog = false

                viewModel.deleteImage(photos[pagerState.currentPage].first)
            })
    }
    if (mainLoader) {
        LoaderDialog(text = "Deleting...")
    }

}

@Composable
fun PhotoItem(viewModel: PhotoViewModel, image: File) {

    val context = LocalContext.current
    val loader by viewModel.imageSyncLoader.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        contentAlignment = Alignment.Center
    ) {
        if (image.exists() && !loader)
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(image)
                    .crossfade(true)
                    .build(),
                contentDescription = "Thumbnail",
                modifier = Modifier.fillMaxSize(),
//                contentScale = ContentScale.Crop,
//            error = painterResource(R.drawable.ic_image_placeholder)
            )
        if (!image.exists() && !loader)
            Icon(
                modifier = Modifier.size(35.dp),
                imageVector = Icons.Default.Error,
                contentDescription = "error",
                tint = Color.White
            )

        if (loader)
            Column() {
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