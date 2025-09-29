package com.sleetworks.serenity.android.newone.presentation.ui.screens.defectList

import android.util.Log
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sleetworks.serenity.android.newone.R
import com.sleetworks.serenity.android.newone.domain.models.point.PointDomain
import com.sleetworks.serenity.android.newone.presentation.navigation.Screen
import com.sleetworks.serenity.android.newone.presentation.ui.components.AppTopBar
import com.sleetworks.serenity.android.newone.presentation.ui.components.MultiSegmentAppBarLoader
import com.sleetworks.serenity.android.newone.presentation.ui.model.PointItemPriority
import com.sleetworks.serenity.android.newone.presentation.ui.model.PointItemStatus
import com.sleetworks.serenity.android.newone.presentation.viewmodels.PointViewModel
import com.sleetworks.serenity.android.newone.presentation.viewmodels.SharedViewModel
import com.sleetworks.serenity.android.newone.ui.theme.BabyBlue
import com.sleetworks.serenity.android.newone.ui.theme.BrightBlue
import com.sleetworks.serenity.android.newone.ui.theme.DarkCharcoal
import com.sleetworks.serenity.android.newone.ui.theme.MidnightBlue
import com.sleetworks.serenity.android.newone.ui.theme.PinpointworksNewTheme
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefectListScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    pointViewModel: PointViewModel = hiltViewModel(),

    ) {

    val TAG = "DefectListScreen"


    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val error by pointViewModel.error.collectAsState()


    BackHandler {
        if (drawerState.isOpen) {
            scope.launch { drawerState.close() } // Close the drawer if open
        } else {
            navController.popBackStack()
        }
    }


    LaunchedEffect(error) {

        if (error.isNotEmpty()) {
            Log.d(TAG, " show snackbar ")
            sharedViewModel.showSnackbar(error)
        }
    }


    ModalNavigationDrawer(
        modifier = Modifier.fillMaxSize(),
        drawerState = drawerState,
        gesturesEnabled = false,
        drawerContent = {
            NavigationDrawerContent(scope, drawerState, pointViewModel)
        }) {
        MainContent(pointViewModel, navController, drawerState)

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    pointViewModel: PointViewModel, navController: NavController,
    drawerState: DrawerState
) {
    val scope = rememberCoroutineScope()
    val isRefreshing by pointViewModel.loader.collectAsState()
    val points by pointViewModel.points.collectAsState()
    val workspace by pointViewModel.workspace.collectAsState()
    val syncTime by pointViewModel.syncTime.collectAsState()
    PullToRefreshBox(
        modifier = Modifier
            .fillMaxSize(),
        isRefreshing = isRefreshing.second,
        onRefresh = { pointViewModel.syncWorkspacesData() },
        indicator = {}
    ) {
        Box(
            modifier = Modifier.fillMaxSize()

        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AppTopBar(
                    scope,
                    drawerState,
                    title = workspace?.accountRef?.caption ?: "",
                    subTitle = workspace?.siteName ?: "",
                    shouldDrawerIntegrate = true,
                    actionIcons = listOf(Pair("More", Icons.Default.MoreVert))
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = DarkCharcoal
                        )
                        .padding(vertical = 4.dp, horizontal = 16.dp),
                    text = "Last Synced: $syncTime ",
                    color = Color.White,
                    fontSize = 14.sp
                )
                if (isRefreshing.second) {
                    MultiSegmentAppBarLoader()
                }
                if (isRefreshing.second) {
                    MessageBox(isRefreshing.first)
                }
//                SyncPending() {
//
//                }

                LazyColumn(
                    modifier = Modifier.fillMaxHeight(),
                    contentPadding = PaddingValues(4.dp)
                ) {

                    items(points, key = { point ->
                        point.id
                    }) { point ->
                        DefectListItem(point) {
                            navController.navigate(Screen.DefectDetailScreen.route + "/" + point.id)
                        }
                    }
                }


            }


            IconToggleButton(
                checked = true,
                onCheckedChange = {

                }

            ) {

            }

        }
    }
}

@Composable
fun CustomWebView() {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
            }
        }
    )
}


@Composable
fun SyncPending(onClick: () -> Unit) {
    Text(
        modifier = Modifier
            .padding(vertical = 12.dp),
        text = "Site has not been synced.",
        fontSize = 16.sp
    )

    ElevatedButton(
        modifier = Modifier.padding(8.dp),
        colors =
            androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = BrightBlue,
                contentColor = Color.White
            ),
        shape = RoundedCornerShape(10),
        onClick = onClick
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Download,
                contentDescription = "Error icon"
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = "SYNC", fontSize = 16.sp)

        }
    }


}

@Composable
fun MessageBox(message: String) {

    Box(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(1f)
            .clip(RoundedCornerShape(10))
            .background(BabyBlue)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.padding(5.dp),
                imageVector = Icons.Outlined.Info,
                contentDescription = "Error icon",
                tint = Color.Black
            )

            Text(
                text = message,
                modifier = Modifier.padding(10.dp),
                fontSize = 16.sp,
                color = MidnightBlue
            )
        }


    }

}

@Composable
fun DefectListItem(point: PointDomain?, onClick: () -> Unit) {

    Card(
        modifier = Modifier
            .padding(horizontal = 5.dp, vertical = 3.dp)
            .heightIn(max = 50.dp),
        shape = RoundedCornerShape(5.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color = Color.White),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(width = 30.dp)
                    .fillMaxHeight()
                    .background(color = PointItemPriority.from(point?.priority ?: "").color),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(15.dp),
                    painter = painterResource(PointItemStatus.from(point?.status ?: "").icon),
                    contentDescription = "Point icon",
                    tint = Color.White

                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            if (point?.isModified == true) {
                Icon(
                    painter = painterResource(R.drawable.ic_cloud_upload_solid),
                    contentDescription = "Point icon",

                    )
            }

            Text(
                text = point?.sequenceNumber.toString(),
                color = Color.Black,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(10.dp))


            VerticalDivider(
                color = Color.Gray,
                thickness = 0.2.dp,
            )

            if (point?.flagged == true) {
                Icon(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    painter = painterResource(R.drawable.ic_red_flag),
                    contentDescription = "Point icon",
                    tint = Color.Red

                )
            }
            Text(
                text = point?.title ?: "",
                color = Color.Black,
                fontSize = 16.sp,
            )

        }
    }
}


@Preview(showBackground = true)
@Composable
fun SyncScreenPreview() {
    PinpointworksNewTheme {
//        DefectListScreen()
    }
}