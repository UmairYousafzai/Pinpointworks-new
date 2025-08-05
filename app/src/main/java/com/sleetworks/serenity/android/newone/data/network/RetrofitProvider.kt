package com.sleetworks.serenity.android.newone.data.network

import com.sleetworks.serenity.android.newone.domain.reporitories.DataStoreRepository
import com.sleetworks.serenity.android.newone.utils.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitProvider @Inject constructor(
    private val dataStore: DataStoreRepository,
    private val okHttpClient: OkHttpClient
) {
    private var retrofit: Retrofit? = null
    private var cachedBaseUrl: String? = null

    suspend fun getRetrofit(): Retrofit {
        val baseUrl = dataStore.getString(BASE_URL)+"api/v1/"
        if (retrofit == null || baseUrl != cachedBaseUrl) {
            cachedBaseUrl = baseUrl
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}
