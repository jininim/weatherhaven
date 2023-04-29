package com.app.weatherhaven.retrofit

import com.google.gson.annotations.SerializedName

data class ColdShelter(
    @SerializedName("EQUP_TYPE") var EQUP_TYPE : Int, // 시설유형
    @SerializedName("R_AREA_NM") var R_AREA_NM : String, // 쉼터명칭
    @SerializedName("R_DETL_ADD") var R_DETL_ADD : String, // 도로명주소
    @SerializedName("USE_PRNB") var USE_PRNB : Int, // 이용가능인원
    @SerializedName("HEAT1_CNT") var HEAT1_CNT : Int, // 선풍기보유대수
    @SerializedName("HEAT2_CNT") var HEAT2_CNT : Int, // 에어컨보유대수
    @SerializedName("HEAT3_CNT") var HEAT3_CNT : Int, // 에어컨보유대수
    @SerializedName("HEAT4_CNT") var HEAT4_CNT : Int, // 에어컨보유대수
    @SerializedName("G2_XMIN") var G2_XMIN : Double, // 경도
    @SerializedName("G2_YMIN") var G2_YMIN : Double // 위도
)
