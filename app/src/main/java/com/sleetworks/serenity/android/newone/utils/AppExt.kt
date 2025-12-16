package com.sleetworks.serenity.android.newone.utils

import androidx.navigation.NavController
import kotlinx.coroutines.flow.StateFlow
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

inline fun <reified T> NavController.resultFlow(key: String, initial: T): StateFlow<T> =
    requireNotNull(currentBackStackEntry).savedStateHandle.getStateFlow(key, initial)

inline fun <reified T> NavController.setResult(key: String, value: T) {
    previousBackStackEntry?.savedStateHandle?.set(key, value)
}

fun NavController.clearResult(key: String) {
    currentBackStackEntry?.savedStateHandle?.remove<Any?>(key)
}

fun String.convertTimeMilisToDisplayValue(showHoursOnly: Boolean): String {
    if (isEmpty()) return "0"
    val timeInMinutes = this.toLong() / 60000
    val days = (timeInMinutes / 60 / 24).toInt()
    var hours = ((timeInMinutes - days * 60 * 24) / 60).toInt()
    val minutes = (timeInMinutes - days * 60 * 24 - hours * 60).toInt()

    val timeValue = StringBuilder()
    timeValue.append("Total ")

    if (!showHoursOnly) {
        timeValue.append("$days d / ")
    }

    if (showHoursOnly) {
        hours = (timeInMinutes / 60).toInt()
    }

    if (hours < 10) timeValue.append(0)
    timeValue.append(hours).append(":")

    if (minutes < 10) timeValue.append(0)
    timeValue.append(minutes)

    return timeValue.toString()
}

fun Double.formulaCFOutputPercentageValueFormat(): String {
    // Create a pattern for decimal formatting
    val pattern = "###.#"

    val decimalFormat = DecimalFormat(pattern)

    return decimalFormat.format(this)
}

fun Double.formulaCFOutputCostValueFormat(): String {
    // Create a pattern for decimal formatting
    val pattern = "###.00"

    val decimalFormat = DecimalFormat(pattern)

    return decimalFormat.format(this)
}

fun Double.formatFormulaCfOutputNumber(
    decimalPlaces: Int = 0,
    isCommas: Boolean = false
): String {
    val pattern = buildString {
        if (isCommas) {
            append("#,###")
        } else {
            append("###")
        }

        if (decimalPlaces > 0) {
            append(".")
            repeat(decimalPlaces) { append("0") }
        }
    }
    val finalPattern = pattern.ifEmpty { "0" }

    return java.text.DecimalFormat(finalPattern).format(this)
}

fun String.validateDecimalInput(maxDigits: Int): String {
    if (isEmpty()) return this

    // Allow only numbers and dot
    if (!matches(Regex("^\\d*\\.?\\d*\$"))) return dropLast(1)

    // Restrict decimal digits
    if (contains(".")) {
        val parts = split(".")
        val decimals = parts.getOrNull(1) ?: ""

        if (decimals.length > maxDigits) {
            return dropLast(1)
        }
    }

    return this
}

fun String.removeLeadingZeros(): String {
    val str =replace("^0+".toRegex(), "")
    return str.ifEmpty { "" }
}

fun String.parseDateToMillis(): Long? {
    return try {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .parse(this)?.time
    } catch (e: Exception) {
        null
    }
}

fun <T> List<T>.chunkedBy(size: Int): List<List<T>> {
    return this.chunked(size)
}
fun Long.formatCommentDateTime(): String {

    val date = Date(this)
    val dateFormat = SimpleDateFormat("dd MMM yyyy 'at' HH:mm", Locale.getDefault())
    return dateFormat.format(date)
}
