# 방탈출 예약 관리

### 뷰

- [x] /admin 요청 시 접속시 어드민 메인 페이지가 응답한다.
- [x] /admin/reservation 요청 시 예약 관리 페이지가 응답한다.
- [x] /admin/theme 요청 시 테마 관리 페이지가 응답한다.

### 예약

- [x] 예약에는 사용자의 이름, 예약 날짜, 예약 시간이 있다.
- [x] 예약을 모두 조회할 수 있다.
- [x] 예약을 추가할 수 있다.
    - [x] 예약 시간은 정해진 예약 시간만 추가할 수 있다.
    - [x] 예약자명, 날짜, 시간에 유효하지 않은 값이 입력될 경우, 예외가 발생한다.
        - [x] 지나간 날짜 및 시간
        - [x] 예약자명 10자 초과
    - [x] 날짜와 시간이 중복될 경우 예외가 발생한다.
- [x] 예약을 취소할 수 있다.
- [ ] 관리자가 방탈출 예약 시, 테마 정보를 포함할 수 있도록 기능을 변경합니다.

### 예약 시간

- [x] 예약 가능한 시간을 추가할 수 있다.
    - [x] 시작 시간에 유효하지 않은 값이 입력될 경우, 예외가 발생한다.
- [x] 예약 가능한 시간을 모두 조회할 수 있다.
- [x] 예약 가능한 시간을 삭제할 수 있다.
    - [x] 삭제하려는 시간에 대한 예약이 존재할 경우, 예외가 발생한다.

### 테마

- [ ] 테마를 추가할 수 있다.
- [ ] 테마를 모두 조회할 수 있다.
- [ ] 테마를 삭제할 수 있다.

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
POST /reservation

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

#### 예약 추가 시 예외

```
Request
Content-Type: application/json
POST /reservation

{
    "date": "2023-08-05",
    "name": "1234567891011",
    "timeId": 1
}

name: string
date: string
timeId: number

1. 예약자명이 10자 초과인 경우
Response
HTTP/1.1 400

{
    "error": "BAD_REQUEST",
    "message": "예약자명은 10자 이하여야합니다."
}


2. 예약 날짜 및 시간이 과거인 경우
Response
HTTP/1.1 400

{
    "error": "BAD_REQUEST",
    "message": "예약은 미래만 가능합니다."
}

3. 예약이 중복될 경우(날짜와 시간이 동일한 경우)
Response
HTTP/1.1 400

{
    "error": "BAD_REQUEST",
    "message": "이미 존재하는 예약입니다."
}

error: string
message: string

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

#### 예약 시간 추가 예외

```

Request
POST /times HTTP/1.1
content-type: application/json

{
    "startAt": "10:00:30"
}

1. 예약 시간이 HH:mm 형식에 안맞는 경우 
Response
HTTP/1.1 400
Content-Type: application/json

{
    "error": "BAD_REQUEST",
    "message": "예약 시간 형식은 HH:mm 입니다."
}

error: string
message: string

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

#### 예약 시간 삭제 예외

```
Request
DELETE /times/{id} HTTP/1.1

1. 삭제하려는 시간에 대한 예약이 존재할 경우
Response
HTTP/1.1 400

{
    "error": "BAD_REQUEST",
    "message": "이 시간에 대한 예약이 존재합니다."
}

error: string
message: string

```

### 테마 목록 조회

```
Request
GET /themes HTTP/1.1

Response
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

id: number
name: string
description: string
thumbnail: string

```

### 테마 추가

```

Request
POST /themes HTTP/1.1
content-type: application/json

{
    "name": "레벨2 탈출",
    "description": "우테코 레벨2를 탈출하는 내용입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}

name: string
description: string
thumbnail: string

Response
HTTP/1.1 201
Location: /themes/1
Content-Type: application/json

{
    "id": 1,
    "name": "레벨2 탈출",
    "description": "우테코 레벨2를 탈출하는 내용입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}

id: number
name: string
description: string
thumbnail: string

```

#### 테마 추가 예외

```
Request
POST /themes HTTP/1.1
content-type: application/json

{
    "name": "레벨2 탈출",
    "description": "우테코 레벨2를 탈출하는 내용입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}

name: string
description: string
thumbnail: string

1. 테마 이름이 중복될 경우
Response
HTTP/1.1 400

{
    "error": "BAD_REQUEST",
    "message": "테마가 이미 존재합니다."
}

error: string
message: string

```

### 테마 삭제

### 예약 시간 삭제

```
Request
DELETE /themes/{id} HTTP/1.1

Response
HTTP/1.1 204
```
