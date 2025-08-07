package com.sleetworks.serenity.android.newone.data.models.remote.response.workspace

import java.io.Serializable

data class AccountFeatures(
    val aalExport: Boolean,
    val aalSitePlan: Boolean,
    val fleetManagement: Boolean,
    val pdfSignatures: Boolean,
    val pdfYcoLayout: Boolean,
    val timeline: Boolean
): Serializable