package com.app.weatherhaven

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.app.weatherhaven.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //현재 날씨, 체감온도, 날씨 상태 크롤링
        CoroutineScope(Dispatchers.IO).launch {
            val doc = Jsoup.connect("https://www.weather.com/ko-KR/weather/today/l/KSXX0037:1:KS").get()
            //날씨 + 체감온도
            val temperature = doc.select(".CurrentConditions--tempValue--MHmYY").text()+ "/ " + doc.select(".TodayDetailsCard--feelsLikeTempValue--2icPt").text()
            //날씨 상태
            val weatherDesc = doc.select(".CurrentConditions--phraseValue--mZC_p").text()
            withContext(Dispatchers.Main) {
                binding.temperature.text = temperature
                binding.weatherDesc.text = weatherDesc
                //텍스트를 받아오면 화면에 보이게
                binding.todayweather.isVisible = true
                binding.temperature.isVisible = true
                binding.weatherDesc.isVisible =true
                binding.textView7.isVisible = true
            }
        }
        //한파 쉼터 버튼 클릭
        binding.coldShelters.setOnClickListener {
            val intent = Intent(this,ColdShelterActivity::class.java)
            startActivity(intent)
        }

        //무더위 쉼터 버튼 클릭
        binding.heatShelters.setOnClickListener {
            val intent = Intent(this, HeatShelterActivity::class.java)
            startActivity(intent)
        }



    }
}