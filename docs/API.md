# 1. 예약

## 1-1. 예약 추가

| 항목           | 내용                 |
| ------------ | ------------------ |
| Method       | `POST`             |
| URL          | `/reservations`          |
| Description  | 새로운 예약을 생성합니다.     |
| Content-Type | `application/json` |

**Request Body**

| Field       | Type     | Required | Description |
| ----------- | -------- | -------: | ----------- |
| name        | `String` |        ✅ | 예약자 이름       |
| date | `LocalDate` |        ✅ | 예약일       |
| timeId   | `Long` |        ✅ | 시간 ID     |
| themeId   | `Long` |        ✅ | 테마 ID     |

**Response Schema**

| Field       | Type     | Nullable | Description |
| ----------- | -------- | -------: | ----------- |
| id          | `Long`   |        ❌ | 예약 ID       |
| name        | `String` |        ❌ | 예약자 이름       |
| date | `LocalDate` |        ❌ | 예약일       |
| time   | `ReservationTimeResponse` |        ❌ | 예약 시간     |
| theme   | `ThemeResponse` |        ❌ | 예약 테마     |

**요청 예시**

```http
POST /reservations
Content-Type: application/json

{
  "name": "홍길동",
  "date": "2026-05-14",
  "timeId": 1,
  "themeId": 1
}
```

**응답 예시**

- 정상 생성

```http
HTTP/1.1 201 CREATED
Content-Type: application/json

{
  "id": 6,
  "name": "홍길동",
  "date": "2026-05-14",
  "time": {
    "id": 1,
    "startAt": "10:00:00"
  },
  "theme": {
    "id": 1,
    "name": "사라진 기록보관소",
    "description": "폐관 직전의 오래된 도서관에서 사라진 원고와 마지막 열쇠를 추적하는 미스터리 테마입니다.",
    "thumbnail": "https://commons.wikimedia.org/wiki/Special:FilePath/Main%20%28Reading%20Room%29%20%283109281787%29.jpg?width=1200"
  }
}
```

- 요청한 테마 혹은 시간 없음

```http
HTTP/1.1 200 
Content-Type: text/plain

예약 시간을 찾을 수 없습니다. 예약 시간 ID를 확인해주세요.(혹은 테마를 찾을 수 없습니다. 테마 ID를 확인해주세요.)
```

- 요청 값 누락

```http
HTTP/1.1 400 
Content-Type: text/plain;charset=UTF-8

예약 시간이 비어있습니다. 시간을 선택해주세요.
```

- 중복 예약 시

```http
HTTP/1.1 409 
Content-Type: text/plain

동일한 날짜, 시간, 테마의 예약이 이미 존재합니다. 다른 조건을 선택해주세요.
```

- 과거 날짜, 시간으로 요청

```http
HTTP/1.1 422 
Content-Type: text/plain

지나간 날짜나 시간으로는 예약을 생성하거나 삭제할 수 없습니다. 현재 이후의 시간을 선택해주세요.
```

## 1-2. 예약 전체 조회

| 항목           | 내용                 |
| ------------ | ------------------ |
| Method       | `GET`             |
| URL          | `/reservations`          |
| Description  | 모든 예약을 조회합니다.     |

**Response Schema**

| Field       | Type     | Nullable | Description |
| ----------- | -------- | -------: | ----------- |
| - | `List` |        ❌ | 예약 목록 |
| id          | `Long`   |        ❌ | 예약 ID       |
| name        | `String` |        ❌ | 예약자 이름       |
| date | `LocalDate` |        ❌ | 예약일       |
| time   | `ReservationTimeResponse` |        ❌ | 예약 시간     |
| theme   | `ThemeResponse` |        ❌ | 예약 테마     |

**요청 예시**

```http
GET /reservations
```

**응답 예시**

- 정상 조회

