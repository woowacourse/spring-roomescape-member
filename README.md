# 방탈출 사용자 예약

## 1단계

- 예외 처리 후 적절한 응답 반환 (404, 403.. 등)
- 예외 처리
    - 예약
        - 시간: not null
        - 날짜: not null
        - 예약자명: not null, not blank
        - 시간 + 날짜
            - 예약 일시가 현재 일시 이후여야 한다.
            - 같은 일시에 예약이 있는 경우
    - 시간
        - not null, 25시, 61분 같은 값
        - 이미 있는 시간인 경우 생성 불가
        - 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때

## 2단계

> 클라이언트 코드를 수정해야 한다.

- 테마 entity가 추가된다.
    - 테마 table추가 및 reservation테이블 수정(제공되는 sql이용)
    - 기존 reservation entity 코드에 theme 추가
- `/admin/theme` -> `theme.html`반환
- reservation페이지 `reservation-new.html`로 변경
- `/reservations`api 필드에 theme 관련 필드 추가
- 테마 조회, 추가, 삭제 구현
- 클라이언트 코드를 수정해야 한다.
    - `/reservations`의 명세가 바뀌게 될텐데 테마와 관련된 필드와 프론트의 맵핑을 해야함

## 3단계

> 클라이언트 코드를 수정해야 한다.

- 예약 조회
    - `/reservation`요청하면 `reservation.html`반환
    - 날짜와 테마에 따른 예약시간 조회
- 예약 추가
    - 예약 추가 api적절히 수정한다.
- 인기테마
    - 최근 일주일 기준 예약이 많은 테마 10개
    - `/`url요청시 `templates/index.html`반환(인기페이지)

    - API 명세
        - 예약 가능 시간 조회
            - request
          ```
          GET /times/available
          ```
            - response
          ```
          HTTP/1.1 200
          Content-Type: application/json
          [
            {
              "id": 1,
              "start_at": "11:00",
              "is_reserved": "true"
            }
          ]
          ```

    - 예약 하기
        - request
          ```
          POST /reservation HTTP/1.1
          content-type: application/json
          
          {
          "name": "예약자명",
          "reservation_date": "2025-04-29",
          "theme_id": "1",
          "time_id": "1"
          }
          ```
        - response
          ```
          HTTP/1.1 201
          Location: /reservation/1
          Content-Type: application/json
          ```

    - 인기 테마 조회
        - request
          ```
          GET /theme/popular
          ```
            - response
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

## 4단계

> 사용자 도메인 추가 및 로그인 기능 구현

### 사용자 도메인 추가

- name, email, password를 가진다.

### 로그인 기능 구현

- `GET /login`요청 시 `login` 페이지 응답
- `POST /login`요청 시 쿠키에 토큰 반환
  - request
    ```angular2html
    POST /login HTTP/1.1
    content-type: application/json
    host: localhost:8080

    {
        "password": "password",
        "email": "admin@email.com"
    }
    ```
    - response
    ```angular2html
    HTTP/1.1 200 OK
    Content-Type: application/json
    Keep-Alive: timeout=60
    Set-Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI; Path=/; HttpOnly
    ```
  - `GET /login/check`인증 정보 조회 api 구현
      - request
      ```angular2html
      GET /login/check HTTP/1.1
      cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
      host: localhost:8080
      ```

      - response
      ```angular2html
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
## 5단계

> 5단계 html, js파일 적절히 수정

### 로그인 리팩터링
- HandlerMethodArgumentResolver를 이용해서 요청에서 Member객체로 변환

### 사용자 예약에서 사용자 정보를 이용하도록 수정
- `POST /reservations`
```angular2html
POST /reservations HTTP/1.1
content-type: application/json
cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI
host: localhost:8080

{
    "date": "2024-03-01",
    "themeId": 1,
    "timeId": 1
}
``` 
### 관리자 예약관리에서 사용자 정보를 이용하도록 수정
- `POST /admin/reservations`
```angular2html
POST /admin/reservations HTTP/1.1
content-type: application/json
cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI
host: localhost:8080

{
"date": "2024-03-01",
"themeId": 1,
"timeId": 1,
"memberId": 1
}
``` 


TODO
- 테스트에서 자주 사용하는 자원 어떤 형식으로 준비할건지 고민 + 리팩터링 하기
- fakeTest vs mockTest 결정하기
- 테스트 리팩터링
- 인터셉터에서 repository 사용하는 구조 고민해보기
