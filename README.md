# API

## ADMIN
### 예약 추가 - POST /admin/reservations

**요청**
- Method: `POST`
- URL: `/admin/reservations`
- Body:
```json
{
  "name": "브라운",
  "date": "2026-05-04",
  "timeId": 1,
  "themeId": 1
}
```

**응답**
- Status: `200 OK`
- Body:
```json
{
  "id": 1,
  "name": "브라운",
  "date": "2026-05-04",
  "themeResponseDto": {
    "id": 1,
    "name": "방탈출 이름",
    "thumbnailUrl": "url",
    "description": "설명"
  },
  "timeDto": {
    "id": 1,
    "startAt": "10:00"
  }
}
```

**예외**
- 400: 필수 값 누락 (`name`, `date`, `timeId`, `themeId`) / 잘못된 날짜·시간 형식
- 404: 존재하지 않는 `timeId` 또는 `themeId`
- 409: 동일 날짜·시간·테마 중복 예약


### 예약 삭제 - DELETE /admin/reservations/{id}

**요청**
- Method: `DELETE`
- URL: `/admin/reservations/{id}`

**응답**
- Status: `200 OK`
- Body: (없음)

**예외**
- 404: 존재하지 않는 id

### 단일 예약 조회 - GET /admin/reservations/{id}

**요청**
- Method: `GET`
- URL: `/admin/reservations/{id}`

**응답**
- Status: `200 OK`
- Body:
```json
{
  "id": 1,
  "name": "브라운",
  "date": "2026-05-04",
  "themeResponseDto": {
    "id": 1,
    "name": "방탈출 이름",
    "thumbnailUrl": "url",
    "description": "설명"
  },
  "timeDto": {
    "id": 1,
    "startAt": "10:00"
  }
}
```

**예외**
- 404: 존재하지 않는 id

### 예약 목록 조회 - GET /admin/reservations

**요청**
- Method: `GET`
- URL: `/admin/reservations`

**응답**
- Status: `200 OK`
- Body:
```json
[
  {
    "id": 1,
    "name": "브라운",
    "date": "2026-05-04",
    "themeResponseDto": {
      "id": 1,
      "name": "방탈출 이름",
      "thumbnailUrl": "url",
      "description": "설명"
    },
    "timeDto": {
      "id": 1,
      "startAt": "10:00"
    }
  }
]
```

---

### 시간 추가 - POST /admin/times

**요청**
- Method: `POST`
- URL: `/admin/times`
- Body:
```json
{
  "startAt": "10:00"
}
```

**응답**
- Status: `200 OK`
- Body:
```json
{
  "id": 1,
  "startAt": "10:00"
}
```

**예외**
- 400: 필수 값(`startAt`) 누락 / 잘못된 시간 형식 (예: `"25:00"`, `"abc"`)
- 409: 동일한 시간 중복 등록


### 시간 삭제 - DELETE /admin/times/{id}

**요청**
- Method: `DELETE`
- URL: `/admin/times/{id}`

**응답**
- Status: `200 OK`
- Body: (없음)

**예외**
- 404: 존재하지 않는 id
- 409: 해당 시간을 사용 중인 예약이 존재

### 단일 시간 조회 - GET /admin/times/{id}

**요청**
- Method: `GET`
- URL: `/admin/times/{id}`

**응답**
- Status: `200 OK`
- Body:
```json
{
  "id": 1,
  "startAt": "10:00"
}
```

**예외**
- 404: 존재하지 않는 id


### 시간 목록 조회 - GET /admin/times

**요청**
- Method: `GET`
- URL: `/admin/times`

**응답**
- Status: `200 OK`
- Body:
```json
[
  {
    "id": 1,
    "startAt": "10:00"
  },
  {
    "id": 2,
    "startAt": "11:00"
  }
]
```

---

## 테마 관리

### 테마 추가 - POST /admin/themes

**요청**
- Method: `POST`
- URL: `/admin/themes`
- Body:
```json
{
  "name": "이름",
  "description": "설명",
  "thumbnailUrl": "url"
}
```

**응답**
- Status: `200 OK`
- Body:
```json
{
  "id": 1,
  "name": "이름",
  "thumbnailUrl": "url",
  "description": "설명"
}
```

**예외**
- 400: 이름 필수 / 이름 20자 초과 / 썸네일 URL 필수 / 올바르지 않은 URL 형식 / 설명 2자 미만 또는 200자 초과
- 409: 동일한 이름 테마 중복 등록


### 테마 삭제 - DELETE /admin/themes/{id}

