package com.sleetworks.serenity.android.newone.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sleetworks.serenity.android.newone.data.mappers.toDomain
import com.sleetworks.serenity.android.newone.data.models.remote.response.Assignee
import com.sleetworks.serenity.android.newone.data.storage.InternalWorkspaceStorageUtils
import com.sleetworks.serenity.android.newone.domain.reporitories.local.DataStoreRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.UserRepository
import com.sleetworks.serenity.android.newone.utils.QuillRichTextHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RichTextEditorViewModel @Inject constructor(
    @ApplicationContext
    val context: Context,
    val userRepository: UserRepository,
    val dataStoreRepository: DataStoreRepository,
    val savedStateHandle: SavedStateHandle

) : ViewModel() {

    private val _fieldType: MutableStateFlow<String> = MutableStateFlow("")
    val fieldType: StateFlow<String>
        get() = _fieldType

    private val _customFieldId: MutableStateFlow<String> = MutableStateFlow("")
    val customFieldId: StateFlow<String>
        get() = _customFieldId


    private val _initialValue: MutableStateFlow<String> = MutableStateFlow("")
    val initialValue: StateFlow<String>
        get() = _initialValue

    private val _fileUrl: MutableStateFlow<String> = MutableStateFlow("")
    val fileUrl: StateFlow<String>
        get() = _fileUrl

    private val _htmlContent: MutableStateFlow<String> = MutableStateFlow("")
    val htmlContent: StateFlow<String>
        get() = _htmlContent
    private val _workspaceID = dataStoreRepository.workspaceIDFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
    val workspaceID
        get() = _workspaceID


    init {
        _fieldType.value = savedStateHandle.get<String>("fieldType") ?: ""
        _initialValue.value = savedStateHandle.get<String>("initialValue") ?: ""
        _customFieldId.value = savedStateHandle.get<String>("customFieldId") ?: ""
    }

    private suspend fun getUsers(): List<Assignee> {
        return userRepository.getUserByWorkspaceId(_workspaceID.value ?: "").map {
            it.toDomain()
        }
    }

    fun createAndLoadQuillHTML(
        maxLength: Int,
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            val users = getUsers()
            // Create the HTML content
            val quillHelper = QuillRichTextHelper()
            val htmlContent =
                quillHelper.getQuillTextEditHtml(
                    _fieldType.value,
                    _initialValue.value,
                    users,
                    context
                )

//            _htmlContent.emit(htmlContent)

            val htmlDir = InternalWorkspaceStorageUtils().getFileReference(
                _workspaceID.value.toString(),
                _workspaceID.value.toString() + "-quill/",
                "",
                context
            )
            if (!htmlDir.exists()) {

                htmlDir.mkdirs()
            }

            val htmlFile = File(htmlDir, "result.html")
            try {
                htmlFile.writeText(htmlContent)

                // Load the HTML file in WebView
                _htmlContent.emit(htmlContent)
                _fileUrl.emit("file://${htmlFile.absolutePath}")

            } catch (e: Exception) {
                e.printStackTrace()

                _fileUrl.emit("file:///android_asset/QuillInit.html")

            }

        }
    }
}