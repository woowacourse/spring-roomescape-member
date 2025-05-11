# 방탈출 사용자 예약

## 4단계

### 요구사항

- [x] 사용자 도메인 추가
    - 사용자
        - name : 사용자 이름
        - email : 이메일
        - password : 비밀번호
    - email을 로그인 id
    - password를 비밀번호로 사용
- 로그인 기능 구현
    - [x] 로그인 페이지 호출 시 GET /login 요청이 호출되고, login.html 페이지가 응답
    - [x] 로그인 요청(POST /login)에 응답하는 API
        - [x] email과 password를 이용해서 멤버를 조회하고
            - [x] 사용자 DB 테이블 스키마 생성
                - table member (user는 h2 예약어라 사용할 수 없고, 관리자도 같은 테이블에서 관리할 것이기 때문에 member로 네이밍)
            - [x] 테스트용 더미 데이터 삽입 후 조회
        - [x] 조회한 멤버로 토큰을 만듭니다.
            - 조회한 멤버의 email을 payload로 사용
        - [x] Cookie를 만들어 응답합니다.
            - header에 cookie 생성하여 정보 입력
        - [x] 인증 정보 조회(GET /login/check)하는 API
            - [x] Cookie에서 토큰 정보를 추출하여
            - [x] 멤버를 찾아 멤버 정보를 응답합니다.

## 5단계

### 요구사항

- [x] Cookie에 담긴 인증 정보를 이용해서 멤버 객체를 만드는 로직을 분리
    - [x] 클라이언트 코드 변경
    - [x] HandlerMethodArgumentResolver을 활용하여 회원 객체를 컨트롤러 메서드에 주입
    - [x] 기존 예약 추가 방식(이름을 필드에 입력하는 방식) -> 쿠키에서 멤버 이름 조회해서 name 의 not null

## 6단계

### 요구사항

- 접근 권한 제어
    - [x] Memeber의 Roledl Admin인 사람만 /admin 으로 시작하는 페이지에 접근할 수 있도록 구현
        - [x] /admin으로 시작하는 요청들을 확인
        - [x] cookie -> token -> email 로 member.role 조회
    - [x] HandlerInterceptor를 이용해 요청자의 권한을 확인하고, 권한이 없는 경우 거부 응답
    - [x] 컨트롤러에 진입하기 전에 Cookie 값을 확인하여 role를 확인
    - [x] return 값에 따라 처리되는 방식을 확인
        - return false 의 경우 컨트롤러 진입이 안된다.
            ```java
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
          
                // ...
          
                if (member == null || !member.getRole().equals("ADMIN")) {
                    response.setStatus(401);
                    return false;
                }
          
                return true;
            }
            ```

- 예약 검색 기능 추가
    - 어드민 > 예약 관리 페이지에서 검색 조건을 선택하고 적용을 누르면, reservation-with-member.js의 applyFilter() 함수가 실행된다.
    - [ ] 입력한 themeId, memberId, dateFrom, dateTo 값을 사용해 검색 기능을 완성
    - [ ] js 명세를 활용해 필터링한 예약 정보를 반환하도록 구현
