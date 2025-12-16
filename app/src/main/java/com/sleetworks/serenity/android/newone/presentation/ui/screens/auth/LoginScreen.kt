package com.sleetworks.serenity.android.newone.presentation.ui.screens.auth

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sleetworks.serenity.android.newone.R
import com.sleetworks.serenity.android.newone.domain.models.APIsListItem
import com.sleetworks.serenity.android.newone.presentation.common.UIEvent
import com.sleetworks.serenity.android.newone.presentation.ui.components.APIsSelectionDialog
import com.sleetworks.serenity.android.newone.presentation.ui.components.ErrorBox
import com.sleetworks.serenity.android.newone.presentation.ui.components.LoaderButton
import com.sleetworks.serenity.android.newone.presentation.ui.components.OTPDialog
import com.sleetworks.serenity.android.newone.presentation.viewmodels.AuthViewModel
import com.sleetworks.serenity.android.newone.presentation.viewmodels.SharedViewModel
import com.sleetworks.serenity.android.newone.ui.theme.Charcoal
import com.sleetworks.serenity.android.newone.ui.theme.LightGrey
import com.sleetworks.serenity.android.newone.ui.theme.PaleGold
import com.sleetworks.serenity.android.newone.ui.theme.PinpointworksNewTheme


@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel
) {

    val TAG: String = "LoginScreen"
    val loader by authViewModel.loader.collectAsState()
    val error by authViewModel.error.collectAsState()
    val message by authViewModel.message.collectAsState()
    val loginSuccess by authViewModel.loginSuccess.collectAsState()
    val authenticatedUrls by authViewModel.authenticatedURLS.collectAsState()
    val tfaNumber by authViewModel.tfaNumber.collectAsState()
    var isPasswordVisible by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showAPIsDialog by remember { mutableStateOf(false) }
    var showOTPDialog by remember { mutableStateOf(false) }
    val uiEvent = authViewModel.uiEvent

        val textFieldColor = TextFieldDefaults.colors(
            focusedPlaceholderColor = LightGrey,
            unfocusedPlaceholderColor = LightGrey,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = LightGrey,
            unfocusedIndicatorColor = LightGrey,

            )


    showAPIsDialog = authenticatedUrls.size > 1

    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            sharedViewModel.showSnackbar(message)
        }
    }

    LaunchedEffect(Unit) {
        uiEvent.collect { event ->
            when (event) {
                is UIEvent.Navigate -> {

                    navController.navigate(event.route) {
                        popUpTo("login") { inclusive = true }

                    }
                }

                is UIEvent.PopBackStack -> navController.popBackStack()
                UIEvent.Logout -> {
                }
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize(1f)
            .background(color = Charcoal),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.auth_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

//        if (loader) {

//        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .padding(top = 70.dp)
                    .width(250.dp)
                    .height(120.dp),
                painter = painterResource(id = R.drawable.pinpoint_works_logo_with_text),
                contentDescription = "Pinpoint Works Logo"
            )

            Box(
                modifier = Modifier
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    if (error.isNotEmpty())
                        ErrorBox(error)
                    TextField(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth(),
                        value = email,
//                        label = { Text("Email") },
                        textStyle = TextStyle(fontSize = 16.sp),
                        onValueChange = {
                            authViewModel.clearError()
                            email = it
                            Log.d("LoginScreen", "LoginScreen: email $email")

                        },
                        placeholder = { Text("Email address") },
                        shape = RoundedCornerShape(0),
                        colors = textFieldColor,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        leadingIcon = {
                            Icon(
                                Icons.Filled.Email,
                                contentDescription = "Email",
                                tint = LightGrey,
                                modifier = Modifier
                                    .width(30.dp)
                                    .height(30.dp),
                            )
                        }

                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = password,
//                        label = { Text("Password") },
                        textStyle = TextStyle(fontSize = 16.sp),
                        onValueChange = {
                            authViewModel.clearError()
                            password = it
                            Log.d("LoginScreen", "LoginScreen: password $password")
                        },
                        placeholder = { Text("Password") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        shape = RoundedCornerShape(0),
                        colors = textFieldColor,
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        leadingIcon = {
                            Icon(
                                Icons.Filled.Lock,
                                contentDescription = "Email",
                                tint = LightGrey,
                                modifier = Modifier
                                    .width(30.dp)
                                    .height(30.dp),


                                )
                        },
                        trailingIcon = {
                            val image =
                                if (isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                            val description =
                                if (isPasswordVisible) "Hide password" else "Show password"

                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    modifier = Modifier
                                        .width(24.dp)
                                        .height(24.dp),
                                    imageVector = image,
                                    contentDescription = description,
                                    tint = LightGrey
                                )
                            }
                        }
                    )

                    LoaderButton(
                        modifier = Modifier
                            .padding(horizontal = 50.dp, vertical = 20.dp)
                            .border(width = 2.dp, color = PaleGold, shape = RoundedCornerShape(30))
                            .fillMaxWidth()
                            .height(80.dp),
                        text = "Log In",
                        fontSize = 18.sp,
                        cornerRadius = 30f,
                        onClick = {
                            authViewModel.checkUserExists(email, password)
                        },
                        shouldShowLoader = loader.second
                    )

                }

            }

        }

        if (showAPIsDialog) {
            APIsSelectionDialog(
                urls = authenticatedUrls.map { it -> APIsListItem(it, false) },
                onContinue = { url ->
                    authViewModel.login(email, password, url)
                },
                onDismiss = {
                    authViewModel.clearAuthURls()

                }
            )
        }

        if (showOTPDialog) {
            OTPDialog(phoneNumber = "1234567", onContinue = { otp ->
                authViewModel.loginWithOTP(email, password, otp)
            }, onResend = {
                authViewModel.login(email, password)
            }) {
                showOTPDialog = false
            }
        }
        if (loader.second) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) { awaitPointerEventScope { while (true) awaitPointerEvent() } }
            )
        }


//        if (loader.second)
//            CustomLoader(loader.first)

    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    PinpointworksNewTheme {
//        LoginScreen()
    }
}