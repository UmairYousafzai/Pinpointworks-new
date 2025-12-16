package com.sleetworks.serenity.android.newone.presentation.ui.screens

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kevinnzou.web.WebView
import com.kevinnzou.web.WebViewNavigator
import com.kevinnzou.web.WebViewState
import com.kevinnzou.web.rememberWebViewNavigator
import com.kevinnzou.web.rememberWebViewState
import com.sleetworks.serenity.android.newone.presentation.ui.components.AppTopBar
import com.sleetworks.serenity.android.newone.presentation.viewmodels.RichTextEditorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RichTextEditorScreen(
    viewModel: RichTextEditorViewModel = hiltViewModel(),
    navController: NavController
) {

    var webView by remember { mutableStateOf<android.webkit.WebView?>(null) }
    val state = rememberWebViewState("")
    val navigator = rememberWebViewNavigator()
    val fieldType by viewModel.fieldType.collectAsState()
    val customFieldId by viewModel.customFieldId.collectAsState()
    BackHandler {

        navController.popBackStack()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            AppTopBar(
                title = fieldType,
                actionIcons = listOf(
                    Pair("Done", Icons.Default.Done)
                )
            ) { action ->
                when (action) {
                    "Done" -> {
                        webView?.evaluateJavascript("onSaveValue();", null)
                    }

                    "Back" -> {
                        navController.popBackStack()
                    }
                }
            }
            QuillRichTextEditor(
                state,
                navigator,
                viewModel,

                maxLength = 1000,
                onWebViewCreated = { webViewInstance ->
                    webView = webViewInstance
                },
                onSave = { plainText, base64Value, mentions ->


                    with(navController.previousBackStackEntry?.savedStateHandle) {
                        this?.set("plainText", plainText)
                        this?.set("base64Value", base64Value)
                        this?.set("mentions", mentions)
                        this?.set("fieldType", fieldType)
                        this?.set("customFieldId", customFieldId)
                        this?.set("customFieldTempId", customFieldId)
                    }
                    navController.popBackStack()
                }
            )
        }


    }
}

@Composable
fun QuillRichTextEditor(
    state: WebViewState,
    navigator: WebViewNavigator,
    viewModel: RichTextEditorViewModel,
    maxLength: Int = 1000,
    onWebViewCreated: (android.webkit.WebView) -> Unit = {},
    onSave: (String, String, List<String>) -> Unit
) {
    val fileUrl by viewModel.fileUrl.collectAsState()
    val workspaceID by viewModel.workspaceID.collectAsState()
    val mainHandler = Handler(Looper.getMainLooper())

    // Generate HTML once when inputs change, not every recomposition
    LaunchedEffect(workspaceID) {
        if (workspaceID?.isNotEmpty() == true) {
            viewModel.createAndLoadQuillHTML(maxLength)
        }


    }

    // Load when fileUrl actually updates
    LaunchedEffect(fileUrl) {
        if (fileUrl.isNotEmpty()) {
            navigator.loadUrl(fileUrl)

        }


    }

    WebView(
        state = state,
        navigator = navigator,
        modifier = Modifier.fillMaxSize(),
        onCreated = { web ->
            web.settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                allowContentAccess = true
                allowFileAccess = true
            }
            web.webViewClient = object : WebViewClient() {}
            web.webChromeClient = object : WebChromeClient() {
                override fun onConsoleMessage(cm: ConsoleMessage): Boolean {
                    Log.d(
                        "WebViewConsole",
                        "${cm.message()} @${cm.sourceId()}:${cm.lineNumber()}"
                    )
                    return true
                }
            }
            web.addJavascriptInterface(object {
                @JavascriptInterface
                fun getValue(plainText: String, base64Value: String, mentions: String) {
                    Log.e("addJavascriptInterface", "getValue: $plainText")

                    val list = mentions
                        .split(",")
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }


                    mainHandler.post {
                        onSave(plainText, base64Value, list)

                    }
                }
            }, "Android")

            onWebViewCreated(web)
        }
    )


}

