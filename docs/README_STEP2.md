# 기능 구현 (STEP 2)

## 4단계

### 기능 요구사항

[x] - 사용자 도메인을 추가한다.
[x] - 로그인 기능을 구현한다.
[x] - 로그인 후 Cookie를 이용하여 사용자의 정보를 조회하는 API를 구현한다.

### 추가 구현사항

[x] - 회원가입 페이지 렌더링한다.
[x] - 회원가입 요청 API를 구현한다.
[x] - 회원 저장 기능 구현
[x] - 이메일 중복 검사 기능 구현

---

## 5단계

### 기능 요구사항

[x] - 예약 생성 API 및 기능을 리팩터링 한다.
    [x] - 예약 도메인 '이름' 필드 삭제
    [x] - 예약 생성 API 페이로드 수정
[x] - 사용자의 정보를 조회하는 로직을 리팩터링 한다.
    [x] - resolveArgument 등록

--- 

## 6단계 

### 기능 요구사항

[x] - 어드민 페이지 admin권한으로 제한한다 .
    [x] - Member의 Role ADMIN -> /admin/** 접근 가능
    [x] -  HandlerInterceptor 구현
[ ] - 관리자가 조건에 따라 예약을 검색할 수 있도록 기능을 추가하세요.
    [ ] - 예약 검색 기능을 추가합니다.
    [ ] - 검색 조건: 예약자별, 테마별, 날짜별

--- 
### 사용자 도메인 명세

name: 사용자 이름
email: 이메일
password: 비밀번호

email과 password를 로그인 정보로 활용

### Http 명세

**Request**

#### 로그인 페이지 요청

```http request
GET /login
```

**Response**

```http request
templates/login.html
```

---

### 로그인 API

**Request**

```http request
POST /login HTTP/1.1
content-type: application/json

{
    "password": "password",
    "email": "admin@email.com"
}
```

**Response**

```http request
HTTP/1.1 200 OK
Content-Type: application/json
Set-Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI; Path=/; HttpOnly

```

---

#### 인증 정보 조회 API

**Request**

```http request
GET /login/check HTTP/1.1
cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM

```

**Response**

```http request
HTTP/1.1 200 OK


{
    "name": "어드민"
}

```

#### 회원가입 API

**Request**

```http request
POST /signup HTTP/1.1
Content-Type: application/json

{ 
    "name": "hello",
    "email": "admin@email.com",
    "password": "password"
}
```

**Response**

```http request
HTTP/1.1 200 OK


{
    "name": "어드민"
}

```

### 사용자 예약 추가 API

**Request**

```http request
POST /reservations HTTP/1.1
cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI
content-type: application/json
host: localhost:8080

{
    "date": "2023-08-05",
    "memberId": 1,
    "timeId": 1,
    "themeId": 1
}
```

**Response**

```http request
HTTP/1.1 201
Content-Type: application/json

{
    "id": 1,
    "date": "2023-08-05",
    "time" : {
        "id": 1,
        "startAt" : "10:00"
    },
    "theme": {
        "id": 1,
        "name": "레벨2 탈출",
        "description": "우테코 레벨2를 탈출하는 내용입니다.",
        "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
    }
}
```

### 관리자 예약 추가 API

**Request**

```http request
POST /admin/reservations HTTP/1.1
cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI
content-type: application/json
host: localhost:8080

{
    "date": "2023-08-05",
    "timeId": 1,
    "themeId": 1
}
```

**Response**

```http request
HTTP/1.1 201
Content-Type: application/json

{
    "id": 1,
    "date": "2023-08-05",
    "time" : {
        "id": 1,
        "startAt" : "10:00"
    },
    "theme": {
        "id": 1,
        "name": "레벨2 탈출",
        "description": "우테코 레벨2를 탈출하는 내용입니다.",
        "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
    }
}
```
