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

## 3️⃣ 구현 기능 목록

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

## 4️⃣ API 명세

### 예약 관리

| 기능 | 메서드 / URL | 요청 본문 | 응답 |
| :--- | :--- | :--- | :--- |
| **예약 조회** | `GET /reservations` | — | `[{id, name, date, time}, ...]` |
| **예약 추가** | `POST /reservations` | `{name, date, time}` | `{id, name, date, time}` |
| **예약 삭제** | `DELETE /reservations/{id}` | — | `200 OK` |

예약 추가 요청/응답 예시 (1~2단계):

```
POST /reservations HTTP/1.1
Content-Type: application/json

{
    "name": "브라운",
    "date": "2023-08-05",
    "time": "15:40"
}
```

```
HTTP/1.1 200
Content-Type: application/json

{
    "id": 1,
    "name": "브라운",
    "date": "2023-08-05",
    "time": "15:40"
}
```

예약 추가 요청/응답 예시 (3단계 이후, 시간 연결):

```
POST /reservations HTTP/1.1
Content-Type: application/json

{
    "name": "브라운",
    "date": "2023-08-05",
    "timeId": 1
}
```

```
HTTP/1.1 200
Content-Type: application/json

{
    "id": 1,
    "name": "브라운",
    "date": "2023-08-05",
    "time": {
        "id": 1,
        "startAt": "10:00"
    }
}
```

### 시간 관리 (3단계~)

| 기능 | 메서드 / URL | 요청 본문 | 응답 |
| :--- | :--- | :--- | :--- |
| **시간 추가** | `POST /times` | `{startAt}` | `{id, startAt}` |
| **시간 조회** | `GET /times` | — | `[{id, startAt}, ...]` |
| **시간 삭제** | `DELETE /times/{id}` | — | `200 OK` |

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
