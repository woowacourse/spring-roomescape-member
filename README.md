# RoomEscape Member

### 예약

- [x] 사용자는 테마, 날짜, 시간를 선택하고 예약자 이름을 입력하여 예약한다.
- [x] 사용자는 자신의 이름으로 자신의 예약 목록을 조회할 수 있다.
- [x] 사용자는 자신의 예약을 취소할 수 있다.
    - [x] 단, 이미 지난 예약은 취소할 수 없다.
- [ ] 사용자는 자신의 예약의 날짜와 시간을 변경할 수 있다.
    - [ ] 단, 변경하려는 시간대가 이미 차있을 경우, 변경할 수 없다.
- [x] 예약은 현재 시각 이후만 가능하다.
- [x] 예약은 특정 테마와 예약 시간 조합을 기준으로 생성된다.
- [x] 같은 테마의 같은 날 같은 시간의 중복 예약은 불가능하다.

### 테마

- [x] 테마는 이름, 설명, 썸네일 이미지 URL을 필수로 가진다.
- [x] 관리자는 테마를 추가하거나 삭제할 수 있다.
- [x] 삭제된 테마는 사용자용 전체 테마 목록에서 제외된다.
- [x] 테마를 삭제해도 기존 예약 데이터는 유지된다.

### 예약 시간

- [x] 예약 시간은 `startAt`으로 관리한다.
- [x] 관리자는 예약 시간을 추가하거나 삭제할 수 있다.
    - [ ] 단, 예약이 존재하는 시간은 삭제할 수 없다.
- [x] 테마별 예약 가능 여부 조회 시 각 예약 시간의 예약 가능 상태를 함께 반환한다.

### 요구사항

- [ ] 500(서버 에러)이 사용자에게 노출되지 않도록 한다.
- [ ] 브라우저에서 에러 발생 시 사용자에게 의미 있는 메시지가 표시되어야 한다.
- [ ] 서비스 정책 위반, 유효하지 않은 입력, 존재하지 않는 리소스 등에 대해 의도된 에러 응답을 반환한다.

## 관련 문서

* [Playwright E2E 테스트 가이드](./e2e-test.md)

## API 공통 규칙

* Base URL: `/api`
* 관리자 API는 요청 헤더에 `role: ADMIN` 이 필요하다.
* 날짜는 `YYYY-MM-DD`, 시간은 `HH:mm:ss` 형식을 사용한다.
* 유효성 검증 실패, 중복 등록, 조회 실패 등의 예외는 에러 응답으로 반환된다.

## 비즈니스별 API 명세

### 1. 사용자 테마 조회

#### 전체 활성 테마 조회

**Request**

```http
GET /api/themes?page=0&size=10 HTTP/1.1
```

**Response**

```json
200 OK

{
  "responses": [
    {
      "id": 1,
      "name": "공포",
      "description": "공포 방탈출입니다.",
      "thumbnailImageUrl": "http://image.url/horror"
    },
    {
      "id": 2,
      "name": "추리",
      "description": "추리 방탈출입니다.",
      "thumbnailImageUrl": "http://image.url/detective"
    }
  ]
}
```

#### 인기 테마 조회

**Request**

```http
GET /api/themes/popular?startDate=2026-05-01&endDate=2026-05-06&limit=10 HTTP/1.1
```

**Response**

```json
200 OK

{
  "responses": [
    {
      "id": 1,
      "name": "공포",
      "description": "공포 방탈출입니다.",
      "thumbnailImageUrl": "http://image.url/horror"
    }
  ]
}
```

#### 특정 테마의 날짜별 예약 가능 시간 조회

**Request**

```http
GET /api/themes/1/times?date=2026-05-10 HTTP/1.1
```

**Response**

```json
200 OK

{
  "responses": [
    {
      "id": 1,
      "startAt": "10:00:00",
      "isReservable": true
    },
    {
      "id": 2,
      "startAt": "12:00:00",
      "isReservable": false
    }
  ]
}
```

