# 방탈출 사용자 예약

## 요구사항

- [X] 시간 관리, 예약 관리 API가 적절한 응답을 하도록 변경
- [X] 어드민의 시간 관리 페이지, 방탈출 예약 페이지에서 모든 기능이 정상적으로 동작하는지 확인합니다.

### 사용자 예약

- [X] 사용자는 원하는 시간에 예약을 할 수 있다.
- [X] 사용자는 날짜와 테마를 선택하면 예약 가능한 시간을 확인할 수 있다.
- [X] `/reservation` 요청 시 사용자 예약 페이지를 응답한다.

### 사용자 인기 테마 조회

- [X] 최근 일주일을 기준으로 하여 해당 기간 내에 방문하는 예약이 많은 테마 10개를 확인할 수 있다.
  - 오늘이 4월 8일인 경우, 게임 날짜가 4월 1일부터 4월 7일까지인 예약 건수가 많은 순서대로 10개의 테마를 조회할 수 있다.
- [X] `/` 요청 시 인기 테마 페이지를 응답한다.

### 사용자 로그인

- [X] 사용자는 이메일, 비밀번호로 로그인 할 수 있다.
- [X] `/login` 요청 시 로그인 페이지를 응답한다.

### 사용자 인증 정보 조회

- [X] `/login/check` 요청 시 사용자 인증 정보를 응답한다.

### 사용자 로그아웃

- [X] 사용자는 로그아웃 할 수 있다.
- [X] `/logout` 요청 시 만료 Token을 응답한다.

### 사용자 회원가입

- [X] 사용자는 이메일, 비밀번호, 이름으로 회원가입 할 수 있다.
- [X] `/signup` 요청 시 로그인 페이지를 응답한다.
- [X] `/members` 요청 시 사용자 정보를 저장한다.

### 사용자 예약 생성

- [X] 사용자는 날짜, 테마, 시간을 선택하여 예약을 생성 할 수 있다.
- [X] `/reservations` 요청 시 예약을 생성한다.

### 관리자 예약 생성

- [X] 관리자는 유저를 조회하여 선택 후 예약을 생성 할 수 있다.
- [X] `/admin/reservations` 요청 시 예약을 생성한다.

### 관리자 기능

- [X] 관리자 페이지 진입은 admin 권한이 있는 사람만 입장 할 수 있다.
- [X] 관리자는 조건에 따라 예약을 검색 할 수 있다.

## 유효성 검증

### 시간

- [X] 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때
  - null, 공백
  - "HH:mm" 형식에 올바르지 않은 경우
- [X] 특정 시간에 대한 현예약이 존재하는데, 그 시간을 삭제하려 할 때
- [X] 동일한 시간은 생성할 수 없다.

### 날짜

- [X] 지나간 날짜와 시간에 대한 예약 생성은 불가능하다.
  - 지난 날짜인 경우 불가능
  - 같은 날짜이면서 시간이 지난 경우 불가능
  - 미래 날짜인데 시간이 현재보다 과거인 경우는 가능

### 예약

- [X] 예약 생성 시 예약자명, 날짜, 시간에 유효하지 않은 값이 입력 되었을 때
- [X] 중복 예약은 불가능하다.
  - ex. 이미 4월 1일 10시에 예약이 되어있다면, 4월 1일 10시에 대한 예약을 생성할 수 없다.
- [X] 예약 생성, 조회, 삭제 시 테마 데이터를 추가하여 반환

### 테마

- [X] 요청 데이터 유효성 검증
  - null, 공백
- [X] 동일한 시간, 날짜, 테마에 대한 중복 예약이 불가능하다.
- [X] 해당 테마에 대한 예약이 있을 경우, 삭제가 불가능하다.

---

## API 명세

### 테마 조회

- [X] 테마 조회 API를 구현한다.
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

### 테마 추가

- [X] 테마 추가 API를 구현한다.
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

### 테마 삭제

- [X] 테마 삭제 API를 구현한다.
  - Request
    ```
    DELETE /themes/1 HTTP/1.1
    ```

  - Response
    ```
    HTTP/1.1 204
    ```

### 사용자 예약 가능 시간 조회

- [X] 사용자 예약 가능 시간 조회 API를 구현한다.
  - Request
    ```
    GET /themes/rankings HTTP/1.1
    ```

  - Response
    ```
    HTTP/1.1 200

    [
      {
        "startAt": "10:00",
        "timeId": "1",
        "alreadyBooked": "true"
      },
      {
        "startAt": "10:10",
        "timeId": "2",
        "alreadyBooked": "false"
      }
    ]
    ```

### 사용자 인기 테마 조회

- [X] 사용자 인기 테마 조회 API를 구현한다.
  - Request
    ```
    GET /themes HTTP/1.1
    ```

  - Response
    ```
    HTTP/1.1 200

    [
      {
        "name": "레벨1 탈출",
        "description": "우테코 레벨1를 탈출하는 내용입니다.",
        "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
      },
      {
        "name": "레벨2 탈출",
        "description": "우테코 레벨2를 탈출하는 내용입니다.",
        "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d45.jpg"
      }
    ]
    ```

### 사용자 로그인

- [X] 사용자 로그인 API를 구현한다.
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

### 사용자 인증 정보 조회

- [X] 사용자 인증 정보 조회 API를 구현한다.
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

### 사용자 로그아웃

- [X] 사용자 로그아웃 API를 구현한다.
  - Request
    ```
    POST /logout HTTP/1.1
    cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
    credentials: 'include'
    ```

  - Response
    ```
    HTTP/1.1 200 OK
    Connection: keep-alive
    Content-Type: application/json
    Keep-Alive: timeout=60
    Set-Cookie: token=null; Path=/; HttpOnly
    ```

### 사용자 회원가입

- [X] 사용자 회원가입 API를 구현한다.
  - Request
    ```
    POST /members HTTP/1.1
    content-type: application/json
    host: localhost:8080
  
    {
        "email": "admin@email.com"
        "password": "password",
        "name": "name"
    }
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
        "name": "name"
    }
    ```

### 사용자 예약 생성

- [X] 쿠키를 이용한 사용자 예약 생성 API를 구현한다.
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
    HTTP/1.1 200 OK
    [
      {
          "id": 1,
          "member": {
            "name": "name"
          },
          "theme": {
            "name": "우테코 레벨1 탈출"
          },
          "date": "2025-05-15",
          "time": {
            "startAt": "14:00"
          }
      }
    ]
    ```

### 관리자 예약 생성

- [X] 관리자 예약 생성 API를 구현한다.
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
    HTTP/1.1 200 OK
    [
      {
          "id": 1,
          "member": {
            "name": "name"
          },
          "theme": {
            "name": "우테코 레벨1 탈출"
          },
          "date": "2025-05-15",
          "time": {
            "startAt": "14:00"
          }
      }
    ]
    ```
