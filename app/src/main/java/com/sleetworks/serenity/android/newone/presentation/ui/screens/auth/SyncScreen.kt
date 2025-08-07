package com.sleetworks.serenity.android.newone.presentation.ui.screens.auth

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sleetworks.serenity.android.newone.R
import com.sleetworks.serenity.android.newone.presentation.ui.components.LoaderButton
import com.sleetworks.serenity.android.newone.presentation.viewmodels.SharedViewModel
import com.sleetworks.serenity.android.newone.presentation.viewmodels.SyncViewModel
import com.sleetworks.serenity.android.newone.ui.theme.PaleGold
import com.sleetworks.serenity.android.newone.ui.theme.PinpointworksNewTheme


@Composable
fun SyncScreen(
    navController: NavController,
    syncViewModel: SyncViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel = hiltViewModel()
) {
    val TAG: String = "SyncScreen"

    val message by syncViewModel.message.collectAsState()
    val error by syncViewModel.error.collectAsState()
    val success by syncViewModel.success.collectAsState()


    LaunchedEffect(error) {
        
        if (error.isNotEmpty()){
            Log.d(TAG, "SyncScreen: show snackbar ")
            sharedViewModel.showSnackbar(error)
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.auth_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .padding(top = 70.dp)
                    .fillMaxWidth()
                    .height(130.dp),
                painter = painterResource(id = R.drawable.pinpoint_works_logo_with_text),
                contentDescription = "Pinpoint Works Logo"
            )

            Spacer(modifier = Modifier.height(70.dp))

            Text(
                text = "You've successfully \nlogged in",
                color = Color.White,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 30.sp,
                    lineHeight = 40.sp
                )
            )
            Spacer(modifier = Modifier.height(150.dp))

            Text(
                text = "Some data will now be \ndownloaded for offline use.",
                color = Color.White,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    lineHeight = 30.sp
                )
            )
            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = message,
                color = Color.White,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 15.sp,
                    lineHeight = 20.sp
                )
            )

            Spacer(modifier = Modifier.height(50.dp))

            LoaderButton(
                modifier = Modifier
                    .padding(horizontal = 80.dp, vertical = 20.dp)
                    .border(width = 2.dp, color = PaleGold, shape = RoundedCornerShape(30))
                    .fillMaxWidth()
                    .height(60.dp),
                text = "Start",
                fontSize = 18.sp,
                cornerRadius = 30f,
                onClick = {
                    syncViewModel.syncData()
                },
                shouldShowLoader = !success
            )


        }
    }

}

@Preview(showBackground = true)
@Composable
fun SyncScreenPreview() {
    PinpointworksNewTheme {
//        SyncScreen()
    }
}