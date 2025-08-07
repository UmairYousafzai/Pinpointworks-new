package com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.site

data class SitePlan(
    val extent: List<Double> ,
    val maxZoom: Int,
    val minZoom: Int,
    val pixelSize: Double,
    val plantilerVersion: Int,
    val resolutions: List<Double>,
    val sitePlanURL: String,
    val version: Int
)