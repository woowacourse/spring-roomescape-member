## 6단계 - 관리자 기능

### 요구사항 분석

- [x] 접근 권한 제어
  - [x] Member 스키마에 Role 구현
  - [x] Interceptor 를 이용하여 권한 확인
    - [x] Role 이 ADMIN 인 경우에만 `/admin` 도메인에 접근 가능
    - [x] 그 외의 경우 401로 거부 응답
- [x] 예약 목록 검색
  - [x] 예약자별, 테마별, 날짜별 검색 조건을 사용해 예약 검색 API 구현

### 커스텀 예외 추가

- NotRemovableByConstraintException - Foreign Key Constraint 로 삭제가 불가능할 때에 발생하는 예외
- NullRequestParameterException - Request 로 null 혹은 빈 값이 입력되었을 때에 발생하는 예외
- WrongStateException - 로직 수행 중 잘못된 상황에 발생하는 예외

## 5단게 - 로그인 리팩터링

### 요구사항 분석

- [x] 로그인 리팩터링
  - [x] Cookie 에 담긴 인증 정보를 이용해 멤버 객체를 만드는 로직 분리
- [x] 예약 생성 기능 리팩터링
  - [x] 사용자 - 로그인 정보를 활용하도록 API 수정
  - [x] 관리자 - 유저를 조회 및 선택하여 예약을 생성

## 4단계 - 사용자 로그인

### 요구사항 분석

- [x] 사용자 도메인 추가
  - [x] 이름, 이메일, 비밀번호를 가지는 도메인
  - [x] 로그인 시 이메일을 아이디로, 비밀번호를 비밀번호로 사용
- [x] 로그인 기능
  - [x] `GET /login` 요청 시 로그인 폼이 있는 페이지(`templates/login.html`) 응답
  - [x] `POST /login` 요청 시 로그인 폼에 입력한 이메일, 비밀번호를 body 에 포함
  - [x] 응답 Cookie 에 'token' 값으로 토큰이 포함
- [x] 로그인 이후 Cookie 를 이용한 사용자 정보 조회 API

---

## 1단계 - 예외 처리와 응답

### 요구사항 분석

- [x] 어드민 페이지 기능 복원
  - [x] 시간 관리 API 응답 수정
  - [x] 예약 관리 API 응답 수정

- [x] 예외 상황 처리
  이하 예외 상황 발생 시 400 Bad Request 로 응답하도록 구현
  - [x] 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때 NullPointException 발생
  - [x] 예약 생성 시 예약자명, 날짜에 유효하지 않은 값이 입력 되었을 때 IllegalArgumentException 발생
  - [x] 예약 생성 시 시간, 테마에 유효하지 않은 값이 입력 되었을 때 HttpMessageNotReadableException 발생
  - [x] 특정 시간 혹은 테마에 대한 예약이 존재하는데, 그 시간 혹은 테마를을 삭제하려 할 때 IllegalStateException 발생
  - [x] 이미 존재하는 테마 이름으로 테마를 추가할 때 IllegalArgumentException 발생
  - [x] 인기 테마 조회 시 특정 날짜('2020-01-01') 이전의 날짜가 입력될 경우 IllegalArgumentException 발생

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
