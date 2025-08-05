package com.sleetworks.serenity.android.newone.data.models.remote.response.workspace

data class Entity(
    val accountFeatures: AccountFeatures,
    val id: String,
    val name: String,
    val order: Int,
    val workspaces: List<Workspace>
)