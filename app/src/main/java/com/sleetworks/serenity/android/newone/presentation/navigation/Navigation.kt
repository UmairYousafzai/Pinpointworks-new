package com.sleetworks.serenity.android.newone.presentation.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sleetworks.serenity.android.newone.presentation.ui.screens.auth.LoginScreen
import com.sleetworks.serenity.android.newone.presentation.ui.screens.auth.SyncScreen
import com.sleetworks.serenity.android.newone.presentation.ui.screens.defectList.DefectListScreen
import com.sleetworks.serenity.android.newone.presentation.viewmodels.SharedViewModel

@Composable
fun Navigation(
    navController: NavHostController,
    isLoggedIn: Boolean,
    sharedViewModel: SharedViewModel
) {
    NavHost(
        navController,
        startDestination = if (!isLoggedIn) Screen.LoginScreen.route else Screen.SyncScreen.route
    ) {
        composable(Screen.LoginScreen.route) {
            LoginScreen(navController, sharedViewModel = sharedViewModel)
        }
        composable(
            route = Screen.SyncScreen.route,
        ) { backStackEntry ->
            SyncScreen(navController, sharedViewModel = sharedViewModel)
        }
        composable(Screen.DefectListScreen.route) {
            DefectListScreen()
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
