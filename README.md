# 방탈출 예약 관리 API 문서

## 홈 및 로그인 관련

### 어드민 페이지

* **GET /admin**

    * 설명: 어드민 페이지 HTML을 반환한다.
    * 응답: HTML 페이지

### 로그인 페이지

* **GET /login**

    * 설명: 로그인 페이지 HTML을 반환한다.
    * 응답: HTML 페이지

### 예약 페이지

* **GET /reservation**

    * 설명: 예약 페이지 HTML을 반환한다.
    * 응답: HTML 페이지

### 인기 테마 페이지

* **GET /**

    * 설명: 인기 테마 페이지 HTML을 반환한다.
    * 응답: HTML 페이지

### 로그인

* **POST /login**

    * 설명: 사용자 로그인 처리 후 쿠키에 JWT 발급
    * 요청 본문:

      ```json
      {
        "email": "user@example.com",
        "password": "1234"
      }
      ```
    * 응답:

        * 상태 코드: 200 OK
        * 헤더: `Set-Cookie: accessToken=...`

### 로그인 체크

* **GET /login/check**

    * 설명: 로그인된 사용자 정보를 확인한다.
    * 응답 예시:

      ```json
      {
        "name": "브라운"
      }
      ```

### 로그아웃

* **POST /logout**

    * 설명: accessToken 쿠키를 만료시켜 로그아웃 처리
    * 응답: `Set-Cookie: accessToken=; Max-Age=0`

---

## 회원 관리

### 회원 목록 조회

* **GET /members**

    * 설명: 전체 회원 목록을 조회한다.
    * 응답 예시:

      ```json
      [
        {
          "id": 1,
          "name": "브라운"
        }
      ]
      ```

---

## 테마 관리

### 테마 목록 조회

* **GET /themes**

    * 설명: 전체 테마 목록을 조회한다.

### 인기 테마 조회

* **GET /themes/popular**

    * 설명: 인기 테마 목록을 조회한다.

### 테마 추가

* **POST /themes**

    * 설명: 테마를 추가한다.
    * 요청 본문:

      ```json
      {
        "name": "공포의 하수구",
        "description": "공포 테마의 탈출 게임"
      }
      ```
    * 응답:

      ```json
      {
        "id": 1,
        "name": "공포의 하수구",
        "description": "공포 테마의 탈출 게임"
      }
      ```

### 테마 삭제

* **DELETE /themes/{id}**

    * 설명: 테마를 삭제한다.

---

## 예약 관리

### 예약 목록 조회

* **GET /reservations**

    * 설명: 전체 예약 목록을 조회한다.

### 예약 검색

* **GET /reservations/search?themeId=1\&memberId=2\&dateFrom=2023-08-01\&dateTo=2023-08-10**

    * 설명: 조건에 맞는 예약을 조회한다.

### 예약 추가

* **POST /reservations**

    * 설명: 예약을 추가한다.
    * 요청 본문:

      ```json
      {
        "name": "브라운",
        "date": "2023-08-05",
        "timeId": 1
      }
      ```
    * 응답 예시:

      ```json
      {
        "id": 1,
        "name": "브라운",
        "date": "2023-08-05",
        "time": {
          "id": 1,
          "startAt": "10:00"
        }
      }
      ```

### 예약 삭제

* **DELETE /reservations/{id}**

    * 설명: 특정 예약을 삭제한다.
    * 응답: 204 No Content

---

## 시간 관리

### 시간 목록 조회

* **GET /times**

    * 설명: 모든 예약 가능 시간을 조회한다.

### 예약 가능 시간 조회

* **GET /times/available?date=2023-08-05\&themeId=1**

    * 설명: 특정 날짜, 테마 기준으로 예약 가능한 시간을 조회한다.
    * 응답 예시:

      ```json
      [
        {
          "timeId": 1,
          "startAt": "10:00",
          "alreadyBooked": true
        },
        {
          "timeId": 2,
          "startAt": "11:00",
          "alreadyBooked": false
        }
      ]
      ```

### 시간 추가

* **POST /times**

    * 설명: 예약 가능한 시간을 추가한다.
    * 요청 예시:

      ```json
      {
        "startAt": "10:00"
      }
      ```
    * 응답 예시:

      ```json
      {
        "id": 1,
        "startAt": "10:00"
      }
      ```

### 시간 삭제

* **DELETE /times/{id}**

    * 설명: 특정 시간을 삭제한다. (예약이 존재할 경우 예외 발생)

---

## 예외 처리

* 시간 형식 오류: HH\:mm 형식이 아닐 경우 400 Bad Request
* 이름이 비어있거나 10자 초과: 400 Bad Request
* 날짜 형식 오류 또는 과거 날짜 예약: 400 Bad Request
* 이미 예약된 시간 중복 예약 시: 400 Conflict
* 예약이 존재하는 시간 삭제 시: 409 Conflict

---
