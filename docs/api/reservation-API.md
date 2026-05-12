# Reservation API 명세

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
  },
  {
    "id": 2,
    "name": "초록",
    "date": "2026-05-11",
    "time": {
      "id": 2,
      "startAt": "11:00"
    },
    "theme": {
      "id": 2,
      "name": "지하실",
      "description": "스산한 지하실에서 탈출해보세요!",
      "thumbnailUrl": "https://example.com/theme2.jpg"
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

| 상태                | 조건                            |
|-------------------|-------------------------------|
| `400 Bad Request` | 검증 규칙 위반(빈 이름, 잘못된 날짜 형식 등)   |
| `404 Not Found`   | 존재하지 않는 `timeId`/`themeId` 참조 |

> 관리자 경로는 지나간 날짜 예약 정책 검증을 거치지 않는다.

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

정책

- `date`는 오늘 이전이면 거부 (`UserReservationSavePolicy`)

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

| 상태                | 조건                            |
|-------------------|-------------------------------|
| `400 Bad Request` | 검증 규칙 위반 / 지나간 날짜로 예약 시도      |
| `404 Not Found`   | 존재하지 않는 `timeId`/`themeId` 참조 |

### 예약 삭제 (사용자)

```http
DELETE /reservations/{reservationId}
```

Response `204 No Content`

---

설계 결정

- 관리자 예약 API는 전화·현장 예약을 대신 등록하고 전체 예약을 관리하는 목적이므로 `/admin` 하위로 분리한다.
- 사용자 예약 API와 같은 예약 리소스를 다루지만 사용 목적이 다르므로 경로를 분리해 잘못 사용하는 일을 줄인다.
- `/admin`은 관리자 목적의 API임을 드러내는 경로 구분이다.
- 실제 관리자 접근 제한은 이후 인증과 인가를 도입할 때 처리한다.
- 사용자 예약은 `UserReservationSavePolicy`로 지나간 날짜를 거부한다. 관리자는 같은 정책을 통과하지 않으므로 운영상 과거 시점 예약을 등록할 수 있다.
- 잘못된 입력 검증은 Bean Validation으로 컨트롤러 진입 시점에 일괄 처리하고, 위반 시 400을 반환한다(`GlobalExceptionHandler`).
