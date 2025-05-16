# View

### [홈화면 View]

- 어드민 홈화면을 보여줍니다.

```text
### 요청 예시

GET localhost:8080/admin

### 응답 예시

templates/admin/index.html (홈화면 뷰)
```

### [예약 전체 조회 View]

- 어드민 예약 전체 조회 화면을 보여줍니다.
- 각 예약의 다음과 같은 정보를 볼 수 있습니다.
    - 예약번호
    - 예약자
    - 날짜
    - 시간
- 다음과 같은 작업을 수행할 수 있습니다.
    - 개별 예약 삭제
    - 예약 추가

```text
### 요청 예시

GET localhost:8080/admin/reservation

### 응답 예시

templates/admin/reservation-legacy.html (전체 조회 뷰)
```

### [테마 관리 View]

- 어드민 테마 관리 화면을 보여줍니다.
- 각 테마는 다음과 같은 정보를 볼 수 있습니다.
  - 순서
  - 제목
  - 설명
  - 썸네일 URL
- 다음과 같은 작업을 수행할 수 있습니다.
  - 테마 추가
  - 테마 삭제

```text
### 요청 예시
GET localhost:8080/admin/theme

### 응답 예시
templates/admin/theme.html (테마 관리 뷰)
```

### [사용자 예약 페이지]

- 일반 사용자는 이 곳에서 예약이 가능합니다.
- 날짜 선택, 테마 선택, 시간 선택이 필요합니다.
  - 이전 정보를 선택해야 다음 정보 선택이 가능합니다.
- 예약자명을 입력할 수 있습니다.

```text
### 요청 예시
GET localhost:8080/reservation

### 응답 예시
templates/reservation.html (사용자 예약 페이지)
```

### [인기 테마 페이지]

- 인기 테마들을 보여줍니다.
- 테마들은 예약 수에 따라 내림차순으로 정렬됩니다.
- 각 테마는 제목, 설명, 썸네일 이미지를 보여줍니다.

```text
### 요청 예시
GET localhost:8080/

### 응답 예시
templates/index.html (인기 테마 페이지)
```

### [로그인 페이지]

```text
### 요청 예시
GET /login

### 응답 예시
templates/login.html (로그인 페이지)
```
---

### [회원가입 페이지]

```text
### 요청 예시
GET /signup

### 응답 예시
templates/signup.html (회원가입 페이지)
```
---
# API

## Reservation

### [예약 추가 API]

- 예약을 추가할 수 있다.
- 다음과 같은 정보를 전달해야 한다.
    - name : 예약자명
    - date : 예약 날짜 (yyyy-MM-dd)
    - time : 예약 시간 (HH:mm)

```text
### 요청 예시

POST localhost:8080/reservations
Content-Type: application/json
cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwiZXhwIjoxNzQ2NzkwNzEyfQ.dzKozcNp-XT4w0lfSp5bQVwzffrCWmd5gLQ8wxqty7g; Path=/

{
    "date": "2025-05-11",
    "timeId": 1,
    "themeId": 1
}

### 응답 예시

HTTP/1.1 201
Content-Type: application/json
```

### [어드민 예약 추가 API]

```text
POST http://localhost:8080/admin/reservations
Content-Type: application/json

{
  "memberId": 2,
  "date": "2025-05-10",
  "timeId": 1,
  "themeId": 1
}
```

### [예약 전체 조회 API]

- 전체 예약 조회를 할 수 있다.
- 다음과 같은 정보가 반환된다.
    - id : 예약 번호
    - name : 예약자명
    - date : 예약 날짜
    - time : 예약 시간

```text
### 요청 예시

GET localhost:8080/reservations

### 응답 예시

HTTP/1.1 200
Content-Type: application/json

[
  {
    "id": 1,
    "name": "dompoo",
    "date": "2024-12-05",
    "time": "20:00:00"
  },
  {
    "id": 2,
    "name": "wade",
    "date": "2024-12-05",
    "time": "18:30:00"
  }
]
```

### [예약 삭제 API]

- 예약을 삭제할 수 있다.
- 다음과 같은 정보를 포함해야 한다.
    - id : 예약 번호

```text
### 요청 예시

DELETE localhost:8080/reservations/{id}

### 응답 예시

HTTP/1.1 204
Content-Length: 0
```

### [예약 검색 API]

