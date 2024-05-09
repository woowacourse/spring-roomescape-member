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
- [ ] 사용자 도메인을 추가한다.
  - [ ] 사용자를 추가한다.
    - [ ] 이미 등록된 email일 경우 Duplication 예외를 발생시킨다.
      - [x] 특정 email이 등록되어 있는지 확인한다.
    - [ ] 등록에 성공할 경우, 토큰 값을 생성하여 응답 헤더에 포함시킨다.
- [ ] 로그인 기능을 구현한다.
  - [ ] POST 요청시 사용자를 등록한다.
- [ ] 로그인 후 Cookie를 이용하여 사용자의 정보를 조회하는 API를 구현한다.
  - [ ] 사용자 정보에 대해 GET 요청시 cookie에 담긴 token을 확인한다.
  - [ ] 로그인 성공 시, 응답 Body에 사용자 이름을 반환한다.
  - [ ] 로그인 실패 시, 인증 예외를 발생시킨다.
