package com.sleetworks.serenity.android.newone.data.models.local

import com.sleetworks.serenity.android.newone.data.models.remote.response.comment.Reaction
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Video
import com.sleetworks.serenity.android.newone.domain.models.CommentDomain
import com.sleetworks.serenity.android.newone.presentation.model.LocalImage

sealed class OfflineFieldValue {
    data class StringValue(val value: String) : OfflineFieldValue()
    data class BooleanValue(val value: Boolean) : OfflineFieldValue()
    data class StringListValue(val value: List<String>) : OfflineFieldValue()
    data class IntValue(val value: Int) : OfflineFieldValue()
    data class DoubleValue(val value: Double) : OfflineFieldValue()
    data class NewCustomFieldValue(val value: List<NewCustomField>) : OfflineFieldValue()
    data class CommentReactionValue(val value: Reaction) : OfflineFieldValue()
    data class CommentValue(val value: CommentDomain) : OfflineFieldValue()
    data class ImageListValue(val value: List<LocalImage>) : OfflineFieldValue()
    data class VideoListValue(val value: List<Video>) : OfflineFieldValue()

    fun getValue(): Any = when (this) {
        is StringValue -> value
        is BooleanValue -> value
        is StringListValue -> value
        is IntValue -> value
        is DoubleValue -> value
        is NewCustomFieldValue -> value
        is CommentReactionValue -> value
        is CommentValue -> value
        is ImageListValue -> value
        is VideoListValue -> value
    }
}