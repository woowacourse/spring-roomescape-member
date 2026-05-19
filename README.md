# 방탈출 예약 관리

방탈출 예약, 예약 시간, 테마를 관리하는 Spring Boot 미션입니다.

## 프로젝트 구조

```text
.
├── BE   # Spring Boot 백엔드
└── FE   # 사용자/관리자 화면
```

## 기능 목록

### 예약 시간 관리

- 예약 시간 생성
- 예약 시간 목록 조회
- 날짜와 테마를 기준으로 이미 예약된 시간 표시
- 예약 시간 삭제
- 예약에서 참조 중인 예약 시간은 삭제할 수 없도록 검증

### 테마 관리

- 테마 생성
- 테마 목록 조회
- 인기 테마 조회
- 테마 삭제
- 테마 생성 시 이름, 설명, 썸네일 URL 입력
- 예약에서 참조 중인 테마는 삭제할 수 없도록 검증

### 예약 관리

- 예약 생성
- 관리자 예약 목록 조회
- 관리자 예약 삭제
- 예약 가능한 시간 목록 조회
- 예약 생성 시 예약자 이름, 예약 날짜, 예약 시간 ID, 테마 ID 입력
- 예약 시간 ID와 테마 ID를 통해 예약을 연결
- 존재하지 않는 예약 시간 ID나 테마 ID로 예약 생성 시 예외 발생
- 지나간 날짜 또는 현재보다 이전인 시간으로 예약 생성 시 예외 발생
- 같은 날짜, 시간, 테마에 이미 예약이 있으면 예외 발생

### 내 예약 관리

- 예약자 이름으로 본인의 예약 목록 조회
- 본인의 예약 날짜와 시간 변경
- 본인의 예약 취소
- 본인이 아닌 예약 변경 시 예외 발생
- 변경하려는 날짜와 시간이 이미 예약되어 있으면 예외 발생

### 예외 처리

- 예약 요청 값이 비어 있으면 `400 Bad Request` 반환
- 예약 시간 요청 값이 비어 있으면 `400 Bad Request` 반환
- 테마 요청 값이 비어 있으면 `400 Bad Request` 반환
- 존재하지 않는 예약 시간으로 예약하면 `404 Not Found` 반환
- 존재하지 않는 테마로 예약하면 `404 Not Found` 반환
- 예약에서 참조 중인 예약 시간을 삭제하면 `400 Bad Request` 반환
- 서비스 정책을 위반하면 `400 Bad Request` 반환
- 처리하지 못한 서버 오류는 상세 내용을 숨기고 `서버 내부 오류` 메시지 반환

## 에러 응답 형식

```json
{
  "message": "이미 예약된 시간입니다."
}
```

브라우저에서는 응답 본문의 `message` 값을 사용자에게 표시합니다.

## API 명세

### 1. 예약 API

#### 1.1 예약 생성

```http
POST /reservations
Content-Type: application/json

{
  "name": "홍길동",
  "date": "2026-05-04",
  "timeId": 1,
  "themeId": 1
}
```

응답: `201 Created`

```http
Location: /reservations/{id}
```

```json
{
  "id": 1,
  "name": "홍길동",
  "date": "2026-05-04",
  "time": {
    "id": 1,
    "startAt": "16:00",
    "alreadyBooked": false
  },
  "theme": {
    "id": 1,
    "name": "우테코 탈출",
    "description": "8기 크루들과 함께하는 즐거운 탈출",
    "thumbnail": "https://images.example.com/woowa.png"
  }
}
```

존재하지 않는 예약 시간 ID나 테마 ID로 요청하면 `404 Not Found`를 반환합니다.

#### 1.2 관리자 예약 목록 조회

```http
GET /reservations
Authorization: ADMIN
```

응답: `200 OK`

```json
[
  {
    "id": 1,
    "name": "홍길동",
    "date": "2026-05-04",
    "time": {
      "id": 1,
      "startAt": "16:00",
      "alreadyBooked": false
    },
    "theme": {
      "id": 1,
      "name": "우테코 탈출",
      "description": "8기 크루들과 함께하는 즐거운 탈출",
      "thumbnail": "https://images.example.com/woowa.png"
    }
  }
]
```

#### 1.3 관리자 예약 조건 조회

```http
GET /reservations?date=2026-05-04&themeId=1
Authorization: ADMIN
```

응답: `200 OK`

