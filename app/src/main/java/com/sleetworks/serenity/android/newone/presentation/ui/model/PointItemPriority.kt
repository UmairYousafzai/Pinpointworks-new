package com.sleetworks.serenity.android.newone.presentation.ui.model

import androidx.compose.ui.graphics.Color
import com.sleetworks.serenity.android.newone.ui.theme.CornflowerBlue
import com.sleetworks.serenity.android.newone.ui.theme.DarkSalmonPink
import com.sleetworks.serenity.android.newone.ui.theme.Goldenrod

sealed class PointItemPriority(val label: String,val  color: Color) {

    object High : PointItemPriority("High", DarkSalmonPink)
    object Medium : PointItemPriority("Medium", Goldenrod)
    object Low : PointItemPriority("Low", CornflowerBlue)

    companion object {
        fun from(label: String): PointItemPriority {
            return when (label.lowercase()) {
                "high" -> High
                "medium" -> Medium
                "low" -> Low
                else -> Low
            }

        }

    }
}