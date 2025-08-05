package com.sleetworks.serenity.android.newone.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sleetworks.serenity.android.newone.R
import com.sleetworks.serenity.android.newone.ui.theme.PaleGold
import com.sleetworks.serenity.android.newone.ui.theme.RedOpacity90


@Composable
fun ErrorBox(error: String = "") {

    Box(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(1f)
            .clip(RoundedCornerShape(10))
            .background(PaleGold)
    ) {

        Row (verticalAlignment = Alignment.CenterVertically){
            Image(
                modifier = Modifier.width(50.dp).height(50.dp).padding(5.dp),
                painter = painterResource(R.drawable.ic_error),
                contentDescription = "Error icon"
            )

            Text(text = error, modifier = Modifier.padding(10.dp), fontSize = 16.sp)
        }



    }
}