# 기능명세서

# API 명세

## reservation

### 예약 전체 조회

`GET /reservations`

**Response** `200 OK`

```json
[
  {
    "id": 1,
    "name": "브라운",
    "date": "2026-05-04",
    "time": {
      "id": 1,
      "startAt": "12:30"
    },
    "theme": {
      "id": 5,
      "name": "공포",
      "description": "짱무섭",
      "imageUrl": "..."
    }
  }
]
```

---

### 예약 단건 추가

`POST /reservations`

**Request**

```json
{
  "name": "브라운",
  "date": "2026-05-04",
  "timeId": 1,
  "themeId": 5
}
```

**Response** `201 Created`

```json
{
  "id": 1
}
```

---

### 예약 단건 삭제

`DELETE /reservations/{id}`

---

## reservation_time

### 예약시간 전체 조회

`GET /times`

**Response** `200 OK`

```json
[
  {
    "id": 1,
    "start_at": "12:30"
  }
]
```

---

### (관리자) 예약시간 단건 추가

`POST /times?role=admin`

**Request**

```json
{
  "start_at": "17:00"
}
```

**Response** `201 Created`

```json
{
  "id": 1
}
```

---

### (관리자) 예약시간 단건 삭제

`DELETE /times/{id}?role=admin`

---

### 예약 가능 시간 조회

`GET /times/available?date={date}&themeId={themeId}`

**Response** `200 OK`

```json
{
  "times": [
    {
      "id": 1,
      "StartAt": "17:00",
      "available": true
    },
    {
      "id": 2,
      "StartAt": "15:00",
      "available": false
    }
  ]
}
```

---

## theme

### 테마 전체 조회

`GET /themes`

**Response** `200 OK`

```json
[
  {
    "id": 1,
    "name": "공포",
    "description": "짱무섭",
    "imageUrl": "..."
  }
]
```

---

### (관리자) 테마 단건 추가

`POST /themes?role=admin`

**Request**

```json
{
  "name": "",
  "description": "",
  "imageUrl": ""
}
```

**Response** `201 Created`

```json
{
  "id": 1
}
```

---

### (관리자) 테마 단건 삭제

`DELETE /themes/{id}?role=admin`

---

### 인기 테마 조회

`GET /themes/popular/week?limit=10`

**Response** `200 OK`

```json
{
  "themes": [
    {
      "id": 2,
      "rank": 1,
      "name": "프랑켄슈타인",
      "description": "설명",
      "imageUrl": "..."
    },
    {
      "id": 1,
      "rank": 2,
      "name": "나폴리탄",
      "description": "설명",
      "imageUrl": "..."
    }
  ]
}
```

## 1단계 - 테마 도메인 추가

### 테마

- [x]  테마는 이름, 설명, 썸네일 이미지 URL을 가진다.
- [x]  **관리자는 테마를 추가할 수 있다.**
- [x]  **관리자는 테마를 삭제할 수 있다.**
- [X]  **관리자는 전체 테마 목록을 조회할 수 있다.**

### 예약

- [x]  예약은 테마 정보를 포함한다.
- [X]  예약 생성 시 테마를 반드시 선택해야 한다.

---

## 2단계 - 사용자

### 예약 가능 시간 조회

- [x]  **사용자는 날짜와 테마를 선택하면 예약 가능한 시간 목록을 조회할 수 있다.**
- [x]  예약 가능한 시간이란 관리자가 등록한 시간 중 해당 날짜 + 테마 조합으로 아직 예약이 없는 시간이다.
- [x]  이미 예약된 시간은 목록에 표시되지만 선택할 수 없다.

### 예약 생성

- [x]  **사용자는 예약 가능한 시간을 선택해 본인 이름으로 예약할 수 있다.**
- [x]  같은 날짜 + 시간 + 테마 조합이 이미 존재하면 예약할 수 없다.
- [x]  날짜와 시간이 같더라도 테마가 다르면 예약할 수 있다.

---

## 3단계 - 인기 테마 조회

- [x]  **최근 1주일간 예약이 많은 테마 상위 10개를 조회할 수 있다.**
- [x]  집계 기간은 오늘을 제외한 직전 7일이다. (예: 오늘이 5/8이면 5/1 ~ 5/7)
- [x]  예약 수가 같은 경우 정렬 기준은 별도로 정의하지 않는다.

---

## 사이클2 - 예약 변경/취소와 에러 처리

### 에러 응답 명세

에러 응답의 대상은 클라이언트(프론트엔드/외부 서버)이다.

`code`는 클라이언트 로직의 조건이고, `message`는 개발자 디버깅 편의용이다.

사용자에게 보여줄 문구는 클라이언트가 `code`를 기반으로 다시 정제한다.

(사용자에게 보여줄 문구는 뷰의 책임이라고 생각한다)

