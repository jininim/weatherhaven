package com.app.weatherhaven.retrofit.heatShelter

data class WeatherData(
    val list_total_count: Int,
    val RESULT: RESULT,
    val row: List<Row>
)