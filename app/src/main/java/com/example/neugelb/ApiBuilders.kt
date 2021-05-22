package com.example.neugelb

import com.example.neugelb.apis.TMDB_URL_API
import com.example.neugelb.apis.TMDBApi
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