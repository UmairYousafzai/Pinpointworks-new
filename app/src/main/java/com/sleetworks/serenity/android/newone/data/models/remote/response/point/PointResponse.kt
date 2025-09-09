package com.sleetworks.serenity.android.newone.data.models.remote.response.point

import java.io.Serializable

data class PointResponse(
    val points: List<Point>,
    val removedPointsIds: List<String>,
    val serverTime: Long
): Serializable