```http
HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "id": 1,
    "name": "김민준",
    "date": "2026-05-13",
    "time": {
      "id": 1,
      "startAt": "10:00:00"
    },
    "theme": {
      "id": 1,
      "name": "사라진 기록보관소",
      "description": "폐관 직전의 오래된 도서관에서 사라진 원고와 마지막 열쇠를 추적하는 미스터리 테마입니다.",
      "thumbnail": "https://commons.wikimedia.org/wiki/Special:FilePath/Main%20%28Reading%20Room%29%20%283109281787%29.jpg?width=1200"
    }
  },
  {
    "id": 2,
    "name": "이서연",
    "date": "2026-05-13",
    "time": {
      "id": 2,
      "startAt": "11:30:00"
    },
    "theme": {
      "id": 2,
      "name": "13호실의 체크아웃",
      "description": "불 꺼진 호텔 복도와 잠긴 객실 사이에서 투숙객의 마지막 동선을 복원해야 합니다.",
      "thumbnail": "https://commons.wikimedia.org/wiki/Special:FilePath/Hotel%20Hallway.jpeg?width=1200"
    }
  }
]
```

## 1-3. 예약 조회 : 날짜, 테마 기반

| 항목           | 내용                 |
| ------------ | ------------------ |
| Method       | `GET`             |
| URL          | `/reservations?date={date}&themeId={themeId}`          |
| Description  | 특정 날짜와 테마를 기준으로 예약 가능한 시간대를 조회합니다.     |

**Query Parameters**

| Parameter | Type | Required | Description |
| --------- | ---- | -------- | ----------- |
| date | `LocalDate` | ✅ | 조회할 날짜 (YYYY-MM-DD) |
| themeId | `Long` | ✅ | 테마 ID |

**Response Schema**

| Field       | Type     | Nullable | Description |
| ----------- | -------- | -------: | ----------- |
| - | `List` |        ❌ | 예약 가능 정보 목록 |
| date | `String` |        ❌ | 예약일 |
| time   | `ReservationTimeResponse` |        ❌ | 예약 시간 |
| theme   | `ThemeResponse` |        ❌ | 예약 테마 |
| isAvailable | `boolean` | ❌ | 예약 가능 여부 (true: 가능, false: 이미 예약됨) |

**요청 예시**

```http
GET /reservations?date=2026-05-13&themeId=1
```

**응답 예시**

- 정상 조회

```http
HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "date": "2026-05-13",
    "time": {
      "id": 1,
      "startAt": "10:00:00"
    },
    "theme": {
      "id": 1,
      "name": "사라진 기록보관소",
      "description": "폐관 직전의 오래된 도서관에서 사라진 원고와 마지막 열쇠를 추적하는 미스터리 테마입니다.",
      "thumbnail": "https://commons.wikimedia.org/wiki/Special:FilePath/Main%20%28Reading%20Room%29%20%283109281787%29.jpg?width=1200"
    },
    "isAvailable": false
  },
  {
    "date": "2026-05-13",
    "time": {
      "id": 2,
      "startAt": "11:30:00"
    },
    "theme": {
      "id": 1,
      "name": "사라진 기록보관소",
      "description": "폐관 직전의 오래된 도서관에서 사라진 원고와 마지막 열쇠를 추적하는 미스터리 테마입니다.",
      "thumbnail": "https://commons.wikimedia.org/wiki/Special:FilePath/Main%20%28Reading%20Room%29%20%283109281787%29.jpg?width=1200"
    },
    "isAvailable": true
  }
]
```

- 존재하지 않는 테마 ID 요청 시

```http
HTTP/1.1 200 OK
Content-Type: text/plain

테마를 찾을 수 없습니다. 테마 ID를 확인해주세요.
```

## 1-4. 예약 조회 : 이름

| 항목           | 내용                 |
| ------------ | ------------------ |
| Method       | `GET`             |
| URL          | `/reservations?name={name}`          |
| Description  | 예약자 이름을 기준으로 예약을 조회합니다.     |

**Query Parameters**

| Parameter | Type | Required | Description |
| --------- | ---- | -------- | ----------- |
| name | `String` | ✅ | 예약자 이름 |

**Response Schema**

| Field       | Type     | Nullable | Description |
| ----------- | -------- | -------: | ----------- |
| - | `List` |        ❌ | 예약 목록 |
| id          | `Long`   |        ❌ | 예약 ID       |
| name        | `String` |        ❌ | 예약자 이름       |
| date | `LocalDate` |        ❌ | 예약일       |
| time   | `ReservationTimeResponse` |        ❌ | 예약 시간     |
| theme   | `ThemeResponse` |        ❌ | 예약 테마     |

