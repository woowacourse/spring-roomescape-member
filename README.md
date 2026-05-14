# 웹 백엔드 레벨2 - 사이클2

## 미션

예약 서비스에 정책을 적용하고, 잘못된 요청에 대해 사용자가 이해할 수 있는 에러 응답을 반환한다.  
사용자는 본인의 예약을 조회하고, 날짜와 시간을 변경하거나 예약을 취소할 수 있다.

## 기능 목록

### 1단계 - 서비스 정책 적용

- [x] 지나간 날짜와 시간에 대한 예약 생성을 거부한다.
- [x] 같은 날짜, 시간, 테마에 이미 예약이 있으면 중복 예약을 거부한다.
- [x] 예약이 존재하는 예약 시간은 삭제할 수 없다.
- [x] 유효하지 않은 입력값을 거부한다.
  - [x] 빈 이름
  - [x] 비어있는 예약 날짜
  - [x] 비어있는 예약 시간
  - [x] 비어있는 테마
  - [x] 잘못된 날짜/시간 형식

### 2단계 - 에러 응답 설계

- [x] 서비스 정책 위반, 입력값 오류, 존재하지 않는 리소스에 대해 의도한 상태 코드를 반환한다.
- [x] 사용자에게 500 에러가 직접 노출되지 않도록 예외 응답을 통합한다.
- [x] 에러 응답 본문에 에러 이름과 사용자 메시지를 포함한다.
- [x] 브라우저에서 에러 메시지를 사용자에게 표시한다.

### 3단계 - 내 예약 조회/변경/취소

- [x] 사용자가 이름으로 본인의 예약 목록을 조회할 수 있다.
- [x] 사용자가 본인의 예약 날짜와 시간을 변경할 수 있다.
- [x] 사용자가 본인의 예약을 취소할 수 있다.
- [x] 지난 예약 변경을 거부한다.
- [x] 지난 예약 취소를 거부한다.
- [x] 이미 예약된 시간으로 변경하는 요청을 거부한다.
- [x] 존재하지 않거나 이름이 일치하지 않는 예약 변경을 거부한다.
- [x] 존재하지 않거나 이름이 일치하지 않는 예약 취소는 성공 응답으로 처리한다.

## API 명세

### 테마

#### 전체 테마 조회

```http
GET /themes
```

**Response 200 OK**

```json
{
  "themes": [
    {
      "id": 1,
      "name": "공포의 방",
      "description": "심장이 멎을 듯한 공포 체험",
      "thumbnail": "/images/공포.png"
    }
  ]
}
```

#### 인기 테마 조회

```http
GET /themes/popular?days=7&limit=10
```

**Response 200 OK**

```json
{
  "themes": [
    {
      "id": 1,
      "name": "공포의 방",
      "description": "심장이 멎을 듯한 공포 체험",
      "thumbnail": "/images/공포.png",
      "rank": 1
    }
  ]
}
```

#### 특정 테마의 예약 가능 시간 조회

```http
GET /themes/{id}/available-times?date=2026-05-20
```

**Response 200 OK**

```json
{
  "times": [
    {
      "id": 1,
      "startAt": "10:00",
      "isAvailable": true
    },
    {
      "id": 2,
      "startAt": "12:00",
      "isAvailable": false
    }
  ]
}
```

### 예약 시간

#### 예약 시간 목록 조회

```http
GET /times
```

**Response 200 OK**

```json
{
  "times": [
    {
      "id": 1,
      "startAt": "10:00"
    }
  ]
}
```

#### 예약 시간 생성

```http
POST /admin/times
Content-Type: application/json
```

**Request**

```json
{
  "startAt": "10:00"
}
```

**Response 201 Created**

```json
{
  "id": 1,
  "startAt": "10:00"
}
```

#### 예약 시간 삭제

```http
DELETE /admin/times/{id}
```

**Response 204 No Content**

### 관리자 테마

#### 테마 생성

```http
POST /admin/themes
Content-Type: application/json
```

**Request**

```json
{
  "name": "공포의 방",
  "description": "심장이 멎을 듯한 공포 체험",
  "thumbnail": "/images/공포.png"
}
```

**Response 201 Created**

```json
{
  "id": 1,
  "name": "공포의 방",
  "description": "심장이 멎을 듯한 공포 체험",
  "thumbnail": "/images/공포.png"
}
```

#### 테마 삭제

```http
DELETE /admin/themes/{id}
```

**Response 204 No Content**

### 예약

#### 전체 예약 조회

