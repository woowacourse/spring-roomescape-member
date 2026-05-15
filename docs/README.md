# 방탈출 예약 시스템

방탈출 예약, 테마, 예약 시간을 관리하는 백엔드 서비스입니다.

사용자는 이름을 기준으로 본인의 예약을 조회하고, 예약 일정을 변경하거나 예약을 취소할 수 있습니다.
관리자는 테마와 예약 시간을 등록, 조회, 삭제할 수 있습니다.

> 현재 미션에는 로그인 기능이 없으므로, 예약자 이름(`name`)을 본인 확인 값으로 사용합니다.

---

## 목차

1. [기능 목록](#기능-목록)
2. [테스트 체크리스트](#테스트-체크리스트)
   - [Domain Test](#1-domain-test)
   - [API Test](#2-api-test)
   - [Repository Test](#3-repository-test)
   - [Service Test 기준](#4-service-test-기준)
3. [API 명세](#api-application-programming-interface-명세)
   - [테마](#1-테마-theme)
   - [예약 시간](#2-예약-시간-reservation-time)
   - [예약](#3-예약-reservation)
4. [예외 및 에러 응답 명세](#예외-및-에러-응답-명세)

---

## 기능 목록

### 테마
- [x] 테마를 등록한다.
- [x] 테마 목록을 조회한다.
- [x] 테마를 삭제한다.
- [x] 최근 예약 건수를 기준으로 인기 테마를 조회한다.

### 예약 시간
- [x] 예약 시간을 등록한다.
- [x] 예약 시간 목록을 조회한다.
- [x] 예약 시간을 삭제한다.
- [x] 특정 날짜와 테마에 대한 예약 가능 시간을 조회한다.
- [x] 동일한 시작 시간을 중복 등록할 수 없다.
- [ ] 예약이 존재하는 시간은 삭제할 수 없다.

### 예약
- [x] 예약을 생성한다.
- [x] 같은 날짜, 시간, 테마에 이미 예약이 있으면 중복 예약을 거부한다.
- [x] 사용자는 이름으로 본인의 예약 목록을 조회할 수 있다.
- [x] 사용자는 본인 예약의 날짜와 시간을 변경할 수 있다.
- [x] 사용자는 본인 예약을 취소할 수 있다.

### 관리자 예약
- [x] 전체 예약 목록을 조회한다.
- [x] 예약을 삭제한다.

---

## 테스트 체크리스트

테스트는 기능의 성격에 따라 다음 기준으로 분류한다.
- 순수 객체 규칙은 `Domain Test`에서 검증한다.
- 사용자 요청과 응답 흐름은 `API Test`에서 검증한다.
- SQL(Structured Query Language), 집계, 정렬, 날짜 범위, 페이징은 `Repository Test`에서 검증한다.
- `Service Test`는 중복을 피하고, 필요한 경우에만 선택적으로 작성한다.

---

## 1. Domain Test

### Theme
- [x] 테마 이름은 null이거나 빈 공백일 수 없다.
- [x] 테마 설명은 null이거나 빈 공백일 수 없다.
- [x] 테마 이미지 경로는 null이거나 빈 공백일 수 없다.
- [x] 테마 이미지 경로는 `/images/themes/`로 시작하는 경로이어야 한다.

### ReservationTime
- [x] 예약 시작 시간은 null일 수 없다.

### Reservation
- [x] 예약 이름은 null이거나 빈 공백일 수 없다.
- [x] 예약 이름은 2자 이상 20자 이하여야 한다.
- [x] 예약 이름은 완성형 한글, 영문, 공백만 허용한다.
- [x] 예약 날짜는 null일 수 없다.

### ReservedTimes
- [x] 특정 시간 ID의 예약 여부를 판단한다.
- [x] 이미 예약된 시간에 예약하려 하면 예외가 발생한다.

---

## 2. API Test

API 테스트는 클라이언트 관점에서 요청, 응답, 상태 코드, 에러 메시지를 검증한다.

### 테스트 이름 기준

성공 케이스는 기능 중심으로 작성한다.
- `예약을_생성한다`
- `테마_목록을_조회한다`
- `예약_시간을_삭제한다`

실패 케이스는 상태 코드를 함께 드러낸다.
- `존재하지_않는_테마로_예약하면_404를_반환한다`
- `중복된_예약을_생성하면_409를_반환한다`
- `예약자_이름이_비어있으면_400을_반환한다`

### ThemeApiTest
- [x] 테마를 등록한다.
- [x] 테마 목록을 조회한다.
- [x] 테마를 삭제한다.
- [x] 인기 테마를 조회한다.
- [x] 인기 테마 조회 시 `days` 파라미터가 유효하지 않으면 400을 반환한다.
- [x] 인기 테마 조회 시 `limit` 파라미터가 유효하지 않으면 400을 반환한다.

### ReservationTimeApiTest
- [x] 예약 시간을 등록한다.
- [x] 예약 시간 목록을 조회한다.
- [x] 예약 시간을 삭제한다.
- [x] 특정 날짜와 테마에 대한 예약 가능 시간을 조회한다.
- [x] 예약 시작 시간은 `HH:mm` 형식이어야 한다.
- [x] 예약 시작 시간이 null이면 400을 반환한다.
- [x] 동일한 시작 시간을 중복 등록하면 409를 반환한다.
- [x] 존재하지 않는 예약 시간을 삭제하면 404를 반환한다.
- [x] 예약이 존재하는 시간을 삭제하면 409를 반환한다.
- [x] 존재하지 않는 테마의 예약 가능 시간을 조회하면 404를 반환한다.
- [x] 예약 가능 시간 조회 시 날짜 형식이 잘못되면 400을 반환한다.

### ReservationApiTest
- [x] 예약을 생성한다.
- [x] 같은 날짜, 시간, 테마에 이미 예약이 있으면 중복 예약을 거부한다.
- [x] 존재하지 않는 테마로 예약하면 404를 반환한다.
- [x] 존재하지 않는 예약 시간으로 예약하면 404를 반환한다.
- [x] 사용자는 이름으로 본인의 예약 목록을 조회할 수 있다.
- [x] 사용자는 본인 예약의 날짜와 시간을 변경할 수 있다.
- [x] 사용자는 본인 예약을 취소할 수 있다.
- [x] 예약 이름이 null이면 400을 반환한다.
- [x] 예약 이름이 빈 공백이면 400을 반환한다.
- [ ] 예약 이름이 2자 미만이면 400을 반환한다.
- [x] 예약 이름이 20자를 초과하면 400을 반환한다.
- [ ] 예약 이름에 허용되지 않는 문자가 포함되면 400을 반환한다.
- [x] 예약 날짜가 null이면 400을 반환한다.
- [ ] 예약 날짜가 `yyyy-MM-dd` 형식이 아니면 400을 반환한다.
- [ ] 예약 시간 식별자(`timeId`)가 null이면 400을 반환한다.
- [ ] 테마 식별자(`themeId`)가 null이면 400을 반환한다.
- [x] 지나간 날짜와 시간으로 예약하면 400을 반환한다.
- [x] 예약 날짜가 오늘이고 현재 서버 시간 이전의 예약 시간이면 400을 반환한다.
- [x] 오늘 기준 30일을 초과한 날짜로 예약하면 400을 반환한다.
- [ ] 존재하지 않는 예약을 조회하면 404를 반환한다.
- [ ] 존재하지 않는 예약을 변경하면 404를 반환한다.
- [ ] 존재하지 않는 예약을 취소하면 404를 반환한다.
- [ ] 예약자 이름이 일치하지 않으면 404를 반환한다.
- [ ] 이미 취소된 예약을 변경하면 409를 반환한다.
- [ ] 이미 취소된 예약을 다시 취소하면 409를 반환한다.

### AdminReservationApiTest
- [x] 전체 예약 목록을 조회한다.
- [x] 예약을 삭제한다.
- [x] 예약 목록 페이징 조건을 검증한다.

---

## 3. Repository Test

Repository 테스트는 실제 데이터 저장소를 기준으로 SQL, 정렬, 집계, 날짜 범위, 페이징을 검증한다.

### ThemeRepositoryTest
- [x] 최근 예약 건수를 기준으로 인기 테마를 조회한다.
- [x] 오늘 예약은 인기 테마 집계에서 제외한다.
- [x] 조회 기간 이전 예약은 인기 테마 집계에서 제외한다.
- [x] 예약 수가 같으면 테마 이름순으로 정렬한다.
- [x] `limit` 개수만큼 인기 테마를 조회한다.
- [x] 예약이 없는 테마는 인기 테마 조회 결과에서 제외한다.

### ReservationTimeRepositoryTest
- [x] 특정 날짜와 테마에 이미 예약된 시간 식별자를 조회한다.
- [x] 특정 날짜와 테마에 예약이 없으면 빈 목록을 반환한다.

### ReservationRepositoryTest
- [x] 예약 목록을 페이징 조회한다.
- [x] 이름으로 예약 목록을 조회한다.
- [x] 이름에 해당하는 예약이 없으면 빈 목록을 반환한다.

---

## 4. Service Test

Service Test는 유스케이스 규칙과 외부 의존성 조합을 검증한다.

### ReservationServiceTest
- [x] 지나간 날짜로 예약할 수 없다.
- [x] 예약 날짜가 오늘이면 현재 서버 시간 이전의 예약 시간은 선택할 수 없다.
- [x] 오늘 기준 30일을 초과한 날짜로 예약할 수 없다.
- [ ] 이미 취소된 예약은 변경하거나 다시 취소할 수 없다.

다음 경우에 Service Test를 작성한다.
- API 테스트로 실패 원인을 파악하기 어렵다.
- 여러 Repository 결과를 조합하는 분기가 복잡하다.
- 특정 조건에서 저장이 호출되지 않아야 함을 명확히 검증하고 싶다.
- 외부 API, 메일, 시간, 랜덤 값처럼 제어하기 어려운 의존성이 있다.

현재 기준에서는 다음 방식으로 우선 검증한다.
- 단순 CRUD(Create, Read, Update, Delete): API Test
- 순수 객체 규칙: Domain Test
- 인기 테마 집계, 정렬, 날짜 범위: Repository Test
- 요청/응답과 상태 코드: API Test

---

## API(Application Programming Interface) 명세

## 1. 테마 (Theme)

| 기능 | Method | URL | 설명 |
| --- | --- | --- | --- |
| 테마 목록 조회 | `GET` | `/themes` | 등록된 모든 테마 목록을 조회한다. |
| 인기 테마 조회 | `GET` | `/themes/rank` | 최근 N일간 예약 수 기준 인기 테마를 조회한다. |
| 테마 추가 | `POST` | `/themes` | 새로운 테마를 등록한다. |
| 테마 삭제 | `DELETE` | `/themes/{id}` | 특정 테마를 삭제한다. |

### 테마 등록 요청

```json
{
  "name": "고대 이집트의 비밀",
  "description": "파라오의 무덤에 숨겨진 비밀을 찾아 탈출하세요.",
  "imagePath": "/images/themes/egypt.webp"
}
````

### 테마 응답

```json
{
  "id": 1,
  "name": "고대 이집트의 비밀",
  "description": "파라오의 무덤에 숨겨진 비밀을 찾아 탈출하세요.",
  "imagePath": "/images/themes/egypt.webp"
}
```

### 테마 목록 조회 응답

```json
{
  "themes": [
    {
      "id": 1,
      "name": "고대 이집트의 비밀",
      "description": "파라오의 무덤에 숨겨진 비밀을 찾아 탈출하세요.",
      "imagePath": "/images/themes/egypt.webp"
    }
  ]
}
```

### 인기 테마 조회 요청

```http
GET /themes/rank?days=7&limit=10
```

### 인기 테마 조회 응답

```json
{
  "themeRankings": [
    {
      "rank": 1,
      "theme": {
        "id": 1,
        "name": "고대 이집트의 비밀",
        "description": "파라오의 무덤에 숨겨진 비밀을 찾아 탈출하세요.",
        "imagePath": "/images/themes/egypt.webp"
      }
    }
  ]
}
```
* * *

## 2. 예약 시간 (Reservation Time)

| 기능          | Method   | URL                                                      | 설명                            |
| ----------- | -------- | -------------------------------------------------------- | ----------------------------- |
| 예약 시간 목록 조회 | `GET`    | `/reservation-times`                                     | 등록된 모든 예약 시간을 조회한다.           |
| 예약 가능 시간 조회 | `GET`    | `/reservation-times/available?themeId=1&date=2026-05-20` | 특정 날짜와 테마에 대한 예약 가능 시간을 조회한다. |
| 예약 시간 등록    | `POST`   | `/reservation-times`                                     | 새로운 예약 시간을 등록한다.              |
| 예약 시간 삭제    | `DELETE` | `/reservation-times/{id}`                                | 특정 예약 시간을 삭제한다.               |

### 예약 시간 등록 요청

```json
{
  "startAt": "14:00"
}
```

### 예약 시간 응답

```json
{
  "id": 1,
  "startAt": "14:00"
}
```

### 예약 시간 목록 조회 응답

```json
{
  "reservationTimes": [
    {
      "id": 1,
      "startAt": "14:00"
    }
  ]
}
```

### 예약 가능 시간 조회 응답

```json
{
  "theme": {
    "id": 1,
    "name": "고대 이집트의 비밀",
    "description": "파라오의 무덤에 숨겨진 비밀을 찾아 탈출하세요.",
    "imagePath": "/images/themes/egypt.webp"
  },
  "availableTimes": [
    {
      "id": 1,
      "startAt": "13:00",
      "available": true
    },
    {
      "id": 2,
      "startAt": "14:00",
      "available": false
    }
  ]
}
```
* * *

## 3. 예약 (Reservation)

### 사용자 예약

| 기능           | Method | URL                               | 설명                            |
| ------------ | ------ | --------------------------------- | ----------------------------- |
| 사용자 예약 목록 조회 | `GET`  | `/reservations?name=고래`           | 사용자가 자신의 이름으로 본인 예약 목록을 조회한다. |
| 예약 생성        | `POST` | `/reservations`                   | 새로운 예약을 생성한다.                 |
| 예약 일정 변경     | `PUT`  | `/reservations/{id}/schedule`     | 예약의 날짜와 시간 영역을 대체한다.          |
| 예약 취소        | `PUT`  | `/reservations/{id}/cancellation` | 예약 취소를 요청한다.                  |

### 관리자 예약

| 기능          | Method   | URL                        | 설명                  |
| ----------- | -------- | -------------------------- | ------------------- |
| 전체 예약 목록 조회 | `GET`    | `/admin/reservations`      | 전체 예약 목록을 조회한다.     |
| 예약 삭제       | `DELETE` | `/admin/reservations/{id}` | 특정 예약을 하드 삭제한다.     |

사용자 예약 취소는 예약 상태를 `CANCELLED`로 변경하고, 관리자 예약 삭제는 예약 데이터를 제거한다.

### 예약 생성 요청

```json
{
  "name": "고래",
  "date": "2026-05-20",
  "timeId": 2,
  "themeId": 3
}
```

### 예약 생성 응답

```json
{
  "id": 1,
  "name": "고래",
  "date": "2026-05-20",
  "time": {
    "id": 2,
    "startAt": "14:00"
  },
  "theme": {
    "id": 3,
    "name": "고대 이집트의 비밀",
    "description": "파라오의 무덤에 숨겨진 비밀을 찾아 탈출하세요.",
    "imagePath": "/images/themes/egypt.webp"
  },
  "status": "RESERVED"
}
```

### 예약 목록 조회 응답

```json
{
  "reservations": [
    {
      "id": 1,
      "name": "고래",
      "date": "2026-05-20",
      "time": {
        "id": 2,
        "startAt": "14:00"
      },
      "theme": {
        "id": 3,
        "name": "고대 이집트의 비밀",
        "description": "파라오의 무덤에 숨겨진 비밀을 찾아 탈출하세요.",
        "imagePath": "/images/themes/egypt.webp"
      },
      "status": "RESERVED"
    }
  ]
}
```

### 사용자 예약 목록 조회 응답

예약이 없을 경우 빈 배열을 반환한다.

```json
{
  "reservations": [
    {
      "id": 1,
      "name": "고래",
      "date": "2026-05-20",
      "time": {
        "id": 2,
        "startAt": "14:00"
      },
      "theme": {
        "id": 3,
        "name": "고대 이집트의 비밀",
        "description": "파라오의 무덤에 숨겨진 비밀을 찾아 탈출하세요.",
        "imagePath": "/images/themes/egypt.webp"
      },
      "status": "RESERVED"
    },
    {
      "id": 2,
      "name": "고래",
      "date": "2026-05-22",
      "time": {
        "id": 4,
        "startAt": "16:30"
      },
      "theme": {
        "id": 5,
        "name": "좀비 바이러스",
        "description": "좀비 바이러스를 피해 연구소에서 탈출하세요.",
        "imagePath": "/images/themes/zombie.webp"
      },
      "status": "CANCELLED"
    }
  ]
}
```

### 예약 일정 변경 요청

예약 전체를 변경하지 않고, 예약의 일정 영역인 날짜와 시간을 대체한다.

```json
{
  "name": "고래",
  "date": "2026-05-21",
  "timeId": 3
}
```

### 예약 일정 변경 응답

```json
{
  "id": 1,
  "name": "고래",
  "date": "2026-05-21",
  "time": {
    "id": 3,
    "startAt": "15:00"
  },
  "theme": {
    "id": 3,
    "name": "고대 이집트의 비밀",
    "description": "파라오의 무덤에 숨겨진 비밀을 찾아 탈출하세요.",
    "imagePath": "/images/themes/egypt.webp"
  },
  "status": "RESERVED"
}
```

### 예약 취소 요청

클라이언트가 예약 상태를 직접 조작하지 않고, 서버에게 예약 취소를 요청한다.
서버는 요청을 처리한 뒤 예약 상태를 `CANCELLED`로 변경한다.

```json
{
  "name": "고래"
}
```

### 예약 취소 응답

```json
{
  "id": 1,
  "name": "고래",
  "date": "2026-05-21",
  "time": {
    "id": 3,
    "startAt": "15:00"
  },
  "theme": {
    "id": 3,
    "name": "고대 이집트의 비밀",
    "description": "파라오의 무덤에 숨겨진 비밀을 찾아 탈출하세요.",
    "imagePath": "/images/themes/egypt.webp"
  },
  "status": "CANCELLED"
}
```
* * *

## 예외 및 에러 응답 명세

### 예외 구조

커스텀 예외는 HTTP 상태 코드의 맥락을 가지는 부모 예외를 상속받아 구현한다.
-   `RoomescapeException`: 최상위 도메인 예외
-   `BadRequestException`: 잘못된 요청 예외, 400 응답
-   `NotFoundException`: 리소스를 찾을 수 없는 예외, 404 응답
-   `ConflictException`: 현재 리소스 상태와 요청이 충돌하는 예외, 409 응답

### 에러 응답 형식

모든 에러 응답은 JSON 형식으로 반환한다.

```json
{
  "message": "해당 시간과 테마에는 이미 예약이 존재합니다."
}
```

### HTTP 상태 코드 매핑 기준

보안 원칙: 소유권 불일치 상황에서는 `403 Forbidden` 대신 `404 Not Found`를 반환한다.
이를 통해 리소스 존재 여부를 노출하지 않고, Blind IDOR(Insecure Direct Object Reference, 직접 객체 참조 취약점) 공격 가능성을 낮춘다.

| 상태 코드                       | 부모 예외/처리기                             | 발생 케이스                             | 응답 메시지 예시                       |
| --------------------------- | ------------------------------------- | ---------------------------------- | ------------------------------- |
| `400 Bad Request`           | `HttpMessageNotReadableException`     | JSON 본문이 잘못되었거나 날짜/시간 역직렬화에 실패한 경우 | `"요청 본문 형식이 올바르지 않습니다."`        |
| `400 Bad Request`           | `MethodArgumentNotValidException`     | `@Valid` 검증에 실패한 경우                | `"예약자 이름은 2자 이상 20자 이하여야 합니다."` |
| `400 Bad Request`           | `MethodArgumentTypeMismatchException` | 요청 파라미터 타입 변환에 실패한 경우              | `"요청 값 형식이 올바르지 않습니다."`         |
| `400 Bad Request`           | `BadRequestException`                 | 과거 날짜 또는 시간으로 예약하는 경우              | `"과거 날짜/시간으로는 예약할 수 없습니다."`     |
| `400 Bad Request`           | `BadRequestException`                 | 오늘 기준 30일을 초과한 날짜로 예약하는 경우         | `"30일을 초과한 날짜로는 예약할 수 없습니다."`   |
| `404 Not Found`             | `NotFoundException`                   | 존재하지 않는 예약을 조회, 변경, 취소하는 경우        | `"해당 예약을 찾을 수 없습니다."`           |
| `404 Not Found`             | `NotFoundException`                   | 예약자 이름이 일치하지 않는 경우                 | `"해당 예약을 찾을 수 없습니다."`           |
| `404 Not Found`             | `NotFoundException`                   | 존재하지 않는 예약 시간으로 예약하는 경우            | `"선택한 예약 시간이 존재하지 않습니다."`       |
| `404 Not Found`             | `NotFoundException`                   | 존재하지 않는 테마로 예약하는 경우                | `"선택한 테마가 존재하지 않습니다."`          |
| `409 Conflict`              | `ConflictException`                   | 같은 날짜, 시간, 테마에 이미 예약이 존재하는 경우      | `"해당 시간과 테마에는 이미 예약이 존재합니다."`   |
| `409 Conflict`              | `ConflictException`                   | 이미 취소된 예약을 변경하거나 다시 취소하는 경우        | `"이미 취소된 예약입니다."`               |
| `409 Conflict`              | `ConflictException`                   | 예약이 존재하는 시간을 삭제하는 경우               | `"예약이 존재하는 시간은 삭제할 수 없습니다."`    |
| `500 Internal Server Error` | `Exception`                           | 서버 내부 오류                           | `"서버에 일시적인 문제가 발생했습니다."`        |
* * *

# 고민 키워드 메모

## 테스트 분류
API, 서비스 어디서 테스트하는 것이 맞고, 누가 우선적인지 고민된다.

## 상태 코드
### 컨트롤러

**Spring 컨트롤러 파라미터/데이터 바인딩 예외 정리** 
해당 예외들은 클라이언트의 잘못된 요청으로 인해 발생하므로, 모두 기본적으로 HTTP 상태 코드 400 Bad Request를 반환한다.

| 예외 클래스 | 상황 요약 | 주로 연관된 어노테이션 |
| --- | --- | --- |
| **`HttpMessageNotReadableException`** | **데이터 구조/문법 오류** (파싱 불가능) | `@RequestBody` |
| **`MissingServletRequestParameterException`** | **필수 파라미터 누락** (값 자체가 없음) | `@RequestParam` |
| **`MethodArgumentTypeMismatchException`** | 값은 존재하나 **데이터 타입 변환 실패** | `@RequestParam`, `@PathVariable` |
| **`MethodArgumentNotValidException`** | 객체 변환은 성공했으나 **유효성 검사 실패** | `@RequestBody` + `@Valid` |
* * *

### 1. HttpMessageNotReadableException (형식부터 틀림)
-   **발생 시점**: `HttpMessageConverter`가 요청 본문(Body)을 읽어 Java 객체로 변환하려는 초기 단계.
-   **상세 원인**: 클라이언트가 보낸 JSON 데이터 등의 문법이 틀렸거나(예: 중괄호 `{` 누락, 쉼표 `,` 오타), 형식이 완전히 깨져서 파싱 자체가 불가능할 때 발생합니다.

### 2. MissingServletRequestParameterException (형식은 맞는데 없음)
-   **발생 시점**: 요청 URL의 쿼리 스트링이나 폼(Form) 데이터에서 값을 추출하는 단계.
-   **상세 원인**: `@RequestParam`의 `required` 속성은 기본값이 `true`입니다. 클라이언트가 이 필수 파라미터를 요청에 아예 포함하지 않고 보냈을 때 발생합니다.

### 3. MethodArgumentTypeMismatchException (형식은 맞고 있는데 타입 변환 잘못됨)
-   **발생 시점**: 추출한 문자열 데이터를 컨트롤러가 요구하는 특정 Java 타입(Integer, Long, Enum 등)으로 변환(Binding)하는 단계.
-   **상세 원인**: 파라미터 값은 전달되었으나 타입이 맞지 않을 때 발생합니다.
-   **예시**: `/api/items?id=abc` 요청을 보냈으나, 컨트롤러에서는 `@RequestParam Long id`로 선언되어 있어 문자열 `"abc"`를 숫자로 바꿀 수 없는 경우.

### 4. MethodArgumentNotValidException (유효성 검증 실패)
-   **발생 시점**: 데이터 파싱 및 객체 변환이 모두 성공한 직후, Bean Validation(`@Valid` 또는 `@Validated`)이 작동하는 단계.
-   **상세 원인**: 클라이언트가 보낸 데이터가 문법적으로도 맞고 타입도 맞아서 객체로 잘 만들어졌지만, 개발자가 설정한 **비즈니스 제약 조건(Validation)을 위반**했을 때 발생합니다.
-   **예시**: 객체 내부에 `@Min(10) int age` 조건이 있는데 `age` 값으로 `5`가 들어오거나, `@NotBlank String name` 조건에 빈 문자열 `""`이 들어온 경우.

## 제어할 수 없는 시간
컨트롤러 vs 서비스(클락)
