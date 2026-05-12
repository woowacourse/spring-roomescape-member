# 📑 Roomescape API 명세서

> **Base URL** `http://localhost:8080`  
> **Content-Type** `application/json`

---

## 목차

- 사용자 API
  - [예약 가능한 날짜 조회](#예약-가능한-날짜-조회)
  - [전체 테마 목록 조회](#전체-테마-목록-조회)
  - [예약 가능한 시간 조회](#예약-가능한-시간-조회)
  - [예약 생성](#예약-생성)
  - [인기 테마 조회](#인기-테마-조회)
- 관리자 API
  - 예약
    - [전체 예약 목록 조회](#전체-예약-목록-조회)
    - [예약 삭제](#예약-삭제)
  - 시간
    - [전체 시간 목록 조회](#전체-시간-목록-조회)
    - [시간 추가](#시간-추가)
    - [시간 삭제](#시간-삭제)
  - 테마 
    - [테마 추가](#테마-추가)
    - [테마 삭제](#테마-삭제)
- [페이지 라우팅](#페이지-라우팅)

---

## 사용자 API

### 예약 가능한 날짜 조회

예약 페이지 진입 시 달력에 표시할 예약 가능 날짜 목록을 반환합니다.

```
GET /reservations/available-dates
```

**Response** `200 OK`

```json
{
  "dates": [
    "2025-05-08",
    "2025-05-09",
    "2025-05-10"
  ]
}
```

---

### 전체 테마 목록 조회

예약 및 관리자 페이지 등에 표시할 전체 방탈출 테마 목록을 반환합니다.

```
GET /themes
```

**Response** `200 OK`

```json
[
  {
    "id": 1,
    "name": "망각의 정신병원",
    "description": "기억을 잃은 채 병원에서 깨어났다...",
    "thumbnailUrl": "/images/hospital.png"
  }
]
```

---

### 예약 가능한 시간 조회

날짜와 테마를 선택한 후, 해당 조건에서 각 시간대의 예약 가능 여부를 조회합니다.

```
GET /reservations/available-times?date={date}&themeId={themeId}
```

**Query Parameters**

| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| `date` | String (yyyy-MM-dd) | ✅ | 조회할 날짜 |
| `themeId` | Long | ✅ | 조회할 테마 ID |

**Response** `200 OK`

```json
[
  {
    "id": 1,
    "startAt": "10:00:00",
    "reserved": false
  },
  {
    "id": 2,
    "startAt": "12:00:00",
    "reserved": true
  },
  {
    "id": 3,
    "startAt": "14:00:00",
    "reserved": false
  }
]
```

> `reserved: true` 인 시간대는 이미 예약된 슬롯으로, 선택 불가 처리해야 합니다.

---

### 예약 생성

```
POST /reservations
```

**Request Body**

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `name` | String | ✅ | 예약자 이름 |
| `date` | String (yyyy-MM-dd) | ✅ | 예약 날짜 |
| `timeId` | Long | ✅ | 예약 시간 ID |
| `themeId` | Long | ✅ | 예약 테마 ID |

**Request 예시**

```json
{
  "name": "홍길동",
  "date": "2025-05-10",
  "timeId": 1,
  "themeId": 3
}
```

**Response** `201 Created`

```json
{
  "id": 15,
  "name": "홍길동",
  "date": "2025-05-10",
  "time": {
    "id": 1,
    "startAt": "10:00:00"
  },
  "theme": {
    "id": 3,
    "name": "망각의 정신병원",
    "description": "기억을 잃은 채 병원에서 깨어났다...",
    "thumbnailUrl": "/images/hospital.png"
  }
}
```

---

### 인기 테마 조회

최근 **7일** 기준 예약 수가 많은 상위 10개 테마를 순서대로 반환합니다.

```
GET /themes/popular
```

**Response** `200 OK`

```json
[
  {
    "id": 5,
    "name": "괴박사의 실험실",
    "description": "미친 과학자의 실험실에서 탈출하라!",
    "thumbnailUrl": "/images/lab.png"
  },
  {
    "id": 2,
    "name": "금서의 도서관",
    "description": "금지된 책이 가득한 도서관의 비밀...",
    "thumbnailUrl": "/images/library.png"
  }
]
```

> 예약이 없는 경우에도 테마 목록은 반환되며, 예약 수 기준 내림차순 정렬입니다.

---

## 관리자 API

### 전체 예약 목록 조회

```
GET /admin/reservations
```

**Response** `200 OK`

```json
[
  {
    "id": 1,
    "name": "홍길동",
    "date": "2025-05-10",
    "time": {
      "id": 2,
      "startAt": "14:00:00"
    },
    "theme": {
      "id": 1,
      "name": "망각의 정신병원",
      "description": "기억을 잃은 채 병원에서 깨어났다...",
      "thumbnailUrl": "/images/hospital.png"
    }
  }
]
```

---

### 예약 삭제

```
DELETE /reservations/{reservation-id}
```

**Path Variable**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `reservation-id` | Long | 삭제할 예약 ID |

**Response** `204 No Content`

---

### 전체 시간 목록 조회

```
GET /times
```

**Response** `200 OK`

```json
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

---

### 시간 추가

```
POST /times
```

**Request Body**

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `startAt` | String (HH:mm) | ✅ | 시작 시간 |

**Request 예시**

```json
{
  "startAt": "16:00"
}
```

**Response** `201 Created`

```json
{
  "id": 5,
  "startAt": "16:00:00"
}
```

---

### 시간 삭제

```
DELETE /times/{time-id}
```

**Path Variable**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `time-id` | Long | 삭제할 시간대 ID |

**Response** `204 No Content`

---

### 테마 추가

```
POST /themes
```

**Request Body**

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `name` | String | ✅ | 테마 이름 |
| `description` | String | ✅ | 테마 설명 |
| `thumbnailUrl` | String | ✅ | 썸네일 이미지 경로 |

**Request 예시**

```json
{
  "name": "우주 탈출",
  "description": "우주 정거장에서 산소가 떨어지고 있다...",
  "thumbnailUrl": "/images/space.png"
}
```

**Response** `201 Created`

```json
{
  "id": 10,
  "name": "우주 탈출",
  "description": "우주 정거장에서 산소가 떨어지고 있다...",
  "thumbnailUrl": "/images/space.png"
}
```

---

### 테마 삭제

```
DELETE /themes/{theme-id}
```

**Path Variable**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `theme-id` | Long | 삭제할 테마 ID |

**Response** `204 No Content`

---

## 페이지 라우팅

| URL | 설명 |
|-----|------|
| `GET /` | 홈 화면 (인기 테마 목록) |
| `GET /reservations` | 예약 화면 (날짜·테마·시간 선택) |
| `GET /admin` | 관리자 화면 (예약·시간·테마 관리) |
