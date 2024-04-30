# 방탈출 예약 관리

## API 명세

### 예약 조회 API

**Request**

```http request
GET /reservations HTTP/1.1
```

<br>

**Response**

```
HTTP/1.1 200 
Content-Type: application/json

[
    {
        "id": 1,
        "name": "브라운",
        "date": "2023-01-01",
        "time": {
            "id": 1,
            "startAt": "10:00"
        }
    },
    {
        "id": 2,
        "name": "브라운",
        "date": "2023-01-02",
        "time": {
            "id": 1,
            "startAt": "10:00"
        }
    }
]
```
<br>

### 예약 추가 API

**Request**

```http request
POST /reservations HTTP/1.1
content-type: application/json

{
    "date": "2023-08-05",
    "name": "브라운",
    "timeId": 1
}
```

<br>

**Response**

```
HTTP/1.1 200
Content-Type: application/json

{
    "id": 1,
    "name": "브라운",
    "date": "2023-08-05",
    "time": {
        "id": 1,
        "startAt": "10:00"
    }
}

```

<br>

### 예약 취소 API

**Request**

```http request
DELETE /reservations/1 HTTP/1.1
```

**Response**
```
HTTP/1.1 200
```

<br>

### 시간 추가 API

**Request**

```http request
POST /times HTTP/1.1
content-type: application/json

{
    "startAt": "10:00"
}
```

**Response**

```
HTTP/1.1 200
Content-Type: application/json

{
    "id": 1,
    "startAt": "10:00"
}
```

<br>

### 시간 조회 API

**Request**

```http request
GET /times HTTP/1.1
```

**Response**

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

<br>

### 시간 삭제 API

**Request**

```http request
DELETE /times/1 HTTP/1.1
```

**Response**

```
HTTP/1.1 200
```
