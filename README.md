### 기능 구현 목록

관리자 API에 prefix 적용

- [x]  관리자 예약 API에 /admin prefix 추가
- [x]  관리자 시간 API에 /admin prefix 추가

Phase 1 - 테마 도메인 추가

- [x]  Theme 도메인 객체 추가
- [x]  theme 테이블 스키마 추가
- [x]  ThemeRepository 인터페이스와 JDBC 구현 추가
- [x]  ThemeService 추가
- [x]  관리자 테마 API 추가 (/admin/themes)

Phase 2 - Reservation에 Theme 연결

- [x]  reservation 테이블에 theme_id FK 추가
- [x]  Reservation 도메인에 theme 필드 추가
- [x]  예약 Repository에 theme 조회·저장 연결
- [x]  예약 생성 시 themeId 포함하도록 변경
- [x]  MissionStepTest 예약과_시간_연결을 theme 포함으로 갱신

Phase 3 - 사용자 API

- [x]  사용자 테마 조회 API 추가 (GET /user/themes)
- [x]  예약 가능 시간 조회 쿼리 추가 (NOT IN)
- [x]  예약 가능 시간 조회 API 추가 (GET /user/themes/{id}/available-times)
- [x]  사용자 예약 추가 API (POST /user/reservations)

Phase 4 - 인기 테마 조회

- [x]  인기 테마 조회 쿼리 추가 (JOIN + GROUP BY + COUNT)
- [x]  인기 테마 조회 API 추가 (GET /user/themes/popular)

Phase 5 - 테스트 & 초기 데이터

- [ ]  사용자 예약 정상 흐름 요구사항 테스트 추가
- [ ]  data.sql로 인기 테마 검증용 초기 데이터 추가
- [ ]  인기 테마 조회 요구사항 테스트 추가
