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
- [ ] 구현 완료

#### 메서드 / URL

- GET /times

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