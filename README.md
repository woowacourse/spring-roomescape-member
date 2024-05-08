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
    - [x] 중복된 테마 이름으로 추가 시 예외가 발생한다.
    - [x] 테마 추가 시, 테마 이름, 설명, 썸네일 중 하나라도 비어 있으면 예외가 발생한다.
- [x] 관리자는 테마를 조회할 수 있다.
- [x] 관리자는 테마를 삭제할 수 있다.
  - [x] 테마 삭제 시 참조된 예약이 있으면 예외가 발생한다.

## 예약
- [x] 방탈출 예약 시 테마 정보를 포함하여 예약을 추가한다.
  - [x] 등록되지 않은 테마에 대한 예약 생성 시 예외가 발생한다.
  - [x] 동시간대에 이미 예약된 테마를 예약하는 경우 예외가 발생한다.

## 화면
- [x] `/admin/theme` 요청 시 `templates/admin/theme.html`가 응답한다.
- [x] `/admin/reservation` 요청 시 `templates/admin/reservation-new.html`가 응답한다.

# 3단계 - 사용자 기능
## 예약
- [x] `/reservation` 요청 시 `templates/reservation.html`가 응답한다.

## 테마
- [x] `/` 요청 시 `templates/index.html`가 응답한다.
- [x] 최근 일주일을 기준으로 예약이 많은 상위 10개 테마를 조회할 수 있다. 

## 시간
- [x] 선택된 테마에 따라 예약 가능 여부가 포함된 시간을 조회한다.

# 4단계 - 사용자 로그인
## 화면
- [x] `/login` 요청 시 `templates/login.html`가 응답한다.

## 사용자
- [x] 사용자는 `name`, `email`, `password`를 갖는다.
- [x] 사용자는 `email`과 `password`로 로그인을 한다.
- [x] `login/check`를 통해 사용자의 인증 정보를 조회할 수 있다.

# 5단계 -  로그인 리팩터링
## 로그인
- [x] Cookie 인증 정보를 활용해서 각 컨트롤러에 멤버 객체를 주입한다.

## 예약
- [x] 사용자는 로그인한 정보로 예약을 생성한다.
- [x] 관리자는 사용자를 선택해 예약을 생성한다.

# 6단계 - 관리자 기능
## 권한
- [x] 관리자만 `/admin/**`에 접속할 수 있게 권한을 설정한다.

## 예약
- [ ] 예약자별, 테마별, 날짜별 검색 조건을 사용해 예약을 검색한다.
