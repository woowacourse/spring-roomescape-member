# 🚪 방탈출 사용자 예약

## 📄 API 명세서

### 예약 조회

| HTTP Method | GET           |
|-------------|---------------|
| End Point   | /reservations |
| Status Code | 200 OK        |

#### Response Body

| Name              | Type   | Description                          |
|-------------------|--------|--------------------------------------|
| id                | Number | 예약 ID(고유한 값)                         |
| name              | String | 예약자 이름                               |
| date              | String | 예약한 날짜 `연-월-일`로 표현된다(ex. 2024-05-05) |
| time              | Object | 예약 시간                                |
| time.id           | Number | 예약 시간 ID(고유한 값)                      |
| time.startAt      | String | 예약한 시간 `시간:분`으로 표현된다(ex. 13:00)      |
| theme             | Object | 예약 테마                                |
| theme.id          | Number | 예약 테마 ID(고유한 값)                      |
| theme.name        | String | 예약한 테마 이름                            |
| theme.description | String | 예약한 테마 설명                            |
| theme.thumbnail   | String | 예약한 테마 썸네일 이미지 url                   |

``` json
[
    {
        "id": 1,
        "date": "2024-05-12",
        "member": {
            "id": 1,
            "name": "user"
        },
        "time": {
            "id": 1,
            "startAt": "10:00"
        },
        "theme": {
            "id": 1,
            "name": "방탈출1",
            "description": "1번 방탈출",
            "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        }
    },
    {
        ...
    }
]
```

### 이용 가능한 예약 시간 조회

| HTTP Method | GET                                                        |
|-------------|------------------------------------------------------------|
| End Point   | /reservations/available-time?date={date}&themeId={themeId} |
| Status Code | 200 OK                                                     |

#### Response Body

| Name          | Type    | Description                    |
|---------------|---------|--------------------------------|
| startAt       | String  | 예약 시간 `시간:분`으로 표현된다(ex. 13:00) |
| timeId        | Number  | 예약 시간 ID(고유한 값)                |
| alreadyBooked | boolean | 예약 여부                          |

