package com.sleetworks.serenity.android.newone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.sleetworks.serenity.android.newone.presentation.ui.screens.auth.LoginScreen
import com.sleetworks.serenity.android.newone.presentation.ui.screens.auth.SyncScreen
import com.sleetworks.serenity.android.newone.presentation.viewmodels.SharedViewModel
import com.sleetworks.serenity.android.newone.ui.theme.PinpointworksNewTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            PinpointworksNewTheme {
                PinpointApp()
            }
        }
    }

}


@Composable
fun PinpointApp(sharedViewModel: SharedViewModel = hiltViewModel()) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        sharedViewModel.snackbarFlow.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            LoginScreen()

        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PinpointworksNewTheme {
        LoginScreen()
    }
}