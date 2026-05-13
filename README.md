# 방탈출 사용자 예약 설계

## 싸이클 1 

### 1 단계 - 테마 도메인 추가

- 규칙 1. 테마는 리소스인가?
    - 맞다.
    - 왜?
    - 예약에 종속적인 도메인 모델이지만,
      독립적인 멘탈 모델이니까
- 규칙 2.
- 규칙 3.
- 규칙 4. 테마의 API 명세는 어떻게 작성해야?
    - 추가 PUT /themes
    - 삭제 DELETE /themes
    - 조회 GET /themes

#### API 설계의 근거

> 사용자가 테마, 날짜에 따른 예약 가능한 시간을 조회할 때, 주 자원이 되는 시간을 중심으로 설계하였습니다.

테마는 독립적인 리소스이기에 단독 엔드포인트로,  
API 설계 기본 원칙에 따라 행위를 HTTP 메서드로 표현

#### 테마 도메인

- 이름
- 설명
- 썸네일 이미지 URL
- 시작 시간과 소요 시간은 동일하다고 가정한다.

#### 기능 목록

> 방탈출 게임에 '테마' 정보를 추가한다.  
> 테마는 이름, 설명, 썸네일 이미지 URL을 가진다.  
> 모든 테마의 시작 시간과 소요 시간은 동일하다고 가정한다.

-[x] record Theme
    -[x] String name
    -[x] String description
    -[x] String thumbnailUrl
    -[x] void validate
        -[x] Theme transientOf

> 예약에 테마 정보를 포함하도록 기존 코드를 변경한다.

-[x] class ThemeController
    -[x] ThemeRequest
    -[x] ThemeResponse

> 관리자가 테마를 추가·삭제할 수 있다.

-[x] class ThemeService
    - [x] Theme saveTime
    - [x] void removeTime
-[x] class ThemeRepository
-[x] class ThemeDao
-[x] record Reservation
    -[x] Theme theme

### 관리자가 테마를 추가, 삭제, 조회

### 추가 API

POST /themes

### 조회 API

GET /themes

### 삭제

DELETE /themes/{themeID}

---

## 추가 단계 - 사용자 클라이언트 화면 제작