<br/>

**응답 형식**

```json
{
  "code": "DUPLICATED_RESERVATION",
  "message": "해당 날짜, 시간, 테마에 이미 예약이 존재합니다."
}
```

**에러 코드 목록**

| code                      | HTTP 상태 | 발생 상황                                        |
|---------------------------|---------|----------------------------------------------|
| `INVALID_REQUEST_FORMAT`  | 400     | JSON 파싱 실패, URL 경로 변수 타입 오류                  |
| `VALIDATION_FAILED`       | 400     | 필수값 누락, 필드 유효성 검증 실패 (`@NotBlank`, `@NotNull` 등) |
| `NOT_FOUND`               | 404     | 존재하지 않는 리소스 요청                               |
| `FORBIDDEN`               | 403     | 접근 권한 없음                                     |
| `DUPLICATED_RESERVATION`  | 409     | 같은 날짜, 시간, 테마 예약이 이미 존재                      |
| `PAST_DATE_RESERVATION`   | 422     | 요청한 날짜 자체가 과거 (생성, 변경 공통)                    |
| `PAST_RESERVATION_CANCEL` | 422     | 이미 지난 예약 취소 시도                               |
| `PAST_RESERVATION_UPDATE` | 422     | 이미 지난 예약 변경 시도                               |
| `INTERNAL_SERVER_ERROR`   | 500     | 예상치 못한 서버 오류 (사용자 노출 X)                      |

---

### 4단계 - 서비스 정책 적용

- [ ]  지나간 날짜, 시간으로 예약하면 예외가 발생한다 -> `PAST_DATE_RESERVATION` (422)
- [ ]  같은 날짜, 시간, 테마 조합이 이미 존재하면 예외가 발생한다 -> `DUPLICATED_RESERVATION` (409)
- [ ]  예약이 존재하는 시간을 삭제하는 경우 예외가 발생한다 -> `INVALID_INPUT` (400)
- [ ]  필수값 누락 및 잘못된 형식의 요청이 오는 경우 예외가 발생한다 -> `INVALID_INPUT` (400)

---

### 5단계 - 에러 응답 설계

- [ ]  `ErrorCode` ENUM을 추가하고, 각 에러 코드에 HTTP 상태와 기본 메시지를 매핑한다
- [ ]  에러 응답 형식을 `{ "code": "...", "message": "..." }`로 변경한다
- [ ]  매핑되지 않는 URL 요청 시 500이 아닌 404를 반환한다. (`NoResourceFoundException` 처리)
- [ ]  `GlobalExceptionHandler`를 `ErrorCode` 기반으로 정비한다
- [ ]  어떤 예외도 500이 사용자에게 노출되지 않는다

---

### 6단계 - 내 예약 조회, 변경, 취소

#### 내 예약 조회

- [ ]  **사용자는 이름으로 본인의 예약 목록을 조회할 수 있다**
- [ ]  존재하지 않는 이름으로 조회하면 빈 목록을 반환한다.

#### 내 예약 변경

- [ ]  **사용자는 본인 예약의 날짜 및 시간을 변경할 수 있다**
- [ ]  존재하지 않는 예약을 변경하는 경우 예외가 발생한다 -> `NOT_FOUND` (404)
- [ ]  이미 지난 예약은 변경하는 경우 예외가 발생한다 -> `PAST_RESERVATION_UPDATE` (422)
- [ ]  변경하려는 날짜가 과거인 경우 예외가 발생한다 -> `PAST_DATE_RESERVATION` (422)
- [ ]  변경하려는 날짜, 시간, 테마가 이미 예약되어 있는 경우 예외가 발생한다 -> `DUPLICATED_RESERVATION` (409)

#### 내 예약 취소

- [ ]  **사용자는 본인의 예약을 취소할 수 있다**
- [ ]  존재하지 않는 예약은 취소할 수 없다. -> `NOT_FOUND` (404)
- [ ]  이미 지난 예약은 취소할 수 없다. -> `PAST_RESERVATION_CANCEL` (422)

---

### API 명세 추가

#### 내 예약 조회

`GET /reservations?name={name}`

**Response** `200 OK`

```json
[
  {
    "id": 1,
    "name": "브라운",
    "date": "2026-05-04",
    "time": {
      "id": 1,
      "startAt": "12:30"
    },
    "theme": {
      "id": 5,
      "name": "공포",
      "description": "짱무섭",
      "imageUrl": "..."
    }
  }
]
```

---

#### 예약 변경

`PATCH /reservations/{id}`

**Request**

```json
{
  "date": "2026-06-01",
  "timeId": 2
}
```

**Response** `204 OK`

---

#### 예약 취소

`DELETE /reservations/{id}` — 기존 API 재사용, 정책 추가

