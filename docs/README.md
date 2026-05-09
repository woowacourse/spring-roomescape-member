# 요구사항

> 사이클1 - 관리자 여부(인가)는 판단하지 않는다. 인가는 구현하지 않는다.

---

## 1. Reservation

`status` : `RESERVED` | `CANCELED`

### 관리자

**예약을 한다.**
- 날짜와 테마를 선택하고 예약 가능한 시간을 선택하면 예약할 수 있다.
- `timeId`, `themeId`, `name`, `date`를 입력받는다.
- 예약의 기본 상태는 `RESERVED`이다.

**예약을 취소한다.**
- 해당 API를 사용한다는 게, 관리자임을 보장한다고 가정한다. 즉, 관리자인지 별도로 검증하지 않는다.
- 예약 상태를 `CANCELED`로 변경한다.

**예약을 조회한다.**
- 전체 예약을 조회한다.

### 사용자

**예약을 한다.**
- 날짜와 테마를 선택하고 예약 가능한 시간을 선택하면 예약할 수 있다.
- `timeId`, `themeId`, `name`, `date`를 입력받는다.
- 예약의 기본 상태는 `RESERVED`이다.

**예약을 취소한다.**
- 예약 상태를 `CANCELED`로 변경한다.

**예약을 조회한다.**
- `name`(예약자 성함)을 Path Variable로 받아 해당 예약자의 예약 목록을 조회한다.
- 예약 날짜 및 시간 기준 오름차순으로 정렬한다.

---

## 2. ClosedDate (휴무일)

### 관리자

**휴무일을 생성한다.**
- `date`를 보내 생성한다.
- 이미 존재하는 `date`라면 예외가 발생한다.

**휴무일을 조회한다.**
- `id`, `date`를 포함한 `List<ClosedDateDetailDto>`를 반환한다.

**휴무일을 삭제한다.**
- hard-delete 방식으로 삭제한다.

### 사용자

**예약 가능한 날짜를 조회한다.**
- 오늘부터 30일치 날짜 중 휴무일을 제외한 날짜 목록을 반환한다.

---

## 3. ReservationTime

### 관리자

**예약 가능한 시간을 생성한다.**
- `startAt`을 보내 생성한다.
- 이미 존재하는 `startAt`이라면 예외가 발생한다.

**예약 가능한 시간을 조회한다.**
- `id`, `startAt`을 포함한 `List<ReservationTimeDetailDto>`를 반환한다.

**예약 가능한 시간을 삭제한다.**
- hard-delete 방식으로 삭제한다.

### 사용자

**예약 가능한 시간을 조회한다.**
- `date`와 `themeId`를 받아 해당 날짜+테마에 `RESERVED` 예약이 없는 시간만 반환한다.

---

## 4. Theme

`isActive` : `true` | `false`

### 관리자

**테마를 생성한다.**
- `name`, `description`, `thumbnailUrl`을 받아 생성한다.
- 기본 상태는 비활성화(`isActive: false`)이다.

**모든 테마를 조회한다.**
- 활성화/비활성화 여부 포함 전체 목록을 반환한다.

**테마의 상태를 변경한다.**
- 활성화/비활성화를 전환한다.

### 사용자

**인기 테마 Top10을 조회한다. (추천용)**
- 최근 7일 기준, 취소를 포함하지 않은(`RESERVED`) 예약 수 Top 10 테마를 조회한다.
- 활성화된 테마만 포함한다.
- 순위만 반환한다. (예약 수 X)

**활성화된 테마 목록을 조회한다.**
- 가나다순 정렬하여 반환한다.

---

# API 명세서

---

## 1. Reservation

### 1-1. 관리자

#### (관리자) 방탈출을 예약한다.

- URL: `/admin/reservations`
- Method: `POST`
- 요청 본문

```json
{
    "name": "송송",
    "date": "2026-05-04",
    "timeId": 1,
    "themeId": 1
}
```

- 응답 본문 `201 Created`

```json
{
    "id": 1,
    "name": "송송",
    "date": "2026-05-04",
    "time": "12:00:00",
    "theme": {
        "id": 1,
        "name": "공포",
        "description": "테스트용 더미 설명1",
        "thumbnailUrl": "https://~",
        "isActive": true
    },
    "status": "RESERVED"
}
```

