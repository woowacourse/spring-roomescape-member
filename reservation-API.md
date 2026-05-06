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

설계 결정

- 관리자 예약 API는 전화나 현장 예약을 대신 등록하고 전체 예약을 관리하는 목적이므로 `/admin` 하위로 분리한다.
- 사용자 예약 API와 같은 예약 리소스를 다루지만 사용 목적이 다르므로 경로를 분리해 잘못 사용하는 일을 줄인다.
- `/admin`은 보안 장치가 아니라 관리자 목적의 API임을 드러내는 경로 구분이다.
- 실제 관리자 접근 제한은 이후 인증과 인가를 도입할 때 처리한다.
