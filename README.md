# 1단계

## 입력 형식에 따른 예외

- [x] 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때 400 응답
    - [x] 시작 시간이 `null`일 떄
    - [x] 시작 시간의 입력 형식이 `HH:mm`이 아닐 때
- [x] 예약 생성 시 예약자명이 유효하지 않은 값이 입력 되었을 때 400 응답
    - [x] 예약자명이 `null`일 때
    - [x] 예약자명이 빈 값일 때
- [x] 예약 생성시 날짜가 유효하지 않은 값이 입력 되었을 때 400 응답
    - [x] 날짜가 `null`일 때
    - [x] 날짜 형식이 `yyyy-MM-dd`가 아닐떄

## 서비스 정책에 따른 예외

- [x] 지나간 날짜와 시간에 대한 예약 생성을 할 수 없다 (400 응답)
- [x] 이미 존재하는 시간은 생성할 수 없다 (400 응답)
- [x] 중복 예약은 불가능하다 (400 응답)
    - [x] 예약의 시간과 날짜가 같으면 중복으로 간주한다
- [x] 특정 시간에 대한 예약이 존재한다면 그 시간은 삭제할 수 없다 (400 응답)

# 2단계

- [x] 테마 도메인을 추가한다
    - [x] 테마는 이름, 설명, 썸네일을 가진다
    - [x] db의 theme 테이블 구조는 다음과 같다
  ```angular2html
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  name        VARCHAR(255) NOT NULL,
  description VARCHAR(255) NOT NULL,
  thumbnail VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
  ```
- [x] 사용자 예약 시 원하는 테마를 선택할 수 있다.
- [x] 관리자가 테마를 관리할 수 있도록 기능을 추가한다
    - [x] 테마를 조회할 수 있다
  ```http request
  GET /themes HTTP/1.1
  ```
  ```http request
  HTTP/1.1 200 
  Content-Type: application/json
  
  [
     {
          "id": 1,
          "name": "레벨2 탈출",
          "description": "우테코 레벨2를 탈출하는 내용입니다.",
          "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
      }
  ]
  ```
    - [x] 테마를 생성할 수 있다.
  ```http request
  POST /themes HTTP/1.1
  content-type: application/json
  
  {
    "name": "레벨2 탈출",
    "description": "우테코 레벨2를 탈출하는 내용입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
  }
  ```
  ```http request
  HTTP/1.1 201
  Location: /themes/1
  Content-Type: application/json
  
  {
      "id": 1,
      "name": "레벨2 탈출",
      "description": "우테코 레벨2를 탈출하는 내용입니다.",
      "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
  }
   ```
    - [x] 테마를 삭제할 수 있다
  ```http request
  DELETE /themes/1 HTTP/1.1
  ```
  ```http request
  HTTP/1.1 204
  ```

# 3단계

- [x] /reservation 요청 시 사용자 예약 페이지를 응답한다
- [x] 인기 테마 조회 기능을 추가한다
    - [x] / 요청 시 인기 테마 페이지를 응답한다
    - [x] 최근 일주일을 기준으로 하여 해당 기간 내에 방문하는 예약이 많은 테마 10개를 조회한다
- [x] 사용자는 날짜와 테마를 선택하면 예약 가능한 시간을 확인할 수 있다
    - [x] 이미 예약된 시간의 경우 isAlreadyBooked를 true로 설정하여 응답한다
