## 기능 요구사항 체크리스트

### 1단계 - 테마 도메인 추가

- [x] 테마 생성 (관리자)
- [x] 테마 삭제 (관리자)
- [x] 테마 목록 조회 (사용자, 관리자)

### 2단계 - 사용자 예약

- [x] 날짜와 테마로 필터링하여 예약 가능한 시간 목록 조회
- [x] 같은 테마에 날짜와 시간이 같은 예약이 존재하는 경우 예약이 불가능하도록 처리

### 3단계 - 인기 테마 조회

- [x] 최근 1주 동안 예약이 많았던 테마 목록 내림차순 조회
    - 지정한 최대 갯수만큼 인기 테마를 조회할 수 있다.

---

## API 문서

서버 실행 후 Swagger UI에서 API를 확인하고 테스트할 수 있습니다.

http://localhost:8080/swagger-ui/index.html

---

## API 명세

### 사용자

`GET /themes` 테마 리스트 조회
`GET /themes/{id}/times?date={date}` 예약 가능한 시간 목록 조회
`GET /themes/popular?limit={limit}` 최근 일주일 인기 테마 조회

`GET /times` 예약 시간 전체 조회

`GET /reservations/my?userId={userId}` 내 예약 조회
`POST /reservations` 예약 생성
`DELETE /reservations/{id}?userId={userId}` 내 예약 삭제

### 관리자

`GET /admin/themes` 테마 리스트 조회
`GET /admin/themes/{id}/times?date={date}` 예약 가능한 시간 목록 조회
`GET /admin/themes/popular?limit={limit}` 최근 일주일 인기 테마 조회
`POST /admin/themes` 테마 생성
`DELETE /admin/themes/{id}` 테마 삭제

`GET /admin/times` 예약 시간 전체 조회
`POST /admin/times` 예약 시간 생성
`DELETE /admin/times/{id}` 예약 시간 삭제

`GET /admin/reservations` 전체 예약 조회
`GET /admin/reservations/{id}` 예약 단건 조회
`POST /admin/reservations` 예약 생성
`DELETE /admin/reservations/{id}` 예약 삭제

---

## 설계 결정 사항

### 예약 가능 시간 조회 — 전체 슬롯 + `isReserved` 플래그 반환

전체 테마 예약 시간에 `isReserved` 필드를 포함해 예약 가능 / 불가 상태를 단일 응답으로 표현한다.
프론트엔드에서 이미 예약된 시간에 대한 표현이 필요하다고 생각했다.

### 인기 테마 limit 서버 상한 (최대 10개)

클라이언트가 `limit=100`을 보내도 서버에서 `Math.clamp(limit, 0, 10)`으로 제한한다.
서버가 API 계약을 직접 보장해야 한다고 판단했다.

### 인기 테마 집계 기준 — `today - 1`까지

오늘의 예약은 진행 중일 수 있으므로 `to = today - 1`로 어제까지만 집계한다.
