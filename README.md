# 방탈출 사용자 예약

## 1단계

- 예외 처리 후 적절한 응답 반환 (404, 403.. 등)
- 예외 처리
    - 예약
        - 시간: not null
        - 날짜: not null
        - 예약자명: not null, not blank
        - 시간 + 날짜
            - 예약 일시가 현재 일시 이후여야 한다.
            - 같은 일시에 예약이 있는 경우
    - 시간
        - not null, 25시, 61분 같은 값
        - 이미 있는 시간인 경우 생성 불가
        - 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때

## 2단계

- 테마 entity가 추가된다.
    - 테마 table추가 및 reservation테이블 수정(제공되는 sql이용)
    - 기존 reservation entity 코드에 theme 추가
- `/admin/theme` -> `theme.html`반환
- reservation페이지 `reservation-new.html`로 변경
- `/reservations`api 필드에 theme 관련 필드 추가
- 테마 조회, 추가, 삭제 구현
- 클라이언트 코드를 수정해야 한다.
    - `/reservations`의 명세가 바뀌게 될텐데 테마와 관련된 필드와 프론트의 맵핑을 해야함

## 3단계

- 예약 조회
    - `/reservation`요청하면 `reservation.html`반환
    - 날짜와 테마에 따른 예약시간 조회
- 예약 추가
    - 예약 추가 api적절히 수정한다.
- 인기테마
    - 최근 일주일 기준 예약이 많은 테마 10개
    - `/`url요청시 `templates/index.html`반환(인기페이지)

## 4단계

- 사용자 도메인을 추가합니다.
- 로그인 기능을 구현하세요.
- 로그인 후 Cookie를 이용하여 사용자의 정보를 조회하는 API를 구현하세요.

- 사용자는 아래의 정보를 가집니다.
    - name: 사용자 이름
    - email: 이메일
    - password: 비밀번호
- email을 로그인의 id로, password를 비밀번호로 사용

- 로그인
    - GET /login 요청 시 로그인 폼이 있는 페이지를 응답합니다.
        - templates/login.html 파일을 이용하세요.
    - POST /login 요청 시 로그인 폼에 입력한 email, password 값을 body에 포함하세요.
        - 응답 Cookie에 "token"값으로 토큰이 포함되도록 하세요.

## 5단계

- 사용자의 정보를 조회하는 로직을 리팩터링
    - Cookie에 담긴 인증 정보를 이용해서 멤버 객체를 만드는 로직을 분리
        - HandlerMethodArgumentResolver을 활용하여 회원정보 객체를 컨트롤러 메서드에 주입
- 예약 생성 API 및 기능을 리팩터링
    - 사용자가 예약 생성 시, 로그인한 사용자 정보를 활용하도록 리팩터링
    - reservation.html, user-reservation.js 파일의 TODO 주석을 참고하여 변경된 명세에 맞게 클라이언트가 동작하도록 변경
- 관리자가 예약 생성 시, 유저를 조회하여 선택 후 예약을 생성하도록 리팩터링 합니다.
    - admin/reservation-new.html 파일에서 안내된 4단계 관련 주석에 따라, 로딩하는 js 파일을 변경합니다.
      AS-IS: /js/reservation-new.js
      TO-BE: /js/reservation-with-member.js

## 6단계

- 어드민 페이지 진입은 admin 권한이 있는 사람만 할 수 있도록 제한
    - Member의 Role이 ADMIN 인 사람만 /admin 으로 시작하는 페이지에 접근 가능
    - HandlerInterceptor를 활용하여 권한을 확인하고, 권한이 없는 경우 요청에 대한 거부 응답 반환
- 관리자가 조건에 따라 예약을 검색할 수 있도록 기능을 추가
    - 예약자별, 테마별, 날짜별 검색 조건을 사용해 예약 검색이 가능하도록 기능을 추가
