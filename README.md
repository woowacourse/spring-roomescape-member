## 기능 요구사항 체크리스트

### 1단계 - 테마 도메인 추가

- [x] 테마 생성 (관리자)
- [x] 테마 삭제 (관리자)

#### API 명세

`GET /theme` 사용자 테마 리스트 조회
`GET /admin/theme` 관리자 테마 리스트 조회

`POST /admin/themes` 관리자 테마 생성

`DELETE /admin/themes/{id}` 관리자 테마 삭제

### 2단계 - 사용자 예약

- [x] 날짜와 테마로 필터링하여 예약 가능한 시간 목록 조회
- [x] 같은 테마에 날짜와 시간이 같은 예약이 존재하는 경우 예약이 불가능하도록 처리

#### API 명세

`GET /themes/{id}/times?date={date}` 사용자 예약 가능한 시간 목록 조회
`GET /admin/themes/{id}/times?date={date}` 관리자 예약 가능한 시간 목록 조회

### 3단계 - 인기 테마 조회

- [x] 최근 1주 동안 예약이 많았던 테마 목록 내림차순 조회
    - 지정한 최대 갯수만큼 인기 테마를 조회할 수 있다.

#### API 명세

`GET /themes/popular?limit={limit}` 사용자 최근 일주일 인기 테마 조회
`GET /admin/themes/popular?limit={limit}` 관리자 최근 일주일 인기 테마 조회
