# API 명세서

## 1. 테마 API

### (1) 테마 생성 (관리자)
**POST** `/admin/themes`

**Request Body**
```json
{
  "name": "string",
  "description": "string",
  "url": "string"
}
```

**Response** `201 Created`
```json
{
  "id": "Long",
  "name": "string",
  "description": "string",
  "url": "string"
}
```

### (2) 전체 테마 조회
**GET** `/themes`

**Response** `200 OK`
```json
[
  {
    "id": "Long",
    "name": "string",
    "description": "string",
    "url": "string"
  }
]
```

### (3) 인기 테마 조회
**GET** `/themes/popular?period={period}&limit={limit}`

**Query Parameters**
- `period`: 조회 기간 (기본값: 7일)
- `limit`: 조회 개수 (기본값: 10개)

**Response** `200 OK`
```json
[
  {
    "id": "Long",
    "name": "string",
    "description": "string",
    "url": "string"
  }
]
```

### (4) 테마 삭제 (관리자)
**DELETE** `/admin/themes/{id}`

**Response** `204 No Content`

---

## 2. 예약 시간 API

### (1) 예약 가능한 시간 조회
**GET** `/times?date={date}&themeId={themeId}`

**Query Parameters**
- `date`: 예약 날짜 (선택, LocalDate 형식)
- `themeId`: 테마 ID (선택)

**Response** `200 OK`
```json
[
  {
    "id": "Long",
    "startAt": "LocalTime (HH:mm)"
  }
]
```

### (2) 예약 시간 생성 (관리자)
**POST** `/admin/times`

**Request Body**
```json
{
  "startAt": "LocalTime (HH:mm)"
}
```

**Response** `201 Created`
```json
{
  "id": "Long",
  "startAt": "LocalTime (HH:mm)"
}
```

### (3) 예약 시간 수정 (관리자)
**PUT** `/admin/times/{id}`

**Request Body**
```json
{
  "startAt": "LocalTime (HH:mm)"
}
```

**Response** `200 OK`

### (4) 예약 시간 삭제 (관리자)
**DELETE** `/admin/times/{id}`

**Response** `204 No Content`

---

## 3. 예약 API

### (1) 전체 예약 조회
**GET** `/reservations`

**Response** `200 OK`
```json
[
  {
    "id": "Long",
    "name": "string",
    "date": "LocalDate",
    "time": {
      "id": "Long",
      "startAt": "LocalTime (HH:mm)"
    },
    "theme": {
      "id": "Long",
      "name": "string",
      "description": "string",
      "url": "string"
    },
    "createdAt": "LocalDateTime"
  }
]
```

### (2) 특정 예약 조회
**GET** `/reservations/{id}`

**Response** `200 OK`
```json
{
  "id": "Long",
  "name": "string",
  "date": "LocalDate",
  "time": {
    "id": "Long",
    "startAt": "LocalTime (HH:mm)"
  },
  "theme": {
    "id": "Long",
    "name": "string",
    "description": "string",
    "url": "string"
  },
  "createdAt": "LocalDateTime"
}
```

### (3) 예약 생성
**POST** `/reservations`

**Request Body**
```json
{
  "name": "string",
  "date": "LocalDate",
  "timeId": "Long",
  "themeId": "Long"
}
```

**Response** `201 Created`
```json
{
  "id": "Long",
  "name": "string",
  "date": "LocalDate",
  "time": {
    "id": "Long",
    "startAt": "LocalTime (HH:mm)"
  },
  "theme": {
    "id": "Long",
    "name": "string",
    "description": "string",
    "url": "string"
  },
  "createdAt": "LocalDateTime"
}
```

### (4) 예약 수정
**PUT** `/reservations/{id}`

**Request Body**
```json
{
  "name": "string",
  "date": "LocalDate",
  "timeId": "Long",
  "themeId": "Long"
}
```

**Response** `200 OK`

### (5) 예약 삭제
**DELETE** `/reservations/{id}`

**Response** `204 No Content`
