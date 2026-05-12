# 방탈출 예약 관리

방탈출 예약, 예약 시간, 테마를 관리하는 Spring Boot 미션입니다.

## 프로젝트 구조

```text
.
├── BE   # Spring Boot 백엔드 (REST API 전용)
└── FE   # 사용자/관리자 화면 (Vanilla JS, 빌드 도구 없음)
```

## 실행 방법

### 사전 요구 사항

- JDK 21
- Node.js 18+ (FE 서버 실행용)

### 백엔드 실행

```bash
cd BE
./gradlew bootRun
```

### 프론트엔드 실행

```bash
cd FE
node dev-server.mjs
```

- 사용자 페이지: `http://localhost:3000/user/index.html` (또는 `http://localhost:3000/`)
- 관리자 페이지: `http://localhost:3000/admin/index.html`

### 테스트

```bash
cd BE
./gradlew test
```

## 기능 목록

### 예약 시간 관리

- 예약 시간 생성
- 예약 시간 목록 조회
- 예약 시간 삭제
- 예약에서 참조 중인 예약 시간은 삭제할 수 없도록 검증

### 테마 관리

- 테마 생성
- 테마 목록 조회
- 테마 삭제
- 테마 생성 시 이름, 설명, 썸네일 URL 입력

### 예약 관리

- 예약 생성
- 예약 목록 조회
- 예약 목록 이름 기반 조회
- 예약 삭제(관리자)
- 예약 삭제(본인)
- 예약 수정(날짜 & 시간)
- 예약 가능한 시간 목록 조회
- 예약 생성 시 예약자 이름, 예약 날짜, 예약 시간 ID, 테마 ID 입력
- 예약 시간 ID와 테마 ID를 통해 예약을 연결
- 존재하지 않는 예약 시간 ID나 테마 ID로 예약 생성 시 예외 발생

### 예외 처리

- 예약 요청 값이 비어 있으면 `400 Bad Request` 반환
- 예약 시간 요청 값이 비어 있으면 `400 Bad Request` 반환
- 존재하지 않는 예약 시간으로 예약하면 `404 Not Found` 반환
- 존재하지 않는 테마로 예약하면 `404 Not Found` 반환
- 예약에서 참조 중인 예약 시간을 삭제하면 `400 Bad Request` 반환
- 처리하지 못한 서버 오류는 `500 Internal Server Error` 반환

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
  "timeId": 1,
  "themeId": 1
}
```

존재하지 않는 예약 시간 ID나 테마 ID로 요청하면 `404 Not Found`를 반환합니다.

#### 1.2 예약 목록 조회

```http
GET /reservations
```

응답: `200 OK`

```json
[
  {
    "id": 1,
    "name": "홍길동",
    "date": "2026-05-04",
    "timeId": 1,
    "themeId": 1
  }
]
```

#### 1.3 예약 가능한 시간 목록 조회

```http
GET /reservations?date=2026-05-04&themeId=1
```

응답: `200 OK`

```json
[
  {
    "date": "2026-05-04",
    "timeId": 1,
    "themeId": 1,
    "available": true
  },
  {
    "date": "2026-05-04",
    "timeId": 2,
    "themeId": 1,
    "available": false
  }
]
```

#### 1.4 예약 삭제

```http
DELETE /reservations/{id}
```

응답: `204 No Content`

### 2. 예약 시간 API

#### 2.1 예약 시간 생성

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
  "startAt": "16:00"
}
```

#### 2.2 예약 시간 목록 조회

```http
GET /times
```

응답: `200 OK`

```json
[
  {
    "id": 1,
    "startAt": "16:00"
  },
  {
    "id": 2,
    "startAt": "17:00"
  }
]
```

#### 2.3 예약 시간 삭제

```http
DELETE /times/{id}
```

응답: `204 No Content`

예약에서 참조 중인 예약 시간이면 `400 Bad Request`를 반환합니다.

### 3. 테마 API

#### 3.1 테마 생성

```http
POST /themes
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

#### 3.2 테마 목록 조회

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

#### 3.3 테마 삭제

```http
DELETE /themes/{id}
```

응답: `204 No Content`

#### 3.4 인기 테마 조회

```http
GET /themes?start={2026-05-01}&end={2026-05-31}&orderBy={popularity}&limit={2}
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
