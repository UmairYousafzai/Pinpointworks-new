package com.sleetworks.serenity.android.newone.utils

import androidx.navigation.NavController
import kotlinx.coroutines.flow.StateFlow

inline fun <reified T> NavController.resultFlow(key: String, initial: T): StateFlow<T> =
    requireNotNull(currentBackStackEntry).savedStateHandle.getStateFlow(key, initial)

inline fun <reified T> NavController.setResult(key: String, value: T) {
    previousBackStackEntry?.savedStateHandle?.set(key, value)
}

fun NavController.clearResult(key: String) {
    currentBackStackEntry?.savedStateHandle?.remove<Any?>(key)
}