``` json
[
  {
    "startAt": "12:00",
    "timeId": 1,
    "alreadyBooked": true
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

| Name    | Type   | Description                        |
|---------|--------|------------------------------------|
| name    | String | 예약자 이름                             |
| date    | String | 예약한 날짜 `연-월-일`로 입력(ex. 2024-05-05) |
| timeId  | Number | 예약 시간 ID(고유한 값)                    |
| themeId | Number | 예약 테마 ID(고유한 값)                    |

``` json
{
    "name" : "브라운",
    "date" : "2024-05-05",
    "timeId" : 1,
    "themeId" : 1
}
```

#### Response Body

| Name              | Type   | Description                          |
|-------------------|--------|--------------------------------------|
| id                | Number | 예약 ID(고유한 값)                         |
| name              | String | 예약자 이름                               |
| date              | String | 예약한 날짜 `연-월-일`로 표현된다(ex. 2024-05-05) |
| time              | Object | 예약 시간                                |
| time.id           | Number | 예약 시간 ID(고유한 값)                      |
| time.startAt      | String | 예약한 시간 `시간:분`으로 표현된다(ex. 13:00)      |
| theme             | Object | 예약 테마                                |
| theme.id          | Number | 예약 테마 ID(고유한 값)                      |
| theme.name        | String | 예약한 테마 이름                            |
| theme.description | String | 예약한 테마 설명                            |
| theme.thumbnail   | String | 예약한 테마 썸네일 이미지 url                   |

``` json
[
    {
        "id": 1,
        "name": "브라운",
        "date": "2024-05-05",
        "time": {
            "id" : 1,
            "startAt" : "13:00"
        }
        "theme": {
            "id": 1,
            "name": "방탈출 1",
            "description": "공포 테마 방탈출입니다.",
            "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        }
    },
    {
        ...
    }
]
```

### 예약 취소

| HTTP Method | DELETE             |
|-------------|--------------------|
| End Point   | /reservations/{id} |
| Status Code | 204 No Content     |

#### Path Variable

| Name | Type   | Description      |
|------|--------|------------------|
| id   | Number | 삭제할 예약 ID(고유한 값) |

```
id : 1
```

### 시간 조회

| HTTP Method | GET    |
|-------------|--------|
| End Point   | /times |
| Status Code | 200 OK |

#### Response Body

| Name    | Type   | Description                     |
|---------|--------|---------------------------------|
| id      | Number | 예약 시간 ID(고유한 값)                 |
| startAt | String | 예약한 시간 `시간:분`으로 표현된다(ex. 13:00) |

``` json
[
    {
        "id": 1,
        "startAt": "13:00",
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

| Name    | Type   | Description                     |
|---------|--------|---------------------------------|
| startAt | String | 예약 가능한 시간 `시간:분`으로 입력(ex 13:00) |

``` json
{
    "startAt" : "13:00"
}
```

#### Response Body

| Name    | Type   | Description                     |
|---------|--------|---------------------------------|
| id      | Number | 예약 시간 ID(고유한 값)                 |
| startAt | String | 예약한 시간 `시간:분`으로 표현된다(ex. 13:00) |

``` json
{
    "id": 1,
    "startAt": "13:00"
}
```

### 시간 삭제

| HTTP Method | DELETE         |
|-------------|----------------|
| End Point   | /times/{id}    |
| Status Code | 204 No Content |

#### Path Variable

| Name | Type   | Description      |
|------|--------|------------------|
| id   | Number | 삭제할 시간 ID(고유한 값) |

```
id : 1
```

### 테마 조회

| HTTP Method | GET     |
|-------------|---------|
| End Point   | /themes |
| Status Code | 200 OK  |

#### Response Body

| Name        | Type   | Description        |
|-------------|--------|--------------------|
| id          | Number | 예약 테마 ID(고유한 값)    |
| name        | String | 예약한 테마 이름          |
| description | String | 예약한 테마 설명          |
| thumbnail   | String | 예약한 테마 썸네일 이미지 url |

``` json
[
    {
        "id": 1,
        "name": "방탈출 1번",
        "description": "공포 테마 방탈출입니다.",
        "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg",
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
        "id": 1,
        "name": "방탈출 1번",
        "description": "공포 테마 방탈출입니다.",
        "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
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

| Name        | Type   | Description        |
|-------------|--------|--------------------|
| name        | String | 예약한 테마 이름          |
| description | String | 예약한 테마 설명          |
| thumbnail   | String | 예약한 테마 썸네일 이미지 url |

``` json
{
    "name": "방탈출 1번",
    "description": "공포 테마 방탈출입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg",
}
```

#### Response Body

| Name        | Type   | Description        |
|-------------|--------|--------------------|
| id          | Number | 예약 테마 ID(고유한 값)    |
| name        | String | 예약한 테마 이름          |
| description | String | 예약한 테마 설명          |
| thumbnail   | String | 예약한 테마 썸네일 이미지 url |

``` json
{
    "id": 1,
    "name": "방탈출 1번",
    "description": "공포 테마 방탈출입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg",
}
```

### 테마 삭제

| HTTP Method | DELETE         |
|-------------|----------------|
| End Point   | /themes/{id}   |
| Status Code | 204 No Content |

#### Path Variable

| Name | Type   | Description      |
|------|--------|------------------|
| id   | Number | 삭제할 테마 ID(고유한 값) |

```
id : 1
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

- [x] `GET /login` : 로그인 페이지 응답(templates/login.html)
- [x] `POST /login`
    - [x] Request : 로그인 폼에 입력한 email, password 값을 body에 포함
      ```markdown
      POST /login HTTP/1.1
      content-type: application/json
      host: localhost:8080
      
      {
      "password": "password",
      "email": "admin@email.com"
      }
      ```
    - [x] 사용자 도메인 추가
        - name: 사용자 이름
        - email: 이메일
        - password: 비밀번호
        - email을 로그인의 id로, password를 비밀번호로 사용
    - [x] Response : 응답 Cookie에 "token"값으로 토큰이 포함
        - [x] email과 password를 이용해서 멤버를 조회
        - [x] 조회한 멤버로 토큰 생성
        - [x] Cookie를 만들어 응답
      ```markdown
      HTTP/1.1 200 OK
      Content-Type: application/json
      Keep-Alive: timeout=60
      Set-Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI; Path=/; HttpOnly
      ```
- [x] `GET /login/check` : 사용자의 정보를 조회하는 API
    - [x] Cookie에서 토큰 정보를 추출
    - [x] 멤버를 찾아 멤버 정보를 응답합
    - [x] Request
      ```markdown
      GET /login/check HTTP/1.1
      cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
      host: localhost:8080
      ```
    - [x] Response
      ```markdown
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

## 5단계 요구사항

- [x] Member DB 테이블 생성
  - [x] JdbcMemberDao 생성
- [x] Cookie를 통해 멤버 정보를 조회하는 로직
    - [x] `HandlerMethodArgumentResolver` 컨트롤러에 진입하기 전에 처리
    - [x] `HandlerMethodArgumentResolver` 에서 만든 멤버 정보 객체를 컨트롤러 메서드에서 주입 받아 사용
- [x] 사용자가 예약 생성 시, 로그인한 사용자 정보를 활용하도록 리팩터링
    - Request
      ```markdown
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
- [x] 관리자가 예약 생성 시, 유저를 조회하여 선택 후 예약을 생성하도록 리팩터링
    - [x] 유저 정보를 모두 조회할 수 있는 API 생성 
    - Request
      ```markdown
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
- [x] 존재하지 않는 멤버 아이디로 예약 생성시 예외 발생
