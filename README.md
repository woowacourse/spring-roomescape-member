# 방탈출 예약 관리

## 기능 목록

<details>
<summary>지난 미션들에서의 기능 목록</summary>
<div markdown="1">

- [x] `localhost:8080/admin` 요청 시 어드민 메인 페이지가 응답할 수 있게 한다.<br>
    - [x] 어드민 메인 페이지는 `templates/admin/index.html` 파일을 이용한다.<br>
- [x] 필요한 의존성을 찾아서 `build.gradle`에 추가한다.<br>
- [x] `/admin/reservation` 요청 시 예약 관리 페이지가 응답할 수 있게 한다.<br>
    - [x] 페이지는 `templates/admin/reservation-legacy.html` 파일을 이용한다.<br>
- [x] API 명세를 따라 예약 관리 페이지 로드 시 호출되는 예약 목록 조회 API도 구현한다.<br>
- [x] API 명세를 따라 예약 추가 API를 구현한다.<br>
- [x] API 명세를 따라 예약 삭제 API를 구현한다.<br>
    - Spring MVC가 제공하는 Annotation을 활용한다.<br>
- [x] 예약 정보의 식별자를 생성할 때 AtomicLong을 활용한다. <br>
- [x] h2 데이터베이스를 연동한다.<br>
- [x] 데이터베이스의 예약 스키마를 추가한다.<br>
- [x] 예약 조회를 구현한다.<br>
- [x] 예약 추가를 구현한다.<br>
- [x] 예약 삭제를 구현한다.<br>
- [x] 시간 추가를 구현한다.<br>
- [x] 시간 조회를 구현한다.<br>
- [x] 시간 삭제를 구현한다.<br>
- [x] 시간 관리 페이지를 구현한다.<br>
- [x] 예약 페이지 파일 수정한다.<br>
- [x] 외래키 지정을 통해 예약 테이블과 예약 시간 테이블의 관계를 설정한다.<br>
- [x] 바탕 코드는 spring-roomescape-admin 미션의 결과물을 사용한다.
- [x] 어드민 페이지에서 기능이 정상 동작하도록 API들을 수정한다.
- [x] 발생할 수 있는 예외 상황에 대한 처리를 한다.
  - [x] 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때
  - [x] 예약 생성 시 예약자명, 날짜, 시간에 유효하지 않은 값이 입력되었을 때
  - [x] 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려고 할 때
  - [x] 지나간 날짜와 시간에 대한 예약 생성을 하려고 할 때
  - [x] 중복 예약을 하려고 할 때
- [x] 테마 도메인을 추가한다.
  - [x] 해당 도메인에 맞게 예약 도메인 또한 수정한다.
  - [x] 어드민에서 방탈출 예약 시, 테마 정보를 포함할 수 있도록 신규 페이지 파일을 사용한다.
- [x]  `/admin/theme` 요청 시 테마 관리 페이지를 응답한다.
- 페이지는 `templates/admin/theme.html` 파일을 이용한다.
- [x] 테마 조회를 구현한다.
- [x] 테마 추가를 구현한다.
- [x] 테마 삭제를 구현한다.
- [x] 테마 관련 예외를 구현한다.
- [x] 사용자가 예약 가능한 시간을 조회할 수 있다.
  - [x] 날짜와 테마를 선택하면 예약 가능한 시간을 확인할 수 있다.
  - [x] 예약 시 사용자 구분은 어드민과 동일하게 사용자의 이름으로 한다.
- [x] 사용자 예약 추가 기능을 구현한다.
- [x] `/reservation` 요청 시 사용자 예약 페이지를 응답한다.
  - [x] 페이지는 `templates/reservation.html` 파일을 이용한다.
- [x] 인기 테마 조회 기능을 구현한다.
  - [x] 최근 일주일을 기준으로, 해당 기간 내에 방문하는 예약이 많은 테마 10개를 확인한다.
- [x] `/` 요청 시 인기 테마 페이지를 응답한다.
  - [x]  페이지는 `templates/index.html` 파일을 이용한다.

</div>
</details>

