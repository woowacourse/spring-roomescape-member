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

Error Response

| 상태             | `code`                    | 조건                         |
|----------------|---------------------------|----------------------------|
| `409 Conflict` | `RESERVATION_TIME_IN_USE` | 해당 시간을 참조하는 예약이 존재하여 삭제 불가 |

`409 RESERVATION_TIME_IN_USE` 응답 예

```json
{
  "code": "RESERVATION_TIME_IN_USE",
  "path": "/admin/times/3",
  "message": "예약이 존재하는 시간은 삭제할 수 없습니다.",
  "action": "해당 시간의 예약을 먼저 취소해주세요."
}
```

---

설계 결정

- 예약 시간은 관리자가 관리하는 리소스이므로 `/admin/times` 하위로 분리한다.
- 실제 관리자 접근 제한은 이후 인증과 인가를 도입할 때 처리한다.
- 시간 응답 필드는 다른 API와의 일관성을 위해 `startAt`(camelCase)으로 통일한다.
- 예약이 참조 중인 시간 삭제는 자원 상태 충돌이므로 `409 Conflict`로 응답한다 (요청 형식 자체는 유효).
