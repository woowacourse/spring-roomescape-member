# 방탈출 예약 관리

## 응답 페이지

- `/` : 웰컴 페이지
- `/admin` : 어드민 메인 페이지
- `/admin/reservation` : 예약 관리 페이지
- `/admin/time` : 예약 시간 관리 페이지

## API 명세

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