# 방탈출 예약 시스템

방탈출 카페의 예약을 관리하고, 사용자가 직접 예약할 수 있는 웹 애플리케이션.

## 미션 진화

- [미션 1] 방탈출 예약 관리 - 관리자가 전화 및 현장 예약을 등록,관리하는 백엔드 API
- [미션 2] 방탈출 사용자 예약 - 사용자가 브라우저에서 직접 예약하는 서비스로 확장

## 기능 구현 목록

관리자 API에 prefix 적용

- [x]  관리자 예약 API에 /admin prefix 추가
- [x]  관리자 시간 API에 /admin prefix 추가

Phase 1 - 테마 도메인 추가

- [x]  Theme 도메인 객체 추가
- [x]  theme 테이블 스키마 추가
- [x]  ThemeRepository 인터페이스와 JDBC 구현 추가
- [x]  ThemeService 추가
- [x]  관리자 테마 API 추가 (/admin/themes)

Phase 2 - Reservation에 Theme 연결

- [x]  reservation 테이블에 theme_id FK 추가
- [x]  Reservation 도메인에 theme 필드 추가
- [x]  예약 Repository에 theme 조회·저장 연결
- [x]  예약 생성 시 themeId 포함하도록 변경
- [x]  MissionStepTest 예약과_시간_연결을 theme 포함으로 갱신

Phase 3 - 사용자 API

- [x]  사용자 테마 조회 API 추가 (GET /user/themes)
- [x]  예약 가능 시간 조회 쿼리 추가 (NOT IN)
- [x]  예약 가능 시간 조회 API 추가 (GET /user/themes/{id}/available-times)
- [x]  사용자 예약 추가 API (POST /user/reservations)

Phase 4 - 인기 테마 조회

- [x]  인기 테마 조회 쿼리 추가 (JOIN + GROUP BY + COUNT)
- [x]  인기 테마 조회 API 추가 (GET /user/themes/popular)

Phase 5 - 테스트 & 초기 데이터

- [x]  사용자 예약 정상 흐름 요구사항 테스트 추가
- [x]  data.sql로 인기 테마 검증용 초기 데이터 추가
- [x]  인기 테마 조회 요구사항 테스트 추가

## API 명세

### 관리자 API (`/admin/...`)

#### 예약 관리

| 기능    | Method / URL                      | 요청 본문                           | 응답                                     |
|-------|-----------------------------------|---------------------------------|----------------------------------------|
| 예약 조회 | `GET /admin/reservations`         | -                               | `[{id, name, date, time, theme}, ...]` |
| 예약 추가 | `POST /admin/reservations`        | `{name, date, timeId, themeId}` | `{id, name, date, time, theme}`        |
| 예약 삭제 | `DELETE /admin/reservations/{id}` | -                               |                                        |

#### 시간 관리

| 기능    | Method / URL               | 요청 본문       | 응답                     |
|-------|----------------------------|-------------|------------------------|
| 시간 조회 | `GET /admin/times`         | -           | `[{id, startAt}, ...]` |
| 시간 추가 | `POST /admin/times`        | `{startAt}` | `{id, startAt}`        |
| 시간 삭제 | `DELETE /admin/times/{id}` | -           |                        |

#### 테마 관리

| 기능    | Method / URL                | 요청 본문                            | 응답                                          |
|-------|-----------------------------|----------------------------------|---------------------------------------------|
| 테마 조회 | `GET /admin/themes`         | -                                | `[{id, name, description, thumbnail}, ...]` |
| 테마 추가 | `POST /admin/themes`        | `{name, description, thumbnail}` | `{id, name, description, thumbnail}`        |
| 테마 삭제 | `DELETE /admin/themes/{id}` | -                                |                                             |

---

### 사용자 API (`/user/...`)

> **사용자 흐름:** 테마를 본다 → 날짜를 고른다 → 그 조건에서 예약 가능한 시간을 본다 → 시간을 골라 예약한다.

| 기능          | Method / URL                                                 | 요청 본문                           | 응답                                                            |
|-------------|--------------------------------------------------------------|---------------------------------|---------------------------------------------------------------|
| 테마 목록 조회    | `GET /user/themes`                                           | -                               | `[{id, name, description, thumbnail}, ...]`                   |
| 예약 가능 시간 조회 | `GET /user/themes/{themeId}/available-times?date=YYYY-MM-DD` | -                               | `[{id, startAt}, ...]`                                        |
| 사용자 예약 추가   | `POST /user/reservations`                                    | `{name, date, timeId, themeId}` | `{id, name, date, time, theme}`                               |
| 인기 테마 조회    | `GET /user/themes/popular`                                   | -                               | `[{id, name, description, thumbnail, reservationCount}, ...]` |

---
