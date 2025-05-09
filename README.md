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

### 테마

- [x] 특정 테마에 대한 예약이 존재할 때 테마를 삭제할 수 없다.
- [x] 테마 이름은 최소 1글자, 최대 20글자이다.
- [x] 테마 소개는 최소 5글자, 최대 200글자이다.
- [x] 테마 이름은 중복될 수 없다.
- [x] 인기 테마는 최근 일주일을 기준으로 가장 예약이 많은 상위 10개의 테마이다.
  - 최근 일주일 내 예약이 존재하지 않을 경우 해당 테마는 포함되지 않는다.

## 페이지 목록

### 어드민 페이지

- `/admin`: 어드민 메인 페이지
- `/admin/reservation`: 예약 관리 페이지
- `/admin/time`: 시간 관리 페이지
- `/admin/theme` : 테마 관리 페이지

### 사용자 페이지

- `/login` : 로그인 페이지
- `/` : 인기 테마 페이지
- `/reservation` : 사용자 예약 페이지

---

## API 명세

### 예약 API

#### 사용자 예약 추가 API

**Request**

```
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

**Response**

```
HTTP/1.1 201
Location: /reservations/1
Content-Type: application/json

{
    "id": 1,
    "name": "브라운",
    "date": "2024-03-01",
    "time" : {
        "id": 1,
        "startAt" : "10:00"
    },
    "theme" : {
        "id": 1,
        "name": "레벨2 탈출",
        "description": "우테코 레벨2를 탈출하는 내용입니다.",
        "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
    }
}

```

#### 관리자 예약 추가 API

**Request**

```
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

**Response**

```
HTTP/1.1 201
Location: /reservations/1
Content-Type: application/json

{
    "id": 1,
    "name": "브라운",
    "date": "2024-03-01",
    "time" : {
        "id": 1,
        "startAt" : "10:00"
    },
    "theme" : {
        "id": 1,
        "name": "레벨2 탈출",
        "description": "우테코 레벨2를 탈출하는 내용입니다.",
        "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
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
        },
        "theme" : {
            "id": 1,
            "name": "레벨2 탈출",
            "description": "우테코 레벨2를 탈출하는 내용입니다.",
            "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
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
Location: /times/1
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

#### 예약 가능 시간 조회 API

**Request**

```
GET /available-times?date=2025-03-30&themeId=1 HTTP/1.1
```

**Response**

```
HTTP/1.1 200
Content-Type: application/json

[
    {
        "id": 1,
        "startAt": "10:00",
        "alreadyBooked": false
    }
]
```


### 테마 API

#### 테마 추가 API

**Request**

```
POST /themes HTTP/1.1
content-type: application/json

{
    "name": "레벨2 탈출",
    "description": "우테코 레벨2를 탈출하는 내용입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}
```

**Response**

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

#### 테마 조회 API

**Request**

```
GET /themes HTTP/1.1
```

**Response**

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

#### 테마 삭제 API

**Request**

```
DELETE /themes/1 HTTP/1.1
```

**Response**

```
HTTP/1.1 204
```

#### 인기 테마 조회 API

**Request**

```
GET /themes/popular-themes HTTP/1.1
```

**Response**

```
HTTP/1.1 200
Content-Type: application/json

[
   {
        "id": 2,
        "name": "레벨1 탈출",
        "description": "우테코 레벨1을 탈출하는 내용입니다.",
        "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
   },
   {
        "id": 1,
        "name": "레벨2 탈출",
        "description": "우테코 레벨2를 탈출하는 내용입니다.",
        "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
   }
]
```

### 사용자 API
#### 로그인 API

**Request**

```
POST /login HTTP/1.1
content-type: application/json
host: localhost:8080

{
    "password": "password",
    "email": "admin@email.com"
}
```

**Response**

```
HTTP/1.1 200 OK
Content-Type: application/json
Keep-Alive: timeout=60
Set-Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI; Path=/; HttpOnly
```

#### 인증 정보 조회 API
**Request**

```
GET /login/check HTTP/1.1
cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
host: localhost:8080
```

**Response**

```
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
