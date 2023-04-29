package com.app.weatherhaven.retrofit

import com.google.gson.annotations.SerializedName


data class HeatShelterList(
    var HeatShelter : List<HeatShelter>
)

data class HeatShelter(
    @SerializedName("equp_type") var equpType : Int, // 시설유형
    @SerializedName("r_area_nm") var areaNm : String, // 쉼터명칭
    @SerializedName("r_detl_add") var detlAdd : String, // 도로명주소
    @SerializedName("use_prnb") var usePrnb : Int, //이용가능인원
    @SerializedName("cler1_cnt") var clerCnt1 : Int, //선풍기보유대수
    @SerializedName("cler2_cnt") var clerCnt2 : Int, //에어컨보유대수
    @SerializedName("lo") var lo : Double, //경도
    @SerializedName("la") var la : Double //위도
)



