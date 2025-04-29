# 방 탈출 예약 관리

## 기능 요구사항

### 예약

- [x] `yyyy-MM-dd` 형식으로 예약 날짜를 입력해야한다.
- [x] 시간 ID는 양의 정수로 입력해야한다.
- [x] 예약자명은 최소 1글자, 최대 5글자여야한다.
- [x] 같은 날짜와 시간에 중복 예약을 할 수 없다.
- [x] 과거에 대한 예약은 불가능하다.
- [x] 존재하는 시간 ID를 입력해야한다.

### 예약 시간

- [x] `HH:mm` 형식으로 시간을 입력해야한다.
- [x] 예약 시간을 중복 생성할 수 없다.
- [x] 특정 시간에 대한 예약이 존재할 때 예약 시간을 삭제할 수 없다.

## 페이지 목록

- `http://localhost:8080/admin`: 어드민 메인 페이지
- `http://localhost:8080/admin/reservation`: 예약 관리 페이지
- `http://localhost:8080/admin/time`: 시간 관리 페이지

---

## API 명세

### 예약 API

#### 예약 추가 API

**Request**

```
POST /reservations HTTP/1.1
content-type: application/json

{
    "date": "2023-08-05",
    "name": "브라운",
    "timeId": 1
}

```

**Response**

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

#### 예약 조회 API

**Request**

```
GET /reservations HTTP/1.1
```

**Response**

```
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

#### 예약 취소 API

**Request**

```
DELETE /reservations/1 HTTP/1.1
```

**Response**

```
HTTP/1.1 204
```

### 시간 API

#### 시간 추가 API

**Request**

```
POST /times HTTP/1.1
content-type: application/json

{
    "startAt": "10:00"
}

```

**Response**

```
HTTP/1.1 201
Content-Type: application/json

{
    "id": 1,
    "startAt": "10:00"
}
```

#### 시간 조회 API

**Request**

```
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

#### 시간 삭제 API

**Request**

```
DELETE /times/1 HTTP/1.1
```

**Response**

```
HTTP/1.1 204
```
