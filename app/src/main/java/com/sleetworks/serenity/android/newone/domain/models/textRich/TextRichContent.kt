package com.sleetworks.serenity.android.newone.domain.models.textRich

import com.google.gson.internal.LinkedTreeMap
import com.sleetworks.serenity.android.newone.utils.TextRichOptions



data class TextRichContent(
    var listContent: Boolean = false,
    var containsBreak: Boolean = false,
    var richContentValue: String? = null
) {
    companion object {
        fun getBreakingId(startId: Int, richContent: List<TextRichContent>): Int {
            var currentId = startId
            while (currentId < richContent.size) {
                if (richContent[currentId].containsBreak) {
                    return currentId
                }
                currentId++
            }
            return currentId - 1
        }

        fun getRichValue(textRichOptions: List<TextRichOptions>): String {
            val richContent = getRichContentList(textRichOptions)
            val descriptionRichValue = StringBuilder()
            var startId = 0
            var listRow = 1

            while (startId < richContent.size) {
                val breakLineId = getBreakingId(startId, richContent)
                val endLineFragment = richContent[breakLineId]

                if (!endLineFragment.listContent) {
                    while (startId <= breakLineId) {
                        val fragment = richContent[startId++]
                        descriptionRichValue.append(fragment.richContentValue)
                    }
                } else {
                    val currentListType = endLineFragment.richContentValue
                    val listElement = if (currentListType == "bullet") "\t\t\u2022 \t" else "\t\t$listRow. \t"

                    val index = descriptionRichValue.lastIndexOf("<br>")
                    if (listRow == 1 && index >= 0) {
                        descriptionRichValue.insert(index + 4, listElement)
                    } else {
                        descriptionRichValue.append(listElement)
                    }

                    val nextListTypeId = getBreakingId(breakLineId + 1, richContent)
                    val nextListType = richContent.getOrNull(nextListTypeId)?.richContentValue
                    if (currentListType == nextListType) {
                        listRow++
                    } else {
                        listRow = 1
                    }

                    while (startId < breakLineId) {
                        val fragment = richContent[startId++]
                        descriptionRichValue.append(fragment.richContentValue)
                    }

                    descriptionRichValue.append("<br>")
                }
                startId = breakLineId + 1
            }

            return descriptionRichValue.toString()
        }

        private fun getRichContentList(textRichOptions: List<TextRichOptions>): List<TextRichContent> {
            val textRichContent = mutableListOf<TextRichContent>()
            for (richOption in textRichOptions) {
                var isLink = false
                val richContentValue = StringBuilder()
                val attributes = richOption.attributes

                if (attributes?.list != null) {
                    richContentValue.append(attributes.list)
                    textRichContent.add(TextRichContent(true, true, richContentValue.toString()))
                    continue
                }

                attributes?.let {
                    if (it.bold) richContentValue.append("<b>")
                    if (it.italic) richContentValue.append("<i>")
                    if (it.underline) richContentValue.append("<u>")
                    if (it.strike) richContentValue.append("<del>")
                    if (it.link != null) isLink = true
                }

                var containsBreak = false
                when (val insert = richOption.insert) {
                    is String -> {
                        containsBreak = insert.contains("\n")
                        if (isLink) {
                            richContentValue
                                .append("<a href='").append(attributes?.link).append("'>")
                                .append(insert.replace("\n", "<br>"))
                                .append("</a>")
                        } else {
                            richContentValue.append(insert.replace("\n", "<br>"))
                        }
                    }
                    is LinkedTreeMap<*, *> -> {
                        try {
                            val mentionMap = insert["mention"] as? LinkedTreeMap<*, *>
                            if (mentionMap != null && mentionMap["denotationChar"] != null && mentionMap["value"] != null) {
                                richContentValue
                                    .append(" <a href='#'>")
                                    .append(mentionMap["denotationChar"])
                                    .append(mentionMap["value"])
                                    .append("</a> ")
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    is Map<*, *> -> {
                        try {
                            val mentionMap = insert["mention"] as? Map<String, Any>
                            if (mentionMap != null && mentionMap["denotationChar"] != null && mentionMap["value"] != null) {
                                richContentValue
                                    .append(" <a href='#'>")
                                    .append(mentionMap["denotationChar"])
                                    .append(mentionMap["value"])
                                    .append("</a> ")
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                attributes?.let {
                    if (it.strike) richContentValue.append("</del>")
                    if (it.underline) richContentValue.append("</u>")
                    if (it.italic) richContentValue.append("</i>")
                    if (it.bold) richContentValue.append("</b>")
                }

                textRichContent.add(TextRichContent(false, containsBreak, richContentValue.toString()))
            }

            return textRichContent
        }
    }
}