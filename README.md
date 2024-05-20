# 방탈출 사용자 예약

## 1단계 - 예외 처리와 응답

- [x] 발생할 수 있는 예외 상황에 대한 처리를 하여, 사용자에게 적절한 응답을 한다.
    - [x] 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때
    - [x] 예약 생성 시 예약자명, 날짜, 시간에 유효하지 않은 값이 입력 되었을 때
    - [x] 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때
    - [x] 특정 시간이 존재하지 않는데, 그 시간을 삭제하려 할 때
    - [x] 특정 예약이 존재하지 않는데, 그 예약을 삭제하려 할 때
- [x] 아래와 같은 서비스 정책을 반영한다.
    - [x] 지나간 날짜와 시간에 대한 예약 생성은 불가능하다.
    - [x] 중복 예약은 불가능하다.
    - [x] 중복 예약 시간은 불가능하다.

## 2단계 - 테마 추가

- [x] `/admin/theme` 요청 시 테마 관리 페이지를 응답한다.
- [x] GET `/themes` 요청 시 테마를 전체 조회한다.
- [x] POST `/themes` 요청 시 테마를 추가한다.
    - [x] 테마 생성 시 테마 이름, 설명, 이미지에 유효하지 않은 값이 입력 되었을 때
    - [x] 테마 이미지의 확장자는 jpg, jpeg, png, heic만 가능하다.
- [x] DELETE `/themes/{id}` 요청 시 테마를 삭제한다.
    - [x] 특정 테마가 존재하지 않는데, 그 테마를 삭제하려 할 때 404를 응답한다.
    - [x] 특정 테마에 대한 예약이 존재하는데, 그 테마를 삭제하려 할 때 409를 응답한다.

## 3단계 - 사용자 기능

- [x] `/reservation` 요청 시 사용자 예약 페이지를 응답한다.
    - [x] 사용자는 예약 가능한 시간을 확인한다.
    - [x] 사용자는 원하는 시간에 예약한다.
- [x] `/` 요청 시 인기 테마 페이지를 응답한다.
    - [x] 최근 일주일을 기준으로 하여 해당 기간 내에 방문하는 예약이 많은 테마 10개를 조회한다.

## 4단계 - 사용자 로그인 

- [x] 사용자 도메인을 추가한다.
  - 사용자는 사용자 이름, 이메일, 비밀번호를 가진다.
- [x] GET `/login` 요청 시 로그인 폼이 있는 페이지를 응답한다.
- [x] POST `/login` 요청 시 로그인 요청에 응답한다.
  - 로그인 폼에 입력한 email, password 값을 body에 포함한다.
  - email, password를 이용해서 멤버를 조회한다.
  - 조회한 멤버로 토큰을 만든다.
  - 응답 Cookie에 "token"값으로 토큰이 포함되도록 한다.
- [x] GET `/login/check` 요청 시 인증 정보 조회를 한다.
  - Cookie에서 토큰 정보를 추출한다.
  - 멤버를 찾아 멤버 정보를 응답한다.

## 5단계 - 로그인 리팩터링

- [x] 사용자의 정보를 조회하는 로직을 리팩터링한다.
  - [x] Cookie에 담긴 인증 정보를 이용해서 멤버 객체를 만드는 로직을 분리한다.
- [x] 예약 생성 API 및 기능을 리팩터링한다.
  - [x] 사용자가 예약 생성 시, 로그인한 사용자 정보를 활용하도록 리팩터링한다.
    - [x] reservation.html, user-reservation.js 파일의 TODO 주석을 참고하여 변경된 명세에 맞게 클라이언트가 동작하도록 변경한다.
  - [x] 관리자가 예약 생성 시, 유저를 조회하여 선택 후 예약을 생성하도록 리팩터링한다.
    - [x] admin/reservation-new.html 파일에서 안내된 4단계 관련 주석에 따라, 로딩하는 js 파일을 변경한다.

## 6단계 - 관리자 기능

- [x] 어드민 페이지 진입은 admin권한이 있는 사람만 할 수 있도록 제한한다.
- [x] 관리자가 조건에 따라 예약을 검색할 수 있도록 기능을 추가한다.
  - [x] 예약자별, 테마별, 날짜별 검색 조건을 사용해 예약 검색이 가능하도록 기능을 추가한다.

<br>

## 로그인 

### Request
```
POST /login HTTP/1.1
content-type: application/json
host: localhost:8080

{
"password": "password",
"email": "admin@email.com"
}
```

### Response
```
HTTP/1.1 200 OK
Content-Type: application/json
Keep-Alive: timeout=60
Set-Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI; Path=/; HttpOnly
```

## 인증 정보 조회

### Request
```
GET /login/check HTTP/1.1
cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
host: localhost:8080
```

### Response
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

## 사용자 예약 생성 

### Request
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

## 관리자 예약 생성

### Request
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
