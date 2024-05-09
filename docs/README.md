# 요구사항

- [ ] 사용자 도메인 추가.
  - [ ] 로그인 기능 구현.
    - [x] GET /login 요청 시 로그인 폼이 있는 페이지를 응답.
    - [ ] POST /login 요청 시 로그인 폼에 입력한 email, password 값을 body에 포함.
    - [ ] 응답 Cookie에 "token"값으로 토큰 포함.
  - [ ] 로그인 후 Cookie를 이용하여 사용자의 정보를 조회하는 API 구현.

# 기능 목록

- [X] 기존 API 입력값 검증 기능
- [X] 유저 페이지 라우팅 기능
- [X] 테마 도메인 추가
    - [X] 테마 전체 조회 기능
    - [X] 테마 추가 기능
    - [X] 테마 삭제 기능
- [X] 사용자 예약 가능 시간 조회 기능
- [X] 사용자 페이지 방탈출 예약 기능
- [X] 인기 테마 조회 기능

# 추가된 API 명세서

### 테마 전체 조회

```http request
GET http://localhost:8080/themes
Content-Type: application/json
```

### 테마 생성

```http request
POST http://localhost:8080/themes
Content-Type: application/json

{
  "name": "",
  "description": "",
  "thumbnail": ""
}
```

### 테마 삭제

```http request
DELETE http://localhost:8080/themes/{{themeId}}
```

### 예약 가능 시간 조회

```http request
GET http://localhost:8080/available-reservation-times?
    date={{$random.alphanumeric(8)}}&
    theme-id={{$random.integer(100)}}
```

### 인기 테마 조회

```http request
GET http://localhost:8080/popular-themes
```

### 로그인

```http request
Request
POST /login HTTP/1.1
content-type: application/json
host: localhost:8080

{
"password": "password",
"email": "admin@email.com"
}
```
