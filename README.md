# 요구사항 분석

## 1단계

- [x] 시간 관리 API가 적절한 응답을 하도록 변경 
- [x] 예약 관리 API가 적절한 응답을 하도록 변경
- [x] 발생할 수 있는 예외 상황에 대한 처리
  - [x] 사용자에게 적절한 응답 

### 예외 처리 예시
- [x] 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때
- [x] 예약 생성 시 예약자명, 날짜, 시간에 유효하지 않은 값이 입력 되었을 때
- [x] 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때

### 서비스 정책 반영
- [x] 지나간 날짜와 시간에 대한 예약 생성은 불가능하다.
- [x] 중복 예약은 불가능하다.

## 2단계
- [x] 사용자 예약 시 지정할 테마 도메인 추가 
  - 모든 테마는 시작 시간과 소요 시간이 동일하다고 가정 
- [x] 관리자 - 테마 관리 기능을 추가
  - [x] 테마 조회 기능 구현
  - [x] 테마 추가 기능 구현
  - [x] 테마 삭제 기능 구현
  - [x] `/admin/theme` 요청 시 `templates/admin/theme.html` 반환
- 관리자 - 저장된 예약의 테마 변경 기능 추가(예약 관리)
  - [x] 방탈출 예약 페이지를 `templates/admin/reservation-new.html`로 변경
  - [x] 예약 시 테마 기능 추가
- [x] ~~프론트 코드 변경 - 예약 목록 조회 API 호출 후 렌더링. response 명세에 맞춰 값 설정~~

## 3단계
- 사용자 - 예약 페이지
  - [x] 사용자가 예약 가능한 시간 조회
  - [x] 사용자가 예약 가능한 시간 추가/변경
  - [x] `/reservation` 요청 시 `templates/reservation.html` 반환
- 인기 테마
  - [x] `/` 요청 시 `templates/index.html` 반환
  - [x] 인기 테마 조회 기능을 추가 
    - 최근 일주일을 기준으로 예약이 많은 테마 10개를 확인
- [x] [3단계] 주석을 검색하여 안내사항에 맞게 클라이언트 코드를 수정
