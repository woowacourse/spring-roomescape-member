
# 1~3단계 요구 사항
- [x] `localhost:8080/admin` get 요청 시 어드민 메인 페이지가 응답할 수 있다.
- [x] `localhost:8080` get 요청 시 어드민 메인 페이지가 응답할 수 있다.
- [x] `/admin/reservation` get 요청 시 예약 관리 페이지가 응답할 수 있다.
- [x] 예약 관리 페이지 응답 시, 현재 예약 목록을 함께 보여준다.
- [x] `/admin/reservation` post 요청 시 예약을 추가한다.
- [x] `/reservations/{id}` delete 요청 시 예약을 삭제한다.
  - [x] id 값이 없는 경우 예외를 발생시킨다.

# 4단계 요구사항
- [x] JdbcTemplate을 이용하여 DataSource객체에 접근하기
- [x] DataSource 객체를 이용하여 Connection 확인하기
- [x] Connection 객체를 이용하여 데이터베이스 이름 검증
- [x] Connection 객체를 이용하여 테이블 이름 검증

# 5단계 요구사항
- [x] db의 reservation을 조회 할 수 있다.

# 6단계 요구사항
- [x] 예약 추가 api가 db와 연동된다.
- [x] 예약 취소 api를 통해 db의 예약 정보를 삭제가능하다.

# 7단계 요구사항
- [x] 시간 관리 페이지를 응답할 수 있다.
- [x] 예약 시간 추가를 할 수 있다.
- [x] 예약 시간 리스트 조회를 할 수 있다.
- [x] 예약 시간 삭제를 할 수 있다.

# 8단계 요구사항
- [x] 예약 페이지를 reservation.html로 변경
- [x] 방탈출 예약 시 정해진 시간만을 예약 가능합니다.
