package com.sleetworks.serenity.android.presentation.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sleetworks.serenity.android.R
import com.sleetworks.serenity.android.ui.theme.PinpointworksNewTheme
import com.sleetworks.serenity.android.ui.theme.charcoal
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.sleetworks.serenity.android.ui.theme.brightBlue

@Composable
fun LoginScreen() {
    var isPasswordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize(1f)
            .background(color = charcoal),
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .padding(top = 50.dp)
                    .width(250.dp)
                    .height(120.dp),
                painter = painterResource(id = R.drawable.pinpoint_works_logo_with_text),
                contentDescription = "Pinpoint Works Logo"
            )

            TextField(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth(),
                value = "",
                onValueChange = {},
                placeholder = { Text("Email address") },
                shape = RoundedCornerShape(0),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )

            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = "",
                onValueChange = {},
                placeholder = { Text("Password") },
                shape = RoundedCornerShape(0),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image =
                        if (isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    val description = if (isPasswordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(imageVector = image, contentDescription = description, tint = charcoal)
                    }
                }
            )

            ElevatedButton(
                modifier = Modifier
                    .padding(top = 10.dp),
                onClick = {

                },
                shape = RoundedCornerShape(8),
                colors = ButtonDefaults.buttonColors(
                    containerColor = brightBlue,
                    contentColor = Color.White
                )
            ) {
                Text("Log In", fontSize = 16.sp)
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    PinpointworksNewTheme {
        LoginScreen()
    }
}