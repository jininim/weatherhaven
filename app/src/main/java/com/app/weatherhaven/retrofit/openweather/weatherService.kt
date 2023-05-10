package com.app.weatherhaven.retrofit.openweather

import com.app.weatherhaven.BuildConfig
import com.app.weatherhaven.retrofit.lola.LOLA
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface weatherService {
    @GET("/data/2.5/weather")
    fun getData(
        @Query("lat") service: String = "37.530618",
        @Query("lon")  request : String = "127.030651",
        @Query("lang")  version : String ="kr",
        @Query("units")  simple : String = "metric",
        @Query("appid")  key : String = BuildConfig.API_KEY3,
    ): Call<WHATHER>
}