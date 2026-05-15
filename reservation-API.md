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

### 예약 생성

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

### 예약 삭제

```http
DELETE /admin/reservations/{reservationId}
```

Response `204 No Content`

### 사용자 예약 조회

```http
GET /reservations?name={name}
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
    "id": 3,
    "name": "브라운",
    "date": "2026-05-12",
    "time": {
      "id": 3,
      "startAt": "12:00"
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

### 사용자 예약 생성

```http
POST /reservations
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

### 사용자 예약 취소

```http
DELETE /reservations/{reservationId}
```

Response `204 No Content`

### 사용자 예약 변경

```http
PATCH /reservations/{reservationId}
Content-Type: application/json
```

Request Body

```json
{
  "date": "2026-05-16",
  "timeId": 2
}
```

Response `200 OK`

```json
{
  "id": 1,
  "name": "브라운",
  "date": "2026-05-16",
  "time": {
    "id": 2,
    "startAt": "11:00"
  },
  "theme": {
    "id": 1,
    "name": "우주",
    "description": "우주 느낌의 몽환적인 방탈출!",
    "thumbnailUrl": "https://example.com/theme1.jpg"
  }
}
```

### 에러 응답

공통 에러 응답 형식

```json
{
  "code": "ERROR_CODE",
  "path": "/요청 경로",
  "message": "사용자에게 보여줄 메시지",
  "action": "사용자가 다음에 할 수 있는 행동"
}
```

예약이 존재하지 않는 경우

Response `404 Not Found`

```json
{
  "code": "RESERVATION_NOT_FOUND",
  "path": "/reservations/1",
  "message": "예약을 찾을 수 없습니다.",
  "action": "예약 정보를 다시 확인해주세요."
}
```

지난 예약을 취소하거나 변경하는 경우

Response `400 Bad Request`

```json
{
  "code": "RESERVATION_ALREADY_PAST",
  "path": "/reservations/1",
  "message": "이미 지난 예약입니다.",
  "action": "지난 예약은 취소할 수 없습니다."
}
```

지난 날짜와 시간으로 예약을 생성하거나 변경하는 경우

Response `400 Bad Request`

```json
{
  "code": "RESERVATION_PAST_TIME",
  "path": "/reservations",
  "message": "지난 날짜와 시간은 예약할 수 없습니다.",
  "action": "오늘 이후의 예약 가능 시간을 선택해주세요."
}
```

이미 같은 날짜, 시간, 테마의 예약이 존재하는 경우

Response `409 Conflict`

```json
{
  "code": "RESERVATION_DUPLICATED",
  "path": "/reservations",
  "message": "이미 존재하는 예약입니다.",
  "action": "다른 테마, 날짜, 시간으로 예약을 시도해주세요."
}
```

요청 값이 올바르지 않은 경우

Response `400 Bad Request`

```json
{
  "code": "INVALID_INPUT",
  "path": "/reservations",
  "message": "예약 날짜는 필수입니다.",
  "action": "입력값을 확인해주세요."
}
```

설계 결정

- 관리자 예약 API는 전화나 현장 예약을 대신 등록하고 전체 예약을 관리하는 목적이므로 `/admin` 하위로 분리한다.
- 사용자 예약 API와 같은 예약 리소스를 다루지만 사용 목적이 다르므로 경로를 분리해 잘못 사용하는 일을 줄인다.
- `/admin`은 관리자 목적의 API임을 드러내는 경로 구분이다.
- 실제 관리자 접근 제한은 이후 인증과 인가를 도입할 때 처리한다.