**요청 예시**

```http
GET /reservations?name=김민준
```

**응답 예시**

- 정상 조회

```http
HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "id": 1,
    "name": "김민준",
    "date": "2026-05-13",
    "time": {
      "id": 1,
      "startAt": "10:00:00"
    },
    "theme": {
      "id": 1,
      "name": "사라진 기록보관소",
      "description": "폐관 직전의 오래된 도서관에서 사라진 원고와 마지막 열쇠를 추적하는 미스터리 테마입니다.",
      "thumbnail": "https://commons.wikimedia.org/wiki/Special:FilePath/Main%20%28Reading%20Room%29%20%283109281787%29.jpg?width=1200"
    }
  }
]
```

- 예약자 이름이 비어있는 경우

```http
HTTP/1.1 400 
Content-Type: text/plain;charset=UTF-8

예약자 이름이 비어있습니다. 이름을 입력해주세요.
```

## 1-5. 예약 수정

| 항목           | 내용                 |
| ------------ | ------------------ |
| Method       | `PATCH`             |
| URL          | `/reservations/{id}?name={name}`          |
| Description  | 본인의 예약 정보를 수정합니다. (날짜, 시간 수정 가능)     |
| Content-Type | `application/json` |

**Path Parameters**

| Parameter | Type | Description |
| --------- | ---- | ----------- |
| id | `Long` | 수정할 예약 ID |

**Query Parameters**

| Parameter | Type | Required | Description |
| --------- | ---- | -------- | ----------- |
| name | `String` | ✅ | 예약자 이름 (본인 확인용) |

**Request Body**

| Field       | Type     | Required | Description |
| ----------- | -------- | -------: | ----------- |
| date        | `LocalDate` |        ❌ | 변경할 예약일       |
| timeId   | `Long` |        ❌ | 변경할 시간 ID     |

**Response Schema**

| Field       | Type     | Nullable | Description |
| ----------- | -------- | -------: | ----------- |
| id          | `Long`   |        ❌ | 예약 ID       |
| name        | `String` |        ❌ | 예약자 이름       |
| date | `LocalDate` |        ❌ | 예약일       |
| time   | `ReservationTimeResponse` |        ❌ | 예약 시간     |
| theme   | `ThemeResponse` |        ❌ | 예약 테마     |

**요청 예시**

```http
PATCH /reservations/1?name=김민준
Content-Type: application/json

{
  "date": "2026-05-15",
  "timeId": 2
}
```

**응답 예시**

- 정상 수정

```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "id": 1,
  "name": "김민준",
  "date": "2026-05-15",
  "time": {
    "id": 2,
    "startAt": "11:30:00"
  },
  "theme": {
    "id": 1,
    "name": "사라진 기록보관소",
    "description": "폐관 직전의 오래된 도서관에서 사라진 원고와 마지막 열쇠를 추적하는 미스터리 테마입니다.",
    "thumbnail": "https://commons.wikimedia.org/wiki/Special:FilePath/Main%20%28Reading%20Room%29%20%283109281787%29.jpg?width=1200"
  }
}
```

- 본인의 예약이 아닌 경우 (이름 불일치) 혹은 예약을 찾을 수 없는 경우

```http
HTTP/1.1 200 OK
Content-Type: text/plain;charset=UTF-8

예약을 찾을 수 없습니다. 예약 ID를 확인해주세요. (혹은 예약을 찾을 수 없습니다. 예약자 성함을 확인해주세요.)
```

- 수정하려는 예약 정보가 이미 존재하는 경우 (중복 예약)

```http
HTTP/1.1 409 Conflict
Content-Type: text/plain

동일한 날짜, 시간, 테마의 예약이 이미 존재합니다. 다른 조건을 선택해주세요.
```

## 1-6. 예약 삭제 : 본인

| 항목           | 내용                 |
| ------------ | ------------------ |
| Method       | `DELETE`             |
| URL          | `/reservations/{id}?name={name}`          |
| Description  | 본인의 예약을 삭제합니다. (과거 예약 삭제 불가)     |

