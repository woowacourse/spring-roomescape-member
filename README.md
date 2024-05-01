# 기능 명세

## Admin

- [X] `localhost:8080/admin` 요청 시 어드민 메인 페이지를 응답한다.
- [X] `/admin/reservation` 요청 시 예약 관리 페이지를 응답한다.
- [X] `/admin/time` 요청 시 시간 관리 페이지를 응답한다.

## Reservation

- [X] `/reservations` `GET` 요청 시 예약 목록을 조회하고 API 명세에 맞게 응답을 반환한다.
- [X] `/reservations` `POST` 요청 시 예약을 추가하고 API 명세에 맞게 응답을 반환한다.
    - [ ] 예약자 명이 비어있으면 예외를 발생시키고 상태코드 400을 반환한다.
    - [ ] 예약 날짜가 형식에 맞지 않는다면 예외를 발생시키고 상태코드 400을 반환한다.
    - [ ] 예약 시간 아이디가 자연수가 아니라면 예외를 발생시키고 상태코드 400을 반환한다.
    - [ ] 현재보다 이전 날짜 및 시간이라면 예외를 발생시키고 상태코드 400을 반환한다.
    - [ ] 예약 시간 아이디가 시간 테이블에 없으면 예외를 발생시키고 상태코드 400을 반환한다.
    - [ ] 예약 날짜와 시간이 중복이라면 예외를 발생시키고 상태코드 400을 반환한다.
- [X] `/reservations/{id}` `DELETE` 요청 시 예약을 삭제하고 API 명세에 맞게 응답을 반환한다.
    - [ ] 존재하지 않는 아이디를 삭제하려고 하면 예외를 발생시키고 상태코드 400을 반환한다.

## Time

- [X] `/times` `GET` 요청 시 시간 목록을 조회하고 API 명세에 맞게 응답을 반환한다.
- [X] `/times` `POST` 요청 시 시간을 추가하고 API 명세에 맞게 응답을 반환한다.
    - [ ] 시작 시간이 null이라면 예외를 발생시키고 상태코드 400을 반환한다.
    - [ ] 시작 시간이 형식에 맞지 않는다면 예외를 발생시키고 상태코드 400을 반환한다.
    - [ ] 시작 시간이 중복이라면 예외를 발생시키고 상태코드 400을 반환한다.
- [X] `/times/{id}` `DELETE` 요청 시 시간을 삭제하고 API 명세에 맞게 응답을 반환한다.
    - [ ] 예약이 존재하는 시간을 삭제하려고 하면 예외를 발생시키고 상태코드 400을 반환한다.
    - [ ] 존재하지 않는 아이디를 삭제하려고 하면 예외를 발생시키고 상태코드 400을 반환한다.

# API 명세

## reservations

### GET

```http
GET /reservations HTTP/1.1
```

```http
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

### Post

```http
POST /reservations HTTP/1.1
content-type: application/json
{
    "date": "2023-08-05",
    "name": "브라운",
    "timeId": 1
}
```

```http
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

### Delete

```http
DELETE /reservations/1 HTTP/1.1
```

```http
HTTP/1.1 204
```

## times

### GET

```http
GET /times HTTP/1.1
```

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

### Post

```http
POST /times HTTP/1.1
content-type: application/json
{
    "startAt": "10:00"
}
```

```http
HTTP/1.1 201
Content-Type: application/json
{
    "id": 1,
    "startAt": "10:00"
}
```

### Delete

```http
DELETE /times/1 HTTP/1.1
```

```http
HTTP/1.1 204
```
