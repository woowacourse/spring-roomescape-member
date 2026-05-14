# 방탈출 사용자 예약

## 기능 요구 사항

### 부적절한 요청 거부

- [x] 지나간 날짜/시간에 대한 예약은 생성할 수 없다.
- [x] 같은 날짜/시간/테마에 이미 예약이 있으면 중복 예약할 수 없다.
  - [x] CANCELED 상태는 예약할 수 있다.
- [x] 예약이 존재하는 시간및 테마를 삭제할 수 없다.
- 유효하지 않은 값의 예약을 생성할 수 없다.
    - [x] 빈 이름
    - [x] 잘못된 날짜 형식
    - [x] 시간 정보 없음
    - [x] 테마 정보 없음

### 내 예약 조회/변경/취소

- [x] 사용자가 자신의 이름으로 본인의 예약 목록을 조회할 수 있다.
- [x] 사용자가 본인의 예약을 취소할 수 있다.
- [x] 사용자가 본인의 예약의 날짜·시간을 변경할 수 있다.

---

## 에러 응답 명세

### 응답 형식

모든 에러 응답은 아래 JSON 구조를 따른다.

```json
{
  "code": "에러_코드",
  "message": "사용자에게 표시할 에러 메시지"
}
```
- `code` : 클라이언트가 에러 종류를 구분할 수 있는 코드 메세지
- `message` : 사용자가 읽을 수 있는 설명

### HTTP 상태 코드별 에러 코드 목록

#### 400 Bad Request — 유효하지 않은 입력값

요청 본문의 형식 또는 값이 잘못된 경우.

| code | 발생 상황 | message 예시 |
|---|---|---|
| `VALIDATION_FAILED` | `@Valid` 검증 실패 (null, 빈 값, 범위 초과 등) | `요청 값 검증에 실패했습니다.` |
| `INVALID_REQUEST` | 도메인 레이어의 값 검증 실패 (빈 이름, null 필드 등) | `예약자 이름은 반드시 입력해야 합니다.` |

**예시**

```
POST /reservations
{ "name": "", "date": "2026-06-01", "timeId": 1, "themeId": 1 }
```

```json
HTTP/1.1 400 Bad Request
{
  "code": "VALIDATION_FAILED",
  "message": "요청 값 검증에 실패했습니다."
}
```

---

#### 404 Not Found — 존재하지 않는 리소스

요청한 ID에 해당하는 리소스가 없는 경우.

| code | 발생 상황 | message 예시 |
|---|---|---|
| `RESOURCE_NOT_FOUND` | 예약 ID 없음 | `해당 ID의 예약이 존재하지 않습니다. ID: 99` |
| `RESOURCE_NOT_FOUND` | 예약 시간 ID 없음 | `해당 ID의 예약 시간이 존재하지 않습니다. ID: 99` |
| `RESOURCE_NOT_FOUND` | 테마 ID 없음 | `해당 ID의 테마가 존재하지 않습니다. ID: 99` |

**예시**

```
GET /admin/reservations/99
```

```json
HTTP/1.1 404 Not Found
{
  "code": "RESOURCE_NOT_FOUND",
  "message": "해당 ID의 예약이 존재하지 않습니다. ID: 99"
}
```

---

#### 409 Conflict — 중복 리소스

동일한 리소스가 이미 존재하는 경우.

| code | 발생 상황 | message 예시 |
|---|---|---|
| `DUPLICATE_RESOURCE` | 같은 날짜·시간·테마 예약이 이미 존재 | `이미 해당 날짜와 시간에 예약이 존재합니다.` |
| `DUPLICATE_RESOURCE` | 동일한 시작 시각의 예약 시간이 이미 존재 | `해당 시간이 이미 존재합니다.` |
| `DUPLICATE_RESOURCE` | 동일한 이름의 테마가 이미 존재 | `이미 존재하는 테마 이름입니다.` |

**예시**

```
POST /reservations
{ "name": "홍길동", "date": "2026-06-01", "timeId": 1, "themeId": 1 }
```

```json
HTTP/1.1 409 Conflict
{
  "code": "DUPLICATE_RESOURCE",
  "message": "이미 해당 날짜와 시간에 예약이 존재합니다."
}
```

---

#### 422 Unprocessable Entity — 서비스 정책 위반

입력값 형식은 올바르지만, 비즈니스 규칙에 위반되는 경우.

| code | 발생 상황 | message 예시 |
|---|---|---|
| `BUSINESS_RULE_VIOLATION` | 과거 날짜·시간으로 예약 생성 시도 | `과거 시각으로는 예약할 수 없습니다.` |
| `BUSINESS_RULE_VIOLATION` | 이미 취소된 예약을 취소 시도 | `이미 취소된 예약입니다.` |
| `BUSINESS_RULE_VIOLATION` | 이미 완료된 예약을 취소 시도 | `이미 이용 완료된 예약은 취소할 수 없습니다.` |
| `BUSINESS_RULE_VIOLATION` | 취소/완료된 예약을 수정 시도 | `이미 취소되었거나 완료된 예약은 수정할 수 없습니다.` |
| `BUSINESS_RULE_VIOLATION` | 예약이 존재하는 시간 삭제 시도 | `이 시간을 참조하는 예약이 있어 삭제할 수 없습니다. ID: 1` |
| `BUSINESS_RULE_VIOLATION` | 예약이 존재하는 테마 삭제 시도 | `이 테마를 참조하는 예약이 있어 삭제할 수 없습니다. ID: 1` |
| `BUSINESS_RULE_VIOLATION` | 이름이 10글자를 초과 | `이름은 10글자 이하여야 합니다. (현재 이름의 글자 수: 11)` |
| `BUSINESS_RULE_VIOLATION` | 검색 기간의 from이 to보다 늦음 | `from 은 to 보다 이전이어야 합니다.` |

**예시**

```
POST /reservations
{ "name": "홍길동", "date": "2020-01-01", "timeId": 1, "themeId": 1 }
```

```json
HTTP/1.1 422 Unprocessable Entity
{
  "code": "BUSINESS_RULE_VIOLATION",
  "message": "과거 시각으로는 예약할 수 없습니다."
}
```

---

#### 500 Internal Server Error — 서버 내부 오류

예상하지 못한 서버 에러. 원인은 서버 로그에만 기록되며, 클라이언트에는 고정된 메시지만 반환된다.

| code | 발생 상황 | message |
|---|---|---|
| `INTERNAL_ERROR` | 처리되지 않은 모든 예외 | `일시적인 오류가 발생했습니다.` |

```json
HTTP/1.1 500 Internal Server Error
{
  "code": "INTERNAL_ERROR",
  "message": "일시적인 오류가 발생했습니다."
}
```

---

#### 스프링 기본 예외

`ResponseEntityExceptionHandler` 를 상속받아 해결

`MethodArgumentNotValidException` 는 override를 통해 정제된 ErrorResponse 사용자에게 반환한다. 이외의 `HttpMessageNotReadableException` 
`MethodArgumentTypeMismatchException` 등과 같은 나머지 기본 예외들은 스프링 기본 응답 형식을 이용한다.

이유: @Valid에 @NotNull, @Size, @Positive 같은 기본 입력 형식 검증은 사용자가 자주 보게 될 것이기 때문에 이 예외는 @Override해서 정제된 메세지를 주는 건 의미가 있다고 생각했습니다.
