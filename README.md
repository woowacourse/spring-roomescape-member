# application을 직접 실행하여 테스트 시

`resources/data.sql` 의 주석을 제거하면 초기데이터가 세팅된 채로 테스트해볼 수 있습니다!

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
  GET /themes HTTP/1.1
  ```
  ```http request
  HTTP/1.1 200 
  Content-Type: application/json
  
  [
     {
          "id": 1,
          "name": "레벨2 탈출",
          "description": "우테코 레벨2를 탈출하는 내용입니다.",
          "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
      }
  ]
  ```
    - [x] 테마를 생성할 수 있다.
  ```http request
  POST /themes HTTP/1.1
  content-type: application/json
  
  {
    "name": "레벨2 탈출",
    "description": "우테코 레벨2를 탈출하는 내용입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
  }
  ```
  ```http request
  HTTP/1.1 201
  Content-Type: application/json
  
  {
      "id": 1,
      "name": "레벨2 탈출",
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

# 4단계

- [x] 사용자 도메인을 추가한다
    - [x] 사용자는 이름을 가진다
    - [x] 사용자는 이메일을 가진다
    - [x] 사용자는 비밀번호를 가진다
- [x] 회원가입 API를 구현한다
  ```http request
    POST /members HTTP/1.1
    content-type: application/json
    host: localhost:8080
  
    {
        "name": "name",
        "email": "admin@email.com",
        "password": "password"
    }
  ```
  ```http request
    HTTP/1.1 200
          Content-Type: application/json
  
    {
        "id": 1,
        "name": "name",
        "email": "email"
    }
  ```

- [x] 로그인 API를 구현한다
    - [x] 로그인의 id로는 사용자의 이메일, password로는 사용자의 비밀번호를 사용한다
      ```http request
      POST /login HTTP/1.1
      content-type: application/json
      host: localhost:8080
    
      {
          "password": "password",
          "email": "admin@email.com"
      }
      ```
      ```http request
      HTTP/1.1 200 OK
      Content-Type: application/json
      Keep-Alive: timeout=60
      Set-Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI; Path=/; HttpOnly
      ```

- [x] 인증 정보 조회 API를 구현단다
  ```http request
  GET /login/check HTTP/1.1
  cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
  host: localhost:8080
  ```
  ```http request
  HTTP/1.1 200 OK
  Connection: keep-alive
  Content-Type: application/json
  Date: Sun, 03 Mar 2024 19:16:56 GMT
  Keep-Alive: timeout=60
  Transfer-Encoding: chunked
  
  {
      "name": "어드민"
  }
  ```
- [x] 로그아웃 API를 구현한다

- [x] `GET /login` 요청시 로그인 폼 페이지를 응답한다
- [x] `GET /signup` 요청시 회원가입 폼 페이지를 응답한다

# 5단계

- [x] 사용자 예약 생성과 관리자 예약 생성 API를 분리한다
- [x] 사용자 예약 생성 시, 로그인한 사용자 정보를 활용한다
    - [x] 예약 객체가 사용자를 멤버로 가지도록 한다
- [x] 관리자 예약 생성 시, 사용자 id를 요청으로 받게한다

# 6단계

- [x] "admin/**" API 요청에 대해 관리자 계정만 접근 가능하도록 한다
- [x] 예약 전체 조회에 필터링 조건을 적용한다
    - [x] 멤버, 테마, 날짜 필터링 조건을 적용한다