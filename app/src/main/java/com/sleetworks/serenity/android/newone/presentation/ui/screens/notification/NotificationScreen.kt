package com.sleetworks.serenity.android.newone.presentation.ui.screens.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sleetworks.serenity.android.newone.R
import com.sleetworks.serenity.android.newone.presentation.common.UIEvent
import com.sleetworks.serenity.android.newone.presentation.ui.components.AppTopBar
import com.sleetworks.serenity.android.newone.presentation.ui.components.LoaderDialog
import com.sleetworks.serenity.android.newone.presentation.viewmodels.NotificationViewModel
import com.sleetworks.serenity.android.newone.presentation.viewmodels.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val notifications by viewModel.notifications.collectAsState()
    val isLoading by viewModel.mainLoader.collectAsState()
    val listState = rememberLazyListState()
    val uiEvent = viewModel.uiEvent

    LaunchedEffect(Unit) {
        uiEvent.collect { event ->

            when (event) {
                UIEvent.Logout -> {}
                is UIEvent.Navigate -> {
                    navController.navigate(event.route) {
                        popUpTo(0)
                    }
                }

                UIEvent.PopBackStack -> {

                }
            }

        }
    }

    // Load notifications when screen is first displayed
    LaunchedEffect(Unit) {
        viewModel.loadFirstTimeNotifications()

    }

    // Load more when scrolled to bottom
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null && lastVisibleIndex >= notifications.size - 3) {
                    viewModel.loadMoreNotifications()
                }
            }
    }




    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            AppTopBar(
                title = "Notifications",
                leadingIcon = R.drawable.ic_bell,
                actionIcons = listOf(
                    Pair("Close", Icons.Default.Close)
                ),
                actionClick = { key ->
                    if (key == "Close") {
                        navController.popBackStack()
                    }
                }
            )
            if (notifications.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No notifications to display",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            if (notifications.isNotEmpty()) {
                NotificationList(
                    notifications = notifications,
                    viewModel = viewModel,
                    onNotificationClick = {},
                    listState = listState
                )

            }

            if (isLoading) {
                LoaderDialog(text = "Loading...")
            }


        }
    }

}

