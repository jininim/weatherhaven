package com.app.weatherhaven.retrofit.heatShelter


import com.google.gson.annotations.SerializedName


data class TbGtnHwcwP(
    @SerializedName("list_total_count") val listTotalCount: Int,
    @SerializedName("RESULT") val result: Result,
    @SerializedName("row") val row: MutableList<Row>

)
data class Result(
    @SerializedName("CODE") val code: String,
    @SerializedName("MESSAGE") val message: String

)
data class Row(
    @SerializedName("R_SEQ_NO") val r_seq_no : String, //시설번호
    @SerializedName("YEAR") val year : String, //년도
    @SerializedName("AREA_CD") val area_cd : String, //지역코드
    @SerializedName("EQUP_TYPE") val equpType : String, // 시설유형
    @SerializedName("R_AREA_NM") val areaNm : String, // 쉼터명칭
    @SerializedName("R_DETL_ADD") val detlAdd : String, // 도로명주소
    @SerializedName("J_DETL_ADD") val j_detl_add : String, //지번상세주소
    @SerializedName("R_AREA_SQR") val r_area_sqr : String,// 면적
    @SerializedName("USE_PRNB") val usePrnb : Double, //이용가능인원
    @SerializedName("CLER1_CNT") val clerCnt1 : Double, //선풍기보유대수
    @SerializedName("CLER2_CNT") val clerCnt2 : Double, //에어컨보유대수
    @SerializedName("CHK1_YN") val chk1_yn : String, //야간개방
    @SerializedName("CHK2_YN") val chk2_yn : String, //휴일개방
    @SerializedName("CHK3_YN") val chk3_yn : String, //숙박가능여부
    @SerializedName("CRE_DTTM") val cre_dttm : String, //입력시간
    @SerializedName("UPDT_DTTM") val updt_dttm : String, //수정시간
    @SerializedName("USE_YN") val use_yn : String, //사용여부
    @SerializedName("RMRK") val rmrk : String, //비고
    @SerializedName("DT_START") val dt_start : String, //운영시작일
    @SerializedName("DT_END") val dt_end : String, // 운영종료일
    @SerializedName("LO") val lo : String, //경도
    @SerializedName("LA") val la : String, //위도
    @SerializedName("XX") val xx : String, //x좌표
    @SerializedName("YY") val yy : String, //y좌표

)



