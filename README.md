# spring-roomescape-admin

방탈출 예약을 관리하는 Spring 웹 애플리케이션입니다.

## 📋 목차

- [프로젝트 구조](#-프로젝트-구조)
- [API 명세](#-api-명세)
- [2단계 - 사용자 예약](#2단계---사용자-예약)
- [테스트 케이스](#-테스트-케이스)

---

## 🗂 프로젝트 구조

### Domain

#### Reservation

예약 정보를 관리한다.

| 필드     | 타입          | 설명 |
|--------|-------------|-----|
| `id`   | `Long`      | 예약 ID |
| `name` | `String`    | 예약자 이름 |
| `date` | `LocalDate` | 예약 날짜 |
| `time` | `LocalTime` | 예약 시간 |

#### ReservationTime

예약 시간 정보를 관리한다.

| 필드       | 타입        | 설명 |
|----------|-----------|------|
| `id`     | `Long`    | 시간 ID |
| `startAt` | `LocalTime` | 시작 시간 |

#### Theme

테마 정보를 관리한다.

| 필드 | 타입 | 설명 |
|---|---|---|
| `id` | `Long` | 테마 ID |
| `name` | `String` | 테마 이름 |
| `description` | `String` | 테마 설명 |

#### Holiday

영업일이 아닌 날(휴일) 정보를 관리한다.

| 필드 | 타입 | 설명 |
|---|---|---|
| `id` | `Long` | 휴일 ID |
| `date` | `LocalDate` | 휴일 날짜 |

---

### Controller

#### ReservationController

예약 관련 HTTP 요청을 처리한다.

#### ReservationTimeController

시간 관련 HTTP 요청을 처리한다.

---

## API 명세

| 기능 | 메서드 / URL | 요청 본문 | 응답 |
|------|-------------|-----------|------|
| 예약 전체 조회 | `GET /reservations` | — | `[{id, name, date, time}, ...]` |
| 예약 추가 | `POST /reservations` | `{name, date, timeId}` | `{id, name, date, time}` |
| 예약 삭제 | `DELETE /reservations/{id}` | — | `204 No Content` |
| 시간 전체 조회 | `GET /times` | — | `[{id, startAt}, ...]` |
| 시간 추가 | `POST /times` | `{startAt}` | `{id, startAt}` |
| 시간 삭제 | `DELETE /times/{id}` | — | `204 No Content` |
| 테마 전체 조회 | `GET /themes` | — | `[{id, name, description}, ...]` |
| 테마 추가 | `POST /themes` | `{name, description}` | `{id, name, description}` |
| 테마 삭제 | `DELETE /themes/{id}` | — | `204 No Content` |
| 사용 가능 날짜 | `GET /available-dates?month=YYYY-MM` | — | `["yyyy-MM-dd", ...]` |
| 테마별 가능 시간 | `GET /themes/{themeId}/times?date=yyyy-MM-dd` | — | `[{id, startAt}, ...]` |
| 사용자 예약 추가 | `POST /users/reservations` | `{name, date, themeId, timeId}` | `{id, name, date, time, theme}` |
| 사용자 예약 취소 | `DELETE /users/reservations/{id}` | — | `204 No Content` |
| 인기 테마 조회 | `GET /themes/best?date=yyyy-MM-dd` | — | `[{id, name}, ...]` |

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

- [ ] 이름·설명으로 테마를 생성한다.
- [ ] 생성된 테마 목록을 조회한다.
- [ ] 테마 ID로 테마를 삭제한다.
- [ ] 이름 없이 테마를 생성하는 경우, 400을 반환한다.
- [ ] 존재하지 않는 테마 ID로 삭제하는 경우, 404를 반환한다.

### ThemeTimeAPITest

- [ ] 날짜·테마 ID로 해당 테마의 예약 가능한 시간 목록을 조회한다.
- [ ] 이미 예약된 시간 슬롯은 목록에 포함되지 않는다.

### AvailableDateAPITest

- [ ] 사용 가능한 날짜 목록을 조회한다.
- [ ] `month` 쿼리로 특정 월만 필터하여 조회한다.
- [ ] 영업일이 아닌 날(휴일)은 `holiday` 테이블 기준으로 목록에서 제외한다.

### HolidayDBTest

- [ ] 영업일이 아닌 날(휴일) 정보를 저장하기 위한 `holiday` 테이블을 생성한다.
- [ ] `holiday` 테이블 컬럼은 `id`, `date`를 가진다.
- [ ] `holiday` 데이터를 추가하고 조회한다.
- [ ] `holiday` 데이터를 삭제한다.

### UserReservationAPITest

- [ ] 이름·날짜·테마 ID·시간 ID로 사용자 예약을 생성한다.
- [ ] 생성된 사용자 예약 응답에 시간·테마 정보가 포함된다.
- [ ] 예약 ID로 사용자 예약을 취소한다.
- [ ] 필수 값 누락으로 예약을 생성하는 경우, 400을 반환한다.
- [ ] 존재하지 않는 테마·시간 ID로 예약을 생성하는 경우, 400을 반환한다.
- [ ] 과거 날짜로 예약을 생성하는 경우, 400을 반환한다.
- [ ] 존재하지 않는 예약 ID로 취소하는 경우, 404를 반환한다.

### ThemeBestAPITest

- [ ] 날짜 쿼리로 인기 테마 목록을 조회한다.
- [ ] 해당 날짜의 예약 건수 기준으로 인기 순이 결정된다.
