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

500 응답 본문은 다음 형식으로만 응답한다 - 스택 트레이스, SQL, 테이블/컬럼, 예외 클래스명은 포함하지 않는다.

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

- `name`: 빈 문자열/공백/`null` 불가 (`@NotBlank`)
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

> 관리자는 운영상 과거 시점 예약을 정정/등록할 수 있으므로 지난 날짜나 지난 시간 검증을 적용하지 않는다.

### 예약 삭제 (관리자)

```http
DELETE /admin/reservations/{reservationId}
```

> 행을 물리적으로 제거하는 **hard delete**다. 사용자의 `DELETE /reservations/{id}`(soft cancel: `isActive=false` 전이)와는 의도가 다르다.
> 운영상 잘못 들어간 예약을 정정하기 위한 용도이므로 이력 보존이 필요 없을 때 사용한다. 삭제된 예약은 조회·집계 어디에도 남지 않는다.

Response `204 No Content`

존재 여부와 무관하게 204로 응답한다 (관리자 삭제 API 공통 규칙 — 테마/시간 삭제와 동일). 결국 존재하지 않는 상태를 만드는 것이 목적이며, DELETE 멱등성과도 맞다.

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
    },
    "isActive": true
  },
  {
    "id": 8,
    "name": "브라운",
    "date": "2026-05-05",
    "time": {
      "id": 2,
      "startAt": "11:00"
    },
    "theme": {
      "id": 2,
      "name": "지하실",
      "description": "스산한 지하실에서 탈출해보세요!",
      "thumbnailUrl": "https://example.com/theme2.jpg"
    },
    "isActive": false
  }
]
```

취소된 예약도 본인 이름으로 조회할 수 있다.

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

- `name`: 빈 문자열/공백/`null` 불가 (`@NotBlank`)
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

### 본인 예약 응답에 포함되는 상태

조회/생성/변경 응답의 각 항목에는 `isActive` 필드가 포함된다.

- `true` - 유효한 예약
- `false` - 취소된 예약 (이력으로 조회됨)

### 예약 변경 (사용자)

```http
PATCH /reservations/{reservationId}
Content-Type: application/json
```

Request Body

```json
{
  "date": "2026-05-15",
  "timeId": 3
}
```

- `themeId`는 변경 대상이 아니므로 받지 않는다.

검증 규칙

- `date`, `timeId`: `null` 불가 (`@NotNull`), `date`는 ISO `yyyy-MM-dd` 형식

정책

- 이미 시작된 예약(예약 시점이 현재 이전)은 변경 불가
- 변경 후 (date, timeId, themeId)가 다른 활성 예약과 충돌하면 거부 - 같은 자리로 변경하는 경우는 허용(자기 자신 제외 검사)
- 변경 후 시점이 과거(`UserReservationSavePolicy`)면 거부

Response `200 OK`

```json
{
  "id": 10,
  "name": "브라운",
  "date": "2026-05-15",
  "time": {
    "id": 3,
    "startAt": "15:00"
  },
  "theme": {
    "id": 1,
    "name": "우주",
    "description": "우주 느낌의 몽환적인 방탈출!",
    "thumbnailUrl": "https://example.com/theme1.jpg"
  },
  "isActive": true
}
```

Error Response

| 상태                         | `code`                        | 조건                                     |
|----------------------------|-------------------------------|----------------------------------------|
| `400 Bad Request`          | `VALIDATION_FAILED`           | Bean Validation 위반                     |
| `404 Not Found`            | `RESERVATION_NOT_FOUND`       | 존재하지 않는 `reservationId`                |
| `404 Not Found`            | `RESERVATION_TIME_NOT_FOUND`  | 변경할 `timeId`가 존재하지 않음                  |
| `409 Conflict`             | `RESERVATION_DUPLICATED`      | 변경 후 (date, time, theme)가 다른 활성 예약과 충돌 |
| `422 Unprocessable Entity` | `RESERVATION_ALREADY_STARTED` | 이미 지난 예약을 변경 시도                        |
| `422 Unprocessable Entity` | `RESERVATION_PAST_DATE`       | 변경 후 날짜가 과거                            |
| `422 Unprocessable Entity` | `RESERVATION_PAST_TIME`       | 변경 후 오늘 + 시간이 과거                       |

### 예약 취소 (사용자)

```http
DELETE /reservations/{reservationId}
```

> 행을 삭제하지 않고 `isActive`를 `false`로 바꾼다. 내 예약 조회에서는 계속 노출되지만 중복, 가능한 예약 시간 조회할 때는 제외한다.

Response `204 No Content`

Error Response

| 상태                         | `code`                        | 조건                      |
|----------------------------|-------------------------------|-------------------------|
| `404 Not Found`            | `RESERVATION_NOT_FOUND`       | 존재하지 않는 `reservationId` |
| `422 Unprocessable Entity` | `RESERVATION_ALREADY_STARTED` | 이미 지난 예약을 취소 시도         |

---

설계 결정

- 관리자 예약 API는 전화나 현장 예약으로 대신 예약을 등록하고 전체 예약을 관리하는 목적이므로 `/admin` 하위로 분리한다.
- 사용자 예약 API와 같은 리소스를 다루지만 사용 목적이 다르므로 경로를 분리해 잘못 사용하는 일을 줄인다.
- 실제 관리자 접근 제한은 이후 인증과 인가를 도입할 때 처리한다.
- 비즈니스 규칙 위반(지난 날짜 혹은 시간)은 요청 형식은 유효하므로 `422 Unprocessable Entity`로 응답한다. 자원 상태 충돌(중복 예약)은 `409 Conflict`로, Bean
  Validation으로 잡히는 형식 위반은 `400 Bad Request`로 구분한다.
- 관리자는 운영상 과거 시점의 예약 정정이 필요하므로 사용자와 같은 시간/날짜 정책을 적용하지 않는다.
- 서버 내부 오류는 시스템 구조 노출을 막기 위해 메시지를 일반화하고, 스택 트레이스 등 내부 정보를 본문에 포함하지 않는다.
- 예약 변경은 `PATCH`로 부분 필드(`date`, `timeId`)만 받는다. `themeId`는 변경 대상이 아니다.
