# 🚪 방탈출 사용자 예약

## 📄 API 명세서

### 예약 조회

| HTTP Method | GET           |
|-------------|---------------|
| End Point   | /reservations |
| Status Code | 200 OK        |

#### Response Body

``` json
[
    {
        "id": long,
        "name": String,
        "date": String,
        "time": {
            "id" : long,
            "startAt" : String
        }
        "theme": {
            "id": long,
            "name": String,
            "description": String,
            "thumbnail": String
        }
    },
    {
        ...
    }
]
```

### 이용 가능한 예약 시간 조회

| HTTP Method | GET                                                         |
|-------------|-------------------------------------------------------------|
| End Point   | /reservations/available-times?date={date}&themeId={themeId} |
| Status Code | 200 OK                                                      |

#### Response Body

``` json
[
  {
    "startAt": String,
    "timeId": long,
    "alreadyBooked": boolean
  },
  {
   ...
  }
]
```

### 예약 추가

| HTTP Method | POST          |
|-------------|---------------|
| End Point   | /reservations |
| Status Code | 201 Created   |

#### Request Body

``` json
{
    "name" : String,
    "date" : String,
    "timeId" : long
    "themeId" : long
}
```

#### Response Body

``` json
{
    "id": long,
    "name": String,
    "date": String,
    "time": {
        "id" : long,
        "startAt" : String
    }
    "theme": {
        "id": long,
        "name": String,
        "description": String,
        "thumbnail": String
    }
}
```

### 예약 취소

| HTTP Method | DELETE             |
|-------------|--------------------|
| End Point   | /reservations/{id} |
| Status Code | 204 No Content     |

#### Path Variable

```
id : long
```

### 시간 조회

| HTTP Method | GET    |
|-------------|--------|
| End Point   | /times |
| Status Code | 200 OK |

#### Response Body

``` json
[
    {
        "id": long,
        "startAt": String,
    },
    {
        ...
    }
]
```

### 시간 추가

| HTTP Method | POST        |
|-------------|-------------|
| End Point   | /times      |
| Status Code | 201 Created |

#### Request Body

``` json
{
    "startAt" : String
}
```

#### Response Body

``` json
{
    "id": long,
    "startAt": String
}
```

### 시간 삭제

| HTTP Method | DELETE         |
|-------------|----------------|
| End Point   | /times/{id}    |
| Status Code | 204 No Content |

#### Path Variable

```
id : long
```

### 테마 조회

| HTTP Method | GET     |
|-------------|---------|
| End Point   | /themes |
| Status Code | 200 OK  |

#### Response Body

``` json
[
    {
        "id": long,
        "name": String,
        "description": String,
        "thumbnail": String,
    },
    {
        ...
    }
]
```

### 인기 테마 조회

| HTTP Method | GET              |
|-------------|------------------|
| End Point   | /themes/populars |
| Status Code | 200 OK           |

#### Response Body

``` json
[
    {
        "id": long,
        "name": String,
        "description": String,
        "thumbnail": String,
    },
    {
        ...
    }
]
```

### 테마 추가

| HTTP Method | POST        |
|-------------|-------------|
| End Point   | /themes     |
| Status Code | 201 Created |

#### Request Body

``` json
{
    "name": String,
    "description": String,
    "thumbnail": String,
}
```

#### Response Body

``` json
{
    "id": long,
    "name": String,
    "description": String,
    "thumbnail": String,
}
```

### 테마 삭제

| HTTP Method | DELETE         |
|-------------|----------------|
| End Point   | /themes/{id}   |
| Status Code | 204 No Content |

#### Path Variable

```
id : long
```

---

## 페어 프로그래밍 컨벤션

- 클래스를 정의한 뒤 다음 줄은 공백으로 한다.
- `@Test` ➡️ `@DisplayName` 순으로 작성한다.

## 1단계 요구사항

- [x] 정상 동작 확인
- [x] 시간 관리, 예약 관리 API가 적절한 응답을 하도록 변경
- [x] 발생할 수 있는 예외 상황에 대한 처리를 하여, 사용자에게 적절한 응답
    - [x] 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때
        - [x] null 또는 공백 문자열이 입력된 경우
        - [x] 시작 시간 형식이 HH:mm이 아닌 경우
    - [x] 예약 생성 시 예약자명, 날짜, 시간에 유효하지 않은 값이 입력 되었을 때
        - [x] 이름, 날짜가 null 또는 공백 문자열이 입력된 경우
        - [x] 날짜 형식이 yyyy-MM-dd가 아닌 경우
        - [x] 존재하지 않는 시간 아이디가 입력된 경우
    - [x] 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때
