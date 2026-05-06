# 방탈출 사용자 예약

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

- GET /times?available=true
  -  전체 시간 목록 중 특정 날짜와 테마에 대해 이용 가능한 시간만 조회하는 기능, 즉 시간에 대한 필터링이므로, 이용 가능 여부를 나타내는 `available=true` 쿼리 파라미터를 적용한다.

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
period=7d&limit=10
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

