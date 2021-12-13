package com.example.hyperxpress.service.repository.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHyperXpress private constructor() {
    companion object {
        private lateinit var retrofit: Retrofit
        private val httpClient = OkHttpClient.Builder()

        private fun getRetrofitInstance(url: String): Retrofit {
            retrofit = Retrofit.Builder()
                .baseUrl(url)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit

        }

        fun <S> createService(serviceClass: Class<S>, url: String): S {
            return getRetrofitInstance(url).create(serviceClass)
        }
    }
}