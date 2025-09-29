package com.sleetworks.serenity.android.newone.presentation.navigation


sealed class Screen(val route: String) {
    object LoginScreen : Screen("login")
    object SyncScreen : Screen("first_sync")
    object DefectListScreen : Screen("defect_list")
    object DefectDetailScreen : Screen("defect_detail")
    object RichTextEditorScreen : Screen("Rich_text_editor_screen")
//    object MovieDetail : Screen("movie_detail/{movieId}") {
//        fun createRoute(movieId: Int): String = "movie_detail/$movieId"
//    }
}
