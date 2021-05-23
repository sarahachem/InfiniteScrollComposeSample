package com.example.neugelb.apis

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiBuilders {

    fun getTMDBApi(): TMDBApi {
        return Retrofit.Builder()
            .client(
                OkHttpClient.Builder().build()
            )
            .baseUrl(TMDB_URL_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TMDBApi::class.java)
    }
}