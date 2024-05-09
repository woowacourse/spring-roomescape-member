# API 명세

## 웹 어플리케이션 API 명세

### 예약 시간 추가 API

#### Request

```http request
POST /times HTTP/1.1
content-type: application/json

{
    "startAt": "10:00"
}
```

#### Response

```http request
HTTP/1.1 201
Content-Type: application/json

{
    "id": 1,
    "startAt": "10:00"
}
```

### 예약 시간 조회 API

#### Request

```http request
GET /times HTTP/1.1
```

#### Response

```http request
HTTP/1.1 200
Content-Type: application/json

{
  "reservationTimes": [
    {
      "id": 1,
      "startAt": "10:00"
    },
    {
      "id": 2,
      "startAt": "11:00"
    },
  ]
}
```

### 예약 가능 시간 조회 API

#### Request

```http request
GET /times/available?date=2025-02-03&themeId=1 HTTP/1.1
```

#### Response

```http request
HTTP/1.1 200
Content-Type: application/json

{
  "reservationTimes": [
    {
      "id": 1,
      "startAt": "10:00",
      "alreadyBooked": false
    },
    {
      "id": 2,
      "startAt": "11:00",
      "alreadyBooked": false
    }
  ]
}
```

### 시간 삭제 API

#### Request

```http request
DELETE /times/1 HTTP/1.1
```

#### Response

```http request
HTTP/1.1 204
```

### 예약 추가 API

#### Request

```http request
POST /reservations HTTP/1.1
content-type: application/json
cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI

{
    "date": "2023-08-05",
    "timeId": 1,
    "themeId": 1
}
```

#### Response

```http request
HTTP/1.1 201
Content-Type: application/json

{
    "id": 1,
    "name": "브라운",
    "date": "2023-08-05",
    "time" : {
        "id": 1,
        "startAt" : "10:00"
    },
    "theme" : {
        "id": 1,
        "name": "세렌디피티: 뜻밖의 행운",
        "description": "방탈출 게임은 주어진 시간 내에 팀이 퍼즐을 해결하고 탈출하는 것이 목표입니다.",
        "thumbnail": "https://i.postimg.cc/T2Df9mR3/theme-PNG-SERENDIPITY.png"
    }
}
```

### 예약 조회 API

#### Request

```http request
GET /reservations HTTP/1.1
```

#### Response

HTTP/1.1 200
Content-Type: application/json

```http request
HTTP/1.1 201
Content-Type: application/json

{
  "reservations": [
    {
      "id": 1,
      "name": "피케이",
      "date": "2024-04-29",
      "startAt": "10:00",
      "name": "세렌디피티: 뜻밖의 행운",
    },
    {
      "id": 2,
      "name": "망쵸",
      "date": "2024-04-30",
      "startAt": "11:00",
      "name": "세렌디피티: 뜻밖의 행운",
    }
  ]
}
```

### 예약 삭제 API

#### Request

```http request
DELETE /reservations/1 HTTP/1.1
```

#### Response

```http request
HTTP/1.1 204
```

### 테마 추가 API

#### Request

```http request
POST /themes HTTP/1.1
content-type: application/json

{
    "name": "세렌디피티: 뜻밖의 행운",
    "description": "방탈출 게임은 주어진 시간 내에 팀이 퍼즐을 해결하고 탈출하는 것이 목표입니다.",
    "thumbnail": "https://i.postimg.cc/T2Df9mR3/theme-PNG-SERENDIPITY.png"
}
```

#### Response

```http request
HTTP/1.1 201
location: /themes/{id}

{
    "id": 1,
    "name": "세렌디피티: 뜻밖의 행운",
    "description": "방탈출 게임은 주어진 시간 내에 팀이 퍼즐을 해결하고 탈출하는 것이 목표입니다.",
    "thumbnail": "https://i.postimg.cc/T2Df9mR3/theme-PNG-SERENDIPITY.png"
}
```

### 테마 조회 API

#### Request

```http request
GET /themes HTTP/1.1
```

#### Response

```http request
HTTP/1.1 200
content-type: application/json

[
    {
        "id": 1,
        "name": "세렌디피티: 뜻밖의 행운",
        "description": "방탈출 게임은 주어진 시간 내에 팀이 퍼즐을 해결하고 탈출하는 것이 목표입니다.",
        "thumbnail": "https://i.postimg.cc/T2Df9mR3/theme-PNG-SERENDIPITY.png"
    },
    {
        "id": 2,
        "name": "포레스트: 신비한 숲속의 비밀",
        "description": "방탈출 게임은 주어진 시간 내에 팀이 퍼즐을 해결하고 탈출하는 것이 목표입니다.",
        "thumbnail": "https://i.postimg.cc/3xjvZz1Y/theme-PNG-FOREST.png"
    }
]
```

### 인기 테마 조회 API

#### Request

```http request
GET /themes/tops HTTP/1.1
```

#### Response

```http request
HTTP/1.1 200
content-type: application/json

[
    {
        "id": 1,
        "name": "세렌디피티: 뜻밖의 행운",
        "description": "방탈출 게임은 주어진 시간 내에 팀이 퍼즐을 해결하고 탈출하는 것이 목표입니다.",
        "thumbnail": "https://i.postimg.cc/T2Df9mR3/theme-PNG-SERENDIPITY.png"
    },
    {
        "id": 2,
        "name": "포레스트: 신비한 숲속의 비밀",
        "description": "방탈출 게임은 주어진 시간 내에 팀이 퍼즐을 해결하고 탈출하는 것이 목표입니다.",
        "thumbnail": "https://i.postimg.cc/3xjvZz1Y/theme-PNG-FOREST.png"
    }
]
```

### 테마 삭제 API

#### Request

```http request
DELETE /themes/1 HTTP/1.1
```

#### Response

```http request
HTTP/1.1 204
```

### 사용자 로그인 API

#### Request

```http request
POST /login HTTP/1.1
content-type: application/json

{
    "email": "mangchoWithPk@woowa.com",
    "password": "1234"
}
```

#### Response

```http request
HTTP/1.1 200
Content-Type: application/json
Keep-Alive: timeout=60
Set-Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI; Path=/; HttpOnly
```

### 사용자 로그인 체크 API

#### Request

```http request
GET /login/check HTTP/1.1
cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
```

#### Response

```http request
HTTP/1.1 200
Connection: keep-alive
Content-Type: application/json
Date: Sun, 03 Mar 2024 19:16:56 GMT
Keep-Alive: timeout=60
Transfer-Encoding: chunked

{
    "name": "어드민"
}
```
