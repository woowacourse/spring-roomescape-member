# spring-roomescape-member

## 기능 목록

### 예약 관리

[사이클 1]

- 사용자는 이름, 날짜 ID, 시간 ID, 테마 ID를 입력해 예약을 생성한다.
- 예약 생성 시 이름이 비어 있으면 예외를 반환한다.
- 예약 생성 시 날짜, 시간, 테마 값이 누락되면 예외를 반환한다.
- 관리자는 전체 예약 목록을 조회한다.
- 관리자는 예약 ID로 예약을 삭제한다.

[사이클 2]

- 사용자는 자신의 이름을 통해 예약 목록을 조회한다.
- 사용자가 본인의 예약을 취소할 수 있다.
    - 예약일 하루 전 자정까지 예약을 취소할 있다.
- 사용자가 본인의 예약 날짜, 시간을 변경할 수 있다.
    - 예약일 하루 전 자정까지 예약 날짜, 시간을 변경할 수 있다.
    - 변경하고자 하는 날짜, 시간에 이미 예약이 있을 경우에는 변경할 수 없다.
- 지나간 날짜, 시간에 대한 예약 생성을 시도하면 예외를 반환한다.
- 같은 날짜, 시간, 테마에 이미 예약이 존재하면 중복 예약으로 간주하여 예외를 반환한다.

### 예약 날짜 관리

- 사용자는 예약 가능한 날짜 목록을 조회한다.
- 관리자는 전체 예약 날짜 목록을 조회한다.
- 관리자는 예약 날짜를 추가한다.
- 관리자는 예약 날짜를 삭제한다.
- 존재하지 않는 날짜를 삭제하려 하면 예외를 반환한다.
- 이미 예약에 사용 중인 날짜를 삭제하려 하면 예외를 반환한다.

### 예약 시간 관리

- 사용자는 특정 날짜와 테마에 대한 예약 가능 시간 목록을 조회한다.
- 관리자는 전체 예약 시간 목록을 조회한다.
- 관리자는 예약 시간을 추가한다.
- 관리자는 예약 시간을 삭제한다.
- 예약 시간 입력이 없거나 형식이 올바르지 않으면 예외를 반환한다.
- 존재하지 않는 시간을 삭제하려 하면 예외를 반환한다.
- 이미 예약에 사용 중인 시간을 삭제하려 하면 예외를 반환한다.

### 테마 관리

- 사용자는 전체 테마 목록을 조회한다.
- 사용자는 예약 인기 테마 랭킹을 조회한다.
- 관리자는 전체 테마 목록을 조회한다.
- 관리자는 테마를 추가한다.
- 관리자는 테마를 삭제한다.
- 예약 생성 시 테마 값이 누락되면 예외를 반환한다.
- 존재하지 않는 테마를 삭제하려 하면 예외를 반환한다.
- 이미 예약에 사용 중인 테마를 삭제하려 하면 예외를 반환한다.

### 공통 예외 처리

[사이클 1]

- 관리자 API는 `X-ADMIN-TOKEN` 헤더가 올바르지 않으면 `401 Unauthorized`를 반환한다.
- 비즈니스 예외는 `{"code": "...", "message": "..."}` 형식으로 반환한다.
- 처리하지 못한 서버 예외는 공통 에러 응답으로 반환한다.

[사이클 2]

- 행동까지 명시한 에러 응답 객체로 수정한다.
- 비즈니스 예외, 잘못된 요청은 4XX로 예외 처리한다.
- 예측 못한 예외, DB 예외와 같은 서버 책임의 에러는 5XX로 예외 처리한다.
- 에러 메시지에 개인정보나 내부 구현 정보가 포함될 수 있다면 추상화해서 반환한다.

## 자기 API 명세

### 공통 규칙

- Base URL: `/`
- 관리자 API 헤더: `X-ADMIN-TOKEN: {admin-token}`
- Content-Type: `application/json`

### 화면 경로

#### `GET /`

- 설명: 사용자 예약 페이지 반환
- 응답: `times` 뷰 렌더링

#### `GET /admin`

- 설명: 관리자 페이지 반환
- 응답: `admin` 뷰 렌더링

### 예약

#### `GET /admin/reservations`

- 설명: 전체 예약 조회
- 인증: 관리자
- 응답 `200 OK`

```json
[
  {
    "id": 1,
    "name": "보예",
    "date": "2026-05-01",
    "time": {
      "id": 1,
      "startAt": "10:00"
    },
    "theme": {
      "id": 1,
      "name": "공포",
      "content": "오금이 저리는 공포입니다.",
      "url": "/themes/scary"
    }
  }
]
```

#### `POST /reservations`

- 설명: 예약 생성
- 요청 본문

```json
{
  "name": "쿠키",
  "dateId": 1,
  "timeId": 2,
  "themeId": 3
}
```

- 응답 `201 Created`

