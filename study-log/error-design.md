# 에러 응답 설계 문서

## 에러 응답 형식 (토론 그룹에서 정한 형식)

```json
{
  "code": "에러_코드",
  "path": "/요청_URI",
  "message": "어떤 값이 왜 문제인지, 해결 방법까지 포함한 상세한 설명"
}
```

- `code`: 프론트에서 예외를 쉽게 분기할 수 있게 하기 위해 제공
- `path`: 예외가 발생한 URI
- `message`: 문제 원인 + 해결 방법까지 포함한 상세 설명 (`action` 필드 제거, message에 통합)
- 에러 응답의 수신 대상은 **프론트엔드 개발자**이다.

---

## 공통: 입력 형식 오류

형식 오류(null, 빈값, 타입 불일치)는 DTO `@Valid`와 Jackson 역직렬화 단계에서 일괄 처리한다.

| 상황 | 상태 코드 | code | message |
|:---:|:---:|:---:|:---|
| 필수 값 누락 / 빈값 (`@Valid` 위반) | 400 | INVALID_REQUEST_BODY | 입력값이 없습니다. |
| JSON 타입 불일치 / 잘못된 형식 | 400 | INVALID_REQUEST_BODY | 입력 형식이 잘못되었습니다. |
| 쿼리스트링 누락 | 400 | INVALID_QUERY_STRING | 쿼리 스트링(파라미터)가 오지 않았습니다. |

---

## 예약 (Reservation)

### POST /reservations

| 상황 | 상태 코드 | code | message |
|:---:|:---:|:---:|:---|
| 이름 길이 위반 (2~20자) | 400 | RESERVATION_WRONG_NAME | 이름은 2자 이상 20자 이하 입니다. |
| 이미 지난 날짜로 예약 | 400 | RESERVATION_WRONG_DATE | 이미 지난 날짜로는 예약이 불가능합니다. |
| 오늘 날짜인데 이미 지난 시간으로 예약 | 400 | RESERVATION_WRONG_TIME | 이미 지난 시간으로는 예약이 불가능합니다. |
| 존재하지 않는 timeId | 404 | RESERVATION_TIMEID_NOT_FOUND | 해당 시간은 존재하지 않습니다. |
| 존재하지 않는 themeId | 404 | RESERVATION_THEMEID_NOT_FOUND | 해당 테마는 존재하지 않습니다. |
| 같은 날짜+시간+테마 중복 예약 | 409 | RESERVATION_DUPLICATE | 중복된 예약이 존재합니다. 다른 날짜, 시간, 테마로 예약해 주세요. |

### DELETE /reservations/{id}

| 상황 | 상태 코드 | code | message |
|:---:|:---:|:---:|:---|
| 존재하지 않는 예약 삭제 | 404 | RESERVATION_NOT_FOUND | 해당 예약이 존재하지 않아서 삭제할 수 없습니다. 예약 아이디를 확인해주세요. |

---

## 시간 (Time)

### POST /times

| 상황 | 상태 코드 | code | message |
|:---:|:---:|:---:|:---|
| 잘못된 시간 형식 | 400 | TIME_WRONG_STARTAT | startAt의 값이 잘못되었습니다. TIME 형태에 맞게 작성해주세요. ex) 08:00 |
| 이미 존재하는 시간 | 409 | TIME_DUPLICATE | 이미 존재하는 시간이 있습니다. 다른 startAt을 입력해주세요. |

### DELETE /times/{id}

| 상황 | 상태 코드 | code | message |
|:---:|:---:|:---:|:---|
| 존재하지 않는 시간 삭제 | 404 | TIME_NOT_FOUND | 해당 시간이 존재하지 않아서 삭제할 수 없습니다. 시간 아이디를 확인해주세요. |
| 예약이 존재하는 시간 삭제 | 409 | TIME_CANNOT_DELETE | 해당 시간의 예약이 존재하기에 삭제할 수 없습니다. 해당 시간의 모든 예약을 삭제한 후 재시도 해주세요. |

---

## 테마 (Theme)

