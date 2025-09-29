package com.sleetworks.serenity.android.newone.data.models.remote.response.auth
import com.google.gson.annotations.SerializedName
import com.sleetworks.serenity.android.newone.data.models.remote.response.BaseResponse
import java.io.Serializable


data class User(
    @SerializedName("_id")
    var id: String ,
    var activeWorkspaceRef: ActiveWorkspaceRef,
    var email: String,
    var enabled: Boolean,
    var enabled2fa: Boolean,
    var images: ArrayList<Image>,
    var lastActivityEpochMillis: Long,
    var name: String,
    var notificationStatus: NotificationStatus,
    var preferenceDocRef: PreferenceDocRef,
    var setup2faAfter: Long,
    var timeZoneId: String,
    var type: String,
    var userType: String,
    var verified: Boolean,
    var passwordHash: String,
    val tags: ArrayList<String>?,

    ): Serializable, BaseResponse()
