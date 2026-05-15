# 구현 기능 목록

## 1단계 - 테마 도메인 추가

- 방탈출 게임에 '테마' 정보를 추가한다.
- 테마는 이름, 설명, 썸네일 이미지 URL을 가진다.
- 모든 테마의 시작 시간과 소요 시간은 동일하다고 가정한다.
    - [x] 테마 테이블을 만든다.
    - [x] 테마 도메인 클래스를 만든다.
    - [x] 관련 DTO 클래스를 만든다.
- 예약에 테마 정보를 포함하도록 기존 코드를 변경한다.
    - [x] 예약 테이블과 도메인 클래스에 테마ID 속성을 추가한다.
- [x] 사용자의 테마 조회 API를 구현한다.
- 관리자가 테마를 추가·삭제할 수 있다.
    - [x] 관리자의 테마 추가 API를 구현한다.
    - [x] 관리자의 테마 삭제 API를 구현한다.

## 2단계 - 사용자 예약

- 사용자가 날짜와 테마를 선택하면 예약 가능한 시간 목록이 표시된다.
- 예약 가능한 시간이란, 관리자가 등록한 시간 중 해당 날짜+테마에 아직 예약이 없는 시간이다.
    - [x] 전체 예약에서 지정된 날짜와 테마에 해당하는 예약들의 시간을 조회한다.
    - [x] 전체 시간 목록에서 조회된 시간들을 제외한다.

- 테마와 날짜를 쿼리 파라미터로 사용
    - 같은 날짜와 시간이라도 테마가 다르면 예약이 가능해야 하므로, 테마 ID를 조건으로 포함하여 조회하도록 설계했습니다.

- 사용자가 예약 가능한 시간을 선택하여 본인의 이름으로 예약한다.
    - 이전 미션에서 구현한 예약 생성 API를 사용한다.
- 같은 날짜·시간이라도 테마가 다르면 각각 예약 가능하다.
    - [x] 같은 날짜, 시간, 테마인 예약이 이미 존재하면 예외를 발생시킨다.

## 3단계 - 인기 테마 조회

- 최근 1주 동안 예약이 많았던 테마 상위 10개를 조회한다.
- 예: 오늘이 5월 8일이면, 게임 날짜가 5월 1일~5월 7일인 예약을 집계해 인기 순서대로 10개를 응답한다.
- [x] 전체 테마 목록을 가져오면서, 예약 정보 테이블에서 각 테마당 예약 횟수의 총합도 가져온다.

---

## API 설계 결정 사항

### 테마 API 분리 (`/themes` vs `/admin/themes`)

관리자만 수행할 수 있는 기능이므로 명확한 역할 분리를 위해 엔드포인트를 나누었습니다.  
추후 '테마 비활성화' 기능 도입 시, 관리자는 모든 테마를 봐야 하지만 사용자는 활성 테마만 봐야 합니다.  
필터링으로 처리할 경우 클라이언트 코드 노출 등의 위험이 있어 서버 레벨에서 미리 분리했습니다.

### 예약 가능 시간 조회 (`/times/available`)

단순히 `/times`에 파라미터를 추가할 경우, 전체 시간 목록을 반환하는 기존 응답과 필터링된 예약 가능 목록을 반환하는 응답의 형식이 달라져 API의 일관성을 해칩니다.  
리소스의 성격을 명확히 구분하고 클라이언트의 혼선을 방지하기 위해 `/available` 서브 패스를 추가했습니다.

### 예약 생성 API (`/reservations`)

사용자 예약과 관리자 예약을 같은 엔드포인트로 처리합니다.  
현 단계에서는 관리자 예약에 대한 특별한 요구사항이 없었기 때문에 분리하지 않았습니다.

### 인기 테마 조회 (`/themes/ranking?start-date=...&end-date=...`)

상대적 표현(week)은 기준 시점이 모호하지만, 시작·끝 날짜를 명시하면 의미가 명확합니다.  
특정 단위로 고정하지 않고 날짜 범위를 받아 다양한 기간 요구사항에 유연하게 대응합니다.

---

## API 명세

> **공통 사항**
> - 모든 요청/응답 본문의 Content-Type은 `application/json`입니다.
> - 날짜 형식: `yyyy-MM-dd` (예: `2026-05-15`)
> - 시간 형식: `HH:mm:ss` (예: `10:00:00`)

---

### 테마

#### `GET /themes` — 테마 전체 조회

**Response** `200 OK`

