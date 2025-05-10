# :dart: API 정보

### 페이지 응답

- GET /
    - 설명 : 랭킹 페이지 응답
    - 정상 응답 (200)

- GET /admin (ADMIN 권한 유저만 접근 가능)
    - 설명 : 어드민 메인페이지 응답
    - 정상 응답 (200)
      ```
      메인 페이지 HTML 파일
      ```

- GET /admin/reservation (ADMIN 권한 유저만 접근 가능)
    - 설명 : 예약페이지 응답
    - 정상 응답 (200)
      ```
      예약 페이지 HTML 문서
      ```

- GET /admin/time (ADMIN 권한 유저만 접근 가능)
    - 설명 : 시간페이지 응답
    - 정상 응답 (200)
      ```
      시간 페이지 HTML 문서
      ```

- GET /admin/theme (ADMIN 권한 유저만 접근 가능)
    - 설명 : 테마페이지 응답
    - 정상 응답 (200)
      ```
      테마 페이지 HTML 문서
      ```

- GET /reservation
    - 설명 : 사용자 예약페이지 응답
    - 정상 응답 (200)
      ```
      테마 페이지 HTML 문서
      ```

- GET /login
    - 설명 : 사용자 로그인 페이지 응답
    - 정상 응답 (200)
      ```
      테마 페이지 HTML 문서
      ```

### 예약 API

- GET /reservations
    - 설명 : 예약 조회
        - 정상 응답 (200)
          ```
          [
              {
                  "id":1,
                  "member":{"id":1,"name":"아마"},
                  "date":"2025-04-30",
                  "time":{"id":1,"startAt":"10:00:00"},
                  "theme":{"id":12,"name":"테마12","description":"서커스의신","thumbnail":"http://localhost:8080/image/theme.jpg"}
              }
              ...
          ]
          ```
- POST /reservations
    - 설명 : 사용자 예약 추가
    - 요청 파라미터
      ```
      {
          LocalDate "date": "2023-08-05",  // NotNull, (과거 날짜 허용X)
          Long "timeId": 1                 // NotNull, (과거 시간 허용X)
          Long "themeId": 1                 // NotNull, (과거 시간 허용X)
      }
      ```
    - 정상 응답 (201)
      ```
      {
          Long "id": 1,
          String "name": "브라운",
          LocalDate "date": "2023-08-05",
          ReservationTime "time": {
             Long "id": 1,
             LocalTime "startAt" : "10:00"
          }
          Theme "theme": {
                Long "id": 12,
                String "name": "테마12",
                String "description": "서커스의신",
                String "thumbnail": "http://localhost:8080/image/theme.jpg"
          }
      }
      ```
    - 예외 응답 (400)
        - API 입력값 입력되지 않은 경우
        - 비어있거나 공백 이름이 입력된 경우
        - 과거의 날짜나 시간인 경우
        - 중복 예약인 경우

- DELETE /reservations/{reservationId}
    - 설명 : 예약 취소
    - 정상 응답 (204)
    - 예외 응답 (404)
        - reservationId가 NULL인 경우
    - 예외 응답 (400)
        - reservationId에 해당하는 예약이 없는 경우

### 예약 시간 API

- POST /times
    - 설명 : 예약 가능한 시간 추가
    - 요청 페이로드
      ```
      {
          LocalTime "startAt": "10:00"  // NotNull
      }
      ```
    - 정상 응답 (201)
      ```
      {
          Long "id": 1,
          LocalTime "startAt": "10:00"
      }
      ```
    - 예외 응답 (400)
        - API 입력값 입력되지 않은 경우
        - 이미 추가되어 있는 예약 가능 시간일 경우

- GET /times
    - 설명 : 예약 가능한 시간 모두 조회
    - 정상 응답 (200)
      ```
      [
          {
             Long "id": 1,
             LocalTime "startAt": "10:00"
          },
          ...
      ]
      ```
- GET /{date}/{themeId}/times
    - 설명 : 예약 여부와 함께 예약 가능한 시간 모두 조회
    - 정상 응답 (200)
      ```
      [
          {
             Long "id": 1,
             LocalTime "startAt": "10:00"
             Boolean "bookState": true
          },
          ...
      ]
      ```

- DELETE /times/{reservationTimeId}
    - 설명 : ID에 해당하는 예약 가능한 시간 삭제
    - 정상 응답 (204)
    - 예외 응답 (404)
        - API 입력값 입력되지 않은 경우
    - 예외 응답 (400)
        - 이미 해당 예약 가능 시간으로 예약 데이터가 존재하는 경우

### 테마 API

- GET /themes
    - 설명 : 모든 테마를 조회
    - 정상 응답 (200)
      ```
      [
          {
              Long "id": 1,                                                                             
              String "name": "레벨2 탈출",
              String description": "우테코 레벨2를 탈출하는 내용입니다.",
              String "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
          },
          ...
      ]
      ```

- GET /themes/top
    - 설명 : 일주일간 인기 테마 최대 10개 조회
    - 정상 응답 (200)
      ```
      [
          {
              Long "id": 1,                                                                             
              String "name": "레벨2 탈출",
              String description": "우테코 레벨2를 탈출하는 내용입니다.",
              String "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
          },
          ...
      ]
      ```

- POST /themes
    - 설명 : 테마 추가
    - 요청 페이로드
      ```
      String "name": "레벨2 탈출",                                // NotNull, 최대 255자
      String "description": "우테코 레벨2를 탈출하는 내용입니다.",     // NotNull, 최대 255자
      String "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg" // NotNull, 최대 255자
      ```
    - 정상 응답 (200)
      ```
        {
          Long "id": 1,
          String "name": "레벨2 탈출",
          String "description": "우테코 레벨2를 탈출하는 내용입니다.",
          String "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
      }
      ```
    - 예외 응답 (400)
        - 요청 페이로드가 올바르지 않을 경우

- DELETE /themes/{themesId}
    - 설명 : 테마 삭제
    - 정상 응답 (204)
    - 예외 응답 (404)
        - ID에 대한 테마가 존재하지 않는 경우
    - 예외 응답 (400)
        - themesId가 NULL인 경우
        - 테마에 대한 예약이 존재할 경우

### 로그인 API

- POST /login
    - 설명 : 사용자 로그인
    - 정상 응답 (200)
      ```
        HTTP/1.1 200 OK
        Content-Type: application/json
        Keep-Alive: timeout=60
        Set-Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI; Path=/; HttpOnly

      ```

- GET /login/check
    - 설명 : 사용자 로그인 정보 조회
    - 정상 응답 (200)
      ```
      {
         "name": "어드민"
      }
      ```