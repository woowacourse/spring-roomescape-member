### 기능 구현 목록

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

### 어드민 API

### 예약 관리

| 기능    | Method / URL                      | 요청 본문                           | 응답                                     |
|-------|-----------------------------------|---------------------------------|----------------------------------------|
| 예약 조회 | `GET /admin/reservations`         | -                               | `[{id, name, date, time, theme}, ...]` |
| 예약 추가 | `POST /admin/reservations`        | `{name, date, timeId, themeId}` | `{id, name, date, time, theme}`        |
| 예약 삭제 | `DELETE /admin/reservations/{id}` | -                               | `200 OK`                               |

### 시간 관리

| 기능    | Method / URL               | 요청 본문       | 응답                     |
|-------|----------------------------|-------------|------------------------|
| 시간 조회 | `GET /admin/times`         | -           | `[{id, startAt}, ...]` |
| 시간 추가 | `POST /admin/times`        | `{startAt}` | `{id, startAt}`        |
| 시간 삭제 | `DELETE /admin/times/{id}` | -           | `200 OK`               |

### 테마 관리

| 기능    | Method / URL                | 요청 본문                            | 응답                                          |
|-------|-----------------------------|----------------------------------|---------------------------------------------|
| 테마 조회 | `GET /admin/themes`         | -                                | `[{id, name, description, thumbnail}, ...]` |
| 테마 추가 | `POST /admin/themes`        | `{name, description, thumbnail}` | `{id, name, description, thumbnail}`        |
| 테마 삭제 | `DELETE /admin/themes/{id}` | -                                | `200 OK`                                    |

### 사용자 API

> **사용자 흐름:** 테마를 본다 → 날짜를 고른다 → 그 조건에서 예약 가능한 시간을 본다 → 시간을 골라 예약한다.
>

| 기능          | Method / URL                                                 | 요청 본문                           | 응답                                                            |
|-------------|--------------------------------------------------------------|---------------------------------|---------------------------------------------------------------|
| 테마 목록 조회    | `GET /user/themes`                                           | -                               | `[{id, name, description, thumbnail}, ...]`                   |
| 예약 가능 시간 조회 | `GET /user/themes/{themeId}/available-times?date=YYYY-MM-DD` | -                               | `[{id, startAt}, ...]`                                        |
| 사용자 예약 추가   | `POST /user/reservations`                                    | `{name, date, timeId, themeId}` | `{id, name, date, time, theme}`                               |
| 인기 테마 조회    | `GET /user/themes/popular`                                   | -                               | `[{id, name, description, thumbnail, reservationCount}, ...]` |

### 로그 정책

SLF4J(`LoggerFactory`)를 사용하며, 레벨은 아래 기준으로 구분한다.

| 레벨      | 기준                                                              | 예시                                |
|---------|-----------------------------------------------------------------|-----------------------------------|
| `ERROR` | 서버 내부 오류, 프로그래머가 예측하지 못한 예외. **즉시 대응 필요.**                      | 예기치 못한 `RuntimeException`, 인프라 장애 |
| `WARN`  | 형식 검증은 통과했으나 도메인 단계에서 거부된 요청. 서버는 정상, **클라이언트 오류**. 추세 모니터링 대상. | 존재하지 않는 시간/테마/예약 조회·삭제 시도         |
| `INFO`  | 정상적인 주요 비즈니스 이벤트. 운영 흐름 추적용.                                    | 예약 생성 완료                          |
| `DEBUG` | 개발·디버깅용 상세 정보. 운영 환경에서는 보통 비활성화.                                | 상세 파라미터 추적                        |

- **INFO**: 예약 생성 등 정상 흐름의 핵심 이벤트를 기록한다.
- **WARN**: "형식은 맞지만 실재하지 않는 값"이 도메인까지 도달한 경우(예: `timeId` 조회 실패) 기록한다. 단건은 클라이언트 실수지만, 급증하면 클라이언트 버그나 비정상 트래픽의 신호가 된다.
- **ERROR**: 서버가 책임져야 할 예기치 못한 오류에 사용한다.
