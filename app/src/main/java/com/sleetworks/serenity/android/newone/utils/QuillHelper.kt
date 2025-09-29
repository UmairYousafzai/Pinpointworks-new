package com.sleetworks.serenity.android.newone.utils

import android.content.Context
import android.util.Base64
import com.sleetworks.serenity.android.newone.data.models.remote.response.Assignee
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class QuillRichTextHelper {

    fun getQuillTextEditHtml(
        fieldType: String,
        value: String?,
        users: List<Assignee>,
        context: Context
    ): String {
        // Read the base HTML template from assets
        val baseHtml = readAssetFile(context, "QuillInit.html")
        val sb = StringBuilder(baseHtml)

        // Handle initial value
        val quoted = if (!value.isNullOrEmpty()) {
            try {
                val decodedValue = String(Base64.decode(value, Base64.DEFAULT), Charsets.UTF_8)
                JSONObject.quote(decodedValue)
            } catch (e: Exception) {
                JSONObject.quote(value)
            }
        } else {
            "''"
        }

        // Set text limit based on field type
        val textLimit =
            if (fieldType.lowercase() == "description") {
                10000
            } else {
                5000
            }

        sb.append("let textLimit = $textLimit; \n")
        sb.append("const jsonValue = $quoted;\n")

        // Build user list
        sb.append("let userList = [\n")
        users.forEach { user ->
            sb.append("{\n")
            sb.append("id:\"${user.id}\",\n")
            sb.append("value:\"${user.caption}\",\n")
            sb.append("avatar:\"${user.primaryImageId ?: ""}\",\n")
            sb.append("email:\"${user.email}\",\n")
            sb.append("},\n")
        }
        sb.append("];\n")

        // Set initial content if exists
        if (quoted.length > 2) {
            sb.append("const richTextComponent = JSON.parse(jsonValue);\n")
            sb.append("quill.setContents(richTextComponent.ops);\n")
        }
        sb.append("checkExistingMentions();\n")
        sb.append("</script> </body> </html>")

        return sb.toString()
    }

    fun readFromFile(fileName: String, context: Context): String {
        val returnString = StringBuilder()
        try {
            context.assets.open(fileName).use { fIn ->
                InputStreamReader(fIn).use { isr ->
                    BufferedReader(isr).use { input ->
                        var line: String?
                        while (input.readLine().also { line = it } != null) {
                            returnString.append(line).append("\n")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace() // better than just calling getMessage()
        }
        return returnString.toString()
    }

    private fun readAssetFile(context: Context, fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }
}