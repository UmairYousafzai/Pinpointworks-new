package com.sleetworks.serenity.android.newone.presentation.viewmodels

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.sleetworks.serenity.android.newone.BuildConfig
import com.sleetworks.serenity.android.newone.data.mappers.toEntity
import com.sleetworks.serenity.android.newone.data.models.local.datastore.UserPreference
import com.sleetworks.serenity.android.newone.data.models.remote.request.LoggedRequest
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.LoginResponse
import com.sleetworks.serenity.android.newone.data.network.ApiException
import com.sleetworks.serenity.android.newone.data.network.Resource
import com.sleetworks.serenity.android.newone.domain.reporitories.AuthRemoteRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.DataStoreRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.UserRepository
import com.sleetworks.serenity.android.newone.utils.BASE_URL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRemoteRepository: AuthRemoteRepository,
    private val userRepository: UserRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val TAG = "AuthViewModel"
    private val _loader: MutableStateFlow<Pair<String, Boolean>> = MutableStateFlow(Pair("", false))
    val loader: StateFlow<Pair<String, Boolean>>
        get() = _loader

    private val _error: MutableStateFlow<String> = MutableStateFlow("")
    val error: StateFlow<String>
        get() = _error

    private val _message: MutableStateFlow<String> = MutableStateFlow("")
    val message: StateFlow<String>
        get() = _message

    private val _loginSuccess: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean>
        get() = _loginSuccess

    private val _authenticatedURLS: MutableStateFlow<ArrayList<String>> = MutableStateFlow(
        arrayListOf()
    )

    val authenticatedURLS: StateFlow<List<String>>
        get() = _authenticatedURLS

    private val _tfaNumber: MutableStateFlow<String> = MutableStateFlow(
        ""
    )

    val tfaNumber: StateFlow<String>
        get() = _tfaNumber


    fun clearError() {
        viewModelScope.launch {
            _error.value = "";
        }
    }

    fun clearAuthURls() {
        viewModelScope.launch {
            _authenticatedURLS.value = arrayListOf();

        }
    }

    fun loginWithOTP(email: String, password: String, code: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _loader.emit(Pair("Logging in", true))

            val result = authRemoteRepository.loginWithCode(email, password, code)
            when (result) {
                is Resource.Success -> {
                    result.data.entity?.let {
                        storeUserData(it.authToken, it.user, password)
                    }
                }

                is Resource.Error -> {
                    Log.d(TAG, "login: ", result.apiException)
                    var message = ""

                    when (result.apiException) {
                        is ApiException.NetworkException -> {
                            message = result.apiException.message ?: "Unexpected error"
                        }

                        is ApiException.UnauthorizedException -> {
                            message = "Invalid email address or password"
                        }

                        else -> {
                            message = result.apiException.message ?: "Unexpected error"
                        }
                    }


                    _error.emit(message)


                }

                Resource.Loading -> {
                    // optional: handle loading state per call if needed
                }
            }
            _loader.emit(Pair("", false))

        }
    }

    fun login(email: String, password: String, baseUrl: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            _loader.emit(Pair("Logging in", true))
            clearAuthURls()
            if (baseUrl.isNotEmpty()) {
                saveBaseUrl(baseUrl)
            }


            val result = authRemoteRepository.login(email, password)
            when (result) {
                is Resource.Success -> {

                    val response = result.data.entity

                    if (response == null) {
                        result.data.error?.let { _tfaNumber.emit(it) }
                    } else {

                        storeUserData(
                            response.authToken,
                            response.user,
                            password,
                        )

                        setupEnvironment(response)

                        Log.d(TAG, "login: store info")
                    }
                }

                is Resource.Error -> {
                    Log.d(TAG, "login: ", result.apiException)
                    val message = when (result.apiException) {
                        is ApiException.NetworkException -> {
                            result.apiException.message ?: "Unexpected error"
                        }

                        is ApiException.UnauthorizedException -> {
                            "Invalid email address or password"
                        }

                        else -> {
                            result.apiException.message ?: "Unexpected error"
                        }
                    }


                    _error.emit(message)


                }

                Resource.Loading -> {
                    // optional: handle loading state per call if needed
                }
            }
            _loader.emit(Pair("", false))

        }
    }


    suspend fun storeUserData(authToken: String, user: String, password: String) {

        dataStoreRepository.saveUserInfo(
            UserPreference(
                authToken,
                user,
                password,
            )
        )

    }

    suspend fun setupEnvironment(response: LoginResponse) {

        _loader.emit(Pair("Fetching user", true))

        val result = authRemoteRepository.getAuthUser(LoggedRequest(response.user, response.authToken))


        when (result) {
            is Resource.Success -> {
                Log.d(TAG,"auth user ${Gson().toJson(result.data.entity?.user)}")
                result.data.entity?.user?.apply {
                    passwordHash= ""
                }
                result.data.entity?.user?.let {

                    userRepository.insertUser(it.toEntity())
                }
                setupFirebase()
                setupSentry()
            }

            is Resource.Error -> {
                Log.d(TAG, "setupEnvironment: ", result.apiException)
                _error.emit("Unexpected error")

            }

            is Resource.Loading -> {}
        }
        _loader.emit(Pair("", false))

    }

    fun setupFirebase() {

    }

    fun setupSentry() {

    }

    fun checkUserExists(email: String, password: String) {

        viewModelScope.launch {
            clearAuthURls()
            if (!isEmailAndPasswordValid(email, password)) {
                return@launch
            }

            _loader.emit(Pair("Checking user", true))
            val urls = (BuildConfig.URL_ARRAY).toList()
            val authURls = mutableListOf<String>()

            val results = urls.map { url ->
                async(Dispatchers.IO) { url to authRemoteRepository.checkIfUserExists(email, url) }
            }.awaitAll()

            results.forEach { (url, result) ->
                when (result) {
                    is Resource.Success -> {
                        authURls.add(url)
                    }

                    is Resource.Error -> {
                        Log.d(TAG, "checkUserExists: ", result.apiException)
                        var message = ""

                        if (result.apiException is ApiException.NetworkException) {
                            message = result.apiException.message ?: "Unexpected error"
                        }

                        _error.emit(message)


                    }

                    Resource.Loading -> {
                        // optional: handle loading state per call if needed
                    }
                }
            }
            _loader.emit(Pair("", false))
            if (authURls.size == 1) {
                login(email, password, authURls[0])
            } else if (authURls.size > 1) {
               _authenticatedURLS.emit(authURls as ArrayList<String>)
            } else {
                _error.emit("User does not exists")
            }
        }


    }

    fun saveBaseUrl(url: String) {
        viewModelScope.launch {
            dataStoreRepository.putString(BASE_URL, url)
        }
    }


    suspend fun isEmailAndPasswordValid(email: String, password: String): Boolean {
        if (email.isEmpty() && password.isEmpty()) {
            _error.emit("Please enter email and password")
            return false;
        }
        if (email.isEmpty()) {
            _error.emit("Please enter an email address")
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _error.emit("Please enter a valid email address")
            return false
        }
        if (password.isEmpty()) {
            _error.emit("Please enter a password")
            return false
        }
        return true
    }
}