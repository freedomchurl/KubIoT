# KubIoT
2020 오픈인프라 경진대회 - 밴인사이드 팀의 프로젝트입니다.

"Kubernetes/KubeEdge 를 이용한 대규모 IoT 네트워크 통합 플랫폼 : Industry 4.0 과 IIoT 를 위한 인프라 구축"을 진행합니다.


실행 파일

1) `git pull " git repository "`

-> Spring Boot Project 에서 메이븐 빌드로 jar 파일을 생성 후, 각각의 이름에 맞는 폴더에 넣어준다.

( Anaylzer, Collector, Controller 의 경우 )

2) Go to FinalKubiot

3)`` docker build -t "container name"``

> container name 예시 : dockerFile 을 보고, controllerFile 인 경우 이미지 네임은 controller

4) 데이터 베이스 구축

- Redis -> password : dlcjf2779!

- MySql -> password : dlcjf2779!

  - 테이블 초기 생성은 db.sh의 명령문 실행.

  5)  이미지 컨테이너 실행

  ~/FinalKubiot 홈페이지로 이동 후,

  `docker-compose up -d` 를 통해 실행

  6) 웹페이지 접속 : "서버IP" PORT 7676 번 포트.

  7) 테스트 클라이언트

  ​	-  HTTP 이미지 : ImageWebClient ( Run with spring boot )

      -  HTTP 실수형 : TestWebClient (Run with spring boot)
      -  MQTT 이미지 : ImageMQTTClient ( Run with spring boot )
      -  MQTT 실수형 : TestMQTTClient 폴더에서 node floatMQTTClient.js
      -  안드로이드 : KubIoTAndroid 폴더 ( 안드로이드 스튜디오 프로젝트 )

  8) HTTP 프로토콜

  - 실수형 : /sendFData
    - POST
    - Request : JSON : (string) deviceId, type, data double 배열, time String 배열, regi (String 0 or 1)
    - Return : JSON : status  0

  - 이미지 : sendBData
    - POST
    - JSON {deviceId, type, regi, time} !@ 이미지BYTEarr



  9) MQTT 프로토콜

  \-   실수형 : JSON {deviceId, type, regi, data 배열, time 배열}

  \-   이미지 : JSON {deviceId, type, regi, time} !@ 이미지BYTEarr



  => 공통으로 응답은 status : 0 , regi : 1 / 0
