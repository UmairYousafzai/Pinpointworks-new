package com.sleetworks.serenity.android.newone.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sleetworks.serenity.android.newone.R
import com.sleetworks.serenity.android.newone.ui.theme.OuterSpace
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppTopBar(
    scope: CoroutineScope? = null,
    drawerState: DrawerState? = null,
    title: String = "",
    subTitle: String = "",
    shouldDrawerIntegrate: Boolean = false,
    actionIcons: List<Pair<String, ImageVector>> = emptyList(),
    actionClick: (String) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(OuterSpace)
            .padding(vertical = 8.dp)

    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    if (shouldDrawerIntegrate) {
                        scope?.launch { drawerState?.open() }

                    }else{
                        actionClick("Back")
                    }
                },
            ) {
                val icon = if (shouldDrawerIntegrate) {
                    R.drawable.ic_hamburger
                } else {
                    R.drawable.ic_back_solid

                }

                Icon(
                    painter = painterResource(icon),
                    contentDescription = "Menu",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.weight(0.1f))


            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(
                    title,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
                if (subTitle.isNotEmpty())
                    Text(

                        subTitle,
                        style = TextStyle(color = Color.Gray, fontSize = 16.sp)
                    )
            }

            Spacer(modifier = Modifier.weight(1f))

            actionIcons.forEach { iconPair ->
                IconButton(
                    onClick = { actionClick(iconPair.first) },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = iconPair.second,
                        contentDescription = "Search",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }


        }
    }
}