# 방탈출 예약 관리

## 버전 관리

- JDK 21
- Gradle 8.13
- Spring Boot 3.4.4

## 요구사항 명세서

- [x] 예약 시간 검증
    - [x] 시작 시간은 LocalTime 형식을 만족시켜야 한다.
    - [x] 예약이 이미 존재하는 시간은 삭제할 수 없다.
    - [x] 중복된 시간 추가는 불가능하다.

- [x] 예약 검증
    - [x] 예약자명이 존재하지 않거나, 10자를 초과할 수 없다.
    - [x] 날짜는 LocalDate 형식을 만족시켜야 한다.
    - [x] 지나간 날짜와 시간에 대한 예약 생성은 불가능하다.
    - [x] 중복된 일시의 예약은 불가능하다.
    - [x] 존재하지 않는 예약 시간 ID를 이용하여 예약할 수 없다.

- [x] 테마 검증
    - [x] 예약이 이미 존재하는 테마는 삭제할 수 없다.

- [x] NotNull
    - [x] Reservation: date, themeId, timeId
    - [x] ReservationTime: startAt
- [x] NotBlank
    - [x] Reservation: name
    - [x] Theme: name

## API 명세서

## 회원 API

### 로그인 API

- [x] 로그인 API 구현

#### Request

```
POST /login HTTP/1.1
content-type: application/json
host: localhost:8080

{
    "password": "password",
    "email": "admin@email.com"
}
```

#### Response

```
HTTP/1.1 200 OK
Content-Type: application/json
Keep-Alive: timeout=60
Set-Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI; Path=/; HttpOnly
```

### 인증 정보 조회 API

- [x] 인증 정보 조회 API 구현

#### Request

```
GET /login/check HTTP/1.1
cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
host: localhost:8080
```

#### Response

```
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

## 예약 API

### 예약 조회 API

- [x] 예약 조회 API 구현

#### Request

```
GET /reservations HTTP/1.1
content-type: application/json
cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI
host: localhost:8080
```

#### Response

```
[
    {
        "id": 1,
        "member": {
          "name": "브라운"
        }
        "theme": {
            "id": 1,
            "name": "레벨2 탈출",
            "description": "우테코 레벨2를 탈출하는 내용입니다.",
            "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        }
        "date": "2023-08-05",
        "time": {
            "id": 1,
            "startAt": "10:00"
        }
    }
]
```

### 관리자 예약 목록 검색 API

- [x] 관리자 예약 목록 검색 API 구현

#### Request

```
GET /admin/reservations?themeId=?memberId=?dateFrom=?dateTo=? HTTP/1.1
content-type: application/json
cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI
host: localhost:8080
```

#### Response

```
[
    {
        "id": 1,
        "member": {
          "name": "브라운"
        }
        "theme": {
            "id": 1,
            "name": "레벨2 탈출",
            "description": "우테코 레벨2를 탈출하는 내용입니다.",
            "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        }
        "date": "2023-08-05",
        "time": {
            "id": 1,
            "startAt": "10:00"
        }
    }
]
```

### 예약 추가 API

- [x] 사용자 예약 추가 API 구현

#### Request

```
POST /reservations HTTP/1.1
content-type: application/json
cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI
host: localhost:8080

{
    "date": "2024-03-01",
    "themeId": 1,
    "timeId": 1
}
```

#### Response

```
HTTP/1.1 200
Content-Type: application/json

{
    "id": 1,
    "name": "브라운",
    "theme": {
        "id": 1,
        "name": "레벨2 탈출",
        "description": "우테코 레벨2를 탈출하는 내용입니다.",
        "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
    }
    "date": "2023-08-05",
    "time" : {
        "id": 1,
        "startAt" : "10:00"
    }
}
```

- [x] 관리자 예약 추가 API 구현

#### Request

```
POST /admin/reservations HTTP/1.1
content-type: application/json
cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI
host: localhost:8080

{
    "date": "2024-03-01",
    "themeId": 1,
    "timeId": 1
}
```

#### Response

```
HTTP/1.1 200
Content-Type: application/json

{
    "id": 1,
    "name": "브라운",
    "theme": {
        "id": 1,
        "name": "레벨2 탈출",
        "description": "우테코 레벨2를 탈출하는 내용입니다.",
        "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
    }
    "date": "2023-08-05",
    "time" : {
        "id": 1,
        "startAt" : "10:00"
    }
}
```

### 예약 취소 API

- [x] 예약 취소 API 구현

#### Request

```
DELETE /reservations/1 HTTP/1.1
```

#### Response

```
HTTP/1.1 200
```

## 예약 시간 API

### 예약 시간 추가 API

- [x] 예약 시간 추가 API 구현

#### Request

```
POST /times HTTP/1.1
content-type: application/json

{
    "startAt": "10:00"
}

```

#### Response

```
HTTP/1.1 200
Content-Type: application/json

{
    "id": 1,
    "startAt": "10:00"
}
```

### 예약 시간 조회 API

- [x] 예약 시간 조회 API 구현

#### Request

```
GET /times HTTP/1.1
```

#### Response

```
HTTP/1.1 200 
Content-Type: application/json

[
   {
        "id": 1,
        "startAt": "10:00",
        "alreadyBookmarked": true
    }
]
```

### 예약 시간 삭제 API

- [x] 예약 시간 삭제 API 구현

#### Request

```
DELETE /times/1 HTTP/1.1
```

#### Response

```
HTTP/1.1 200
```

## 테마 API

### 테마 추가 API

- [x] 테마 추가 API 구현

#### Request

```
POST /themes HTTP/1.1
content-type: application/json

{
    "name": "레벨2 탈출",
    "description": "우테코 레벨2를 탈출하는 내용입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}
```

#### Response

```
HTTP/1.1 201
Location: /themes/1
Content-Type: application/json

{
    "id": 1,
    "name": "레벨2 탈출",
    "description": "우테코 레벨2를 탈출하는 내용입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}
```

### 테마 조회 API

- [x] 테마 조회 API 구현

#### Request

```
GET /themes HTTP/1.1
```

#### Response

```
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

### 인기 테마 조회 API

- [x] 인기 테마 조회 API 구현
    - [x] 최근 일주일을 기준으로 하여 해당 기간 내에 방문하는 예약이 많은 테마 10개를 조회한다.

#### Request

```
GET /themes/popular HTTP/1.1
```

#### Response

```
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

### 테마 삭제 API

- [x] 테마 삭제 API 구현

#### Request

```
DELETE /themes/1 HTTP/1.1
```

#### Response

```
HTTP/1.1 204
```
