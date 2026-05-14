# 방탈출 사용자 예약 시스템 (Room Escape User Reservation System)

## 프로젝트 소개

방탈출 사용자 예약 시스템은 **Spring Boot 기반의 REST API 서버**로, 방탈출 게임 예약 서비스를 관리하는 웹 애플리케이션입니다.

## 기술 스택

- **프레임워크**: Spring Boot 3.4.4
- **언어**: Java 21
- **데이터베이스**: H2 (In-memory)
- **빌드 도구**: Gradle
- **테스트**: JUnit 5, REST Assured


## 프로젝트 구조

```
src/main/java/roomescape/
├── config/
│   └── RestConfig.java
├── controller/
│   ├── ReservationController.java
│   ├── ReservationTimeController.java
│   └── ThemeController.java
├── service/
│   ├── ReservationService.java
│   ├── ReservationTimeService.java
│   └── ThemeService.java
├── repository/
│   ├── reservation/
│   │   ├── ReservationRepository.java
│   │   └── JdbcReservationRepository.java
│   ├── reservationTime/
│   │   ├── ReservationTimeRepository.java
│   │   └── JdbcReservationTimeRepository.java
│   └── theme/
│       ├── ThemeRepository.java
│       └── JdbcThemeRepository.java
├── domain/
│   ├── reservation/
│   │   └── Reservation.java
│   ├── reservationTime/
│   │   ├── ReservationTime.java
│   │   ├── ReservationTimeCondition.java
│   │   └── ReservationTimeWithAvailable.java
│   └── theme/
│       ├── Theme.java
│       ├── ThemeWithCount.java
│       └── PopularThemeCondition.java
├── dto/
│   ├── reservation/
│   │   ├── AddReservationRequest.java
│   │   ├── UpdateReservationRequest.java
│   │   ├── ReservationResponse.java
│   │   └── ReservationCondition.java
│   ├── reservationTime/
│   │   ├── AddReservationTimeRequest.java
│   │   ├── ReservationTimeResponse.java
│   │   └── AvailableReservationTimeResponse.java
│   └── theme/
│       ├── AddThemeRequest.java
│       ├── ThemeResponse.java
│       ├── PopularConditionRequest.java
│       └── PopularThemeResponse.java
├── exception/
│   ├── exception/
│   │   ├── CustomException.java
│   │   ├── BaseCustomException.java
│   │   ├── InvalidRequestException.java
│   │   ├── DuplicatedResourceException.java
│   │   ├── NotFoundResourceException.java
│   │   └── DataReferencedException.java
│   ├── dto/
│   │   ├── ErrorCode.java
│   │   ├── ErrorResponse.java
│   │   └── FieldErrorResponse.java
│   └── handler/
│       └── GlobalExceptionHandler.java
└── RoomescapeApplication.java
```

## 주요 기능

### 1. 예약 관리
- **예약 조회**: 현재 예약된 모든 정보 조회
- **예약 생성**: 특정 날짜/시간/테마로 방탈출 게임 예약
- **예약 취소**: 예약된 게임 취소
- **사용자별 예약 조회**: 사용자가 본인의 예약 조회
- **사용자별 예약 취소**: 사용자가 본인의 예약 취소
- **사용자별 예약 수정**: 사용자가 본인의 예약 수정

### 2. 예약 시간 관리
- **예약 시간 조회**: 모든 예약 시간 목록 조회
- **예약 시간 추가**: 새로운 예약 시간대 생성
- **예약 시간 삭제**: 기존 예약 시간대 삭제
- **예약 가능 시간 조회**: 특정 날짜와 테마 기준으로 예약 가능 여부 포함한 시간 목록 조회

### 3. 테마 관리
- **테마 조회**: 모든 방탈출 테마 목록 조회
- **테마 추가**: 새로운 방탈출 테마 생성
- **테마 삭제**: 기존 방탈출 테마 삭제
- **인기 테마 조회**: 특정 기간 내 예약 횟수 기준 인기 테마 조회


## 데이터 모델

### Reservation (예약)
| 필드 | 타입 | 설명 |
|------|------|------|
| id | long | 예약 고유 ID |
| name | String | 예약자 이름 |
| date | String | 예약 날짜 |
| time | ReservationTime | 예약 시간 객체 |
| theme | Theme | 테마 객체 |

