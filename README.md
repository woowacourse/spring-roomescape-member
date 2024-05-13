# 기능 명세

## Admin

- [X] `localhost:8080/admin` 요청 시 어드민 메인 페이지를 응답한다.
- [X] `/admin/reservation` 요청 시 예약 관리 페이지를 응답한다.
- [X] `/admin/time` 요청 시 시간 관리 페이지를 응답한다.
- [x] `/admin/theme` 요청 시 테마 관리 페이지를 응답한다.
- [x] admin 권한이 있는 사람만 위 페이지에 접근 가능하다.

## Reservation

- [X] `/reservations` `GET` 요청 시 예약 목록을 조회하고 API 명세에 맞게 응답을 반환한다.
- [X] `/reservations` `POST` 요청 시 예약을 추가하고 API 명세에 맞게 응답을 반환한다.
    - [x] 예약자 명, 테마 아이디, 예약 날짜, 시간 아이디가 비어있으면 예외를 발생시키고 상태코드 400을 반환한다.
    - [x] 예약 날짜가 형식에 맞지 않는다면 예외를 발생시키고 상태코드 400을 반환한다.
    - [x] 테마 아이디가 자연수가 아니라면 예외를 발생시키고 상태코드 400을 반환한다.
    - [x] 예약 시간 아이디가 자연수가 아니라면 예외를 발생시키고 상태코드 400을 반환한다.
    - [x] 현재보다 이전 날짜 및 시간이라면 예외를 발생시키고 상태코드 400을 반환한다.
    - [x] 예약 시간 아이디가 시간 테이블에 없으면 예외를 발생시키고 상태코드 400을 반환한다.
    - [x] 해당 테마에 같은 날짜와 시간의 예약이 존재하면 예외를 발생시키고 상태코드 400을 반환한다.
- [X] `/reservations/{id}` `DELETE` 요청 시 예약을 삭제하고 API 명세에 맞게 응답을 반환한다.
    - [x] 존재하지 않는 아이디를 삭제하려고 하면 예외를 발생시키고 상태코드 400을 반환한다.
- [x] `/reservations/specific?themeId=${themeId}&memberId=${memberId}&dateFrom=${dateFrom}&dateTo=${dateTo}` `GET` 요청 시 검색 조건에 맞는 응답을 반환한다.

## Time

- [X] `/times` `GET` 요청 시 시간 목록을 조회하고 API 명세에 맞게 응답을 반환한다.
- [X] `/times` `POST` 요청 시 시간을 추가하고 API 명세에 맞게 응답을 반환한다.
    - [X] 시작 시간이 null이라면 예외를 발생시키고 상태코드 400을 반환한다.
    - [X] 시작 시간이 형식에 맞지 않는다면 예외를 발생시키고 상태코드 400을 반환한다.
    - [X] 시작 시간이 중복이라면 예외를 발생시키고 상태코드 400을 반환한다.
- [X] `/times/{id}` `DELETE` 요청 시 시간을 삭제하고 API 명세에 맞게 응답을 반환한다.
    - [X] 예약이 존재하는 시간을 삭제하려고 하면 예외를 발생시키고 상태코드 400을 반환한다.
    - [X] 존재하지 않는 아이디를 삭제하려고 하면 예외를 발생시키고 상태코드 400을 반환한다.
- [X] `/times/availability?date=${date}&themeId=${themeId}` `GET` 요청 시 시간 목록을 조회하고 API 명세에 맞게 응답을 반환한다.
    - [X] 날짜, 테마 아이디가 비어있으면 예외를 발생시키고 상태코드 400을 반환한다.
    - [X] 예약 날짜가 형식에 맞지 않는다면 예외를 발생시키고 상태코드 400을 반환한다.
    - [X] 테마 아이디가 자연수가 아니라면 예외를 발생시키고 상태코드 400을 반환한다.

### Theme

- [x] `/themes` `GET` 요청 시 테마 목록을 조회하고 API 명세에 맞게 응답을 반환한다.
- [x] `/themes` `POST` 요청 시 테마를 추가하고 API 명세에 맞게 응답을 반환한다.
    - [x] 테마 이름, 테마 설명, 테마 썸네일이 비어있다면 예외를 발생시키고 상태코드 400을 반환한다.