- [x] 지나간 날짜와 시간에 대한 예약 생성은 불가능
- [x] 중복 예약 시간은 불가능
- [x] 중복 예약은 불가능
    - 날짜와 시간이 동일한 경우 중복으로 간주

## 2단계 요구사항

- [x] /admin/theme 요청 시 테마 관리 페이지를 응답(templates/admin/theme.html)
- [x] 예약 페이지 변경(templates/admin/reservation-new.html)
- [x] 스키마 변경
- [x] API
    - [x] 테마 Create
    - [x] 테마 Read
    - [x] 테마 Delete
- [x] reservation -> 객체 필드 변경, dao 변경, dto 변경, 테스트 코드 변경
- [x] 테마 예외 처리
    - [x] 이름, 설명, 썸네일이 null 또는 공백 문자열이 입력된 경우
    - [x] 예약이 존재하면 테마를 삭제할 수 없다.
- [x] 중복 예약 예외처리 수정
    - 날짜, 시간, 테마가 모두 동일한 경우 중복으로 간주

## 3단계 요구사항

- [x] 사용자 예약 페이지 응답(templates/reservation.html)
- [x] 인기 테마 페이지 응답(templates/index.html)
- [x] 사용자 예약
    - [x] 예약 가능한 시간 조회(date, themeId)(GET)(/reservations?date={date}&themeID={themeId})
    - [x] 예약 기능(POST)(/reservations)
    - [x] 클라이언트 코드 수정
    - [x] 오늘 날짜가 요청된 경우 현재 시각 이후의 시간만 보내준다.
- [x] 인기 테마(GET)(/themes/populars)
    - [x] `일주일을 기준으로` 예약이 많은 테마 10개 확인
        - 4월 8일인 경우, 게임 날짜가 4월 1일부터 4월 7일까지인 예약 건수가 많은 순서대로 10개의 테마를 조회
    - [x] 테스트 만들기

## 4단계 요구사항

- [x] 사용자 도메인을 추가
  - name: 사용자 이름
  - email: 이메일
  - password: 비밀번호
  - email을 로그인의 id로, password를 비밀번호로 사용
- [x] GET /login 요청 시 로그인 폼이 있는 페이지 응답(templates/login.html)
- [x] GET /signup 요청 시 회원가입 폼이 있는 페이지 응답(templates/signup.html)
- [x] POST /signup 요청 시 로그인 폼에 입력한 email, password 값을 body에 포함
  - 응답 시 201 created 응답
  - /login 으로 로케이터 지정
  - 이메일이 중복시 예외 발생
- [x] POST /login 요청 시 로그인 폼에 입력한 email, password 값을 body에 포함
  - JWT 토큰 의존성 `build.gradle`에 추가하기 
  - 토큰 발급하기
  - 응답 헤더 `Set-Cookie`에 `token:{access_token}`값으로 토큰을 포함
- [x] 인증 정보 조회
  - 사용자의 정보를 조회하는 API 구현
  - Cookie를 이용하여 로그인 사용자의 정보를 확인

## 5단계 요구사항

- [x] 로그인 리팩터링
  - Cookie에 담긴 인증 정보를 이용하여 멤버 객체를 만드는 로직을 분리
  - HandlerMethodArgumentResolver을 활용
- [x] 사용자 예약 생성 기능 변경
  - 사용자가 예약 생성 시 로그인한 사용자 정보를 활용하도록 리팩터링
  - reservation.html, user-reservation.js 파일의 TODO 주석을 참고
- [x] 예약 생성 기능 변경 - 관리자
  - 관리자가 예약 생성 시 유저를 조회하여 선택 후 예약을 생성하도록 리팩터링
  - admin/reservation-new.html 파일에서 안내된 4단계 관련 주석에 따라 js 파일을 변경
    - AS-IS: /js/reservation-new.js
    - TO-BE: /js/reservation-with-member.js

## 6단계 요구사항

- [x] 접근 권한 제어
  - Member의 Role이 ADMIN 인 사람만 /admin 으로 시작하는 페이지에 접근 가능
  - `HandlerInterceptor`를 활용하여 권한이 없는 경우 요청에 대한 거부 응답
- [ ] 예약 목록 검색
  - 예약자별, 테마별, 날짜별 검색 조건을 사용해 예약 검색 기능 추가

## 추가된 요구사항

- [ ] 로그아웃 기능
