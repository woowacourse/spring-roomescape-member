# :dart: API 정보

### 페이지 응답

- GET /
    - 설명 : 웰컴 페이지 응답 (메인 페이지로 리다이렉션)
    - 정상 응답 (308)

- GET /admin
    - 설명 : 메인페이지 응답
    - 정상 응답 (200)
      ```
      메인 페이지 HTML 파일
      ```

- GET /admin/reservation
    - 설명 : 예약페이지 응답
    - 정상 응답 (200)
      ```
      예약 페이지 HTML 문서
      ```

- GET /admin/time
    - 설명 : 시간페이지 응답
    - 정상 응답 (200)
      ```
      시간 페이지 HTML 문서
      ```
      
- GET /admin/theme
    - 설명 : 테마페이지 응답
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
              Long "id": 1,
              String "name": "브라운",
              LocalDate "date": "2023-01-01",
              ReservationTime "time": {
                  Long "id": 1,
                  LocalTime "startAt" : "10:00"
          },
          {
              Long "id": 2,
              String "name": "브라운",
              LocalDate "date": "2023-01-02",
              ReservationTime "time": {
                  Long "id": 1,
                  LocalTime "startAt" : "10:00"
              },
          },
          ...
      ]
      ```
- POST /reservations
    - 설명 : 예약 추가
    - 요청 파라미터
      ```
      {
          String "name": "브라운",          // NotNull, NotBlank
          LocalDate "date": "2023-08-05",  // NotNull, (과거 날짜 허용X)
          Long "timeId": 1                 // NotNull, (과거 시간 허용X)
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
      }
      ```
    - 예외 응답 (400)
        - API 입력값 입력되지 않은 경우
        - 비어있거나 공백 이름이 입력된 경우
        - 과거의 날짜나 시간인 경우
        - 이미 예약이 완료된 날짜와 시간인 경우

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
