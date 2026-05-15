## 기능 요구 사항
### API

- [X] 사용자가 자신의 이름으로 본인의 예약 목록을 조회
   - [X] 전체 예약 조회는 관리자만 가능
- [X] 사용자가 본인의 예약을 취소
   - [X] 지난 예약일 경우 취소 불가
   - [X] 이름과 예약 정보가 일치하지 않으면 취소 불가
- [X] 사용자가 본인 예약의 날짜/시간을 변경
   - [X] 지난 날짜는 불가능
   - [X] 이미 예약 있으면 불가능

### 예외 처리

- [X] 예약
   - [X] 잘못된 요청 입력 값
   - [X] 지나간 날짜/시간에 대한 예약 생성
   - [X] 존재하지 않는 시간에 예약 생성 요청
   - [X] 존재하지 않는 테마로 예약 생성 요청
   - [X] 같은 날짜+시간+테마에 이미 예약이 있으면 중복 예약을 거부
- [X] 예약 시간
   - [X] 잘못된 요청 입력 값
   - [X] 이미 있는 예약 시간을 중복 생성 요청
   - [X] 예약이 존재하는 시간을 삭제
- [X] 테마
   - [X] 잘못된 요청 입력값
   - [X] 같은 이름의 테마 생성 요청

## API 명세

| 기능 | Method | URL | 설명 |
| --- | --- | --- | --- |
| 전체 테마 조회 | GET | `/api/themes` | 전체 테마 조회 |
| 단일 테마 조회 | GET | `/api/themes/{id}` | 특정 테마 조회 |
| 인기 테마 조회 | GET | `/api/themes/trending?from={from}&to={to}&limit={limit}` | 인기 테마 조회 |
| 전체 테마 조회 (관리자) | GET | `/admin/themes` | 전체 테마 조회 |
| 단일 테마 조회 (관리자) | GET | `/admin/themes/{id}` | 특정 테마 조회 |
| 테마 생성 (관리자) | POST | `/admin/themes` | 테마 생성 |
| 테마 삭제 (관리자) | DELETE | `/admin/themes/{id}` | 테마 삭제 |
| 전체 예약 시간 조회 | GET | `/api/times` | 전체 예약 시간 조회 |
| 예약 가능 시간 조회 | GET | `/api/times?themeId={themeId}&date={date}` | 날짜/테마 기준 예약 가능 시간 조회 |
| 예약 시간 생성 (관리자) | POST | `/admin/times` | 예약 시간 생성 |
| 예약 시간 삭제 (관리자) | DELETE | `/admin/times/{id}` | 예약 시간 삭제 |
| 전체 예약 조회 | GET | `/admin/reservations` | 전체 예약 조회 |
| 사용자 예약 조회 | GET | `/api/reservations?name={name}` | 이름으로 본인 예약 조회 |
| 예약 생성 | POST | `/api/reservations` | 예약 생성 |
| 예약 삭제 | DELETE | `/api/reservations/{id}?name={name}` | 이름 확인 후 본인 예약 취소 |

### 응답 상태 코드 규칙

| 상태 코드 | 의미 |
| --- | --- |
| 200 OK | 조회 성공 |
| 201 Created | 생성 성공 |
| 204 No Content | 삭제 성공 |
| 400 Bad Request | 잘못된 요청 형식 |
| 403 Forbidden | 본인 예약 아님 |
| 404 Not Found | 리소스 없음 |
| 409 Conflict | 중복/사용 중 |
| 422 Unprocessable Entity | 비즈니스 규칙 위반 |
| 500 Internal Server Error | 예상하지 못한 서버 처리 실패 |
