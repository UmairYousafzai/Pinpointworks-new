package com.sleetworks.serenity.android.newone.data.models.remote.response.updatedPoint

import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Point
import java.io.Serializable

data class UpdatedPoint(
    var updatedPoint: Point? = null,
    var correctlyUpdatedFields: Set<String>? = null,
    var incorrectlyUpdatedFields: Map<String, String>? = null
) : Serializable