```json
[
  {
    "id": 1,
    "name": "홍길동",
    "date": "2026-05-04",
    "time": {
      "id": 1,
      "startAt": "16:00",
      "alreadyBooked": false
    },
    "theme": {
      "id": 1,
      "name": "우테코 탈출",
      "description": "8기 크루들과 함께하는 즐거운 탈출",
      "thumbnail": "https://images.example.com/woowa.png"
    }
  }
]
```

#### 1.4 관리자 예약 삭제

```http
DELETE /reservations/{id}
Authorization: ADMIN
```

응답: `204 No Content`

### 2. 내 예약 API

#### 2.1 내 예약 목록 조회

```http
GET /reservations/me?name=홍길동
```

응답: `200 OK`

```json
[
  {
    "id": 1,
    "name": "홍길동",
    "date": "2026-05-04",
    "time": {
      "id": 1,
      "startAt": "16:00",
      "alreadyBooked": false
    },
    "theme": {
      "id": 1,
      "name": "우테코 탈출",
      "description": "8기 크루들과 함께하는 즐거운 탈출",
      "thumbnail": "https://images.example.com/woowa.png"
    }
  }
]
```

#### 2.2 내 예약 변경

```http
PATCH /reservations/me/{id}?name=홍길동&date=2026-05-05&timeId=2
```

응답: `204 No Content`

본인의 예약이 아니거나 변경하려는 시간이 이미 예약되어 있으면 `400 Bad Request`를 반환합니다.

#### 2.3 내 예약 취소

```http
DELETE /reservations/me/{id}?name=홍길동
```

응답: `204 No Content`

### 3. 예약 시간 API

#### 3.1 예약 시간 생성

```http
POST /times
Content-Type: application/json

{
  "startAt": "16:00"
}
```

응답: `201 Created`

```http
Location: /times/{id}
```

```json
{
  "id": 1,
  "startAt": "16:00",
  "alreadyBooked": false
}
```

#### 3.2 예약 시간 목록 조회

```http
GET /times
```

응답: `200 OK`

```json
[
  {
    "id": 1,
    "startAt": "16:00",
    "alreadyBooked": false
  },
  {
    "id": 2,
    "startAt": "17:00",
    "alreadyBooked": false
  }
]
```

#### 3.3 예약 가능 시간 목록 조회

```http
GET /times?date=2026-05-04&themeId=1
```

응답: `200 OK`

```json
[
  {
    "id": 1,
    "startAt": "16:00",
    "alreadyBooked": true
  },
  {
    "id": 2,
    "startAt": "17:00",
    "alreadyBooked": false
  }
]
```

#### 3.4 예약 시간 삭제

```http
DELETE /times/{id}
```

응답: `204 No Content`

예약에서 참조 중인 예약 시간이면 `400 Bad Request`를 반환합니다.

### 4. 테마 API

#### 4.1 테마 생성

```http
POST /themes
Authorization: ADMIN
Content-Type: application/json

{
  "name": "잠실의 유령",
  "description": "잠실 캠퍼스에 나타나는 의문의 유령을 추적하는 공포 테마",
  "thumbnail": "https://images.example.com/ghost-jamsil.png"
}
```

응답: `201 Created`

```http
Location: /themes/{id}
```

```json
{
  "id": 3,
  "name": "잠실의 유령",
  "description": "잠실 캠퍼스에 나타나는 의문의 유령을 추적하는 공포 테마",
  "thumbnail": "https://images.example.com/ghost-jamsil.png"
}
```

#### 4.2 테마 목록 조회

```http
GET /themes
```

응답: `200 OK`

```json
[
  {
    "id": 1,
    "name": "우테코 탈출",
    "description": "8기 크루들과 함께하는 즐거운 탈출",
    "thumbnail": "https://images.example.com/woowa.png"
  },
  {
    "id": 2,
    "name": "코드 리뷰 지옥",
    "description": "끝나지 않는 리뷰의 굴레에서 벗어나세요",
    "thumbnail": "https://images.example.com/review.png"
  }
]
```

#### 4.3 테마 삭제

```http
DELETE /themes/{id}
Authorization: ADMIN
```

응답: `204 No Content`

예약에서 참조 중인 테마이면 `400 Bad Request`를 반환합니다.

#### 4.4 인기 테마 조회

```http
GET /themes?sortBy=popular&from=2026-05-01&to=2026-05-31&limit=2
```

응답: `200 OK`
