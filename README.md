## 🏃‍♀️ 방탈출 사용자 예약

우아한 테크코스 7기 BE 2번째 미션

### ✅ 0단계 - 기본 코드 준비

- [x] 이전 미션의 코드를 가져온다.

### ✅ 1단계 - 예외 처리와 응답

#### 1. 기존 기능 동작 확인

- [x] 기존 관리자 시간/예약 관리 API가 정상 동작하도록 만든다.

#### 2. 예외 처리 구조

- [x] 전역 예외 핸들러 클래스를 생성해 처리한다.

#### 3. 예외 처리 목록

- [x] 시간 추가 시, 시작 시간이 유효하지 않은 경우 예외가 발생한다.
  - [x] 시작 시간이 null인 경우 예외가 발생한다.
  - [x] 시작 시간이 빈 값인 경우 예외가 발생한다.
  - [x] 시작 시간이 HH:mm 형식을 따르지 않는 경우 예외가 발생한다.
- [X] 시간 추가 시, 이미 존재하는 시작 시간을 추가하려는 경우 예외가 발생한다.
- [x] 시간 삭제 시, 존재하지 않는 id를 입력할 경우 예외가 발생한다.
- [x] 시간 삭제 시, 해당 시간에 대한 예약이 존재할 경우 예외가 발생한다.


- [x] 예약 추가 시, 예약자명이 유효하지 않은 경우 예외가 발생한다.
  - [x] 예약자명이 null인 경우 예외가 발생한다.
  - [x] 예약자명이 빈 값인 경우 예외가 발생한다.
- [x] 예약 추가 시, 날짜가 유효하지 않은 경우 예외가 발생한다.
  - [x] 날짜가 null인 경우 예외가 발생한다.
  - [x] 날짜가 빈 값인 경우 예외가 발생한다.
  - [x] 날짜가 yyyy-MM-dd 형식을 따르지 않을 경우 예외가 발생한다.
- [x] 예약 추가 시, 시간이 유효하지 않은 경우 예외가 발생한다.
  - [x] 시간이 null인 경우 예외가 발생한다.
  - [x] 시간이 빈 값인 경우 예외가 발생한다.
  - [x] 시간이 숫자가 아닌 경우 예외가 발생한다.
- [X] 예약 추가 시, 지나간 날짜에 대한 예약을 생성하려는 경우 예외가 발생한다.
- [X] 예약 추가 시, 지나간 시간에 대한 예약을 생성하려는 경우 예외가 발생한다.
- [X] 예약 추가 시, 동일한 날짜, 시간, 테마에 대한 예약을 추가하려는 경우 예외가 발생한다.
- [x] 예약 삭제 시, 존재하지 않는 id를 입력할 경우 예외가 발생한다.

### ✅ 2단계 - 테마 추가

#### 1. 테마 관리 페이지 동작
- [X] 관리자가 테마 관리 페이지에 접속할 수 있도록 한다.
- [X] 수정된 관리자 예약 페이지도 동작할 수 있도록 한다.

#### 2. 테마 조회 API 구현
- [X] 테마를 응답 스펙에 맞게 조회할 수 있다.
  - [X] 요청 스펙: GET, /themes, HTTP/1.1
  - [X] 응답 스펙: HTTP/1.1, 200, application/json


- 응답 예시
````
[
   {
        "id": 1,
        "name": "레벨2 탈출",
        "description": "우테코 레벨2를 탈출하는 내용입니다.",
        "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
    }
]
````

#### 3. 테마 추가 API 구현
- [X] 테마 이름, 설명, 썸네일 URL을 입력해 테마를 추가할 수 있다.
  - [X] 요청 스펙: POST, /themes, HTTP/1.1, application/json
  - [X] 응답 스펙: HTTP/1.1, 201, application/json
- [x] 테마 이름에 유효하지 않은 값을 입력할 시 예외가 발생한다.
  - [x] 테마 이름이 null인 경우 예외가 발생한다.
  - [x] 테마 이름이 빈 값인 경우 예외가 발생한다.
- [x] 설명에 유효하지 않은 값을 입력할 시 예외가 발생한다.
  - [x] 설명이 null인 경우 예외가 발생한다.
  - [x] 설명이 빈 값인 경우 예외가 발생한다.
- [x] 썸네일 URL에 유효하지 않은 값을 입력할 경우 예외가 발생한다.
  - [x] 썸네일 URL이 null인 경우 예외가 발생한다.
  - [x] 썸네일 URL이 빈 값인 경우 예외가 발생한다.
- [x] 테마 추가 시, 중복되는 이름의 테마를 추가하려는 경우 예외가 발생한다.


