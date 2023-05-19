# weatherhaven
서울시 열린데이터 광장 공공데이터 활용 서울시 쉼터&날씨 정보 서비스

개발기간 : 2023.04.30 ~ 2023.5.1

사용 언어 및 라이브러리 : Kotlin, Coroutine , Retrofit2 , Naver Cloud Platform Map API, vworld 오픈 API,openweathermap API

[기능]

• 현재 서울시 날씨 정보 제공: 서울시의 현재 기온, 습도, 날씨의 상태(흐림, 비, 맑음 등)의 정보를 제공합니다.

• 네이버 Map API를 통해 쉼터의 위치 표기: 제공되는 데이터 값 중 상세 주소와 위도 경도 값을 통해 네이버 Map에 쉼터의 위치를 표기해 사용자가 쉽게 위치를 알 수 있도록 합니다.

• 사용자 위치 기반 주변 정보 제공: 현재 위치를 기반으로 근처의 무더위 쉼터와 한파 쉼터의 위치를 제공합니다.

• 쉼터 상세 정보 제공: 선택한 무더위 쉼터와 한파 쉼터의 상세 정보(이용 가능한 인원, 쉼터 명, 주소, 난방 기구 및 냉방 기구의 개수 및 종류)를 제공합니다.
![메인화면](https://github.com/jininim/weatherhaven/assets/91578450/bb6b8bfa-95cd-4f5a-bc50-edeb3451e6cc)
![무더위쉼터](https://github.com/jininim/weatherhaven/assets/91578450/5945c80b-cac1-4dc0-a8c5-95347e42cdd9)
![한파쉼터](https://github.com/jininim/weatherhaven/assets/91578450/475153da-af97-43d2-a5e0-5bc55f3d73aa)


