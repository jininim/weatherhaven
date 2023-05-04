package com.app.weatherhaven.retrofit.coldshelter

import com.app.weatherhaven.BuildConfig
import com.app.weatherhaven.retrofit.heatshelter.DATA
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ColdService {
    @GET("${BuildConfig.API_KEY}/json/TbGtnCwP/{start}/{end}")
    fun getData(
        @Path("start") start: Int,
        @Path("end") end: Int
    ): Call<ColdDATA>
}