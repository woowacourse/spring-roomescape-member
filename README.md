## 1단계 - 예외 처리와 응답

### 요구사항 분석

- [x] 어드민 페이지 기능 복원
  - [x] 시간 관리 API 응답 수정
  - [x] 예약 관리 API 응답 수정

- [x] 예외 상황 처리
  - [x] 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때
  - [x] 예약 생성 시 예약자명, 날짜, 시간에 유효하지 않은 값이 입력 되었을 때
  - [x] 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때

- [x] 서비스 정책
  - [x] 지나간 날짜와 시간에 대한 예약 생성은 불가능
  - [x] 중복 예약은 불가능

## 2단계 - 테마 추가

### 요구사항 분석

- [x] 테마 도메인 추가
- [x] 테마 관리 기능 추가

## 3단계 - 사용자 기능

### 요구사항 분석

- [x] 사용자가 예약 가능한 시간을 조회 및 예약할 수 있도록 기능 추가
- [x] 인기 테마 조회 기능 추가


## 4단계 - 사용자 로그인
- [x] 로그인 기능을 구현한다.
  - [x] GET /login 요청 시 로그인 폼 페이지를 응답한다.
  - [x] POST /login 요청시, 폼에 입력한 email, password 값을 body에 포함한다.
  - [x] 로그인 시 응답 Cookie의 "token"에 토큰이 포함되도록 한다.
  
- [x] 사용자 도메인을 추가한다.
  - [x] 사용자는 name, email, password 정보를 가진다.
  - [x] email을 로그인의 id로 사용한다.

- [x] 로그인 후 Cookie를 이용하여 사용자의 정보를 조회하는 API를 구현한다.

## 5단계 - 로그인 리팩터링
- [x] Cookie에 담긴 인증 정보를 이용하여 멤버 객체를 만드는 로직을 분리한다.
- [ ] 로그인한 사용자 정보를 활용하여 예약하도록 리팩터링 한다.
- [ ] 관리자가 예약 생성 시, 유저를 조회하여 선택 후 예약을 생성하도록 리팩터링 한다.