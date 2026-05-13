# Reservation API 명세

## 공통 에러 응답 형식

모든 에러 응답은 동일한 본문을 따른다.

```json
{
  "code": "RESERVATION_PAST_DATE",
  "path": "/reservations",
  "message": "지난 날짜는 예약할 수 없습니다.",
  "action": "오늘 이후의 날짜로 예약해주세요."
}
```

| 필드        | 설명                                              |
|-----------|-------------------------------------------------|
| `code`    | 프론트가 분기 처리하기 위한 에러 식별자 (`ErrorCode` enum 상수 이름) |
| `path`    | 에러가 발생한 요청 URI                                  |
| `message` | 사용자에게 보일 수 있는 자연어 설명                            |
| `action`  | 해결을 위한 다음 행동 안내 (없을 수 있음)                       |

### 전역 핸들러가 일률 처리하는 경우

| 상태                          | `code`                  | 트리거                                             |
|-----------------------------|-------------------------|-------------------------------------------------|
| `400 Bad Request`           | `VALIDATION_FAILED`     | `@Valid` 위반 또는 요청 본문 파싱 실패                      |
| `400 Bad Request`           | `INVALID_PATH`          | 경로 파라미터 타입 변환 실패 (예: `/admin/reservations/abc`) |
| `400 Bad Request`           | `INVALID_REQUEST`       | 도메인 외 `IllegalArgumentException`                |
| `404 Not Found`             | `RESOURCE_NOT_FOUND`    | 매핑된 핸들러가 없는 경로                                  |
| `500 Internal Server Error` | `INTERNAL_SERVER_ERROR` | 미처리 예외                                          |

500 응답 본문은 다음 형식으로만 응답한다 — 스택 트레이스, SQL, 테이블/컬럼, 예외 클래스명은 포함하지 않는다.

```json
{
  "code": "INTERNAL_SERVER_ERROR",
  "path": "/reservations",
  "message": "요청 처리에 문제가 발생했습니다.",
  "action": null
}
```

---

## 관리자 API

### 전체 예약 조회

```http
GET /admin/reservations
```

Response `200 OK`

```json
[
  {
    "id": 1,
    "name": "브라운",
    "date": "2026-05-10",
    "time": {
      "id": 1,
      "startAt": "10:00"
    },
    "theme": {
      "id": 1,
      "name": "우주",
      "description": "우주 느낌의 몽환적인 방탈출!",
      "thumbnailUrl": "https://example.com/theme1.jpg"
    }
  }
]
```

### 예약 생성 (관리자)

```http
POST /admin/reservations
Content-Type: application/json
```

Request Body

```json
{
  "name": "브라운",
  "date": "2026-05-10",
  "timeId": 1,
  "themeId": 1
}
```

검증 규칙

- `name`: 빈 문자열·공백·`null` 불가 (`@NotBlank`)
- `date`, `timeId`, `themeId`: `null` 불가 (`@NotNull`), `date`는 ISO `yyyy-MM-dd` 형식

Response `201 Created`

```json
{
  "id": 1,
  "name": "브라운",
  "date": "2026-05-10",
  "time": {
    "id": 1,
    "startAt": "10:00"
  },
  "theme": {
    "id": 1,
    "name": "우주",
    "description": "우주 느낌의 몽환적인 방탈출!",
    "thumbnailUrl": "https://example.com/theme1.jpg"
  }
}
```

Error Response

| 상태                | `code`                       | 조건                          |
|-------------------|------------------------------|-----------------------------|
| `400 Bad Request` | `VALIDATION_FAILED`          | Bean Validation 위반 (빈 이름 등) |
| `404 Not Found`   | `RESERVATION_TIME_NOT_FOUND` | 존재하지 않는 `timeId` 참조         |
| `404 Not Found`   | `THEME_NOT_FOUND`            | 존재하지 않는 `themeId` 참조        |
| `409 Conflict`    | `RESERVATION_DUPLICATED`     | 같은 날짜/시간/테마에 예약이 이미 존재      |

> 관리자는 운영상 과거 시점 예약을 정정/등록할 수 있으므로 지난 날짜·지난 시간 검증을 적용하지 않는다.

### 예약 삭제 (관리자)

```http
DELETE /admin/reservations/{reservationId}
```

Response `204 No Content`

---

## 사용자 API

### 본인 예약 조회

```http
GET /reservations?name={name}
```

쿼리 파라미터

