package com.sleetworks.serenity.android.newone.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sleetworks.serenity.android.newone.R
import com.sleetworks.serenity.android.newone.ui.theme.PaleGold

@Composable
fun  LoaderButton(
    modifier: Modifier,
    text: String,
    fontSize: TextUnit,
    cornerRadius: Float,
    onClick: () -> Unit,
    shouldShowLoader: Boolean,

) {


    ElevatedButton(
        modifier = modifier

        ,
        onClick = onClick,
        shape = RoundedCornerShape(cornerRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = PaleGold
        )
    ) {



        if (shouldShowLoader) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_btn_anim))
            LottieAnimation(
                composition,
                iterations = LottieConstants.IterateForever,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().fillMaxHeight()
            )
        } else {
            Text(text, fontSize = fontSize)
        }
    }

}