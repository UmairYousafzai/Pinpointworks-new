package com.sleetworks.serenity.android.newone.data.network

import com.sleetworks.serenity.android.newone.domain.reporitories.local.DataStoreRepository
import com.sleetworks.serenity.android.newone.utils.AUTH_TOKEN
import com.sleetworks.serenity.android.newone.utils.EMAIL
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { dataStoreRepository.getString(AUTH_TOKEN) }
        val email = runBlocking { dataStoreRepository.getString(EMAIL) }
        val request = chain.request().newBuilder()
            .addHeader("X-Serene-Wave-Auth-Token", token ?: "")
            .addHeader("X-Serene-Wave-User-Email", email ?: "")
            .addHeader("Content-Type", "application/json")
            .build()

        return chain.proceed(request)
    }
}
