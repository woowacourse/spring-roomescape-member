## 1단계 기능 요구 사항

- 예외 상황 처리 (엄청 많이)
  - 예약
    - 예약자명, 날짜, 시간에 유효하지 않은 값이 입력 되었을 때
      - [x] 날짜가 형식에 맞지 않을 때
      - [x] 날짜/시간id/예약자명 중 하나라도 비어있을 때
      - [x] 이름이 "" 일 때
      - [x] 이름이 10자 초과일 때
    - [x] 지나간 날짜와 시간에 대한 예약 생성은 불가
    - [x] 해당 시간id가 시간 테이블에 없을 때
    - [x] 중복 예약(시간, 날짜와 테마가 동일할 때)은 불가
  - 시간
    - 시작 시간에 유효하지 않은 값이 입력되었을 때
      - [x] 시간이 형식에 맞지 않을 때
      - [x] 시간이 비워있을 때
    - [x] 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때
    - [x] 중복된 시간은 불가

- [x] 위의 예외가 발생할 경우, 400 Bad Request를 응답한다.
  - [x] body에는 예외 메시지가 들어간다.
    ```text
    {
      errorMessage : "날짜(20_20_20)가 yyyy-MM-dd에 맞지 않습니다."
    }
    ```

- [x] 어드민의 시간 관리 페이지,
  - 방탈출 예약 페이지에서 모든 기능이 정상적으로 동작하는지 확인

## 2단계 기능 요구 사항

- [x] /admin/theme 요청 시 테마 관리 페이지를 응답
- [x] 어드민에서 방탈출 예약 페이지 변경

- '테마' 도메인 추가 (id, name, description, thumbnail)
  - [x] 유효성 검사
    - [x] 이름이 1자 미만 30자 초과인 경우
    - [x] 설명이 1자 미만 255자 초과인 경우
    - [x] 썸네일(이미지 주소)가 1자 미만 500자 초과인 경우
- [x] 테마를 조회할 수 있다.
- [x] 테마를 추가할 수 있다.
  - [x] 중복된 이름의 테마 불가
- [x] 테마를 삭제할 수 있다.
  - [x] 이미 예약이 존재하는 경우, 테마 삭제 불가

- 예약 도메인 변경
  - [x] 테마 객체 추가

## 3단계 기능 요구 사항

- [x] /reservation 요청 시 사용자 예약 페이지 응답
- [x] / 요청 시 인기 테마 페이지 응답

- 사용자 방탈출 예약 [/times?date=2024-08-05&themeId=1]
- 타임을 다 꺼내와 / 예약에 해당 날짜이고 해당 테마이고 해당 시간인 거는 alreadyBooked true
- join -> time left oupter join reservation
  - [x] 날짜와 테마가 주어지면, 해당 날짜에 예약 가능한 시간을 조회할 수 있다. (startAt, timeId, alreadyBooked)
    - [x] 가능한 시간인 경우, alreadyBooked을 true
    - [x] 불가능한 시간인 경우, alreadyBooked을 false
  - [x] 사용자는 원하는 날짜, 테마, 시간, 예약자명으로 예약할 수 있다.

- 인기 테마 조회
  - [x] 현재 날짜 이전 일주일을 기준으로, 해당 기간 내에 방문하는 예약이 많은 테마 10개를 조회할 수 있다.
    - ex. 오늘이 4/8 -> 4/1 ~ 4/7
    - 예약이 없는 경우는 인기테마에 포함하지 않는다.

- [x] `ranking.js` render 함수에서 사용할 data에는 (name, thumbnail, description)이 있어야 함
- [x] `user-reservation.js` renderTheme 함수, fetchAvailableTimes 함수 등 수정

## 4단계 기능 요구 사항
- [x] 로그인 폼이 있는 페이지를 응답 [GET /login] (templates/login.html 파일을 이용)
- [x] 사용자 도메인을 추가
  - [x] name, email, password로 구성
- [x] 로그인 기능 구현 [POST /login]
  - [x] 요청 시 로그인 폼에 입력한 email, password 값을 body에 포함
  - [x] 응답 Cookie에 "token"값으로 토큰이 포함
- [x] 로그인 후 Cookie를 이용하여 사용자의 정보를 조회하는 API를 구현 [GET /login/check]
  - [x] 요청 시 쿠키 값을 요청
  - [x] 응답 시 이름을 반환
  - [ ] 응답 형식, 해석 등이 되지 않는 경우 예외 처리를 한다

## 5단계 기능 요구 사항
- 사용자의 정보를 조회하는 로직 리팩터링
  - [x] `HandlerMethodArgumentResolver`을 활용하여 회원정보를 객체를 컨트롤러 메서드에 주입
- 유저 전체 조회 기능
  - [x] 유저의 id, name을 조회하는 기능
- 로그인 기능 변경
  - [x] 비밀번호와 상관없이 이메일이 저장되어 있다면, 로그인이 되도록 수정
- 예약 테이블 변경
  - [x] 예약 테이블에서 유저 id를 사용하도록 변경
- 예약 생성 기능 변경 - 사용자 [POST /reservations]
  - [x] 사용자가 예약 생성 시, 로그인한 사용자 정보를 활용
  - [x] `reservation.html`, `user-reservation.js` 변경된 명세에 맞게 클라이언트가 동작하도록 변경
- 예약 생성 기능 변경 - 관리자 [POST /admin/reservations]
  - [x] 관리자가 예약 생성 시, 유저를 조회하여 선택 후 예약을 생성
  - [x] `admin/reservation-new.html` 파일에서 로딩하는 js 파일을 변경 
    - `/js/reservation-new.js` -> `/js/reservation-with-member.js`

## 6단계 기능 요구 사항
- 멤버 권한 추가
  - [x] 멤버 테이블에 역할 추가
- 어드민 페이지 진입은 admin 권한이 있는 사람만 할 수 있도록 제한
  - [ ] Member의 Role이 ADMIN 인 사람만 /admin 으로 시작하는 페이지에 접근
  - [ ] HandlerInterceptor를 활용하여 권한을 확인하고, 권한이 없는 경우 요청에 대한 거부 응답
- 관리자가 조건에 따라 예약을 검색하는 기능
  - [ ] 예약자별, 테마별, 날짜별 검색 조건을 사용해 예약 검색이 가능하도록 기능을 추가
    - themeId, memberId, dateFrom, dateTo 값을 사용 
