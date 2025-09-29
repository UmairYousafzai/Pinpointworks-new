package com.sleetworks.serenity.android.newone.presentation.ui.screens.pointDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sleetworks.serenity.android.newone.R
import com.sleetworks.serenity.android.newone.data.models.local.PointFieldType
import com.sleetworks.serenity.android.newone.domain.models.point.PointDomain
import com.sleetworks.serenity.android.newone.presentation.navigation.Screen
import com.sleetworks.serenity.android.newone.presentation.ui.components.AppTopBar
import com.sleetworks.serenity.android.newone.presentation.viewmodels.PointDetailViewModel
import com.sleetworks.serenity.android.newone.ui.theme.Goldenrod
import com.sleetworks.serenity.android.newone.ui.theme.PinpointworksNewTheme
import com.sleetworks.serenity.android.newone.ui.theme.gray
import com.sleetworks.serenity.android.newone.ui.theme.onyx
import com.sleetworks.serenity.android.newone.ui.theme.paleBlueGray
import com.sleetworks.serenity.android.newone.utils.TextRichOptions
import com.sleetworks.serenity.android.newone.utils.clearResult
import com.sleetworks.serenity.android.newone.utils.resultFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun DefectDetailScreen(
    navController: NavHostController,
    viewModel: PointDetailViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.getPointComments()
    }

    var showEditTextDialog by remember {
        mutableStateOf(false)
    }
    var pointFieldsLoader by remember { mutableStateOf(mutableMapOf<String, String>()) }
    var pointFieldsSuccess by remember { mutableStateOf(mutableMapOf<String, String>()) }

    val plainText by navController.resultFlow("plainText", "").collectAsState()
    val base64Value by navController.resultFlow("base64Value", "").collectAsState()
    val mentions by navController.resultFlow("mentions", emptyList<String>()).collectAsState()
    val fieldType by navController.resultFlow("fieldType", emptyList<String>()).collectAsState()

    val point by viewModel.point.collectAsState()

    LaunchedEffect(plainText) {

        if (plainText.isNotEmpty()) {
            navController.clearResult("plainText")
        }
        if (base64Value.isNotEmpty()) {
            navController.clearResult("base64Value")
        }
        if (mentions.isNotEmpty()) {
            navController.clearResult("mentions")
        }
        if (mentions.isNotEmpty()) {
            navController.clearResult("mentions")
        }
    }



    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn() {
            item {
                Column() {
                    AppTopBar() { }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, start = 10.dp, end = 10.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.fillMaxSize(0.1f),
                            text = "ID",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = gray
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(

                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .clickable(onClick = {
                                    showEditTextDialog = true
                                }),
                            text = point.title,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 2
                        )
                        if (pointFieldsLoader.contains(PointFieldType.TITLE.value)) {
                            CircularProgressIndicator(modifier = Modifier.size(30.dp))
                        }

                        if (pointFieldsSuccess.contains(PointFieldType.TITLE.value)) {
                            Icon(
                                painter = painterResource(R.drawable.ic_green_check_solid),
                                contentDescription = "Icon checked",
                                tint = null
                            )
                        }
                    }

//                    Spacer(modifier = Modifier.height(5.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 5.dp),
                    ) {
                        TextChipWithIcon("Medium")
                        Spacer(modifier = Modifier.width(10.dp))
                        TextChipWithIcon("Medium", shouldShowIcon = true)
                        Spacer(modifier = Modifier.width(20.dp))
                        IconWithBg()
                        Spacer(modifier = Modifier.width(20.dp))
                        IconWithBg(icon = R.drawable.ic_reminder)

                    }

                    Text(
                        text = TextRichOptions.convertBase64ToRichContentString(point.descriptionRich),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = onyx,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .clickable(
                                onClick = {

                                    navController
                                        .navigate(Screen.RichTextEditorScreen.route + "/${PointFieldType.DESCRIPTION.value}/${point.descriptionRich}")
                                }
                            )
                    )


                }
            }


        }

        if (showEditTextDialog)
            PointTextEditDialog("Title", onDismiss = {
                showEditTextDialog = false
            }, onConfirm = { value, customField ->
                showEditTextDialog = false

                if (customField == null) {
                    point = point.copy(title = value)

                }

            })

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconWithBg(icon: Int = R.drawable.ic_flagged_gray) {

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color = paleBlueGray)
            .padding(horizontal = 17.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    )
    {
        Icon(
            modifier = Modifier.size(18.dp),
            painter = painterResource(icon),
            contentDescription = "Icon",
            tint = null,
        )
    }
}

@Composable
fun TextChipWithIcon(
    text: String = "",
    icon: Int = R.drawable.ic_open_light,
    color: Color = Goldenrod,
    shouldShowIcon: Boolean = false
) {

    val isIconVisible = remember { shouldShowIcon }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color = color)
            .padding(horizontal = 15.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (isIconVisible)
            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(icon),
                contentDescription = "Icon",
                tint = Color.White,
            )
        Spacer(modifier = Modifier.width(5.dp))
        Text(

            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }

}


@Preview(showBackground = true)
@Composable
fun DefectDetailScreenPreview() {
    PinpointworksNewTheme {
//        DefectDetailScreen()
    }
}