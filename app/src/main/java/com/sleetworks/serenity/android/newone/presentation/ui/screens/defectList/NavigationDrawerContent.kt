package com.sleetworks.serenity.android.newone.presentation.ui.screens.defectList

import CircularImage
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sleetworks.serenity.android.newone.BuildConfig
import com.sleetworks.serenity.android.newone.R
import com.sleetworks.serenity.android.newone.presentation.model.UserUiModel
import com.sleetworks.serenity.android.newone.presentation.model.WorkspaceUiModel
import com.sleetworks.serenity.android.newone.presentation.viewmodels.PointViewModel
import com.sleetworks.serenity.android.newone.ui.theme.BrightBlue
import com.sleetworks.serenity.android.newone.ui.theme.OuterSpace_2
import com.sleetworks.serenity.android.newone.ui.theme.TransparentBlack
import com.sleetworks.serenity.android.newone.ui.theme.gray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawerContent(
    scope: CoroutineScope,
    drawerState: DrawerState,
    pointViewModel: PointViewModel,
) {
    val user by pointViewModel.user.collectAsState()
    val imageFile by pointViewModel.imageFile.collectAsState()
    val workspaces by pointViewModel.workspaces.collectAsState()
    val workspaceID by pointViewModel.workspaceID.collectAsState()

    LaunchedEffect(user.imageID) {
        pointViewModel.getUserImage(user.imageID)
    }

    ModalDrawerSheet(
        modifier = Modifier.fillMaxWidth(0.65f),
        drawerContainerColor = OuterSpace_2
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
,            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            DrawerHeader(user, imageFile)

            Spacer(modifier = Modifier.height(20.dp))

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 2.dp,
                color = TransparentBlack
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(), horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(R.drawable.ic_bell_notification),
                    contentDescription = "Notification",
                    tint = null
                )
                Spacer(modifier = Modifier.width(15.dp))

                Text(
                    text = "Notifications",
                    style = TextStyle(color = Color.White, fontSize = 16.sp),
                )

            }
            Spacer(modifier = Modifier.height(16.dp))


            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 2.dp,
                color = TransparentBlack
            )

            DrawerContentItem(workspaces, workspaceID) { workspace ->
                scope.launch {
                    drawerState.close()
                    workspace?.let { workspace ->
                        scope.launch(Dispatchers.IO) {
                            pointViewModel.storeWorkspaceIDAndSiteID(
                                workspace.id,
                                workspace.siteRef.id
                            )
                            pointViewModel.syncWorkspacesData()

                        }
                    }
                }
            }

            DrawerFooter()

        }
    }

}

@Composable
fun DrawerHeader(user: UserUiModel, imageFile: File?) {
    Box(modifier = Modifier.fillMaxHeight(0.15f).padding(15.dp)) {
        CircularImage(user.username, imageFile)
    }
    Text(
        text = user.username,
        style = TextStyle(color = Color.White, fontWeight = Bold, fontSize = 14.sp),
    )
    Text(
        text = user.email,
        style = TextStyle(color = Color.White, fontWeight = Bold, fontSize = 14.sp),
        modifier = Modifier.padding(bottom = 20.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerFooter() {
    Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(1.5f).background(color = gray), contentAlignment = Alignment.Center) {
        Column {
            Spacer(modifier = Modifier.height(15.dp))

            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_setting),
                    contentDescription = "Settings",
                    tint = null

                )
                Spacer(modifier = Modifier.width(10.dp))

                Text(text = "Settings", fontSize = 16.sp, color = Color.White)

            }
            Spacer(modifier = Modifier.height(15.dp))

            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_sign_out),
                    contentDescription = "Logout",
                            tint = null

                )
                Spacer(modifier = Modifier.width(10.dp))

                Text(text = "Logout", fontSize = 16.sp, color = Color.White)

            }
            Spacer(modifier = Modifier.height(15.dp))

            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = "Logo",
                    tint = null
                )
                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Version ${BuildConfig.VERSION_NAME}",
                    fontSize = 16.sp,
                    color = Color.White
                )

            }
            Spacer(modifier = Modifier.height(15.dp))

        }

    }
}

@Composable
fun DrawerContentItem(
    workspaces: List<WorkspaceUiModel?>,
    workspaceID: String?,
    onClick: (WorkspaceUiModel?) -> Unit
) {
    val grouped = workspaces.groupBy { it?.accountRef?.id }

    LazyColumn(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.75f).padding(15.dp),
        horizontalAlignment = Alignment.Start
    ) {

        grouped.forEach { (accountID, workspaceList) ->
            item {
                WorkspaceHeading(workspaceList.first()?.accountRef?.caption ?: "")
            }
            items(workspaceList, key = { workspace -> workspace?.id ?: "" }) { workspace ->
                SiteItem(workspace, workspaceID, onClick)
            }


        }
    }

}

@Composable
fun WorkspaceHeading(name: String) {
    Text(
        modifier = Modifier.padding(vertical = 6.dp),
        text = name,
        style = TextStyle(color = Color.White, fontWeight = Bold, fontSize = 18.sp),
    )
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 2.dp,
        color = TransparentBlack
    )
}

@Composable
fun SiteItem(
    workspace: WorkspaceUiModel?,
    workspaceID: String?,
    onClick: (WorkspaceUiModel?) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clickable(onClick = {
                onClick(workspace)
            }),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = workspace?.id == workspaceID,
            onClick = null, // Handled by Row click
            colors = RadioButtonDefaults.colors(
                selectedColor = BrightBlue,
                unselectedColor = Color.Gray
            )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            modifier = Modifier.padding(vertical = 6.dp),
            text =
                workspace?.siteRef?.caption ?: "",
            style = TextStyle(
                color = if (workspace?.id == workspaceID) BrightBlue else Color.White,
                fontSize = 16.sp
            ),
        )
    }


    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 2.dp,
        color = TransparentBlack
    )
}