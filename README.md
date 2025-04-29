# 방탈출 예약 관리

## 1단계 - 예외 처리와 응답

- [x] 시간 관리, 예약 관리 API 구현
- 예외 처리
    - [x] 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때
    - [x] 예약 생성 시 예약자명, 날짜, 시간에 유효하지 않은 값이 입력 되었을 때
    - [x] 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때
- 비즈니스 로직 추가
    - [ ] 지나간 날짜와 시간에 대한 예약 생성은 불가능하다.
    - [ ] 중복 예약은 불가능하다.

# API 명세

### 예약 목록 조회

```
Request
GET /reservations HTTP/1.1

Response
HTTP/1.1 200 
Content-Type: application/json
[
    {
        "id": "Long",
        "name": String,
        "date": LocalDate (YYYY-MM-DD),
        "reservationTime": LocalTime (HH:mm)
    },
    {
        "id": "Long",
        "name": String,
        "date": LocalDate (YYYY-MM-DD),
        "reservationTime": {
            "id": Long,
            "startAt" : LocalTime (HH:mm)
        }
    }
]
```

### 예약 추가

```
Request
Content-Type: application/json
POST /reservations
{
    "name": String,
    "date": LocalDate (YYYY-MM-DD),
    "timeId": Long
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
    }
}

```

### 예약 취소

```
Request
DELETE /reservations/1 HTTP/1.1

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
