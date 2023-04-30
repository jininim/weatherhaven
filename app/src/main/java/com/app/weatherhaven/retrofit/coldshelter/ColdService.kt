package com.app.weatherhaven.retrofit.coldshelter

import retrofit2.Call
import retrofit2.http.GET

interface ColdService {
    @GET("/v3/79c6322b-9c41-4828-9870-74f1cae803c1")
    fun getColdList(): Call<ColdDTO>
}