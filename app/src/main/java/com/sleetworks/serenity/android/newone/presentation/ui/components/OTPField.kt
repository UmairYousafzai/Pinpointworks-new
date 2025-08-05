package com.sleetworks.serenity.android.newone.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sleetworks.serenity.android.newone.ui.theme.AliceBlue
import com.sleetworks.serenity.android.newone.ui.theme.PinpointworksNewTheme
import com.sleetworks.serenity.android.newone.ui.theme.BrightBlue
import com.sleetworks.serenity.android.newone.ui.theme.SkyBlue

@Composable
fun OTPTextField(
    otpLength: Int = 6,
    otpValue: String,
    onOtpChanged: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current


    BasicTextField(
        value = otpValue,
        onValueChange = { newValue ->
            if (newValue.length <= otpLength && newValue.all { it.isDigit() }) {
                onOtpChanged(newValue)
                if (newValue.length == otpLength) {
                    keyboardController?.hide()
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(otpLength) { index ->
                    val char = if (index < otpValue.length) otpValue[index].toString() else ""
                    val isFocused = otpValue.length == index
                    Box(
                        modifier = Modifier
                            .size(width = 44.dp, height = 44.dp)
                            .border(
                                width = 2.dp,
                                color = if (isFocused) BrightBlue
                                else SkyBlue,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .background(AliceBlue)
                            .clickable {
                                focusRequester.requestFocus()
                                keyboardController?.show()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Preview(showBackground = true)
@Composable
fun OTPFieldPreview() {
    PinpointworksNewTheme {
        OTPTextField(otpLength = 6,
            otpValue = "",
            onOtpChanged = { })
    }
}