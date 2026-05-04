# API

## ADMIN
### 예약 추가 - POST /admin/reservations

**요청**
- Method: `POST`
- URL: `/admin/reservations`
- Body:
```json
{
  "name": "브라운",
  "date": "2026-05-04",
  "time_id": 1,
  "theme_id": 1
}
```

**응답**
- Status: `201 Created`
- Header: Location: admin/reservations/{id}
- Body:
```json
{
  "id": 1,
  "name": "브라운",
  "date": "2026-05-04",
  "time": {
    "id" : 1,
    "start_at" : "10:00"
  },
  "theme": {
    "name" : "방탈출 이름",
    "description" : "설명",
    "thumbnail_url" : "url"
  }
}
```

**예외**
- 400: 필수 값 누락 / 잘못된 날짜 형식
- 404: 존재하지 않는 time_id, theme_id
- 409: 동일 날짜,시간,테마 중복 예약
- 403: 관리자가 아니면 [로그인 기능 추가시]
- 401: 로그인을 하지 않았을 경우 [ 로그인 기능 추가시 ]


### 예약 삭제 - DELETE /admin/reservations/{id}

**요청**
- Method: `DELETE`
- URL: `/admin/reservations/{id}`

**응답**
- Status: `204  No Content`
- Body:

**예외**
- 400: 필수 값 누락
- 404: 존재하지 않는 id
- 403: 관리자가 아니면 [로그인 기능 추가시]
- 401: 로그인을 하지 않았을 경우 [ 로그인 기능 추가시 ]

### 단일 예약 조회 - GET /admin/reservations/{id}

**요청**
- Method: `GET`
- URL: `/admin/reservations/{id}`

**응답**
- Status: `200  OK`
- Body:
```json
  {
  "id": 1,
  "name": "브라운",
  "date": "2026-05-04",
  "time": {
    "id": 1,
    "startAt": "10:00"
  },
  "theme": {
    "name": "방탈출 이름",
    "description": "설명",
    "thumbnailUrl": "url"
  }
}
```

**예외**
- 400: id 입력되지 않았을 때
- 404: id 존재하지 않을 때
- 403: 관리자가 아니면 [로그인 기능 추가시]
- 401: 로그인을 하지 않았을 경우 [ 로그인 기능 추가시 ]

### 예약 목록 조회 - GET /admin/reservations

**요청**
- Method: `GET`
- URL: `/admin/reservations`

**응답**
- Status: `200  OK`
- Body:
```json
[
  {
    "id": 1,
    "name": "브라운",
    "date": "2026-05-04",
    "time": {
      "id": 1,
      "startAt": "10:00"
    },
    "theme": {
      "name": "방탈출 이름",
      "description": "설명",
      "thumbnailUrl": "url"
    }
  },
  {
    "id": 2,
    "name": "...",
    ...
  }
]
```

**예외**
- 403: 관리자가 아니면 [로그인 기능 추가시]
- 401: 로그인을 하지 않았을 경우 [ 로그인 기능 추가시 ]


-----------------------------------

### 시간 추가 - POST /admin/times

**요청**
- Method: `POST`
- URL: `/admin/times`
- Body:
```json
{
  "startAt": "10:00"
}
```

**응답**
- Status: `201 Created`
- Headers: `Location: /admin/times/{id}`
- Body:
```json
{
  "id": 1,
  "startAt": "10:00"
}
```

**예외**
- 400: 필수 값(`startAt`) 누락
- 400: 잘못된 시간 형식 (예: `"25:00"`, `"abc"`)
- 409: 동일한 시간 중복 등록
- 403: 관리자가 아니면 [로그인 기능 추가시]
- 401: 로그인을 하지 않았을 경우 [로그인 기능 추가시]


### 시간 삭제 - DELETE /admin/times/{id}

**요청**
- Method: `DELETE`
- URL: `/admin/times/{id}`

**응답**
- Status: `204 No Content`
- Body:

**예외**
- 400: 잘못된 형식의 id (숫자가 아닌 값)
- 404: 존재하지 않는 id
- 409: 해당 시간을 사용 중인 예약이 존재 (참조 무결성 위반)
- 403: 관리자가 아니면 [로그인 기능 추가시]
- 401: 로그인을 하지 않았을 경우 [로그인 기능 추가시]

### 단일 시간 조회 - GET /admin/times/{id}

**요청**
- Method: `GET`
- URL: `/admin/times/{id}`

**응답**
- Status: `200 OK`
- Body:
```json

{
  "id": 1,
  "startAt": "10:00"
}
```

**예외**
- 400: id 값 누락
- 404: id값 존재하지 않을 때.
- 403: 관리자가 아니면 [로그인 기능 추가시]
- 401: 로그인을 하지 않았을 경우 [로그인 기능 추가시]


### 시간 목록 조회 - GET /admin/times

**요청**
- Method: `GET`
- URL: `/admin/times`

**응답**
- Status: `200 OK`
- Body:
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

**예외**
- 403: 관리자가 아니면 [로그인 기능 추가시]
- 401: 로그인을 하지 않았을 경우 [로그인 기능 추가시]

--------------------------------------------------
## Themes

### 테마 추가 - POST /admin/themes

**요청**
- Method: `POST`
- URL: `/admin/themes`
- Body:
```json
{
  "name": "이름",
  "description": "설명",
  "thumbnail_url": "url"
}
```

**응답**
- Status: `201 Created`
- Headers: `Location: /admin/themes/{id}`
- Body:
```json
{
  "id": 1,
  "name": "이름",
  "description": "설명",
  "thumbnail_url": "url"
}
```

**예외**
- 400: 필수 값 누락
- 400: 잘못된 URL 형식
- 409: 동일한 이름 테마 중복 등록
- 403: 관리자가 아니면 [로그인 기능 추가시]
- 401: 로그인을 하지 않았을 경우 [로그인 기능 추가시]


### 테마 삭제 - DELETE /admin/themes/{id}

**요청**
- Method: `DELETE`
- URL: `/admin/themes/{id}`

**응답**
- Status: `204 No Content`
- Body:

**예외**
- 400: 잘못된 형식의 id (숫자가 아닌 값)
- 404: 존재하지 않는 id
- 409: 해당 시간을 사용 중인 예약이 존재 (참조 무결성 위반)
- 403: 관리자가 아니면 [로그인 기능 추가시]
- 401: 로그인을 하지 않았을 경우 [로그인 기능 추가시]

### 단일 테마 조회 - GET /admin/themes/{id}

**요청**
- Method: `GET`
- URL: `/admin/themes/{id}`

**응답**
- Status: `200 OK`
- Body:
```json
{
  "id": 1,
  "name": "이름",
  "description": "설명",
  "thumbnail_url": "url"
}
```

**예외**
- 400: id 누락됐을 때
- 404: id 없을 때
- 403: 관리자가 아니면 [로그인 기능 추가시]
- 401: 로그인을 하지 않았을 경우 [로그인 기능 추가시

### 테마 목록 조회 - GET /admin/themes

**요청**
- Method: `GET`
- URL: `/admin/themes`

**응답**
- Status: `200 OK`
- Body:
```json
[
  {
    "id": 1,
    "name": "이름",
    "description": "설명",
    "thumbnail_url": "url"
  },
  {
    "id": 2,
    "name": "이름",
    "description": "설명",
    "thumbnail_url": "url"
  }
]
```

**예외**
- 403: 관리자가 아니면 [로그인 기능 추가시]
- 401: 로그인을 하지 않았을 경우 [로그인 기능 추가시]
