# 1단계 - 예외 처리와 응답
## 예약
- [x] 지나간 날짜와 시간에 대한 예약 생성 시 예외가 발생한다.
- [x] 중복 예약 시 예외가 발생한다.
- [x] 예약 생성 시 예약자명이 비어있으면 예외가 발생한다.
- [x] 날짜가 유효하지 않은 값일 시 예외가 발생한다.
- [x] 등록되지 않은 시간에 대한 예약 생성 시 예외가 발생한다.
- [x] 요청 본문이 JSON 형식이 아닐 경우 예외가 발생한다.

## 시간
- [x] 중복된 시간 생성 시 예외가 발생한다.
- [x] 시간 삭제 시 참조된 예약이 있으면 예외가 발생한다.
- [x] 시간이 유효하지 않은 값일 시 예외가 발생한다.
- [x] 요청 본문이 JSON 형식이 아닐 경우 예외가 발생한다.

# 2단계 - 테마 추가
## 테마
- [x] 관리자는 테마를 추가할 수 있다.
    - [ ] 중복된 테마 이름으로 추가 시 예외가 발생한다.
    - [ ] 테마 추가 시, 테마 이름, 설명, 썸네일 중 하나라도 비어 있으면 예외가 발생한다.
    - [ ] 썸네일 문자열이 `http` 로 시작하지 않는 경우 예외가 발생한다.
- [ ] 관리자는 테마를 조회할 수 있다.
- [ ] 관리자는 테마를 삭제할 수 있다.
  - [ ] 테마 삭제 시 참조된 예약이 있으면 예외가 발생한다.

## 예약
- [ ] 방탈출 예약 시 테마 정보를 포함하여 예약을 추가한다.
  - [ ] 등록되지 않은 테마에 대한 예약 생성 시 예외가 발생한다.
  - [ ] 동시간대에 이미 예약된 테마를 예약하는 경우 예외가 발생한다.

## 화면
- [x] `/admin/theme` 요청 시 `templates/admin/theme.html`가 응답한다.
- [x] `/admin/reservation` 요청 시 `templates/admin/reservation-new.html`가 응답한다.
