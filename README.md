# spring-roomescape-admin

방탈출 예약을 관리하는 Spring 웹 애플리케이션입니다.

## 📋 목차

- [프로젝트 구조](#-프로젝트-구조)
- [이번 사이클 구현 기능 목록](#-이번-사이클-구현-기능-목록)
- [API 명세](#-api-명세)
- [에러 응답 명세](#-에러-응답-명세)
- [2단계 - 사용자 예약](#2단계---사용자-예약)
- [테스트 케이스](#-테스트-케이스)

---

## 🗂 프로젝트 구조

### Domain

#### Reservation

예약 정보를 관리한다.

| 필드     | 타입          | 설명     |
|--------|-------------|--------|
| `id`   | `Long`      | 예약 ID  |
| `name` | `String`    | 예약자 이름 |
| `date` | `LocalDate` | 예약 날짜  |
| `time` | `LocalTime` | 예약 시간  |

#### ReservationTime

예약 시간 정보를 관리한다.

| 필드        | 타입          | 설명    |
|-----------|-------------|-------|
| `id`      | `Long`      | 시간 ID |
| `startAt` | `LocalTime` | 시작 시간 |

#### Theme

테마 정보를 관리한다.

| 필드            | 타입       | 설명    |
|---------------|----------|-------|
| `id`          | `Long`   | 테마 ID |
| `name`        | `String` | 테마 이름 |
| `description` | `String` | 테마 설명 |

#### Holiday

영업일이 아닌 날(휴일) 정보를 관리한다.

| 필드     | 타입          | 설명    |
|--------|-------------|-------|
| `id`   | `Long`      | 휴일 ID |
| `date` | `LocalDate` | 휴일 날짜 |

---

### Controller

#### ReservationController

예약 관련 HTTP 요청을 처리한다.

#### ReservationTimeController

시간 관련 HTTP 요청을 처리한다.

---

## 🧩 이번 사이클 구현 기능 목록

### 1단계 - 서비스 정책 적용

- [ ] 과거 날짜·시간 예약 생성 거부
- [ ] 같은 날짜·시간·테마의 중복 예약 생성 거부
- [ ] 예약이 존재하는 예약 시간 삭제 거부
- [ ] 빈 이름, 빈 설명, 잘못된 날짜·시간 형식, 존재하지 않는 ID 등 유효하지 않은 입력 거부

### 2단계 - 에러 응답 설계

- [ ] 서비스 정책 위반, 입력값 오류, 리소스 없음 오류를 일관된 JSON 형식으로 반환
- [ ] 예상 가능한 예외가 `500 Internal Server Error`로 사용자에게 노출되지 않도록 처리
- [ ] 브라우저 화면에서 API 에러 메시지를 사용자 친화 문구로 표시

### 3단계 - 내 예약 조회/변경/취소

- [ ] 이름으로 내 예약 목록 조회
- [ ] 내 예약 취소
- [ ] 내 예약 날짜·시간 변경
- [ ] 지난 예약 취소·변경 거부
- [ ] 이미 예약된 날짜·시간·테마로 변경 거부

### 화면 동작

- [ ] 예약 생성, 조회, 변경, 취소 성공 시 화면 목록 갱신
- [ ] 잘못된 요청 실패 시 응답 본문의 `message`를 화면에 표시
- [ ] 서버 내부 오류가 발생해도 기술적 스택트레이스 대신 공통 안내 문구 표시

---

## API 명세

| 기능        | 메서드 / URL                                     | 요청 본문                           | 응답                               |
|-----------|-----------------------------------------------|---------------------------------|----------------------------------|
| 예약 전체 조회  | `GET /reservations`                           | —                               | `[{id, name, date, time}, ...]`  |
| 예약 추가     | `POST /reservations`                          | `{name, date, timeId}`          | `{id, name, date, time}`         |
| 예약 삭제     | `DELETE /reservations/{id}`                   | —                               | `204 No Content`                 |
| 시간 전체 조회  | `GET /times`                                  | —                               | `[{id, startAt}, ...]`           |
| 시간 추가     | `POST /times`                                 | `{startAt}`                     | `{id, startAt}`                  |
| 시간 삭제     | `DELETE /times/{id}`                          | —                               | `204 No Content`                 |
| 테마 전체 조회  | `GET /themes`                                 | —                               | `[{id, name, description}, ...]` |
| 테마 추가     | `POST /themes`                                | `{name, description}`           | `{id, name, description}`        |
| 테마 삭제     | `DELETE /themes/{id}`                         | —                               | `204 No Content`                 |
| 사용 가능 날짜  | `GET /available-dates?month=YYYY-MM`          | —                               | `["yyyy-MM-dd", ...]`            |
| 테마별 가능 시간 | `GET /themes/{themeId}/times?date=yyyy-MM-dd` | —                               | `[{id, startAt}, ...]`           |
| 사용자 예약 목록 조회 | `GET /users/reservations?name={name}`         | —                               | `[{id, name, date, time, theme}, ...]` |
| 사용자 예약 추가 | `POST /users/reservations`                    | `{name, date, themeId, timeId}` | `{id, name, date, time, theme}`  |
| 사용자 예약 변경 | `PATCH /users/reservations/{id}`              | `{date, timeId}`                | `{id, name, date, time, theme}`  |
| 사용자 예약 취소 | `DELETE /users/reservations/{id}`             | —                               | `204 No Content`                 |
| 인기 테마 조회  | `GET /themes/best?date=yyyy-MM-dd`            | —                               | `[{id, name}, ...]`              |

---

## 🚨 에러 응답 명세

### 공통 응답 형식

```json
{
  "code": "INVALID_REQUEST",
  "message": "예약자 이름은 비어 있을 수 없습니다."
}
```

| 필드 | 설명 | 예시 |
|---|---|---|
| `code` | 클라이언트가 분기할 수 있는 에러 코드 | `DUPLICATE_RESERVATION` |
| `message` | 사용자가 이해할 수 있는 한국어 메시지 | `이미 예약된 시간입니다.` |

### 상태 코드 결정

| 상황 | HTTP 상태 | code | message 예시 |
|---|---:|---|---|
| 빈 이름, 필수 값 누락, 잘못된 날짜·시간 형식 | `400 Bad Request` | `INVALID_REQUEST` | `요청 형식이 올바르지 않습니다.` |
| 과거 날짜·시간 예약 생성 | `400 Bad Request` | `PAST_RESERVATION_NOT_ALLOWED` | `지난 날짜와 시간으로 예약할 수 없습니다.` |
| 같은 날짜·시간·테마 중복 예약 | `409 Conflict` | `DUPLICATE_RESERVATION` | `이미 예약된 시간입니다.` |
| 예약이 존재하는 시간 삭제 | `409 Conflict` | `RESERVED_TIME_DELETE_NOT_ALLOWED` | `예약이 존재하는 시간은 삭제할 수 없습니다.` |
| 지난 예약 취소 | `400 Bad Request` | `PAST_RESERVATION_CANCEL_NOT_ALLOWED` | `지난 예약은 취소할 수 없습니다.` |
| 지난 예약 변경 | `400 Bad Request` | `PAST_RESERVATION_CHANGE_NOT_ALLOWED` | `지난 예약은 변경할 수 없습니다.` |
| 변경하려는 날짜·시간·테마가 이미 예약됨 | `409 Conflict` | `DUPLICATE_RESERVATION` | `이미 예약된 시간입니다.` |
| 존재하지 않는 예약, 시간, 테마, 휴일 | `404 Not Found` | `*_NOT_FOUND` | `예약을 찾을 수 없습니다.` |
| 예상하지 못한 서버 오류 | `500 Internal Server Error` | `INTERNAL_SERVER_ERROR` | `서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.` |

### 클라이언트 표시 규칙

- API 호출 실패 시 응답 본문의 `message`를 우선 표시한다.
- 응답 본문이 없거나 파싱할 수 없으면 `요청 처리 중 오류가 발생했습니다.`를 표시한다.
- `500` 응답은 내부 구현 정보 없이 공통 안내 문구만 표시한다.

---

## 1단계 - 방탈출 예약 관리

### GET /reservations — 예약 전체 조회

**응답 예시**

```http
HTTP/1.1 200 OK
Content-Type: application/json

[
    {
        "id": 1,
        "name": "브라운",
        "date": "2023-08-05",
        "time": {
            "id": 1,
            "startAt": "10:00"
        }
    }
]
```

---

### POST /reservations — 예약 추가

**요청 예시**

```http
POST /reservations HTTP/1.1
Content-Type: application/json

{
    "name": "브라운",
    "date": "2023-08-05",
    "timeId": 1
}
```

**응답 예시**

```http
HTTP/1.1 201 Created
Content-Type: application/json

{
    "id": 1,
    "name": "브라운",
    "date": "2023-08-05",
    "time": {
        "id": 1,
        "startAt": "10:00"
    }
}
```

---

### DELETE /reservations/{id} — 예약 삭제

**응답 예시**

```http
HTTP/1.1 204 No Content
```

---

### GET /times — 시간 전체 조회

**응답 예시**

```http
HTTP/1.1 200 OK
Content-Type: application/json

[
    {
        "id": 1,
        "startAt": "10:00"
    }
]
```

---

### POST /times — 시간 추가

**요청 예시**

```http
POST /times HTTP/1.1
Content-Type: application/json

{
    "startAt": "10:00"
}
```

**응답 예시**

```http
HTTP/1.1 201 Created
Content-Type: application/json

{
    "id": 1,
    "startAt": "10:00"
}
```

---

### DELETE /times/{id} — 시간 삭제

**응답 예시**

```http
HTTP/1.1 204 No Content
```

---

## 2단계 - 사용자 예약

### GET /themes — 테마 전체 조회

**응답 예시**

```http
HTTP/1.1 200 OK
Content-Type: application/json

[
    {
        "id": 1,
        "name": "테마명",
        "description": "테마 설명"
    }
]
```

---

### POST /themes — 테마 추가

**요청 예시**

```http
POST /themes HTTP/1.1
Content-Type: application/json

{
    "name": "테마명",
    "description": "테마 설명"
}
```

**응답 예시**

```http
HTTP/1.1 201 Created
Content-Type: application/json

{
    "id": 1,
    "name": "테마명",
    "description": "테마 설명"
}
```

---

### DELETE /themes/{id} — 테마 삭제

**응답 예시**

```http
HTTP/1.1 204 No Content
```

---

### GET /available-dates — 사용 가능 날짜

**요청 예시** (쿼리 생략 시 전체 기간 등 구현에 따름)

```http
GET /available-dates?month=2026-05 HTTP/1.1
```

**응답 예시**

```http
HTTP/1.1 200 OK
Content-Type: application/json

[
    "2026-05-04",
    "2026-05-05",
    "2026-05-06"
]
```

---

### GET /themes/{themeId}/times — 테마별 가능한 시간

**요청 예시**

```http
GET /themes/1/times?date=2025-05-10 HTTP/1.1
```

**응답 예시**

```http
HTTP/1.1 200 OK
Content-Type: application/json

[
    {
        "id": 1,
        "startAt": "10:00"
    },
    {
        "id": 2,
        "startAt": "12:00"
    }
]
```

---

### POST /users/reservations — 사용자 예약 추가

**요청 예시**

```http
POST /users/reservations HTTP/1.1
Content-Type: application/json

{
    "name": "브라운",
    "date": "2025-05-10",
    "themeId": 1,
    "timeId": 1
}
```

**응답 예시**

```http
HTTP/1.1 201 Created
Content-Type: application/json

{
    "id": 1,
    "name": "브라운",
    "date": "2025-05-10",
    "time": {
        "id": 1,
        "startAt": "10:00"
    },
    "theme": {
        "id": 1,
        "name": "테마명"
    }
}
```

---

### DELETE /users/reservations/{id} — 사용자 예약 취소

**응답 예시**

```http
HTTP/1.1 204 No Content
```

---

### GET /themes/best — 인기 테마 조회

**요청 예시**

```http
GET /themes/best?date=2025-05-10 HTTP/1.1
```

**응답 예시**

```http
HTTP/1.1 200 OK
Content-Type: application/json

[
    {
        "id": 1,
        "name": "테마명"
    }
]
```

---

## ✅ 테스트 케이스

### ReservationAPITest

- [x] 예약자 이름, 날짜, 시간으로 예약을 생성한다.
- [x] 생성된 예약 정보를 조회한다.
- [x] 예약 ID로 예약을 삭제한다.
- [x] 예약자 이름 없이 예약을 생성하는 경우, 400을 반환한다.
- [x] 존재하지 않는 시간 ID로 예약을 생성하는 경우, 400을 반환한다.
- [x] 과거 날짜로 예약을 생성하는 경우, 400을 반환한다.

### ReservationTimeAPITest

- [x] 시작 시간으로 예약 시간을 생성한다.
- [x] 생성된 예약 시간 목록을 조회한다.
- [x] 예약 시간 ID로 예약 시간을 삭제한다.
- [x] 시작 시간 없이 예약 시간을 생성하는 경우, 400을 반환한다.
- [x] 예약과 시간이 올바르게 연결된다.

### DBTest

- [x] H2 데이터베이스 연결 및 테이블 생성을 확인한다.
- [x] DB에서 예약 목록을 조회한다.
- [x] DB에 예약을 추가하고 삭제한다.

### LayerSeparationTest

- [x] 컨트롤러에 JdbcTemplate이 직접 주입되지 않는다.

### ThemeAPITest

- [x] 이름·설명으로 테마를 생성한다.
- [x] 생성된 테마 목록을 조회한다.
- [x] 테마 ID로 테마를 삭제한다.
- [x] 존재하지 않는 테마 ID로 삭제하는 경우, 404를 반환한다.

### ThemeTimeAPITest

- [x] 날짜·테마 ID로 해당 테마의 예약 가능한 시간 목록을 조회한다.
- [x] 이미 예약된 시간 슬롯은 목록에 포함되지 않는다.

### HolidayDBTest

- [x] 영업일이 아닌 날(휴일) 정보를 저장하기 위한 `holiday` 테이블을 생성한다.
- [x] `holiday` 데이터를 추가하고 조회한다.
- [x] `holiday` 데이터를 삭제한다.

### ReservationAPITest

- [x] 이름·날짜·테마 ID·시간 ID로 예약을 생성한다.
- [x] 생성된 예약 응답에 시간·테마 정보가 포함된다.
- [x] 예약 ID로 예약을 취소한다.
- [x] 필수 값 누락으로 예약을 생성하는 경우, 400을 반환한다.
- [x] 존재하지 않는 예약 ID로 취소하는 경우, 404를 반환한다.

### ThemeBestAPITest

- [x] 현재 날짜 기준으로 인기 테마 목록을 조회한다.
- [x] 기준 기간의 예약 건수로 인기 순이 결정된다.