[마이찬](https://github.com/Uechann)의 실험 기록을 참조해,  
정리된 API 명세를 기반으로 Claude 를 통해 React.js 클라이언트 제작

[클라이언트 저장소](https://github.com/Uechann/react-roomscape)

---

## 2단계 - 사용자 예약

### 1. 테마들의 모든 정보 조회 API

```json
GET /themes HTTP/1.1 
```

### 2. 사용자가 (날짜, 테마)를 통해 예약 가능 시간 조회 API

```json
GET /times?date=2026-05-04&themeId=1HTTP/1.1 
```

### 3. 사용자가 날짜, 테마, 예약 시간을 통해 예약 등록 API

```json
POST /reservations HTTP/1.1

{
  "themeId": 1,
  "date": "2026-05-03",
  "timeId": 1,
  "name": "브라운"
}
```

#### 요청과 응답은 1단계의 진행 과정을 통해 구체화

## 3단계 - 인기 테마 조회

### 1. 최근 1주 동안 예약이 많았던 테마 상위 10개를 조회한다.

- 반환값은 테마 10개.

```json
GET /themes?topCount=10&during=7 HTTP / 1.1 
```

---

## 싸이클 2

### 1단계 - 서비스 정책 적용
다음 정책을 만족하지 않는 요청은 거부한다.

공통 규칙
- 유효하지 않은 입력값(빈 이름, 잘못된 날짜 형식 등)을 거부한다. 400 

예약 규칙
- 예약이 존재하지 않는 경우 404 Not Found
- 자신의 예약이 아닌 예약에 접근하는 경우 403 Forbidden
- 이미 취소된 예약을 취소하는 경우 409 Conflict
- 이미 완료된 예약을 취소하는 경우 409 Conflict
- 지나간 날짜·시간에 대한 예약 변경, 취소가 불가능하다. 422 Unprocessable Entity
- 존재하지 않는 시간 ID에 대한 예약 변경일 경우 404 Not Found
- 같은 날짜+시간+테마에 이미 예약이 있으면 중복 예약을 거부한다. 409 Conflict
- 예약이 존재하는 시간을 삭제할 수 없다. 422 Unprocessable Entity

### 2단계 - 에러 응답 설계
서비스 정책 위반, 유효하지 않은 입력, 존재하지 않는 리소스 등에 대해 의도된 에러 응답을 반환한다.
500(서버 에러)이 사용자에게 노출되지 않도록 한다.

에러 응답에 담을 데이터

ErrorResponse
- HTTP 상태 코드 (statusCode)
- 에러 메시지 (errorMessage)
- 에러 발생 시각 (timeStamp)
- API 경로 (Url)
- 추적 ID (traceId)

CustomException
- ErrorCode

ErrorCode
- HTTP 상태
- ErrorMessage

### 3단계 - 내 예약 조회/변경/취소
사용자가 자신의 이름으로 본인의 예약 목록을 조회할 수 있다.
사용자가 본인의 예약을 취소할 수 있다.
사용자가 본인의 예약의 날짜·시간을 변경할 수 있다.
변경·취소 시 발생하는 에러 케이스(이미 지난 예약을 취소, 변경하려는 시간이 이미 차 있음 등)도 2단계의 규칙에 맞춰 처리한다.

#### 사용자가 자신의 예약 취소 API

사용자의 예약의 상태를 취소 상태로 변경한다.

이때 그러면 예약에 상태라는 필드가 추가되어야 한다.

예약 상태 (대기, 확정, 완료, 취소)

요청

```http request
PATCH http://localhost:8080/reservations/{reservationId}
{
	"status": "CANCELLED"
}
```

```http request
PATCH http://localhost:8080/reservations/{reservationId}/cancel
```

#### 예약 날짜·시간 변경 API

예약의 날짜, 시간을 변경한다.

존재하는 필드에 대해서만 변경한다.

요청

```http request
PATCH http://localhost:8080/reservations/{reservationId}
{
	"date": "2026-05-15",
	"timeId": 1
}
```

---

## API 명세서

### 전체 API 목록

| 리소스 | 메서드 | 경로 | 설명 |
|--------|--------|------|------|
| 예약 | GET | `/reservations` | 전체 예약 조회 |
| 예약 | GET | `/reservations?name=` | 이름으로 내 예약 조회 |
| 예약 | POST | `/reservations` | 예약 등록 |
| 예약 | PATCH | `/reservations/{id}/cancel` | 예약 취소 |
| 예약 | PATCH | `/reservations/{id}` | 예약 날짜·시간·테마 변경 |
| 예약 | DELETE | `/reservations/{id}` | 예약 삭제 |
| 시간 | GET | `/times` | 전체 시간 조회 |
| 시간 | GET | `/times?themeId=&date=` | 테마·날짜별 예약 가능 시간 조회 |
| 시간 | POST | `/times` | 시간 등록 |
| 시간 | DELETE | `/times/{id}` | 시간 삭제 |
| 테마 | GET | `/themes` | 전체 테마 조회 |
| 테마 | GET | `/themes?topCount=&during=` | 인기 테마 조회 |
| 테마 | POST | `/themes` | 테마 등록 |
| 테마 | DELETE | `/themes/{id}` | 테마 삭제 |

---

### 공통 에러 응답

모든 API는 오류 발생 시 아래 형식으로 응답한다.

```json
{
  "httpStatus": "NOT_FOUND",
  "errorMessage": "예약이 존재하지 않습니다.",
  "timeStamp": "2026-05-13T10:00:00",
  "apiUrl": "/reservations/1",
  "traceId": "abc-123"
}
```

---

### 예약 API

#### 전체 예약 조회

```
GET /reservations
```

**응답 200 OK**
```json
[
  {
    "id": 1,
    "name": "브라운",
    "date": "2026-05-15",
    "time": {
      "id": 1,
      "startAt": "10:00",
      "isAvailable": true
    },
    "theme": {
      "id": 1,
      "name": "공포의 방",
      "description": "무서운 방탈출",
      "thumbnailUrl": "https://example.com/thumbnail.jpg"
    },
    "status": "CONFIRMED"
  }
]
```

---

#### 내 예약 조회

```
GET /reservations?name={name}
```

| 파라미터 | 타입   | 필수 | 설명      |
|----------|--------|------|-----------|
| name     | String | O    | 예약자 이름 |

**응답 200 OK** — 전체 예약 조회와 동일한 형식

---

#### 예약 등록

```
POST /reservations
```

**요청 Body**
```json
{
  "name": "브라운",
  "date": "2026-05-15",
  "timeId": 1,
  "themeId": 1
}
```

| 필드    | 타입      | 필수 | 설명              |
|---------|-----------|------|-------------------|
| name    | String    | O    | 예약자 이름 (공백 불가) |
| date    | LocalDate | O    | 예약 날짜 (과거 불가)  |
| timeId  | Long      | O    | 예약 시간 ID       |
| themeId | Long      | O    | 테마 ID            |

**응답 201 Created** — 등록된 예약 단건 (전체 예약 조회 요소와 동일한 형식)

| 에러 상황 | 상태 코드 |
|-----------|-----------|
| 유효하지 않은 입력값 | 400 Bad Request |
| 과거 날짜 예약 시도 | 422 Unprocessable Entity |
| 시간 ID 없음 | 404 Not Found |
| 테마 ID 없음 | 404 Not Found |
| 같은 날짜·시간·테마에 예약 이미 존재 | 409 Conflict |

---

#### 예약 취소

```
PATCH /reservations/{reservationId}/cancel
```

| 경로 변수 | 타입 | 설명   |
|-----------|------|--------|
| reservationId | Long | 예약 ID |

**응답 204 No Content**

| 에러 상황 | 상태 코드 |
|-----------|-----------|
| 예약 없음 | 404 Not Found |
| 본인 예약이 아닌 경우 | 403 Forbidden |
| 이미 취소된 예약 | 409 Conflict |
| 이미 완료된 예약 | 409 Conflict |
| 이미 지난 예약 | 422 Unprocessable Entity |

---

#### 예약 날짜·시간·테마 변경

```
PATCH /reservations/{reservationId}
```

| 경로 변수 | 타입 | 설명   |
|-----------|------|--------|
| reservationId | Long | 예약 ID |

**요청 Body**
```json
{
  "date": "2026-05-20",
  "timeId": 2,
  "themeId": 1
}
```

| 필드    | 타입      | 필수 | 설명              |
|---------|-----------|------|-------------------|
| date    | LocalDate | O    | 변경할 날짜 (과거 불가) |
| timeId  | Long      | O    | 변경할 시간 ID     |
| themeId | Long      | O    | 변경할 테마 ID     |

**응답 200 OK** — 변경된 예약 단건 (전체 예약 조회 요소와 동일한 형식)

| 에러 상황 | 상태 코드 |
|-----------|-----------|
| 예약 없음 | 404 Not Found |
| 본인 예약이 아닌 경우 | 403 Forbidden |
| 과거 날짜로 변경 시도 | 422 Unprocessable Entity |
| 이미 지난 예약 | 422 Unprocessable Entity |
| 변경할 시간 ID 없음 | 404 Not Found |
| 같은 날짜·시간·테마에 예약 이미 존재 | 409 Conflict |

---

#### 예약 삭제

```
DELETE /reservations/{id}
```

| 경로 변수 | 타입 | 설명   |
|-----------|------|--------|
| id        | Long | 예약 ID |

**응답 204 No Content**

---

### 예약 시간 API

#### 전체 시간 조회

```
GET /times
```

**응답 200 OK**
```json
[
  {
    "id": 1,
    "startAt": "10:00",
    "isAvailable": true
  }
]
```

---

#### 테마·날짜별 예약 가능 시간 조회

```
GET /times?themeId={themeId}&date={date}
```

| 파라미터 | 타입      | 필수 | 설명     |
|----------|-----------|------|----------|
| themeId  | Long      | O    | 테마 ID  |
| date     | LocalDate | O    | 조회 날짜 |

**응답 200 OK** — `isAvailable` 필드로 예약 가능 여부를 구분한다.
```json
[
  {
    "id": 1,
    "startAt": "10:00",
    "isAvailable": true
  },
  {
    "id": 2,
    "startAt": "14:00",
    "isAvailable": false
  }
]
```

---

#### 시간 등록

```
POST /times
```

**요청 Body**
```json
{
  "startAt": "10:00"
}
```

| 필드    | 타입      | 필수 | 설명      |
|---------|-----------|------|-----------|
| startAt | LocalTime | O    | 시작 시간 |

**응답 201 Created**
```json
{
  "id": 1,
  "startAt": "10:00",
  "isAvailable": true
}
```

---

#### 시간 삭제

```
DELETE /times/{id}
```

| 경로 변수 | 타입 | 설명    |
|-----------|------|---------|
| id        | Long | 시간 ID |

**응답 204 No Content**

| 에러 상황 | 상태 코드 |
|-----------|-----------|
| 해당 시간에 예약이 존재하는 경우 | 422 Unprocessable Entity |

---

### 테마 API

#### 전체 테마 조회

```
GET /themes
```

**응답 200 OK**
```json
[
  {
    "id": 1,
    "name": "공포의 방",
    "description": "무서운 방탈출",
    "thumbnailUrl": "https://example.com/thumbnail.jpg"
  }
]
```

---

#### 인기 테마 조회

```
GET /themes?topCount={topCount}&during={during}
```

| 파라미터 | 타입 | 필수 | 설명                     |
|----------|------|------|--------------------------|
| topCount | Long | O    | 조회할 테마 수            |
| during   | Long | O    | 최근 N일 기준으로 집계    |

**응답 200 OK** — 전체 테마 조회와 동일한 형식 (예약 수 내림차순 정렬)

---

#### 테마 등록

```
POST /themes
```

**요청 Body**
```json
{
  "name": "공포의 방",
  "description": "무서운 방탈출",
  "thumbnailUrl": "https://example.com/thumbnail.jpg"
}
```

| 필드         | 타입   | 필수 | 설명              |
|--------------|--------|------|-------------------|
| name         | String | O    | 테마 이름 (공백 불가) |
| description  | String | O    | 테마 설명 (공백 불가) |
| thumbnailUrl | String | O    | 썸네일 URL (공백 불가) |

**응답 201 Created** — 등록된 테마 단건 (전체 테마 조회 요소와 동일한 형식)

---

#### 테마 삭제

```
DELETE /themes/{id}
```

| 경로 변수 | 타입 | 설명    |
|-----------|------|---------|
| id        | Long | 테마 ID |

**응답 204 No Content**

