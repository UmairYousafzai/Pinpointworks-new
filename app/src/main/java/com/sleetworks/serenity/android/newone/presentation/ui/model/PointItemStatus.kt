package com.sleetworks.serenity.android.newone.presentation.ui.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.sleetworks.serenity.android.newone.R
import com.sleetworks.serenity.android.newone.ui.theme.CornflowerBlue
import com.sleetworks.serenity.android.newone.ui.theme.DarkSalmonPink
import com.sleetworks.serenity.android.newone.ui.theme.Goldenrod

sealed class PointItemStatus(val label: String, @DrawableRes val icon: Int) {

    object Open : PointItemStatus("Open", R.drawable.ic_open_light)
    object InProgress : PointItemStatus("In Progress", R.drawable.ic_in_progress)
    object ToReview : PointItemStatus("To Review", R.drawable.ic_in_to_review)
    object OnHold : PointItemStatus("On Hold", R.drawable.ic_hold_light)
    object Completed : PointItemStatus("Completed", R.drawable.ic_tick_light)
    object Canceled : PointItemStatus("Canceled", R.drawable.ic_in_canceled)

    companion object {
        fun from(status: String): PointItemStatus {
            return when (status.lowercase()) {
                "open" -> Open
                "in_progress" -> InProgress
                "to_review" -> ToReview
                "on_hold" -> OnHold
                "completed" -> Completed
                "canceled" -> Canceled
                else -> Canceled
            }

        }
    }
}