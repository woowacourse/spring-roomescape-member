# 방탈출 예약 관리

## API 명세

| Method | Endpoint             | Description     | File Path                              | Controller Type   |
|--------|----------------------|-----------------|----------------------------------------|-------------------|
| GET    | `/admin`             | 어드민 페이지 요청      | `templates/admin/index.html`           | `@Controller`     |
| GET    | `/admin/reservation` | 예약 관리 페이지 요청    | `templates/admin/reservation-new.html` | `@Controller`     |
| GET    | `/admin/time`        | 예약 시간 관리 페이지 요청 | `templates/admin/time.html`            | `@Controller`     |
| GET    | `/admin/theme`       | 테마 관리 페이지 요청    | `templates/admin/theme.html`           | `@Controller`     |
| GET    | `/reservations`      | 예약 정보           |                                        | `@RestController` |
| POST   | `/reservations`      | 예약 추가           |                                        | `@RestController` |
| DELETE | `/reservations/{id}` | 예약 취소           |                                        | `@RestController` |
| GET    | `/times`             | 예약 시간 조회        |                                        | `@RestController` |
| POST   | `/times`             | 예약 시간 삭제        |                                        | `@RestController` |
| DELETE | `/times/{id}`        | 예약 시간 추가        |                                        | `@RestController` |

### 예약 목록 조회 API

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

### 예약 취소 API

- Request

```
DELETE /reservations/1 HTTP/1.1
```

- Response

```
HTTP/1.1 204
```

### 시간 목록 조회 API

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

### 시간 추가 API

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

### 시간 삭제 API

- Request

```
DELETE /times/1 HTTP/1.1
```

- Response

```
HTTP/1.1 204
```