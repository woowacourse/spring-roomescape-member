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
| 본인 예약 조회    | `GET /user/reservations?name={name}`                         | -                               | `[{id, name, date, time, theme}, ...]`                        |
| 본인 예약 변경    | `PATCH /user/reservations/{id}`                              | `{name, date, timeId}`          | `{id, name, date, time, theme}`                               |
| 본인 예약 취소    | `DELETE /user/reservations/{id}?name={name}`                 | -                               | `204 No Content`                                              |
| 인기 테마 조회    | `GET /user/themes/popular`                                   | -                               | `[{id, name, description, thumbnail, reservationCount}, ...]` |

### 예외 처리 정책

모든 예외는 `GlobalExceptionHandler`에서 처리한다. 아래 원칙을 따른다.

**1. 400은 호출자 잘못, 500은 서버 잘못 — 포장하지 않는다.**
서버 내부 오류를 400으로 바꿔 내려보내지 않는다. 정상적인 값을 보냈는데 400을 받는 상황은
클라이언트를 혼란스럽게 하고 프론트엔드와의 불필요한 마찰을 만든다. 서버 책임인 오류는 500으로 정확히 전달한다.

**2. 클래스 계층 ≠ 위험도.**
`Exception → RuntimeException → 커스텀 예외`라는 계층은 **타입 분류 도구**일 뿐 위험도 순서가 아니다.
위험도는 예외에 우리가 부여하는 의미다. 둘이 보통 정렬돼 보이는 이유는 "Checked = 인프라 오류,
Runtime = 프로그래밍 오류"라는 자바의 관습 위에서 도메인 예외를 설계하기 때문이다.
따라서 **커스텀 예외이면서 500인 경우**도 존재한다 (`DomainValidationException`).

**3. RuntimeException 핸들러와 Exception 핸들러를 분리한다.**

- `RuntimeException` 핸들러: 프로그래머가 미처 예상하지 못한 런타임 오류의 fallback.
- `Exception` 핸들러: checked 예외(인프라·외부 라이브러리)까지 받는 최종 방어선.

예상 가능한 범위에서 예상 불가능한 범위로 갈수록 위험도가 높아진다. 둘을 나누면 로그 메시지·운영 대응
(알람 정책 등)을 다르게 가져갈 수 있다.

**4. 도메인 검증은 유지한다 — fail-fast.**
DTO·테스트로 입력을 거르더라도 도메인 객체 생성 시점의 검증을 유지한다. 검증이 없으면 잘못된 객체가
서비스·DB를 돌다 엉뚱한 곳에서 터져 원인 추적이 어려워진다. 도메인 검증 실패는 "DTO 검증을 통과하고도
잘못된 값이 도메인에 도달한" 상황 = 프로그래머 오류이므로, 자바 기본 `IllegalArgumentException`이 아닌
커스텀 `DomainValidationException`으로 던진다. 기본 예외를 쓰면 다른 곳에서 나는 같은 예외와 구분할 수
없어, 전용 핸들러로 잡아 500·후속 대응(알람 등)을 결정할 수 없기 때문이다.

#### 예외별 응답 매핑

| 예외                                 | HTTP | 의미                            |
|------------------------------------|------|-------------------------------|
| Bean Validation / 타입 변환 실패 등       | 400  | 입력 형식 위반 — 호출자 잘못             |
| `PastReservationException`         | 400  | 비즈니스 규칙 위반 (과거 시점)            |
| `ResourceConflictException`        | 409  | 리소스 충돌 (중복 예약, 사용 중 리소스 삭제 등) |
| `ResourceNotFoundException`        | 404  | 존재하지 않는 리소스 참조                |
| `UnauthorizedReservationException` | 403  | 본인 리소스가 아님                    |
| `DomainValidationException`        | 500  | 도메인 불변식 위반 — 프로그래머 오류         |
| `RuntimeException` (fallback)      | 500  | 예상하지 못한 런타임 오류                |
| `Exception` (최종 fallback)          | 500  | 그 외 모든 오류 (checked·인프라 포함)    |

500 응답 본문은 내부 정보(스택·SQL·경로 등)를 노출하지 않도록 고정 문구만 반환하고,
예외 상세는 ERROR 로그로만 남긴다.