### POST /themes

| 상황 | 상태 코드 | code | message |
|:---:|:---:|:---:|:---|
| 이름 길이 위반 (2~20자) | 400 | THEME_WRONG_NAME | 이름은 2자 이상 20자 이하 입니다. |

### DELETE /themes/{id}

| 상황 | 상태 코드 | code | message |
|:---:|:---:|:---:|:---|
| 존재하지 않는 테마 삭제 | 404 | THEME_NOT_FOUND | 해당 테마가 존재하지 않아서 삭제할 수 없습니다. 테마 아이디를 확인해주세요. |

### GET /themes/ranks

| 상황 | 상태 코드 | code | message |
|:---:|:---:|:---:|:---|
| limit 음수 또는 0 | 400 | THEME_RANK_INVALID_LIMIT | limit는 0 이하일 수 없습니다. |
| limit 범위 초과 (30 초과) | 400 | THEME_RANK_INVALID_LIMIT | limit는 30 초과 일 수 없습니다. |

---

## 공통 (예상치 못한 서버 에러)

| 상황 | 상태 코드 | code | message |
|:---:|:---:|:---:|:---|
| 처리되지 않은 예외 | 500 | INTERNAL_ERROR | 요청 처리에 문제가 발생했습니다. |

---

## 설계 판단 메모

- `action` 필드 제거: `message`를 구체적으로 작성하면 action 없이도 문제를 파악할 수 있다.
  해결 방법이 필요한 경우 message에 통합한다.
- 에러 메시지는 사용자에게 노출될 수 있음을 가정하고 작성한다. 서버 내부 정보(SQL, 테이블명, 스택트레이스 등)는 절대 포함하지 않는다.
- 입력 형식 오류(null, 빈값, 타입 불일치)는 DTO `@Valid`와 Jackson 역직렬화 단계에서 일괄 처리하여 `INVALID_REQUEST_BODY`로 응답한다. 개별 에러코드로 분리하지 않는다.
- 비즈니스 규칙 위반(중복 예약, 과거 날짜 예약 등)만 `ErrorCode` enum으로 관리한다.

---

## 구현 체크리스트

### 공통 입력 형식 오류

- [x] INVALID_REQUEST_BODY — 필수 값 누락 / 빈값 (@Valid)
- [x] INVALID_REQUEST_BODY — JSON 타입 불일치 / 잘못된 형식
- [x] INVALID_QUERY_STRING — 쿼리스트링 누락

### POST /reservations

- [x] RESERVATION_WRONG_NAME — 이름 길이 위반 (2~20자)
- [x] RESERVATION_WRONG_DATE — 이미 지난 날짜로 예약
- [x] RESERVATION_WRONG_TIME — 오늘 날짜인데 이미 지난 시간으로 예약
- [x] RESERVATION_TIMEID_NOT_FOUND — 존재하지 않는 timeId
- [x] RESERVATION_THEMEID_NOT_FOUND — 존재하지 않는 themeId
- [x] RESERVATION_DUPLICATE — 같은 날짜+시간+테마 중복 예약

### DELETE /reservations/{id}

- [x] RESERVATION_NOT_FOUND — 존재하지 않는 예약 삭제

### POST /times

- [x] TIME_WRONG_STARTAT — 잘못된 시간 형식
- [x] TIME_DUPLICATE — 이미 존재하는 시간

### DELETE /times/{id}

- [x] TIME_NOT_FOUND — 존재하지 않는 시간 삭제
- [x] TIME_CANNOT_DELETE — 예약이 존재하는 시간 삭제

### POST /themes

- [x] THEME_WRONG_NAME — 이름 길이 위반

### DELETE /themes/{id}

- [x] THEME_NOT_FOUND — 존재하지 않는 테마 삭제

### GET /themes/ranks

- [x] THEME_RANK_INVALID_LIMIT — limit 음수 또는 0
- [x] THEME_RANK_INVALID_LIMIT — limit 범위 초과 (30 초과)

### 공통

- [x] INTERNAL_ERROR — 처리되지 않은 예외