```json
[
  {
    "id": 1,
    "name": "피즈의 모험",
    "description": "피즈가 모험을 떠나는 이야기입니다.",
    "thumbnailUrl": "http://localhost:8080/images/fizz.jpg"
  },
  {
    "id": 2,
    "name": "나무의 일대기",
    "description": "나무가 살아온 인생을 보여주는 이야기입니다.",
    "thumbnailUrl": "http://localhost:8080/images/tree.jpg"
  }
]
```

---

#### `GET /themes/ranking?start-date={date}&end-date={date}` — 인기 테마 조회 (상위 10개)

| 파라미터         | 위치    | 타입           | 필수 | 설명     |
|--------------|-------|--------------|----|--------|
| `start-date` | Query | `yyyy-MM-dd` | Y  | 집계 시작일 |
| `end-date`   | Query | `yyyy-MM-dd` | Y  | 집계 종료일 |

**Response** `200 OK`

```json
[
  {
    "id": 1,
    "name": "피즈의 모험",
    "description": "피즈가 모험을 떠나는 이야기입니다.",
    "thumbnailUrl": "http://localhost:8080/images/fizz.jpg"
  }
]
```

---

#### `POST /admin/themes` — 테마 추가

**Request Body**

```json
{
  "name": "피즈의 모험",
  "description": "피즈가 모험을 떠나는 이야기입니다.",
  "thumbnailUrl": "http://localhost:8080/images/fizz.jpg"
}
```

**Response** `201 Created`

```json
{
  "id": 1,
  "name": "피즈의 모험",
  "description": "피즈가 모험을 떠나는 이야기입니다.",
  "thumbnailUrl": "http://localhost:8080/images/fizz.jpg"
}
```

---

#### `DELETE /admin/themes/{id}` — 테마 삭제

| 파라미터 | 위치   | 타입     | 필수 | 설명    |
|------|------|--------|----|-------|
| `id` | Path | `Long` | Y  | 테마 ID |

**Response** `204 No Content`

**Error**

| 상황            | 상태 코드           |
|---------------|-----------------|
| 존재하지 않는 테마 ID | `404 Not Found` |

---

### 예약 시간

#### `GET /admin/times` — 예약 시간 전체 조회

**Response** `200 OK`

```json
[
  {
    "id": 1,
    "startAt": "10:00:00"
  },
  {
    "id": 2,
    "startAt": "14:00:00"
  }
]
```

---

#### `GET /times/available?date={date}&themeId={id}` — 예약 가능 시간 조회

| 파라미터      | 위치    | 타입           | 필수 | 설명     |
|-----------|-------|--------------|----|--------|
| `date`    | Query | `yyyy-MM-dd` | Y  | 조회할 날짜 |
| `themeId` | Query | `Long`       | Y  | 테마 ID  |

**Response** `200 OK`

```json
[
  {
    "time": {
      "id": 1,
      "startAt": "10:00:00"
    },
    "available": true
  },
  {
    "time": {
      "id": 2,
      "startAt": "14:00:00"
    },
    "available": false
  }
]
```

---

#### `POST /admin/times` — 예약 시간 추가

**Request Body**

```json
{
  "startAt": "10:00:00"
}
```

**Response** `201 Created`

```json
{
  "id": 1,
  "startAt": "10:00:00"
}
```

---

#### `DELETE /admin/times/{id}` — 예약 시간 삭제

| 파라미터 | 위치   | 타입     | 필수 | 설명       |
|------|------|--------|----|----------|
| `id` | Path | `Long` | Y  | 예약 시간 ID |

**Response** `204 No Content`

**Error**

| 상황                 | 상태 코드           |
|--------------------|-----------------|
| 존재하지 않는 시간 ID      | `404 Not Found` |
| 해당 시간에 예약이 존재하는 경우 | `409 Conflict`  |

---

### 예약

#### `GET /reservations` — 예약 전체 조회

**Response** `200 OK`

```json
[
  {
    "id": 1,
    "name": "fizz",
    "date": "2026-05-15",
    "time": {
      "id": 1,
      "startAt": "10:00:00"
    },
    "theme": {
      "id": 1,
      "name": "피즈의 모험",
      "description": "피즈가 모험을 떠나는 이야기입니다.",
      "thumbnailUrl": "http://localhost:8080/images/fizz.jpg"
    }
  }
]
```

---

#### `GET /reservations?name={name}` — 이름으로 예약 조회

| 파라미터   | 위치    | 타입       | 필수 | 설명     |
|--------|-------|----------|----|--------|
| `name` | Query | `String` | Y  | 예약자 이름 |

**Response** `200 OK` — 형식은 전체 조회와 동일

---

#### `POST /reservations` — 예약 생성

**Request Body**

```json
{
  "name": "fizz",
  "date": "2026-05-15",
  "timeId": 1,
  "themeId": 1
}
```