### ReservationTime (예약 시간)
| 필드 | 타입 | 설명 |
|------|------|------|
| id | long | 예약 시간 고유 ID |
| startAt | String | 시작 시간 |

### Theme (테마)
| 필드 | 타입 | 설명 |
|------|------|------|
| id | long | 테마 고유 ID |
| name | String | 테마 이름 |
| description | String | 테마 설명 |
| imageUrl | String | 썸네일 이미지 URL |


## API 엔드포인트

### 예약 관련

| 메서드 | URL | 설명 | 상태 코드 |
|--------|-----|------|-----------|
| GET | `/reservations` | 모든 예약 조회 | 200 |
| GET | `/reservations?name={name}` | 이름으로 예약 조회 | 200 |
| POST | `/reservations` | 새로운 예약 생성 | 201 |
| PATCH | `/reservations/{id}` | 예약 수정 | 200 |
| DELETE | `/reservations/{id}` | 예약 삭제 | 204 |

**POST /reservations - Request Body**
| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| name | String | Y | 예약자 이름 (1~20자) |
| date | LocalDate | Y | 예약 날짜 (YYYY-MM-DD) |
| timeId | Long | Y | 예약 시간 ID (1 이상) |
| themeId | Long | Y | 예약 테마 ID (1 이상) |

**PATCH /reservations/{id} - Request Body**
| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| name | String | Y | 예약자 이름 (1~20자) |
| date | LocalDate | Y | 예약 날짜 (YYYY-MM-DD) |
| timeId | Long | Y | 예약 시간 ID (1 이상) |

**GET /reservations, POST /reservations, PATCH /reservations/{id} - Response**
| 필드 | 타입 | 설명 |
|------|------|------|
| id | long | 예약 고유 ID |
| name | String | 예약자 이름 |
| date | LocalDate | 예약 날짜 |
| time | ReservationTimeResponse | → 공통 타입 참고 |
| theme | ThemeResponse | → 공통 타입 참고 |

---

### 예약 시간 관련

| 메서드 | URL | 설명 | 상태 코드 |
|--------|-----|------|-----------|
| GET | `/times` | 모든 예약 시간 조회 | 200 |
| POST | `/times` | 새로운 예약 시간 추가 | 200 |
| GET |	`/times/availability` |	날짜·테마 기준 예약 가능 시간 조회 |	200 |
| DELETE | `/times/{id}` | 예약 시간 삭제 | 204 |

**POST /times - Request Body**
| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| startAt | String | Y | 시작 시간 (HH:mm) |

**GET /times, POST /times - Response**
| 필드 | 타입 | 설명 |
|------|------|------|
| id | long | 예약 시간 고유 ID |
| startAt | String | 시작 시간 |

**GET /times/availability - Query Parameters**
| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| date | String | Y | 조회 날짜 (YYYY-MM-DD) |
| themeId | long | Y | 테마 ID |

**GET /times/availability - Response**
| 필드 | 타입 | 설명 |
|------|------|------|
| id | long | 예약 시간 고유 ID |
| startAt | String | 시작 시간 (HH:mm) |
| isAvailable | boolean | 예약 가능 여부 |

---

### 테마 관련

| 메서드 | URL | 설명 | 상태 코드 |
|--------|-----|------|-----------|
| GET | `/themes` | 모든 테마 조회 | 200 |
| POST | `/themes` | 새로운 테마 추가 | 201 |
| DELETE | `/themes/{id}` | 테마 삭제 | 204 |
| GET | `/themes/popular` | 인기 테마 조회 | 200 |

**POST /themes - Request Body**
| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| name | String | Y | 테마 이름 |
| description | String | Y | 테마 설명 |
| imageUrl | String | Y | 썸네일 이미지 URL |

**GET /themes, POST /themes - Response**
| 필드 | 타입 | 설명 |
|------|------|------|
| id | long | 테마 고유 ID |
| name | String | 테마 이름 |
| description | String | 테마 설명 |
| imageUrl | String | 썸네일 이미지 URL |

**GET /themes/popular - Query Parameters**
| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| start_date | String | Y | 조회 시작 날짜 (YYYY-MM-DD) |
| end_date | String | Y | 조회 종료 날짜 (YYYY-MM-DD) |
| size | long | Y | 조회할 테마 개수 |