---

#### (관리자) 방탈출 예약을 취소한다.

- URL: `/admin/reservations/{id}`
- Method: `PATCH`
- 요청 본문 없음 (취소로 고정)
- 응답 본문 `200 OK`

```json
{
    "id": 1,
    "name": "송송",
    "date": "2026-05-04",
    "time": "12:00:00",
    "theme": {
        "id": 1,
        "name": "공포",
        "description": "테스트용 더미 설명1",
        "thumbnailUrl": "https://~",
        "isActive": true
    },
    "status": "CANCELED"
}
```

---

#### (관리자) 방탈출 예약을 조회한다.

- URL: `/admin/reservations`
- Method: `GET`
- 응답 본문 `200 OK`

```json
[
    {
        "id": 1,
        "name": "송송",
        "date": "2026-05-04",
        "time": "12:00:00",
        "theme": {
            "id": 1,
            "name": "공포",
            "description": "테스트용 더미 설명1",
            "thumbnailUrl": "https://~",
            "isActive": true
        },
        "status": "RESERVED"
    }
]
```

---

### 1-2. 사용자

#### (사용자) 방탈출을 예약한다.

- URL: `/reservations`
- Method: `POST`
- 요청 본문

```json
{
    "name": "송송",
    "date": "2026-05-04",
    "timeId": 1,
    "themeId": 1
}
```

- 응답 본문 `201 Created`

```json
{
    "id": 1,
    "name": "송송",
    "date": "2026-05-04",
    "time": "12:00:00",
    "theme": {
        "id": 1,
        "name": "공포",
        "description": "테스트용 더미 설명1",
        "thumbnailUrl": "https://~",
        "isActive": true
    },
    "status": "RESERVED"
}
```

---

#### (사용자) 방탈출 예약을 취소한다.

- URL: `/reservations/{id}`
- Method: `PATCH`
- 요청 본문 없음 (취소로 고정)
- 응답 본문 `200 OK`

```json
{
    "id": 1,
    "name": "송송",
    "date": "2026-05-04",
    "time": "12:00:00",
    "theme": {
        "id": 1,
        "name": "공포",
        "description": "테스트용 더미 설명1",
        "thumbnailUrl": "https://~",
        "isActive": true
    },
    "status": "CANCELED"
}
```

---

#### (사용자) 방탈출 예약을 조회한다.

- URL: `/reservations/{name}`
- Method: `GET`
- Path Variable: `name` — 예약자 성함
- 정렬: 예약 날짜 및 시간 기준 오름차순
- 응답 본문 `200 OK`

```json
[
    {
        "id": 1,
        "name": "송송",
        "date": "2026-05-04",
        "time": "12:00:00",
        "theme": {
            "id": 1,
            "name": "공포",
            "description": "테스트용 더미 설명1",
            "thumbnailUrl": "https://~",
            "isActive": true
        },
        "status": "RESERVED"
    }
]
```

---

## 2. ReservationTime

### 2-1. 관리자

#### (관리자) 예약 시간을 등록한다.

- URL: `/admin/times`
- Method: `POST`
- 요청 본문

```json
{
    "startAt": "12:00:00"
}
```

- 응답 본문 `201 Created`

```json
{
    "id": 1,
    "startAt": "12:00:00"
}
```

---

#### (관리자) 예약 시간 목록을 조회한다.

- URL: `/admin/times`
- Method: `GET`
- 응답 본문 `200 OK`

```json
[
    {
        "id": 1,
        "startAt": "12:00:00"
    }
]
```

---

#### (관리자) 예약 시간을 삭제한다.

- URL: `/admin/times/{id}`
- Method: `DELETE`
- 응답 본문 `200 OK`

```json
{
    "id": 1,
    "startAt": "12:00:00"
}
```

---

### 2-2. 사용자

#### (사용자) 예약 가능한 시간을 조회한다.

- URL: `/times?date={date}&themeId={themeId}`
- Method: `GET`
- 응답 본문 `200 OK`

```json
[
    {
        "id": 1,
        "startAt": "10:00:00"
    }
]
```

