# 기능 구현 목록

### 어드민 페이지

- [x] "/admin" get 요청시, 어드민 페이지를 출력한다
- [x] "/admin" 으로 시작되는 URL은 어드민 권한이 있는 유저에 한해서 접근할 수 있다.
- [x] "/admin/reservation" get 요청 시, 예약 관리 페이지를 출력한다.
    - 예약을 추가하거나 삭제할 수 있다.
- [x] "admin/reservation" post 요청 시 예약을 추가할 수 있다.
- [x] "/admin/time" get 요청 시, 예약 시간 관리 페이지를 출력한다.
    - 예약 시간을 추가하거나 삭제할 수 있다.

## 예약

### 전체 조회 (어드민용)

- [x] "admin/reservations" get 요청 시 모든 예약 정보를 반환한다.
- [x] "/reservations/available" get 요청으로 날짜(date) 와 테마(themeId)를 쿼리 스트링으로 요청하면 예약 가능한 시간을 가져올 수 있다.
    - 시간 정보 (timeResponse), 예약 여부 (alreadyBooked)를 반환한다.

### 조건 조회 (어드민용)

- [x] 예약자별, 테마별, 날짜별 검색 조건을 사용해 조건에 맞는 예약을 조회할 수 있다.
    - [x] "admin/reservations "에 예약자명(name), 날짜(date), 시간(timeId), 테마(themeId) 를 쿼리 스트링으로 요청하면 예약 정보를 가져올 수 있다.
        - [x] 예약자명, 날짜, 시간에 유효하지 않은 값을 입력할 경우 400 Bad Request 상태 코드가 반환된다.
            - 아래의 경우 예외가 발생된다.
                - 예약자명이 공백이거나, 25자 이하가 아닌 경우
                - 날짜 형식이 "yyyy-MM-dd' 아닌 경우
                - 시간 값이 숫자 값이 아닌 경우

### 추가

- [x] "/reservations" post 요청으로 예약자명(name), 날짜(date), 시간(timeId), 테마(themeId) 을 body 로 전송하여 추가할 수
  있다.
    - [x] 예약 정보를 가져올 수 있다.
    - [x] 예약자명, 날짜, 시간에 유효하지 않은 값을 입력할 경우 400 Bad Request 상태 코드가 반환된다.
        - 아래의 경우 예외가 발생된다.
            - 예약자명이 공백이거나, 25자 이하가 아닌 경우
            - 날짜 형식이 "yyyy-MM-dd' 아닌 경우
            - 시간 값이 숫자 값이 아닌 경우
    - [x] 현재시간을 기준으로 지나간 날짜와 시간에 대해 예약을 할 경우 400 Bad Request 상태 코드가 반환된다.
    - [x] 이미 존재하는 예약 시간에 예약을 할 경우 409 Conflict 상태 코드가 반환된다.

### 삭제

- [x] "/reservations/{id}" delete 요청 시 해당 id로 설정된 예약을 삭제되고 204 NO_CONTENT 상태 코드가 반환된다.
    - [x] 만약 해당 예약이 없다면 404 NOT FOUND 상태 코드가 반환된다.

## 예약 시간

### 조회

- [x] "/times" get 요청 시 모든 예약 시간 정보를 반환한다.
    - [x] 예약 시간 정보를 가져올 수 있다.

### 추가

- [x] "/times" post 요청 시 startAt 을 body로 전송하여 추가할 수 있다.
    - [x] startAt 은 "HH:mm" 형식이여야 한다.
    - [x] 유효하지 않은 시작 시간을 입력하였을 때 400 Bad Request 상태 코드가 반환된다.
        - startAt이 정해진 형식이 아닌 경우

### 삭제

- [x] "/reservations/{id}" delete 요청 시 해당 id로 설정된 예약 시간을 삭제하고 204 NO_CONTENT 상태 코드가 반환된다.
    - [x] 만약 해당 예약 시간이 없다면 404 NOT FOUND 상태 코드가 반환된다.
    - [x] 해당 예약 시간을 다른 곳에서 사용된다면 삭제 시 409 CONFLICT 상태 코드가 반환된다.

## 테마

### 조회

- [x] "/themes" get 요청 시 모든 테마 정보를 반환한다.
- [x] "/themes/popular" get 요청 시 오늘 날짜를 기준으로 8일 전 ~ 1일 전까지의 데이터에서 예약 건수가 많은 순서대로 10개의 테마를 알 수 있다.

### 추가

- [x] "/themes" post 요청 시 name, description, thumbnail 을 body로 전송하여 추가할 수 있다.

### 삭제

- [x] "/themes/{id}" delete 요청 시 해당 id로 설정된 테마를 삭제하고 204 NO_CONTENT 상태 코드가 반환된다.
    - [x] 만약 해당 테마가 없다면 404 NOT FOUND 상태 코드가 반환된다.

## 유저

- [x] "/members" get 요청 시 모든 유저 정보를 반환한다.
  - 어드민 권한을 제외하고 가져온다.

### 로그인

- [x] "/login" POST 요청 시 로그인을 할 수 있다.
    - [x] 이메일(email), 패스워드(password)를 필요로 한다.
    - [x] 성공 시 상태 코드 200과 함께 토큰을 쿠키에 Token 이라는 이름으로 저장한다.
    - [x] 로그인 정보가 올바르지 않는다면 401 Unauthorized 상태 코드를 반환한다.

### 인증 정보 조회

- [x] "/login/check" GET 요청 시 인증 정보를 조회할 수 있다.
    - [x] HTTP 헤더에 Cookie를 활용하여 토큰을 통해 정보를 조회한다.
    - [x] 토큰이 올바르지 않을 경우 401 Unauthorized 상태 코드를 반환한다.
    - [x] 성공 시 200 상태 코드와 함께 인증 정보를 반환한다.

### 생성

- [x] "/members" POST 요청 시 유저를 생성할 수 있다.
    - [x] email, password, name이 필요로 해야 한다. 각 값들은 공백일 수 없으며 최대 25자이다.
    - [x] email은 중복될 수 없다. 중복될 경우 409 CONFLICT 상태 코드가 반환된다.
    - [x] 성공 시 201 CREATED 상태코드가 반환된다.

