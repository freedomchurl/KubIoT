# 대규모 IoT 네트워크 통합 플랫폼 : Industry 4.0 과 IIoT 를 위한 인프라 구축


2020 오픈인프라 경진대회 - 밴인사이드 팀의 프로젝트입니다.

"대규모 IoT 네트워크 통합 플랫폼 : Industry 4.0 과 IIoT 를 위한 인프라 구축"을 진행합니다.

자세한 설명은 [PPT](https://github.com/freedomchurl/KubIoT/blob/master/%EB%8C%80%EA%B7%9C%EB%AA%A8_IoT_%EB%84%A4%ED%8A%B8%EC%9B%8C%ED%81%AC_%ED%86%B5%ED%95%A9%ED%94%8C%EB%9E%AB%ED%8F%BC_%EB%B0%9C%ED%91%9C%EC%9E%90%EB%A3%8C.pptx)를 참고해주세요.



---



## 주요 기능

- HTTP와 MQTT 로 실수형 데이터, 이미지 데이터를 다루는 IoT 기기를 대상으로 합니다.

- 기능

  - 데이터 수집 기능 

    * 수집한 데이터는 네이버 클라우드 플랫폼의 Object Storage와 연동이 가능합니다.

  - IoT 기기 제어 기능

    *  웹 대시보드를 통해 원하는 제어 명령을 IoT 기기로 전송 가능합니다.

  - 데이터 분석 기능

    * 이상 데이터 범위를 등록하면, 이상 데이터가 들어왔을 시 기록됩니다.

    * API 를 이용하여, K-means, Logistic Regression 등의 분석이 가능합니다.

  -  안드로이드 Push 알림 기능

    * 분석 서비스에서 이상 데이터 감지 시, 어플로 Push 알림을 제공합니다.

  - 웹 대시보드 기능

    * 관리자는 웹 대시보드를 통해 플랫폼 이용이 가능합니다.
    * 장치 리스트, 분석 현황 확인, 그룹 관리, 알림 리스트 등을 확인 가능합니다. 

  

![대시보드](https://github.com/freedomchurl/KubIoT/blob/master/%EB%8C%80%EC%8B%9C%EB%B3%B4%EB%93%9C%EC%9D%B4%EB%AF%B8%EC%A7%80.png)

___



## 실행환경 구축

1) `git pull " git repository "`

-> Spring Boot Project 에서 메이븐 빌드로 jar 파일을 생성 후, 각각의 이름에 맞는 폴더에 넣어준다.

( Anaylzer, Collector, Controller 의 경우 )

2) Go to FinalKubiot

3) `` docker build -t "container name"``

> container name 예시 : dockerFile 을 보고, controllerFile 인 경우 이미지 네임은 controller

4) 데이터 베이스 구축

- Redis -> password : dlcjf2779!

- MySql -> password : dlcjf2779!

  - 테이블 초기 생성은 db.sh의 명령문 실행.

5)  이미지 컨테이너 실행

~/FinalKubiot 홈페이지로 이동 후,

`docker-compose up -d` 를 통해 실행

6) 이미지 컨테이너 종료

``docker-compose down``

7) 웹페이지 접속 : "서버IP" PORT 7676 번 포트.

8) 테스트 클라이언트

​	-  HTTP 이미지 : ImageWebClient ( Run with spring boot )

    -  HTTP 실수형 : TestWebClient (Run with spring boot)
    -  MQTT 이미지 : ImageMQTTClient ( Run with spring boot )
    -  MQTT 실수형 : TestMQTTClient 폴더에서 node floatMQTTClient.js
    -  안드로이드 : KubIoTAndroid 폴더 ( 안드로이드 스튜디오 프로젝트 )

9) HTTP 프로토콜

( {} 로 표시된 부분은 JSON 데이터를 의미 )

- 실수형 : /sendFData
  - POST { (string) deviceId, type, data double 배열, time String 배열, regi (String 0 or 1) }
  
- 이미지 : /sendBData
  - POST {deviceId, type, regi, time} !@ 이미지BYTEarr

  => 공통응답 { status : 0 , regi : 1 / 0 }



  10) MQTT 프로토콜

  \-   실수형 : JSON {deviceId, type, regi, data 배열, time 배열}

  \-   이미지 : JSON {deviceId, type, regi, time} !@ 이미지BYTEarr

  => 공통응답 { status : 0 , regi : 1 / 0 }



---



## 서비스 아키텍쳐

* 각각의 서비스가 마이크로 서비스로 구성된 MSA 구조.
* 각각의 서비스를 Docker를 이용하여 컨테이너화 하였습니다.

![전체구조](https://github.com/freedomchurl/KubIoT/blob/master/%EC%A0%84%EC%B2%B4%EA%B5%AC%EC%A1%B0.PNG)