# 요구사항 분석

## 1단계

- [x] 시간 관리 API가 적절한 응답을 하도록 변경 
- [x] 예약 관리 API가 적절한 응답을 하도록 변경
- [ ] 발생할 수 있는 예외 상황에 대한 처리
  - [ ] 사용자에게 적절한 응답 

### 예외 처리 예시
- [ ] 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때
- [ ] 예약 생성 시 예약자명, 날짜, 시간에 유효하지 않은 값이 입력 되었을 때
- [ ] 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때

### 서비스 정책 반영
- [ ] 지나간 날짜와 시간에 대한 예약 생성은 불가능하다.
- [ ] 중복 예약은 불가능하다.
