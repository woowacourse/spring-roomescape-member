# Theme API 명세

## 사용자/공통 테마 조회

### 테마 목록 조회

```http
GET /themes
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

## 관리자 테마 관리

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

설계 결정

- 테마 목록 조회는 관리자와 사용자 모두 같은 목록을 같은 목적으로 조회하므로 `/themes` 하나로 사용한다. (같은 행위, 같은 목적)
- 테마 생성과 삭제는 관리자만 수행하는 관리 기능이므로 `/admin/themes` 하위로 분리한다.
- 실제 관리자 접근 제한은 이후 인증과 인가를 도입할 때 처리한다.
