# 🛠️ 기능 목록

## 1단계 - 서비스 정책 적용

> 다음 정책을 만족하지 않는 요청은 거부한다.

- [x] 지나간 날짜·시간에 대한 예약 생성은 불가능하다. `ReservationServiceTest`
- [x] 같은 날짜+시간+테마에 이미 예약이 있으면 중복 예약을 거부한다. `ReservationServiceTest`
- [x] 예약이 존재하는 시간을 삭제할 수 없다. `JdbcTimeSlotRepositoryTest`
- [x] 유효하지 않은 입력값(빈 이름, 잘못된 날짜 형식 등)을 거부한다. `ReservationControllerTest`

---

- [ ] 거부되어야 하는 요청
    - [x] 존재하지 않는 예약에 대한 수정/삭제 `NoSuchElementException`
    - [ ] 다른 사용자의 예약에 대한 수정/삭제 `AccessDeniedException `
    - [ ] 이미 지난 예약에 대한 수정/삭제 `DuplicateKeyException`
    - [x] 이미 존재하는 예약(동일한 날짜, 테마, 시간)으로의 수정 `DuplicateKeyException`
    - [x] 이미 지난 시간으로의 수정 `IllegalArgumentException`
- [x] 잘못된 요청
    - [x] 존재하지 않는 예약 조회 `NoSuchElementException` / 404
    - [x] 필수 값이 빠진 예약 생성 `IllegalArgumentException`
    - [x] 같은 시간·테마에 중복 예약 시도 `DuplicateKeyException`
    - [x] 잘못된 값 `IllegalArgumentException`
        - [x] 필요한 값 존재하지 않음
        - [x] 값이 형식과 일치하지 않음
        - [x] 값이 정해진 범위를 초과함(이미 지난 날짜로 예약 시도)
- [ ] 
