package com.app.weatherhaven.retrofit.coldshelter

data class ColdModel(
    val c_end_wd: String, // 한파쉼터주말종료시간
    val r_seq_no: String, // 시설코드
    val c_strt: String, // 한파쉼터평일시작시간
    val c_strt_wd: String, // 한파쉼터주말시작시간
    val c_end: String, // 한파쉼터평일종료시간
    val c_dt_strt: String, // 한파쉼터운영시작일
    val c_dt_end: String, // 한파쉼터운영종료일
    val use_yn: String, // 사용여부
    val chk1_yn: String, // 점검사항(냉방기기구비)
    val chk2_yn: String, // 점검사항(휴식공간)
    val chk3_yn: String, // 점검사항(야간개방)
    val chk4_yn: String, // 점검사항(적정온도)
    val chk5_yn: String, // 점검사항(전기료지원)
    val chk6_yn: String, // 점검사항(간판부착)
    val chk7_yn: String, // 점검사항(홍보물비치)
    val chk8_yn: String, // 점검사항(숙박가능)
    val chk9_yn: String, // 점검사항(휴일개방)
    val year: String, // 년도
    val g2_ymin: Double, // Y좌표최소값
    val g2_ymax: Double, // Y좌표최대값
    val g2_xmin: Double, // X좌표최소값
    val g2_xmax: Double, // X좌표최대값
    val g2_id: String, // 공간정보관리번호
    val equp_type: String, // 시설유형
    val r_area_sqr: String, // 면적
    val r_area_nm: String, // 쉼터명칭
    val r_detl_add: String, // 상세주소
    val use_prnb: Int, // 이용가능인원
    val heat1_cnt: String, // 난방기보유(열풍기)
    val heat2_cnt: String, // 난방기보유(히터)
    val heat3_cnt: String, // 난방기보유(난로)
    val heat4_cnt: String // 난방기보유(라디에이터)
)
