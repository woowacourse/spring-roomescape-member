# API 명세

## 화면 출력

### 메인 페이지

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

### 예약 관리 페이지

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

### 시간 관리 페이지

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

## 데이터 전달

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
        }
        "theme" : {
            "id" : 1,
            "name" : "theme 1"
        }
    },
    {
        "id": 2,
        "name": "ted",
        "date": "2024-04-01",
        "time": {
            "id": 2,
            "startAt": "11:00"
        }
        "theme" : {
            "id" : 1,
            "name" : "theme 1"
        }
    }
]
```

---

### 예약 조회(단일)

#### Request

```http
GET /reservation/1 HTTP/1.1
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
    }
    "theme" : {
            "id" : 1,
            "name" : "theme 1"
        }
}
```

---

### 예약 등록

#### Request

```http
POST /reservations HTTP/1.1
Content-type: application/json

{
    "name": "ted",
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

