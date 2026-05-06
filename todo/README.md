# TODO

## 설계 구조

- [ ] 패키지 계층 이름을 presentation, application, domain, infra로 정리하기
  - Controller와 요청/응답 DTO는 presentation에 두기.
  - 현재 Service 역할을 하는 애플리케이션 흐름 조합 로직은 application에 두기.
  - 도메인 객체와 Repository 인터페이스는 domain에 두기.
  - JdbcRepository 같은 Repository 구현체는 infra에 두기.
  - 최상위 계층 기준으로 나누기보다 reservation, reservationtime, theme 같은 도메인 패키지 안에서 계층을 나누는 방향으로 고민하기.
- [x] 서비스 테스트에서 Mockito로 만든 목 객체 Repository를 Fake Repository로 리팩터링하기!
  - 지금처럼 목 객체로 하면 서비스의 상태 변화나 흐름보다 스텁으로 정의한 반환값에 의존하는 테스트가 되어버리는 느낌...
- [ ] Reservation, ReservationTime, Theme 도메인 패키지 분리 기준 다시 고민..
  - 지금처럼 각각 다른 패키지로 분리한다면 서로 다른 애그리거트로 본다는 의미인데...
  - 다른 애그리거트로 본다면 Reservation이 Theme, ReservationTime 객체를 직접 참조하기보다 id만 가지는 방향이 더 자연스러움
- [ ] 날짜와 테마로 시간 조회 시 가능한 시간, 불가능한 시간을 모두 내려주도록 응답 구조 정하기
  - 조회 전용 DTO 새로 만들기...
  - Repository에서는 API 응답 DTO가 아니라 ReservationTimeAvailability 같은 조회 전용 DTO를 반환하고, Service에서 Response DTO로 변환하기.

## 기능 요구사항에서 빼먹지 말아야 할 것

- [x] 예약 시간 추가 시 같은 시간이 중복으로 등록되지 않도록 검증 추가하기
  - 현재 시간 추가 시 startAt 중복 여부를 따로 체크하지 않음.
- [ ] 예약이 존재하는 시간 삭제하면 그 예약 어떻게 할지 정하기
  - 현재 reservation.time_id 외래키에 ON DELETE CASCADE가 없어서, 예약이 연결된 ReservationTime을 삭제해도 Reservation이 함께 삭제되지 않음..
  - 테마 추가하게 되면 해당 테마에서도 비슷한 설정 필요. 어떻게 해야할지 정하기!
- [x] 삭제 실패 시 응답을 404 방식으로 통일하기
  - 존재하지 않는 테마, 예약 시간, 예약을 삭제할 때의 응답 정책 맞추기.
- [x] 예외 타입을 커스텀 예외로 통일하기
  - IllegalArgumentException 대신 도메인/서비스 의도가 드러나는 커스텀 예외를 사용하도록 정리하기.
- [x] 조회 API 정렬 기준 추가하기
  - 전체 조회나 시간 조회 시 등록순, 시간순 등 어떤 기준으로 정렬할지 정하기.
- [x] 변수명 통일하기
  - time, reservationTime 혼용중...
- [ ] 예약 시 현재보다 이전 날짜 예약 불가능 로직 추정
- [x] time repository에서 delete 호출 시 int 반환하도록 변ㅌ환

## 테스트

- [ ] 테스트 환경 개선해보기
  - 카야 리뷰어가 말해준 부분....  어떻게 개선할지는 모르지만...
- [ ] data.sql 사용해서 테스트 중인데 data.sql 의존성 제거해서 테스트 진행하기 .. 

## 리드미

- [ ] 리드미에 API 명세 형식 바꾸기 표 말고, 표로 하니까 가독성이 떨어짐. 그냥 1개씩 소개해서 거기에 Request Body, Response Body 코드 블럭으로 잘 적기