### 2. 사용자 예약

#### 예약 생성

**Request**

```http
POST /api/reservations HTTP/1.1
Content-Type: application/json

{
  "name": "lim",
  "date": "2026-05-10",
  "themeId": 1,
  "timeId": 2
}
```

**Response**

```json
201 Created
Location: /api/reservations/1

{
"id": 1,
"name": "lim",
"date": "2026-05-10",
"time": {
"id": 2,
"startAt": "12:00:00"
},
"theme": {
"id": 1,
"name": "공포",
"description": "공포 방탈출입니다.",
"thumbnailImageUrl": "http://image.url/horror"
}
}
```

### 3. 관리자 테마 관리

#### 테마 추가

**Request**

```http
POST /api/admin/themes HTTP/1.1
Content-Type: application/json
role: ADMIN

{
  "name": "공포",
  "description": "공포 방탈출입니다.",
  "thumbnailImageUrl": "http://image.url/horror"
}
```

**Response**

```json
201 Created
Location: /api/admin/themes/1

{
"id": 1,
"name": "공포",
"description": "공포 방탈출입니다.",
"thumbnailImageUrl": "http://image.url/horror"
}
```

#### 테마 삭제

**Request**

```http
DELETE /api/admin/themes/1 HTTP/1.1
role: ADMIN
```

**Response**

```http
204 No Content
```

### 4. 관리자 예약 관리

#### 전체 예약 조회

**Request**

```http
GET /api/admin/reservations?page=0&size=10 HTTP/1.1
role: ADMIN
```

**Response**

```json
200 OK

{
  "responses": [
    {
      "id": 1,
      "name": "lim",
      "date": "2026-05-10",
      "time": {
        "id": 2,
        "startAt": "12:00:00"
      },
      "theme": {
        "id": 1,
        "name": "공포",
        "description": "공포 방탈출입니다.",
        "thumbnailImageUrl": "http://image.url/horror"
      }
    }
  ]
}
```

#### 관리자 예약 생성

**Request**

```http
POST /api/admin/reservations HTTP/1.1
Content-Type: application/json
role: ADMIN

{
  "name": "lim",
  "date": "2026-05-10",
  "themeId": 1,
  "timeId": 2
}
```

**Response**

```json
201 Created
Location: /api/admin/reservations/1

{
"id": 1,
"name": "lim",
"date": "2026-05-10",
"time": {
"id": 2,
"startAt": "12:00:00"
},
"theme": {
"id": 1,
"name": "공포",
"description": "공포 방탈출입니다.",
"thumbnailImageUrl": "http://image.url/horror"
}
}
```

#### 예약 삭제

**Request**

```http
DELETE /api/admin/reservations/1 HTTP/1.1
role: ADMIN
```

**Response**

```http
204 No Content
```

### 5. 관리자 예약 시간 관리

#### 예약 시간 전체 조회

**Request**

```http
GET /api/admin/times?page=0&size=10 HTTP/1.1
role: ADMIN
```

**Response**

```json
200 OK

{
  "responses": [
    {
      "id": 1,
      "startAt": "10:00:00"
    },
    {
      "id": 2,
      "startAt": "12:00:00"
    }
  ]
}
```

#### 예약 시간 추가

**Request**

```http
POST /api/admin/times HTTP/1.1
Content-Type: application/json
role: ADMIN

{
  "startAt": "10:00:00"
}
```

**Response**

```json
201 Created
Location: /api/admin/times/1

{
"id": 1,
"startAt": "10:00:00"
}
```

#### 예약 시간 삭제

**Request**

```http
DELETE /api/admin/times/1 HTTP/1.1
role: ADMIN
```

**Response**

```http
204 No Content
```

## 페이지 경로

* 메인 페이지: `/`
* 예약 페이지: `/reserve`
* 관리자 페이지: `/admin`
