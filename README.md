# 방탈출 사용자 예약

## 기능 구현 및 리팩터링

### 추가된 API
- 인기 테마 조회
- 예약 가능한 시간 조회
- 신규 회원가입
- 사용자 로그인
- 사용자 로그인 여부 확인
- 사용자 로그아웃
- 멤버 조회
- 예약 검색

### 예외 처리와 응답
- 아래 상황에 대한 예외 처리를 한다
    - 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때
    - 예약 생성 시 예약자명, 날짜, 시간에 유효하지 않은 값이 입력 되었을 때
    - 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때
    - 존재하지 않는 시간에 대한 예약을 생성하려고 할 때

- 아래 서비스 정책을 적용한다
    - 지나간 날짜와 시간에 대한 예약 생성은 불가능하다
    - 중복 예약은 불가능하다

### 사용자 기능
- 사용자 도메인을 추가한다
- Cookie를 이용하여 로그인 기능을 구현한다
- (관리자가 아닌) 사용자가 예약 가능한 시간을 조회하고, 원하는 시간에 예약할 수 있도록 한다
  - 예약 시 사용자 구분은 어드민과 동일하게 사용자의 이름으로 한다
- 예약 생성 기능을 리팩터링한다
  - 사용자가 예약 생성 시, 로그인한 사용자 정보를 활용한다
  - 관리자가 예약 생성 시, 유저를 조회하여 선택 후 예약을 생성한다

### 관리자 기능
- 어드민 페이지 진입은 admin권한이 있는 사람만 할 수 있도록 제한한다
- 관리자는 admin/reservation 페이지에서 멤버를 직접 선택해 예약할 수 있도록 한다
- 관리자가 조건에 따라 예약을 검색할 수 있도록 한다

## API 명세

### 테마 조회 API

request
```
GET /themes HTTP/1.1
```

response
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

### 테마 추가 API

request
```
POST /themes HTTP/1.1
content-type: application/json

{
  "name": "레벨2 탈출",
  "description": "우테코 레벨2를 탈출하는 내용입니다.",
  "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}
```

response
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

### 테마 삭제 API

request
```
DELETE /themes/1 HTTP/1.1
```
response
```
HTTP/1.1 204
```

### 인기 테마 조회 API

request
```
GET /themes/popular HTTP/1.1
```
response
```
HTTP/1.1 200
Content-Type: application/json

[
  {
    "id": 2,
    "name": "레벨2 탈출",
    "description": "우테코 레벨2를 탈출하는 내용입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
  },
  {
    "id": 1,
    "name": "레벨1 탈출",
    "description": "우테코 레벨1를 탈출하는 내용입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
  }
]
```

### 회원가입 API

request
```
POST /members HTTP/1.1
content-type: application/json
host: localhost:8080

{
    "email": "admin@email.com",
    "password": "password",
    "name": "name"
}
```

response
```
HTTP/1.1 200 OK
```

### 로그인 API

request
```
POST /login HTTP/1.1
content-type: application/json
host: localhost:8080

{
    "password": "password",
    "email": "admin@email.com"
}
```

response
```
HTTP/1.1 200 OK
Content-Type: application/json
Keep-Alive: timeout=60
Set-Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI; Path=/; HttpOnly
```

### 로그아웃 API
request
```
POST /logout HTTP/1.1
cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI
```
response
```
HTTP/1.1 200 OK
Set-Cookie: token=null
```

### 인증 정보 조회 API

request
```
GET /login/check HTTP/1.1
cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
host: localhost:8080
```

response
```
HTTP/1.1 200 OK
Connection: keep-alive
Content-Type: application/json
Date: Sun, 03 Mar 2024 19:16:56 GMT
Keep-Alive: timeout=60
Transfer-Encoding: chunked

{
    "id": 1,
    "name": "어드민"
}
```

### 사용자 조회 API
request
```
GET /members HTTP/1.1
cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI
```
response
```
HTTP/1.1 200
Content-Type: application/json

[
    {
        "id": 1,
        "name": "어드민"
    },
    {
        "id": 2,
        "name": "회원1"
    }
]
```

### 예약 가능한 시간 조회 API
request
```
GET /times/available?date=2025-05-23&themeId=4 HTTP/1.1
```
response
```
[
    {
        "id": 1,
        "startAt": "10:00",
        "alreadyBooked": true
    },
    {
        "id": 2,
        "startAt": "13:00",
        "alreadyBooked": false
    },
    {
        "id": 3,
        "startAt": "15:00",
        "alreadyBooked": false
    },
    {
        "id": 4,
        "startAt": "17:00",
        "alreadyBooked": false
    }
]
```

### 예약 생성 API (일반 유저)
request
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
response
```
HTTP/1.1 201
Content-Type: application/json

{
    "id": 1,
    "date": "2024-03-01",
    "member": {
        "id": 2,
        "name": "회원1"
    },
    "theme": {
        "id": 1,
        "name": "테마",
        "description": "테마입니다.",
        "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
    },
    "time": {
        "id": 1,
        "startAt": "10:00"
    }
}
```

### 예약 생성 API (관리자)
request
```
POST /admin/reservations HTTP/1.1
content-type: application/json
cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI
host: localhost:8080

{
    "date": "2024-03-01",
    "themeId": 1,
    "timeId": 1,
    "memberId": 2
}
```
response
```
HTTP/1.1 201
Content-Type: application/json

{
    "id": 1,
    "date": "2024-03-01",
    "member": {
        "id": 2,
        "name": "회원1"
    },
    "theme": {
        "id": 1,
        "name": "테마",
        "description": "테마입니다.",
        "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
    },
    "time": {
        "id": 1,
        "startAt": "10:00"
    }
}
```

### 예약 검색 API
request
```
GET /admin/reservations?theme=2&member=4&from=2025-04-29&to=2025-05-20 HTTP/1.1
cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI
```
response
```
HTTP/1.1 200
Content-Type: application/json

[
    {
        "id": 5,
        "date": "2025-05-06",
        "member": {
            "id": 4,
            "name": "회원3"
        },
        "theme": {
            "id": 2,
            "name": "테마 B",
            "description": "테마 B입니다.",
            "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        },
        "time": {
            "id": 1,
            "startAt": "10:00"
        }
    },
    {
        "id": 16,
        "date": "2025-05-10",
        "member": {
            "id": 4,
            "name": "회원3"
        },
        "theme": {
            "id": 2,
            "name": "테마 B",
            "description": "테마 B입니다.",
            "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        },
        "time": {
            "id": 3,
            "startAt": "15:00"
        }
    }
]
```
