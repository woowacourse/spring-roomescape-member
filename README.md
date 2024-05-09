# 방탈출 예약 관리

## API 명세

| Method | Endpoint                              | Description           | File Path                              | Controller Type   |
|--------|---------------------------------------|-----------------------|----------------------------------------|-------------------|
| GET    | `/`                                   | 인기 테마 페이지 요청          | `templates/index.html`                 | `@Controller`     |
| GET    | `/reservation`                        | 사용자 예약 페이지 요청         | `templates/reservation.html`           | `@Controller`     |
| GET    | `/admin`                              | 어드민 페이지 요청            | `templates/admin/index.html`           | `@Controller`     |
| GET    | `/admin/reservation`                  | 예약 관리 페이지 요청          | `templates/admin/reservation-new.html` | `@Controller`     |
| GET    | `/admin/time`                         | 예약 시간 관리 페이지 요청       | `templates/admin/time.html`            | `@Controller`     |
| GET    | `/admin/theme`                        | 테마 관리 페이지 요청          | `templates/admin/theme.html`           | `@Controller`     |
| GET    | `/reservations`                       | 예약 정보 조회              |                                        | `@RestController` |
| GET    | `/reservations/themes/{themeId}?date` | 특정 날짜의 특정 테마 예약 정보 조회 |                                        | `@RestController` |
| POST   | `/reservations`                       | 예약 추가                 |                                        | `@RestController` |
| DELETE | `/reservations/{id}`                  | 예약 취소                 |                                        | `@RestController` |
| GET    | `/times`                              | 예약 시간 조회              |                                        | `@RestController` |
| DELETE | `/times/{id}`                         | 예약 시간 추가              |                                        | `@RestController` |
| POST   | `/times`                              | 예약 시간 삭제              |                                        | `@RestController` |
| GET    | `/themes`                             | 테마 정보 조회              |                                        | `@RestController` |
| POST   | `/themes`                             | 테마 추가                 |                                        | `@RestController` |
| DELETE | `/themes/{id}`                        | 테마 삭제                 |                                        | `@RestController` |

---

### 예약 정보 조회 API

- Request

```
GET /reservations HTTP/1.1
```

- Response

```
HTTP/1.1 200 
Content-Type: application/json

[
    {
        "id": 1,
        "name": "브라운",
        "date": "2023-08-05",
        "time": {
            "id": 1,
            "startAt": "10:00"
        }
    }
]
```

---

### 특정 날짜의 특정 테마 예약 정보 조회

- Request

```
GET /reservations/themes/1?date=2024-12-31 HTTP/1.1
```

---

- Response

```
[
    {
        "timeId": 1,
        "startAt": "17:00",
        "alreadyBooked": false
    },
    {
        "timeId": 2,
        "startAt": "20:00",
        "alreadyBooked": false
    }
]
```

---

### 예약 추가 API

- Request

```
POST /reservations HTTP/1.1
content-type: application/json

{
    "date": "2023-08-05",
    "name": "브라운",
    "timeId": 1
}
```

- Response

```
HTTP/1.1 201
Content-Type: application/json

{
    "id": 1,
    "name": "브라운",
    "date": "2023-08-05",
    "time" : {
        "id": 1,
        "startAt" : "10:00"
    }
}
```

---

### 예약 취소 API

- Request

```
DELETE /reservations/1 HTTP/1.1
```

- Response

```
HTTP/1.1 204
```

---

### 예약 시간 조회 API

- Request

```
GET /times HTTP/1.1
```

- Response

```
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

### 예약 시간 추가 API

- Request

```
POST /times HTTP/1.1
content-type: application/json

{
    "startAt": "10:00"
}
```

- Response

```
HTTP/1.1 201
Content-Type: application/json

{
    "id": 1,
    "startAt": "10:00"
}
```

---

### 예약 시간 삭제 API

- Request

```
DELETE /times/1 HTTP/1.1
```

- Response

```
HTTP/1.1 204
```

---

### 테마 정보 조회 API

- Request

```
GET /themes HTTP/1.1
```

- response

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

---

### 테마 추가 API

- Request

```
POST /themes HTTP/1.1
content-type: application/json

{
    "name": "레벨2 탈출",
    "description": "우테코 레벨2를 탈출하는 내용입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}
```

- response

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

---

### 테마 삭제 API

- Request

```
DELETE /themes/1 HTTP/1.1
```

- response

```
HTTP/1.1 204
```