#### 예외 계층과 관리 영역

커스텀 예외는 모두 `RuntimeException` 아래에 둔다. 계층과 각 예외를 관리하는 핸들러는 다음과 같다.

```
Exception                              → handleUnexpected        (500)
└─ RuntimeException                    → handleRuntime           (500)
   ├─ DomainValidationException        → handleDomainValidation  (500)
   ├─ PastReservationException         → handlePastReservation   (400)
   ├─ UnauthorizedReservationException → handleUnauthorized       (403)
   ├─ ResourceConflictException        → handleConflict           (409)
   │   ├─ ReservationConflictException
   │   ├─ ReservationTimeConflictException
   │   ├─ ReservationTimeInUseException
   │   ├─ ThemeConflictException
   │   └─ ThemeInUseException
   └─ ResourceNotFoundException        → handleNotFound           (404)
       ├─ ReservationNotFoundException
       ├─ ReservationTimeNotFoundException
       └─ ThemeNotFoundException
```

요청 형식 검증 예외(Bean Validation, 타입 변환, 파라미터 누락 등)는 위 계층과 별개로
프레임워크가 던지며, 각각 전용 핸들러가 400으로 처리한다.

**관리 영역** — 예상 가능한 영역에서 예상 불가능한 영역으로 갈수록 책임 소재가
호출자 → 프로그래머 → 인프라로 옮겨가고, 위험도가 함께 상승한다. 각 영역의 로그 레벨은 아래 로그 정책을 따른다.

| 관리 영역          | 대표 예외                                             | 책임 소재  | HTTP      |
|----------------|---------------------------------------------------|--------|-----------|
| 요청 형식 검증       | Bean Validation, 타입 변환 실패                         | 호출자    | 400       |
| 요청 대상·권한 검증    | `ResourceNotFoundException`, `Unauthorized...`    | 호출자    | 404 / 403 |
| 비즈니스 규칙        | `PastReservationException`, `ResourceConflict...` | 호출자    | 400 / 409 |
| 도메인 불변식        | `DomainValidationException`                       | 프로그래머  | 500       |
| 예상 못한 런타임 오류   | `RuntimeException` (fallback)                     | 프로그래머  | 500       |
| checked·인프라 오류 | `Exception` (최종 fallback)                         | 인프라/외부 | 500       |

### 로그 정책

SLF4J(`LoggerFactory`)를 사용하며, 레벨은 아래 기준으로 구분한다.
운영 시 레벨별로 알람(예: Slack)을 연동해 대응할 수 있도록 설계한다.

| 레벨      | 기준                                                        | 예시                                |
|---------|-----------------------------------------------------------|-----------------------------------|
| `ERROR` | 서버가 책임져야 할 오류. 500으로 응답되는 모든 예외. **즉시 대응 필요.**            | 도메인 불변식 위반, 예기치 못한 런타임 예외, 인프라 장애 |
| `WARN`  | 형식 검증은 통과했으나 처리할 수 없는 요청. 서버는 정상, **호출자 귀책**. 추세 모니터링 대상. | 존재하지 않는 시간/테마/예약 조회·삭제 시도         |
| `INFO`  | 정상적인 주요 비즈니스 이벤트. 운영 흐름 추적용.                              | 예약 생성 완료                          |
| `DEBUG` | 개발·디버깅용 상세 정보. 운영 환경에서는 보통 비활성화.                          | 상세 파라미터 추적                        |

- **INFO**: 예약 생성 등 정상 흐름의 핵심 이벤트를 기록한다.
- **WARN**: 존재하지 않는 리소스를 참조하는 등 "형식은 맞지만 처리할 수 없는" 요청을 기록한다.
  단건은 호출자 실수지만, 급증하면 클라이언트 버그나 비정상 트래픽의 신호가 된다.
  도메인 불변식 위반(`DomainValidationException`)은 WARN이 아니라 **ERROR**다 — 위 예외 처리 정책 참고.
- **ERROR**: 서버가 책임져야 할 오류, 즉 500으로 응답되는 예외에 사용한다. 예외 상세를 로그에 남긴다.
