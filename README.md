# 기능 구현

## 1단계

- [x] 바뀐 API 스펙에 맞게 응답 코드를 수정한다
- [x] 시간 추가에 대한 예외를 처리한다
    - [x] 빈 값 혹은 형식이 다른 시간
    - [x] 중복된 시간 (해당 시간이 이미 존재)
- [x] 시간 삭제에 대한 예외를 처리한다
    - [x] 예약이 존재하는 시간
    - [x] 존재 하지 않는 시간
- [x] 예약 추가에 대한 예외를 처리한다
    - [x] 빈 값 혹은 형식이 다른 예약
    - [x] 중복된 예약 (해당 시간대에 이미 예약이 존재)
    - [x] 지나간 날짜와 시간에 대한 예약
- [x] 예약 삭제에 대한 예외를 처리한다
    - [x] 존재 하지 않는 예약

## 2단계

- [x] 테마 테이블을 생성한다
- [x] 테마 도메인을 추가한다
- [x] 테마 목록 조회 API를 구현한다
- [x] 테마 추가 API를 구현한다
    - [x] 빈 값 혹은 형식이 다른 테마
- [x] 테마 삭제 API를 구현한다
    - [x] 예약이 존재하는 테마
    - [x] 존재 하지 않는 테마
- [x] 예약 도메인에 테마 필드를 추가한다
- [x] 예약 목록 조회 API에 테마 필드를 추가한다
- [x] 예약 추가 API에 테마 필드를 추가한다
- [x] 예약 단권 조회 쿼리에 테마 필드를 추가한다 (예약 삭제 API 관련)
- [x] /admin/theme 페이지를 제공한다
- [x] 중복된 예약 검사 시 시간대와 테마를 함께 검사한다

## 3단계

- [x] 사용자 예약 페이지를 제공한다
- [x] 예약 가능 시간 목록 조회 API를 구현한다
    - [x] 날짜와 테마id로 필터링
    - [x] 예약이 불가능한 시간을 플래그로 구분
- [x] 사용자 예약 페이지에서 예약 추가 API를 사용한다
- [x] 인기 테마 조회 API를 구현한다 (인기 테마는 최근 일주일동안 예약 많은 테마)
    - [x] 예약 건수 많은 순서대로 10개 테마

## 4단계

- [x] 사용자 테이블을 생성한다
- [x] 사용자 도메인을 추가한다 (name(사용자 이름), email(이메일), password(비밀번호))
- [x] 로그인 폼 페이지를 제공한다
- [x] 로그인 API를 구현한다
    - [x] 요청 body에 담은 이메일과 비밀번호로 로그인
    - [x] 응답 Cookie에 "token" 값으로 토큰 포함
- [x] 인증 정보 조회 API를 구현한다 (로그인한 사용자 정보)
- [x] 로그아웃 API를 구현한다

## 5단계

- [x] Cookie의 인증 정보로 사용자 객체 만드는 로직을 분리한다
    - [x] HandlerMethodArgumentResolver 이용해 사용자 객체를 컨트롤러 메서드에 주입
    - [x] 인증 정보 조회 로직 리팩토링
- [x] 사용자가 예약 생성 시, 로그인한 사용자 정보 이용하도록 리팩토링한다
    - [x] 로그인 안한 사용자는 예외 발생
    - [x] reservation.html, user-reservation.js 파일을 변경된 명세에 맞게 수정
- [x] 사용자 목록 조회 API를 구현한다
- [x] 관리자가 예약 생성 시, 사용자 목록 조회해 선택하도록 리팩토링한다
    - [x] memberId 인자로 전달한 정보로 예약 생성
    - [x] admin/reservation-new.html 파일에서 로딩하는 js 파일을 변경

## 6단계

- [x] 사용자 도메인에 role 필드를 추가한다
- [ ] 토큰 생성 시 토큰에 role을 포함한다
- [ ] 어드민 페이지 진입은 admin 권한이 있는 사람만 할 수 있도록 제한한다
    - [ ] HandlerInterceptor를 활용해 권한 확인하고, 권한 없는 경우 요청에 대한 거부 응답
- [ ] 예약 검색 API를 구현한다
    - [ ] 관리자가 조건에 따라 예약 검색
    - [ ] 예약자별(memberId), 테마별(themeId), 기간(날짜)별(dateFrom, dateTo) 검색 조건 사용해 검색

## 선택 단계

- [ ] 회원 가입 페이지를 제공한다
- [ ] 회원 가입 API를 구현한다
- [ ] 관리자 접근 권한 걸린 api 분리해, 관리자만 호출할 수 있도록 제한한다

