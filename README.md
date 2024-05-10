## 기능 요구 사항

### 1단계 기능 요구사항

- [x] 예약관리 API가 적절한 응답을 하도록 한다.
- [x] 발생 가능한 예외사항을 처리한다
    - [x] 시간 생성 시 : 시작 시간에 유효하지 않은 값이 입력되었을 때
    - [x] 예약 생성 시 : 예약자명, 날짜, 시간에 유효하지 않은 값이 입력 되었을 때
        - [x] 예약 날짜는 오늘 혹은 오늘 이후여야 한다.
        - [x] 예약 시간
            - [x] 예약 날짜가 오늘이라면 현재 시간 이후여야 한다.
            - [x] 예약 날짜가 오늘이 아니라면, 모든 시간에 예약이 가능하다.
    - [x] 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때

- [x] 새로운 서비스 정책을 반영한다
    - 예약 생성 시
        - [x] 지나간 날짜와 시간에 대한 예약 생성은 불가능하다.
        - [x] 중복 예약은 불가능하다.
        - ```
      ex. 이미 4월 1일 10시에 예약이 되어있다면, 4월 1일 10시에 대한 예약을 생성할 수 없다.
      ```

### 2단계 기능 요구사항

- [x] 관리자가 테마를 관리하는 기능
    - [x] 테마를 추가한다.
        - [x] 모든 테마는 시작 시간과 소요 시간이 동일하다고 가정한다.
    - [x] 테마를 조회한다.
    - [x] 테마를 삭제한다.
        - [x] 테마를 사용 중인 방탈출 예약이 있다면 테마를 삭제할 수 없다.
- [x] 관리자가 방탈출 예약 시, 테마 정보를 포함할 수 있도록 기능을 변경한다.

### 3단계 기능 요구사항

- [x] 사용자가 예약 시간을 조회하고 예약한다.
    - [x] 사용자가 날짜와 테마를 기준으로 예약 가능한 시간을 확인한다.
    - [x] 사용자가 원하는 시간에 예약한다.

- [x] 인기 테마를 조회할 수 있다
    - [x]  최근 일주일을 기준으로 방문 예약이 많은 상위 10개의 테마를 확인한다.

### 4단계 기능 요구사항

- [x] 사용자 도메인을 추가한다.
- [x] 로그인 기능을 구현한다.
    - [x] 이메일과 비밀번호를 전달 받고 토큰을 생성한다.
    - [x] 이메일과 비밀번호가 null이거나 빈 값이 아닌 지 검증한다.
    - [x] 이메일 형식이 올바른지 검증한다.
- [x] 로그인 후 Cookie를 이용하여 사용자의 정보를 조회한다.
- [x] 사용자가 정보를 입력하고 회원 가입한다.

### 5단계 기능 요구사항

- [x] 사용자 정보를 조회하는 로직을 ArgumentResolver를 사용하도록 수정
- [x] 사용자가 예약 생성 시 로그인한 사용자 정보를 사용하도록 수정
- [x] 관리자가 예약 생성 시 유저를 조회하여 선택하도록 수정
    - [x] /admin/reservation-new.html 파일이 reservation-with-member.js 파일을 import 하도록 수정
    - [x] 관리자가 예약을 생성하는 화면에서 유저의 이름을 선택할 수 있도록 구현

### 6단계 기능 요구사항

- [x] admin 권한을 가진 사람만 admin page에 접근하도록 수정
    - [x] Member에 Role을 저장하는 필드를 추가한다.
    - [x] Role이 ADMIN인 사람만 /admin으로 시작하는 페이지에 접근하도록 수정한다.
- [ ] 관리자가 조건에 따라 예약을 검색하도록 수정
    - [ ] 예약자별, 테마별, 날짜별 검색 조건에 해당하는 예약을 검색하도록 한다.
