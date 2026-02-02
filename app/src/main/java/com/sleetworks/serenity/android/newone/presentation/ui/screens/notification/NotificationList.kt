package com.sleetworks.serenity.android.newone.presentation.ui.screens.notification

import CircularImage
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sleetworks.serenity.android.newone.domain.models.AssigneeDomain
import com.sleetworks.serenity.android.newone.domain.models.NotificationDomain
import com.sleetworks.serenity.android.newone.presentation.model.PushNotificationFieldType
import com.sleetworks.serenity.android.newone.presentation.model.WorkspaceUiModel
import com.sleetworks.serenity.android.newone.presentation.viewmodels.NotificationViewModel
import com.sleetworks.serenity.android.newone.ui.theme.CoolMediumGray
import com.sleetworks.serenity.android.newone.ui.theme.OuterSpace
import com.sleetworks.serenity.android.newone.ui.theme.PaleBlue

data class NotificationItem(
    val notification: NotificationDomain,
    val dateLabel: String? = null
)

@Composable
fun NotificationList(
    notifications: List<NotificationItem>,
    viewModel: NotificationViewModel,
    onNotificationClick: (NotificationDomain) -> Unit,
    listState: LazyListState
) {
    var currentDateLabel: String? by remember { mutableStateOf(null) }

    val users by viewModel.workspaceUser.collectAsState()
    val workspaces by viewModel.workspaces.collectAsState()


    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(notifications) { item ->
            val workspace = workspaces[item.notification.workspaceId]
            // Show date label if needed
            item.dateLabel?.let { label ->
                if (currentDateLabel != label) {
                    currentDateLabel = label
                    DateLabel(label)
                }
            }
            NotificationItemCard(
                notification = item.notification,
                user = users[item.notification.creatorUserId],
                workspace = workspace,
                viewModel = viewModel,
                onNotificationClick = onNotificationClick
            )
        }
    }
}

@Composable
fun DateLabel(label: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}

@Composable
fun NotificationItemCard(
    notification: NotificationDomain,
    user: AssigneeDomain?,
    workspace: WorkspaceUiModel?,
    viewModel: NotificationViewModel,
    onNotificationClick: (NotificationDomain) -> Unit
) {
    val fieldType = PushNotificationFieldType.fromString(notification.pushNotificationType)
    val backgroundColor = if (notification.markedAsRead) {
        Color.White
    } else {
        PaleBlue
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onNotificationClick(notification) },
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {

        Row {
            NotificationAvatar(user = user, viewModel = viewModel)
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    // User Avatar
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        text = notification.creatorUserName ?: "Unknown",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = calculateElapsedTime(notification.timestampEpochMillis ?: 0),
                        fontSize = 13.sp,
                        color = CoolMediumGray
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                NotificationContent(notification = notification, viewModel = viewModel)

                Text(
                    text = if (fieldType == PushNotificationFieldType.REMINDER_DELETED
                        || fieldType == PushNotificationFieldType.REMINDER_EDITED
                        || fieldType == PushNotificationFieldType.REMINDER_CREATED
                    ) "${notification.reminderDetails?.pointSequenceNumber} - ${notification.reminderDetails?.pointTitle}"
                    else "${notification.refObject.sequenceNumber} - ${notification.refObject.name}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = OuterSpace,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                val siteInfo = remember(notification.workspaceId, notification.siteName) {

                    val accountName = workspace?.accountRef?.caption ?: ""
                    val siteName = notification.siteName
                    if (accountName.isNotEmpty() && siteName.isNotEmpty()) {
                        "$accountName > $siteName"
                    } else siteName
                }

                if (siteInfo.isNotEmpty()) {
                    Text(
                        text = siteInfo,
                        fontSize = 14.sp,
                        color = CoolMediumGray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }


    }

}

@Composable
fun NotificationAvatar(user: AssigneeDomain?, viewModel: NotificationViewModel) {
    val avatarSignal by viewModel.avatarUpdateSignal.collectAsState(initial = "")
    val avatarFile = remember(avatarSignal) {
        viewModel.getUserAvatar(
            user?.primaryImageId ?: ""
        )
    }
    Box(modifier = Modifier.padding(12.dp)) {
        CircularImage(imageFile = avatarFile, userName = user?.caption, size = 44)
    }
}

@Composable
fun NotificationContent(
    notification: NotificationDomain, viewModel: NotificationViewModel,
) {
    val fieldType = remember { getFieldType(notification) }
    val context = LocalContext.current

    // Get like state for comments
    val isLiked = remember(notification.changeBody?.commentId) {
        false
    }

    NotificationContentRenderer(
        notification = notification,
        fieldType = fieldType,
        viewModel = viewModel,
        onLikeClick = { commentId ->
        },
        isLiked = isLiked
    )
}

fun calculateElapsedTime(epochTimeMillis: Long): String {
    val timeDifference = System.currentTimeMillis() - epochTimeMillis
    val minutesDifference = timeDifference / (1000 * 60)

    return when {
        minutesDifference < 1 -> "just now"
        minutesDifference == 1L -> "a minute ago"
        minutesDifference < 60 -> "$minutesDifference minutes ago"
        else -> {
            val hoursDifference = minutesDifference / 60
            when {
                hoursDifference == 1L -> "an hour ago"
                hoursDifference < 24 -> "$hoursDifference hours ago"
                else -> {
                    val daysDifference = hoursDifference / 24
                    when {
                        daysDifference == 1L -> "a day ago"
                        else -> "$daysDifference days ago"
                    }
                }
            }
        }
    }
}