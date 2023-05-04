package com.app.weatherhaven


import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.app.weatherhaven.databinding.ActivityColdShelterBinding
import com.app.weatherhaven.retrofit.coldshelter.ColdDATA
import com.app.weatherhaven.retrofit.coldshelter.ColdRow
import com.app.weatherhaven.retrofit.coldshelter.ColdService
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.Utmk

import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
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

class ColdShelterActivity : AppCompatActivity(), OnMapReadyCallback, Overlay.OnClickListener {


    private lateinit var naverMap: NaverMap

    private lateinit var binding: ActivityColdShelterBinding

    //런타임 권한 처리
    private lateinit var locationSource: FusedLocationSource

    private var dataList : MutableList<List<ColdRow>> = mutableListOf()
    private var start = 1
    private var pageSize = 100
    private var totalCount = 0

    private val infoWindow = InfoWindow() //정보 창

    //레트로핏 빌더
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://openapi.seoul.go.kr:8088/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val mapView: MapView by lazy {
        binding.mapView
    }


   val bottomSheetTitleTextView: TextView by lazy {
        binding.bottomSheet.bottomSheetTitleTextView
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityColdShelterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //맵 생성
        mapView.onCreate(savedInstanceState)
        //맵 객체 받아오기
        mapView.getMapAsync(this)

        //api 호출
        val apiService = retrofit.create(ColdService::class.java)

        //total count 가져오기
        apiService.getData(1,1).enqueue(object: Callback<ColdDATA>{
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<ColdDATA>, response: Response<ColdDATA>) {
                if (response.isSuccessful) {
                    val headerBody = response.body()
                    if(headerBody != null){
                        //totalCount를 구함
                        totalCount = headerBody.TbGtnCwP.list_total_count
                        //totalCount 만큼 레트로핏 api 호출
                        CoroutineScope(Dispatchers.IO).launch {
                            while (start <= totalCount){
                                val end = minOf(start + pageSize - 1, totalCount)
                                apiService.getData(start,end).enqueue(object : Callback<ColdDATA>{
                                    @SuppressLint("SetTextI18n")
                                    override fun onResponse(
                                        call: Call<ColdDATA>,
                                        response: Response<ColdDATA>
                                    ) {
                                        //성공 했을때
                                        if (response.isSuccessful) {
                                            val response = response.body()
                                            if(response != null){
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
                            withContext(Dispatchers.Main){
                                updateMarker(dataList) // 마커찍기
                                bottomSheetTitleTextView.text = "${totalCount}개의 쉼터"
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
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
    override fun onMapReady(map: NaverMap) { // 네이버 맵 객체 얻어오기
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


//        val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.497898550942466, 127.02768639039702)) // 초기 화면 설정
//        naverMap.moveCamera(cameraUpdate)

    }

    private fun updateMarker(datas: MutableList<List<ColdRow>>) { // 마커 찍기
        datas.forEach { data ->
            data.forEach { row->
                val marker = Marker()
                //마커 태그
                marker.tag = "수용 가능 인원 ${row.USE_PRNB.toInt()}명\n" +
                        "선풍기 보유대수 ${row.HEAT1_CNT.toInt()}개\n" +
                        "에어컨 보유대수 ${row.HEAT2_CNT.toInt()}개"
                marker.width = 80 // 마커 크기 가로
                marker.height = 110// 마커 크기 세로
                marker.captionText = row.R_AREA_NM //마커 하단 텍스트
                val utmk = Utmk(row.G2_XMAX.toDouble()/100, row.G2_YMAX.toDouble()/100)
                val latLng = utmk.toLatLng()
                Log.d("giiiiiiiiiiiiiiiiiiiiiiii",latLng.toString())
                marker.position = LatLng(latLng.latitude, latLng.longitude)
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




    override fun onClick(p0: Overlay): Boolean { // 마커 클릭 시 이벤트 함수
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