# 4단계 요구사항

- [x] 사용자 도메인을 추가한다.
    - [x] 사용자는 아래의 정보를 가진다.
        - name : 사용자 이름
        - email : 이메일
        - password : 비밀번호
    - [x] email을 로그인의 id로, password를 비밀번호로 사용한다.
    - [x] 사용자 테이블을 생성한다.
- [x] 로그인 기능을 구현한다.
    - [x] `GET /login` 요청 시 로그인 폼이 있는 페이지를 응답한다.
        - [x] `templates/login.html` 파일을 이용한다.
    - [x] `POST /login` 요청 시 로그인 폼에 입력한 email, password 값을 body에 포함한다.
    - [x] 응답 Cookie에 "token" 값으로 토큰이 포함되도록 한다.
- [x] 검증 로직을 추가한다.
    - [x] 토큰 dto
    - [x] Member domain

```
POST /login HTTP/1.1
content-type: application/json
host: localhost:8080

{
    "password": "password",
    "email": "admin@email.com"
}

HTTP/1.1 200 OK
Content-Type: application/json
Keep-Alive: timeout=60
Set-Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI; Path=/; HttpOnly
```

- [x] 로그인 후 Cookie를 이용하여 사용자의 정보를 조회하는 API를 구현한다.
    - [x] 상단바 우측 로그인 상태를 표현해주기 위해 사용자의 정보를 조회하는 API를 구현한다.
    - [x] Cookie를 이용하여 로그인 사용자의 정보를 확인한다.

``` 
GET /login/check HTTP/1.1
cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
host: localhost:8080

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
