package com.sleetworks.serenity.android.newone.utils

import android.util.Base64
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sleetworks.serenity.android.newone.data.models.remote.response.ApiResponse
import com.sleetworks.serenity.android.newone.domain.models.textRich.AttributesRich
import com.sleetworks.serenity.android.newone.domain.models.textRich.TextRichContent
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern

data class TextRichOptions(
    var insert: Any? = null,
    var attributes: AttributesRich? = null
) {
    companion object {
        fun decodeJSONValue(descriptionRichJSON: String): List<TextRichOptions> {
            val decodedJSONValue = mutableListOf<TextRichOptions>()
            try {
                val responseType = object : TypeToken<ApiResponse<Map<String, String>>>() {}.type
                val descriptionRichList =
                    Gson().fromJson<ApiResponse<Map<String, List<TextRichOptions>>>>(
                        descriptionRichJSON,
                        responseType
                    )
                descriptionRichList.ops?.let { decodedJSONValue.addAll(it) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return decodedJSONValue
        }

        fun convertBase64ToRichContentString(base64Value: String = "eyJvcHMiOlt7Imluc2VydCI6IlRlc3RpbmcgdGhlIHBvaW50ICJ9LHsiYXR0cmlidXRlcyI6eyJib2xkIjp0cnVlfSwiaW5zZXJ0IjoieW91ciB0ZXN0ICJ9LHsiaW5zZXJ0Ijp7Im1lbnRpb24iOnsiaW5kZXgiOiI5IiwiZGVub3RhdGlvbkNoYXIiOiJAIiwiaWQiOiI2OGQ2NjA1ZjY3NGNlNzcwNmI4YmQyODgiLCJ2YWx1ZSI6ImFtamFkMTU5In19fSx7Imluc2VydCI6IiBcbiJ9XX0="): String {
            return try {
                val data = Base64.decode(base64Value, Base64.DEFAULT)
                val customFieldRichJSON = String(data, StandardCharsets.UTF_8)
                val textRichOptions = decodeJSONValue(customFieldRichJSON)
                TextRichContent.getRichValue(textRichOptions)
            } catch (e: Exception) {
                ""
            }
        }

        fun convertBase64ToRichContentStringPlainText(base64Value: String): String {
            return try {
                val data = Base64.decode(base64Value, Base64.DEFAULT)
                val customFieldRichJSON = String(data, StandardCharsets.UTF_8)
                val textRichOptions = decodeJSONValue(customFieldRichJSON)
                removeHtmlTags(TextRichContent.getRichValue(textRichOptions))
            } catch (e: Exception) {
                ""
            }
        }

        fun removeHtmlTags(valWithTags: String): String {
            return try {
                val pattern = Pattern.compile("<[^>]*>")
                val matcher = pattern.matcher(valWithTags)
                matcher.replaceAll("")
            } catch (e: Exception) {
                ""
            }
        }

        fun encodeJSONToBase64(jsonValue: String): String {
            val base64 = Base64.encodeToString(jsonValue.toByteArray(), Base64.DEFAULT)
            return base64.replace("\n", "")
        }
    }
}