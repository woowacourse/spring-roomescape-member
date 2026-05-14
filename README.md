## 사이클2 - 미션 (예약 변경/취소와 에러 처리) 기능 목록

### 1단계 - 서비스 정책 적용

- [x] 지나간 날짜·시간 예약 차단
- [x] 같은 날짜·시간·테마 중복 예약 차단 (Service 검증 + DB UNIQUE 2중)
- [x] 예약 존재 시간 삭제 차단
- [x] 유효하지 않은 입력 거부 (빈 이름, 잘못된 날짜 형식 등)
- [x] 가능 시간 조회 시 과거 시간 제외

### 2단계 - 에러 응답 설계

- [x] 500 사용자 노출 방지
- [x] 응답 본문 구조화 (RFC 7807 ProblemDetail)
- [x] GlobalExceptionHandler 일관성 정리
- [x] 브라우저에서 의미 있는 메시지 표시

### 3단계 - 내 예약 조회/변경/취소

- [x] 본인 예약 목록 조회 (GET /api/reservations?user_name={name})
- [x] 본인 예약 변경 (PATCH /api/reservations/{id}, 본인 검증 포함)
- [x] 본인 예약 취소 (DELETE /api/reservations/{id}, soft delete)
- [x] 변경/취소 시 거부 케이스 처리 (이미 취소된 예약, 지난 예약, 변경 시간 점유 등)
- [x] 취소 후 같은 슬롯 재예약 허용 (soft delete + active_key partial unique)

### 4단계 - 프론트 화면

- [x] 본인 예약 목록 화면
- [x] 예약 변경/취소 화면
- [x] 에러 발생 시 사용자에게 의미 있는 메시지 표시
