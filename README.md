# 추가된 API

[ADMIN]
- 테마 생성 API
- 테마 삭제 API

[USER]
- 테마 전체 조회 API (Admin과 공유)
- 테마 인기 테마 목록 조회 API
- 예약 가능 시간 조회 API

# 추가 기능

## 예외 처리 후 클라이언트에게 응답 메시지 반환
- IllegalArgumentException 예외 발생시 발생 상황에 맞는 예외메시지 반환
- 필수 정보 누락시 MissingServletRequestParameterException 캐치 후 고정 예외메시지 반환
- 입력 형식을 지키지 않을 시 HttpMessageNotReadableException 캐치 후 상황에 맞는 예외메시지 혹은 고정 예외메시지 반환

# 리팩토링
- 사용자, 관리자가 사용하는 API에 따라 Controller, Service 분리
