# RoomEscape Member

## 도메인 개요

### 예약

* 고객은 테마, 날짜, 시간, 예약자 이름을 선택해 예약한다.
  * 예약은 과거 날짜는 불가능하다.
  * 예약은 특정 테마와 예약 시간 조합을 기준으로 생성된다.
* 이미 예약된 날짜 및 시간 정보를 변경 할 수 있다.
  * 이미 지난 예약에 대해서는 변경 할 수 없다.
  * 예약 변경은 과거 날짜는 불가능하다.
* 예약 정보를 취소 할 수 있다.
  * 이미 지난 예약에 대해서는 취소 할 수 없다.
* 사용자 별 예약 목록을 검색할 수 있다.

### 테마

* 테마는 이름, 설명, 썸네일 이미지 URL을 필수로 가진다.
* 관리자는 테마를 추가하거나 비활성화할 수 있다.
  * 비활성화된 테마는 사용자용 전체 테마 목록에서 제외된다.
  * 기존 예약 데이터는 유지되며 관리자 화면에서 조회 가능하다.

### 예약 시간

* 예약 시간은 `startAt`으로 관리한다.
* 관리자는 예약 시간을 추가하거나 비활성화할 수 있다.
  * 비활성화된 예약 시간은 신규 예약 가능 목록에서 제외된다.
  * 기존 예약과 연결된 예약 시간 정보는 유지되어 관리자 화면에서 조회 가능하다.
* 테마별 예약 가능 여부 조회 시 각 예약 시간의 예약 가능 상태를 함께 반환한다.

## API 공통 규칙

* Base URL: `/api`
* 관리자 API는 요청 헤더에 `role: ADMIN` 이 필요하다.
* 날짜는 `YYYY-MM-DD`, 시간은 `HH:mm:ss` 형식을 사용한다.
* 유효성 검증 실패, 중복 등록, 조회 실패 등의 예외는 에러 응답으로 반환된다.

## 비즈니스별 API 명세

### 1. 사용자 테마 조회

#### 전체 활성 테마 조회

**Request**

```http
GET /api/themes HTTP/1.1
```

**Response**

```json
200 OK

[
  {
    "id": 1,
    "name": "공포",
    "description": "공포 방탈출입니다.",
    "thumbnailImageUrl": "http://image.url/horror"
  },
  {
    "id": 2,
    "name": "추리",
    "description": "추리 방탈출입니다.",
    "thumbnailImageUrl": "http://image.url/detective"
  }
]
```

#### 인기 테마 조회

**Request**

```http
GET /api/themes/popular?startDate=2026-05-01&endDate=2026-05-06 HTTP/1.1
```

**Response**

```json
200 OK

[
  {
    "id": 1,
    "name": "공포",
    "description": "공포 방탈출입니다.",
    "thumbnailImageUrl": "http://image.url/horror"
  }
]
```

#### 특정 테마의 날짜별 예약 가능 시간 조회

**Request**

```http
GET /api/themes/1/times?date=2026-05-10 HTTP/1.1
```

**Response**

```json
200 OK

[
  {
    "id": 1,
    "startAt": "10:00:00",
    "isReservable": true
  },
  {
    "id": 2,
    "startAt": "12:00:00",
    "isReservable": false
  }
]
```

### 2. 사용자 예약

#### 예약 생성

**Request**

```http
POST /api/reservations HTTP/1.1
Content-Type: application/json

{
  "name": "lim",
  "date": "2026-05-10",
  "themeId": 1,
  "timeId": 2
}
```

**Response**

```json
201 Created
Location: /api/reservations/1

{
  "id": 1,
  "name": "lim",
  "date": "2026-05-10",
  "time": {
    "id": 2,
    "startAt": "12:00:00"
  }
}
```

### 예약 변경

```http
PATCH /api/reservations/{id} HTTP/1.1
Content-Type: application/json

{
  "date": "2026-05-11",
  "timeId": 1
}
```

**Response**

```json
200 OK

{
  "id": 1,
  "name": "lim",
  "date": "2026-05-11",
  "time": {
    "id": 1,
    "startAt": "11:00:00"
  }
}
```

### 예약 취소

```http
DELETE /api/reservations/{id} HTTP/1.1
```

**Response**

```json
204 NoContent
```

