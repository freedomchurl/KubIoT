# Redis 문서

## redis 키는 아래와 같다

* {ID}:input
* {ID}:control
* {gID}:group:control

의 3가지가 존재함

- 현재는 1:control / 2:control / 1:input / 2:input 으로 담아 놓았음

## 백업 Flow

1. MySQL을 통하여 모든 user와 group의 key를 가져온다
2. Redis에서 multi를 통하여 데이터를 가져오고, 각각



