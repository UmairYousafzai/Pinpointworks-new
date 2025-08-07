package com.sleetworks.serenity.android.newone

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.sleetworks.serenity.android.newone.presentation.navigation.Navigation
import com.sleetworks.serenity.android.newone.presentation.viewmodels.DataStoreViewModel
import com.sleetworks.serenity.android.newone.presentation.viewmodels.SharedViewModel
import com.sleetworks.serenity.android.newone.ui.theme.PinpointworksNewTheme
import dagger.hilt.android.AndroidEntryPoint

const val TAG="MainActivity"
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val dataStoreViewModel: DataStoreViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition {

           dataStoreViewModel.isLoggedIn.value!=null
        }

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            PinpointworksNewTheme {
                val isLoggedIn = dataStoreViewModel.isLoggedIn.collectAsState()
                if (isLoggedIn.value != null) {
                    PinpointApp(sharedViewModel,isLoggedIn=isLoggedIn.value == true)
                }
            }
        }
    }

}


@Composable
fun PinpointApp(sharedViewModel: SharedViewModel , isLoggedIn: Boolean) {
    Log.e(TAG, "PinpointApp: isLoggedIn $isLoggedIn", )
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        sharedViewModel.snackbarFlow.collect { message ->
            Log.e(TAG, "PinpointApp: snackbar $message", )
            snackbarHostState.showSnackbar(message)
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Navigation(navController,isLoggedIn,sharedViewModel)
        }


    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PinpointworksNewTheme {
//        Navigation()
    }
}