package com.app.weatherhaven.retrofit


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {
    @GET("{apikey}/{json}/{TbGtnHwcwP}/{start}/{end}")
    fun getData(
        @Path("apikey") apiKey: String,
        @Path("json") json: String,
        @Path("TbGtnHwcwP") TbGtnHwcwP: String,
        @Path("start") start: Int,
        @Path("end") end: Int,
    ): Call<HeatShelterList>

}