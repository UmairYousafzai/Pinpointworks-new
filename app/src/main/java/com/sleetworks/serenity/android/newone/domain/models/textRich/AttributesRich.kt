package com.sleetworks.serenity.android.newone.domain.models.textRich

data class AttributesRich(
    var italic: Boolean = false,
    var bold: Boolean = false,
    var underline: Boolean = false,
    var strike: Boolean = false,
    var link: String? = null,
    var list: String? = null
)