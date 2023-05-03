package com.app.weatherhaven


import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.app.weatherhaven.databinding.ActivityColdShelterBinding
import com.app.weatherhaven.retrofit.coldshelter.ColdDTO
import com.app.weatherhaven.retrofit.coldshelter.ColdModel
import com.app.weatherhaven.retrofit.coldshelter.ColdService
import com.app.weatherhaven.viewpager.ColdViewPagerAdapter
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

class ColdShelterActivity : AppCompatActivity(), OnMapReadyCallback, Overlay.OnClickListener {

    private lateinit var naverMap: NaverMap
    private lateinit var binding: ActivityColdShelterBinding
    private val mapView: MapView by lazy {
        binding.mapView
    }
    private val viewPager: ViewPager2 by lazy {
        binding.coldViewPager
    }
    private val coldViewPagerAdapter =  ColdViewPagerAdapter(itemClicked = {

    })
   val bottomSheetTitleTextView: TextView by lazy {
        binding.bottomSheet.bottomSheetTitleTextView
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityColdShelterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        viewPager.adapter = coldViewPagerAdapter
    }

    override fun onMapReady(map: NaverMap) { // 네이버 맵 객체 얻어오기
        naverMap = map

        naverMap.maxZoom = 18.0 // 최대 줌
        naverMap.minZoom = 10.0 // 최소 줌

        val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.497898550942466, 127.02768639039702)) // 초기 화면 설정
        naverMap.moveCamera(cameraUpdate)

        getColdShelterFromApi() // 레트로핏 정보 가져오기
    }

    private fun updateMarker(coldShelters: List<ColdModel>){ // 마커 찍기
        coldShelters.forEach{ cold ->
            val marker = Marker() // 마커 객체 생성
//            marker.position = LatLng(cold.lat, cold.lng) // 마커 위치 설정
            marker.onClickListener = this // 마커 클릭 시 이벤트 -> onClick 함수
            marker.map = naverMap // 네이버 맵 사용
            marker.tag = cold.r_seq_no // 마커 고유 번호
            marker.icon = MarkerIcons.BLACK // 마커 아이콘
            marker.iconTintColor = Color.RED // 마커 색깔
        }
    }

    private fun getColdShelterFromApi(){ // 레트로핏 정보 가져오기
        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io") // mocky 에 저장된 json 데이터를 가져오기위한 mocky url
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ColdService::class.java).also { // ColdService 연동해서 retrofit으로 데이터 가져오기
            it.getColdList()
                .enqueue(object : Callback<ColdDTO>{
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<ColdDTO>, response: Response<ColdDTO>) { // 성공 시
                        if(response.isSuccessful.not()){ // 실패했는지 한번더 검열
                            return
                        }

                        response.body()?.let{ dto -> // 가져온 데이터로 마커찍기
                            //updateMarker(dto.DATA)
                            coldViewPagerAdapter.submitList(dto.DATA)
                            bottomSheetTitleTextView.text = "${dto.DATA.size}개의 쉼터"
                        }
                    }

                    override fun onFailure(call: Call<ColdDTO>, t: Throwable) {
                        // 실패 시 해야할 것
                    }
                })
        }
    }




    /*private fun convertXYToLatLng(x: Double, y: Double): DoubleArray{

    }*/

    override fun onClick(p0: Overlay): Boolean { // 마커 클릭 시 이벤트 함수
        TODO("Not yet implemented")
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