# 방탈출 예약 관리

방탈출 테마, 예약 시간, 사용자 예약을 관리하는 Spring Boot 기반 예약 서비스입니다.

## 기능 목록

### 사용자
- 이름으로 로그인하고 세션 기반으로 본인 예약을 관리한다.
- 테마 목록과 최근 7일 기준 인기 테마를 조회한다.
- 테마와 날짜를 선택해 예약 가능한 시간을 조회한다.
- 예약을 생성한다.
- 본인의 예약 목록을 조회한다.
- 본인의 예약 날짜와 시간을 변경한다.
- 본인의 예약을 취소한다.

### 관리자
- 전체 예약 목록을 조회한다.
- 예약을 삭제한다.
- 테마를 추가하고 삭제한다.
- 예약 시간을 조회, 추가, 삭제한다.

### 공통
- API 에러 응답은 `{code, details, message}` 형식으로 통일한다.
- 예약 생성/변경 시 과거 날짜, 과거 시간, 중복 예약, 존재하지 않는 테마/시간을 검증한다.

## API 명세

### 인증

| 기능 | Method | URL | Request | Response |
|:---|:---|:---|:---|:---|
| 로그인 | `POST` | `/login` | `{username}` | `204` |

로그인 성공 시 서버 세션에 로그인 사용자 정보가 저장됩니다.

### 테마

| 기능 | Method | URL | Request | Response |
|:---|:---|:---|:---|:---|
| 테마 목록 조회 | `GET` | `/themes` | - | `200 [{id, name, description, thumbnailUrl}]` |
| 인기 테마 조회 | `GET` | `/themes/popular?limit={n}` | - | `200 [{id, name, description, thumbnailUrl}]` |
| 테마별 예약 가능 시간 조회 | `GET` | `/themes/{id}/times?date={yyyy-MM-dd}` | - | `200 [{id, startAt, isAvailable}]` |

### 예약

| 기능 | Method | URL | Request | Response |
|:---|:---|:---|:---|:---|
| 내 예약 조회 | `GET` | `/reservations/me` | 로그인 세션 | `200 [Reservation]` |
| 예약 생성 | `POST` | `/reservations` | `{name, date, timeId, themeId}` | `201 Reservation` |
| 내 예약 날짜/시간 변경 | `PATCH` | `/reservations/{id}` | 로그인 세션, `{date, timeId}` | `200 Reservation` |
| 내 예약 취소 | `DELETE` | `/reservations/{id}` | 로그인 세션 | `204` |

`Reservation`

```json
{
  "id": 1,
  "date": "2026-05-20",
  "themeId": 1,
  "themeName": "공포의 저택",
  "themeDescription": "버려진 저택에서 탈출하라!",
  "themeThumbnailUrl": "https://example.com/theme.jpg",
  "timeId": 1,
  "time": "10:00"
}
```

### 예약 시간

| 기능 | Method | URL | Request | Response |
|:---|:---|:---|:---|:---|
| 시간 목록 조회 | `GET` | `/times` | - | `200 [{id, startAt}]` |
| 시간 추가 | `POST` | `/times` | `{startAt}` | `201 {id, startAt}` |
| 시간 삭제 | `DELETE` | `/times/{id}` | - | `204` |

### 관리자

| 기능 | Method | URL | Request | Response |
|:---|:---|:---|:---|:---|
| 전체 예약 조회 | `GET` | `/admin/reservations` | - | `200 [{id, name, date, themeName, time}]` |
| 예약 삭제 | `DELETE` | `/admin/reservations/{id}` | - | `204` |
| 테마 추가 | `POST` | `/admin/themes` | `{name, description, thumbnailUrl}` | `201 {id, name, description, thumbnailUrl}` |
| 테마 삭제 | `DELETE` | `/admin/themes/{id}` | - | `204` |

## 에러 응답

```json
{
  "code": "RESERVATION_ALREADY_RESERVED",
  "details": null,
  "message": "누군가 이미 예약한 시간입니다."
}
```

| 상태 코드 | 주요 코드 |
|:---|:---|
| `400` | `VALIDATION_ERROR`, `INVALID_REQUEST`, `NOT_READABLE_MESSAGE` |
| `404` | `RESERVATION_NOT_FOUND`, `RESERVATION_TIME_NOT_FOUND`, `THEME_NOT_FOUND` |
| `409` | `RESERVATION_ALREADY_RESERVED`, `RESERVATION_TIME_ALREADY_USED`, `RESERVATION_TIME_ALREADY_EXISTS` |
| `422` | `RESERVATION_DATE_IN_PAST`, `RESERVATION_TIME_IN_PAST`, `RESERVATION_NOT_CHANGED` |
| `500` | `UNKNOWN_ERROR` |