# API 명세

## 인증

### 로그인 API

Request

```
POST /login
Content-Type: application/json

{
    "password": "password",
    "email": "admin@email.com"
}
```

Response

```
HTTP/1.1 200 OK
Content-Type: application/json
Set-Cookie: token=hello.example.token; Path=/; HttpOnly
```

### 로그아웃 API

Request

```
POST /logout
Cookie: token=hello.example.token
```

Response

```
HTTP/1.1 200 OK
```

### 인증 정보 조회 API

Request

```
GET /login/check
Cookie: token=hello.example.token
```

Response

```
HTTP/1.1 200 OK
Content-Type: application/json

{
    "name": "어드민"
}
```

## 사용자

### 사용자 목록 조회 API (접근 권한: 관리자)

Request

```
GET /members
```

Response

```
HTTP/1.1 200
Content-Type: application/json

[
    {
        "id": 1,
        "name": "브라운",
        "email": "admin@email.com"
    }
]
```

## 예약

### 예약 목록 조회 API (접근 권한: 관리자)

Request

```
GET /reservations
```

Response

```
HTTP/1.1 200
Content-Type: application/json

[
    {
        "id": 1,
        "name": "브라운",
        "date": "2023-08-05",
        "time": {
            "id": 1,
            "startAt": "10:00"
        },
        "theme": {
            "id": 1,
            "name": "레벨2 탈출"
        }
    }
]
```

### 예약 검색 API (접근 권한: 관리자)

Request

```
GET /reservations/search?theme-id={$}&member-id={$}&date-from={$}&date-to={$}
```

Response

```
HTTP/1.1 200
Content-Type: application/json

[
    {
        "id": 1,
        "name": "브라운",
        "date": "2023-08-05",
        "time": {
            "id": 1,
            "startAt": "10:00"
        },
        "theme": {
            "id": 1,
            "name": "레벨2 탈출"
        }
    }
]
```

### 예약 추가 API

Request

```
POST /reservations
Cookie: token=hello.example.token
Content-Type: application/json

{
    "date": "2023-08-05",
    "themeId": 1,
    "timeId": 1
}
```

Response

```
HTTP/1.1 201
Content-Type: application/json

{
    "id": 1,
    "name": "브라운",
    "date": "2023-08-05",
    "time": {
        "id": 1,
        "startAt": "10:00"
    },
    "theme": {
        "id": 1,
        "name": "레벨2 탈출"
    }
}
```

### 관리자용 예약 추가 API (접근 권한: 관리자)

Request

```
POST /admin/reservations
Cookie: token=hello.example.token
Content-Type: application/json

{
    "date": "2023-08-05", 
    "themeId": 1,
    "timeId": 1,
    "memberId": 1
}
```

Response

```
HTTP/1.1 201
Content-Type: application/json

{
    "id": 1,
    "name": "브라운",
    "date": "2023-08-05",
    "time": {
        "id": 1,
        "startAt": "10:00"
    },
    "theme": {
        "id": 1,
        "name": "레벨2 탈출"
    }
}
```

### 예약 삭제 API (접근 권한: 관리자)

Request

```
DELETE /reservations/1
```

Response

```
HTTP/1.1 204
```

## 시간

### 시간 목록 조회 API (접근 권한: 관리자)

Request

```
GET /times
```

Response

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

### 예약 가능 시간 목록 조회 API

Request

```
GET /times/available?date={&}&time-id={$}
```

Response

```
HTTP/1.1 200 
Content-Type: application/json

[
   {
        "id": 1,
        "startAt": "10:00",
        "alreadyBooked": true
    }
]
```

### 시간 추가 API (접근 권한: 관리자)

Request

```
POST /times
Content-Type: application/json

{
    "startAt": "10:00"
}
```

Response

```
HTTP/1.1 201
Content-Type: application/json

{
    "id": 1,
    "startAt": "10:00"
}
```

### 시간 삭제 API (접근 권한: 관리자)

Request

```
DELETE /times/1
```

Response

```
HTTP/1.1 204
```

## 테마

### 테마 목록 조회 API

Request

```
GET /themes
```

Response

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

### 인기 테마 목록 조회 API

Request

```
GET /themes/ranking
```

Response

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

### 테마 추가 API (접근 권한: 관리자)

Request

```
POST /themes
Content-Type: application/json

{
    "name": "레벨2 탈출",
    "description": "우테코 레벨2를 탈출하는 내용입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}
```

Response

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

### 테마 삭제 API (접근 권한: 관리자)

Request

```
DELETE /themes/1
```

Response

```
HTTP/1.1 204
```
