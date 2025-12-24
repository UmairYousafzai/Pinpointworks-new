package com.sleetworks.serenity.android.newone.utils

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.sleetworks.serenity.android.newone.data.models.local.NewCustomField
import com.sleetworks.serenity.android.newone.data.models.local.OfflineFieldValue
import com.sleetworks.serenity.android.newone.data.models.remote.response.comment.Reaction
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Video
import com.sleetworks.serenity.android.newone.domain.models.CommentDomain
import com.sleetworks.serenity.android.newone.presentation.model.LocalImage
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject
import java.io.File
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
    val str = replace("^0+".toRegex(), "")
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

fun Any.convertToOfflineFieldValue(): OfflineFieldValue {
    return when (this) {
        is Reaction -> OfflineFieldValue.CommentReactionValue(this)
        is CommentDomain -> OfflineFieldValue.CommentValue(this)
        is NewCustomField -> OfflineFieldValue.NewCustomFieldValue(value as List<NewCustomField>)
        is String -> OfflineFieldValue.StringValue(this)
        is Boolean -> OfflineFieldValue.BooleanValue(this)
        is List<*> -> {
            if (this.isNotEmpty() && this.first() is String) {
                OfflineFieldValue.StringListValue(this as List<String>)
            } else if (this.isNotEmpty() && this.first() is NewCustomField) {
                OfflineFieldValue.NewCustomFieldValue(this as List<NewCustomField>)
            } else if (this.isNotEmpty() && this.first() is LocalImage) {
                OfflineFieldValue.ImageListValue(this as List<LocalImage>)
            } else if (this.isNotEmpty() && this.first() is Video) {
                OfflineFieldValue.VideoListValue(this as List<Video>)
            } else {
                OfflineFieldValue.StringListValue(this.map { it.toString() })
            }
        }

        is Set<*> -> {
            if (this.isNotEmpty() && this.first() is String) {
                OfflineFieldValue.StringListValue(this.toList() as List<String>)
            } else {
                OfflineFieldValue.StringListValue(this.map { it.toString() })
            }
        }

        is Int -> OfflineFieldValue.IntValue(this)
        is Double -> OfflineFieldValue.DoubleValue(this)
        else -> OfflineFieldValue.StringValue(this.toString())
    }
}

fun Context.requestStoragePermission(
    onPermissionGranted: () -> Unit,
    onRequestPermission: (String) -> Unit
) {
    // For Android 13+ (API 33+), PickVisualMedia doesn't require permissions
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        onPermissionGranted()
    } else {
        // For Android 12 and below, we need READ_EXTERNAL_STORAGE permission
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(this, permission) ==
            android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            onPermissionGranted()
        } else {
            onRequestPermission(permission)
        }
    }
}

fun Context.requestCameraPermission(
    onPermissionGranted: () -> Unit,
    onRequestPermission: (String) -> Unit
) {
    val permission = Manifest.permission.CAMERA
    if (ContextCompat.checkSelfPermission(this, permission) ==
        android.content.pm.PackageManager.PERMISSION_GRANTED
    ) {
        onPermissionGranted()
    } else {
        onRequestPermission(permission)
    }
}

fun Context.requestVideoPermissions(
    onPermissionsGranted: () -> Unit,
    onRequestPermissions: (Array<String>) -> Unit
) {
    val cameraPermission = Manifest.permission.CAMERA
    val audioPermission = Manifest.permission.RECORD_AUDIO

    val cameraGranted = ContextCompat.checkSelfPermission(
        this,
        cameraPermission
    ) == android.content.pm.PackageManager.PERMISSION_GRANTED

    val audioGranted = ContextCompat.checkSelfPermission(
        this,
        audioPermission
    ) == android.content.pm.PackageManager.PERMISSION_GRANTED

    if (cameraGranted && audioGranted) {
        // Both permissions already granted
        onPermissionsGranted()
    } else {
        // Request missing permissions
        val permissionsToRequest = mutableListOf<String>()
        if (!cameraGranted) {
            permissionsToRequest.add(cameraPermission)
        }
        if (!audioGranted) {
            permissionsToRequest.add(audioPermission)
        }
        onRequestPermissions(permissionsToRequest.toTypedArray())
    }
}

fun Context.createCameraImageFile(saveToExternalStorage: Boolean = false): Uri? {
    return try {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"

        if (saveToExternalStorage) {
            // Save to public external storage (visible in gallery)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ (API 29+) - Use MediaStore
                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, "${imageFileName}.jpg")
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            } else {
                // Android 9 and below - Use MediaStore (works for all versions)
                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, "${imageFileName}.jpg")
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                }
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            }
        } else {
            // Save to app-specific external storage
            val storageDir = getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES)
            val imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
            FileProvider.getUriForFile(
                this,
                "${packageName}.fileprovider",
                imageFile
            )
        }
    } catch (e: Exception) {
        null
    }
}

fun Context.createCameraVideoFile(saveToExternalStorage: Boolean = false): Uri? {
    return try {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val videoFileName = "VIDEO_${timeStamp}_"

        if (saveToExternalStorage) {
            // Save to public external storage (visible in gallery)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ (API 29+) - Use MediaStore
                val contentValues = ContentValues().apply {
                    put(MediaStore.Video.Media.DISPLAY_NAME, "${videoFileName}.mp4")
                    put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
                    put(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_MOVIES)
                }
                contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues)
            } else {
                // Android 9 and below - Use MediaStore (works for all versions)
                val contentValues = ContentValues().apply {
                    put(MediaStore.Video.Media.DISPLAY_NAME, "${videoFileName}.mp4")
                    put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
                }
                contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues)
            }
        } else {
            // Save to app-specific external storage
            val storageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES)
            val videoFile = File.createTempFile(videoFileName, ".mp4", storageDir)
            FileProvider.getUriForFile(
                this,
                "${packageName}.fileprovider",
                videoFile
            )
        }
    } catch (e: Exception) {
        null
    }
}

fun File.readExifDataAsJson(): String {
    return try {
        val exif = androidx.exifinterface.media.ExifInterface(this)

        // Read DateTimeOriginal from EXIF
        val dateTimeOriginal = exif.getAttribute(
            androidx.exifinterface.media.ExifInterface.TAG_DATETIME_ORIGINAL
        ) ?: exif.getAttribute(
            androidx.exifinterface.media.ExifInterface.TAG_DATETIME
        )

        if (dateTimeOriginal != null) {// Create JSON object with the required format
            val exifJson = JSONObject().apply {
                if (dateTimeOriginal.isNotEmpty()) {
                    put("DateTimeOriginal", dateTimeOriginal)
                } else {
                    put("DateTimeOriginal", "")
                }
                put("isFormatDate", 0)
            }

            exifJson.toString()
        } else {
            ""
        }
    } catch (e: Exception) {
        Log.e("readExifDataAsJson", "Error reading EXIF data: ${e.message}", e)
        // Return default format if EXIF reading fails
        JSONObject().apply {
            put("DateTimeOriginal", "")
            put("isFormatDate", 0)
        }.toString()
    }
}