package com.example.network

import okhttp3.Interceptor
import okhttp3.Response

const val APPID = "appid"

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(
        chain.request().run {
            val newUrl = url.newBuilder()
                .addQueryParameter(APPID, BuildConfig.OPEN_WEATHER_API_KEY)
                .build()

            newBuilder()
                .url(newUrl)
                .build()
        }
    )
}
