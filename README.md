# 기능 요구 사항 목록

## 1단계
- [x] 0단계에서 이전 미션의 코드를 옮겨온다.
  - [x] API가 적절한 응답을 함으로써 정상작동하도록 수정한다.
- [x] 발생할 수 있는 예외 상황에 대한 처리와 함께 사용자에게 적절한 응답을 한다.
  - [x] 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때
    - [x] 빈 값이나 null은 들어올 수 없다.
    - [x] 시간은 중복 등록이 불가능하다.
  - [x] 예약 생성 시 유효하지 않은 값이 입력 되었을 때
    - [x] 예약자명은 빈 값이나 null이 들어올 수 없다.
    - [x] 날짜가 잘못된 경우 예외를 발생시킨다.
      - [x] 빈 값이나 null인 경우
      - [x] 지나간 날짜인 경우
      - [x] 날짜가 오늘 이면서 시간이 지나간 경우
  - [x] 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때
  - [x] 중복 예약은 불가능하다.

## 2단계
- [x] 테마 도메인을 추가합니다.
- [x] 사용자 예약 시 원하는 테마를 선택할 수 있다.
- [x] 모든 테마는 시작 시간과 소요 시간이 동일하다고 가정합니다.
  - [x] 테마에 대한 중복 체크를 할때, 이름이 같은지 확인해라. 
  - [x] 예약에 대한 중복 체크를 할때, 날짜, 시간, 테마 이름이 같은지 확인해라. 
- [x] 관리자가 테마를 관리할 수 있도록 기능을 추가합니다. 
  - [x] 테마 추가 기능을 구현한다.
  - [x] 테마 조회 기능을 구현한다.
  - [x] 테마 삭제 기능을 구현한다. 
- [x] 관리자가 방탈출 예약 시, 테마 정보를 포함할 수 있도록 기능을 변경합니다.

## 3단계
- [x] (관리자가 아닌) 사용자가 예약 기능을 추가한다.
  - [x] 예약 가능한 시간을 조회할 수 있다.
    - [x] 사용자는 날짜와 테마를 선택하면 예약 가능한 시간을 확인할 수 있다.
  - [x] 예약할 수 있다.
- [x] 인기 테마 조회 기능을 추가합니다.
  - [x] 최근 일주일을 기준으로 예약이 많은 테마 10개를 보여준다.

## 4단계
- [x] 사용자 도메인을 추가한다.
- [x] 로그인 기능을 구현한다.
  - [x] 이메일과 비밀번호로 로그인 한다.
    - [x] 이메일 또는 비밀번호가 틀린 경우 예외를 발생시킨다.
    - [x] 로그인에 성공하면 토큰을 발행한다.
    - [x] 발행한 토큰을 쿠키에 담아 응답한다.
  - [x] 로그인 후 Cookie를 이용하여 사용자의 정보를 조회하는 API를 구현한다.
    - [x] 사용자 정보에 대해 GET 요청시 cookie에 담긴 token을 확인한다.
    - [x] 로그인 성공 시, 응답 Body에 사용자 이름을 반환한다.
    - [x] 로그인 실패 시, 인증 예외를 발생시킨다.

## 5단계 (세부사항까지 구체적으로 포함되어 있는 점 양해 부탁드립니다🙇‍)
- [x] `HandlerMethodArgumentResolver`를 활용하여 Cookie 값으로 멤버 정보를 조회하는 로직을 분리한다.
  - [x] `LoginMember` 객체를 만든다.
- [x] 사용자가 예약 생성 시, 로그인한 사용자 정보를 활용한다. (아래 1번 API 참고)
  - [x] `Reservation` 객체에서 ReservatorName 필드를 제거하고 `LoginMember` 필드 추가 
  - [x] `reservation` 테이블에 name 컬럼을 제거하고 `memberId`를 컬럼으로 갖도록 수정한다.
  - [x] 변경된 명세에 맞게 클라이언트가 동작하도록 코드를 수정한다.
    - [x] `reservation.html`
    - [x] `user-reservation.js`
- [x] 관리자가 예약 생성 시, 유저를 조회하여 선택 후 예약을 생성하도록 리팩터링 한다. (아래 2번 API 참고)
  - [x] 관리자용 `AdminReservationRequest`를 만든다.
  - [x] admin/reservation-new.html 파일에서 안내된 4단계 관련 주석에 따라, 로딩하는 js 파일을 변경한다.
  - `js/reservation-new.js` -> `/js/reservation-with-member.js`
  
### 5단계에서 변경된 명세
1. 사용자 예약 생성 요청: 요청 body에 name이 제거 + 쿠키가 추가됨. 
  ```http request
  POST /reservations HTTP/1.1
  content-type: application/json
  cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI
  host: localhost:8080
  
  {
      "date": "2024-03-01",
      "themeId": 1,
      "timeId": 1
  }
  ```

2. 관리자 예약 생성 요청: 요청 body에 name이 제거되고 memberId가 추가됨 + 쿠키가 추가됨 
```http request
POST /admin/reservations HTTP/1.1
content-type: application/json
cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI
host: localhost:8080

{
    "date": "2024-03-01",
    "themeId": 1,
    "timeId": 1,
    "memberId": 1
}
```

## 6단계
- [x] Member에 Role을 추가한다.
- [x] `/admin`으로 시작하는 페이지 접근은 Role이 `ADMIN`인 Member만 가능하도록 제한한다.
  - [x] 인터셉터가 쿠키에 포함된 토큰을 바탕으로 사용자가 admin 권한이 있는지 판단한다.
- [ ] 관리자가 예약자별, 테마별, 날짜별 검색 조건을 이용해 예약을 검색할 수 있도록 기능을 추가한다.
  - 검색 조건을 선택하고 적용을 누르면, reservation-with-member.js의 applyFilter() 함수가 실행된다
  - [ ] 입력한 themeId, memberId, dateFrom, dateTo 값을 사용해 검색 기능을 완성한다.
