# 방탈출 예약 관리

# 화면

- 어드민 메인 페이지: localhost:8080/admin
- 예약 관리 페이지: localhost:8080/admin/reservation
- 시간 관리 페이지: localhost:8080/admin/time
- 사용자 예약 페이지: localhost:8080/reservation
- 인기 테마 페이지: localhost:8080
- 로그인 페이지: localhost:8080/login

# API 명세

### 관리자 예약 목록 조회

```
Request
GET admin/reservations HTTP/1.1
Query Parameters: memberId, themeId, dateFrom, dateTo

Response
HTTP/1.1 200 
Content-Type: application/json
[
    {
        "id": "Long",
        "name": String,
        "date": LocalDate (YYYY-MM-DD),
        "reservationTime": {
            "id": Long,
            "startAt" : LocalTime (HH:mm)
        },
        "themeName": String
    }
]
```

### 예약 추가

```
Request
Content-Type: application/json
POST /reservations
{
    "date": LocalDate (YYYY-MM-DD),
    "timeId": Long,
    "themeId": Long
}

Response
Content-Type: application/json
HTTP/1.1 200 
{
    "id": Long,
    "name": String,
    "date": LocalDate (YYYY-MM-DD),
    "reservationTime": {
        "id": Long,
        "startAt" : LocalTime (HH:mm)
    },
    "themeName" : String
}

```

### 관리자 예약 추가

```
Request
Content-Type: application/json
POST admin/reservations
{
    "memberId": Long,
    "date": LocalDate (YYYY-MM-DD),
    "timeId": Long,
    "themeId": Long
}

Response
Content-Type: application/json
HTTP/1.1 200 
{
    "id": Long,
    "name": String,
    "date": LocalDate (YYYY-MM-DD),
    "reservationTime": {
        "id": Long,
        "startAt" : LocalTime (HH:mm)
    },
    "themeName" : String
}

```

### 예약 취소

```
Request
DELETE /reservations/{id} HTTP/1.1

Response
HTTP/1.1 200
```

### 시간 추가

```
Request
POST /times HTTP/1.1
content-type: application/json

{
    "startAt": LocalTime (HH:mm)
}

Response
HTTP/1.1 200
Content-Type: application/json

{
    "id": Long,
    "startAt": LocalTime (HH:mm)
}
```

### 시간 조회

```
Request
GET /times HTTP/1.1

Response
HTTP/1.1 200 
Content-Type: application/json

[
   {
        "id": Long,
        "startAt": LocalTime (HH:mm)
    }
]
```

### 시간 삭제

```
Request
DELETE /times/1 HTTP/1.1

Response
HTTP/1.1 200
```

### 테마 목록 조회

```
Request
GET /themes HTTP/1.1

Response
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

### 테마 추가

```
Request
POST /themes HTTP/1.1
content-type: application/json

{
    "name": "레벨2 탈출",
    "description": "우테코 레벨2를 탈출하는 내용입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}

Response
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

### 테마 삭제

```
Request
DELETE /themes/1 HTTP/1.1

Response
HTTP/1.1 204
```

### 예약 가능한 시간 조회

```
Request
GET /times/theme/{themeId}?date={} HTTP/1.1

Response
HTTP/1.1 200 
Content-Type: application/json

[
   {
        "id": Long,
        "startAt": LocalTime (HH:mm)
        "alreadyBooked": boolean
    }
]
```

### 인기 테마 조회

```
Request
GET /theme/top10

Response
HTTP/1.1 200 
Content-Type: application/json

[
   {
        "id": Number
        "name": String
        "description": String
        "thumbnail": String
    }
]
```