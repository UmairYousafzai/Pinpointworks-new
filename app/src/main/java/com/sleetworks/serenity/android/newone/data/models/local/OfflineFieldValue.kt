package com.sleetworks.serenity.android.newone.data.models.local

sealed class OfflineFieldValue {
    data class StringValue(val value: String) : OfflineFieldValue()
    data class BooleanValue(val value: Boolean) : OfflineFieldValue()
    data class StringListValue(val value: List<String>) : OfflineFieldValue()
    data class IntValue(val value: Int) : OfflineFieldValue()
    data class DoubleValue(val value: Double) : OfflineFieldValue()
    
    fun getValue(): Any = when (this) {
        is StringValue -> value
        is BooleanValue -> value
        is StringListValue -> value
        is IntValue -> value
        is DoubleValue -> value
    }
}