package com.app.weatherhaven.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitManager {
    private const val URL = "https://openapi.seoul.go.kr:8088/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val myApi: RetrofitService = retrofit.create(RetrofitService::class.java)

}