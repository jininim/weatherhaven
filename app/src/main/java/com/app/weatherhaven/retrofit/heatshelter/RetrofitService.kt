package com.app.weatherhaven.retrofit.heatshelter

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitService {
    @GET("4d5365574f68616b33355675786c58/json/TbGtnHwcwP/{start}/{end}")
    fun getData(
        @Path("start") start: Int,
        @Path("end") end: Int
    ): Call<DATA>

    @GET("4d5365574f68616b33355675786c58/json/TbGtnHwcwP/{start}/{end}")
    fun getResponse(
        @Path("start") start: Int,
        @Path("end") end: Int
    ): Response<JsonObject>
}