**요청**
- Method: `DELETE`
- URL: `/admin/themes/{id}`

**응답**
- Status: `200 OK`
- Body: (없음)

**예외**
- 404: 존재하지 않는 id
- 409: 해당 테마를 사용 중인 예약이 존재

### 단일 테마 조회 - GET /admin/themes/{id}

**요청**
- Method: `GET`
- URL: `/admin/themes/{id}`

**응답**
- Status: `200 OK`
- Body:
```json
{
  "id": 1,
  "name": "이름",
  "thumbnailUrl": "url",
  "description": "설명"
}
```

**예외**
- 404: 존재하지 않는 id

### 테마 목록 조회 - GET /admin/themes

**요청**
- Method: `GET`
- URL: `/admin/themes`

**응답**
- Status: `200 OK`
- Body:
```json
[
  {
    "id": 1,
    "name": "이름",
    "thumbnailUrl": "url",
    "description": "설명"
  }
]
```

---

## USER

### 테마 목록 조회 - GET /themes

**요청**
- Method: `GET`
- URL: `/themes`

**응답**
- Status: `200 OK`
- Body:
```json
[
  {
    "id": 1,
    "name": "이름",
    "thumbnailUrl": "url",
    "description": "설명"
  }
]
```

### 단일 테마 조회 - GET /themes/{id}

**요청**
- Method: `GET`
- URL: `/themes/{id}`

**응답**
- Status: `200 OK`
- Body:
```json
{
  "id": 1,
  "name": "이름",
  "thumbnailUrl": "url",
  "description": "설명"
}
```

**예외**
- 404: 존재하지 않는 id

### 예약 가능 시간 조회 - GET /themes/{themeId}/times

**요청**
- Method: `GET`
- URL: `/themes/{themeId}/times?localDate=2026-05-04`
- Query Parameter: `localDate` (yyyy-MM-dd)

**응답**
- Status: `200 OK`
- Body:
```json
[
  {
    "id": 1,
    "startAt": "10:00",
    "alreadyBooked": false
  },
  {
    "id": 2,
    "startAt": "12:00",
    "alreadyBooked": true
  }
]
```

### 인기 테마 조회 - GET /themes/populars

**요청**
- Method: `GET`
- URL: `/themes/populars?limit=10&days=7`
- Query Parameter:
  - `limit`: 조회할 테마 수 (기본값 10, 최솟값 1, 최댓값 15)
  - `days`: 집계 기준 일수 (기본값 7, 최솟값 1, 최댓값 10)
  - `date` (선택): 집계 기준일 (yyyy-MM-dd). 미지정 시 서버의 오늘 날짜 사용

**응답**
- Status: `200 OK`
- Body:
```json
[
  {
    "id": 1,
    "name": "이름",
    "thumbnailUrl": "url",
    "description": "설명"
  }
]
```

**예외**
- 400: `limit` 1 미만 또는 15 초과 / `days` 1 미만 또는 10 초과

### 예약 추가 - POST /reservations

**요청**
- Method: `POST`
- URL: `/reservations`
- Body:
```json
{
  "name": "이름",
  "date": "2026-05-04",
  "timeId": 1,
  "themeId": 1
}
```

**응답**
- Status: `200 OK`
- Body:
```json
{
  "id": 1,
  "name": "이름",
  "date": "2026-05-04",
  "themeResponseDto": {
    "id": 1,
    "name": "방탈출 이름",
    "thumbnailUrl": "url",
    "description": "설명"
  },
  "timeDto": {
    "id": 1,
    "startAt": "10:00"
  }
}
```

**예외**
- 400: 필수 값 누락 (`name`, `date`, `timeId`, `themeId`) / 이름 20자 초과 / 잘못된 날짜 형식
- 404: 존재하지 않는 `timeId` 또는 `themeId`
- 409: 동일 날짜·시간·테마 중복 예약

---

# 방탈출 예약 관리 시스템 - 기능 요구사항

## 0. 개요

본 시스템은 방탈출 카페의 예약을 관리하는 웹 서비스다. **사용자**와 **관리자**는 서로 다른 API를 통해 시스템을 이용한다. (실제 서비스가 아니므로 로그인/인증은 없으며, 단순히 API 경로만 분리된다.)

## 1. 용어

- **테마**: 방탈출 컨텐츠. 이름, 설명, 사진을 가짐.
- **시간(타임슬롯)**: 예약 가능한 시각 단위. 예) 14:00, 16:00.
  > 본 문서에서 "시간"은 항상 "예약 가능한 시각 슬롯"을 의미한다.
