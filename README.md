# 방탈출 예약 시스템 API 문서

## 인증 API (AuthApiController)

### 로그인

- **URL**: `POST /login`
- **Request Body**:
  ```json
  {
    "email": "string",
    "password": "string"
  }
  ```
- **Response**: authToken 쿠키

### 로그인 체크

- **URL**: `GET /login/check`
- **Auth Required**
- **Response**: 현재 로그인 정보

## 사용자 API (UserApiController)

### 회원 목록 조회

- **URL**: `GET /members`
- **Auth Required**
- **Role Required**: ADMIN
- **Response**: 회원 목록

### 회원가입

- **URL**: `POST /members`
- **Request Body**:
  ```json
  {
    "name": "string",
    "email": "string",
    "password": "string"
  }
  ```
- **Response**: 회원가입된 유저 정보

## 테마 API (ThemeApiController)

### 테마 생성

- **URL**: `POST /themes`
- **Auth Required**
- **Role Required**: ADMIN
- **Request Body**:
  ```json
  {
    "name": "string",
    "description": "string"
  }
  ```
- **Response**: 생성된 테마 정보

### 테마 목록 조회

- **URL**: `GET /themes`
- **Auth Required**: Yes
- **Response**: 테마 목록

## 예약 시간 API (ReservationTimeApiController)

### 예약 시간 생성

- **URL**: `POST /times`
- **Auth Required**
- **Role Required**: ADMIN
- **Request Body**:
  ```json
  {
    "startAt": "HH:mm"
  }
  ```
- **Response**: 생성된 예약 시간 정보

### 예약 시간 조회

- **URL**: `GET /times`
- **Auth Required**
- **Response**: 예약 시간 목록

### 특정 날짜/테마의 가능한 예약 시간 조회

- **URL**: `GET /times/possible`
- **Auth Required**
- **Parameters**:
  - `date`: 날짜 (YYYY-MM-DD)
  - `themeId`: 테마 ID
- **Response**: 예약 가능한 시간 목록

### 예약 시간 삭제

- **URL**: `DELETE /times/{id}`
- **Auth Required**
- **Role Required**: ADMIN

## 예약 API (ReservationApiController)

### 예약 생성

- **URL**: `POST /reservations`
- **Auth Required**
- **Request Body**:
  ```json
  {
    "themeId": "string",
    "reservationDate": "YYYY-MM-DD",
    "timeId": "string"
  }
  ```
- **Response**: 생성된 예약 정보

### 예약 목록 조회

- **URL**: `GET /reservations`
- **Auth Required**
- **Parameters**:
  - `themeId`: 테마 ID (선택)
  - `memberId`: 회원 ID (선택)
  - `dateFrom`: 시작 날짜 (선택)
  - `dateTo`: 종료 날짜 (선택)
- **Response**: 예약 목록

## 인증 관련 정보

1. 보호된 엔드포인트에 접근하려면 유효한 `authToken` 쿠키가 필요합니다.
2. 관리자 권한이 필요한 엔드포인트는 Role이 ADMIN인 사용자만 접근할 수 있습니다.
3. 인증되지 않은 사용자가 보호된 엔드포인트에 접근하면 401 Unauthorized 오류가 발생합니다.
4. 권한이 없는 사용자가 관리자 엔드포인트에 접근하면 403 Forbidden 오류가 발생합니다.
