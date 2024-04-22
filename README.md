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
