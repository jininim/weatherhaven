package com.app.weatherhaven.retrofit.heatshelter

import com.app.weatherhaven.BuildConfig
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitService {
    @GET("${BuildConfig.API_KEY}/json/TbGtnHwcwP/{start}/{end}")
    fun getData(
        @Path("start") start: Int,
        @Path("end") end: Int
    ): Call<DATA>

}