### 4단계
- [x] 사용자 도메인을 추가한다.
    - [x] 사용자는 name(사용자 이름), email(email), password(비밀번호)를 가진다.
- [x] 로그인 기능을 구현한다.
    - [x] `GET /login` 요청 시 로그인 폼이 있는 페이지`templates/login.htm`를 응답한다.
    - [x] `POST /login` 요청 시 로그인 폼에 입력한 email, password 값을 body에 포함한다.
        - [x] 응답 Cookie에 "token"값으로 토큰을 포함한다.
- [x] 로그인 후 Cookie를 이용하여 사용자의 정보를 조회하는 API를 구현한다.

### 5단계
- [x] 사용자의 정보를 조회하는 로직을 리팩터링한다.
  - [x] Cookie에 담긴 인증 정보를 이용해서 멤버 객체를 만드는 로직을 분리한다.
  - [x] HandlerMethodArgumentResolver을 활용하면 회원정보를 객체를 컨트롤러 메서드에 주입할 수 있다.
- [x] 유저 예약 생성 기능을 리팩터링한다.
  - [x] 예약 생성 시, 로그인한 사용자 정보를 활용하도록 리팩터링 한다.
  - [x] reservation.html, user-reservation.js 파일의 TODO 주석을 참고하여 변경된 명세에 맞게 클라이언트가 동작하도록 변경한다.
- [x] 관리자 예약 생성 기능을 리팩터링한다.
  - [x] 관리자가 예약 생성 시, 유저를 조회하여 선택 후 예약을 생성하도록 한다.
  - [x] admin/reservation-new.html 파일에서 안내된 4단계 관련 주석에 따라, 로딩하는 js 파일을 변경한다.
- [x] 5단계 주석을 검색하여 클라이언트 코드를 수정한다.

### 6단계
- [x] 어드민 페이지 진입은 admin 권한이 있는 사람만 할 수 있도록 제한한다.
- [ ] 관리자가 조건에 따라 예약 목록에서 검색할 수 있다.
  - [ ] themeId, memberId, dateFrom, dateTo 값을 사용한다.
- [ ] 프론트엔드에 따라 추가 요구사항을 구현한다.


## API 명세

<details>
<summary>STEP1에서의 API 명세</summary>
<div markdown="1">

### 예약 목록 조회 API

- Request

```
GET /reservations HTTP/1.1
```

- Response

```
[
    {
        "id": 1,
        "name": "브라운",
        "date": "2023-08-05",
        "time": {
            "id": 1,
            "startAt": "10:00"
        }
    }
]
```

### 예약 추가 API

- Request

```
POST /reservations HTTP/1.1
content-type: application/json
{
    "date": "2023-08-05",
    "name": "브라운",
    "timeId": 1
}
```

- Response

```
HTTP/1.1 200
Content-Type: application/json
{
    "id": 1,
    "name": "브라운",
    "date": "2023-08-05",
    "time" : {
        "id": 1,
        "startAt" : "10:00"
    }
}
```

### 예약 취소 API

- Request

```
DELETE /reservations/1 HTTP/1.1
```

- Response

```
HTTP/1.1 200
```

### 시간 추가 API

- Request

```
POST /times HTTP/1.1
content-type: application/json
{
    "startAt": "10:00"
}
```

- Response

```
HTTP/1.1 200
Content-Type: application/json
{
    "id": 1,
    "startAt": "10:00"
}
```

### 시간 조회 API

- Request

```
GET /times HTTP/1.1
```

- Response

```
HTTP/1.1 200
Content-Type: application/json
[
    {
        "id": 1,
        "startAt": "10:00"
    }
]
```

### 시간 삭제 API

- Request

```
DELETE /times/1 HTTP/1.1
```

- Response

```
HTTP/1.1 200
```

### 테마 조회 API

- Request
  ```
  GET /themes HTTP/1.1
  ```
- Response
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

- Request
  ```
  POST /themes HTTP/1.1
  content-type: application/json
  
  {
      "name": "레벨2 탈출",
      "description": "우테코 레벨2를 탈출하는 내용입니다.",
      "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
  }
  ```
