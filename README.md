## 기능 명세

### 기능 명세 체크리스트

#### 웹 사용자

- [x] **메인 화면**: 접속 시 전체 테마 목록을 확인할 수 있다.
- [x] **예약 페이지 이동**: 예약하기 버튼을 통해 예약 신청 페이지(`/reservations`)로 이동할 수 있다.
- [x] **예약 신청**: 예약 페이지에서 이름, 날짜, 테마, 예약 시간을 선택하여 예약을 완료할 수 있다.
- [x] **내 예약 조회**: 이름을 입력하여 자신의 예약 목록을 조회할 수 있다 (`/my-reservations`).
- [x] **내 예약 변경**: 예약 목록에서 날짜 및 시간을 변경할 수 있다.
- [x] **내 예약 취소**: 예약 목록에서 자신의 예약을 취소할 수 있다.

#### REST API 클라이언트

- **공통**
  - [x] 유효하지 않은 입력값(빈 이름, 잘못된 날짜 형식 등)을 거부한다 (400, 422).
  - [x] 에러 발생 시 사용자 친화적인 메시지를 담은 RFC 7807 형식의 응답을 반환한다.

- **테마 (Theme)**
  - [x] `GET /themes`: 전체 테마 목록을 조회한다.
  - [x] `GET /themes/popular`: 인기가 많은 테마 목록을 조회한다.
  - [x] `GET /themes/{id}`: 특정 테마의 상세 정보를 조회한다.
- **예약 (Reservation)**
  - [x] 예약 생성
    - [x] `POST /api/reservations`: 새로운 예약을 생성한다.
    - [x] 지나간 날짜·시간에 대한 예약 생성은 불가능하다 (422).
    - [x] 같은 날짜+시간+테마에 이미 예약이 있으면 중복 예약을 거부한다 (409).
  - [x] 내 예약 조회
    - [x] `GET /api/reservations?username=...`: 입력한 이름에 해당하는 예약 목록을 조회한다.
  - [x] 예약 수정
    - [x] `PATCH /api/reservations/{id}`: 예약의 날짜 및 시간을 변경한다.
    - [x] 이미 지난 예약은 수정할 수 없다 (422).
    - [x] 변경하려는 시간이 이미 차 있으면 거부한다 (409).
  - [x] 예약 취소
    - [x] `DELETE /api/reservations`: 이름, 날짜, 시간ID, 테마ID 정보를 통해 예약을 취소한다.
    - [x] 이미 지난 예약은 취소할 수 없다 (422).
- **예약 시간 (Time)**
  - [x] `GET /times`: 등록된 모든 예약 시간 목록을 조회한다.
  - [x] `GET /times/reserved`: 특정 날짜와 테마에 대해 이미 예약된 시간들을 조회한다.

#### 관리자

- [x] **예약 내역 관리**: 서비스의 모든 예약 내역을 한눈에 조회할 수 있다 (`GET /admin/reservations`).
- [x] **테마 설정**:
  - [x] 새로운 방탈출 테마를 시스템에 등록할 수 있다 (`POST /admin/themes`).
  - [x] 더 이상 운영하지 않는 테마를 삭제할 수 있다 (`DELETE /admin/themes/{id}`).
- [x] **시간 설정**:
  - [x] 예약 가능한 시간대를 새롭게 추가할 수 있다 (`POST /admin/times`).
  - [x] 시간 삭제
    - [x] 특정 예약 시간대를 시스템에서 삭제할 수 있다 (`DELETE /admin/times/{id}`).
    - [x] 예약이 존재하는 시간을 삭제할 수 없다 (409).

---

## API 명세

### 1. 관리자 (Admin)

#### 모든 예약 조회
- `GET /admin/reservations`

#### 예약 시간 추가
- `POST /admin/times`
- Body: `{"startAt": "15:40"}`
- Response: `201 Created`

#### 예약 시간 삭제
- `DELETE /admin/times/{id}`
- Response: `204 No Content`

#### 테마 추가
- `POST /admin/themes`
- Body: `{"name": "...", "description": "...", "thumbnailUrl": "..."}`
- Response: `201 Created`

#### 테마 삭제
- `DELETE /admin/themes/{id}`
- Response: `204 No Content`

### 2. 테마 (Theme)

#### 전체 테마 목록 조회
- `GET /themes`

#### 인기 테마 목록 조회
- `GET /themes/popular`

#### 특정 테마 상세 조회
- `GET /themes/{id}`

### 3. 예약 (Reservation)

#### 예약 생성
- `POST /api/reservations`
- Body: `{"date": "2026-05-15", "timeId": 1, "themeId": 1, "name": "루드비코"}`
- Response: `201 Created`

