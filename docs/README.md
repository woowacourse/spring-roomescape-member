# spring-roomescape-admin

## 1. 기능 요구 사항

### 어드민 페이지
- localhost:8080/admin 요청 시 어드민 메인 페이지가 응답할 수 있도록 구현한다.
- /admin/reservation 요청 시 예약 관리 페이지가 응답할 수 있도록 구현한다.
- /admin/time 요청 시 시간 관리 페이지가 응답할 수 있도록 구현한다.
- /admin/theme 요청 시 테마 관리 페이지가 응답할 수 있도록 구현한다.

### 사용자 페이지
- localhost:8080 요청 시 인기 테마 페이지가 응답할 수 있도록 구현한다.
- localhost:8080/reservation 요청 시 사용자 예약 페이지가 응답할 수 있도록 구현한다.
- localhost:8080/login 요청 시 사용자 로그인 페이지가 응답할 수 있도록 구현한다.

### 기능
- 예약 관리 페이지 로드 시 호출되는 예약 목록 조회 API도 함께 구현한다.
- 예약 추가 API와 삭제 API를 구현한다.
- 시간 관리 페이지 로드 시 호출되는 시간 목록 조회 API도 함께 구현한다.
- 시간 추가 API와 삭제 API를 구현한다. 
- 테마 관리 페이지 로드 시 호출되는 테마 목록 조회 API도 함께 구현한다.
- 테마 추가 API와 삭제 API를 구현한다.
- 예약 가능 시간 조회 API를 구현한다.
- 인기 테마 목록 조회 API를 구현한다.
- 로그인 요청 API를 구현한다.
- 로그인 인증 정보 조회 API를 구현한다.

## 2. 요구 사항 목록
### 어드민 페이지
- [x] 어드민 페이지를 응답한다.
- [x] 예약 페이지를 응답한다.
- [x] 테마 페이지를 응답한다.
- [x] 예약 목록을 조회한다.
- [x] 예약을 추가한다.
  - [x] 예약 생성 시 예약자명, 날짜, 시간에 유효하지 않은 값이 입력 되었을 때 예외로 처리한다.
    - 이름
      - [x] 예약자명의 길이가 1글자 미만, 20글자 초과할 때 예외로 처리한다.
      - [x] 한글, 영문, 숫자를 제외한 값이 입력 되었을 때 예외로 처리한다.
    - 날짜
      - [x] 0000-01-01 ~ 9999-12-31의 범위 밖의 값이 입력 되었을 때 예외로 처리한다.
      - [x] 현재 날짜보다 지나간 날짜가 입력 되었을 때 예외로 처리한다.
      - [x] 빈 값이 입력 되었을 때 예외로 처리한다.
    - 시간
      - [x] 빈 값이 입력 되었을 때 예외로 처리한다.
  - [x] 특정 시간에 대한 예약이 존재하는데, 그 시간과 같은 시간에 예약을 하려고 할 때 예외로 처리한다.
- [x] 예약을 삭제한다.
  - [x] 존재하지 않는 예약을 삭제하려고 할 때 예외로 처리한다.
- [x] 예약 시간 목록을 조회한다.
- [x] 예약 시간을 추가한다.
  - [x] 빈 값이 입력 되었을 때 예외로 처리한다.
  - [x] Date, Time으로 인식되지 않는 값이 입력 되었을 때 예외로 처리한다.
  - [x] 특정 예약 시간이 존재하는데, 그 시간과 같은 예약 시간을 추가하려고 할 때 예외로 처리한다.
- [x] 예약 시간을 삭제한다.
  - [x] 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때 예외로 처리한다.
  - [x] 존재하지 않는 예약 시간을 삭제하려고 할 때 예외로 처리한다.
- [x] 테마 목록을 조회한다.
- [x] 테마를 추가한다.
  - [x] 테마 생성 시 테마명, 설명, 이미지에 유효하지 않은 값이 입력 되었을 때 예외로 처리한다.
    - 테마명
      - [x] 빈 값이 입력 되었을 때 예외로 처리한다.
    - 설명
      - [x] 빈 값이 입력 되었을 때 예외로 처리한다.
    - 이미지
      - [x] 빈 값이 입력 되었을 때 예외로 처리한다.
- [x] 테마를 삭제한다.
  - [x] 존재하지 않는 테마를 삭제하려고 할 때 예외로 처리한다.
  - [x] 특정 테마에 대한 예약이 존재하는데, 그 테마를 삭제하려 할 때 예외로 처리한다.

### 사용자 페이지
- [x] 예약 가능 시간을 조회한다.
- [x] 예약을 추가한다.
- [x] 인기 테마 목록을 조회한다.
  - [x] 인기 테마의 조회 시작일, 종료일, 테마 개수는 사용자의 요청에 따라 동적으로 처리한다. 
  - [x] 최근 일주일 기준 방문이 많은 테마 상위 10개를 조회한다.
- [ ] 로그인 페이지를 응답한다.

### 로그인 기능
- [ ] 로그인을 요청한다.
  - [ ] Email과 Password를 이용해서 멤버를 조회한다.
  - [ ] 조회한 멤버로 토큰을 제작한다.
  - [ ] Cookie를 만들어 응답한다.
- [ ] 인증 정보를 조회한다.
  - [ ] Cookie에서 토큰 정보를 추출한다.
  - [ ] 멤버를 찾아 멤버 정보를 응답한다.

## 3. API 명세

