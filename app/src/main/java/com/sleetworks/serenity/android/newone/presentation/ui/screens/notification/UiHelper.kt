//package com.sleetworks.serenity.android.newone.presentation.ui.screens.notification
//
//import android.graphics.Color
//import android.os.Build
//import android.text.Html
//import android.util.Log
//import android.view.View
//import android.widget.ImageView
//import android.widget.TextView
//import com.sleetworks.serenity.android.newone.data.models.remote.response.notification.Notification
//import com.sleetworks.serenity.android.newone.domain.models.AssigneeDomain
//import com.sleetworks.serenity.android.newone.presentation.model.PushNotificationFieldType
//import com.sleetworks.serenity.android.newone.presentation.ui.model.PointItemPriority
//import com.sleetworks.serenity.android.newone.presentation.ui.model.PointItemStatus
//
// fun getNotificationReason(notificationReason: String): String {
//    return when (notificationReason) {
//        "created" -> {
//            "you created"
//        }
//
//        "assigned" -> {
//            "you are assigned"
//        }
//
//        else -> {
//            "you are subscribed"
//        }
//    }
//}
//
//
// fun getPriorityType(type: String): String {
//    val priority: PointItemPriority? = PointItemPriority.from(type)
//     return if (priority === PointItemPriority.Low) {
//         "<span style='color:#4da0e5'><b>Low</b></span>"
//     } else if (priority === PointItemPriority.High) {
//         "<span style='color:#e54f50'><b>High</b></span>"
//     } else {
//         "<span style='color:#ffce29'><b>Medium</b></span>"
//     }
//}
//
//
// fun getStatusType(type: String): String {
//    val status: PointItemStatus? = PointItemStatus.from(type)
//    return if (status === PointItemStatus.InProgress) {
//        "<span style='color:#6bc8f9'><b>In Progress</b></span>"
//    } else if (status === PointItemStatus.OnHold) {
//        "<span style='color:#ff9801'><b>On Hold</b></span>"
//    } else if (status === PointItemStatus.ToReview) {
//        "<span style='color:#f3db12'><b>To Review</b></span>"
//    } else if (status === PointItemStatus.Canceled) {
//        "<span style='color:#838b99'><b>Canceled</b></span>"
//    } else if (status === PointItemStatus.Completed) {
//        "<span style='color:#65b92e'><b>Completed</b></span>"
//    } else {
//        "<span style='color:#3170a7'><b>Open</b></span>"
//    }
//}
//
// fun getAttachmentType(fieldType: PushNotificationFieldType?): String {
//    return if (fieldType === PushNotificationFieldType.IMAGES
//        || fieldType === PushNotificationFieldType.IMAGES_360
//    ) {
//        "an <b>Image</b>"
//    } else if (fieldType === PushNotificationFieldType.VIDEOS) {
//        "a <b>Video</b>"
//    } else if (fieldType === PushNotificationFieldType.DOCUMENTS) {
//        "a <b>File</b>"
//    } else {
//        "<b>an Attachment</b>"
//    }
//}
//
// fun getMentionType(label: String): String {
//    return if (label.isEmpty()) {
//        "a <b>Comment</b>"
//    } else if (label == "Description") {
//        "a <b>$label</b>"
//    } else {
//        "<b>$label</b>"
//    }
//}
//
//private fun setContentBody(
//    notificationItem: Notification,
//    fieldType: PushNotificationFieldType,
//    users: Map<String, AssigneeDomain>
//) {
//    var label = ""
//    var oldStringValue = ""
//    var newStringValue: String? = ""
//
//    val notificationReason = getNotificationReason(notificationItem.notificationReason)
//
//
//
//    label = notificationItem.changeBody.label
//    if (notificationItem.changeBody.oldValue is String) {
//        oldStringValue = notificationItem.changeBody.oldValue
//    }
//
//    if (notificationItem.changeBody.newValue is String) {
//        newStringValue = notificationItem.changeBody.newValue as String?
//    }
//
//
//    var style = "&nbsp;&nbsp;<span style='color:#0084f8'>"
//    var setOrChanged = "Changed"
//    if (oldStringValue.isEmpty()) {
//        setOrChanged = "Set"
//    }
//
//
//    var notificationTypeText = ""
//    var valueChangedText: String? = ""
//
//    when (fieldType) {
//        PushNotificationFieldType.UNKNOWN -> {
//            notificationTypeText = "Made changes of a point $notificationReason to."
////            notificationItem.findViewById<View?>(R.id.valueChangedLayout).setVisibility(View.GONE)
//        }
//
//        PushNotificationFieldType.TITLE, PushNotificationFieldType.DESCRIPTION, PushNotificationFieldType.CF_TEXT, PushNotificationFieldType.CF_RICHTEXT -> {
//            notificationTypeText =
//                "$style$setOrChanged</span> the <b>$label</b> of a point $notificationReason to:"
//            valueChangedText = newStringValue
//        }
//
//        PushNotificationFieldType.PRIORITY -> {
//            val priorityOldValue = getPriorityType(oldStringValue)
//            val priorityNewValue = getPriorityType(newStringValue!!)
//            notificationTypeText =
//                (style + setOrChanged + "</span> the <b>" + label + "</b> of a point " + notificationReason
//                        + " from " + priorityOldValue + " to " + priorityNewValue + ".")
////            notificationItem.findViewById<View?>(R.id.valueChangedLayout).setVisibility(View.GONE)
//        }
//
//        PushNotificationFieldType.STATUS -> {
//            val statusOldValue = getStatusType(oldStringValue)
//            val statusNewValue = getStatusType(newStringValue!!)
//            notificationTypeText =
//                (style + setOrChanged + "</span> the <b>" + label + "</b> of a point " + notificationReason
//                        + " from " + statusOldValue + " to " + statusNewValue + ".")
////            notificationItem.findViewById<View?>(R.id.valueChangedLayout).setVisibility(View.GONE)
//        }
//
//        PushNotificationFieldType.TAGS, PushNotificationFieldType.ASSIGNEES -> {
//            val oldListValue: MutableList<String?>
//            val newListValue: MutableList<String?>
//            try {
//                oldListValue =
//                    notificationItem.changeBody.oldValue as MutableList<String?>
//                newListValue =
//                    notificationItem.changeBody.newValue as MutableList<String?>
//            } catch (e: Exception) {
//                Log.e("Notification", "setContentBody: ", e)
//                return
//            }
//            val uniqueOldValueList: MutableList<String?> =
//                getUniqueValues(oldListValue, newListValue, fieldType,users)
//            val uniqueNewValueList: MutableList<String?> =
//                getUniqueValues(newListValue, oldListValue, fieldType,users)
//
//            val removedIconView =
//                notificationItem.findViewById<View?>(R.id.removeIconType) as ImageView
//            if (fieldType === PushNotificationFieldType.tags) {
//                notificationIv.setImageResource(R.drawable.ic_notification_tags)
//                removedIconView.setImageResource(R.drawable.ic_notification_tags)
//            } else {
//                notificationIv.setImageResource(R.drawable.ic_notification_assignees)
//                removedIconView.setImageResource(R.drawable.ic_notification_assignees)
//            }
//
//            if (uniqueNewValueList.size > 0) {
//                notificationTypeText =
//                    style + "Added</span> the following <b>" + label + "</b> to a point " + notificationReason + ":"
//                val itemContainer: FlexboxLayout =
//                    notificationItem.findViewById(R.id.addedItemsContainer)
//                for (value in uniqueNewValueList) {
//                    val itemView: View = getLayoutInflater().inflate(R.layout.new_part_tag, null)
//                    (itemView.findViewById<View?>(R.id.tagName) as TextView).setText(value)
//                    itemContainer.addView(itemView)
//                }
//                itemContainer.setVisibility(View.VISIBLE)
//            } else {
//                notificationIv.setVisibility(View.GONE)
//                notificationItem.findViewById<View?>(R.id.notificationTypeLayout)
//                    .setVisibility(View.GONE)
//                notificationTypeText = ""
//            }
//
//            if (uniqueOldValueList.size > 0) {
//                removedIconView.setVisibility(View.VISIBLE)
//                style = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style='color:#0084f8'>"
//
//                valueChangedText =
//                    style + "Removed</span> the following <b>" + label + "</b> to a point " + notificationReason + ":"
//                val itemContainer: FlexboxLayout =
//                    notificationItem.findViewById(R.id.removedItemsContainer)
//                for (value in uniqueOldValueList) {
//                    val itemView: View = getLayoutInflater().inflate(R.layout.new_part_tag, null)
//                    (itemView.findViewById<View?>(R.id.tagName) as TextView).setText(value)
//                    itemContainer.addView(itemView)
//                }
//                itemContainer.setVisibility(View.VISIBLE)
//            } else {
//                valueChangedText = ""
//                notificationItem.findViewById<View?>(R.id.valueChangedLayout)
//                    .setVisibility(View.GONE)
//            }
//        }
//
//        cfPercentage, cfNumbers, cfList, cfDate, cfCost, cfTime, cfTimeline -> {
//            if (setOrChanged == "Set") {
//                notificationTypeText =
//                    (style + setOrChanged + "</span> the <b>" + label + "</b> of a point " + notificationReason
//                            + " to:")
//                valueChangedText = newStringValue
//            } else {
//                notificationTypeText =
//                    (style + setOrChanged + "</span> the <b>" + label + "</b> of a point " + notificationReason
//                            + " from: " + oldStringValue + " to: " + newStringValue + ".")
//                notificationItem.findViewById<View?>(R.id.valueChangedLayout)
//                    .setVisibility(View.GONE)
//            }
//        }
//
//        cfCheckbox -> {
//            val checked = if (notificationEntity.getChangeBody().getNewValue()
//                    .equals("yes")
//            ) "Checked" else "Unchecked"
//            notificationTypeText =
//                style + checked + "</span> the <b>" + label + "</b> of a point " + notificationReason + "."
//            notificationItem.findViewById<View?>(R.id.valueChangedLayout).setVisibility(View.GONE)
//        }
//
//        pin, additionalPins, pins, polygons -> {
//            notificationTypeText =
//                style + "Changed</span> the <b>Location</b> of a point " + notificationReason + "."
//            notificationItem.findViewById<View?>(R.id.valueChangedLayout).setVisibility(View.GONE)
//        }
//
//        images, images360, videos, documents, attachments -> {
//            val oldMapValue: MutableList<MutableMap<String?, String?>?>
//            val newMapValue: MutableList<MutableMap<String?, String?>?>
//            try {
//                oldMapValue = notificationEntity.getChangeBody()
//                    .getOldValue() as MutableList<MutableMap<String?, String?>?>
//                newMapValue = notificationEntity.getChangeBody()
//                    .getNewValue() as MutableList<MutableMap<String?, String?>?>
//            } catch (e: ClassCastException) {
//                break
//            } catch (e: NullPointerException) {
//                break
//            }
//
//            val isAttachmentAdded = newMapValue.size > oldMapValue.size
//            val statusType = if (isAttachmentAdded) "Added" else "Removed"
//            val pointType = if (isAttachmentAdded) "to" else "from"
//            val attachmentType = getAttachmentType(fieldType)
//
//            notificationTypeText = style + statusType + "</span> " + attachmentType + " " +
//                    pointType + " a point " + notificationReason + "."
//            notificationItem.findViewById<View?>(R.id.valueChangedLayout).setVisibility(View.GONE)
//        }
//
//        comments -> {
//            notificationTypeText =
//                style + "Commented</span> on a point " + notificationReason + " to:"
//            valueChangedText = "\"" + notificationEntity.getChangeBody().getComment() + "\""
//        }
//
//        commentLike -> {
//            notificationTypeText = style + "Liked</span> your <b>comment</b>."
//            notificationItem.findViewById<View?>(R.id.valueChangedLayout).setVisibility(View.GONE)
//        }
//
//        assignedToNewPoint -> {
//            notificationTypeText = style + "You</span> were <b>assigned</b> to a new point."
//            notificationItem.findViewById<View?>(R.id.valueChangedLayout).setVisibility(View.GONE)
//        }
//
//        mentionedOnNewPoint -> {
//            notificationTypeText =
//                "<span style='color:#0084f8'>@ Mentioned</span> you in a new point."
//            notificationItem.findViewById<View?>(R.id.valueChangedLayout).setVisibility(View.GONE)
//        }
//
//        mentionedInCustomField, mentionedInDescription, mentionedInComments -> {
//            val mentionType = getMentionType(label)
//            notificationTypeText =
//                "<span style='color:#0084f8'>@ Mentioned</span> you in " + mentionType + ":"
//            val contentValue: String?
//            if (label.isEmpty()) {
//                contentValue = notificationEntity.getChangeBody().getComment()
//            } else {
//                contentValue = newStringValue
//            }
//            valueChangedText = "\"" + contentValue + "\""
//        }
//
//        flagged -> {
//            val isFlagged = notificationEntity.getChangeBody().getNewValue() as Boolean
//            notificationIv.setVisibility(View.VISIBLE)
//            if (isFlagged) {
//                notificationTypeText = "\tFlagged this point."
//                notificationIv.setImageResource(R.drawable._redflag_red_flag)
//            } else {
//                notificationTypeText = " Cleared the flag for this point."
//                notificationIv.setImageResource(R.drawable._greyflagged)
//            }
//            notificationItem.findViewById<View?>(R.id.valueChangedLayout).setVisibility(View.GONE)
//        }
//
//        reminderCreated -> {
//            notificationIv.setVisibility(View.VISIBLE)
//            notificationItem.findViewById<View?>(R.id.valueChangedLayout).setVisibility(View.GONE)
//            notificationTypeText = "<span style='color:#0084f8'>Created</span> a reminder for you."
//        }
//
//        reminderEdited -> {
//            notificationIv.setVisibility(View.VISIBLE)
//
//            notificationItem.findViewById<View?>(R.id.valueChangedLayout).setVisibility(View.GONE)
//            notificationTypeText =
//                "<span style='color:#0084f8'>Updated</span> a reminder they created for you."
//        }
//
//        reminderDeleted -> {
//            notificationIv.setVisibility(View.VISIBLE)
//            notificationIv.setColorFilter(Color.RED)
//
//            notificationItem.findViewById<View?>(R.id.valueChangedLayout).setVisibility(View.GONE)
//            notificationTypeText =
//                "<span style='color:#FF3D3D'>Deleted</span> a reminder they created for you."
//        }
//
//        else -> {}
//    }
//
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//        notificationTypeLabel.setText(
//            Html.fromHtml(
//                notificationTypeText,
//                Html.FROM_HTML_MODE_LEGACY
//            )
//        )
//        valueChangedLabel.setText(Html.fromHtml(valueChangedText, Html.FROM_HTML_MODE_LEGACY))
//    } else {
//        notificationTypeLabel.setText(Html.fromHtml(notificationTypeText))
//        valueChangedLabel.setText(Html.fromHtml(valueChangedText))
//    }
//}
//
// fun getUniqueValues(
//    originalValues: MutableList<String?>,
//    compareValues: MutableList<String?>,
//    fieldType: PushNotificationFieldType?,
//    users: Map<String, AssigneeDomain>
//): MutableList<String?> {
//    val uniqueValues: MutableList<String?> = ArrayList(originalValues)
//    uniqueValues.removeAll(compareValues)
//
//    if (fieldType === PushNotificationFieldType.ASSIGNEES) {
//        val assigneeCaptions: MutableList<String?> = ArrayList<String?>()
//        for (value in uniqueValues) {
//            val assignee: AssigneeDomain? = users[value]
//            if (assignee != null && assignee.caption.isNotEmpty()) assigneeCaptions.add(assignee.caption)
//            else assigneeCaptions.add("(Deleted user)")
//        }
//        return assigneeCaptions
//    }
//
//    return uniqueValues
//}