**Path Parameters**

| Parameter | Type | Description |
| --------- | ---- | ----------- |
| id | `Long` | 삭제할 예약 ID |

**Query Parameters**

| Parameter | Type | Required | Description |
| --------- | ---- | -------- | ----------- |
| name | `String` | ✅ | 예약자 이름 (본인 확인용) |

**Response Schema**

| Field       | Type     | Nullable | Description |
| ----------- | -------- | -------: | ----------- |
| (없음)      | -        | -        | `204 No Content` 반환 |

**요청 예시**

```http
DELETE /reservations/1?name=김민준
```

**응답 예시**

- 정상 삭제

```http
HTTP/1.1 204 No Content
```

- 본인의 예약이 아닌 경우 (이름 불일치) 혹은 예약을 찾을 수 없는 경우

```http
HTTP/1.1 200 OK
Content-Type: text/plain;charset=UTF-8

예약을 찾을 수 없습니다. 예약 ID를 확인해주세요. (혹은 예약을 찾을 수 없습니다. 예약자 성함을 확인해주세요.)
```

- 지나간 예약 삭제 시도 시

```http
HTTP/1.1 422 Unprocessable Entity
Content-Type: text/plain

지나간 날짜나 시간으로는 예약을 생성하거나 삭제할 수 없습니다. 현재 이후의 시간을 선택해주세요.
```

## 1-7. 예약 삭제 : 관리자 전용

| 항목           | 내용                 |
| ------------ | ------------------ |
| Method       | `DELETE`             |
| URL          | `/reservations/{id}`          |
| Description  | 관리자 권한으로 예약을 삭제합니다. (과거 예약 삭제 가능)     |
| Authorization | `ADMIN` (Header: `Authorization`) |

**Path Parameters**

| Parameter | Type | Description |
| --------- | ---- | ----------- |
| id | `Long` | 삭제할 예약 ID |

**Response Schema**

| Field       | Type     | Nullable | Description |
| ----------- | -------- | -------: | ----------- |
| (없음)      | -        | -        | `204 No Content` 반환 |

**요청 예시**

```http
DELETE /reservations/1
Authorization: ADMIN
```

**응답 예시**

- 정상 삭제

```http
HTTP/1.1 204 No Content
```

- 관리자 권한이 없는 경우 (Header 누락 혹은 오타)

```http
HTTP/1.1 403 Forbidden
Content-Type: text/plain;charset=UTF-8

관리자 권한이 필요합니다.
```

- 예약을 찾을 수 없는 경우

```http
HTTP/1.1 200 OK
Content-Type: text/plain;charset=UTF-8

예약을 찾을 수 없습니다. 예약 ID를 확인해주세요.
```

# 2. 시간

## 2-1. 시간 추가

| 항목           | 내용                 |
| ------------ | ------------------ |
| Method       | `POST`             |
| URL          | `/times`          |
| Description  | 새로운 예약 시간을 생성합니다.     |
| Content-Type | `application/json` |

**Request Body**

| Field       | Type     | Required | Description |
| ----------- | -------- | -------: | ----------- |
| startAt     | `LocalTime` |        ✅ | 시작 시간 (HH:mm:ss) |

**Response Schema**

| Field       | Type     | Nullable | Description |
| ----------- | -------- | -------: | ----------- |
| id          | `Long`   |        ❌ | 시간 ID       |
| startAt     | `LocalTime` |        ❌ | 시작 시간     |

**요청 예시**

```http
POST /times
Content-Type: application/json

{
  "startAt": "14:00:00"
}
```

**응답 예시**

- 정상 생성

```http
HTTP/1.1 201 CREATED
Content-Type: application/json

{
  "id": 7,
  "startAt": "14:00:00"
}
```

- 요청 값(시간) 누락 시

```http
HTTP/1.1 400 Bad Request
Content-Type: text/plain;charset=UTF-8

예약 시간이 비어있습니다. 시간을 선택해주세요.
```

## 2-2. 시간 전체 조회

