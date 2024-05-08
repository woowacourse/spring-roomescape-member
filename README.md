## 기능 요구 사항

### 1단계

- [x] 0단계에서 이전 미션의 코드를 옮겨와도 어드민 페이지에서 기능이 정상적으로 동작하지 않습니다.
  - [x] 이를 보완하기 위해 시간 관리, 예약 관리 API가 적절한 응답을 하도록 변경합니다.
- [ ] 발생할 수 있는 예외 상황에 대한 처리를 하여, 사용자에게 적절한 응답을 합니다.
  - [x] 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때
    - "", null, "   "
  - [x] 예약 생성 시 예약자명, 날짜, 시간에 유효하지 않은 값이 입력 되었을 때
  - [x] 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때
  - [x] 중복된 예약 시간 생성하려고 할 때
  - [x] 등등
- [x] 아래와 같은 서비스 정책을 반영합니다.
  - [x] 지나간 날짜에 대한 예약 생성은 불가능하다.
  - [x] 지나간 시간에 대한 예약 생성은 불가능하다.
  - [x] 중복 예약은 불가능하다.
    - [x] ex. 이미 4월 1일 10시에 예약이 되어있다면, 4월 1일 10시에 대한 예약을 생성할 수 없다.
- [x] 어드민의 시간 관리 페이지, 방탈출 예약 페이지에서 모든 기능이 정상적으로 동작하는지 확인합니다.

### 2단계

- [x] 방탈출 게임은 '테마'라는 정보를 포함합니다. 사용자 예약 시 원하는 테마를 선택할 수 있도록 테마 도메인을 추가합니다.
- [x] 관리자가 테마를 관리할 수 있도록 기능을 추가합니다.
- [x] 모든 테마는 시작 시간과 소요 시간이 동일하다고 가정합니다.
- [x] 관리자가 방탈출 예약 시, 테마 정보를 포함할 수 있도록 기능을 변경합니다.
- [ ] 발생할 수 있는 예외 상황에 대한 처리를 하여, 사용자에게 적절한 응답을 합니다.
  - [x] 테마 추가 시 테마 이름, 설명, 썸네일에 빈 값이 입력되었을 때
    - "", null, "   "
  - [x] 중복된 테마 생성하려고 할 때
  - [x] 특정 테마에 대한 예약이 존재하는데, 그 테마를 삭제하려 할 때

### 3단계

- [x] TODO: [3단계] 주석을 검색하여 안내사항에 맞게 클라이언트 코드를 수정하세요.

#### 사용자 예약

- (관리자가 아닌) 사용자가 예약 가능한 시간을 조회하고, 예약할 수 있도록 기능을 추가/변경 합니다.
- [x] 사용자는 날짜와 테마를 선택하면 예약 가능한 시간을 확인할 수 있습니다.
- [x] 사용자는 예약 가능한 시간을 확인하고, 원하는 시간에 예약을 할 수 있습니다.
- [ ] 예약 시 사용자 구분은 어드민과 동일하게 사용자의 이름으로 합니다.
- [x] /reservation 요청 시 사용자 예약 페이지를 응답합니다.
- [x] 페이지는 templates/reservation.html 파일을 이용하세요.

#### 인기 테마

- 인기 테마 조회 기능을 추가합니다.
- [x] 최근 일주일을 기준으로 하여 해당 기간 내에 방문하는 예약이 많은 테마 10개를 확인하려 합니다.
- [x] 예를 들어 오늘이 4월 8일인 경우, 게임 날짜가 4월 1일부터 4월 7일까지인 예약 건수가 많은 순서대로 10개의 테마를 조회할 수 있어야 합니다.
- [x] / 요청 시 인기 테마 페이지를 응답합니다.
  - [x] 페이지는 templates/index.html 파일을 이용하세요.

## API
### 예약 가능 시간 조회 API
**request**
GET /times/memeber?date=2024-05-02&themeID=1 HTTP/1.1

**response**
HTTP/1.1 200
Content-Type: application/json

[
{
"id": 1,
"startAt": "19:25:00",
"alreadyBooked": false
}
]

### 인기 테마 목록 조회 API
**request**
GET /themes/ranking HTTP/1.1

**response**
HTTP/1.1 200
Content-Type: application/json

[
{
"id": 1,
"name": "교실방탈출",
"description": "교실테마입니다.",
"thumbnail": "class.jpg"
}
]

## 4단계
- [x] 사용자 도메인을 추가합니다.
- [x] 로그인 기능을 구현하세요.
- [x] 로그인 후 Cookie를 이용하여 사용자의 정보를 조회하는 API를 구현하세요.
- [ ] 로그인 기능 구현 완료 후 클라이언트 코드에 있는 로그인 관련 로직을 활성화해야 합니다. 
  - [ ] TODO: [4단계] 주석을 검색하여 안내사항에 맞게 클라이언트 코드를 수정하세요.

### 사용자
- [x] 사용자는 아래의 정보를 가집니다.
  - name: 사용자 이름
  - email: 이메일
  - password: 비밀번호
- [x] email을 로그인의 id로, password를 비밀번호로 사용합니다.

### 로그인
- [x] GET /login 요청 시 로그인 폼이 있는 페이지를 응답합니다.
  - templates/login.html 파일을 이용하세요.
- [x] POST /login 요청 시 로그인 폼에 입력한 email, password 값을 body에 포함하세요.
  - [x] email과 password를 이용해서 멤버를 조회하고
  - [x] 조회한 멤버로 토큰을 만듭니다.
  - [x] 그리고 Cookie를 만들어 응답합니다.
  - [x] 응답 Cookie에 "token"값으로 토큰이 포함되도록 하세요.
- [x] 인증 정보 조회(GET /login/check)하는 API를 만드세요.
  - [x] Cookie에서 토큰 정보를 추출하여
  - [x] 멤버를 찾아 멤버 정보를 응답합니다.
- [ ] 로그인 기능과 인증 정보 조회 기능이 정상적으로 구현되면 로그인 후 우측 상단의 Login 버튼이 사용자 이름으로 변합니다.
- [ ] 로그아웃 시 다시 Login 버튼이 노출됩니다.

### 인증 정보 조회
-[ ] 상단바 우측 로그인 상태를 표현해주기 위해 사용자의 정보를 조회하는 API를 구현하세요.
-[x] Cookie를 이용하여 로그인 사용자의 정보를 확인하세요.


## 리팩토링 할 것
- [ ] ReservationRequte에 @DateTimeFormat 적용
- [ ] 구현체에 대한 infrastructure 패키지 분리
- [ ] dao 테스트 제거
- [ ] 테스트 mock 적용
- [ ] 테스트 httpStatus 통일
- [ ] /times/member -> /times/available
- [ ] exception 핸들러 컨트롤러 어드바이스 이해하기
- [ ] 날짜 7일 상수화
- [ ] data.sql
- [ ] class IllegalReservationException extends IllegalArgumentException { // TODO: IllegalArgumentException가 최선일까?
- [ ] findIds sql문 in으로 수정
