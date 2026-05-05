# 방탈출 사용자 예약

## 기능 목록

### **테마 (Theme)**

- [x] 테마는 이름, 설명, 썸네일 URL를 가진다.
- [x] 관리자는 테마를 추가할 수 있다.
- [x] 관리자는 테마를 삭제할 수 있다.
- [ ] 테마 삭제 시 해당 테마의 기존 예약이 존재하면 삭제 요청을 거부하거나 처리 정책에 따라 제한한다.

### **예약 시간 (Time)**

- [ ] 전체 운영 시간에서 이미 예약된 시간을 제외한 예약 가능 시간 목록을 반환한다
    - [ ] 예약된 내역이 없다면 10시부터 18시까지의 전체 운영 시간을 반환한다
    - [ ] 해당 날짜와 테마의 모든 시간이 예약되었다면 빈 목록을 반환한다

### **예약 (Reservation)**

- [ ] 사용자는 `날짜`, `테마`, `시간`을 선택하고 `예약자 이름` 을 입력하여 예약한다.
- [ ] 동일한 `날짜 + 시간`이더라도 `테마`가 다르면 중복 예약이 가능하다.
- [ ] 동일한 `날짜 + 시간 + 테마`에 대해서는 단 하나의 예약만 존재해야 한다.
- [ ] 사용자 이름 입력 시 중복은 허용한다. (동명이인은 존재하지 않는다고 가정한다.)

### **조회 및 통계**

- [ ] 날짜와 테마 정보를 입력받아, 관리자가 등록한 시간 중 예약이 점유되지 않은 예약 가능 시간 목록을 반환한다.
- [ ] 현재 날짜 기준, 지난 7일간의 이용일(게임 날짜)을 기준으로 예약 건수가 많은 상위 10개 테마를 집계하여 반환한다.
    - [ ] 예약 횟수가 동일한 경우의 테마 이름순으로 정렬하여 제공한다.

## API 명세

---

## 1. 테마 (Theme)

### 1.1 테마 목록 조회
- **Method / Path:** `GET /themes`
- **Response (200 OK):**
```json
[
  {
    "themeId": 1,
    "themeName": "귀신의 집",
    "description": "무서워요",
    "thumbnailImageUrl": "/resources/image/..." 
  },
  {
    "themeId": 2,
    "themeName": "물고기",
    "description": "어푸",
    "thumbnailImageUrl": "/resources/image/..." 
  }
]
```

### 1.2 인기 테마 조회
- **Method / Path:** `GET /themes`
- **Query Parameter:** `sort=popular&days=7&limit=10`
- **Description:** 당일을 제외하고 계산된 통계를 제공.
- **Response (200 OK):**
```json
[
  {
    "rank": 1,
    "themeId": 1,
    "themeName": "Hello"
  },
  {
    "rank": 2,
    "themeId": 2,
    "themeName": "World"
  }
]
```

### 1.3 테마 추가
- **Method / Path:** `POST /themes`
- **Request:**
```json
{
  "themeName": "귀신의 집",
  "description": "무서워요",
  "thumbnailImageUrl": "/resources/image/..." 
}
```
- **Response (201 Created):**
```json
{
  "themeId": 1,
  "themeName": "귀신의 집",
  "description": "무서워요",
  "thumbnailImageUrl": "/resources/image/..."
}
```

### 1.4 테마 삭제
- **Method / Path:** `DELETE /themes/{themeId}`
- **Response (204 No Content):** `Body 없음`


---


## 2. 예약 (Reservation)

### 2.1 예약 가능 시간 조회
- **Method / Path:** `GET /times`
- **Query Parameter:** `date=2026-05-01&themeId=1&avaiable`
- **Response (200 OK):**
```json
{
  "themeId": 1,
  "themeName": "Hello",
  "times": [
    {
      "id": 1,
      "startAt": "15:00",
      "isAvailable": true
    },
    {
      "id": 2,
      "startAt": "16:00",
      "isAvailable": false
    }
  ]
}
```
- **Error Response (400 Bad Request):** 예약 가능한 날짜가 아니거나(과거/초과 범위), 존재하지 않는 테마인 경우.
```json
{
  "errorMessage": "error"
}
```

### 2.2 예약 생성
- **Method / Path:** `POST /reservations`
- **Request:**
```json
{
  "userName": "고래",
  "date": "2026-05-01",
  "timeId": 1,
  "themeId": 1
}
```
- **Response (201 Created):**
```json
{
  "reservationId": 1,
  "userName": "고래",
  "date": "2026-05-01",
  "timeId": 1,
  "themeId": 1
}
```
- **Error Response (400 Bad Request):** 동시성 문제 등으로 예약이 불가능한 경우.
```json
{
  "errorMessage": "error"
}
```