| 항목           | 내용                 |
| ------------ | ------------------ |
| Method       | `GET`             |
| URL          | `/times`          |
| Description  | 모든 예약 시간 목록을 조회합니다.     |

**Response Schema**

| Field       | Type     | Nullable | Description |
| ----------- | -------- | -------: | ----------- |
| - | `List` |        ❌ | 시간 목록 |
| id          | `Long`   |        ❌ | 시간 ID       |
| startAt     | `LocalTime` |        ❌ | 시작 시간     |

**요청 예시**

```http
GET /times
```

**응답 예시**

- 정상 조회

```http
HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "id": 1,
    "startAt": "10:00:00"
  },
  {
    "id": 2,
    "startAt": "11:30:00"
  }
]
```

## 2-3. 시간 삭제

| 항목           | 내용                 |
| ------------ | ------------------ |
| Method       | `DELETE`             |
| URL          | `/times/{id}`          |
| Description  | 특정 예약 시간을 삭제합니다. (사용 중인 시간은 삭제 불가)     |

**Path Parameters**

| Parameter | Type | Description |
| --------- | ---- | ----------- |
| id | `Long` | 삭제할 시간 ID |

**Response Schema**

| Field       | Type     | Nullable | Description |
| ----------- | -------- | -------: | ----------- |
| (없음)      | -        | -        | `204 No Content` 반환 |

**요청 예시**

```http
DELETE /times/1
```

**응답 예시**

- 정상 삭제

```http
HTTP/1.1 204 No Content
```

- 사용 중인 시간을 삭제하려는 경우

```http
HTTP/1.1 409 Conflict
Content-Type: text/plain

사용 중인 예약 시간은 삭제할 수 없습니다. 관련 예약을 먼저 삭제해주세요.
```

- 존재하지 않는 시간 ID를 삭제하려는 경우

```http
HTTP/1.1 200 OK
Content-Type: text/plain;charset=UTF-8

예약 시간을 찾을 수 없습니다. 예약 시간 ID를 확인해주세요.
```

# 3. 테마

## 3-1. 테마 추가 : 관리자 전용

| 항목           | 내용                 |
| ------------ | ------------------ |
| Method       | `POST`             |
| URL          | `/themes`          |
| Description  | 새로운 테마를 생성합니다.     |
| Authorization | `ADMIN` (Header: `Authorization`) |
| Content-Type | `application/json` |

**Request Body**

| Field       | Type     | Required | Description |
| ----------- | -------- | -------: | ----------- |
| name        | `String` |        ✅ | 테마 이름       |
| description | `String` |        ✅ | 테마 설명       |
| thumbnail   | `String` |        ✅ | 썸네일 URL     |

**Response Schema**

| Field       | Type     | Nullable | Description |
| ----------- | -------- | -------: | ----------- |
| id          | `Long`   |        ❌ | 테마 ID       |
| name        | `String` |        ❌ | 테마 이름       |
| description | `String` |        ❌ | 테마 설명       |
| thumbnail   | `String` |        ❌ | 썸네일 URL     |

**요청 예시**

```http
POST /themes
Authorization: ADMIN
Content-Type: application/json

{
  "name": "레전드 탈출",
  "description": "전설적인 탈출 전문가가 남긴 마지막 수수께끼를 풀어보세요.",
  "thumbnail": "https://example.com/legend.jpg"
}
```

**응답 예시**

- 정상 생성

```http
HTTP/1.1 201 CREATED
Content-Type: application/json

{
  "id": 6,
  "name": "레전드 탈출",
  "description": "전설적인 탈출 전문가가 남긴 마지막 수수께끼를 풀어보세요.",
  "thumbnail": "https://example.com/legend.jpg"
}
```

- 관리자 권한이 없는 경우

```http
HTTP/1.1 403 Forbidden
Content-Type: text/plain;charset=UTF-8

관리자 권한이 필요합니다.
```

- 필수 값(이름, 설명, 썸네일) 누락 시

```http
HTTP/1.1 400 Bad Request
Content-Type: text/plain;charset=UTF-8

테마 이름이 비어있습니다. 이름을 입력해주세요. (혹은 설명/썸네일 비어있음 메시지)
```

## 3-2. 테마 전체 조회