```json
{
  "id": 29,
  "name": "쿠키",
  "date": "2026-05-01",
  "time": "11:00",
  "theme": {
    "name": "청춘물",
    "content": "학교 배경인 테마 입니다.",
    "url": "/themes/youth"
  }
}
```

#### `DELETE /admin/reservations/{id}`

- 설명: 예약 삭제
- 인증: 관리자
- 응답 `204 No Content`

#### `GET /reservations?name={name}`

- 설명: 사용자 이름으로 예약 목록 조회
- 응답 `200 OK`

```json
[
  {
    "id": 1,
    "name": "보예",
    "date": "2026-05-05",
    "time": {
      "id": 1,
      "startAt": "10:00"
    },
    "theme": {
      "id": 1,
      "name": "공포",
      "content": "오금이 저리는 공포입니다.",
      "url": "/themes/scary"
    }
  }
]
```

#### `DELETE /reservations/{id}`

- 설명: 사용자 본인 예약 취소
- 응답 `204 No Content`

#### `PATCH /reservations/{id}`

- 설명: 사용자 본인 예약 날짜 및 시간 변경
- 요청 본문

```json
{
  "dateId": 1,
  "timeId": 2
}
```

- 응답 `200 OK`

```json

{
  "id": 1,
  "name": "보예",
  "date": "2026-05-05",
  "time": {
    "id": 1,
    "startAt": "10:00"
  },
  "theme": {
    "id": 1,
    "name": "공포",
    "content": "오금이 저리는 공포입니다.",
    "url": "/themes/scary"
  }
}
```

### 예약 날짜

#### `GET /reservation-dates`

- 설명: 사용자용 예약 날짜 목록 조회
- 응답 `200 OK`

```json
[
  {
    "id": 1,
    "playDay": "2026-05-01"
  }
]
```

#### `GET /admin/reservation-dates`

- 설명: 관리자용 예약 날짜 목록 조회
- 인증: 관리자
- 응답 `200 OK`

```json
[
  {
    "id": 1,
    "playDay": "2026-05-01"
  }
]
```

#### `POST /admin/reservation-dates`

- 설명: 예약 날짜 추가
- 인증: 관리자
- 요청 본문

```json
{
  "playDay": "2026-05-10"
}
```

- 응답 `201 Created`

```json
{
  "id": 10,
  "playDay": "2026-05-10"
}
```

#### `DELETE /admin/reservation-dates/{id}`

- 설명: 예약 날짜 삭제
- 인증: 관리자
- 응답 `204 No Content`

### 예약 시간

#### `GET /times?themeId={themeId}&dateId={dateId}`

- 설명: 특정 테마와 날짜의 예약 가능 시간 조회
- 응답 `200 OK`

```json
[
  {
    "timeId": 1,
    "startAt": "10:00",
    "available": false
  },
  {
    "timeId": 2,
    "startAt": "11:00",
    "available": true
  }
]
```

#### `GET /admin/times`

- 설명: 전체 예약 시간 목록 조회
- 인증: 관리자
- 응답 `200 OK`

```json
[
  {
    "id": 1,
    "startAt": "10:00"
  }
]
```

#### `POST /admin/times`

- 설명: 예약 시간 추가
- 인증: 관리자
- 요청 본문

```json
{
  "startAt": "18:00"
}
```

- 응답 `201 Created`

```json
{
  "id": 6,
  "startAt": "18:00"
}
```

#### `DELETE /admin/times/{id}`

- 설명: 예약 시간 삭제
- 인증: 관리자
- 응답 `204 No Content`

### 테마

#### `GET /themes`

- 설명: 전체 테마 목록 조회
- 응답 `200 OK`

```json
[
  {
    "id": 1,
    "name": "공포",
    "content": "오금이 저리는 공포입니다.",
    "url": "/themes/scary"
  }
]
```

#### `GET /themes/rank`

- 설명: 인기 테마 랭킹 조회
- 응답 `200 OK`

```json
[
  {
    "id": 1,
    "name": "공포",
    "url": "/themes/scary"
  }
]
```

#### `GET /admin/themes`

- 설명: 관리자용 테마 목록 조회
- 인증: 관리자
- 응답 `200 OK`

```json
[
  {
    "id": 1,
    "name": "공포",
    "content": "오금이 저리는 공포입니다.",
    "url": "/themes/scary"
  }
]
```

#### `POST /admin/themes`

- 설명: 테마 추가
- 인증: 관리자
- 요청 본문

```json
{
  "name": "추리",
  "content": "단서를 조합해 탈출하는 테마입니다.",
  "url": "/themes/detective"
}
```

- 응답 `201 Created`

```json
{
  "id": 13,
  "name": "추리",
  "content": "단서를 조합해 탈출하는 테마입니다.",
  "url": "/themes/detective"
}
```

#### `DELETE /admin/themes/{id}`

- 설명: 테마 삭제
- 인증: 관리자
- 응답 `204 No Content`
