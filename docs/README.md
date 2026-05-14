# 방탈출 예약 시스템

방탈출 예약, 테마, 예약 시간을 관리하는 백엔드 서비스입니다.

사용자는 이름을 기준으로 본인의 예약을 조회하고, 예약 일정을 변경하거나 예약을 취소할 수 있습니다.
관리자는 테마와 예약 시간을 등록, 조회, 삭제할 수 있습니다.

> 현재 미션에는 로그인 기능이 없으므로, 예약자 이름(`name`)을 본인 확인 값으로 사용합니다.

---

## 목차

1. [기능 목록](#기능-목록)
2. [API 명세](#api-application-programming-interface-명세)
   - [테마](#1-테마-theme)
   - [예약 시간](#2-예약-시간-reservation-time)
   - [예약](#3-예약-reservation)
3. [예외 및 에러 응답 명세](#예외-및-에러-응답-명세)

---

## 기능 목록

### 테마

- [x] 테마를 등록한다.
- [x] 테마 목록을 조회한다.
- [x] 테마를 삭제한다.
- [x] 최근 예약 건수를 기준으로 인기 테마를 조회한다.
- [x] 테마 이름은 null이거나 빈 공백일 수 없다.
- [x] 테마 설명은 null일 수 없다.
- [x] 테마 이미지 링크는 null일 수 없다.
- [x] 테마 이미지 링크는 `/images/themes/`로 시작하는 경로이어야 한다.
- [x] 존재하지 않는 테마로 예약할 수 없다.

### 예약 시간

- [x] 예약 시간을 등록한다.
- [x] 예약 시간 목록을 조회한다.
- [x] 예약 시간을 삭제한다.
- [x] 특정 날짜와 테마에 대한 예약 가능 시간을 조회한다.
- [x] 예약 시작 시간은 null일 수 없다.
- [x] 예약 시작 시간은 `HH:mm` 형식이어야 한다.
- [x] 동일한 시작 시간을 중복 등록할 수 없다.
- [x] 예약이 존재하는 시간은 삭제할 수 없다.
- [x] 존재하지 않는 예약 시간으로 예약할 수 없다.

### 예약

- [x] 예약을 생성한다.
- [x] 전체 예약 목록을 조회한다.
- [x] 같은 날짜, 시간, 테마에 이미 예약이 있으면 중복 예약을 거부한다.
- [ ] 사용자는 이름으로 본인의 예약 목록을 조회할 수 있다.
- [ ] 사용자는 본인 예약의 날짜와 시간을 변경할 수 있다.
- [ ] 사용자는 본인 예약을 취소할 수 있다.
- [ ] 예약 이름은 null이거나 빈 공백일 수 없다.
- [ ] 예약 이름은 2자 이상 20자 이하여야 한다.
- [ ] 예약 이름은 완성형 한글, 영문, 공백만 허용한다.
- [ ] 예약 날짜는 null일 수 없다.
- [ ] 예약 날짜는 `yyyy-MM-dd` 형식이어야 한다.
- [ ] 예약 시간 식별자(`timeId`)는 null일 수 없다.
- [ ] 테마 식별자(`themeId`)는 null일 수 없다.
- [ ] 지나간 날짜와 시간으로 예약할 수 없다.
- [ ] 예약 날짜가 오늘이면 현재 서버 시간 이전의 예약 시간은 선택할 수 없다.
- [ ] 오늘 기준 30일을 초과한 날짜로 예약할 수 없다.
- [ ] 존재하지 않는 예약은 조회, 변경, 취소할 수 없다.
- [ ] 이미 취소된 예약은 변경하거나 다시 취소할 수 없다.

---

## API(Application Programming Interface) 명세

### 1. 테마 (Theme)

| 기능       | Method   | URL            | 설명                                   |
|----------|----------|----------------|--------------------------------------|
| 테마 목록 조회 | `GET`    | `/themes`      | 등록된 모든 테마 목록 반환                      |
| 인기 테마 조회 | `GET`    | `/themes/rank` | 최근 N일간 예약 순위 조회 (`days`, `limit` 필요) |
| 테마 추가    | `POST`   | `/themes`      | 새로운 테마 등록                            |
| 테마 삭제    | `DELETE` | `/themes/{id}` | 특정 테마 삭제                             |

```json
{
  "name": "고대 이집트의 비밀",
  "description": "파라오의 무덤에 숨겨진 비밀을 찾아 탈출하세요.",
  "imageUrl": "https://example.com/images/egypt.webp"
}
```

#### 테마 응답

```json
{
  "id": 1,
  "name": "고대 이집트의 비밀",
  "description": "파라오의 무덤에 숨겨진 비밀을 찾아 탈출하세요.",
  "imageUrl": "https://example.com/images/egypt.webp"
}
```

#### 인기 테마 조회 응답

```json
{
  "themes": [
    {
      "rank": 1,
      "theme": {
        "id": 1,
        "name": "고대 이집트의 비밀",
        "description": "파라오의 무덤에 숨겨진 비밀을 찾아 탈출하세요.",
        "imageUrl": "https://example.com/images/egypt.webp"
      }
    }
  ]
}
```

### 2. 예약 시간 (Time)

| 기능          | Method   | URL                                | 설명                            |
|-------------|----------|------------------------------------|-------------------------------|
| 예약 시간 목록 조회 | `GET`    | `/reservation-times`                           | 등록된 모든 예약 시간을 조회한다.           |
| 예약 가능 시간 조회 | `GET`    | `/reservation-times/available?themeId=1&date=2026-05-20` | 특정 날짜와 테마에 대한 예약 가능 시간을 조회한다. |
| 예약 시간 등록    | `POST`   | `/reservation-times`                           | 새로운 예약 시간을 등록한다.              |
| 예약 시간 삭제    | `DELETE` | `/reservation-times/{id}`                      | 특정 예약 시간을 삭제한다.               |

#### 예약 시간 등록 요청

```json
{
  "startAt": "14:00"
}
```

#### 예약 시간 응답

```json
{
  "id": 1,
  "startAt": "14:00"
}
```

#### 예약 가능 시간 조회 응답

```json
{
  "theme": {
    "id": 1,
    "name": "고대 이집트의 비밀",
    "description": "파라오의 무덤에 숨겨진 비밀을 찾아 탈출하세요.",
    "imageUrl": "https://example.com/images/egypt.webp"
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

---

### 3. 예약 (Reservation)

| 기능                  | Method | URL                               | 설명                                                |
| --------------------- | ----------- | --------------------------------- | --------------------------------------------------- |
| 전체 예약 목록 조회   | `GET`       | `/reservations`                   | 전체 예약 목록을 조회한다.                          |
| 사용자 예약 목록 조회 | `GET`       | `/reservations?name=고래`         | 사용자가 자신의 이름으로 본인 예약 목록을 조회한다. |
| 예약 생성             | `POST`      | `/reservations`                   | 새로운 예약을 생성한다.                             |
| 예약 일정 변경        | `PUT`       | `/reservations/{id}/schedule`     | 예약의 날짜와 시간 영역을 대체한다.                 |
| 예약 취소             | `PUT`       | `/reservations/{id}/cancellation` | 예약 취소를 요청한다.                               |

---

#### 예약 생성 요청

```json
{
  "name": "고래",
  "date": "2026-05-20",
  "timeId": 2,
  "themeId": 3
}
```

#### 예약 생성 응답

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
    "imageUrl": "https://example.com/images/egypt.webp"
  },
  "status": "RESERVED"
}
```

---

#### 사용자 예약 목록 조회 응답

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
        "imageUrl": "https://example.com/images/egypt.webp"
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
        "imageUrl": "https://example.com/images/zombie.webp"
      },
      "status": "CANCELLED"
    }
  ]
}
```

---

#### 예약 일정 변경 요청

예약 전체를 변경하지 않고, 예약의 일정 영역인 날짜와 시간을 대체한다.

```json
{
  "name": "고래",
  "date": "2026-05-21",
  "timeId": 3
}
```

#### 예약 일정 변경 응답

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
    "imageUrl": "https://example.com/images/egypt.webp"
  },
  "status": "RESERVED"
}
```

---

#### 예약 취소 요청

클라이언트가 예약 상태를 직접 조작하지 않고, 서버에게 예약 취소를 요청한다.
서버는 요청을 처리한 뒤 예약 상태를 `CANCELLED`로 변경한다.

```json
{
  "name": "고래"
}
```

#### 예약 취소 응답

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
    "imageUrl": "https://example.com/images/egypt.webp"
  },
  "status": "CANCELLED"
}
```

---

## 예외 및 에러 응답 명세

### 예외 구조

커스텀 예외는 HTTP 상태 코드의 맥락을 가지는 부모 예외를 상속받아 구현한다.

- `RoomEscapeException`: 최상위 도메인 예외
- `BadRequestException`: 잘못된 요청 예외, 400 응답
- `NotFoundException`: 리소스를 찾을 수 없는 예외, 404 응답
- `ConflictException`: 현재 리소스 상태와 요청이 충돌하는 예외, 409 응답

### 에러 응답 형식

모든 에러 응답은 JSON(JavaScript Object Notation) 형식으로 반환한다.

```json
{
  "message": "해당 시간과 테마에는 이미 예약이 존재합니다."
}
```

### HTTP 상태 코드 매핑 기준

보안 원칙: 소유권 불일치 상황에서는 `403 Forbidden` 대신 `404 Not Found`를 반환한다.
이를 통해 리소스 존재 여부를 노출하지 않고, Blind IDOR(Insecure Direct Object Reference, 직접 객체 참조 취약점) 공격 가능성을 낮춘다.

| 상태 코드                   | 부모 예외             | 발생 케이스                                       | 응답 메시지 예시                                 |
| --------------------------- | --------------------- | ------------------------------------------------- | ------------------------------------------------ |
| `400 Bad Request`           | `BadRequestException` | 이름 형식이 올바르지 않은 경우                    | `"이름 형식이 올바르지 않습니다."`               |
| `400 Bad Request`           | `BadRequestException` | 날짜 형식이 올바르지 않은 경우                    | `"날짜 형식이 올바르지 않습니다."`               |
| `400 Bad Request`           | `BadRequestException` | 과거 날짜 또는 시간으로 예약하는 경우             | `"과거 날짜/시간으로는 예약할 수 없습니다."`     |
| `400 Bad Request`           | `BadRequestException` | 오늘 기준 30일을 초과한 날짜로 예약하는 경우      | `"30일을 초과한 날짜로는 예약할 수 없습니다."`   |
| `404 Not Found`             | `NotFoundException`   | 존재하지 않는 예약을 조회, 변경, 취소하는 경우    | `"해당 예약을 찾을 수 없습니다."`                |
| `404 Not Found`             | `NotFoundException`   | 예약자 이름이 일치하지 않는 경우                  | `"해당 예약을 찾을 수 없습니다."`                |
| `404 Not Found`             | `NotFoundException`   | 존재하지 않는 예약 시간으로 예약하는 경우         | `"선택한 예약 시간이 존재하지 않습니다."`        |
| `404 Not Found`             | `NotFoundException`   | 존재하지 않는 테마로 예약하는 경우                | `"선택한 테마가 존재하지 않습니다."`             |
| `409 Conflict`              | `ConflictException`   | 같은 날짜, 시간, 테마에 이미 예약이 존재하는 경우 | `"해당 시간과 테마에는 이미 예약이 존재합니다."` |
| `409 Conflict`              | `ConflictException`   | 이미 취소된 예약을 변경하거나 다시 취소하는 경우  | `"이미 취소된 예약입니다."`                      |
| `409 Conflict`              | `ConflictException`   | 예약이 존재하는 시간을 삭제하는 경우              | `"예약이 존재하는 시간은 삭제할 수 없습니다."`   |
| `500 Internal Server Error` | `Exception`           | 서버 내부 오류                                    | `"서버에 일시적인 문제가 발생했습니다."`         |
