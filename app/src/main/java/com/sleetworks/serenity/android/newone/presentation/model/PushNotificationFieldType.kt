package com.sleetworks.serenity.android.newone.presentation.model

import com.sleetworks.serenity.android.newone.R

enum class PushNotificationFieldType(
    val rawValue: String,
    val iconRes: Int
) {

    TITLE("Title", R.drawable.ic_notification_title),
    DESCRIPTION("Description", R.drawable.ic_notification_description),
    PRIORITY("Priority", R.drawable.ic_notification_priority),
    STATUS("Status", R.drawable.ic_notification_status),

    ASSIGNEES("Assignees", R.drawable.ic_notification_assignees),
    ASSIGNED_TO_NEW_POINT("AssignedToNewPoint", R.drawable.ic_notification_assignees),

    TAGS("Tags", R.drawable.ic_notification_tags),

    CF_TEXT("TEXT", R.drawable.ic_notification_text),
    CF_RICHTEXT("RICHTEXT", R.drawable.ic_notification_richtext),
    CF_COST("COST", R.drawable.ic_notification_cost),
    CF_DATE("DATE", R.drawable.ic_notification_date),
    CF_LIST("LIST", R.drawable.ic_notification_list),
    CF_TIME("TIME", R.drawable.ic_notification_time),
    CF_PERCENTAGE("PERCENTAGE", R.drawable.ic_notification_percentage),
    CF_NUMBERS("NUMBERS", R.drawable.ic_notification_numbers),
    CF_CHECKBOX("CHECKBOX", R.drawable.ic_notification_checkbox),
    CF_TIMELINE("TIMELINE", R.drawable.ic_notification_timeline),

    PIN("Location Pin", R.drawable.ic_notification_location),
    ADDITIONAL_PINS("Additional Pins", R.drawable.ic_notification_location),
    PINS("Pins", R.drawable.ic_notification_location),
    POLYGONS("Polygons", R.drawable.ic_notification_location),

    IMAGES("Images", R.drawable.ic_notification_image),
    IMAGES_360("360 Images", R.drawable.ic_notification_image),

    VIDEOS("Videos", R.drawable.ic_notification_video),

    DOCUMENTS("Documents", R.drawable.ic_notification_document),
    ATTACHMENTS("Attachments", R.drawable.ic_notification_document),

    COMMENTS("Comments", R.drawable.ic_notification_comment),
    COMMENT_LIKE("CommentLike", R.drawable.ic_notification_like),

    REMINDER_CREATED("ReminderCreated", R.drawable.icon_reminder),
    REMINDER_EDITED("ReminderEdited", R.drawable.icon_reminder),
    REMINDER_DELETED("ReminderDeleted", R.drawable.icon_reminder),

    MENTIONED_ON_NEW_POINT("MentionedOnNewPoint", -1),
    MENTIONED_IN_DESCRIPTION("MentionedInDescription", -1),
    MENTIONED_IN_CUSTOM_FIELD("MentionedInCustomField", -1),
    MENTIONED_IN_COMMENTS("MentionedInComments", -1),
    FLAGGED("Flagged", -1),

    UNKNOWN("Unknown", -1);

    companion object {

        private val map = values().associateBy { it.rawValue.lowercase() }

        fun fromString(value: String?): PushNotificationFieldType {
            return value
                ?.lowercase()
                ?.let { map[it] }
                ?: UNKNOWN
        }
    }
}
