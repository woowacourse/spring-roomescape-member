
## 1단계 기능 요구 사항

- 예외 상황 처리 (엄청 많이)
  - 예약
    - 예약자명, 날짜, 시간에 유효하지 않은 값이 입력 되었을 때
        - [x] 날짜가 형식에 맞지 않을 때 
        - [x] 날짜/시간/예약자명/테마 중 하나라도 비어있을 때 (null)
        - [x] 예약자명이 "" 일 때
        - [x] 예약자명이 255자를 넘었을 때
    - [x] 지나간 날짜와 시간에 대한 예약 생성은 불가
    - [x] 해당 시간id가 시간 테이블에 없을 때
    - [x] 중복 예약(시간, 날짜와 테마가 동일할 때)은 불가
  - 시간
    - 시작 시간에 유효하지 않은 값이 입력되었을 때
      - [x] 시간이 형식에 맞지 않을 때
      - [x] 시간이 비워있을 때 (null)
    - [x] 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때
    - [x] 중복된 시간은 불가
  - 테마
    - 이름, 설명, 이미지에 유효하지 않은 값이 입력 되었을 때
      - [x] 이름/설명/이미지 중 하나라도 비어있을 때 (null)
      - [x] 이름/이미지 중 하나라도 ""일 때
      - [x] 이름/설명/이미지 중 하나라도 255자를 넘었을 때

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

- [x] GET /login 요청 시 로그인 폼이 있는 페이지 응답
- [x] GET  /signup 요청 시 회원가입 페이지 응답

- [x] POST /login 요청 시 로그인 폼에 입력한 email, password 값을 body에 포함
  - [x] 응답 쿠키에 "token"값으로 토큰이 포함
  - [x] member 테이블에 존재하는지 확인한다.
    - [x] 존재할 경우, 200 반환
    - [x] 존재하지 않을 경우, 203 반환

- [x] GET /login/check 사용자의 정보(이름)를 조회할 수 있다.
- [x] POST /logout 요청 시 쿠키에 로그인 정보를 제거한다.
- [x] POST /members 요청 시 member를 생성해준다. (name, email, password)

## 5단계 기능 요구 사항

- [x] Cookie에 담긴 인증 정보를 이용해서 멤버 객체를 만드는 로직을 분리
  - [x] HandlerMethodArgumentResolver을 활용해 회원정보를 객체를 컨트롤러 메서드에 주입

- [x] `reservation` 테이블 변경
  - 이름(name) -> member id

- [x] 사용자가 예약 생성 시, 로그인한 사용자 정보를 활용
 - [x] date, themeId, timeId를 입력
 - [x] name은 쿠키에 저장된 사용자를 식별해, 사용자 이름으로 대체하도록 수정
   - 사용자 식별은 이름이 아닌, id로 하도록 구현

- [x] 관리자 예약 생성 시, 유저를 조회하여 선택 후 예약을 생성
  - [x] date, themeId, timeId, memberId를 입력받아 예약을 생성한다.
  - [x] GET /members 요청 시 member 정보를 읽어서 반환한다.

- [x] `admin/reservation-new.html` 파일에서 안내된 4단계 관련 주석에 따라, 로딩하는 js 파일을 변경

## 6단계 기능 요구 사항

- [x] Member의 role이 ADMIN인 사람만 /admin 으로 시작하는 페이지에 접근할 수 있다.
  - [x] HandlerInterceptor를 이용
    - [x] Cookie에 role 추가
    - [x] 반환 값에 따라 처리되는 방식을 확인
- [x] 로그인이 되지 않은 경우, login, signUp 페이지에만 접근할 수 있다.

- 예약 검색 기능
  - [ ] 예약자별, 테마별, 날짜별 검색 조건을 사용해 예약 검색이 가능하도록 기능을 추가
  - [ ] `reservation-with-member.js`의 applyFilter() 함수를 수정
