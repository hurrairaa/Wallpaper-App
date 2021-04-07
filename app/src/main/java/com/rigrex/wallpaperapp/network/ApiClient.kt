package com.rigrex.wallpaperapp.network

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class ApiClient {
    companion object {
        const val API_KEY_PIXABAY = "12253541-54470c33af2b44d3aa80af484"
        const val API_KEY_UNSPLASH =
            "c51f45aec3b7a2f395c478c881127e1e1a94da1c56d38e453661add54543e26f"
        const val BASE_URL_PIXABAY = "https://pixabay.com/"
        const val BASE_URL_UNSPLASH = "https://api.unsplash.com/"
        private var retrofitUnSplash: Retrofit? = null
        private var retrofitPixaBay: Retrofit? = null
        private val REQUESt_TIMEOUT = 30
        private val TAG = "ApiClint"
        private var okHttpClient: OkHttpClient? = null
        var CLIENT_TYPE_PIXABAY = 0
        var CLIENT_TYPE_UNSPLASH = 1

        fun getClient(clientType: Int): Retrofit {
            when (clientType) {
                CLIENT_TYPE_PIXABAY -> {
                    if (retrofitPixaBay == null) {
                        retrofitPixaBay = ClientFactory
                            .provideClient(
                                BASE_URL_PIXABAY, REQUESt_TIMEOUT.toLong(), TAG,
                                mapOf(
                                    "cache-control" to "public, max-age=60",
                                    "content-type" to "application/x-www-form-urlencoded",
                                )
                            )
                    }
                    return retrofitPixaBay!!
                }
                CLIENT_TYPE_UNSPLASH -> {
                    if (retrofitUnSplash == null) {
                        retrofitUnSplash = ClientFactory
                            .provideClient(
                                BASE_URL_UNSPLASH, REQUESt_TIMEOUT.toLong(), TAG,
                                mapOf(
                                    "cache-control" to "public, max-age=60",
                                    "content-type" to "application/x-www-form-urlencoded",
                                )
                            )
                    }
                    return retrofitUnSplash!!
                }
                else -> {
                    throw Exception("Invalid options please select from CLIENT_TYPE_OXFORD and CLIENT_TYPE_GRAMMAR")
                }
            }
        }

        fun resetApiClient() {
            retrofitPixaBay = null
            retrofitUnSplash = null
            okHttpClient = null
        }
    }
}