# Step2 추가 구현 사항

## 인증, 인가

- [X] 로그인 API 구현
  - [X] id, password 를 통해 멤버 유효성 체크
  - [X] Jwt생성 로직 구현
- [X] 로그아웃 API 구현
  - [X] 클라이언트 쿠키 만료
  - [ ] 이미 발급한 Token 블랙리스트 추가
- [X] 로그인 체크 API 구현
  - [X] 쿠키에 토큰이 담겼다면, 인증 완료로 판단
  - [X] 쿠키에 담긴 토큰을 통해 로그인 체크 및 인증정보 반환
    - [X] Jwt 파싱 로직 구현
- [X] 인증정보 반환 ArgumentResolver 구현
  - [X] ArgumentResolver 발동 조건 어노테이션 정의
- [X] 특정 URL에 어드민 권한체크 Interceptor 구현
  - [X] /admin/*에 발동

## 멤버(Member)

- [X] Member 테이블 스키마 정의
- [X] Member 엔터티 정의
- [X] Member 전체 조회 API 구현


## 예약(Reservation)

- [X] 어드민 예약생성API와 일반 사용자 예약생성API 명세 분리
- [X] 예약 검색조회 API 구현
- [X] Member와 다대일 연관관계로 테이블 스키마 수정

## 리팩토링

-[X] 전체적인 패키지 구조 변경
  - [X] 도메인 중심형 패키지
