package com.app.weatherhaven

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.app.weatherhaven.databinding.ActivityHeatShelterBinding
import com.app.weatherhaven.retrofit.heatShelter.RetrofitManager.myApi
import com.app.weatherhaven.retrofit.heatShelter.TbGtnHwcwP
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HeatShelterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHeatShelterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeatShelterBinding.inflate(layoutInflater)

        val call: Call<TbGtnHwcwP> = myApi.getData()
        call.enqueue(object : Callback<TbGtnHwcwP>{
            override fun onResponse(
                call: Call<TbGtnHwcwP>,
                response: Response<TbGtnHwcwP>
            ) {
                //성공 했을때
                if (response.isSuccessful) {
                    response.body()?.let{
                        Log.d("giiiiiiiiiiiiiiiiiiiiiii",it.row.toString())
                    }
                }
            }

            override fun onFailure(call: Call<TbGtnHwcwP>, t: Throwable) {
                //실패 했을때
                Log.d("YMC", "onFailure 에러: " + t.message.toString());
            }

        })
        setContentView(binding.root)


    }
}