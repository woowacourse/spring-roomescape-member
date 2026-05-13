# ReservationTime API 명세

에러 응답 본문 형식은 [Reservation API 명세 #공통 에러 응답 형식](reservation-API.md#공통-에러-응답-형식) 참고.

### 시간 목록 조회

```http
GET /admin/times
```

Response `200 OK`

```json
[
  {
    "id": 1,
    "startAt": "09:00"
  },
  {
    "id": 2,
    "startAt": "10:00"
  }
]
```

### 시간 생성

```http
POST /admin/times
Content-Type: application/json
```

Request Body

```json
{
  "startAt": "10:00"
}
```

검증 규칙

- `startAt`: `null` 불가 (`@NotNull`), ISO `HH:mm` 형식

Response `201 Created`

```json
{
  "id": 1,
  "startAt": "10:00"
}
```

Error Response

| 상태                | `code`              | 조건                       |
|-------------------|---------------------|--------------------------|
| `400 Bad Request` | `VALIDATION_FAILED` | 검증 규칙 위반 (빈 값, 잘못된 형식 등) |

### 시간 삭제

```http
DELETE /admin/times/{id}
```

Response `204 No Content`

설계 결정

- 예약 시간은 관리자가 관리하는 리소스이므로 `/admin/times` 하위로 분리한다.
- 실제 관리자 접근 제한은 이후 인증과 인가를 도입할 때 처리한다.
- 시간 응답 필드는 다른 API와의 일관성을 위해 `startAt`(camelCase)으로 통일한다.