**GET /themes/popular - Response**
| 필드 | 타입 | 설명 |
|------|------|------|
| id | long | 테마 고유 ID |
| name | String | 테마 이름 |
| description | String | 테마 설명 |
| imageUrl | String | 썸네일 이미지 URL |
| count | long | 기간 내 예약 횟수 |

---

### 에러 응답 공통 포맷

```json
{
  "errorCode": "에러 코드",
  "errorMessage": "에러 메시지",
  "fieldErrors": [
    {
      "errorField": "필드명",
      "errorMessage": "필드 에러 메시지"
    }
  ]
}
```

> `fieldErrors`는 유효성 검증 실패 시에만 포함되며, 그 외 경우 빈 배열 `[]` 반환

---

### 에러 코드 목록

**예약**

| 에러 코드 | HTTP 상태 | 메시지 | 발생 상황 |
|-----------|-----------|--------|-----------|
| `DUPLICATED_RESERVATION` | 409 | 해당 날짜, 시간, 테마의 예약이 존재하여 예약할 수 없습니다. | 동일 날짜·시간·테마 중복 예약 시 |
| `INVALID_RESERVATION_DATE` | 400 | 지난 날짜에는 예약할 수 없습니다. | 과거 날짜로 예약 생성·수정 시 |
| `INVALID_RESERVATION_TIME` | 400 | 지난 시간에는 예약할 수 없습니다. | 오늘 날짜에 이미 지난 시간으로 예약 시 |
| `NOT_FOUND_RESERVATION` | 404 | 존재하지 않는 예약입니다. | 예약 수정·삭제 시 ID 미존재 |
| `UNAUTHORIZED_RESERVATION_ACCESS` | 400 | 본인의 예약만 접근 가능합니다. | 타인 예약 수정·삭제 시도 시 |

**테마**

| 에러 코드 | HTTP 상태 | 메시지 | 발생 상황 |
|-----------|-----------|--------|-----------|
| `NOT_FOUND_THEME` | 404 | 존재하지 않는 테마입니다. | 예약 생성 시 테마 ID 미존재 |
| `DUPLICATED_THEME` | 409 | 해당 테마가 이미 존재합니다. | 동일 이름 테마 추가 시 |
| `CANNOT_DELETE_THEME_IN_USE` | 409 | 해당 테마를 참조하는 예약 데이터가 존재하기 때문에 삭제할 수 없습니다. | 예약에 사용 중인 테마 삭제 시 |

**예약 시간**

| 에러 코드 | HTTP 상태 | 메시지 | 발생 상황 |
|-----------|-----------|--------|-----------|
| `NOT_FOUND_RESERVATION_TIME` | 404 | 존재하지 않는 예약 시간입니다. | 예약 생성·수정 시 시간 ID 미존재 |
| `DUPLICATED_RESERVATION_TIME` | 409 | 해당 시간이 이미 존재합니다. | 동일 시간 추가 시 |
| `CANNOT_DELETE_RESERVATION_TIME_IN_USE` | 409 | 해당 시간을 참조하는 예약 데이터가 존재하기 때문에 삭제할 수 없습니다. | 예약에 사용 중인 시간 삭제 시 |

**공통**

| 에러 코드 | HTTP 상태 | 메시지 | 발생 상황 |
|-----------|-----------|--------|-----------|
| `INVALID_INPUT` | 400 | 입력값이 올바르지 않습니다. | `@Valid` 유효성 검증 실패 시 |
| `INVALID_REQUEST_FORMAT` | 400 | 입력값의 형식이 올바르지 않습니다. | 요청 본문 파싱 실패 시 |
| `INTEGRITY_VIOLATION_ON_DELETE` | 409 | 데이터 무결성 위반으로 삭제에 실패했습니다. | DB 무결성 제약 위반 시 |
| `SERVER_ERROR` | 500 | 서버 오류 | 예상치 못한 서버 오류 발생 시 |

---

## 데이터베이스

프로젝트는 **H2 In-Memory 데이터베이스**를 사용하며, 초기 스키마와 초기 데이터 저장 쿼리는 다음 파일에 정의됩니다:
- `src/main/resources/schema.sql` - 테이블 정의
- `src/main/resources/data.sql` - 초기 데이터 저장
