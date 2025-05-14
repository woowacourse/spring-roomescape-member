# :dart: API 정보

## 공통 API

### 페이지 응답

- GET /
    - 설명 : 인기 테마페이지 응답
    - 정상 응답 (200)

- GET /reservation
    - 설명 : 사용자 예약페이지 응답
    - 정상 응답 (200)
      ```
      사용자 예약페이지 HTML 문서
      ```

- GET /login
    - 설명 : 로그인 페이지 응답
    - 정상 응답 (200)
      ```
      로그인 페이지 HTML 문서
      ```

### 예약 API

- POST /reservations
    - 설명 : 예약 추가
    - 요청 파라미터
      ```
      {
          LocalDate "date": "2023-08-05",  // NotNull, (과거 날짜 허용X)
          Long "timeId": 1                 // NotNull, (과거 시간 허용X)
          Long "themeId": 1                // NotNull
      }
      ```
    - 정상 응답 (201)
      ```
      {
          Long "id": 1,
          Member "member" : {
                  Long "id" : 1,
                  String "name" : "회원1"
          },
          LocalDate "date": "2023-01-01",
          ReservationTime "time": {
              Long "id": 1,
              LocalTime "startAt" : "10:00"
          },
          Theme "theme" : {
              Long "id" : 1,
              String "name" : "테마1",
              String "description" : "재밌어요!",
              String "thumbnail" : "url"
          }
      },
      ```
    - 예외 응답 (400)
        - API 입력값 입력되지 않은 경우
        - 과거의 날짜나 시간인 경우
        - 이미 예약이 완료된 날짜와 시간인 경우

### 예약 시간 API

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

- GET /times
    - 설명 : 테마와 날짜에 해당하는 예약시간을 예약 여부와 함께 조회
    - 요청 파라미터
    ```
    {
        String "date": 2025-01-01,          // NotNull, NotBlank
        Long "themeId": 1,                  // NotNull
    }
    ```
    - 정상 응답 (200)
    ```
    [
        {
             Long "id": 1,
             LocalTime "startAt": "10:00",
             Boolean "isBooked": true
        },
        ...
    ]
    ```

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
    - 설명 : 일주일 간에 가장 인기있는 테마 조회
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

### 회원 API

- POST /login
    - 설명 : 로그인
    - 요청 페이로드
      ```
      {
          "password": "password",
          "email": "admin@email.com"
      }
      ```
    - 정상 응답 (200)
        - 응답 헤더
        ```
        // 인증 토큰을 쿠키로 전달
        Set-Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI; Path=/; HttpOnly
        ```
    - 예외 응답 (401)
        - 인증 정보가 올바르지 않은 경우

- GET /login/check
    - 설명 : 로그인 여부 확인
    - 요청 헤더
      ```
      // 인증 토큰이 담긴 쿠키를 전달
      cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
      ```
    - 정상 응답 (200)
      ```
      {
          "name": "어드민"
      }
      ```
    - 예외 응답 (401)
        - 토큰에 담긴 인증 정보가 올바르지 않은 경우

- POST /logout
    - 설명 : 로그인 아웃
    - 정상 응답 (200)

- POST /members
    - 설명 : 회원가입
        - 요청 페이로드
          ```
          {
              String "email": "admin@email.com",  // NotNull, 이메일 형식 준수
              String "password": "password",      // NotNull
              String "name": "MemberName"         // NotNUll
          }
          ```
    - 정상 응답 (201)
      ```
      {
              Long "id": 1,                  // NotNull, 이메일 형식 준수
              String "name": "Member1",      // NotNull
      }
      ```
    - 예외 응답 (401)
        - 유효하지 않은 값이 입력된 경우
        - 이미 계정이 존재하는 회원인 경우

## 관리자 API

### 관리자 페이지 응답

- GET /admin
    - 설명 : 메인페이지 응답
    - 요청 헤더 : 관리자 Access 토큰
    - 정상 응답 (200)
      ```
      메인 페이지 HTML 파일
      ```

- GET /admin/reservation
    - 설명 : 예약 관리 페이지 응답
    - 요청 헤더 : 관리자 Access 토큰
    - 정상 응답 (200)
      ```
      예약 페이지 HTML 문서
      ```

