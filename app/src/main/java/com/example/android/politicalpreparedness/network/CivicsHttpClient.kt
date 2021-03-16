package com.example.android.politicalpreparedness.network

import android.util.Log
import com.example.android.politicalpreparedness.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import kotlin.math.log


class CivicsHttpClient: OkHttpClient() {

    companion object {

        const val API_KEY =  BuildConfig.API_KEY //TODO COMPLETE: Place your API Key Here


        fun getClient(): OkHttpClient {
            val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Log.d("TEST", "HttpLoggingInterceptor: $message")
                }
            })
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            return Builder()
                    .addInterceptor(logging)
                    .addInterceptor { chain ->
                        val original = chain.request()
                        val url = original
                                .url
                                .newBuilder()
                                .addQueryParameter("key", API_KEY)
                                .build()
                        val request = original
                                .newBuilder()
                                .url(url)
                                .build()
                        chain.proceed(request)
                    }
                    .build()
        }

    }

}