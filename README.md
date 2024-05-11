# 방탈출 사용자 예약

## 기능 목록

- [x] 시간 관리, 예약 관리 API가 적절한 응답을 하도록 변경한다.
- [x] 발생할 수 있는 예외 상황에 대해 처리한다.
    - [x] 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때
    - [x] 예약 생성 시 예약자명, 날짜, 시간에 유효하지 않은 값이 입력 되었을 때
    - [x] 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때
    - [x] 중복된 예약 시간을 등록할 때
    - [x] 예약 날짜와 시간이 중복될 때

- [x] 사용자 예약 시 원하는 테마를 선택할 수 있도록 테마 도메인을 추가한다.
- [x] /admin/theme 요청 시 테마 관리 페이지를 응답한다.
    - templates/admin/theme.html
- [x] 테마 추가 API를 구현한다.
- [x] 테마 조회 API를 구현한다.
- [x] 테마 삭제 API를 구현한다.

- [x] 어드민에서 방탈출 예약 시, 테마 정보를 포함할 수 있도록 신규 페이지 파일을 사용한다.
    - AS-IS: templates/admin/reservation.html
    - TO-BE: templates/admin/reservation-new.html

- [x] /reservation 요청 시 사용자 예약 페이지를 응답합니다.
    - 페이지는 templates/reservation.html
- [x] 사용자가 예약 가능한 시간을 조회하고, 예약할 수 있도록 기능을 추가/변경 한다.
- [x] 사용자 예약 API를 구현한다.

- [x] 인기 테마 조회 기능을 추가한다.
    - 최근 일주일을 기준으로 하여 해당 기간 내에 방문하는 예약이 많은 테마 10개를 조회한다.
    - 예약 수가 같다면 테마 아이디를 기준으로 정렬하여 보여준다.
- [x] / 요청 시 인기 테마 페이지를 응답한다.
    - 페이지는 templates/index.html

- [x] `GET - /signup` 요청 시 회원가입 폼이 있는 페이지를 응답한다.
  - templates/signup.html
- [x] `GET - /login` 요청 시 로그인 폼이 있는 페이지를 응답한다.
  - templates/login.html
- [x] `POST - /login` 요청 시 로그인 폼에 입력한 email, password 값을 body에 포함한다.
  - [x] 응답 Cookie에 "token"값으로 토큰이 포함한다.
- [x] `GET - /login/check` 요청 시 인증 정보를 조회하는 API를 만든다.
  - [x] Cookie에서 토큰 정보를 추출하여 멤버를 찾아 멤버 정보를 응답한다.

- [ ] admin/reservation-new.html 파일에서 안내된 주석에 따라, 로딩하는 js 파일을 변경한다.
  - AS-IS: /js/reservation-new.js
  - TO-BE: /js/reservation-with-member.js
- [ ] 관리자가 예약 생성 시, 유저를 조회하여 선택 후 예약을 생성한다.
- [ ] 예약 생성은 로그인 사용자와 관리자 모두 사용할 수 있다.
  - [ ] 관리자는 memberId 인자로 전달한 정보로 예약을 생성한다.
  - [ ] 로그인 사용자는 자신의 로그인 정보로 예약을 생성한다.

