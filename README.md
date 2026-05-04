# Roomescape API 명세서

## 관리자

### ☑️ 관리자 예약 조회

#### URL

```http
GET /api/admin/reservations
```

#### Query Parameters

없음

#### Request Body

없음

#### Response Body

##### 200 OK

```json
[
  {
    "id": 1,
    "name": "브라운",
    "date": "2026-05-04",
    "time": {
      "id": 2,
      "startAt": "12:00"
    },
    "theme": {
      "id": 1,
      "name": "피온피온",
      "description": "설명",
      "imageUrl": "https://roomescape.com/images/themes/ring-banner.png"
    }
  },
  {
    "id": 2,
    "name": "네오",
    "date": "2026-05-06",
    "time": {
      "id": 2,
      "startAt": "12:00"
    },
    "theme": {
      "id": 1,
      "name": "피온피온",
      "description": "설명",
      "imageUrl": "https://roomescape.com/images/themes/prison-banner.png"
    }
  }
]
```

### ☑️ 관리자 예약 추가

#### URL

```http
POST /api/admin/reservations
```

#### Query Parameters

없음

#### Request Body

```json
{
  "name": "서여",
  "date": "2026-05-04",
  "timeId": 1,
  "themeId": 1
}
```

#### Response Body

##### 201 Created

```json
{
  "id": 1,
  "name": "서여",
  "date": "2026-05-04",
  "timeId": 1,
  "themeId": 1
}
```

### ☑️ 관리자 예약 삭제

#### URL

```http
DELETE /api/admin/reservations/{id}
```

#### Path Variables

| 파라미터명 | 필수 여부 | 타입     | 설명        | 예시 |
|-------|-------|--------|-----------|----|
| id    | 필수    | Number | 삭제할 예약 ID | 1  |

#### Query Parameters

없음

#### Request Body

없음

#### Response Body

##### 200 OK

없음

### ☑️ 관리자 시간 조회

#### URL

```http
GET /api/admin/times
```

#### Query Parameters

없음

#### Request Body

없음

#### Response Body

##### 200 OK

```json
[
  {
    "id": 1,
    "startAt": "10:00"
  },
  {
    "id": 2,
    "startAt": "11:00"
  }
]
```

### ☑️ 관리자 시간 추가

#### URL

```http
POST /api/admin/times
```

#### Query Parameters

없음

#### Request Body

```json
{
  "startAt": "20:30"
}
```

#### Response Body

##### 201 Created

```json
{
  "id": 1,
  "startAt": "20:30"
}
```

### ☑️ 관리자 시간 삭제

#### URL

```http
DELETE /api/admin/times/{id}
```

#### Path Variables

| 파라미터명 | 필수 여부 | 타입     | 설명        | 예시 |
|-------|-------|--------|-----------|----|
| id    | 필수    | Number | 삭제할 시간 ID | 1  |

#### Query Parameters

없음

#### Request Body

없음

#### Response Body

##### 200 OK

없음

### ✅관리자 테마 추가

#### URL

```http
POST /api/admin/themes
```

#### Query Parameters

없음

#### Request Body

```json
{
  "name": "브라운",
  "description": "테마 설명",
  "imageUrl": "https://roomescape.com/images/themes/prison-room.png"
}
```

#### Response Body

##### 201 Created

```json
{
  "id": 1,
  "name": "브라운",
  "description": "테마 설명",
  "imageUrl": "https://roomescape.com/images/themes/prison-room.png"
}
```

### ✅관리자 테마 삭제

#### URL

```http
DELETE /api/admin/themes/{id}
```

#### Path Variables

| 파라미터명 | 필수 여부 | 타입     | 설명        | 예시 |
|-------|-------|--------|-----------|----|
| id    | 필수    | Number | 삭제할 테마 ID | 1  |

#### Query Parameters

없음

#### Request Body

없음

#### Response Body

##### 200 OK

없음

## 사용자

추후 작성 예정