- GET /admin/time
    - 설명 : 시간페이지 응답
    - 요청 헤더 : 관리자 Access 토큰
    - 정상 응답 (200)
      ```
      시간 페이지 HTML 문서
      ```

- GET /admin/theme
    - 설명 : 테마페이지 응답
    - 요청 헤더 : 관리자 Access 토큰
    - 정상 응답 (200)
      ```
      테마 페이지 HTML 문서
      ```

### 예약 관리 API

- GET /reservations
    - 설명 : 모든 예약 조회
    - 정상 응답 (200)
      ```
      [
          {
              Long "id": 1,
              Member "member" : {
                  Long "id" : 1,
                  String "name" : "회원1"
              },
              LocalDate "date": "2023-01-01",
              ReservationTime "time": {
                  Long "id": 1,
                  LocalTime "startAt" : "10:00"
              },
              Theme "theme" : {
                  Long "id" : 1,
                  String "name" : "테마1",
                  String "description" : "재밌어요!",
                  String "thumbnail" : "url"
              }
          },
          ...
      ]
      ```

- GET /reservations
    - 설명 : 모든 예약 조회
    - 요청 파라미터 :
      ```
      Long "themId" : 1,
      Long "memberId" : 1,
      Long "dateFrom" : "2025-05-10",
      Long "dateTo" : "2025-05-10",
      ```
    - 정상 응답 (200)
      ```
      [
          {
              Long "id": 1,
              Member "member" : {
                  Long "id" : 1,
                  String "name" : "회원1"
              },
              LocalDate "date": "2023-01-01",
              ReservationTime "time": {
                  Long "id": 1,
                  LocalTime "startAt" : "10:00"
              },
              Theme "theme" : {
                  Long "id" : 1,
                  String "name" : "테마1",
                  String "description" : "재밌어요!",
                  String "thumbnail" : "url"
              }
          },
          ...
      ]
      ```

- POST /admin/reservations
    - 설명 : 예약 추가
    - 요청 파라미터
      ```
      {
          Long "memberId": 1,              // NotNull
          LocalDate "date": "2023-08-05",  // NotNull, (과거 날짜 허용X)
          Long "timeId": 1,                // NotNull, (과거 시간 허용X)
          Long "themeId": 1                // NotNull
      }
      ```
    - 정상 응답 (201)
      ```
      {
          Long "id": 1,
          Member "member" : {
                  Long "id" : 1,
                  String "name" : "회원1"
          },
          LocalDate "date": "2023-01-01",
          ReservationTime "time": {
              Long "id": 1,
              LocalTime "startAt" : "10:00"
          },
          Theme "theme" : {
              Long "id" : 1,
              String "name" : "테마1",
              String "description" : "재밌어요!",
              String "thumbnail" : "url"
          }
      },
      ```
    - 예외 응답 (400)
        - API 입력값 입력되지 않은 경우
        - 과거의 날짜나 시간인 경우
        - 이미 예약이 완료된 날짜와 시간인 경우

- DELETE /reservations/{reservationId}
    - 설명 : 예약 취소
    - 정상 응답 (204)
    - 예외 응답 (400)
        - reservationId가 NULL인 경우
    - 예외 응답 (404)
        - reservationId에 해당하는 예약이 없는 경우

### 예약 시간 관리 API

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

- DELETE /times/{reservationTimeId}
    - 설명 : ID에 해당하는 예약 가능한 시간 삭제
    - 정상 응답 (204)
    - 예외 응답 (404)
        - API 입력값 입력되지 않은 경우
    - 예외 응답 (400)
        - 이미 해당 예약 가능 시간으로 예약 데이터가 존재하는 경우

### 테마 관리 API

- POST /themes
    - 설명 : 테마 추가
    - 요청 페이로드
      ```
      String "name": "레벨2 탈출",                                // NotNull, 최대 255자
      String "description": "우테코 레벨2를 탈출하는 내용입니다.",     // NotNull, 최대 255자
      String "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg" // NotNull, 최대 255자
      ```
    - 정상 응답 (201)
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

### 회원 관리 API

- GET /members
    - 설명 : 모든 일반 회원 조회
    - 정상 응답 (200)
      ```
      [
          {
              Long id : 1, 
              String name : "회원1",
          },
          {
              Long id : 2, 
              String name : "회원2",
          },
          ...
      ]
      ```
