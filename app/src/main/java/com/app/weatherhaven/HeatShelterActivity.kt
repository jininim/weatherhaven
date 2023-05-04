package com.app.weatherhaven

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.app.weatherhaven.databinding.ActivityHeatShelterBinding
import com.app.weatherhaven.retrofit.heatshelter.DATA
import com.app.weatherhaven.retrofit.heatshelter.RetrofitService
import com.app.weatherhaven.retrofit.heatshelter.Row
import com.app.weatherhaven.viewpager.ColdViewPagerAdapter
import com.app.weatherhaven.viewpager.HeatViewPagerAdapter
import com.google.gson.JsonObject
import com.naver.maps.geometry.LatLng
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

class HeatShelterActivity : AppCompatActivity(), OnMapReadyCallback, Overlay.OnClickListener {
    private lateinit var binding: ActivityHeatShelterBinding

    private lateinit var naverMap: NaverMap

    private var dataList : MutableList<List<Row>> = mutableListOf()
    private var start = 1
    private var pageSize = 100
    private var totalCount = 0
    //런타임 권한 처리
    private lateinit var locationSource: FusedLocationSource

    private val infoWindow = InfoWindow() //정보 창

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://openapi.seoul.go.kr:8088/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val mapView: MapView by lazy {
        binding.mapView
    }

    private val viewPager: ViewPager2 by lazy {
        binding.heatViewPager
    }

    private val heatViewPagerAdapter = HeatViewPagerAdapter(itemClicked = {

    })

    val bottomSheetTitleTextView: TextView by lazy {
        binding.bottomSheet.bottomSheetTitleTextView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeatShelterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //맵 생성
        mapView.onCreate(savedInstanceState)
        //맵 객체 받아오기
        mapView.getMapAsync(this)



        //위치 오버레이


        //뷰 페이저 어답터
        viewPager.adapter = heatViewPagerAdapter

        //api 호출
        val apiService = retrofit.create(RetrofitService::class.java)




        //total count 가져오기
            apiService.getData(1,1).enqueue(object: Callback<DATA>{
                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call<DATA>, response: Response<DATA>) {
                    if (response.isSuccessful) {
                        val response = response.body()
                        if(response != null){
                            //totalCount를 구함
                            totalCount = response.TbGtnHwcwP.list_total_count
                            //totalCount 만큼 레트로핏 api 호출
                            CoroutineScope(Dispatchers.IO).launch {
                                while (start <= totalCount){
                                    val end = minOf(start + pageSize - 1, totalCount)
                                    apiService.getData(start,end).enqueue(object : Callback<DATA>{
                                        @SuppressLint("SetTextI18n")
                                        override fun onResponse(
                                            call: Call<DATA>,
                                            response: Response<DATA>
                                        ) {
                                            //성공 했을때
                                            if (response.isSuccessful) {
                                                val response = response.body()
                                                if(response != null){
                                                    val data = response.TbGtnHwcwP.row
                                                    dataList.add(data)
                                                }
                                                start += pageSize
                                            }

                                        }
                                        override fun onFailure(call: Call<DATA>, t: Throwable) {
                                            //실패 했을때
                                            Log.d("YMC", "onFailure 에러: " + t.message.toString())
                                        }

                                    })
                                }
                                withContext(Dispatchers.Main){
                                    updateMarker(dataList) // 마커찍기
                                    bottomSheetTitleTextView.text = "${totalCount}개의 쉼터"
                                }
                            }


                        }
                    }
                }

                override fun onFailure(call: Call<DATA>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })












    }
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions,
                grantResults)) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }

            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }



    private fun updateMarker(datas: MutableList<List<Row>>) { // 마커 찍기
        datas.forEach { data ->
            data.forEach { row->
                val marker = Marker()
                //마커 태그
                marker.tag = "수용 가능 인원 ${row.USE_PRNB.toInt()}명\n" +
                        "선풍기 보유대수 ${row.CLER1_CNT.toInt()}개\n" +
                        "에어컨 보유대수 ${row.CLER2_CNT.toInt()}개"
                marker.width = 80 // 마커 크기 가로
                marker.height = 110// 마커 크기 세로
                marker.captionText = row.R_AREA_NM //마커 하단 텍스트
                marker.position = LatLng(row.LA.toDouble(), row.LO.toDouble()) // 마커 좌표
                marker.icon = MarkerIcons.BLACK //마커 아이콘
                marker.iconTintColor = Color.RED // 마커 색
                marker.map = naverMap
                //마커 태그 표시
                infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(this) {
                    override fun getText(infoWindow: InfoWindow): CharSequence {
                        return infoWindow.marker?.tag as CharSequence? ?: ""
                    }
                }
               marker.onClickListener = this
            }
        }
    }
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map
        naverMap.maxZoom = 18.0 // 최대 줌
        naverMap.minZoom = 10.0 // 최소 줌
        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        naverMap.locationSource = locationSource

        //위치 오버레이
        val locationOverlay = naverMap.locationOverlay
        locationOverlay.isVisible = true

        //위치 변경 이벤트
        naverMap.addOnLocationChangeListener { location ->
            Toast.makeText(this, "${location.latitude}, ${location.longitude}",
                Toast.LENGTH_SHORT).show()
        }


//        val cameraUpdate =
//            CameraUpdate.scrollTo(LatLng(37.497898550942466, 127.02768639039702)) // 초기 화면 설정
//        naverMap.moveCamera(cameraUpdate)
    }

    override fun onClick(p0: Overlay): Boolean {
        val marker = p0 as Marker
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