### 예약 상세 조회

```http
GET /api/reservations/{id} HTTP/1.1
```

**Response**

```json
200 OK

{
  "id": 1,
  "name": "lim",
  "date": "2026-05-11",
  "themeId": 1,
  "time": {
    "id": 1,
    "startAt": "11:00:00"
  }
}
```

### 사용자 별 예약 목록 조회

```http
GET /api/reservations?name={name}&page=<optional>&size=<optional> HTTP/1.1
```

**Query Parameters**

| Parameter | Type    | Default | Description |
|-----------|---------|---------|-------------|
| name      | String  | -       | 예약자 이름 |
| page      | Integer | 1       | 1 이하이거나 잘못된 형식일 경우 기본값 적용 |
| size      | Integer | 20      | 1 이하이거나 잘못된 형식일 경우 기본값 적용 (최대 100) |

- `page`, `size`가 누락되거나 잘못된 형식인 경우 기본값으로 보정됩니다.
- `size`가 최대 허용 범위를 초과하면 최대값으로 제한됩니다.

**Response**

```json
200 OK

{
  "totalPages": 10,
  "totalElements": 100,
  "content": [
    {
      "id": 1,
      "name": "lim",
      "date": "2026-05-11",
      "startAt": "11:00:00",
      "theme": "공포의 테마",
    },
    ...
  ],
}
```

### 3. 관리자 테마 관리

#### 테마 추가

**Request**

```http
POST /api/admin/themes HTTP/1.1
Content-Type: application/json
role: ADMIN

{
  "name": "공포",
  "description": "공포 방탈출입니다.",
  "thumbnailImageUrl": "http://image.url/horror"
}
```

**Response**

```json
201 Created

{
  "id": 1,
  "name": "공포",
  "description": "공포 방탈출입니다.",
  "thumbnailImageUrl": "http://image.url/horror"
}
```

#### 테마 활성화 및 비활성화

**Request**

```http
PATCH /api/admin/times/{id}/activate HTTP/1.1
PATCH /api/admin/times/{id}/deactivate HTTP/1.1
role: ADMIN
```

**Response**

```http
204 No Content
```

### 4. 관리자 예약 관리

#### 전체 예약 조회

**Request**

```http
GET /api/admin/reservations HTTP/1.1
role: ADMIN
```

**Response**

```json
200 OK

[
  {
    "id": 1,
    "name": "lim",
    "date": "2026-05-10",
    "time": {
      "id": 2,
      "startAt": "12:00:00"
    }
  }
]
```

#### 관리자 예약 생성

**Request**

```http
POST /api/admin/reservations HTTP/1.1
Content-Type: application/json
role: ADMIN

{
  "name": "lim",
  "date": "2026-05-10",
  "themeId": 1,
  "timeId": 2
}
```

**Response**

```json
201 Created

{
  "id": 1,
  "name": "lim",
  "date": "2026-05-10",
  "time": {
    "id": 2,
    "startAt": "12:00:00"
  }
}
```

#### 예약 삭제

**Request**

```http
DELETE /api/admin/reservations/1 HTTP/1.1
role: ADMIN
```

**Response**

```http
204 No Content
```

### 5. 관리자 예약 시간 관리

#### 예약 시간 전체 조회

**Request**

```http
GET /api/admin/times HTTP/1.1
role: ADMIN
```

**Response**

```json
200 OK

[
  {
    "id": 1,
    "startAt": "10:00:00"
  },
  {
    "id": 2,
    "startAt": "12:00:00"
  }
]
```

#### 예약 시간 추가

**Request**

```http
POST /api/admin/times HTTP/1.1
Content-Type: application/json
role: ADMIN

{
  "startAt": "10:00:00"
}
```

**Response**

```json
201 Created

{
  "id": 1,
  "startAt": "10:00:00"
}
```

#### 예약 시간 활성화 및 비활성화

**Request**

```http
PATCH /api/admin/times/1/activate HTTP/1.1
PATCH /api/admin/times/1/deactivate HTTP/1.1
role: ADMIN
```

**Response**

```http
204 No Content
```

## 페이지 경로

* 메인 페이지: `/`
* 예약 페이지: `/reserve`
* 검색 페이지: `/search`
* 관리자 페이지: `/admin`
