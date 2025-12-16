package com.sleetworks.serenity.android.newone.data.models.local

import com.sleetworks.serenity.android.newone.data.models.remote.response.comment.Reaction

sealed class OfflineFieldValue {
    data class StringValue(val value: String) : OfflineFieldValue()
    data class BooleanValue(val value: Boolean) : OfflineFieldValue()
    data class StringListValue(val value: List<String>) : OfflineFieldValue()
    data class IntValue(val value: Int) : OfflineFieldValue()
    data class DoubleValue(val value: Double) : OfflineFieldValue()
    data class NewCustomFieldValue(val value: List<NewCustomField>) : OfflineFieldValue()
    data class CommentReactionValue(val value: Reaction) : OfflineFieldValue()

    fun getValue(): Any = when (this) {
        is StringValue -> value
        is BooleanValue -> value
        is StringListValue -> value
        is IntValue -> value
        is DoubleValue -> value
        is NewCustomFieldValue -> value
        is CommentReactionValue -> value
    }
}