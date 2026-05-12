# 테마 + 사용자 예약

## 1단계 - 테마 도메인 추가

### 1. 테마 생성 기능

#### 구현
- [x] 구현 완료

#### 메서드 / URL

- POST /admin/themes
  -테마 생성은 관리자만 수행할 수 있는 기능이므로, 일반 사용자가 접근하는 API와 명확히 구분하기 위해 `/admin` 하위 경로에 배치한다.

#### 요청

```json
{
  name,
  description,
  thumbnailUrl
}
```

#### 응답

- 201 created

```text
Location: /themes/{id}
```

### 2. 테마 삭제 기능

#### 구현
- [x] 구현 완료

#### 메서드 / URL

- DELETE /admin/themes/{id}
  - 테마 삭제 역시 관리자만 수행할 수 있는 기능이므로, 일반 사용자가 접근하는 API와 명확히 구분하기 위해 `/admin` 하위 경로에 배치한다.

#### 응답

- 204 No Contents

### 3. 예약에 테마 추가

#### 구현
- [x] 구현 완료

## 2단계 - 사용자 예약

### 1. 예약 가능한 시간 조회 기능

#### 구현
- [x] 구현 완료

#### 메서드 / URL

- GET /times/available-times
  -  예약 가능한 시간 조회는 사용 흐름 상 전체 시간 조회와 완전히 별개로 진행되기 때문에, `/times`의 하위 경로로 둔다.

#### 요청

- query parameter

```text
date=2026-05-08&themeId=1
```

#### 응답

- 200 ok

```json
[
  {
    startAt
  }
]
```

## 3단계 - 인기 테마 조회

### 1. 최근 1주 동안 예약이 많았던 테마 상위 10개 조회 기능

#### 구현
- [x] 구현 완료

#### 메서드 / URL

- GET /themes?popular=true
  -  전체 테마 목록 중 상위 10개 테마만을 조회하는 것이므로, 인기 여부를 나타내는 `popular=true` 쿼리 파라미터를 적용한다.

#### 요청

- query parameter

```text
period=7&limit=10
```

#### 응답

- 200 ok

```json
[
  {
    name,
    description,
    thumbnailUrl
  }
]
```

# 예약 변경/취소와 에러 처리

## 1단계 - 서비스 정책 적용

### 1. 시간 검증

#### 구현
- [ ] 구현 완료

#### 응답

- 422 Unprocessable Entity

```json
{
  "message": "시간이 유효하지 않습니다."
}
```

### 2. 날짜 검증

#### 구현
- [x] 구현 완료

#### 응답

- 422 Unprocessable Entity

```json
{
  "message": "예약 날짜가 유효하지 않습니다."
}
```

### 3. 중복 예약 검증

#### 구현
- [x] 구현 완료

#### 응답

- 409 Conflict

```json
{
  "message": "예약이 이미 존재합니다."
}
```

### 4. 예약 시간 삭제 검증

#### 구현
- [ ] 구현 완료

#### 응답

- 409 Conflict

```json
{
  "message": "해당 시간에 예약이 존재합니다."
}
```
### 5. 유효하지 않은 입력값 검증

#### 구현
- [ ] 구현 완료

#### 응답

- 유효하지 않은 예약자 이름
- 400 Bad Request

```json
{
  "message": "예약 요청 값이 올바르지 않습니다."
}
```

- 유효하지 않은 날짜 형식
- 400 Bad Request

```json

{
  "message": "예약 요청 값이 올바르지 않습니다."
}
```

- 유효하지 않은 시간 형식
- 400 Bad Request

```json
{
  "message": "예약 시간 형식이 유효하지 않습니다."
}
```

- 유효하지 않은 테마 이름
- 400 Bad Request

```json
{
  "message": "테마 이름이 유효하지 않습니다."
}
```

- 유효하지 않은 테마 설명
- 400 Bad Request

```json
{
  "message": "테마 설명이 유효하지 않습니다."
}
```

- 유효하지 않은 테마 썸네일 url
- 400 Bad Request

```json
{
  "message": "테마 썸네일 url이 유효하지 않습니다."
}
```

## 2단계 - 에러 응답 설계

#### 구현
- [ ] 구현 완료

## 3단계 - 내 예약 조회/변경/취소

### 1. 본인 예약 목록 조회 기능

#### 구현
- [x] 구현 완료

#### 메서드 / URL

- GET /reservations
  - 전체 예약 중 특정 예약자의 예약만 필터링 하는 것이기 때문에, 쿼리 파라미터로 이름을 넘긴다.

#### 요청

- query parameter

```text
name=브라운
```

#### 응답

- 200 Ok

```json
[
  {id, name, date, time, theme}
]
```

### 2. 본인 예약 취소 기능

#### 구현
- [ ] 구현 완료

#### 메서드 / URL

- POST /reservations/{id}/cancel
  - 비록 에약을 삭제하는 것이지만, 사용자 이름을 넘겨받아 검증을 해야하기 때문에 POST로 둔다.
    - 메시지 바디에 name을 담아 전달할 수 있다.

#### 요청

```json
{
  name
}
```

#### 응답

- 204 No Content

### 3. 본인 예약 변경 기능

#### 구현
- [ ] 구현 완료

#### 메서드 / URL

- PATCH /reservations/{id}
  - 예약 데이터 중 날짜와 시간이라는 일부 정보를 수정하는 것이기 때문에 PATCH로 둔다.
  - 또한 사용자 본인인지 검증을 해야하기 때문에, 요청 시 name을 함께 넘긴다.

#### 요청

```json
{
  name,
  date(optional),
  time(optional)
}
```

#### 응답

- 204 No Content
