## 기능 요구 사항
### API

- [X]  사용자가 자신의 이름으로 본인의 예약 목록을 조회할 수 있다
    - [x]  전체 예약 조회는 관리자만 가능하다
- [ ]  사용자가 본인의 예약을 취소할 수 있다
- [ ]  사용자가 본인 예약의 날짜/시간을 변경할 수 있다

### 예외 처리

- [ ]  지나간 날짜/시간에 대한 예약 생성은 불가능하다
- [ ]  같은 날짜+시간+테마에 이미 예약이 있으면 중복 예약을 거부한다
- [ ]  예약이 존재하는 시간을 삭제할 수 없다
- [ ]  유효하지 않은 입력값을 거부한다

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
|  |  |  |  |
| 전체 예약 시간 조회 | GET | `/api/times`  | 전체 예약 시간 조회 |
| 예약 가능 시간 조회 | GET | `/api/times?themeId={themeId}&date={date}` | 날짜/테마 기준 예약 가능 시간 조회 |
| 예약 시간 생성 (관리자) | POST | `/admin/times` | 예약 시간 생성 |
| 예약 시간 삭제 (관리자) | DELETE | `/admin/times/{id}` | 예약 시간 삭제 |
|  |  |  |  |
| 전체 예약 조회 | GET | `/admin/reservations` | 전체 예약 조회 |
| 사용자 예약 조회 | GET | `/api/reservations` | 닉네임으로 예약 조회 |
| 예약 생성 | POST | `/api/reservations` | 예약 생성 |
| 예약 삭제 | DELETE | `/api/reservations/{id}` | 예약 삭제 |

### 응답 상태 코드 규칙

| 상태 코드 | 의미 |
| --- | --- |
| 200 OK | 조회 성공 |
| 201 Created | 생성 성공 |
| 204 No Content | 삭제 성공 |
| 500 Internal Server Error | 예약 불가 등 서버 처리 실패 |