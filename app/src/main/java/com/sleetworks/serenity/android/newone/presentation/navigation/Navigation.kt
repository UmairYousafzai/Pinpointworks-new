package com.sleetworks.serenity.android.newone.presentation.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sleetworks.serenity.android.newone.presentation.ui.screens.auth.LoginScreen
import com.sleetworks.serenity.android.newone.presentation.ui.screens.auth.SyncScreen
import com.sleetworks.serenity.android.newone.presentation.ui.screens.defectList.DefectListScreen
import com.sleetworks.serenity.android.newone.presentation.viewmodels.SharedViewModel

@Composable
fun Navigation(
    navController: NavHostController,
    isLoggedIn: Boolean,
    isFirstSync: Boolean,
    sharedViewModel: SharedViewModel
) {
    var startDestination = Screen.LoginScreen.route
    if (isFirstSync) {
        startDestination = Screen.DefectListScreen.route+"/"+false
    } else if (isLoggedIn) {
        startDestination = Screen.SyncScreen.route
    }
    NavHost(
        navController,
//        startDestination = Screen.DefectListScreen.route
        startDestination = startDestination
    ) {
        composable(Screen.LoginScreen.route) {
            LoginScreen(navController, sharedViewModel = sharedViewModel)
        }
        composable(
            route = Screen.SyncScreen.route,
        ) { backStackEntry ->
            SyncScreen(navController, sharedViewModel = sharedViewModel)
        }
        composable(
            Screen.DefectListScreen.route+"/{shouldSyncPoint}",
            arguments = listOf(navArgument("shouldSyncPoint") { type = NavType.BoolType })
        ) { backStackEntry ->
            DefectListScreen(navController, sharedViewModel)
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
