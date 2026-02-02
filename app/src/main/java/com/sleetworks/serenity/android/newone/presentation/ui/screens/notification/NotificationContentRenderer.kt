package com.sleetworks.serenity.android.newone.presentation.ui.screens.notification

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sleetworks.serenity.android.newone.data.models.remote.response.notification.ChangeBody
import com.sleetworks.serenity.android.newone.domain.models.FirebaseNotificationType
import com.sleetworks.serenity.android.newone.domain.models.NotificationDomain
import com.sleetworks.serenity.android.newone.presentation.model.PushNotificationFieldType
import com.sleetworks.serenity.android.newone.presentation.ui.model.PointItemPriority
import com.sleetworks.serenity.android.newone.presentation.ui.model.PointItemStatus
import com.sleetworks.serenity.android.newone.presentation.viewmodels.NotificationViewModel
import com.sleetworks.serenity.android.newone.ui.theme.gray
import androidx.compose.ui.graphics.Color as ComposeColor

/**
 * Renders the full notification content based on field type
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationContentRenderer(
    notification: NotificationDomain,
    fieldType: PushNotificationFieldType,
    viewModel: NotificationViewModel,
    onLikeClick: ((String) -> Unit)? = null,
    isLiked: Boolean = false,
    modifier: Modifier = Modifier
) {
    val changeBody = notification.changeBody
    val label = changeBody?.label ?: ""
    val oldStringValue = if (changeBody?.oldValue is String) changeBody.oldValue as String else ""
    val newStringValue = if (changeBody?.newValue is String) changeBody.newValue as String else ""
    val notificationReason = getNotificationReason(notification.notificationReason)
    val setOrChanged = if (oldStringValue.isEmpty()) "Set" else "Changed"

    Column(modifier = modifier) {
        // Notification Icon


        // Notification Type Text
        val notificationTypeText = buildNotificationTypeText(
            fieldType = fieldType,
            label = label,
            setOrChanged = setOrChanged,
            notificationReason = notificationReason,
            oldStringValue = oldStringValue,
            newStringValue = newStringValue,
            changeBody = changeBody,
            notification = notification
        )

        if (notificationTypeText.isNotEmpty()) {
            Row {
                val iconRes = fieldType.iconRes
                if (iconRes >= 0) {
                    Icon(
                        modifier = Modifier.size(15.dp),
                        painter = painterResource(iconRes),
                        contentDescription = "notification type icon",
                        tint = null
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = notificationTypeText,
                        style = TextStyle(fontSize = 14.sp, color = Color.Black),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }

        }

        // Value Changed Text
        val valueChangedText = buildValueChangedText(
            fieldType = fieldType,
            label = label,
            notificationReason = notificationReason,
            oldStringValue = oldStringValue,
            newStringValue = newStringValue,
            changeBody = changeBody,
            notification = notification
        )

        if (valueChangedText.isNotEmpty()) {
            Text(
                text = valueChangedText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        // Tags/Assignees display
        if (fieldType == PushNotificationFieldType.TAGS || fieldType == PushNotificationFieldType.ASSIGNEES) {
            TagsAssigneesDisplay(
                fieldType = fieldType,
                changeBody = changeBody,
                notificationReason = notificationReason,
                viewModel = viewModel
            )
        }

        // Like button for comments
        if ((fieldType == PushNotificationFieldType.COMMENTS ||
                    fieldType == PushNotificationFieldType.MENTIONED_IN_COMMENTS) &&
            onLikeClick != null
        ) {
            LikeButton(
                commentId = changeBody?.commentId ?: "",
                isLiked = isLiked,
                onLikeClick = onLikeClick
            )
        }
    }
}

@Composable
private fun TagsAssigneesDisplay(
    fieldType: PushNotificationFieldType,
    changeBody: ChangeBody?,
    notificationReason: String,
    viewModel: NotificationViewModel,
) {
    val users by viewModel.workspaceUser.collectAsState()
    if (changeBody == null) return

    val oldListValue = try {
        (changeBody.oldValue as? List<*>)?.filterIsInstance<String>() ?: emptyList()
    } catch (e: Exception) {
        emptyList()
    }

    val newListValue = try {
        (changeBody.newValue as? List<*>)?.filterIsInstance<String>() ?: emptyList()
    } catch (e: Exception) {
        emptyList()
    }

    val uniqueOldValueList = getUniqueValues(oldListValue, newListValue, fieldType)
    val uniqueNewValueList = getUniqueValues(newListValue, oldListValue, fieldType)

    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        if (uniqueNewValueList.isNotEmpty()) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = ComposeColor(0xFF0084F8))) {
                        append("Added")
                    }
                    append(" the following ${fieldType.rawValue} to a point $notificationReason:")
                },
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 3
            ) {
                repeat(uniqueNewValueList.size) { index ->
                    val value = if (fieldType == PushNotificationFieldType.ASSIGNEES) {
                        users[uniqueNewValueList[index]]?.caption ?: uniqueNewValueList[index]
                    } else {
                        uniqueNewValueList[index]
                    }
                    TagChip(text =value)

                }
            }
        }

        if (uniqueOldValueList.isNotEmpty()) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = ComposeColor(0xFF0084F8))) {
                        append("Removed")
                    }
                    append(" the following ${fieldType.rawValue} from a point $notificationReason:")
                },
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 3
            ) {
                repeat(uniqueOldValueList.size) { index ->
                    TagChip(text = uniqueOldValueList.get(index))

                }

            }
        }
    }
}

@Composable
private fun TagChip(text: String) {
    Surface(
        shape = RoundedCornerShape(5.dp),
        color = gray,
        modifier = Modifier.padding(2.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun LikeButton(
    commentId: String,
    isLiked: Boolean,
    onLikeClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(top = 4.dp)
            .clickable { onLikeClick(commentId) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Icon would be loaded here - thumb_up or thumb_up_blue based on isLiked
        Text(
            text = if (isLiked) "👍 Liked" else "👍 Like",
            style = MaterialTheme.typography.labelMedium,
            color = if (isLiked) ComposeColor(0xFF0084F8) else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun buildNotificationTypeText(
    fieldType: PushNotificationFieldType,
    label: String,
    setOrChanged: String,
    notificationReason: String,
    oldStringValue: String,
    newStringValue: String,
    changeBody: ChangeBody?,
    notification: NotificationDomain
): AnnotatedString {
    return buildAnnotatedString {
        when (fieldType) {
            PushNotificationFieldType.UNKNOWN -> {
                append("Made changes of a point $notificationReason to.")
            }

            PushNotificationFieldType.TITLE,
            PushNotificationFieldType.DESCRIPTION,
            PushNotificationFieldType.CF_TEXT,
            PushNotificationFieldType.CF_RICHTEXT -> {
                withStyle(style = SpanStyle(color = ComposeColor(0xFF0084F8))) {
                    append(setOrChanged)
                }
                append(" the ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(label)
                }
                append(" of a point $notificationReason to:")
            }

            PushNotificationFieldType.PRIORITY -> {
                val priorityOldValue = getPriorityType(oldStringValue)
                val priorityNewValue = getPriorityType(newStringValue)
                withStyle(style = SpanStyle(color = ComposeColor(0xFF0084F8))) {
                    append(setOrChanged)
                }
                append(" the ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(label)
                }
                append(" of a point $notificationReason from $priorityOldValue to $priorityNewValue.")
            }

            PushNotificationFieldType.STATUS -> {
                val statusOldValue = getStatusType(oldStringValue)
                val statusNewValue = getStatusType(newStringValue)
                withStyle(style = SpanStyle(color = ComposeColor(0xFF0084F8))) {
                    append(setOrChanged)
                }
                append(" the ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(label)
                }
                append(" of a point $notificationReason from $statusOldValue to $statusNewValue.")
            }

            PushNotificationFieldType.CF_PERCENTAGE,
            PushNotificationFieldType.CF_NUMBERS,
            PushNotificationFieldType.CF_LIST,
            PushNotificationFieldType.CF_DATE,
            PushNotificationFieldType.CF_COST,
            PushNotificationFieldType.CF_TIME,
            PushNotificationFieldType.CF_TIMELINE -> {
                if (setOrChanged == "Set") {
                    withStyle(style = SpanStyle(color = ComposeColor(0xFF0084F8))) {
                        append(setOrChanged)
                    }
                    append(" the ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(label)
                    }
                    append(" of a point $notificationReason to:")
                } else {
                    withStyle(style = SpanStyle(color = ComposeColor(0xFF0084F8))) {
                        append(setOrChanged)
                    }
                    append(" the ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(label)
                    }
                    append(" of a point $notificationReason from: $oldStringValue to: $newStringValue.")
                }
            }

            PushNotificationFieldType.CF_CHECKBOX -> {
                val checked = if (changeBody?.newValue == "yes") "Checked" else "Unchecked"
                withStyle(style = SpanStyle(color = ComposeColor(0xFF0084F8))) {
                    append(checked)
                }
                append(" the ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(label)
                }
                append(" of a point $notificationReason.")
            }

            PushNotificationFieldType.PIN,
            PushNotificationFieldType.ADDITIONAL_PINS,
            PushNotificationFieldType.PINS,
            PushNotificationFieldType.POLYGONS -> {
                withStyle(style = SpanStyle(color = ComposeColor(0xFF0084F8))) {
                    append("Changed")
                }
                append(" the ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Location")
                }
                append(" of a point $notificationReason.")
            }

            PushNotificationFieldType.IMAGES,
            PushNotificationFieldType.IMAGES_360,
            PushNotificationFieldType.VIDEOS,
            PushNotificationFieldType.DOCUMENTS,
            PushNotificationFieldType.ATTACHMENTS -> {
                val oldMapValue = try {
                    (changeBody?.oldValue as? List<*>)?.filterIsInstance<Map<*, *>>() ?: emptyList()
                } catch (e: Exception) {
                    emptyList()
                }
                val newMapValue = try {
                    (changeBody?.newValue as? List<*>)?.filterIsInstance<Map<*, *>>() ?: emptyList()
                } catch (e: Exception) {
                    emptyList()
                }
                val isAttachmentAdded = newMapValue.size > oldMapValue.size
                val statusType = if (isAttachmentAdded) "Added" else "Removed"
                val pointType = if (isAttachmentAdded) "to" else "from"
                val attachmentType = getAttachmentType(fieldType)

                withStyle(style = SpanStyle(color = ComposeColor(0xFF0084F8))) {
                    append(statusType)
                }
                append(" $attachmentType $pointType a point $notificationReason.")
            }

            PushNotificationFieldType.COMMENTS -> {
                withStyle(style = SpanStyle(color = ComposeColor(0xFF0084F8))) {
                    append("Commented")
                }
                append(" on a point $notificationReason to:")
            }

            PushNotificationFieldType.COMMENT_LIKE -> {
                withStyle(style = SpanStyle(color = ComposeColor(0xFF0084F8))) {
                    append("Liked")
                }
                append(" your ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("comment")
                }
                append(".")
            }

            PushNotificationFieldType.ASSIGNED_TO_NEW_POINT -> {
                withStyle(style = SpanStyle(color = ComposeColor(0xFF0084F8))) {
                    append("You")
                }
                append(" were ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("assigned")
                }
                append(" to a new point.")
            }

            PushNotificationFieldType.MENTIONED_ON_NEW_POINT -> {
                withStyle(style = SpanStyle(color = ComposeColor(0xFF0084F8))) {
                    append("@ Mentioned")
                }
                append(" you in a new point.")
            }

            PushNotificationFieldType.MENTIONED_IN_CUSTOM_FIELD,
            PushNotificationFieldType.MENTIONED_IN_DESCRIPTION,
            PushNotificationFieldType.MENTIONED_IN_COMMENTS -> {
                val mentionType = getMentionType(label)
                withStyle(style = SpanStyle(color = ComposeColor(0xFF0084F8))) {
                    append("@ Mentioned")
                }
                append(" you in $mentionType:")
            }

            PushNotificationFieldType.FLAGGED -> {
                val isFlagged = changeBody?.newValue == true
                if (isFlagged) {
                    append("Flagged this point.")
                } else {
                    append("Cleared the flag for this point.")
                }
            }

            PushNotificationFieldType.REMINDER_CREATED -> {
                withStyle(style = SpanStyle(color = ComposeColor(0xFF0084F8))) {
                    append("Created")
                }
                append(" a reminder for you.")
            }

            PushNotificationFieldType.REMINDER_DELETED -> {
                withStyle(style = SpanStyle(color = ComposeColor(0xFF0084F8))) {
                    append("Updated")
                }
                append(" a reminder they created for you.")
            }

            PushNotificationFieldType.REMINDER_EDITED -> {
                withStyle(style = SpanStyle(color = ComposeColor(0xFFFF3D3D))) {
                    append("Deleted")
                }
                append(" a reminder they created for you.")
            }

            else -> {
                // Tags and assignees are handled separately
            }
        }
    }
}

private fun buildValueChangedText(
    fieldType: PushNotificationFieldType,
    label: String,
    notificationReason: String,
    oldStringValue: String,
    newStringValue: String,
    changeBody: ChangeBody?,
    notification: NotificationDomain
): AnnotatedString {
    return buildAnnotatedString {
        when (fieldType) {
            PushNotificationFieldType.TITLE,
            PushNotificationFieldType.DESCRIPTION,
            PushNotificationFieldType.CF_TEXT,
            PushNotificationFieldType.CF_RICHTEXT -> {
                append(newStringValue)
            }

            PushNotificationFieldType.CF_PERCENTAGE,
            PushNotificationFieldType.CF_NUMBERS,
            PushNotificationFieldType.CF_LIST,
            PushNotificationFieldType.CF_DATE,
            PushNotificationFieldType.CF_COST,
            PushNotificationFieldType.CF_TIME,
            PushNotificationFieldType.CF_TIMELINE -> {
                if (oldStringValue.isEmpty()) {
                    append(newStringValue)
                }
            }

            PushNotificationFieldType.COMMENTS -> {
                append("\"${changeBody?.comment ?: ""}\"")
            }

            PushNotificationFieldType.MENTIONED_IN_CUSTOM_FIELD,
            PushNotificationFieldType.MENTIONED_IN_DESCRIPTION,
            PushNotificationFieldType.MENTIONED_IN_COMMENTS -> {
                val contentValue = if (label.isEmpty()) {
                    changeBody?.comment ?: ""
                } else {
                    newStringValue
                }
                append("\"$contentValue\"")
            }

            else -> {
                // No value text for other types
            }
        }
    }
}

fun getNotificationReason(notificationReason: String?): String {
    return when (notificationReason) {
        "created" -> "you created"
        "assigned" -> "you are assigned"
        else -> "you are subscribed"
    }
}

private fun getPriorityType(type: String): String {
    return try {
        val priority = PointItemPriority.from(type)
        when (priority) {
            PointItemPriority.Low -> "Low"
            PointItemPriority.High -> "High"
            else -> "Medium"
        }
    } catch (e: Exception) {
        type
    }
}

private fun getStatusType(type: String): String {
    return try {
        val status = PointItemStatus.from(type)
        when (status) {
            PointItemStatus.InProgress -> "In Progress"
            PointItemStatus.OnHold -> "On Hold"
            PointItemStatus.ToReview -> "To Review"
            PointItemStatus.Canceled -> "Canceled"
            PointItemStatus.Completed -> "Completed"
            else -> "Open"
        }
    } catch (e: Exception) {
        type
    }
}

private fun getAttachmentType(fieldType: PushNotificationFieldType): String {
    return when (fieldType) {
        PushNotificationFieldType.IMAGES,
        PushNotificationFieldType.IMAGES_360 -> "an Image"

        PushNotificationFieldType.VIDEOS -> "a Video"
        PushNotificationFieldType.DOCUMENTS -> "a File"
        else -> "an Attachment"
    }
}

private fun getMentionType(label: String): String {
    return when {
        label.isEmpty() -> "a Comment"
        label == "Description" -> "a Description"
        else -> label
    }
}

private fun getUniqueValues(
    originalValues: List<String>,
    compareValues: List<String>,
    fieldType: PushNotificationFieldType
): List<String> {
    val uniqueValues = originalValues.toMutableList()
    uniqueValues.removeAll(compareValues)

    // For assignees, we'd need to look up user names from database
    // For now, just return the unique values
    return uniqueValues
}

fun getFieldType(notification: NotificationDomain): PushNotificationFieldType {
    val pushNotificationType = notification.pushNotificationType

    when (pushNotificationType) {
        FirebaseNotificationType.POINT_MENTION_NEW_POINT.notificationType -> {
            return PushNotificationFieldType.MENTIONED_ON_NEW_POINT
        }

        FirebaseNotificationType.POINT_MENTION_COMMENT.notificationType -> {
            return PushNotificationFieldType.MENTIONED_IN_COMMENTS
        }

        FirebaseNotificationType.POINT_MENTION_CUSTOM_FIELD.notificationType -> {
            return PushNotificationFieldType.MENTIONED_IN_CUSTOM_FIELD
        }

        FirebaseNotificationType.POINT_MENTION_DESCRIPTION.notificationType -> {
            return PushNotificationFieldType.MENTIONED_IN_DESCRIPTION
        }

        FirebaseNotificationType.POINT_CREATION_ASSIGNEE.notificationType -> {
            return PushNotificationFieldType.ASSIGNED_TO_NEW_POINT
        }

        FirebaseNotificationType.POINT_EDITION_CUSTOM_FIELDS.notificationType -> {
            notification.changeBody?.cfFieldType.let {
                val fieldType = PushNotificationFieldType.fromString(it)
                return fieldType
            }
        }

        FirebaseNotificationType.POINT_EDITION_FLAGGED.notificationType -> {
            return PushNotificationFieldType.FLAGGED
        }

        FirebaseNotificationType.REMINDER_CREATED_FOR.notificationType -> {
            return PushNotificationFieldType.REMINDER_CREATED
        }

        FirebaseNotificationType.REMINDER_EDITED_FOR.notificationType -> {
            return PushNotificationFieldType.REMINDER_EDITED
        }

        FirebaseNotificationType.REMINDER_DELETED_FOR.notificationType -> {
            return PushNotificationFieldType.REMINDER_DELETED
        }
    }

//    if (notification.reactionDetails != null &&
//        notification.reactionDetails?.reactionType?.isNotEmpty() == true
//    ) {
//        return PushNotificationFieldType.commentLike
//    }

    if (notification.changeBody == null) {
        return PushNotificationFieldType.UNKNOWN
    }

    if (notification.changeBody.label.isNotEmpty()) {
        val fieldType = PushNotificationFieldType.fromString(notification.changeBody.label)
        return fieldType
    }

    if (notification.changeBody.comment.isNotEmpty()) {
        return PushNotificationFieldType.COMMENTS
    }

    return PushNotificationFieldType.UNKNOWN
}

