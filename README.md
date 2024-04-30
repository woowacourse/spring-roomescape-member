### 기능 요구사항

- [x] localhost:8080/admin 요청 시 어드민 메인 페이지가 응답한다
    - [x] templates/admin/index.html을 사용한다


- [x] localhost:8080/admin/reservation 요청 시 예약 관리 페이지가 응답한다
    - [x] templates/admin/reservation-legacy.html을 사용한다
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
- [x] 중복 예약을 등록할 경우

#### 예약 시간 등록

- [x] 이미 등록된 시간대가 있을 경우
- [x] 빈 값일 경우(null 포함)
- [x] 시간 포맷이 일치하지 않는 경우 (시:분)

#### 예약 시간 삭제

- [x] 해당 시간에 예약이 등록되어 있는 경우
