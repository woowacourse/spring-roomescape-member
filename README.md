# 방탈출 사용자 예약

## 요구사항

- [X] 시간 관리, 예약 관리 API가 적절한 응답을 하도록 변경
- [X] 어드민의 시간 관리 페이지, 방탈출 예약 페이지에서 모든 기능이 정상적으로 동작하는지 확인합니다.

## 예약
- [X] 사용자는 원하는 시간에 예약을 할 수 있다.
- [X] 사용자는 날짜와 테마를 선택하면 예약 가능한 시간을 확인할 수 있다.
- [X] `/reservation` 요청 시 사용자 예약 페이지를 응답한다.
  - 페이지는 `templates/reservation.html` 파일을 이용
- [X] 사용자는 쿠키에 담긴 정보를 이용하여 예약할 수 있다.
- [X] 관리자는 사용자의 ID를 이용하여 예약할 수 있다.
- [X] 관리자 페이지에서 예약 조건에 맞는 예약 목록을 검색한다.
 
## 로그인
 - [X] `GET /login` 요청 시 로그인 폼이 있는 페이지를 응답한다.
 - `templates/login.html` 파일을 이용
 - [X] `POST /login` 요청 시 body에 포함된 email, password 값을 이용하여 cookie에 "token" 값으로 토큰이 포함되도록 응답
 - [X] `GET /login/check` 요청 시 Cookie에서 토큰 정보를 추출하여 멤버를 찾아 멤버 정보 응답
 - [X] `Member`의 `ROLE`이 `ADMIN`인 사람만 `/admin`으로 시작하는 페이지에 접근할 수 있다.

### 사용자
- [X] `GET /members` 요청 시 사용자의 목록을 반환한다

### 사용자 인기 테마 조회
- [X] 최근 일주일을 기준으로 하여 해당 기간 내에 방문하는 예약이 많은 테마 10개를 확인할 수 있다.
  - 오늘이 4월 8일인 경우, 게임 날짜가 4월 1일부터 4월 7일까지인 예약 건수가 많은 순서대로 10개의 테마를 조회할 수 있다.
- [X] `/` 요청 시 인기 테마 페이지를 응답한다.
  - 페이지는 `templates/index.html` 파일을 이용

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
- [X] 동일한 시간, 날짜, 테마에 대한 중복 에약이 불가능하다.
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

  - Request
    ```
    GET /reservation/available HTTP/1.1
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

- Request
  ```
  GET /themes/ranking HTTP/1.1
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
  
### 사용자 예약 생성 API

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

### 관리자 예약 생성 API

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

### 사용자 목록 조회 API

- Request
  ```
  GET /members HTTP/1.1
  content-type: application/json
  
  {
    "id": "2024-03-01",
    "name": 1,
    "email": 1
  }
  ```

- Response
  ```
  HTTP/1.1 200

  [
    {
      "id": 1,
      "name": "루키",
      "email": "rookie123@woowa.com"
    },
    {
      "id": 2,
      "name": "하루",
      "email": "haru123@woowa.com"
    },
  ]
  ```
