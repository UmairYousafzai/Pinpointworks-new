package com.sleetworks.serenity.android.newone.presentation.ui.model

enum class CustomFieldType(val value: String) {
    TEXT("TEXT"),
    DATE("DATE"),
    COST("COST"),
    LIST("LIST"),
    NUMBERS("NUMBERS"),
    RICHTEXT("RICHTEXT"),
    TIME("TIME"),
    PERCENTAGE("PERCENTAGE"),
    CHECKBOX("CHECKBOX"),
    TIMELINE("TIMELINE"),
    FORMULA("FORMULA"),
    MULTI_LIST("MULTI_LIST");

    companion object {
        fun fromValue(value: String): CustomFieldType? {
            return entries.find { it.value == value }
        }
    }
}
