package com.app.weatherhaven.retrofit.heatShelter

import retrofit2.Call
import retrofit2.http.GET

import retrofit2.http.Query

interface RetrofitService {
    @GET("4d5365574f68616b33355675786c58/json/TbGtnHwcwP/1/1/")
    fun getData(
    ): Call<TbGtnHwcwP>

}