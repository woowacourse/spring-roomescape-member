## 방탈출 사용자 예약

### 요구사항

- **4 단계**
    - 사용자 도메인을 추가합니다.
    - 로그인 기능을 구현하세요.
    - 로그인 후 Cookie를 이용하여 사용자의 정보를 조회하는 API를 구현하세요.

- **5단계**
    - 사용자의 정보를 조회하는 로직을 리팩터링 합니다.
    - 예약 생성 API 및 기능을 리팩터링 합니다.

### 세부 요구 사항

#### 로그인 기능

- [x] 사용자가 이메일과 비밀번호를 입력하여 로그인 할 수 있다.
    - [x] 로그인 시 쿠키를 함께 보낸다.
- [x] 토큰 값으로 사용자의 이름을 가져올 수 있다.

#### 사용자 기능

- [ ] 사용자가 예약 생성 시, 로그인한 사용자 정보를 활용한다.

### 관리자 기능

- [ ] 관리자가 예약 생성 시, 유저를 조회하여 선택 후 예약을 생성한다.

## 추가 API 명세서

<br>

- **로그인**

### Request

> POST /login HTTP/1.1
>
> content-type: application/json
> host: localhost:8080

```json
{
  "password": "password",
  "email": "admin@email.com"
}
```

### Response

> HTTP/1.1 200 OK
>
> Content-Type: application/json
> Keep-Alive: timeout=60
> Set-Cookie:
>
token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI;
> Path=/; HttpOnly

<br>

- 인증 정보 조회

### Request

> GET /login/check HTTP/1.1
>
> cookie: 쿠키 ~~ <br>
> host: localhost:8080

### Response

> HTTP/1.1 200 OK
>
> Connection: keep-alive <br>
> Content-Type: application/json <br>
> Date: Sun, 03 Mar 2024 19:16:56 GMT <br>
> Keep-Alive: timeout=60 <br>
> Transfer-Encoding: chunked

- 유저 예약 생성

### Request

> POST /reservations HTTP/1.1 <br>
>
> content-type: application/json <br>
> cookie: token= 토큰~ <br>
> host: localhost:8080

```json
{
  "date": "2024-03-01",
  "themeId": 1,
  "timeId": 1
}
```

### Response

>
> HTTP/1.1 200
>
> Content-Type: application/json

```json
{
  "id": 1,
  "reservationName": "포레스트",
  "date": "2024.03.01",
  "startAt": "10:00:00",
  "themeName": "공포"
}
```

<br>

- **관리자 예약 생성**

### Request

> POST /admin/reservations HTTP/1.1
>
> content-type: application/json <br>
> cookie: token=??? <br>
> host: localhost:8080

```json
{
  "date": "2024-03-01",
  "themeId": 1,
  "timeId": 1,
  "memberId": 1
}
```

### Response

> HTTP/1.1 200
>
> Content-Type: application/json

```json
{
  "id": 1,
  "reservationName": "포레스트",
  "date": "2024.03.01",
  "startAt": "10:00:00",
  "themeName": "공포"
}
```


