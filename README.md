# 방탈출 예약 관리

## 기능 목록
- [x] `localhost:8080/admin` 요청 시 어드민 메인 페이지가 응답할 수 있게 한다.
  - [x] 어드민 메인 페이지는 `templates/admin/index.html` 파일을 이용한다.
- [x] 필요한 의존성을 찾아서 `build.gradle`에 추가한다.
- [x] `/admin/reservation` 요청 시 예약 관리 페이지가 응답할 수 있게 한다.
  - [x] 페이지는 `templates/admin/reservation-legacy.html` 파일을 이용한다.
- [x] API 명세를 따라 예약 관리 페이지 로드 시 호출되는 예약 목록 조회 API도 구현한다.
- [x] API 명세를 따라 예약 추가 API를 구현한다.
- [x] API 명세를 따라 예약 삭제 API를 구현한다.
  - Spring MVC가 제공하는 Annotation을 활용한다.
- [x] 예약 정보의 식별자를 생성할 때 AtomicLong을 활용한다. 
- [x] h2 데이터베이스를 연동한다.
- [x] 데이터베이스의 예약 스키마를 추가한다.
- [x] 예약 조회를 구현한다.
- [x] 에약 추가를 구현한다.
- [x] 에약 삭제를 구현한다.
- [x] 시간 추가를 구현한다.
- [x] 시간 조회를 구현한다.
- [x] 시간 삭제를 구현한다.
- [x] 시간 관리 페이지를 구현한다.
- [x] 예약 페이지 파일 수정한다.
- [x] 외래키 지정을 통해 예약 테이블과 예약 시간 테이블의 관계를 설정한다.

## 커밋 컨밴션
- feat, docs, fix, refactor, test, style, chore
> EX) feat: Piece id 필드 추가