#### 내 예약 목록 조회
- `GET /api/reservations?username=루드비코`
- Response: `200 OK`

#### 예약 변경
- `PATCH /api/reservations/{id}`
- Body: `{"date": "2026-05-16", "timeId": 2}`
- Response: `204 No Content`

#### 예약 취소
- `DELETE /api/reservations?name=루드비코&date=2026-05-15&timeId=1&themeId=1`
- Response: `204 No Content`

### 4. 예약 시간 (Time)

#### 전체 예약 시간 목록 조회
- `GET /times`

#### 예약된 시간 목록 조회
- `GET /times/reserved?themeId=1&selectedDate=2026-05-15`

---

## 에러 응답 명세

### 1. 응답 본문 형식 (JSON)
| 필드명 | 설명 | 비고 |
| :--- | :--- | :--- |
| `status` | HTTP 상태 코드 | |
| `title` | 에러의 짧은 요약 | 사용자에게 제목으로 노출 가능 |
| `detail` | 구체적인 에러 메시지 | 사용자가 다음에 할 수 있는 행동 제안 포함 |
| `instance` | 에러가 발생한 API 경로 | |
| `errors` | 입력값 검증 실패 리스트 | `400 Bad Request` (@Valid 실패) 시에만 포함 |

### 2. 비즈니스 예외 발생 케이스 및 메시지
클라이언트가 에러 상황을 정확히 판단하고 사용자에게 적절한 피드백을 줄 수 있도록, 주요 비즈니스 상황별 응답을 정의합니다.

| 상태 코드 | 에러 제목 (`title`) | 발생 상황 | 사용자 안내 메시지 (`detail`) |
| :--- | :--- | :--- | :--- |
| **400** | 잘못된 요청 | 필수 값 누락 | "요청하신 데이터의 형식이 올바르지 않거나 필수 값이 누락되었습니다." |
| **409** | 중복된 예약 | 동일 테마/시간 중복 예약 시 | "{날짜} {시간} ({테마명}) 테마는 이미 예약이 완료되었습니다. 다른 시간을 선택해 주세요." |
| **409** | 삭제 불가 | 예약이 존재하는 시간/테마 삭제 시 | "해당 정보로 등록된 예약이 존재하여 삭제할 수 없습니다. 관련 예약을 먼저 정리해 주세요." |
| **422** | 조회 실패 | 존재하지 않는 ID 조회/수정/삭제 시 | "해당 정보를 찾을 수 없어 요청을 처리하지 못했습니다. 입력 정보를 다시 확인해 주세요." |
| **422** | 형식 오류 | 날짜/시간 형식이 올바르지 않을 시 | "날짜 또는 시간 형식이 올바르지 않습니다. (예: 2024-05-14, 10:00)" |
| **422** | 예약 불가 | 과거 날짜/시간으로 예약 시도 시 | "이미 지난 날짜나 시간은 예약할 수 없습니다." |
| **422** | 수정/취소 불가 | 이미 지난 예약에 대한 변경/취소 시 | "이미 지난 예약은 수정하거나 취소할 수 없습니다." |

### 3. 응답 예시

#### 입력값 검증 실패 (400 Bad Request)

```json
{
  "status": 400,
  "title": "잘못된 요청",
  "detail": "입력값 검증에 실패했습니다.",
  "instance": "/api/reservations",
  "errors": [
    {
      "field": "name",
      "value": "",
      "message": "이름은 필수입니다."
    }
  ]
}
```

#### 리소스 충돌 (409 Conflict)

중복된 데이터 입력이나 연관 데이터가 존재하는 리소스 삭제 시도 시 반환됩니다.

```json
{
  "status": 409,
  "title": "중복된 예약",
  "detail": "2026-05-15 13:00 (우테코 탈출) 테마는 이미 예약이 완료되었습니다. 다른 시간을 선택해 주세요.",
  "instance": "/api/reservations"
}
```

#### 비즈니스 정책 위반 (422 Unprocessable Entity)

존재하지 않는 리소스 접근이나 비즈니스 규칙 위반 시 반환됩니다.

```json
{
  "status": 422,
  "title": "조회 실패",
  "detail": "해당하는 예약 정보를 찾을 수 없어 요청을 처리하지 못했습니다. 예약 번호나 정보를 다시 확인해 주세요.",
  "instance": "/api/reservations/99"
}
```

#### 서버 내부 오류 (500 Internal Server Error)

처리되지 않은 예기치 못한 예외 발생 시 반환됩니다.

```json
{
  "status": 500,
  "title": "서버 오류",
  "detail": "서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.",
  "instance": "/api/reservations"
}
```
