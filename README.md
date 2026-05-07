# 방탈출 사용자 예약

## 1단계 - 테마 도메인 추가

### 1. 테마 생성 기능

#### 구현
- [x] 구현 완료

#### 메서드 / URL

- POST /themes

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

- DELETE /themes/{id}

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
    id,
    startAt
  }
]
```

## 3단계 - 인기 테마 조회

### 1. 최근 1주 동안 예약이 많았던 테마 상위 10개 조회 기능

#### 구현
- [x] 구현 완료

#### 메서드 / URL

- GET /themes/popular

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
    id,
    name,
    description,
    thumbnailUrl
  }
]
```

## 추가 - 리팩토링

### 1. admin URL 분리

#### 구현
- [x] 구현 완료

관리자 전용 API를 `/admin` 하위 경로로 분리.

#### Theme

- 테마 생성: POST /admin/themes
- 테마 삭제: DELETE /admin/themes/{id}

#### ReservationTime

- 타임 생성: POST /admin/times
- 타임 삭제: DELETE /admin/times/{id}

#### Reservation

- 예약 전체 삭제: DELETE /admin/reservations
