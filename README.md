### 📝 기능 구현 목록

#### 1. 서비스 정책 검증 (예외 발생)
- [x] 지나간 날짜·시간에 대한 예약 생성 검증 로직 추가
- [x] 같은 날짜, 시간, 테마에 대한 중복 예약 검증 로직 추가
- [x] 예약이 존재하는 시간을 삭제하려는 경우의 검증 로직 추가
- [x] 유효하지 않은 입력값(빈 이름, 잘못된 날짜 형식 등) 검증 로직 추가

#### 2. 에러 응답 및 예외 처리
- [x] 글로벌 예외 처리기(`@RestControllerAdvice`) 구현
- [x] 예외 상황별 커스텀 에러 응답 본문 및 상태 코드 정의
- [x] 500 Internal Server Error 발생 시 사용자에게 내부 구조가 노출되지 않도록 에러 응답 처리

#### 3. 예약 관리 기능 (조회/변경/취소)
- [x] 사용자가 자신의 이름으로 예약 목록을 조회하는 API 및 로직 구현
- [x] 사용자가 본인의 예약을 취소하는 API 및 로직 구현
- [x] 사용자가 본인의 예약 날짜·시간을 변경하는 API 및 로직 구현
- [x] 변경/취소 시 발생하는 엣지 케이스(이미 지난 예약 취소, 변경하려는 시간이 이미 차 있는 경우 등) 예외 처리

#### 4. 클라이언트 연동
- [x] 에러 발생 시 사용자에게 의미 있는 메시지가 표시되도록 브라우저 화면 연동
- [x] 내 예약 조회/변경/취소 화면 동작 구현

---

### 에러 응답 명세

#### 1. 공통 에러 응답 포맷
```json
{
  "errorCode": "ERROR_CODE_STRING",
  "message": "사용자가 이해할 수 있는 에러 메시지"
}
```

#### 2. 에러 코드 리스트 
| Http Status | Error Code | Message | 발생 상황 |
| :--- | :--- | :--- | :--- |
| 400 Bad Request | `INVALID_INPUT` | 입력값이 올바르지 않습니다. | 빈 이름 등 유효성 검사 실패, 잘못된 인자 |
| 400 Bad Request | `BAD_REQUEST` | 지나간 날짜/시간은 예약할 수 없습니다. | 현재 시간보다 이전 시간을 예약/변경하려는 경우 |
| 400 Bad Request | `BAD_REQUEST` | 이미 지난 예약은 취소할 수 없습니다. | 현재 시간보다 이전의 예약을 취소하려는 경우 |
| 404 Not Found | `NOT_FOUND` | 존재하지 않는 예약입니다. | 존재하지 않는 예약 ID로 조회/변경/취소 요청 |
| 409 Conflict | `CONFLICT` | 이미 예약된 시간입니다. | 같은 날짜, 시간, 테마에 예약이 존재하는 경우 |
| 409 Conflict | `CONFLICT` | 예약이 존재하는 시간은 삭제할 수 없습니다. | 예약이 있는 시간 슬롯을 삭제하려는 경우 |
| 500 Internal Server Error | `INTERNAL_SERVER_ERROR` | 일시적인 오류가 발생했습니다. | 예상치 못한 서버 오류 |

### API 명세

---

#### 예약 목록 조회 (이름 기준)

| Method | URL |
| :--- | :--- |
| `GET` | `/reservations?name={name}` |

| Status | 설명 |
| :--- | :--- |
| 200 OK | 해당 이름의 예약 목록 반환 |

**Response Body**
```json
[
  {
    "id": 1,
    "name": "현미밥",
    "date": "2026-08-05",
    "time": {
      "id": 1,
      "startAt": "10:00:00"
    },
    "themeId": 1,
    "themeName": "테마A"
  }
]
```

> 특정 사용자의 예약을 조건으로 검색하는 의미이므로 Path Variable이 아닌 Query Parameter 사용.

---

#### 예약 변경

| Method | URL |
| :--- | :--- |
| `PATCH` | `/reservations/{id}` |

**Request Body**
```json
{
  "date": "2026-09-01",
  "timeId": 2
}
```

| Status | 설명 |
| :--- | :--- |
| 200 OK | 변경된 예약 반환 |
| 400 Bad Request | 유효하지 않은 날짜 등 입력 오류, 지나간 예약/시간 |
| 404 Not Found | 존재하지 않는 id |
| 409 Conflict | 변경하려는 시간에 이미 예약 존재 |

> `PUT`은 리소스 전체를 교체하는 의미로 예약자 이름까지 변경하는 것은 의도하지 않음. 날짜·시간 일부만 수정하는 부분 업데이트이므로 `PATCH` 사용.

**Response Body**
```json
{
  "id": 1,
  "name": "현미밥",
  "date": "2026-09-01",
  "time": {
    "id": 2,
    "startAt": "14:00:00"
  },
  "themeId": 1,
  "themeName": "테마A"
}
```

---

#### 예약 취소

| Method | URL |
| :--- | :--- |
| `DELETE` | `/reservations/{id}` |

| Status | 설명 |
| :--- | :--- |
| 204 No Content | 삭제 성공 |
| 400 Bad Request | 이미 지난 예약 취소 시도 |
| 404 Not Found | 존재하지 않는 id |

> 취소는 예약 리소스를 제거하는 행위이므로 `DELETE`. 어떤 예약을 삭제할지는 id로 식별하므로 Path Variable 사용.
