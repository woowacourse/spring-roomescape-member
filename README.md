## 방탈출 사용자 예약

### 기능 목록

- [x] 시간 관리, 예약 관리 API 정상 동작
- [x] 예외 상황에 대한 처리
    - [X] 시작 시간이 유효하지 않은 값이 입력할 때 -> 시간 형식 (Time)  
        - [X] 널 값, 빈 값, HH:mm
    - [x] 특정 시간에 대한 예약이 존재할 때, 시간 삭제를 하려고 할 때 -> 외래키... (Time)
        - [x] ReservationTime의 id가 Reservation의 외래키(timeId)로 이미 있는 경우 
    - [X] 예약자명, 날짜, 시간이 유효하지 않은 값이 입력할 때 -> 예약자명 날짜 시간 형식 (Reservation)
        - [X] 예약자명: 널 값, 빈 값
        - [X] 날짜: 널 값, 빈 값, yyyy-mm-dd
        - [X] 시간 id: 널 값, 빈 값, 문자 X(Long)
    - [x] 지나간 날짜와 시간에 대한 예약 생성을 하려할 때 -> 도메인 로직 관련 (Reservation)
        - [x] 날짜: 오늘 이전 날짜 X
        - [x] 시간: 지금 이전 시간 X
    - [X] 중복 예약을 하려할 때 -> 서비스 로직 관련 (Reservation)
        - 이미 동일한 날짜와 시간에 대한 예약이 존재하는 경우
    - [X] 중복 시간을 추가하려 할 때 -> 서비스 로직 관련 (ReservationTime)
        - 이미 존재하는 시간대를 추가하려는 경우
    - [x] 존재하지 않는 id로 삭제하려 할 때 -> 서비스 로직 관련 삭제 (Reservation, ReservationTime)

---

## 테스트 목록

- [ ] 예외 상황에 대한 처리
    - [X] 시작 시간이 유효하지 않은 값이 입력할 때
        - [X] 널 값 (TimeRequest)
        - [x] 빈 값, HH:mm (TimeApiController)
      
    - [x] 특정 시간에 대한 예약이 존재할 때, 시간 삭제를 하려고 할 때
        - ReservationTime의 id가 이미 있는 경우 (TimeService)
      
    - [ ] 예약자명, 날짜, 시간이 유효하지 않은 값이 입력할 때
        - [X] 예약자명: 널 값, 빈 값 (ReservationRequest)
        - [ ] 날짜
            - [x] 널 값 (ReservationRequest)
            - [ ] 빈 값, yyyy-mm-dd (ReservationApiController)
        - [ ] 시간 id 
            - [x] 널 값 (ReservationRequest)
            - [ ] 빈 값, 문자 X(Long) (ReservationApiController)
          
    - [x] 지나간 날짜와 시간에 대한 예약 생성을 하려할 때 (Reservation)
        - [x] 날짜: 오늘 이전 날짜 X
        - [x] 시간: 지금 이전 시간 X
      
    - [ ] 중복 예약을 하려할 때 (Reservation)
        - 이미 동일한 날짜와 시간에 대한 예약이 존재하는 경우
      
    - [ ] 중복 시간을 추가하려 할 때 (ReservationTime)
        - 이미 존재하는 시간대를 추가하려는 경우
      
    - [ ] 존재하지 않는 id로 삭제하려 할 때 (Reservation, ReservationTime)

---
