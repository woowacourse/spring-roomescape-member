# 방탈출 예약 시스템 (Member)

방탈출 예약 및 테마 관리를 위한 백엔드 서비스입니다.

## 목차
1. [기능 목록](#기능-목록)
2. [API 명세](#api-명세)
    - [테마 (Theme)](#1-테마-theme)
    - [예약 시간 (Time)](#2-예약-시간-time)
    - [예약 (Reservation)](#3-예약-reservation)

---

## 기능 목록

### **테마 (Theme)**
- [x] 테마 관리: 추가, 조회, 삭제 기능 제공
- [x] 인기 테마 조회: 최근 7일간 예약 건수 기준 상위 10개 테마 집계

### **예약 시간 (Time)**
- [x] 예약 시간 관리: 추가, 조회, 삭제 기능 제공
- [x] 예약 가능 시간 조회: 특정 날짜와 테마에 대해 예약 가능한 시간 목록 반환

### **예약 (Reservation)**
- [x] 예약 생성: 사용자 이름, 날짜, 테마, 시간을 선택하여 예약
- [x] 예약 조회 및 취소: 전체 예약 목록 조회 및 특정 예약 취소 기능 제공
- [x] 중복 예약 방지: 동일한 날짜/시간/테마에 대해 중복 예약 불가

---

## API 명세

### 1. 테마 (Theme)

| 기능 | Method | Path | 설명 |
| --- | --- | --- | --- |
| 테마 목록 조회 | `GET` | `/themes` | 등록된 모든 테마 목록 반환 |
| 인기 테마 조회 | `GET` | `/themes/rank` | 최근 N일간 예약 순위 조회 (`days`, `limit` 필요) |
| 테마 추가 | `POST` | `/themes` | 새로운 테마 등록 |
| 테마 삭제 | `DELETE` | `/themes/{id}` | 특정 테마 삭제 |

#### 인기 테마 조회 응답 (Example)
```json
[
  {
    "rank": 1,
    "theme": { "id": 1, "name": "우테코 탈출", "description": "재미있어요", "imageUrl": "..." }
  }
]
```

### 2. 예약 시간 (Time)

| 기능 | Method | Path | 설명 |
| --- | --- | --- | --- |
| 시간 목록 조회 | `GET` | `/times` | 등록된 모든 예약 시간 목록 반환 |
| 예약 가능 시간 조회 | `GET` | `/times?themeId=1&date=2024-05-07` | 특정 테마/날짜의 예약 가능 여부 확인 |
| 시간 추가 | `POST` | `/times` | 새로운 예약 시간 등록 (`startAt`: "HH:mm") |
| 시간 삭제 | `DELETE` | `/times/{id}` | 특정 예약 시간 삭제 |

#### 예약 가능 시간 조회 응답 (Example)
```json
{
  "theme": { "id": 1, "name": "우테코 탈출", ... },
  "availableTimes": [
    { "id": 1, "startAt": "13:00", "available": true },
    { "id": 2, "startAt": "14:00", "available": false }
  ]
}
```

### 3. 예약 (Reservation)

| 기능 | Method | Path | 설명 |
| --- | --- | --- | --- |
| 예약 목록 조회 | `GET` | `/reservations` | 전체 예약 내역 조회 |
| 예약 생성 | `POST` | `/reservations` | 새로운 예약 등록 |
| 예약 취소 | `DELETE` | `/reservations/{id}` | 특정 예약 취소 |

#### 예약 생성 요청 (Example)
```json
{
  "name": "브라운",
  "date": "2024-05-07",
  "timeId": 1,
  "themeId": 1
}
```

#### 예약 목록 조회 응답 (Example)
```json
[
  {
    "id": 1,
    "name": "브라운",
    "date": "2024-05-07",
    "time": { "id": 1, "startAt": "13:00" },
    "theme": { "id": 1, "name": "우테코 탈출", ... }
  }
]
```