| 이름     | 필수 | 설명           |
|--------|----|--------------|
| `name` | 필수 | 조회 대상 사용자 이름 |

Response `200 OK`

```json
[
  {
    "id": 10,
    "name": "브라운",
    "date": "2026-05-12",
    "time": {
      "id": 1,
      "startAt": "10:00"
    },
    "theme": {
      "id": 1,
      "name": "우주",
      "description": "우주 느낌의 몽환적인 방탈출!",
      "thumbnailUrl": "https://example.com/theme1.jpg"
    }
  }
]
```

### 예약 생성 (사용자)

```http
POST /reservations
Content-Type: application/json
```

Request Body

```json
{
  "name": "브라운",
  "date": "2026-05-12",
  "timeId": 1,
  "themeId": 1
}
```

검증 규칙

- `name`: 빈 문자열·공백·`null` 불가 (`@NotBlank`)
- `date`, `timeId`, `themeId`: `null` 불가 (`@NotNull`), `date`는 ISO `yyyy-MM-dd` 형식

정책 (`UserReservationSavePolicy`)

- `date`가 오늘 이전이면 거부 → 422 `RESERVATION_PAST_DATE`
- `date`가 오늘이고 `timeId`가 가리키는 시간이 현재 시각 이전이면 거부 → 422 `RESERVATION_PAST_TIME`

Response `201 Created`

```json
{
  "id": 10,
  "name": "브라운",
  "date": "2026-05-12",
  "time": {
    "id": 1,
    "startAt": "10:00"
  },
  "theme": {
    "id": 1,
    "name": "우주",
    "description": "우주 느낌의 몽환적인 방탈출!",
    "thumbnailUrl": "https://example.com/theme1.jpg"
  }
}
```

Error Response

| 상태                         | `code`                       | 조건                          |
|----------------------------|------------------------------|-----------------------------|
| `400 Bad Request`          | `VALIDATION_FAILED`          | Bean Validation 위반 (빈 이름 등) |
| `404 Not Found`            | `RESERVATION_TIME_NOT_FOUND` | 존재하지 않는 `timeId` 참조         |
| `404 Not Found`            | `THEME_NOT_FOUND`            | 존재하지 않는 `themeId` 참조        |
| `409 Conflict`             | `RESERVATION_DUPLICATED`     | 같은 날짜/시간/테마에 예약이 이미 존재      |
| `422 Unprocessable Entity` | `RESERVATION_PAST_DATE`      | 지난 날짜로 예약 시도                |
| `422 Unprocessable Entity` | `RESERVATION_PAST_TIME`      | 오늘 날짜로 예약하되 시간이 이미 지났음      |

`422 RESERVATION_PAST_DATE` 응답 예

```json
{
  "code": "RESERVATION_PAST_DATE",
  "path": "/reservations",
  "message": "지난 날짜는 예약할 수 없습니다.",
  "action": "오늘 이후의 날짜로 예약해주세요."
}
```

`409 RESERVATION_DUPLICATED` 응답 예

```json
{
  "code": "RESERVATION_DUPLICATED",
  "path": "/reservations",
  "message": "이미 존재하는 예약입니다.",
  "action": "다른 테마, 날짜, 시간으로 예약을 시도해주세요."
}
```

### 예약 삭제 (사용자)

```http
DELETE /reservations/{reservationId}
```

Response `204 No Content`

---

설계 결정

- 관리자 예약 API는 전화·현장 예약을 대신 등록하고 전체 예약을 관리하는 목적이므로 `/admin` 하위로 분리한다.
- 사용자 예약 API와 같은 리소스를 다루지만 사용 목적이 다르므로 경로를 분리해 잘못 사용하는 일을 줄인다.
- 실제 관리자 접근 제한은 이후 인증과 인가를 도입할 때 처리한다.
- 비즈니스 규칙 위반(지난 날짜·시간)은 요청 형식은 유효하므로 `422 Unprocessable Entity`로 응답한다. 자원 상태 충돌(중복 예약)은 `409 Conflict`로, Bean
  Validation으로 잡히는 형식 위반은 `400 Bad Request`로 구분한다.
- 관리자는 운영상 과거 시점의 예약 정정이 필요하므로 사용자와 같은 시간/날짜 정책을 적용하지 않는다.
- 서버 내부 오류는 시스템 구조 노출을 막기 위해 메시지를 일반화하고, 스택 트레이스 등 내부 정보를 본문에 포함하지 않는다.