---

## 3. ClosedDate (휴무일)

### 3-1. 관리자

#### (관리자) 휴무일을 등록한다.

- URL: `/admin/closed-dates`
- Method: `POST`
- 요청 본문

```json
{
    "date": "2026-05-04"
}
```

- 응답 본문 `201 Created`

```json
{
    "id": 1,
    "date": "2026-05-04"
}
```

---

#### (관리자) 휴무일 목록을 조회한다.

- URL: `/admin/closed-dates`
- Method: `GET`
- 응답 본문 `200 OK`

```json
[
    {
        "id": 1,
        "date": "2026-05-04"
    }
]
```

---

#### (관리자) 휴무일을 삭제한다.

- URL: `/admin/closed-dates/{id}`
- Method: `DELETE`
- 응답 본문 `200 OK`

```json
{
    "id": 1,
    "date": "2026-05-04"
}
```

---

### 3-2. 사용자

#### (사용자) 예약 가능한 날짜를 조회한다.

- URL: `/dates`
- Method: `GET`
- 오늘부터 30일치 날짜 중 휴무일 제외
- 응답 본문 `200 OK`

```json
[
    {
        "date": "2026-05-04"
    }
]
```

---

## 4. Theme

### 4-1. 관리자

#### (관리자) 테마를 생성한다.

- URL: `/admin/themes`
- Method: `POST`
- 요청 본문

```json
{
    "name": "공포",
    "description": "테스트 더미 설명1",
    "thumbnailUrl": "https://~~"
}
```

- 응답 본문 `201 Created`

```json
{
    "id": 1,
    "name": "공포",
    "description": "테스트 더미 설명1",
    "thumbnailUrl": "https://~~",
    "isActive": false
}
```

---

#### (관리자) 모든 테마를 조회한다.

- URL: `/admin/themes`
- Method: `GET`
- 응답 본문 `200 OK`

```json
[
    {
        "id": 1,
        "name": "공포",
        "description": "테스트 더미 설명1",
        "thumbnailUrl": "https://~~",
        "isActive": false
    }
]
```

---

#### (관리자) 테마를 활성화/비활성화한다.

- URL: `/admin/themes/{id}`
- Method: `PATCH`
- 요청 본문

```json
{
    "isActive": true
}
```

- 응답 본문 `200 OK`

```json
{
    "id": 1,
    "name": "공포",
    "description": "테스트 더미 설명1",
    "thumbnailUrl": "https://~",
    "isActive": true
}
```

---

### 4-2. 사용자

#### (사용자) 인기 테마 목록을 조회한다.

- URL: `/themes/popular?top={top}`
- Method: `GET`
- Query Parameters: `top` (required)
- 기준: 오늘 기준 최근 7일 이내 게임 날짜, `RESERVED` 예약 수 기준 내림차순, 활성화된 테마만
- 응답 본문 `200 OK`

```json
[
    {
        "id": 1,
        "name": "공포",
        "description": "테스트 더미 설명1",
        "thumbnailUrl": "https://~",
        "isActive": true
    }
]
```

---

#### (사용자) 테마 목록을 조회한다.

- URL: `/themes`
- Method: `GET`
- 활성화된 테마만, 가나다순 정렬
- 응답 본문 `200 OK`

```json
[
    {
        "id": 1,
        "name": "공포",
        "description": "테스트 더미 설명1",
        "thumbnailUrl": "https://~",
        "isActive": true
    }
]
```

---

## ERD

```mermaid
erDiagram
    THEME ||--o{ RESERVATION : "하나의 테마는 여러 예약을 가짐"
    RESERVATION_TIME ||--o{ RESERVATION : "특정 시간에 여러 예약 가능"

    RESERVATION {
        Long id PK
        String name
        Date date
        Time start_at
        Long theme_id FK "theme(id) 참조"
        Enum status "RESERVED | CANCELED"
    }

    THEME {
        Long id PK
        String name
        String description
        String thumbnail_url
        Boolean is_active
    }

    CLOSED_DATE {
        Long id PK
        Date date UNIQUE
    }

    RESERVATION_TIME {
        Long id PK
        Time start_at UNIQUE
    }
```
