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
            "start_at": "12:30"
        },
        "theme": {
            "id": 5,
            "name": "공포",
            "description": "짱무섭",
            "image_url": "..."
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
    "time_id": 1,
    "theme_id": 5
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

### 예약시간 단건 추가

`POST /times`

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

### 예약시간 단건 삭제

`DELETE /times/{id}`

---

### 예약 가능 시간 조회

`GET /times?type=available&date={date}&themeId={themeId}`

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
        "image_url": "..."
    }
]
```

---

### 테마 단건 추가

`POST /themes`

**Request**
```json
{
    "name": "",
    "description": "",
    "image_url": ""
}
```

**Response** `201 Created`
```json
{
    "id": 1
}
```

---

### 테마 단건 삭제

`DELETE /themes/{id}`

---

### 인기 테마 조회

`GET /themes/popular?period=week&limit=10`

**Response** `200 OK`
```json
[
    {
        "id": 2,
        "rank": 1,
        "name": "프랑켄슈타인",
        "description": "설명",
        "image_url": "..."
    },
    {
        "id": 1,
        "rank": 2,
        "name": "나폴리탄",
        "description": "설명",
        "image_url": "..."
    }
]
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

## 2단계 - 사용자 예약

### 예약 가능 시간 조회

- [x]  **사용자는 날짜와 테마를 선택하면 예약 가능한 시간 목록을 조회할 수 있다.**
- [x]  예약 가능한 시간이란 관리자가 등록한 시간 중 해당 날짜 + 테마 조합으로 아직 예약이 없는 시간이다.
- [ ]  이미 예약된 시간은 목록에 표시되지만 선택할 수 없다.

### 예약 생성

- [x]  **사용자는 예약 가능한 시간을 선택해 본인 이름으로 예약할 수 있다.**
- [x]  같은 날짜 + 시간 + 테마 조합이 이미 존재하면 예약할 수 없다.
- [x]  날짜와 시간이 같더라도 테마가 다르면 예약할 수 있다.

---

## 3단계 - 인기 테마 조회

- [ ]  **최근 1주일간 예약이 많은 테마 상위 10개를 조회할 수 있다.**
- [ ]  집계 기간은 오늘을 제외한 직전 7일이다. (예: 오늘이 5/8이면 5/1 ~ 5/7)
- [ ]  예약 수가 같은 경우 정렬 기준은 별도로 정의하지 않는다.

