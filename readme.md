## 기능 구현 목록

### 어드민 페이지
- [x] "/admin" get 요청시, 어드민 페이지를 출력한다
- [x] "/admin/reservation" get 요청 시, 예약 관리 페이지를 출력한다.
  - 예약을 추가하거나 삭제할 수 있다.

### 예약 조회
- [x] "/reservations" get 요청 시 모든 예약 정보를 반환한다.

### 예약 추가
- [x] "/reservations" post 요청으로  name, date, time 을 body 로 전송하여 추가할 수 있다.

### 예약 취소
- [x] "/reservations/{id}" delete 요청 시 해당 id로 설정된 예약을 삭제되고 204 NO_CONTENT 상태 코드가 반환된다.
- [x] 만약 해당 예약이 삭제되었다면 404 NOT FOUND 상태 코드가 반환된다. 
