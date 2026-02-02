package com.sleetworks.serenity.android.newone.domain.models

enum class FirebaseNotificationType(
    val notificationType: String
) {

    POINT_CREATION("Created a point\n"),
    POINT_CREATION_ASSIGNEE("Assigned you to a point\n"),
    POINT_EDITION("Point has been edited\n"),
    POINT_EDITION_TITLE("Changed the Title\n"),
    POINT_EDITION_STATUS("Changed the Status\n"),
    POINT_EDITION_PRIORITY("Changed the Priority\n"),
    POINT_EDITION_PIN_MOVE("Changed the Location of a point\n"),
    POINT_EDITION_ADDITIONAL_PINS_MOVE("Changed the Location of a point\n"),
    POINT_EDITION_PINS_MOVE("Changed the Location of a point\n"),
    POINT_EDITION_POLYGONS_MOVE("Changed the Location of a point\n"),
    POINT_EDITION_ATTACHMENT("Edited the Attachments\n"),
    POINT_EDITION_VIDEOS("Edited the Videos\n"),
    POINT_EDITION_IMAGES("Edited the Images\n"),
    POINT_EDITION_DOCUMENTS("Edited the Files\n"),
    POINT_EDITION_IMAGES360("Edited the Images360\n"),
    POINT_EDITION_TAGS("Changed the Tags\n"),
    POINT_EDITION_CUSTOM_FIELDS("Changed the Custom Fields\n"),
    POINT_EDITION_ASSIGNEE("Changed the Assignees\n"),
    POINT_EDITION_DESCRIPTION("Changed the Description\n"),
    POINT_EDITION_COMMENT("Commented on a point\n"),
    POINT_MENTION_DESCRIPTION("Mentioned you in the Description\n"),
    POINT_MENTION_COMMENT("Mentioned you in the Comment\n"),
    POINT_MENTION_CUSTOM_FIELD("Mentioned you in the Rich Text Custom Field\n"),
    POINT_MENTION_NEW_POINT("Mentioned you on a point\n"),
    REACTION_COMMENT("Liked your comment\n"),
    POINT_EDITION_FLAGGED("Edited the Flagged\n"),
    REMINDER_CREATED_FOR("REMINDER_CREATED_FOR"),
    REMINDER_DELETED_FOR("REMINDER_DELETED_FOR"),
    REMINDER_EDITED_FOR("REMINDER_EDITED_FOR");
}
