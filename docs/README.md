## 기능 목록 작성
### 관리자 기능
- 예약을 조회한다. 
  > GET /admin/reservations
- 예약을 생성한다. 
  > POST /admin/reservations
- 예약을 삭제한다.
  > DELETE /admin/reservations
- 예약 시간을 조회한다.
    > GET /admin/times
- 예약 가능 시간을 생성한다.
    > POST /admin/times
- 예약 시간을 삭제한다.
    > DELETE /admin/times
- 테마를 조회한다.
   > GET /admin/themes
- 테마를 추가한다.
   > POST /admin/themes
- 테마를 삭제한다.
  > DELETE /admin/themes

### 사용자 기능
- 자기 이름을 예약을 조회한다.
  > GET /reservations/{username}
- 예약을 생성한다.
    > POST /reservations/{username}
- 테마를 조회한다.
   > GET /themes
- 예약 가능 시간을 조회한다.
   > GET /times&theme={theme}
