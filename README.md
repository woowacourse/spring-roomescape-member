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
    - [ ] 로그인 요청(POST /login)에 응답하는 API
        - [ ] email과 password를 이용해서 멤버를 조회하고
        - [ ] 조회한 멤버로 토큰을 만듭니다.
        - [ ] Cookie를 만들어 응답합니다.
        - [ ] 인증 정보 조회(GET /login/check)하는 API
            - [ ] Cookie에서 토큰 정보를 추출하여
            - [ ] 멤버를 찾아 멤버 정보를 응답합니다.