```http
GET /reservations?page=0&size=10
```

**Response 200 OK**

```json
{
  "reservations": [
    {
      "id": 1,
      "name": "레서",
      "date": "2026-05-20",
      "time": {
        "id": 1,
        "startAt": "10:00"
      },
      "theme": {
        "id": 1,
        "name": "공포의 방",
        "description": "심장이 멎을 듯한 공포 체험",
        "thumbnail": "/images/공포.png"
      }
    }
  ]
}
```

#### 사용자 예약 조회

```http
GET /reservations?name=레서&page=0&size=10
```

**Response 200 OK**

```json
{
  "reservations": [
    {
      "id": 1,
      "name": "레서",
      "date": "2026-05-20",
      "time": {
        "id": 1,
        "startAt": "10:00"
      },
      "theme": {
        "id": 1,
        "name": "공포의 방",
        "description": "심장이 멎을 듯한 공포 체험",
        "thumbnail": "/images/공포.png"
      }
    }
  ]
}
```

#### 예약 생성

```http
POST /reservations
Content-Type: application/json
```

**Request**

```json
{
  "name": "레서",
  "date": "2026-05-20",
  "timeId": 1,
  "themeId": 1
}
```

**Response 201 Created**

```json
{
  "id": 1,
  "name": "레서",
  "date": "2026-05-20",
  "time": {
    "id": 1,
    "startAt": "10:00"
  },
  "theme": {
    "id": 1,
    "name": "공포의 방",
    "description": "심장이 멎을 듯한 공포 체험",
    "thumbnail": "/images/공포.png"
  }
}
```

#### 사용자 예약 변경

```http
PATCH /reservations/{id}?name=레서
Content-Type: application/json
```

**Request**

```json
{
  "date": "2026-05-21",
  "timeId": 2
}
```

**Response 200 OK**

```json
{
  "id": 1,
  "name": "레서",
  "date": "2026-05-21",
  "time": {
    "id": 2,
    "startAt": "12:00"
  },
  "theme": {
    "id": 1,
    "name": "공포의 방",
    "description": "심장이 멎을 듯한 공포 체험",
    "thumbnail": "/images/공포.png"
  }
}
```

#### 사용자 예약 취소

```http
DELETE /reservations/{id}?name=레서
```

**Response 204 No Content**

존재하지 않거나 이름이 일치하지 않는 예약을 취소하는 경우에도 204를 반환한다.

## 에러 응답 명세

### 응답 형식

```json
{
  "name": "DUPLICATE_RESERVATION",
  "message": "이미 예약된 시간입니다."
}
```

### 에러 코드

| 상황 | 상태 코드 | name | message |
| --- | --- | --- | --- |
| 입력값이 올바르지 않은 경우 | 400 | INVALID_INPUT | 입력값이 올바르지 않습니다. |
| 지원하지 않는 HTTP 메서드 | 405 | METHOD_NOT_ALLOWED | 지원하지 않는 요청 방식입니다. |
| 정적 리소스를 찾을 수 없는 경우 | 404 | RESOURCE_NOT_FOUND | 요청한 리소스를 찾을 수 없습니다. |
| 존재하지 않는 예약 | 404 | RESERVATION_NOT_FOUND | 존재하지 않는 예약입니다. |
| 중복 예약 | 409 | DUPLICATE_RESERVATION | 이미 예약된 시간입니다. |
| 지난 날짜/시간 예약 생성 | 400 | PAST_RESERVATION | 지난 시간은 예약할 수 없습니다. |
| 지난 예약 변경 | 409 | PAST_RESERVATION_UPDATE | 지난 예약은 변경할 수 없습니다. |
| 지난 예약 취소 | 409 | PAST_RESERVATION_DELETE | 지난 예약은 취소할 수 없습니다. |
| 존재하지 않는 예약 시간 | 404 | RESERVATION_TIME_NOT_FOUND | 존재하지 않는 예약 시간입니다. |
| 예약이 존재하는 시간 삭제 | 409 | RESERVATION_TIME_IN_USE | 예약이 존재하는 시간입니다. |
| 존재하지 않는 테마 | 404 | THEME_NOT_FOUND | 존재하지 않는 테마입니다. |

### 입력값 검증 실패 예시

유효성 검증 실패 시 `name`은 `INVALID_INPUT`으로 반환하고, `message`에는 검증 메시지를 담는다.

```json
{
  "name": "INVALID_INPUT",
  "message": "이름은 비어있을 수 없습니다"
}
```