**Response** `201 Created`

```json
{
  "id": 1,
  "name": "fizz",
  "date": "2026-05-15",
  "time": {
    "id": 1,
    "startAt": "10:00:00"
  },
  "theme": {
    "id": 1,
    "name": "피즈의 모험",
    "description": "피즈가 모험을 떠나는 이야기입니다.",
    "thumbnailUrl": "http://localhost:8080/images/fizz.jpg"
  }
}
```

**Error**

| 상황                     | 상태 코드                      |
|------------------------|----------------------------|
| 존재하지 않는 시간 또는 테마 ID    | `404 Not Found`            |
| 이미 지난 날짜로 예약           | `422 Unprocessable Entity` |
| 오늘 날짜에 이미 지난 시간으로 예약   | `422 Unprocessable Entity` |
| 동일한 날짜·시간·테마 예약이 이미 존재 | `409 Conflict`             |

---

#### `PATCH /reservations/{id}` — 예약 날짜·시간 변경

| 파라미터 | 위치   | 타입     | 필수 | 설명    |
|------|------|--------|----|-------|
| `id` | Path | `Long` | Y  | 예약 ID |

**Request Body**

```json
{
  "date": "2026-06-01",
  "timeId": 2
}
```

**Response** `204 No Content`

**Error**

| 상황                        | 상태 코드                      |
|---------------------------|----------------------------|
| 존재하지 않는 예약 ID             | `404 Not Found`            |
| 이미 지난 예약을 변경 시도           | `422 Unprocessable Entity` |
| 변경하려는 날짜·시간·테마 조합이 이미 예약됨 | `409 Conflict`             |

---

#### `DELETE /reservations/{id}` — 예약 취소

| 파라미터 | 위치   | 타입     | 필수 | 설명    |
|------|------|--------|----|-------|
| `id` | Path | `Long` | Y  | 예약 ID |

**Response** `204 No Content`

**Error**

| 상황              | 상태 코드                      |
|-----------------|----------------------------|
| 존재하지 않는 예약 ID   | `404 Not Found`            |
| 이미 지난 예약을 취소 시도 | `422 Unprocessable Entity` |

---

## 에러 응답 명세

모든 에러는 아래 형식의 JSON 본문을 반환합니다.

```json
{
  "status": "CONFLICT",
  "message": "동일한 예약이 이미 존재합니다."
}
```

| 필드        | 타입       | 설명                                       |
|-----------|----------|------------------------------------------|
| `status`  | `String` | HTTP 상태 코드명 (예: `NOT_FOUND`, `CONFLICT`) |
| `message` | `String` | 사용자에게 전달할 에러 설명                          |

### 에러 코드 목록

| 상황             | HTTP 상태 | `status` 값              | `message`                   |
|----------------|---------|-------------------------|-----------------------------|
| 존재하지 않는 예약     | `404`   | `NOT_FOUND`             | 해당 ID의 예약을 찾을 수 없습니다.       |
| 동일한 예약 중복      | `409`   | `CONFLICT`              | 동일한 예약이 이미 존재합니다.           |
| 이미 지난 날짜로 예약   | `422`   | `UNPROCESSABLE_ENTITY`  | 오늘보다 이전 날짜로 예약할 수 없습니다.     |
| 이미 지난 시간으로 예약  | `422`   | `UNPROCESSABLE_ENTITY`  | 현재 시각보다 이전 시간으로 예약할 수 없습니다. |
| 이미 지난 예약 변경·취소 | `422`   | `UNPROCESSABLE_ENTITY`  | 이미 지난 예약은 변경하거나 취소할 수 없습니다. |
| 사용 중인 시간 삭제    | `409`   | `CONFLICT`              | 해당 시간에 예약이 존재하여 삭제할 수 없습니다. |
| 존재하지 않는 예약 시간  | `404`   | `NOT_FOUND`             | 해당 ID의 예약 시간을 찾을 수 없습니다.    |
| 존재하지 않는 테마     | `404`   | `NOT_FOUND`             | 해당 ID의 테마를 찾을 수 없습니다.       |
| 잘못된 날짜 형식      | `400`   | `BAD_REQUEST`           | 날짜 형식이 잘못되었습니다.             |
| 잘못된 ID 형식      | `400`   | `BAD_REQUEST`           | ID 형식이 잘못되었습니다.             |
| 잘못된 요청 형식      | `400`   | `BAD_REQUEST`           | 요청 형식이 올바르지 않습니다.           |
| 서버 내부 오류       | `500`   | `INTERNAL_SERVER_ERROR` | 서버 내부에서 에러가 발생했습니다.         |
