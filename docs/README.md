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
