package com.sleetworks.serenity.android.newone.presentation.common


sealed class UIEvent {
    data class Navigate(val route: String) : UIEvent()
    object PopBackStack : UIEvent()
    data class ShowSnackbar(val message: String) : UIEvent()
}