- [x] `/themes` `DELETE` 요청 시 테마를 삭제하고 API 명세에 맞게 응답을 반환한다.
    - [x] 예약이 존재하는 테마를 삭제하려고 하면 예외를 발생시키고 상태코드 400을 반환한다.
    - [x] 존재하지 않는 아이디를 삭제하려고 하면 예외를 발생시키고 상태코드 400을 반환한다.
- [X] `/themes/rank` `GET` 요청 시 예약 순서로 인기 테마 결과를 API 명세에 맞게 반환한다.

### login

- [x] `/login` `GET` 요청 시 로그인 폼이 있는 페이지를 응답한다.
- [x] `/login` `POST` 요청 시 멤버를 조회하고 쿠키를 만들어 응답한다.
- [x] 로그인이 성공하면 우측 상단의 login 버튼이 사용자 이름으로 변경된다.

### Member

- [x] `/members` `GET` 요청 시 멤버 목록을 조회하고 API 명세에 맞게 응답을 반환한다.

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
        "theme": {
            "id": 1,
            "name": "레벨2 탈출",
            "description": "우테코 레벨2를 탈출하는 내용입니다.",
            "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        },
        "date": "2023-08-05",
        "time": {
            "id": 1,
            "startAt": "10:00"
        }
    }
]
```

### GET

```http
GET /reservations/specific?themeId=1&memberId=1&dateFrom="2023-01-01"&dateTo="2024-12-31" HTTP/1.1
```

```http
[
    {
        "id": 1,
        "name": "브라운",
        "theme": {
            "id": 1,
            "name": "레벨2 탈출",
            "description": "우테코 레벨2를 탈출하는 내용입니다.",
            "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        },
        "date": "2023-08-05",
        "time": {
            "id": 1,
            "startAt": "10:00"
        }
    }
]
```

### POST

```http
POST /reservations HTTP/1.1
content-type: application/json
cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI
host: localhost:8080

{
    "date": "2024-03-01",
    "themeId": 1,
    "timeId": 1
}
```

```http
HTTP/1.1 201
Content-Type: application/json
{
    "id": 1,
    "name": "브라운",
    "theme": {
        "id": 1,
        "name": "레벨2 탈출",
        "description": "우테코 레벨2를 탈출하는 내용입니다.",
        "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
    },
    "date": "2024-03-01",
    "time" : {
        "id": 1,
        "startAt" : "10:00"
    }
}
```

### POST

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

```http
HTTP/1.1 201
Content-Type: application/json
{
    "id": 1,
    "name": "브라운",
    "theme": {
        "id": 1,
        "name": "레벨2 탈출",
        "description": "우테코 레벨2를 탈출하는 내용입니다.",
        "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
    },
    "date": "2024-03-01",
    "time" : {
        "id": 1,
        "startAt" : "10:00"
    }
}
```

### DELETE

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

### GET

```http
GET /times/availability?date="2024-12-20"&themeId=1 HTTP/1.1
```

```http
HTTP/1.1 200
Content-Type: application/json
[
   {
        "id": 1,
        "startAt": "10:00"
        "alreadyBooked": true
    }
]
```

### POST

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

### DELETE

```http
DELETE /times/1 HTTP/1.1
```

```http
HTTP/1.1 204
```

## theme

### GET

```http
GET /themes HTTP/1.1
```

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

### GET

```http
GET /themes/rank HTTP/1.1
```

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

### POST

```http
POST /themes HTTP/1.1
content-type: application/json

{
    "name": "레벨2 탈출",
    "description": "우테코 레벨2를 탈출하는 내용입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}
```

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

### DELETE

```http
DELETE /themes/1 HTTP/1.1
```

```http
HTTP/1.1 204
```

## login

### GET

```http
GET /login/check HTTP/1.1
cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
host: localhost:8080
```

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

### POST

```http
POST /login HTTP/1.1
content-type: application/json
host: localhost:8080

{
    "password": "password",
    "email": "admin@email.com"
}
```

```http
HTTP/1.1 200 OK
Content-Type: application/json
Keep-Alive: timeout=60
Set-Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI; Path=/; HttpOnly
```

## member

### GET

```http
GET /members HTTP/1.1
```

```http
HTTP/1.1 200 
Content-Type: application/json

[
   {
        "id": 1,
        "name": "아토",
        "role": "admin",
        "email": "attomail"
        "password": "attoword"
    }
]
```