- **예약**: 특정 날짜 + 시간 + 테마 + 예약자 이름 1건.
- **사용자**: 예약을 생성하는 일반 이용자.
- **관리자**: 시스템 운영 주체. 예약·시간·테마 전반을 관리한다.

---

# 사용자 기능

## U1. 메인 화면

- 진입점: **테마 목록**, **인기 테마**

## U2. 인기 테마 조회

- **최근 1주간 예약이 가장 많았던 테마 상위 10개**를 조회한다.
- 집계 기준: 오늘 기준으로 **어제부터 7일 전까지의 게임 날짜**를 가진 예약을 집계한다.
  - 예: 오늘이 5월 8일이면, 게임 날짜가 5월 1일 ~ 5월 7일인 예약을 집계.

## U3. 테마 목록

- 등록된 모든 테마를 조회한다.
- 각 테마의 간단한 정보(이름, 설명, 사진)를 표시한다.

## U4. 테마 상세 (예약 페이지)

- 테마 목록에서 특정 테마를 선택해 진입한다.
- **날짜를 선택**하면, 해당 (테마 + 날짜)에 대해 **예약 가능한 시간 슬롯**이 표시된다.
  - 이미 예약된 시간 슬롯은 선택 불가로 표시된다.

## U5. 예약 생성

- 예약 가능한 시간 슬롯을 클릭하면 다이얼로그가 열린다.
- **예약자 이름을 입력**하고 확정하면 예약이 생성된다.
- 예약 1건의 구성: `예약자 이름 + 날짜 + 시간 + 테마`.

---

# 관리자 기능

## A1. 메인 화면

- 진입점: **예약 관리**, **테마 관리**, **시간 관리**
- **이번 달 통계**: 총 예약 수, 총 매출, 인기 테마 Top 3

## A2. 예약 관리

### A2.1 예약 목록

- 예약자 이름, 날짜, 시간, 테마를 한눈에 확인할 수 있다.
- 정렬 기본값: 가까운 날짜/시간 순.

### A2.2 예약 상세

- 목록에서 한 건을 선택하면 상세 정보를 본다.
- 존재하지 않는 예약 → "존재하지 않는 예약입니다" 안내.

### A2.3 예약 등록

- 입력: 예약자 이름, 날짜, 시간, 테마.
- 성공 시 목록 화면으로 돌아가며 토스트로 안내한다.
- 실패 시 입력 화면에 머물고 사유를 안내한다.

### A2.4 예약 삭제

- 상세 화면에서 삭제할 수 있다.
- 삭제 전 확인 다이얼로그를 띄운다.

## A3. 시간 관리

### A3.1 시간 목록 / 상세 조회

- 등록된 모든 시간을 조회한다.

### A3.2 시간 등록

- 시작 시각을 입력해 등록한다.
- 초기 시드 데이터로 2시간 단위 슬롯 제공 (10:00, 12:00, 14:00 …).
- 동일 시각이 이미 있으면 등록 실패.

### A3.3 시간 삭제

- **이 시간을 사용 중인 예약이 있으면 삭제할 수 없다.**
- 삭제 전 확인 다이얼로그를 띄운다.

## A4. 테마 관리

### A4.1 테마 목록 / 상세 조회

- 이름, 설명, 사진을 조회한다.

### A4.2 테마 등록

- 입력: 이름, 설명, 사진.
- 같은 이름의 테마가 이미 있으면 등록 실패.

### A4.3 테마 삭제

- **이 테마를 사용 중인 예약이 있으면 삭제할 수 없다.**
- 삭제 전 확인 다이얼로그를 띄운다.

---

# 비즈니스 규칙 (모아보기)

## R1. 예약 등록 시 (사용자/관리자 공통)

| 규칙 | 위반 시 응답 |
|------|--------------|
| 이름, 날짜, 시간, 테마 모두 필수 | 항목별 안내 메시지 |
| 같은 (날짜 + 시간 + 테마)에 이미 예약이 있으면 불가 | "이미 존재하는 예약이 있습니다." |

## R2. 시간 / 테마 삭제 시

| 규칙 | 위반 시 응답 |
|------|--------------|
| 사용 중인 예약이 있으면 삭제 불가 | "예약이 존재하여 시간/테마를 삭제할 수 없습니다." |

## R3. 시간 / 테마 등록 시

| 규칙 | 위반 시 응답 |
|------|--------------|
| 시간: 같은 시각 중복 불가 | "이미 존재하는 시간 입니다." |
| 테마: 같은 이름 중복 불가 | "이미 존재하는 테마 이름입니다." |
