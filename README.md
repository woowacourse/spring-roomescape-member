# 방탈출 사용자 예약

## 1단계 - 예외 처리와 응답

- [x] 발생할 수 있는 예외 상황에 대한 처리를 하여, 사용자에게 적절한 응답을 한다.
    - [x] 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때
    - [x] 예약 생성 시 예약자명, 날짜, 시간에 유효하지 않은 값이 입력 되었을 때
    - [x] 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때
    - [x] 특정 시간이 존재하지 않는데, 그 시간을 삭제하려 할 때
    - [x] 특정 예약이 존재하지 않는데, 그 예약을 삭제하려 할 때
- [x] 아래와 같은 서비스 정책을 반영한다.
    - [x] 지나간 날짜와 시간에 대한 예약 생성은 불가능하다.
    - [x] 중복 예약은 불가능하다.
    - [x] 중복 예약 시간은 불가능하다.

## 2단계 - 테마 추가

- [x] `/admin/theme` 요청 시 테마 관리 페이지를 응답한다.
- [x] GET `/themes` 요청 시 테마를 전체 조회한다.
- [x] POST `/themes` 요청 시 테마를 추가한다.
    - [x] 테마 생성 시 테마 이름, 설명, 이미지에 유효하지 않은 값이 입력 되었을 때
    - [x] 테마 이미지의 확장자는 jpg, jpeg, png, heic만 가능하다.
- [x] DELETE `/themes/{id}` 요청 시 테마를 삭제한다.
    - [x] 특정 테마가 존재하지 않는데, 그 테마를 삭제하려 할 때 404를 응답한다.
    - [x] 특정 테마에 대한 예약이 존재하는데, 그 테마를 삭제하려 할 때 409를 응답한다.

## 3단계 - 사용자 기능

- [ ] `/reservation` 요청 시 사용자 예약 페이지를 응답한다.
  - [ ] 사용자는 예약 가능한 시간을 확인한다.
  - [ ] 사용자는 원하는 시간에 예약한다.
- [ ] `/` 요청 시 인기 테마 페이지를 응답한다.
  - [ ] 최근 일주일을 기준으로 하여 해당 기간 내에 방문하는 예약이 많은 테마 10개를 조회한다.
