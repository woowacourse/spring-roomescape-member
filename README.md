# 방탈출 예약 관리

## 📋 목차
- [개요](#-개요)
- [실행 방법](#-실행-방법)
- [구현 기능 목록](#-구현-기능-목록)
- [API 명세](#-api-명세)
- [커밋 컨벤션](#-커밋-컨벤션)

---

## 1️⃣ 개요

방탈출 카페 관리자가 전화·현장 예약을 직접 등록·관리할 수 있는 예약 관리 시스템이다.
총 4단계에 걸쳐 메모리 저장 → DB 연동 → 시간 관리 → 계층 분리 순서로 구현한다.

---

## 2️⃣ 실행 방법

### 요구 사항

| 항목        | 버전               |
|-------------|-------------------|
| Java        | 21                |
| Gradle      | 8.x (Wrapper 포함) |
| Spring Boot | 3.4.4             |

### 실행

```bash
./gradlew bootRun
```

### 테스트 실행

```bash
./gradlew test
```

---

## 방탈출 예약 관리 : 구현 기능 목록

### ✅ 1단계: 웹 요청-응답

> 별도의 데이터베이스 없이 메모리(List + AtomicLong)로 예약 상태를 관리한다.

- [x] **예약 조회 API** (`GET /reservations`)
    - [x] 전체 예약 목록을 반환한다.
- [x] **예약 추가 API** (`POST /reservations`)
    - [x] 이름, 날짜, 시간을 입력받아 예약을 생성한다.
    - [x] 생성된 예약의 id(AtomicLong 자동 증가)를 응답에 포함한다.
- [x] **예약 삭제 API** (`DELETE /reservations/{id}`)
    - [x] id에 해당하는 예약을 삭제한다.

### ✅ 2단계: 데이터베이스 연동

> H2 인메모리 데이터베이스로 전환하여 서버 재시작 전까지 데이터를 유지한다.

- [x] **환경 설정**
    - [x] `spring-boot-starter-jdbc`, `h2` 의존성을 추가한다.
    - [x] `application.properties`에 H2 콘솔 및 datasource URL을 설정한다.
- [x] **스키마 정의** (`resources/schema.sql`)
    - [x] `reservation` 테이블을 정의한다.
- [ ] **API 전환**
    - [x] 예약 조회를 JdbcTemplate 기반으로 전환한다.
    - [x] 예약 추가를 JdbcTemplate 기반으로 전환하고, DB가 생성한 id를 응답에 담는다.
    - [x] 예약 삭제를 JdbcTemplate 기반으로 전환한다.
    - [x] 기존의 `List<Reservation>`, `AtomicLong`을 제거한다.

### ✅ 3단계: 시간 관리

> 정해진 시간 슬롯을 관리하고, 예약과 시간을 연결한다.

- [x] **시간 관리 API**
    - [x] 시간 추가 (`POST /times`): `startAt`을 입력받아 시간 슬롯을 생성한다.
    - [x] 시간 조회 (`GET /times`): 전체 시간 슬롯 목록을 반환한다.
    - [x] 시간 삭제 (`DELETE /times/{id}`): id에 해당하는 시간 슬롯을 삭제한다.
- [x] **스키마 추가** (`resources/schema.sql`)
    - [x] `reservation_time` 테이블을 추가한다.
- [x] **예약과 시간 연결**
    - [x] `reservation` 테이블의 `time` 컬럼을 `time_id (FK → reservation_time.id)`로 변경한다.
    - [x] `Reservation` 클래스의 `time` 필드를 `String → ReservationTime` 객체로 변경한다.
    - [x] 예약 추가 요청 본문을 `time → timeId`로 변경한다.
    - [x] 예약 조회 응답의 `time` 필드를 객체(`{id, startAt}`)로 변경한다.
    - [x] 예약 조회 쿼리에 INNER JOIN을 적용한다.

### ✅ 4단계: 계층 분리

> 레이어드 아키텍처로 레이어별 책임에 따라 코드를 분리한다.

| 레이어 | 책임 |
| :--- | :--- |
| Controller | 웹 요청·응답 |
| Service | 비즈니스 플로우 |
| DAO (Repository) | DB 접근 |
| Domain | 비즈니스 규칙 |

- [x] **레이어 분리**
    - [x] `ReservationController`에서 비즈니스 로직과 DB 접근 코드를 분리한다.
    - [x] DB 접근 책임을 DAO(repository)에 위임한다.
    - [x] 비즈니스 플로우 책임을 Service에 위임한다.
    - [x] 비즈니스 규칙 책임을 Domain에 위임한다.
    - [x] `ReservationController`에 `JdbcTemplate` 필드가 남아있지 않아야 한다.
- [x] **Spring Bean 등록**
    - [x] 분리한 클래스를 `@Component` · `@Service` · `@Repository` 등으로 등록한다.

---

## 테마 + 사용자 예약 cycle 1 : 구현 기능 목록

### 관리자 기능
- [x] 테마 정보 추가 기능 (관리자)
  - 테마는 이름, 설명, 썸네일 이미지 URL을 가진다.
  - 모든 테마의 시작 시간과 소요 시간은 동일하다고 가정한다.
- [x] 테마 삭제 기능 (관리자)
- [x] 관리자 예약 조회 기능
  - [x] response에 테마 추가할 것. 


### 사용자 기능
- [x] 테마 조회 기능
- [x] 사용자 테마 시간 조회 기능
- [ ] 사용자 예약 조회 기능(수정)
  - 사용자의 예약 목록을 보여준다.
  - 예약 id, 테마 이름, 테마 썸네일 url, 날짜, 시간
- [ ] 사용자 예약 기능 수정
  - 예약 기능 request는 이름, 날짜, 시간 id, 테마 id를 포함한다.
  - 같은 날짜·시간이라도 테마가 다르면 각각 예약 가능하다.
- [ ] 인기 테마 조회 기능
  - 최근 1주 동안 예약이 많았던 테마 상위 10개를 조회한다.
    - ex) 오늘이 5월 8일이면, 게임 날짜가 5월 1일~5월 7일인 예약을 집계해 인기 순서대로 10개를 응답한다.

---

## 4️⃣ API 명세

### 관리자 API(`/admin`)
| 기능               | 메서드 / URL        | 요청 본문                                | 응답                                                                                                                         |
|:-----------------|:-----------------|:-------------------------------------|:---------------------------------------------------------------------------------------------------------------------------|
| **관리자 테마 추가 기능** | `POST /themes`   | `{name, description, thumbnail_url}` | `201 header: {location}`                                                                                                   |
| **관리자 테마 삭제 기능** | `DELETE /themes` | `{id}`                               | `204`                                                                                                                      |
| **관리자 예약 조회 기능** | `GET /reservations`      | -                                  | `200 [{id: 1, name: "브라운", theme_name: "호러", theme_description, theme_thumbnail: "...", date:"2021-05-20", time: "10:00"}]` |
| **관리자 예약 삭제**    | `DELETE /reservations/{id}` | — | `204 OK`                                                                                                                   |
| **관리자 시간 추가**    | `POST /times` | `{startAt}` | `{id, startAt}`                                                                                                            |
| **관리자 시간 조회**    | `GET /times` | — | `[{id, startAt}, ...]`                                                                                                     |
| **관리자 시간 삭제**    | `DELETE /times/{id}` | — | `200 OK`                                                                                                                   |

### 사용자 API

| 기능                  | 메서드 / URL                   | 요청 본문                              | 응답                                                                                                                            |
|:--------------------|:----------------------------|:-----------------------------------|:------------------------------------------------------------------------------------------------------------------------------|
| **사용자 테마 조회 기능**    | `GET /themes`               | -                                  | `200 [{id, name, description, thumbnail_url}]`                                                                                |
| **사용자 테마 시간 조회 기능** | `GET /themes/{id}/times`    | -                                  | `200 [{id: 1, start_at:"10:00", available:true},...]`                                                                         |
| **사용자 예약 추가 기능**    | `POST /reservations`        | `{name, date, time_id, theme_id }` | `201`                                                                                                                         |
| **사용자 예약 조회 기능**    | `GET /reservations`         | -                                  | `200 [{id: "1", name: "브라운", theme_name: "호러", theme_description, theme_thumbnail: "...", date:"2021-05-20", time: "10:00"}]` |
| **사용자 예약 삭제**       | `DELETE /reservations/{id}` | `{name}`                           | `204 OK`                                                                                                                      |

---

## 5️⃣ 커밋 컨벤션

Following convention : https://gist.github.com/stephenparish/9941e89d80e2bc58a153

```markdown
# basic structure
<type>(<scope>): <subject>
<BLANK LINE>
<body>
<BLANK LINE>
<footer>

# <type>
feat (feature)
fix (bug fix)
docs (documentation)
style (formatting, missing semi colons, …)
refactor
test (when adding missing tests)
chore (maintain)

# <scope>
console - I/O
domain - 핵심 로직
validation - 유효성검사
test - 테스트코드 추가
```
