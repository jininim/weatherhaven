package com.app.weatherhaven

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.app.weatherhaven.databinding.ActivityHeatShelterBinding
import com.app.weatherhaven.retrofit.HeatShelter
import com.app.weatherhaven.retrofit.HeatShelterList
import com.app.weatherhaven.retrofit.RetrofitManager.myApi
import com.app.weatherhaven.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HeatShelterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHeatShelterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeatShelterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val call: Call<HeatShelterList> = myApi.getData("4d5365574f68616b33355675786c58","json","TbGtnHwcwP",1,1000)
        call.enqueue(object : Callback<HeatShelterList>{
            override fun onResponse(
                call: Call<HeatShelterList>,
                response: Response<HeatShelterList>
            ) {
                //성공 했을때
                if (response.isSuccessful) {
                    val data: HeatShelterList? = response.body()
                    Log.d("giiiiiiiiiiiiiiii",data.toString())
                }
            }

            override fun onFailure(call: Call<HeatShelterList>, t: Throwable) {
                //실패 했을때
                Log.d("YMC", "onFailure 에러: " + t.message.toString());
            }

        })
    }
}