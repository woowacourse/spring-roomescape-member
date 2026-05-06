# Spring Roomescape Member API

이 문서는 현재 컨트롤러 코드 기준의 HTTP API 명세입니다.

## 공통 규칙

- Base URL: 미정 (서버 호스트/포트를 사용하세요).
- Content-Type: JSON 요청/응답은 `application/json`.
- 날짜 형식: `yyyy-MM-dd` (예: `2026-05-01`).
- 시간 형식: `HH:mm:ss` (예: `10:00:00`).

## 오류 응답

처리된 오류는 아래 형태로 반환됩니다.

```json
{
  "message": "string",
  "timestamp": "2026-05-01T12:34:56.789"
}
```

`GlobalExceptionHandler` 기준 공통 상태 코드:

- `400 Bad Request`: 검증 실패, 예약/시간/테마 중복, 예약 제약 위반.
- `404 Not Found`: 예약 시간 또는 예약을 찾을 수 없음.
- `409 Conflict`: 예약이 있어 시간 삭제 불가.
- `500 Internal Server Error`: 처리되지 않은 예외.

## 테마

### GET /themes

쿼리 파라미터:

- `sort` (기본값: `id`)
- `order` (기본값: `DESC`)
- `startDate` (선택, 형식 `yyyy-MM-dd`)
- `endDate` (선택, 형식 `yyyy-MM-dd`)
- `limit` (선택)

서버 기본값:

- `endDate`: 오늘
- `startDate`: `endDate - 7 days`

응답: `200 OK`

```json
[
  {
    "id": 1,
    "name": "Roomescape A",
    "description": "...",
    "thumbnail": "https://example.com/a.png"
  }
]
```

### POST /admin/themes

요청 본문:

```json
{
  "name": "Roomescape A",
  "description": "...",
  "thumbnail": "https://example.com/a.png"
}
```

응답: `201 Created`

```json
{
  "id": 1,
  "name": "Roomescape A",
  "description": "...",
  "thumbnail": "https://example.com/a.png"
}
```

### DELETE /admin/themes/{id}

응답: `204 No Content`

## 예약 시간

### GET /times

응답: `200 OK`

```json
[
  {
    "id": 1,
    "startAt": "10:00:00"
  }
]
```

### POST /times

요청 본문:

```json
{
  "startAt": "10:00:00"
}
```

응답: `200 OK`

```json
{
  "id": 1,
  "startAt": "10:00:00"
}
```

### DELETE /times/{id}

응답: `200 OK`

가능한 오류:

- `409 Conflict` 예약이 존재하는 시간은 삭제 불가.

## 스케줄

### GET /schedules

쿼리 파라미터:

- `date` (필수, 형식 `yyyy-MM-dd`)
- `themeId` (필수)

응답: `200 OK`

```json
{
  "themeId": 3,
  "date": "2026-05-01",
  "schedules": [
    {
      "timeId": 1,
      "startAt": "10:00:00",
      "isAvailable": true
    }
  ]
}
```

## 예약

### GET /reservations

응답: `200 OK`

```json
[
  {
    "id": 1,
    "themeResponse": {
      "id": 2,
      "name": "Roomescape A",
      "description": "...",
      "thumbnail": "https://example.com/a.png"
    },
    "name": "Brown",
    "date": "2026-05-01",
    "time": {
      "id": 1,
      "startAt": "10:00:00"
    }
  }
]
```

### POST /reservations

요청 본문:

```json
{
  "themeId": 2,
  "name": "Brown",
  "date": "2026-05-01",
  "timeId": 1
}
```

응답: `200 OK`

```json
{
  "id": 1,
  "themeResponse": {
    "id": 2,
    "name": "Roomescape A",
    "description": "...",
    "thumbnail": "https://example.com/a.png"
  },
  "name": "Brown",
  "date": "2026-05-01",
  "time": {
    "id": 1,
    "startAt": "10:00:00"
  }
}
```

가능한 오류:

- `400 Bad Request` 중복 예약 또는 유효성 검증 실패.
- `404 Not Found` `timeId` 또는 `themeId`가 존재하지 않음.

### DELETE /reservations/{id}

요청 본문 (JSON이 아닌 raw string):

```
Brown
```

응답: `200 OK`

가능한 오류:

- `400 Bad Request` 이름이 일치하지 않음.

## 관리자 예약

### POST /admin/reservations

요청 본문:

```json
{
  "themeId": 2,
  "name": "Brown",
  "date": "2026-05-01",
  "timeId": 1
}
```

응답: `201 Created`

```json
{
  "id": 1,
  "name": "Brown",
  "date": "2026-05-01",
  "time": {
    "id": 1,
    "startAt": "10:00:00"
  },
  "theme": {
    "id": 2,
    "name": "Roomescape A",
    "description": "...",
    "thumbnail": "https://example.com/a.png"
  }
}
```

가능한 오류:

- `404 Not Found` `timeId` 또는 `themeId`가 존재하지 않음.

### DELETE /admin/reservations/{id}

응답: `204 No Content`