- 아래 조건을 선택적으로 적용하여 검색이 가능하다.
  - memberId: 예약자 id
  - themeId: 테마 id
  - dateFrom: 검색 시작 일자
  - dateTo: 검색 종료 일자
- 존재하지 않는 memberId 혹은 themeId으로 검색을 시도할 경우 예외를 반환한다.

```text
### 요청 예시

GET http://localhost:8080/reservations/search?
    themeId=1&
    memberId=1&
    dateFrom=2025-05-01&
    dateTo=2025-05-10

### 응답 예시

HTTP/1.1 200 
Content-Type: application/json

[
  {
    "id": 1,
    "name": "웨이드",
    "date": "2025-05-01",
    "time": {
      "id": 1,
      "startAt": "10:00"
    },
    "theme": {
      "id": 2,
      "name": "시간 도둑",
      "description": "스릴 넘치는 우테코 미션",
      "thumbnail": "/image/time.png"
    }
  }
]
```

## Time

### [시간 추가 API]

```text
### 요청 예시

POST /times HTTP/1.1
content-type: application/json

{
    "startAt": "10:00"
}

### 응답 예시

HTTP/1.1 201
Content-Type: application/json

{
    "id": 1,
    "startAt": "10:00"
}
```

### [시간 조회 API]

```text
### 요청 예시

GET /times HTTP/1.1

### 응답 예시

HTTP/1.1 200 
Content-Type: application/json

[
   {
        "id": 1,
        "startAt": "10:00"
    }
]
```

### [시간 삭제 API]

```text
### 요청 예시

DELETE /times/1 HTTP/1.1

### 응답 예시

HTTP/1.1 204

```

## Theme

### [테마 조회 API]

```text
### 요청 예시

GET /themes HTTP/1.1

### 응답 예시
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
### [테마 추가 API]

```text
### 요청 예시
POST /themes HTTP/1.1
content-type: application/json

{
"name": "레벨2 탈출",
"description": "우테코 레벨2를 탈출하는 내용입니다.",
"thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}

### 응답 예시
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
### [테마 삭제 API]

```text
### 요청 예시
DELETE /themes/1 HTTP/1.1

### 응답 예시
HTTP/1.1 204
```

### [인기 테마 조회 API]

- 예약 수 내림차순으로 테마들을 반환합니다.

```text
### 요청 예시
GET /themes/popular

### 응답 예시
HTTP/1.1 200
Location: /themes/popular
Content-Type: application/json
[
    {
        "id": 3,
        "name": "우테코 학교",
        "description": "잃어버린 DDD를 찾아라",
        "thumbnail": "/image/school.png"
    },
    {
        "id": 1,
        "name": "공포의 우테코",
        "description": "우테코에서 벌어지는 미스터리를 풀어라",
        "thumbnail": "/image/horror.png"
    },
    {
        "id": 2,
        "name": "시간 도둑",
        "description": "스릴 넘치는 우테코 미션",
        "thumbnail": "/image/time.png"
    }
]
```

## Member

### [로그인 API]

```text
### 요청 예시
POST /login HTTP/1.1
content-type: application/json
host: localhost:8080

{
    "password": "password",
    "email": "admin@email.com"
}

### 응답 예시
HTTP/1.1 200 OK
Content-Type: application/json
Set-Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI; Path=/; HttpOnly
```

### [로그아웃 API]

```text
### 요청 예시
GET /logout HTTP/1.1
cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
host: localhost:8080

### 응답 예시
HTTP/1.1 204
Set-Cookie: token=; Max-Age=0; Expires=Thu, 01 Jan 1970 00:00:10 GMT; Path=/

```

### [인증 정보 조회 API]

```text
### 요청 예시
GET /login/check HTTP/1.1
cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
host: localhost:8080

### 응답 예시
HTTP/1.1 200 OK
Content-Type: application/json

{
    "name": "어드민"
}
```

### [회원가입 API]

```text
### 요청 예시
POST /signup HTTP/1.1
host: localhost:8080

{
    "name": "moko",
    "email": "admin@email.com",
    "password": "password"
}

### 응답 예시
HTTP/1.1 201 CREATED
```

### [회원 전체 조회 API]

```text
### 요청 예시
GET /members HTTP/1.1

### 응답 예시
HTTP/1.1 200 
Content-Type: application/json

[
  {
    "id": 1,
    "name": "웨이드"
  },
  {
    "id": 2,
    "name": "모코"
  }
]
```
