### 기능 요구사항

#### 관리자 기능

- [x] localhost:8080/admin 요청 시 어드민 메인 페이지가 응답한다
    - [x] templates/admin/index.html을 사용한다

- [x] localhost:8080/admin/reservation 요청 시 예약 관리 페이지가 응답한다
    - [x] templates/admin/reservation-new.html을 사용한다
        - h2 데이터 베이스를 활용하여 예약 목록에 접근할 수 있다
            - [x] 예약 목록을 조회할 수 있다
            - [x] 예약 건수를 생성할 수 있다
                - [x] 생성 시 시간 슬롯 목록에서 시간을 선택할 수 있다
            - [x] 예약 건수를 삭제할 수 있다

- [x] localhost:8080/admin/time 요청 시 시간 관리 페이지가 응답한다
    - [x] templates/admin/time.html을 사용한다
    - h2 데이터 베이스를 활용하여 시간 슬롯 목록에 접근할 수 있다
        - [x] 시간 목록을 조회할 수 있다
        - [x] 시간 슬롯을 생성할 수 있다
        - [x] 시간 슬롯을 삭제할 수 있다

- [x] localhost:8080/admin/theme 요청 시 테마 관리 페이지가 응답한다
    - [x] templates/admin/theme.html을 사용한다
    - h2 데이터 베이스를 활용하여 테마 목록에 접근할 수 있다
        - [x] 테마 목록을 조회할 수 있다
        - [x] 테마 슬롯을 생성할 수 있다
        - [x] 테마 슬롯을 삭제할 수 있다

- [x] localhost:8080/admin/**는 관리자만 접근할 수 있다.

#### 사용자 기능

- [x] localhost:8080/reservation 요청 시 사용자 예약 페이지가 응답한다
    - [x] templates/reservation.html을 사용한다
        - [x] 날짜와 테마를 선택하면 예약 가능한 시간을 확인할 수 있다

- [x] localhost:8080/ 요청 시 인기 테마 페이지가 응답한다
    - [x] templates/index.html을 사용한다
        - [x] 일주일 기준으로 예약이 가장 많이 된 10개의 테마를 확인할 수 있다

- [x] localhost:8080/login 요청 시 사용자 로그인 페이지가 응답한다
    - [x] templates/login.html을 사용한다
    - [x] 로그인에 성공하면 jwt 토큰이 발급된다

- [x] localhost:8080/signup 요청 시 사용자 로그인 페이지가 응답한다
- [x] templates/signup.html을 사용한다
    - h2 데이터 베이스를 활용하여 회원 정보에 접근할 수 있다
        - [x] 회원을 등록할 수 있다.

### 데이터베이스 접근 방식

- http://localhost:8080/h2-console 에서 확인 가능
    - jdbc url : dbc:h2:mem:database
        - select * from reservation_time
        - select * from reservation

### 예외 처리 기능

#### 예약 등록

- [x] 부적절한 이름 등록할 경우
    - [x] 이름이 공백, null, 빈 값
- [x] 부적절한 예약 날짜를 등록할 경우
    - [x] 날짜가 공백, null, 빈 값
    - [x] 현재 날짜와 비교
- [x] 부적절한 예약 시간을 등록할 경우
    - [x] 존재하지 않는 timeId로 예약을 등록할 때
    - [x] 현재 시간과 비교
- [x] 부적절한 테마로 등록할 경우
    - [x] 존재하지 않는 themeId 예약을 등록할 때
    - [x] 공백 or null
- [x] 중복 예약을 등록할 경우
    - [x] time, themeId

#### 예약 시간 등록

- [x] 이미 등록된 시간대가 있을 경우
- [x] 빈 값일 경우(null 포함)
- [x] 시간 포맷이 일치하지 않는 경우 (시:분)

#### 예약 시간 삭제

- [x] 해당 시간에 예약이 등록되어 있는 경우

#### 테마 등록

- [x] 빈 값일 경우(null 포함)

#### 테마 삭제

- [x] 해당 테마에 예약이 등록되어 있는 경우

#### 회원 가입

- [x] 이미 가입한 회원일 경우