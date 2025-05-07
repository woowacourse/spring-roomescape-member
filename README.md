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

## 예약 생성 검증
- 중복된 예약 시간이 있다면 예외 발생
- 예약 시간이 유효하지 않다면 예외 발생
  - 입력 예약 시간 값이 유효하지 않을 경우
  - 이미 지나간 시간으로 예약 요청을 할 경우

## 예약 시간 생성 검증
- 유효하지 않는 값을 입력할 때 예외 발생
  - 입력 시간이 비어있을 경우
  - 시간 형식에 맞지 않는 값을 입력할 경우

## 예약 시간 삭제 검증
- 해당 예약 시간으로 예약된 예약이 있지만, 예약 시간을 삭제할 때 예외 발생

# 리팩토링
- 사용자, 관리자가 사용하는 API에 따라 Controller, Service 분리

---

### TODO (첫 번째 리뷰 이후)
- [X] 테스트 작성
  - [x] DAO Test
  - [x] Repository Test
  - [x] Service Test
  - [X] Domain Test
  - [X] 입력 검증 테스트(requestDTO)
- [X] 예외 처리 리팩토링
  - [X] 커스텀 예외 생성
    - [X] 예외 처리 기준 및 위치 명확하게 하기
  - [X] 응답 에러 메시지 생성
- [X] null 체크 기준 정하기
- [X] 외래키 제약 삭제 로직, 데이터베이스에 의존하지 않고 코드로 구현하기
- [X] jdbcTemplate -> named
