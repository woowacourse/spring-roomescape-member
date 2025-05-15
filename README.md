# 방탈출 예약 관리

## 1단계 - 예외 처리와 응답

- [x] 시간 관리, 예약 관리 API 구현
- [x] 예외 처리
    - [x] 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때 예외가 발생한다.
    - [x] 예약 생성 시 예약자명, 날짜, 시간에 유효하지 않은 값이 입력 되었을 때 예외가 발생한다.
    - [x] 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때 예외가 발생한다.
- [x] 비즈니스 로직 추가
    - [x] 지나간 날짜와 시간에 대한 예약 생성은 불가능하다.
    - [x] 중복 예약은 불가능하다.

## 2단계 - 테마 추가

- [x] 테마를 추가할 수 있다.
    - [x] 예외 처리
        - [x] 테마 추가 시 빈 값이 들어오면 예외가 발생한다.
        - [x] 테마 추가 시 중복된 테마 이름이 들어오면 예외가 발생한다.
        - [x] 테마 추가 시 설명에 5자 이상 100자 이하의 문자열이 들어와야 한다.
        - [x] 테마 추가 시 썸네일에 이미지 형식이 들어오지 않으면 예외가 발생한다.
- [x] 테마를 조회할 수 있다.
- [x] 테마를 삭제할 수 있다.

## 3단계 - 사용자 기능

- [x] 사용자가 예약을 생성한다.
    - [x] 사용자는 날짜와 테마를 선택하면 예약 가능한 시간을 확인할 수 있다.
    - [x] 사용자는 예약 가능한 시간을 확인하고, 원하는 시간에 예약을 할 수 있다.
    - [x] /reservation 요청 시 사용자 예약 페이지를 응답한다.
- [x] 인기 테마를 조회한다.
    - [x] 최근 일주일을 기준으로 하여 해당 기간 내에 방문하는 예약이 많은 테마 10개를 확인할 수 있다.
    - [x] 예를 들어 오늘이 4월 8일인 경우, 게임 날짜가 4월 1일부터 4월 7일까지인 예약 건수가 많은 순서대로 10개의 테마를 조회할 수 있다.
    - [x] / 요청 시 인기 테마 페이지를 응답한다

## 3.5단계 - 회원가입 기능 - 나중

- [ ] 회원가입을 할 수 있다.
    - [ ] 사용자가 이름, 이메일, 비밀번호를 입력하여 회원가입을 할 수 있다.

## 4단계 - 사용자 로그인

- [x] 사용자가 로그인을 할 수 있다.
    - [x] 사용자가 이메일과 비밀번호를 입력하여 로그인 할 수 있다.
    - [x] /login 요청 시 로그인 페이지를 응답한다.
- [x] 사용자의 정보를 조회하여 상단바 우측 로그인 상태를 표현할 수 있다.
    - [x] /login/check 요청 시 사용자의 정보를 조회한다.

## 5단계 - 로그인 리팩터링

- [x] 사용자의 정보를 조회하는 로직을 리팩터링 한다.
    - [x] Cookie에 담긴 인증 정보를 이용해서 멤버 객체를 만드는 로직을 분리한다.
- [x] 예약 생성 API 및 기능을 리팩터링 한다.
    - [x] 관리자가 예약 생성 시, 유저를 조회하여 선택 후 예약을 생성할 수 있다.

## 6단계 - 관리자 기능

- [x] 어드민 페이지 진입은 admin권한이 있는 사람만 할 수 있다.
    - [x] Member의 Role이 ADMIN 인 사람만 /admin 으로 시작하는 페이지에 접근할 수 있다.
    - [x] HandlerInterceptor를 활용하여 권한을 확인하고, 권한이 없는 경우 요청에 대한 거부 응답을 할 수 있다.
- [x] 관리자가 조건에 따라 예약을 검색할 수 있다.
    - [x] 예약자별, 테마별, 날짜별 검색 조건을 사용해 예약 검색이 가능하다.

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
        "id": Long,
        "name": String,
        "date": LocalDate (YYYY-MM-DD),
        "reservationTime": {
            "id": Long,
            "startAt" : LocalTime (HH:mm)
        },
        "themeName": String
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
    "timeId": Long,
    "themeId": Long
}

Response
Content-Type: application/json
HTTP/1.1 200 
{
    "id": Long
}

```

### 예약 삭제

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
    "id": Long
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

### 테마 목록 조회

```
Request
GET /themes HTTP/1.1

Response
HTTP/1.1 200 
Content-Type: application/json

[
   {
        "id": Long,
        "name": String,
        "description": String,
        "thumbnail": String
    }
]
```

### 테마 추가

```
Request
POST /themes HTTP/1.1
content-type: application/json

{
    "name": String
    "description": String,
    "thumbnail": String
}

Response
HTTP/1.1 201
Location: /themes/1
Content-Type: application/json

{
    "id": Long
}
```

### 테마 삭제

```
Request
DELETE /themes/1 HTTP/1.1

Response
HTTP/1.1 204
```

### 로그인 요청

```
Request
POST /login HTTP/1.1
content-type: application/json
host: localhost:8080

{
    "password": String,
    "email": String
}

Response
HTTP/1.1 200 OK
Content-Type: application/json
Keep-Alive: timeout=60
Set-Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI; Path=/; HttpOnly
```

### 사용자 인증 정보 조회

```
Request
GET /login/check HTTP/1.1
cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
host: localhost:8080

Response
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

### 쿠키를 이용한 예약 생성

```
Request
POST /reservations HTTP/1.1
content-type: application/json
cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI
host: localhost:8080

{
    "date": "2024-03-01",
    "themeId": 1,
    "timeId": 1
}
```
