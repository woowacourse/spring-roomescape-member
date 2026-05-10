## 기능 명세

### 기능 명세 체크리스트

#### 웹 사용자

- [x] **메인 화면**: 접속 시 전체 테마 목록을 확인할 수 있다.
- [x] **예약 페이지 이동**: 예약하기 버튼을 통해 예약 신청 페이지(`/reservations`)로 이동할 수 있다.
- [x] **예약 신청**: 예약 페이지에서 이름, 날짜, 테마, 예약 시간을 선택하여 예약을 완료할 수 있다.

#### REST API 클라이언트

- [x] **테마 (Theme)**
  - [x] `GET /themes`: 전체 테마 목록을 조회한다.
  - [x] `GET /themes/popular`: 인기가 많은 테마 목록을 조회한다.
  - [x] `GET /themes/{id}`: 특정 테마의 상세 정보를 조회한다.
- [x] **예약 (Reservation)**
  - [x] `POST /reservations`: 새로운 예약을 생성한다.
  - [x] `DELETE /reservations/{id}`: 특정 예약을 취소(삭제)한다.
- [x] **예약 시간 (Time)**
  - [x] `GET /times`: 등록된 모든 예약 시간 목록을 조회한다.
  - [x] `GET /times/reserved`: 특정 날짜와 테마에 대해 이미 예약된 시간들을 조회한다.

#### 관리자

- [x] **예약 내역 관리**: 서비스의 모든 예약 내역을 한눈에 조회할 수 있다 (`GET /admin/reservations`).
- [x] **테마 설정**:
  - [x] 새로운 방탈출 테마를 시스템에 등록할 수 있다 (`POST /admin/themes`).
  - [x] 더 이상 운영하지 않는 테마를 삭제할 수 있다 (`DELETE /admin/themes/{id}`).
- [x] **시간 설정**:
  - [x] 예약 가능한 시간대를 새롭게 추가할 수 있다 (`POST /admin/times`).
  - [x] 특정 예약 시간대를 시스템에서 제거할 수 있다 (`DELETE /admin/times/{id}`).

## API 명세

### 1. 관리자 (Admin)

#### 모든 예약 조회

```json
GET /admin/reservations

[
  {
    "id": 1,
    "name": "루드비코",
    "date": "2024-05-10",
    "time": {
      "id": 1,
      "startAt": "13:00"
    },
    "theme": {
      "id": 1,
      "name": "베네치아의 유령",
      "description": "공포/스릴러 테마입니다.",
      "thumbnailUrl": "https://example.com/image.png"
    }
  }
]
```

#### 예약 시간 추가

```json
POST /admin/times

{
  "startAt": "15:40"
}

// Response: 201 Created (Location: /times/{id})
```


#### 예약 시간 삭제

```json
DELETE /admin/times/{id}

// Response: 204 No Content
```


#### 테마 추가

```json
POST /admin/themes

{
  "name": "우테코 탈출",
  "description": "제한 시간 내에 미션을 완료하세요.",
  "thumbnailUrl": "https://example.com/image.png"
}

// Response: 201 Created (Location: /theme/{id})
```


#### 테마 삭제

```json
DELETE /admin/themes/{id}

// Response: 204 No Content
```


<br>

### 2. 테마 (Theme)

#### 전체 테마 목록 조회

```json
GET /themes

[
  {
    "id": 1,
    "name": "우테코 탈출",
    "description": "제한 시간 내에 미션을 완료하세요.",
    "thumbnailUrl": "https://example.com/image.png"
  }
]
```


#### 인기 테마 목록 조회

```json
GET /themes/popular

[
  {
    "id": 1,
    "name": "우테코 탈출",
    "description": "가장 인기 있는 테마입니다.",
    "thumbnailUrl": "https://example.com/image.png"
  }
]
```

#### 특정 테마 상세 조회

```json
GET /themes/{id}

{
  "id": 1,
  "name": "우테코 탈출",
  "description": "제한 시간 내에 미션을 완료하세요.",
  "thumbnailUrl": "https://example.com/image.png"
}
```


<br>

### 3. 예약 (Reservation)

#### 예약 생성

```json
POST /reservations

{
  "date": "2024-05-15",
  "timeId": 1,
  "themeId": 1,
  "name": "루드비코"
}

// Response: 201 Created (Location: /reservations/{id})
```


#### 예약 취소

```json
DELETE /reservations/{id}

// Response: 204 No Content
```

<br>

### 4. 예약 시간 (Time)

#### 전체 예약 시간 목록 조회

```json
GET /times

[
  {
    "id": 1,
    "startAt": "13:00"
  },
  {
    "id": 2,
    "startAt": "15:00"
  }
]
```

#### 예약된 시간 목록 조회

```json
GET /times/reserved?themeId=1&selectedDate=2024-05-15

[
  {
    "id": 1,
    "startAt": "13:00"
  }
]
```


<br>

### 5. 화면 (View)

#### 예약 페이지

```json
GET /reservations

// Response: reservation.html 렌더링
```
