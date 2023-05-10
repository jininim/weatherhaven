package com.app.weatherhaven

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.app.weatherhaven.databinding.ActivityMainBinding
import com.app.weatherhaven.retrofit.heatshelter.RetrofitService
import com.app.weatherhaven.retrofit.openweather.WHATHER
import com.app.weatherhaven.retrofit.openweather.weatherService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private  var temp : String =""
    private  var humidity : String = ""
    private  var desc : String = ""

    //레트로핏 빌더
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val apiService = retrofit.create(weatherService::class.java)

        //현재 날씨, 습도, 날씨 상태 정보 받아오기
            apiService.getData().enqueue(object : Callback<WHATHER> {
                override fun onResponse(call: Call<WHATHER>, response: Response<WHATHER>) {
                    if (response.isSuccessful) {
                        val response = response.body()
                        if (response != null) {
                             temp = response.main.temp.toInt().toString()
                             humidity = response.main.humidity.toString()
                             desc = response.weather[0].description
                            binding.temperature.text = "$temp / $humidity"
                            binding.weatherDesc.text = desc

                        }
                    }
                }

                override fun onFailure(call: Call<WHATHER>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })


        //한파 쉼터 버튼 클릭
        binding.coldShelters.setOnClickListener {
            val intent = Intent(this, ColdShelterActivity::class.java)
            startActivity(intent)
        }

        //무더위 쉼터 버튼 클릭
        binding.heatShelters.setOnClickListener {
            val intent = Intent(this, HeatShelterActivity::class.java)
            startActivity(intent)
        }


    }

}