- Response
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

- Request
  ```
  DELETE /themes/1 HTTP/1.1
  ```
- Response
  ```
  HTTP/1.1 204
  ```

### 사용자 예약 가능 시간 API

- Request
  ```
  GET /times/available/date="2024-05-30"&themeId=1 HTTP/1.1
  ```
- Response
  ```
  HTTP/1.1 200
  Content-Type: application/json
  [
    {
        "id": 1,
        "startAt": "10:00"
    }
  ]
  ```

### 인기 테마 API

- Request
  ```
  GET /themes/popular HTTP/1.1
  ```
- Response
  ```
  HTTP/1.1 200
  Content-Type: application/json
  [
    {
      "id":1,
      "name":"테마1",
      "description":"설명1",
      "thumbnail":"https://www.urbanbrush.net/web/wp-content/uploads/edd/2022/08/urbanbrush-20220801083851022216.jpg"
    }
  ]
  ```
  
</div>
</details>



### 로그인 API

- Request
  ```
  POST /login HTTP/1.1
  content-type: application/json
  host: localhost:8080
  
  {
  "password": "password",
  "email": "admin@email.com"
  }
  ```
- Response
  ```
  HTTP/1.1 200 OK
  Content-Type: application/json
  Keep-Alive: timeout=60
  Set-Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI; Path=/; HttpOnly
  ```

### 인증 정보 조회 API

- Request
  ```
  GET /login/check HTTP/1.1
  cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
  host: localhost:8080
  ```
- Response
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
  
### 쿠키를 이용한 예약 생성 API
- Request
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
  - Response
    ```
    HTTP/1.1 200
    Content-Type: application/json
    {
      "id": 1,
      "name": "브라운",
      "date": "2023-08-05",
      "time" : {
        "id": 1,
        "startAt" : "10:00"
      }
    }
    ```
    
### 예약 생성 기능 변경 - 관리자 
- Request
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

- Response
  ```
  HTTP/1.1 200
    Content-Type: application/json
    {
      "id": 1,
      "name": "브라운",
      "date": "2023-08-05",
      "time" : {
        "id": 1,
        "startAt" : "10:00"
      }
    }
  ```

### 관리자 예약 필터 조회 기능
- Request
  ```
  POST /admin/reservations HTTP/1.1
  content-type: application/json
  cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI
  host: localhost:8080
  
  {
    "themeId": 1,
    "memberId": 1,
    "dateForm": "2025-05-21",
    "dateTo": "2025-05-30"
  }
  ```

  - Response
    ```
    HTTP/1.1 200
      Content-Type: application/json
      [
        {
            "id": 1,
            "date": "2024-05-21",
            "member": {
                "id": 2,
                "name": "멤버"
            },
            "time": {
                "id": 1,
                "startAt": "11:59:00"
            },
            "theme": {
                "id": 1,
                "name": "테마1",
                "description": "설명1",
                "thumbnail": "https://www.urbanbrush.net/web/wp-content/uploads/edd/2022/08/urbanbrush-20220801083851022216.jpg"
            }
        },
        {
            "id": 2,
            "date": "2025-05-25",
            "member": {
                "id": 2,
                "name": "멤버"
            },
            "time": {
                "id": 2,
                "startAt": "17:12:00"
            },
            "theme": {
                "id": 2,
                "name": "테마2",
                "description": "설명2",
                "thumbnail": "https://www.urbanbrush.net/web/wp-content/uploads/edd/2022/12/urbanbrush-20221209202526239031.jpg"
            }
        },
        {
            "id": 3,
            "date": "2024-05-29",
            "member": {
                "id": 2,
                "name": "멤버"
            },
            "time": {
                "id": 3,
                "startAt": "11:11:00"
            },
            "theme": {
                "id": 2,
                "name": "테마2",
                "description": "설명2",
                "thumbnail": "https://www.urbanbrush.net/web/wp-content/uploads/edd/2022/12/urbanbrush-20221209202526239031.jpg"
            }
        }
      ]
    ```