- 요청 예시
````
{
    "name": "레벨2 탈출",
    "description": "우테코 레벨2를 탈출하는 내용입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}
````


- 응답 예시
````
{
    "id": 1,
    "name": "레벨2 탈출",
    "description": "우테코 레벨2를 탈출하는 내용입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}
````


#### 4. 테마 삭제 API 구현
- [X] 테마 id를 입력해 테마를 삭제할 수 있다.
  - [X] 요청 스펙: DELETE, /themes/1, HTTP/1.1
  - [X] 응답 스펙: HTTP/1.1, 204
- [x] 테마 삭제 시, 존재하지 않는 테마 id를 입력할 경우 예외가 발생한다.
- [x] 테마 삭제 시, 예약에 이용된 테마 id를 입력할 경우 예외가 발생한다.

### ✅ 3단계 - 사용자 기능

#### 1. 사용자 예약
- [x] 사용자는 예약 페이지를 사용할 수 있다.
- [x] 사용자는 날짜와 테마를 선택하면 예약 가능한 시간을 확인할 수 있다.
  - [X] 요청 스펙: GET, /times/available, HTTP/1.1
  - [X] 응답 스펙: HTTP/1.1, 200, application/json
- [x] 예약 시간 확인 시, 유효하지 않은 값을 입력할 경우 예외가 발생한다.
  - [x] 날짜가 null인 경우 예외가 발생한다.
  - [x] 날짜가 빈 값인 경우 예외가 발생한다.
  - [x] 날짜가 yyyy-MM-dd 형식이 아닌 경우 예외가 발생한다.
  - [x] 날짜가 이전 날짜일 경우 예외가 발생한다.
  - [x] 테마 id가 null인 경우 예외가 발생한다.
  - [x] 테마 id가 빈 값인 경우 예외가 발생한다.
  - [x] 테마 id가 문자일 경우 예외가 발생한다.
  - [x] 테마 id가 존재하지 않을 경우 예외가 발생한다.

- 요청 예시
````
localhost:8080/times/available?date=2025-05-02&themeId=1
````

- 응답 예시
````
[
    {
        "timeId": 1,
        "startAt": "10:00",
        "alreadyBooked": false
    },
    {
        "timeId": 2,
        "startAt": "11:00",
        "alreadyBooked": false
    },
    {
        "timeId": 3,
        "startAt": "12:00",
        "alreadyBooked": false
    },
    {
        "timeId": 4,
        "startAt": "13:00",
        "alreadyBooked": false
    },
    {
        "timeId": 5,
        "startAt": "14:00",
        "alreadyBooked": false
    }
]
````

#### 2. 인기 테마
- [X] 인기 테마 페이지를 접속할 수 있다.
- [X] 최근 일주일을 기준으로 하여 해당 기간 내에 방문하는 예약이 많은 테마 10개를 반환한다.
  - [X] 요청 스펙: GET, /themes/popular/weekly, HTTP/1.1
  - [X] 응답 스펙: HTTP/1.1, 200, application/json

- 응답 예시
````
[
    {
        "name": "theme11",
        "description": "description11",
        "thumbnail": "thumbnail11"
    },
    {
        "name": "theme10",
        "description": "description10",
        "thumbnail": "thumbnail10"
    },
    {
        "name": "theme9",
        "description": "description9",
        "thumbnail": "thumbnail9"
    },
    {
        "name": "theme8",
        "description": "description8",
        "thumbnail": "thumbnail8"
    },
    {
        "name": "theme7",
        "description": "description7",
        "thumbnail": "thumbnail7"
    },
    {
        "name": "theme6",
        "description": "description6",
        "thumbnail": "thumbnail6"
    },
    {
        "name": "theme5",
        "description": "description5",
        "thumbnail": "thumbnail5"
    },
    {
        "name": "theme1",
        "description": "description1",
        "thumbnail": "thumbnail1"
    },
    {
        "name": "theme4",
        "description": "description4",
        "thumbnail": "thumbnail4"
    },
    {
        "name": "theme3",
        "description": "description3",
        "thumbnail": "thumbnail3"
    }
]
````

### ✅ 4단계 - 사용자 로그인

#### 1. 사용자 도메인 추가
- [X] 이름(name), 이메일(email), 비밀번호(password)를 저장한다.

#### 2. 로그인 기능
- [X] 사용자는 로그인 페이지에 접속할 수 있다.
- [X] 로그인 폼에 입력된 id(email), 비밀번호(password)를 기반으로 토큰을 반환한다.


- 요청 예시
````
POST /login HTTP/1.1
content-type: application/json
host: localhost:8080

{
    "password": "password",
    "email": "admin@email.com"
}
````

- 응답 예시
````
HTTP/1.1 200 OK
Content-Type: application/json
Keep-Alive: timeout=60
Set-Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI; Path=/; HttpOnly
````


#### 3. 사용자 정보 조회 API 구현
- [X] 요청된 쿠키 정보를 기반으로 사용자의 정보를 반환한다.

- 요청 예시
````
GET /login/check HTTP/1.1
cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
host: localhost:8080
````

- 응답 예시
````
HTTP/1.1 200 OK
Connection: keep-alive
Content-Type: application/json
Date: Sun, 03 Mar 2024 19:16:56 GMT
Keep-Alive: timeout=60
Transfer-Encoding: chunked

{
    "name": "어드민"
}
````

### ✅ 5단계 - 로그인 리팩터링

#### 1. 로그인 리팩터링
- [X] Cookie에 담긴 인증 정보를 이용해서 멤버 객체를 만드는 로직을 분리한다.

#### 2. 예약 생성 기능 변경 - 사용자
- [X] 사용자가 예약 생성 시, 로그인한 사용자 정보를 활용하도록 한다.
- [X] 관련된 클라이언트 코드를 변경한다.

- 요청 예시
````
POST /reservations HTTP/1.1
content-type: application/json
cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI
host: localhost:8080

{
    "date": "2024-03-01",
    "themeId": 1,
    "timeId": 1
}
````

#### 3. 예약 생성 기능 변경 - 관리자
- [X] 관리자가 예약 생성 시, 유저를 조회한다.
- [X] 유저를 선택하여 예약을 생성할 수 있다.
- [X] 관련된 클라이언트 코드를 변경한다.

- 요청 예시
````
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
````

### ✅ 6단계 - 관리자 기능

#### 1. 접근 권한 제어
- [X] Member의 Role이 ADMIN 인 사람만 /admin 으로 시작하는 페이지에 접근할 수 있다.
  - [X] HandlerInterceptor를 활용하여 권한을 확인하고, 권한이 없는 경우 요청에 대한 거부 응답을 한다.

#### 2. 예약 목록 검색
- [X] 예약자별, 테마별, 날짜별 검색 조건을 사용해 예약 검색이 가능하도록 한다.
