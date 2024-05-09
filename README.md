## 기능 요구 사항

### 1단계 기능 요구사항

- [x] 예약관리 API가 적절한 응답을 하도록 한다.
- [x] 발생 가능한 예외사항을 처리한다
    - [x] 시간 생성 시 : 시작 시간에 유효하지 않은 값이 입력되었을 때
    - [x] 예약 생성 시 : 예약자명, 날짜, 시간에 유효하지 않은 값이 입력 되었을 때
        - [x] 예약 날짜는 오늘 혹은 오늘 이후여야 한다.
        - [x] 예약 시간
            - [x] 예약 날짜가 오늘이라면 현재 시간 이후여야 한다.
            - [x] 예약 날짜가 오늘이 아니라면, 모든 시간에 예약이 가능하다.
    - [x] 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때

- [x] 새로운 서비스 정책을 반영한다
    - 예약 생성 시
        - [x] 지나간 날짜와 시간에 대한 예약 생성은 불가능하다.
        - [x] 중복 예약은 불가능하다.
        - ```
      ex. 이미 4월 1일 10시에 예약이 되어있다면, 4월 1일 10시에 대한 예약을 생성할 수 없다.
      ```

### 2단계 기능 요구사항

- [x] 관리자가 테마를 관리하는 기능
    - [x] 테마를 추가한다.
        - [x] 모든 테마는 시작 시간과 소요 시간이 동일하다고 가정한다.
    - [x] 테마를 조회한다.
    - [x] 테마를 삭제한다.
        - [x] 테마를 사용 중인 방탈출 예약이 있다면 테마를 삭제할 수 없다.
- [x] 관리자가 방탈출 예약 시, 테마 정보를 포함할 수 있도록 기능을 변경한다.

### 3단계 기능 요구사항

- [x] 사용자가 예약 시간을 조회하고 예약한다.
    - [x] 사용자가 날짜와 테마를 기준으로 예약 가능한 시간을 확인한다.
    - [x] 사용자가 원하는 시간에 예약한다.

- [x] 인기 테마를 조회할 수 있다
    - [x]  최근 일주일을 기준으로 방문 예약이 많은 상위 10개의 테마를 확인한다.

### 4단계 기능 요구사항

- [x] 로그인 기능을 구현한다.
    - [x] 로그인 후 Cookie를 이용하여 사용자의 정보를 조회한다
- [x] 로그아웃 기능을 구현한다.

### 5단계 기능 요구사항

- [x] 예약 생성 기능 변경
    - [x] 사용자 예약 생성 기능
        - [x] 로그인한 사용자 정보를 활용하여 예약을 생성한다
    - [x] 관리자 예약 생성 시

### 6단계 기능 요구사항

- [x] 어드민인 사람만 /admin 으로 시작하는 페이지에 접근한다.
    -[ ] HandlerInterceptor를 활용하여 권한을 확인하고, 권한이 없는 경우 요청에 대한 거부 응답을 반환한다
- [ ] 예약이 많아질 경우 관리가 용이하도록 예약 검색 기능을 추가합니다.
    -[ ] 예약자별, 테마별, 날짜별 검색 조건을 사용해 예약 검색이 가능하도록 기능을 추가하세요.

---

## API 명세

### 테마 조회 API

request

```json
GET /themes HTTP/1.1
```

response

```json
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

request

```json
POST /themes HTTP/1.1
content-type: application/json

{
"name": "레벨2 탈출",
"description": "우테코 레벨2를 탈출하는 내용입니다.",
"thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}
```

response

```json
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

request

```json
DELETE /themes/1 HTTP/1.1
```

response

```json
HTTP/1.1 204
```

### 로그인

request

```json
DELETE /themes/1 HTTP/1.1
```

request

```json
POST /login HTTP/1.1
content-type: application/json
host: localhost: 8080

{
"password": "password",
"email": "admin@email.com"
}
```

response

```json
HTTP/1.1 200 OK
Content-Type: application/json
Keep-Alive: timeout=60
Set-Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI; Path=/; HttpOnly
```

### 인증 정보 조회

request

```json
GET /login/check HTTP/1.1
cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
host: localhost: 8080
```

response

```json
HTTP/1.1 200 OK
Connection: keep-alive
Content-Type: application/json
Date: Sun, 03 Mar 2024 19: 16: 56 GMT
Keep-Alive: timeout=60
Transfer-Encoding: chunked

{
"name": "어드민"
}
```

### 예약 생성 - 사용자

request

```json
POST /reservations HTTP/1.1
content-type: application/json
cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI
host: localhost: 8080

{
"date": "2024-03-01",
"themeId": 1,
"timeId": 1
}
```

### 예약 생성 - 관리자

request

```json
POST /admin/reservations HTTP/1.1
content-type: application/json
cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI
host: localhost: 8080

{
"date": "2024-03-01",
"themeId": 1,
"timeId": 1,
"memberId": 1
}
```

---

## 1차 리뷰 개선사안

- 예약 생성 시 시간검증 책임 이전 : Reservation > ReservationService
- domain 생성 책임을 builder 클래스에게 분담
- Service/dao 추상화
- default 접근 제어자를 활용한 Service/Dao 구현체 은닉
- ReservationDao : join문을 활용한 mapping 로직 일반화
- 에러 처리 로직 추가
    - dto / fail-fast를 위해 Valid를 활용한 1차 검증
    - 명확하고 유의한 메세지 전달을 위한 ErrorResponse 객체 반환
    - ControllerAdvice :스프링 내부 에러 처리를 위해 ResponseEntityExceptionHandler 상속
    - ControllerAdvice/ ExceptionHandler 추가
        - NoSuchElement > NOT_FOUND
        - MethodArgumentNotValid > BAD_REQUEST(Override)
        - IllegalArgument > BAD_REQUEST
        - IllegalState > INTERNAL_SERVER
        - etc > INTERNAL_SERVER
