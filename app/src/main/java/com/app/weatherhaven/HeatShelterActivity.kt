package com.app.weatherhaven

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.app.weatherhaven.databinding.ActivityHeatShelterBinding
import com.app.weatherhaven.retrofit.heatshelter.DATA
import com.app.weatherhaven.retrofit.heatshelter.RetrofitService

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HeatShelterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHeatShelterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeatShelterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://openapi.seoul.go.kr:8088/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(RetrofitService::class.java).also {
            it.getData(1,5)
                .enqueue(object : Callback<DATA>{
                    override fun onResponse(
                        call: Call<DATA>,
                        response: Response<DATA>
                    ) {
                        //성공 했을때
                        if (response.isSuccessful) {
                                val response = response.body()
                            if(response != null){
                                val data = response.TbGtnHwcwP.row
                                Log.d("sadasdada", data[4].toString())
                            }
                        }
                    }

                    override fun onFailure(call: Call<DATA>, t: Throwable) {
                        //실패 했을때
                        Log.d("YMC", "onFailure 에러: " + t.message.toString())
                    }

                })
        }

    }
}