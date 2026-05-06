# Theme API 명세

### 테마 목록 조회

```http
GET admin/themes
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

Response `201 Created`

```json
{
  "id": 1,
  "name": "우주",
  "description": "우주 느낌의 몽환적인 방탈출!",
  "thumbnailUrl": "https://example.com/theme1.jpg"
}
```

### 테마 삭제

```http
DELETE /admin/themes/{themeId}
```

Response `204 No Content`

### 이용 가능 시간 조회

```http
GET /themes/{themeId}/times?date=2024-01-01
```

Response `200 OK`

```json
[
  {
    "time_id": 1,
    "start_at": "09:00"
  },
  {
    "time_id": 2,
    "start_at": "10:00"
  }
]
```

설계 결정

- 테마 생성과 삭제는 관리자만 수행하는 관리 기능이므로 `/admin/themes` 하위로 분리한다.
- 실제 관리자 접근 제한은 이후 인증과 인가를 도입할 때 처리한다.
- 관리자와 사용자 API는 구분한다. (`/admin/themes`, `/themes`).
    1. 관리자는 관리의 목적으로 사용하며, 사용자는 관리의 목적이기 때문에 사용의 목적이 다르다.(관리자가 관할하는 기능과 사용자가 관할하는 기능은 명확히 다르기 때문이다.)
    2. 관리자에게 보여줘야 하는 응답과 사용자에게 보여줘야 하는 응답이 달라질 가능성이 크기 때문에 분리를 하는 것이 좋다고 판단했다.
    3. 관리 측면(문서화, 로그)에서 비용이 증가할 수 있다.
- 관리자가 테마를 삭제하는 경우, 존재하는 데이터를 삭제하든, 존재하지 않는 데이터를 삭제하든 결국에는 존재하지 않는 데이터이며, 통일성을 확보하기 위해서 204 No Content로 통일한다. 
