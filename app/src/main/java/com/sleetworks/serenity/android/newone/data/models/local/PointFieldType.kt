package com.sleetworks.serenity.android.newone.data.models.local

enum class PointFieldType(val value: String) {
    TITLE("title"),
    DESCRIPTION("description"),
    DESCRIPTIONRICH("descriptionRich"),
    PRIORITY("priority"),
    STATUS("status"),
    ASSIGNEES("assignees"),
    TAGS("tags"),
    RED_FLAG("flagged"),
    PIN("pin"),
    PINS("pins"),
    POLYGONS("polygons"),
    CUSTOM_FIELDS("customFieldsList"),
    IMAGES("images"),
    DELETED_IMAGES("deletedImages"),
    DELETED_VIDEOS("deletedVideos"),
    ANNOTATED_IMAGES("annotatedImages"),
    COMMENT("comment"),
    MENTIONS("mentions")
}
