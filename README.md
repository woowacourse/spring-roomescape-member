# 방탈출 사용자 예약

## 예외 처리와 응답
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

## 테마 추가
- 클라이언트 코드를 수정해야 한다.
- 테마 entity가 추가된다.
    - 테마 table추가 및 reservation테이블 수정(제공되는 sql이용)
    - 기존 reservation entity 코드에 theme 추가
- `/admin/theme` -> `theme.html`반환
- reservation페이지 `reservation-new.html`로 변경
- `/reservations`api 필드에 theme 관련 필드 추가
- 테마 조회, 추가, 삭제 구현
- 클라이언트 코드를 수정해야 한다.
    - `/reservations`의 명세가 바뀌게 될텐데 테마와 관련된 필드와 프론트의 맵핑을 해야함

## 사용자 기능
- 클라이언트 코드를 수정해야 한다.
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
    - request (관리자)
      ```
      POST /reservations HTTP/1.1
      cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI
      content-type: application/json
      
      {
          "reservation_date": "2025-04-29",
          "theme_id": 1,
          "time_id": 1,
          "memberId": 1
      }
      ```
    - request (사용자)
      ```
      POST /reservations HTTP/1.1
      cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI
      content-type: application/json
      
      {
          "reservation_date": "2025-04-29",
          "theme_id": 1,
          "time_id": 1
      }
      ```
    - response
      ```
      HTTP/1.1 201
      Location: /reservations/1
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
      
## 사용자 로그인
- 클라이언트 코드를 수정해야 한다.
- 사용자 도메인 추가
  - `name`, `email`, `password` 포함
- 로그인 기능
  - `/login` 요청 시 `templates/login.html` 응답
- API 명세
  - 로그인
    - request
      ```
      POST /login HTTP/1.1
      content-type: application/json
      host: localhost:8080
      
      {
          "password": "password",
          "email": "admin@email.com"
      }
      ```
    - response
      ```
      HTTP/1.1 200 OK
      Content-Type: application/json
      Keep-Alive: timeout=60
      Set-Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI; Path=/; HttpOnly
      ```
  - 인증 정보 조회    
    - request
      ```
      GET /login/check HTTP/1.1
      cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
      host: localhost:8080
      ```
    - response
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
      
## 관리자 기능
- 클라이언트 코드를 수정해야 한다.
- 접근 권한 제어
  - 어드민 페이지는 `Member`의 `Role`이 `ADMIN`인 사람만 접근 가능
  - 권한이 없다면 요청에 대한 거부 응답
- 예약 목록 검색
  - 예약자별, 테마별, 날짜별 검색 조건을 이용해 예약 검색