package com.sleetworks.serenity.android.newone.presentation.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.sleetworks.serenity.android.newone.R
import com.sleetworks.serenity.android.newone.ui.theme.BrightBlue
import com.sleetworks.serenity.android.newone.ui.theme.Grey
import com.sleetworks.serenity.android.newone.ui.theme.PinpointworksNewTheme
import com.sleetworks.serenity.android.newone.ui.theme.Red
import kotlinx.coroutines.delay

val TAG: String= "OTPDialog"

@Composable
fun OTPDialog(
    phoneNumber: String="1234567",
    shouldShowError: Boolean = false,
    onContinue:(String)-> Unit,
    onResend:()-> Unit,
    onDismiss: () -> Unit = {}
) {

    var time by remember {
        mutableStateOf(60)
    }
    var otp by remember {
        mutableStateOf("")
    }

    LaunchedEffect(time) {
        if (time > 0) {
            delay(1000)
            time--
        }
    }

    Dialog(onDismissRequest = onDismiss) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)

        ) {

            IconButton(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .align(alignment = Alignment.TopStart),
                onClick = onDismiss) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Favorite"
                )
            }

            Image(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .width(250.dp)
                    .height(80.dp)
                    .align(alignment = Alignment.TopCenter),
                painter = painterResource(id = R.drawable.pinpoint_works_bg_black_text),
                contentDescription = "Pinpoint Works Logo"
            )

            Column(modifier = Modifier.padding(top = 100.dp, start = 20.dp, end = 20.dp)) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Enter Code",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "We\'ve just sent a code to $phoneNumber, please check your Whatsapp account or SMS and enter it below.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Grey
                )
                Spacer(modifier = Modifier.height(8.dp))

                OTPTextField(otpValue = otp, onOtpChanged = { newOTP->

                    otp= newOTP

                    Log.d(TAG, "OTPDialog: onOtpChanged===> $otp")
                })

                Spacer(modifier = Modifier.height(10.dp))

                if (shouldShowError) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "Info",
                            tint = Red
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "There's an issue with the verification code you entered. Please try again.",
                            fontSize = 14.sp, color = Red, style = TextStyle(
                                lineHeight = 13.sp
                            )
                        )

                    }
                }
                Spacer(modifier = Modifier.height(10.dp))

                ElevatedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    enabled = time==0,
                    onClick = {
                        onContinue(otp)
//
                    },
                    shape = RoundedCornerShape(8),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrightBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text("Continue", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Didn't receive a code?",
                        fontSize = 13.sp, color = Color.Black,
                    )
                    Spacer(modifier = Modifier.width(2.dp))

                    Text(
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .clickable(onClick = {
                                time = 60
                                onResend()
                            }),
                        text = if (time==0) "Resend Code" else "Resend ($time Seconds)",
                        fontSize = 13.sp, color = BrightBlue,
                    )

                }
                Spacer(modifier = Modifier.height(20.dp))


            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun OTPDialogPreview() {
    PinpointworksNewTheme {
        OTPDialog(phoneNumber = "1234567", onContinue = {}, onResend = {}) { }
    }
}
