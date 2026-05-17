## 기능 명세

### 기능 명세 체크리스트

#### 웹 사용자

- [x] **메인 화면**: 접속 시 전체 테마 목록을 확인할 수 있다.
- [x] **예약 페이지 이동**: 예약하기 버튼을 통해 예약 신청 페이지(`/reservations`)로 이동할 수 있다.
- [x] **예약 신청**: 예약 페이지에서 이름, 날짜, 테마, 예약 시간을 선택하여 예약을 완료할 수 있다.
- [x] **내 예약 조회**: 이름을 입력하여 자신의 예약 목록을 조회할 수 있다 (`/my-reservation`).
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

### 응답 본문 형식 (JSON)
| 필드명 | 설명 | 비고 |
| :--- | :--- | :--- |
| `status` | HTTP 상태 코드 | |
| `title` | 에러의 짧은 요약 | 사용자에게 제목으로 노출 가능 |
| `detail` | 구체적인 에러 메시지 | 사용자가 다음에 할 수 있는 행동 제안 포함 |
| `instance` | 에러가 발생한 API 경로 | |
| `errors` | 입력값 검증 실패 리스트 | `400 Bad Request` 시에만 포함 |

### 상태 코드별 발생 케이스
| 상태 코드 | 의미 | 발생 케이스 |
| :--- | :--- | :--- |
| **400** | Bad Request | 필수 파라미터 누락, @Valid 검증 실패 |
| **409** | Conflict | 중복 예약 시도, 예약이 있는 시간/테마 삭제 시도 |
| **422** | Unprocessable Entity | 존재하지 않는 ID 조회, 과거 시점 예약/수정/취소 시도, 형식 오류 |
| **500** | Internal Server Error | 서버 내부의 예기치 못한 장애 |
