# 방탈출 예약 관리

### 뷰

- [x] /admin 요청 시 접속시 어드민 메인 페이지가 응답한다.
- [x] /admin/reservation 요청 시 예약 관리 페이지가 응답한다.

### 예약

- [x] 예약에는 사용자의 이름, 예약 날짜, 예약 시간이 있다.
- [x] 예약을 모두 조회할 수 있다.
- [x] 예약을 추가할 수 있다.
    - [x] 예약 시간은 정해진 예약 시간만 추가할 수 있다.
- [x] 예약을 취소할 수 있다.

### 예약 시간

- [x] 예약 가능한 시간을 추가할 수 있다.
- [x] 예약 가능한 시간을 모두 조회할 수 있다.
- [x] 예약 가능한 시간을 삭제할 수 있다.

# API 명세

### 예약 목록 조회

```
Request
GET /reservationDao HTTP/1.1

Response
HTTP/1.1 200 
Content-Type: application/json
[
    {
        "id": 1, 
        "name": "브라운",
        "date": "2023-01-01",
        "time": "10:00"
    },
    {
        "id": 2,
        "name": "브라운",
        "date": "2023-01-02",
        "time": "11:00"
    }
]

id: number
name: string
date: string
time: string 
```

### 예약 추가

```
Request
Content-Type: application/json
POST /reservationDao

{
    "date": "2026-08-05",
    "name": "브라운",
    "timeId": 1
}

name: string
date: string
timeId: number 

Response
Location: http://url/reservations/{id}
HTTP/1.1 201
Content-Type: application/json

{
    "id": 1,
    "name": "브라운",
    "date": "2026-08-05",
    "time": {
        "id": 1,
        "startAt": "10:00:00"
    }
}

id: number
name: string
date: string
time
  id: number
  startAt: string
  
```

### 예약 취소

```
Request
DELETE /reservationDao/{id} HTTP/1.1

Response
HTTP/1.1 204
```

### 예약 시간 추가

```
Request
POST /times HTTP/1.1
content-type: application/json

{
    "startAt": "10:00"
}

startAt: string

Response
HTTP/1.1 200
Location: http://url/times/{id}
Content-Type: application/json

{
    "id": 1,
    "startAt": "10:00"
}

id: number
startAt: string
```

### 예약 시간 목록 조회

```
Request
GET /times HTTP/1.1

Response
HTTP/1.1 200 
Content-Type: application/json
[
   {
        "id": 1,
        "startAt": "10:00"
    }
]

id: number
startAt: string
```

### 예약 시간 삭제

```
Request
DELETE /times/{id} HTTP/1.1

Response
HTTP/1.1 200
```
