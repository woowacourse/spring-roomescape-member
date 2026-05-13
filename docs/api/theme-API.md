# Theme API 명세

에러 응답 본문 형식은 [Reservation API 명세 #공통 에러 응답 형식](reservation-API.md#공통-에러-응답-형식) 참고.

### 테마 목록 조회

```http
GET /admin/themes
```

Response `200 OK`

```json
[
  {
    "id": 1,
    "name": "우주",
    "description": "우주 느낌의 몽환적인 방탈출!",
    "thumbnailUrl": "https://example.com/theme1.jpg"
  },
  {
    "id": 2,
    "name": "지하실",
    "description": "스산한 지하실에서 탈출해보세요!",
    "thumbnailUrl": "https://example.com/theme2.jpg"
  }
]
```

### 테마 생성

```http
POST /admin/themes
Content-Type: application/json
```

Request Body

```json
{
  "name": "우주",
  "description": "우주 느낌의 몽환적인 방탈출!",
  "thumbnailUrl": "https://example.com/theme1.jpg"
}
```

검증 규칙

- `name`, `description`, `thumbnailUrl`: 빈 문자열·공백·`null` 불가 (`@NotBlank`)

Response `201 Created`

```json
{
  "id": 1,
  "name": "우주",
  "description": "우주 느낌의 몽환적인 방탈출!",
  "thumbnailUrl": "https://example.com/theme1.jpg"
}
```

Error Response

| 상태                | `code`              | 조건                       |
|-------------------|---------------------|--------------------------|
| `400 Bad Request` | `VALIDATION_FAILED` | 검증 규칙 위반 (빈 값, 잘못된 형식 등) |

### 테마 삭제

```http
DELETE /admin/themes/{themeId}
```

Response `204 No Content`

### 이용 가능 시간 조회

```http
GET /themes/{themeId}/times?date=2026-05-10
```

쿼리 파라미터

| 이름     | 필수 | 설명                       |
|--------|----|--------------------------|
| `date` | 선택 | 조회 기준 날짜. 생략 시 오늘 날짜로 조회 |

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

### 인기 테마 조회

```http
GET /themes/popular
```

최근 1주(오늘 기준 7일 전 ~ 1일 전)의 예약을 집계해 상위 10개를 반환한다.

Response `200 OK`

```json
[
  {
    "id": 1,
    "name": "우주",
    "description": "우주 느낌의 몽환적인 방탈출!",
    "thumbnailUrl": "https://example.com/theme1.jpg"
  },
  {
    "id": 2,
    "name": "지하실",
    "description": "스산한 지하실에서 탈출해보세요!",
    "thumbnailUrl": "https://example.com/theme2.jpg"
  }
]
```

설계 결정

- 테마 생성과 삭제는 관리자만 수행하는 관리 기능이므로 `/admin/themes` 하위로 분리한다.
- 실제 관리자 접근 제한은 이후 인증과 인가를 도입할 때 처리한다.
- 관리자와 사용자 API는 구분한다. (`/admin/themes`, `/themes`).
    1. 관리자는 관리의 목적으로 사용하며, 사용자는 사용의 목적이기 때문에 사용 목적이 다르다.
    2. 관리자에게 보여줘야 하는 응답과 사용자에게 보여줘야 하는 응답이 달라질 가능성이 크기 때문에 분리한다.
    3. 관리 측면(문서화, 로그)에서 비용이 증가할 수 있다.
- 관리자가 테마를 삭제하는 경우, 존재하는 데이터를 삭제하든 존재하지 않는 데이터를 삭제하든 결국 존재하지 않는 데이터이며, 통일성을 확보하기 위해 204 No Content로 통일한다.
- `/themes/popular`가 되는 이유: 인기가 많은 방탈출의 의미를 가져야 하기 때문.
- 이용 가능 시간 조회의 `date` 쿼리를 옵셔널로 둔다. 생략 시 오늘로 해석되어 첫 진입 경험이 자연스럽다.
