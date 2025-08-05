package com.sleetworks.serenity.android.newone.presentation.navigation


sealed class Screen(val route: String) {
    object LoginScreen : Screen("login")
    object SyncScreen : Screen("first_sync")
//    object MovieDetail : Screen("movie_detail/{movieId}") {
//        fun createRoute(movieId: Int): String = "movie_detail/$movieId"
//    }
}
