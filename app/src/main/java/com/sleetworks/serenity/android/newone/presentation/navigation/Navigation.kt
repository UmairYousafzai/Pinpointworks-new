package com.sleetworks.serenity.android.newone.presentation.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.sleetworks.serenity.android.newone.presentation.ui.screens.auth.LoginScreen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.LoginScreen.route) {
        composable(Screen.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(
            route = Screen.SyncScreen.route,
        ) { backStackEntry ->
        }

//        composable(
//            route = Screen.SyncScreen.route,
//            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
//        ) { backStackEntry ->
//            val movieId = backStackEntry.arguments?.getInt("movieId") ?: return@composable
//            MovieDetail(movieId)
//        }
    }
}
