package com.app.weatherhaven.retrofit.lola

import com.app.weatherhaven.BuildConfig
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LolaService {
    @GET("/req/address")
    fun getData(
        @Query("service") service: String = "address",
        @Query("request")  request : String = "getcoord",
        @Query("version")  version : String ="2.0",
        @Query("simple")  simple : String = "true",
        @Query("type")  type : String = "road",
        @Query("key")  key : String =BuildConfig.API_KEY2,
        @Query("address")  address : String
    ): Call<LOLA>

}