| 항목           | 내용                 |
| ------------ | ------------------ |
| Method       | `GET`             |
| URL          | `/themes`          |
| Description  | 모든 테마 목록을 조회합니다.     |

**Response Schema**

| Field       | Type     | Nullable | Description |
| ----------- | -------- | -------: | ----------- |
| - | `List` |        ❌ | 테마 목록 |
| id          | `Long`   |        ❌ | 테마 ID       |
| name        | `String` |        ❌ | 테마 이름       |
| description | `String` |        ❌ | 테마 설명       |
| thumbnail   | `String` |        ❌ | 썸네일 URL     |

**요청 예시**

```http
GET /themes
```

**응답 예시**

- 정상 조회

```http
HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "id": 1,
    "name": "사라진 기록보관소",
    "description": "폐관 직전의 오래된 도서관에서 사라진 원고와 마지막 열쇠를 추적하는 미스터리 테마입니다.",
    "thumbnail": "https://example.com/archive.jpg"
  }
]
```

## 3-3. 테마 일부 조회 - 특정 기간, 정렬 조건, 조회 개수

| 항목           | 내용                 |
| ------------ | ------------------ |
| Method       | `GET`             |
| URL          | `/themes?sortType={sortType}&from={from}&to={to}&limit={limit}`          |
| Description  | 특정 기간 동안의 예약 데이터를 기준으로 인기 테마 등을 조회합니다.     |

**Query Parameters**

| Parameter | Type | Required | Description |
| --------- | ---- | -------- | ----------- |
| sortType  | `String` | ✅ | 정렬 조건 (예: `popularity`) |
| from      | `LocalDate` | ✅ | 조회 시작일 (YYYY-MM-DD) |
| to        | `LocalDate` | ✅ | 조회 종료일 (YYYY-MM-DD) |
| limit     | `Long` | ❌ | 조회 개수 (기본값: 10) |

**Response Schema**

| Field       | Type     | Nullable | Description |
| ----------- | -------- | -------: | ----------- |
| - | `List` |        ❌ | 테마 목록 |
| id          | `Long`   |        ❌ | 테마 ID       |
| name        | `String` |        ❌ | 테마 이름       |
| description | `String` |        ❌ | 테마 설명       |
| thumbnail   | `String` |        ❌ | 썸네일 URL     |

**요청 예시**

```http
GET /themes?sortType=popularity&from=2026-05-01&to=2026-05-31&limit=5
```

**응답 예시**

- 정상 조회

```http
HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "id": 1,
    "name": "사라진 기록보관소",
    "description": "...",
    "thumbnail": "..."
  }
]
```

## 3-4. 테마 삭제 : 관리자 전용

| 항목           | 내용                 |
| ------------ | ------------------ |
| Method       | `DELETE`             |
| URL          | `/themes/{id}`          |
| Description  | 특정 테마를 삭제합니다. (사용 중인 테마는 삭제 불가)     |
| Authorization | `ADMIN` (Header: `Authorization`) |

**Path Parameters**

| Parameter | Type | Description |
| --------- | ---- | ----------- |
| id | `Long` | 삭제할 테마 ID |

**Response Schema**

| Field       | Type     | Nullable | Description |
| ----------- | -------- | -------: | ----------- |
| (없음)      | -        | -        | `204 No Content` 반환 |

**요청 예시**

```http
DELETE /themes/1
Authorization: ADMIN
```

**응답 예시**

- 정상 삭제

```http
HTTP/1.1 204 No Content
```

- 관리자 권한이 없는 경우

```http
HTTP/1.1 403 Forbidden
Content-Type: text/plain;charset=UTF-8

관리자 권한이 필요합니다.
```

- 사용 중인 테마를 삭제하려는 경우

```http
HTTP/1.1 409 Conflict
Content-Type: text/plain

사용 중인 테마는 삭제할 수 없습니다. 관련 예약을 먼저 삭제해주세요.
```

- 존재하지 않는 테마 ID를 삭제하려는 경우

```http
HTTP/1.1 200 OK
Content-Type: text/plain;charset=UTF-8

테마를 찾을 수 없습니다. 테마 ID를 확인해주세요.
```
