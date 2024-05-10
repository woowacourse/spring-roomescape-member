# API 명세

## 화면 출력

### Admin_메인 페이지

#### Request
```http
GET /admin HTTP/1.1
```

#### Response
```http
HTTP/1.1 200
Content-Type : text/html

admin/index.html
```
---

### Admin_예약 관리 페이지

#### Request
```http
GET /admin/reservation HTTP/1.1
```

#### Response
```http
HTTP/1.1 200
Content-Type : text/html

admin/reservation-new.html
```

---

### Admin_시간 관리 페이지

#### Request
```http
GET /admin/time HTTP/1.1
```

#### Response
```http
HTTP/1.1 200
Content-Type : text/html

admin/time.html
```

---

### Front_인기 테마 페이지

#### Request
```http
GET / HTTP/1.1
```

#### Response
```http
HTTP/1.1 200
Content-Type : text/html

index.html
```

---

### Front_로그인 페이지

#### Request
```http
GET /login HTTP/1.1
```

#### Response
```http
HTTP/1.1 200
Content-Type : text/html

login.html
```

---

### Front_예약 페이지

#### Request
```http
GET /reservation HTTP/1.1
```

#### Response
```http
HTTP/1.1 200
Content-Type : text/html

reservation.html
```

---

## 데이터 전달

### 로그인

#### Request
```http
POST /login HTTP/1.1
content-type: application/json
host: localhost:8080

{
"password": "password",
"email": "admin@email.com"
}
```

#### Response
```http
HTTP/1.1 200 OK
Content-Type: application/json
Keep-Alive: timeout=60
Set-Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI; Path=/; HttpOnly
```

---

### 로그인 정보 조회

#### Request
```http
GET /login/check HTTP/1.1
cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
host: localhost:8080
```

#### Response
```http
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

---

### 예약 조회(전체)

#### Request
```http
GET /reservations HTTP/1.1
```

#### Response
```http
HTTP/1.1 200
Content-Type: application/json

[
    {
        "id": 1,
        "name": "ted",
        "date": "2024-04-01",
        "time": {
            "id": 1,
            "startAt": "10:00"
        },
        "theme" : {
            "id" : 1,
            "name" : "theme 1"
        },
        "member" : {
            "id" : 1,
            "name" : "ted"
        }
    },
    {
        "id": 2,
        "name": "ted",
        "date": "2024-04-01",
        "time": {
            "id": 2,
            "startAt": "11:00"
        },
        "theme" : {
            "id" : 1,
            "name" : "theme 1"
        },
        "member" : {
            "id" : 1,
            "name" : "ted"
        }
    }
]
```

---

### 예약 조회(단일)

#### Request
```http
GET /reservations/1 HTTP/1.1
```

#### Response
```http
HTTP/1.1 200
Content-Type: application/json

{
    "id": 1,
    "name": "tesd",
    "date": "2023-04-01",
    "time" : {
        "id": 1,
        "startAt" : "10:00"
    },
    "theme" : {
            "id" : 1,
            "name" : "theme 1"
    },
    "member" : {
            "id" : 1,
            "name" : "ted"
    }
}
```

---

### 예약 등록_사용자

#### Request
```http
POST /reservations HTTP/1.1
Content-type: application/json
cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI
host: localhost:8080

{
    "date": "2024-04-01",
    "timeId": 1,
    "themeId" : 1
}
```

#### Response
```http
HTTP/1.1 201
Content-Type: application/json
Location: /reservations/1
```
---

### 예약 등록_관리자

#### Request
```http
POST /admin/reservations HTTP/1.1
content-type: application/json
cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI
host: localhost:8080

{
    "date": "2024-03-01",
    "themeId": 1,
    "timeId": 1,
    "memberId": 1
}
```

#### Response
```http
HTTP/1.1 201
Content-Type: application/json
Location: /reservations/1
```

---

### 예약 취소

#### Request
```http
DELETE /reservations/1 HTTP/1.1
```

#### Response
```http
HTTP/1.1 204
```

---

### 예약 시간 추가

#### Request
```http
POST /times HTTP/1.1
Content-type: application/json

{
    "startAt": "10:00"
}
```

#### Response
```http
HTTP/1.1 201
Content-Type: application/json
Location: /times/1
```

---

### 예약 시간 조회(전체)

#### Request
```http
GET /times HTTP/1.1
```

#### Response
```http
HTTP/1.1 200 
Content-Type: application/json

[
   {
        "id": 1,
        "startAt": "10:00"
    }
]
```

---

### 예약 시간 조회(단일)

#### Request
```http
GET /times/1 HTTP/1.1
```

#### Response
```http
HTTP/1.1 200
Content-Type: application/json

{
    "id": 1,
    "startAt": "10:00"
}
```
---

### 예약 가능 시간 조회

#### Request
```http
GET /times/available?date=2024-05-01&themeId=1 HTTP/1.1
```

#### Response
```http
[
   {
        "id": 1,
        "startAt": "10:00"
    }
]
```

---

### 예약 시간 삭제

#### Request
```http
DELETE /times/1 HTTP/1.1
```

#### Response
```http
HTTP/1.1 204
```
---

### 테마 조회

#### Request
```http
GET /themes HTTP/1.1
```

#### Response
```http
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

---

### 인기 테마 조회

#### Request
```http
GET /themes/popular HTTP/1.1
```

#### Response
```http
HTTP/1.1 200
content-type: application/json

[
    {
        "id": 1,
        "name": "레벨1 탈출",
        "description": "우테코 레벨1를 탈출하는 내용입니다.",
        "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
    }
        {
        "id": 2,
        "name": "레벨2 탈출",
        "description": "우테코 레벨2를 탈출하는 내용입니다.",
        "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
    }
]
```

---

### 테마 추가

#### Request
```http
POST /themes HTTP/1.1
content-type: application/json

{
    "name": "레벨2 탈출",
    "description": "우테코 레벨2를 탈출하는 내용입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}
```

#### Response
```http
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

---

### 테마 삭제

#### Request
```http
DELETE /themes/1 HTTP/1.1
```

#### Response
```http
HTTP/1.1 204
```

---

