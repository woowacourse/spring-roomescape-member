# 방탈출 예약 관리 시스템 🚪

## 📌 소개

- 방탈출 예약을 관리할 수 있는 웹 애플리케이션입니다.
- 예약 시간 관리 기능을 제공합니다.
- 사용자 친화적인 UI/UX를 제공합니다.

## 📌 기능

- 예약 전체 조회
- 예약 시간 전체 조회
- 예약 가능한 시간 조회
- 테마 전체 조회
- 예약 추가
- 예약 시간 추가
- 테마 추가
- 예약 삭제
- 예약 시간 삭제
- 테마 삭제

## 📌 프로젝트 구조

```
📦roomescape
 ┣ 📂controller
 ┃ ┣ 📜AdminPageController.java
 ┃ ┣ 📜ReservationController.java
 ┃ ┣ 📜ReservationTimeController.java
 ┃ ┣ 📜ThemeController.java
 ┃ ┗ 📜UserPageController.java
 ┣ 📂dao
 ┃ ┣ 📜JdbcReservationDao.java
 ┃ ┣ 📜JdbcReservationTimeDao.java
 ┃ ┣ 📜JdbcThemeDao.java
 ┃ ┣ 📜ReservationDao.java
 ┃ ┣ 📜ReservationTimeDao.java
 ┃ ┗ 📜ThemeDao.java
 ┣ 📂domain_entity
 ┃ ┣ 📜Reservation.java
 ┃ ┣ 📜ReservationTime.java
 ┃ ┗ 📜Theme.java
 ┣ 📂dto
 ┃ ┣ 📜ReservationRequestDto.java
 ┃ ┣ 📜ReservationResponseDto.java
 ┃ ┣ 📜ReservationTimeAvailableResponse.java
 ┃ ┣ 📜ReservationTimeRequestDto.java
 ┃ ┣ 📜ReservationTimeResponseDto.java
 ┃ ┣ 📜ThemeRequestDto.java
 ┃ ┗ 📜ThemeResponseDto.java
 ┣ 📂global
 ┃ ┣ 📜ExceptionResponse.java
 ┃ ┗ 📜GlobalExceptionHandler.java
 ┣ 📂mapper
 ┃ ┣ 📜ReservationMapper.java
 ┃ ┣ 📜ReservationTimeMapper.java
 ┃ ┗ 📜ThemeMapper.java
 ┣ 📂service
 ┃ ┣ 📜ReservationService.java
 ┃ ┣ 📜ReservationTimeService.java
 ┃ ┗ 📜ThemeService.java
 ┗ 📜RoomescapeApplication.java
```

## 📌 API 명세

### 예약 관련 API

| Method | URL                 | Description | HTTP Status    |
|--------|---------------------|-------------|----------------|
| GET    | /reservations       | 예약 전체 조회    | 200 OK         |
| POST   | /reservations       | 사용자 예약 추가   | 201 CREATED    |
| POST   | /admin/reservations | 관리자 예약 추가   | 201 CREATED    |
| DELETE | /reservations/{id}  | 예약 삭제       | 204 NO CONTENT |

-[x] 예약 생성 시 관리자면 리퀘스트바디로 전달한 memberId에 맞는 회원의 예약을 생성한다.
-[x] 예약 생성 시 사용자면 자신의 토큰으로 전달한 로그인 정보로 예약을 생성한다.

### 예약 시간 관련 API

| Method | URL              | Description  | HTTP Status    |
|--------|------------------|--------------|----------------|
| GET    | /times           | 예약 시간 전체 조회  | 200 OK         |
| GET    | /times/available | 예약 가능한 시간 조회 | 200 OK         |
| POST   | /times           | 예약 시간 추가     | 201 CREATED    |
| DELETE | /times/{id}      | 예약 시간 삭제     | 204 NO CONTENT |

### 테마 관련 API

| Method | URL          | Description | HTTP Status    |
|--------|--------------|-------------|----------------|
| GET    | /themes      | 테마 전체 조회    | 200 OK         |
| GET    | /themes/rank | 테마 순위 조회    | 200 OK         |
| POST   | /themes      | 테마 추가       | 201 CREATED    |
| DELETE | /themes/{id} | 테마 삭제       | 204 NO CONTENT |

### 회원 관련 API

| Method | URL          | Description   | HTTP Status |
|--------|--------------|---------------|-------------|
| GET    | /members     | 전체 회원 조회      | 200 OK      |
| GET    | /login/check | 로그인 상태 확인     | 200 OK      |
| GET    | /signup      | 회원가입 및 로그인 처리 | 200 OK      |
| POST   | /login       | 사용자 로그인 처리    | 200 OK      |
| POST   | /logout      | 사용자 로그아웃 처리   | 200 OK      |
| POST   | /members     | 회원가입 처리       | 201 CREATED |
