package com.tritreesoultions.utils.api

import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

class ApiClientManager {

    private val cookieJar: JavaNetCookieJar = JavaNetCookieJar(CookieManager())

    fun <T : Any> apiClient(kClass: KClass<T>, baseUrl: String): T = Retrofit.Builder()
        .client(getClient())
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(kClass.java)

    private fun getClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .addInterceptor {
                it.proceed(it.request().newBuilder().build())
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .cookieJar(cookieJar)
            .build()
    }
}