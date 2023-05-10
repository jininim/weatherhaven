package com.app.weatherhaven


import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.app.weatherhaven.HeatShelterActivity.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.app.weatherhaven.databinding.ActivityColdShelterBinding
import com.app.weatherhaven.retrofit.coldshelter.ColdDATA
import com.app.weatherhaven.retrofit.coldshelter.ColdRow
import com.app.weatherhaven.retrofit.coldshelter.ColdService
import com.app.weatherhaven.retrofit.lola.LOLA
import com.app.weatherhaven.retrofit.lola.LolaService
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.Utmk
import com.naver.maps.map.*

import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class ColdShelterActivity : AppCompatActivity(), OnMapReadyCallback, Overlay.OnClickListener {


    private lateinit var naverMap: NaverMap

    private var PERMISSION = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private lateinit var binding: ActivityColdShelterBinding

    //런타임 권한 처리
    private lateinit var locationSource: FusedLocationSource



    private var dataList: MutableList<List<ColdRow>> = mutableListOf()
    private var start = 1
    private var pageSize = 10
    private var totalCount = 0

    private val infoWindow = InfoWindow() //정보 창

    //레트로핏 빌더
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://openapi.seoul.go.kr:8088/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofit2 = Retrofit.Builder()
        .baseUrl("http://api.vworld.kr")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService2: LolaService = retrofit2.create(LolaService::class.java)


    private val mapView: MapView by lazy {
        binding.mapView
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityColdShelterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //맵 생성
        mapView.onCreate(savedInstanceState)
        //맵 객체 받아오기
        mapView.getMapAsync(this)
        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        //api 호출
        val apiService = retrofit.create(ColdService::class.java)
        //total count 가져오기
        apiService.getData(1, 1).enqueue(object : Callback<ColdDATA> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<ColdDATA>, response: Response<ColdDATA>) {
                if (response.isSuccessful) {
                    val headerBody = response.body()
                    if (headerBody != null) {
                        //totalCount를 구함
                        totalCount = headerBody.TbGtnCwP.list_total_count
                        //totalCount 만큼 레트로핏 api 호출
                        CoroutineScope(Dispatchers.IO).launch {
                            while (start <= totalCount) {
                                val end = minOf(start + pageSize - 1, totalCount)
                                apiService.getData(start, end).enqueue(object : Callback<ColdDATA> {
                                    @SuppressLint("SetTextI18n")
                                    override fun onResponse(
                                        call: Call<ColdDATA>,
                                        response: Response<ColdDATA>
                                    ) {
                                        //성공 했을때
                                        if (response.isSuccessful) {
                                            val response = response.body()
                                            if (response != null) {
                                                val data = response.TbGtnCwP.row
                                                dataList.add(data)
                                            }
                                            start += pageSize
                                        }

                                    }

                                    override fun onFailure(call: Call<ColdDATA>, t: Throwable) {
                                        //실패 했을때
                                        Log.d("YMC", "onFailure 에러: " + t.message.toString())
                                    }

                                })
                            }
                            withContext(Dispatchers.Main) {
                                updateMarker(dataList)
                                binding.bottomSheetTitleTextView.text = "${totalCount}개의 쉼터"
                            }
                        }


                    }
                }
            }

            override fun onFailure(call: Call<ColdDATA>, t: Throwable) {
                //실패 했을때
                Log.d("YMC", "onFailure 에러: " + t.message.toString())
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions,
                grantResults
            )
        ) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            } else {
                naverMap.locationTrackingMode = LocationTrackingMode.Follow
                val locationOverlay = naverMap.locationOverlay
                locationOverlay.isVisible = true
            }

            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }



    private fun updateMarker(datas: MutableList<List<ColdRow>>) {

        datas.forEach { data ->
            data.forEach { row ->
                //도로명주소를 위도경도로 변환
                apiService2.getData(address = row.R_DETL_ADD).enqueue(object : Callback<LOLA> {
                    override fun onResponse(call: Call<LOLA>, response: Response<LOLA>) {
                        if (response.isSuccessful) {
                            val rb = response.body()
                            if (rb != null) {
                                val result = rb.response.result
                                if (result != null) {
                                    val lola = result.point
                                     val x = lola.x.toDouble()
                                     val y = lola.y.toDouble()
                                    val marker = Marker()
                                    marker.position = LatLng(y, x)

                                    marker.map = naverMap
                                    //마커 태그
                                    marker.tag = "수용 가능 인원 ${row.USE_PRNB.toInt()}명\n" +
                                            "히터 보유대수 ${row.HEAT2_CNT.toInt()}개\n" +
                                            "난로 보유대수 ${row.HEAT3_CNT.toInt()}개\n" +
                                            "열풍기 보유대수 ${row.HEAT1_CNT.toInt()}개\n" +
                                            "라디에이터 보유대수 ${row.HEAT4_CNT.toInt()}개"

                                    marker.width = 80 // 마커 크기 가로
                                    marker.height = 110// 마커 크기 세로
                                    marker.captionText = row.R_AREA_NM //마커 하단 텍스트
                                    marker.icon = MarkerIcons.BLACK //마커 아이콘
                                    marker.iconTintColor = Color.BLUE // 마커 색

                                    //마커 태그 표시
                                    infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(this@ColdShelterActivity) {
                                        override fun getText(infoWindow: InfoWindow): CharSequence {
                                            return infoWindow.marker?.tag as CharSequence? ?: ""
                                        }
                                    }
                                    marker.setOnClickListener {
                                        val marker = it as Marker
                                        //마커 위치에 따른 카메라 이동
                                        val cameraUpdate =
                                            CameraUpdate.scrollTo(LatLng(marker.position.latitude, marker.position.longitude))
                                                .animate(CameraAnimation.Fly, 1000)
                                        naverMap.moveCamera(cameraUpdate)

                                        if (marker.infoWindow == null) {
                                            // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                                            infoWindow.open(marker)
                                        } else {
                                            // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                                            infoWindow.close()
                                        }
                                        return@setOnClickListener true
                                    }


                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<LOLA>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })

            }
        }
    }
    override fun onMapReady(map: NaverMap) { // 네이버 맵 객체 얻어오기
        naverMap = map
        ActivityCompat.requestPermissions(
            this, PERMISSION,
            LOCATION_PERMISSION_REQUEST_CODE
        )
        //로케이션 버튼
        binding.currentLocationButton.map = naverMap
        // 현재 위치 받아오기
        naverMap.locationSource = locationSource
        val uiSetting = naverMap.uiSettings // 현위치 버튼
        uiSetting.isLocationButtonEnabled = false
        naverMap.maxZoom = 18.0 // 최대 줌
        naverMap.minZoom = 10.0 // 최소 줌







    }


    override fun onClick(p0: Overlay): Boolean { // 마커 클릭 시 이벤트 함수
        val marker = p0 as Marker
        //마커 위치에 따른 카메라 이동
        val cameraUpdate =
            CameraUpdate.scrollTo(LatLng(marker.position.latitude, marker.position.longitude))
                .animate(CameraAnimation.Fly, 1000)
        naverMap.moveCamera(cameraUpdate)

        if (marker.infoWindow == null) {
            // 현재 마커에 정보 창이 열려있지 않을 경우 엶
            infoWindow.open(marker)
        } else {
            // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
            infoWindow.close()
        }

        return true
    }

    // 지도는 생명주기가 중요하기 때문에 아래 생명주기를 모두 추가함
    override fun onStart() { // 액티비티 시작
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() { // 액티비티 재개
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() { // 액티비티 일시중지
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) { // 액티비티 데이터 유지
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() { // 액티비티 중단
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() { // 액티비티 소멸
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() { // 메모리 부족
        super.onLowMemory()
        mapView.onLowMemory()
    }
}