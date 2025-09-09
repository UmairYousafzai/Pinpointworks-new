package com.sleetworks.serenity.android.newone.data.models.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.sleetworks.serenity.android.newone.data.models.remote.response.Header
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.ActiveWorkspaceRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.Image
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.NotificationStatus
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.PreferenceDocRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.User

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    val id: String,
    @Embedded
    val notificationStatus: NotificationStatus,
    val activeWorkspaceRef: ActiveWorkspaceRef,
    val preferenceDocRef: PreferenceDocRef,
    val header: Header?,
    val tags: ArrayList<String>,
    val email: String,
    val enabled: Boolean,
    val enabled2fa: Boolean,
    val images: ArrayList<Image>,
    val lastActivityEpochMillis: Long,
    val name: String,
    val setup2faAfter: Long,
    val timeZoneId: String,
    val type: String,
    val userType: String,
    val verified: Boolean,
    val passwordHash: String
)