### 예약 조회
- Request
```http request
GET /reservations HTTP/1.1
```
- Response
```http request
HTTP/1.1 200
Content-Type: application/json
```
```json
{
  "responses": [
    {
      "id": 1,
      "name": "브라운",
      "date": "2024-08-05",
      "time": {
        "id": 1,
        "startAt": "10:00"
      },
      "theme": {
        "id": 1,
        "name": "테마명01",
        "description": "테마 설명01",
        "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
      }
    }
  ]
}
```

### 예약 단건 조회
- Request
```http request
GET /reservations/1 HTTP/1.1
```
- Response
```http request
HTTP/1.1 200
Content-Type: application/json
```
```json
{
  "id": 1,
  "name": "브라운",
  "date": "2024-08-05",
  "time": {
    "id": 1,
    "startAt": "10:00"
  },
  "theme": {
    "id": 1,
    "name": "테마명01",
    "description": "테마 설명01",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
  }
}
```

### 예약 추가
- Request
```http request
POST /reservations HTTP/1.1
content-type: application/json
```
```json
{
    "date": "2024-08-05",
    "name": "브라운",
    "timeId": 1,
    "themeId": 1
}
```
- Response
```http request
HTTP/1.1 201
Content-Type: application/json
```
```json
{
    "id": 1,
    "name": "브라운",
    "date": "2024-08-05",
    "time" : {
        "id": 1,
        "startAt" : "10:00"
    },
    "theme": {
        "id": 1,
        "name": "테마명",
        "description": "테마 설명",
        "thumbnail": "테마 이미지"
    }
}
```

### 예약 취소
- Request
```http request
DELETE /reservations/1 HTTP/1.1
```
- Response
```http request
HTTP/1.1 204
```

### 예약 시간 조회
- Request
```http request
GET /times HTTP/1.1
```
- Response
```http request
HTTP/1.1 200 
Content-Type: application/json
```
```json
{
  "responses": [
    {
      "id": 1,
      "startAt": "10:00"
    }
  ]
}
```

### 예약 시간 단건 조회
- Request
```http request
GET /times/1 HTTP/1.1
```
- Response
```http request
HTTP/1.1 200 
Content-Type: application/json
```
```json
{
  "id": 1,
  "startAt": "10:00"
}
```

### 예약 시간 추가
- Request
```http request
POST /times HTTP/1.1
content-type: application/json
```
```json
{
    "startAt": "10:00"
}
```
- Response
```http request
HTTP/1.1 201
Content-Type: application/json
```
```json
{
    "id": 1,
    "startAt": "10:00"
}
```

### 예약 시간 삭제
- Request
```http request
DELETE /times/1 HTTP/1.1
```
- Response
```http request
HTTP/1.1 204
```

### 테마 조회
- Request
```http request
GET /themes HTTP/1.1
```
- Response
```http request
HTTP/1.1 200
Content-Type: application/json
```
```json
{
  "responses": [
    {
      "id": 1,
      "name": "레벨2 탈출",
      "description": "우테코 레벨2를 탈출하는 내용입니다.",
      "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
    }
  ]
}
```

### 테마 단건 조회
- Request
```http request
GET /themes/1 HTTP/1.1
```
- Response
```http request
HTTP/1.1 200
Content-Type: application/json
```
```json
{
  "id": 1,
  "name": "레벨2 탈출",
  "description": "우테코 레벨2를 탈출하는 내용입니다.",
  "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}
```

### 테마 추가
- Request
```http request
POST /themes HTTP/1.1
content-type: application/json
```
```json
{
  "name": "레벨2 탈출",
  "description": "우테코 레벨2를 탈출하는 내용입니다.",
  "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}
```
- Response
```http request
HTTP/1.1 201
Location: /themes/1
Content-Type: application/json
```
```json
{
  "id": 1,
  "name": "레벨2 탈출",
  "description": "우테코 레벨2를 탈출하는 내용입니다.",
  "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}
```

### 테마 삭제
- Request
```http request
DELETE /themes/1 HTTP/1.1
```
- Response
```http request
HTTP/1.1 204
```

### 예약 가능 시간 조회
- Request
```http request
GET /reservations/available?date=${date}&themeId=${themeId} HTTP/1.1
content-type: application/json
```
- Response
```json
{
  "responses": [
    {
      "startAt": "10:00",
      "timdId": "1",
      "alreadyBooked": false
    }
  ]
}
```

### 인기 테마 목록 조회
- Request
```http request
GET /themes/hot?start=${start}&end=${end}&limit=${limit}&offset=${offset} HTTP/1.1
content-type: application/json
```
- Response
```json
{
  "responses": [
    {
      "id" : "1",
      "name": "테마명",
      "thumbnail": "테마 이미지",
      "description": "테마 설명"
    }
  ]
}
```

### 로그인 요청
- Request
```http request
POST /login HTTP/1.1
content-type: application/json
host: localhost:8080
```
```json
{
    "password": "password",
    "email": "admin@email.com"
}
```
- Response
```http request
HTTP/1.1 200 OK
Content-Type: application/json
Keep-Alive: timeout=60
Set-Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI; Path=/; HttpOnly
```

### 인증 정보 조회
- Request
```http request
GET /login/check HTTP/1.1
cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
host: localhost:8080
```

- Response
```http request
HTTP/1.1 200 OK
Connection: keep-alive
Content-Type: application/json
Date: Sun, 03 Mar 2024 19:16:56 GMT
Keep-Alive: timeout=60
Transfer-Encoding: chunked
```
```json
{
  "name": "어드민"
}
```
