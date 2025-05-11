# 방탈출 예약

## 엔드포인트 정리



### 회원 관련

- [x] `POST /login`  
  로그인


- [x] `GET /signup`  
  회원가입


- [x] `GET /login/check`
  로그인 상태 확인


- [x] `POST /logout`  
  로그아웃


---

### 예약 관련

- [x] `GET /reservations`  
  모든 예약 목록 조회

 
- [x] `GET /reservations/times`  
  날짜와 테마에 따른 예약 가능 여부를 포함한 시간 목록 조회


- [x] `POST /reservations`  
  예약 생성


- [x] `DELETE /reservations/{id}`  
  예약 삭제
  - 예약에서 참조 중인 시간은 삭제 불가능


- [x] `GET /reservations/mine`  
    나의 예약 목록 조회


---

### 예약 시간 관련

- [x] `GET /times`  
  모든 예약 시간 목록 조회


- [x] `POST /times`  
  예약 시간 생성


- [x] `DELETE /times/{id}`  
  예약 시간 삭제

---


### 테마 관련

- [x] `GET /themes`  
  모든 테마 목록 조회


- [x] `POST /themes`  
  테마 생성


- [x] `DELETE /themes/{id}`  
  테마 삭제


- [x] `GET /themes/ranking`
  인기 테마 조회

  
---

### 사용자 페이지

- [x] `GET /login`  
  로그인 페이지


- [x] `GET /signup`  
  회원가입 페이지


---


### 관리자 페이지


- [x] `GET /admin`  
  어드민 메인 페이지


- [x] `GET /admin/reservation`  
  예약 관리용 페이지


- [x] `GET /admin/time`  
  예약 시간 관리 페이지  


- [x] `GET /admin/theme`  
  테마 관리 페이지  


---

### 회원 페이지

- [x] `GET /`  
  인기 테마 페이지


- [x] `GET /reservation`  
  회원 예약 페이지


- [x] `GET /reservation-mine`
  나의 예약 페이지
