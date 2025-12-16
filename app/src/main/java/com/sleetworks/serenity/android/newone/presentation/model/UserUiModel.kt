package com.sleetworks.serenity.android.newone.presentation.model

import java.io.File

data class UserUiModel(
    val id:String="",
    val username: String="",
    val isLogin: Boolean= false,
    val email: String="",
    val password: String="",
    val imageID: String="",
    val imageFile: File? =null,
)
{

}
