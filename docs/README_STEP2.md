# 기능 구현 (STEP 2)

## 4단계

### 기능 요구사항

[x] - 사용자 도메인을 추가한다.
[ ] - 로그인 기능을 구현하세요.
[ ] - 로그인 후 Cookie를 이용하여 사용자의 정보를 조회하는 API를 구현하세요.

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
host: localhost:8080

{
    "password": "password",
    "email": "admin@email.com"
}
```

**Response**

```http request
HTTP/1.1 200 OK
Content-Type: application/json
Keep-Alive: timeout=60
Set-Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI; Path=/; HttpOnly

```

---

#### 인증 정보 조회 API

**Request**

```http request
GET /login/check HTTP/1.1
cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
host: localhost:8080

```

**Response**

```http request
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

