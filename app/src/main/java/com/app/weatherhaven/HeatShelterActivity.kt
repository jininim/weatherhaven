package com.app.weatherhaven

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.app.weatherhaven.databinding.ActivityHeatShelterBinding
import com.app.weatherhaven.retrofit.heatshelter.DATA
import com.app.weatherhaven.retrofit.heatshelter.RetrofitService
import com.app.weatherhaven.retrofit.heatshelter.Row
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.MarkerIcons
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HeatShelterActivity : AppCompatActivity(), OnMapReadyCallback, Overlay.OnClickListener  {
    private lateinit var binding: ActivityHeatShelterBinding
    private lateinit var naverMap: NaverMap
    private val mapView: MapView by lazy {
        binding.mapView
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeatShelterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://openapi.seoul.go.kr:8088/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(RetrofitService::class.java).also {
            it.getData(1,1000)
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
                                updateMarker(data)
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

    private fun updateMarker(datas: List<Row>){ // 마커 찍기
        datas.forEach{ row ->
            val marker = Marker()
            marker.position = LatLng(row.LA.toDouble(), row.LO.toDouble())
            marker.onClickListener = this
            marker.map = naverMap
            marker.icon = MarkerIcons.BLACK
            marker.iconTintColor = Color.RED
        }
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map

        naverMap.maxZoom = 18.0 // 최대 줌
        naverMap.minZoom = 10.0 // 최소 줌

        val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.497898550942466, 127.02768639039702)) // 초기 화면 설정
        naverMap.moveCamera(cameraUpdate)
    }

    override fun onClick(p0: Overlay): Boolean {
        TODO("Not yet implemented")
    }
}