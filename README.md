## 4단계 요구사항

- [x] 사용자 도메인을 추가한다.
    - [x] 사용자는 아래의 정보를 가진다.
        - name : 사용자 이름
        - email : 이메일
        - password : 비밀번호
    - [x] email을 로그인의 id로, password를 비밀번호로 사용한다.
    - [x] 사용자 테이블을 생성한다.
- [x] 로그인 기능을 구현한다.
    - [x] `GET /login` 요청 시 로그인 폼이 있는 페이지를 응답한다.
        - [x] `templates/login.html` 파일을 이용한다.
    - [x] `POST /login` 요청 시 로그인 폼에 입력한 email, password 값을 body에 포함한다.
    - [x] 응답 Cookie에 "token" 값으로 토큰이 포함되도록 한다.
- [x] 검증 로직을 추가한다.
    - [x] 토큰 dto
    - [x] Member domain

```
POST /login HTTP/1.1
content-type: application/json
host: localhost:8080

{
    "password": "password",
    "email": "admin@email.com"
}

HTTP/1.1 200 OK
Content-Type: application/json
Keep-Alive: timeout=60
Set-Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI; Path=/; HttpOnly
```

- [x] 로그인 후 Cookie를 이용하여 사용자의 정보를 조회하는 API를 구현한다.
    - [x] 상단바 우측 로그인 상태를 표현해주기 위해 사용자의 정보를 조회하는 API를 구현한다.
    - [x] Cookie를 이용하여 로그인 사용자의 정보를 확인한다.

``` 
GET /login/check HTTP/1.1
cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
host: localhost:8080

HTTP/1.1 200 OK
Connection: keep-alive
Content-Type: application/json
Date: Sun, 03 Mar 2024 19:16:56 GMT
Keep-Alive: timeout=60
Transfer-Encoding: chunked

{
    "name": "어드민"
}
```

## 5단계 요구사항

- [x] 사용자의 정보를 조회하는 로직을 리팩터링 한다.
    - [x] Cookie에 담긴 인증 정보를 이용해서 멤버 객체를 만드는 로직을 분리한다.
    - [x] HandlerMethodArgumentResolver 을 활용하면 회원정보 객체를 컨트롤러 메서드에 주입할 수 있다.
- [x] 예약 생성 API 및 기능을 리팩터링 한다.
    - 사용자
    - [x] 사용자가 예약 생성 시, 로그인한 사용자 정보를 활용하도록 리팩터링한다.
    - [x] `reservation.html`, `user-reservation.js` 파일의 TODO 주석을 참고하여 변경된 명세에 맞게 클라이언트가 동작하도록 변경한다.
    - **쿠키를 이용한 예약 생성 Request**
    ``` yaml
    POST /reservations HTTP/1.1
    content-type: application/json
    cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI
    host: localhost:8080
    
    {
        "date": "2024-03-01",
        "themeId": 1,
        "timeId": 1
    }
    ```
    - 관리자
    - [x] 관리자가 예약 생성 시, 유저를 조회하여 선택 후 예약을 생성하도록 리팩터링 한다.
    - [x] `admin/reservation-new.html` 파일에서 안내된 4단계 관련 주석에 따라, 로딩하는 js 파일을 변경한다.
        - [x] AS-IS: `/js/reservation-new.js`
        - [x] TO-BE: `/js/reservation-with-member.js`
    - **요청 Body 값을 이용한 예약 생성 Request**
    ``` yaml
    POST /admin/reservations HTTP/1.1
    content-type: application/json
    cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI
    host: localhost:8080
    
    {
        "date": "2024-03-01",
        "themeId": 1,
        "timeId": 1,
        "memberId": 1
    }
    ```

## 6단계 요구사항

- [x] ***< 접근 권한 제어 >*** : 어드민 페이지 진입은 admin 권한이 있는 사람만 할 수 있도록 제한한다.
    - [x] Member의 Role이 ADMIN 인 사람만 /admin 으로 시작하는 페이지에 접근할 수 있다.
    - [x] HandlerInterceptor를 활용하여 권한을 확인하고, 권한이 없는 경우 요청에 대한 거부 응답을 한다.
- [x] ***< 예약 목록 검색 >*** : 관리자가 조건에 따라 예약을 검색할 수 있도록 기능을 추가한다.
    - [x] 예약이 많아질 경우 관리가 용이하도록 예약 검색 기능을 추가한다.
    - [x] 예약자별, 테마별, 날짜별 검색 조건을 사용해 예약 검색이 가능하도록 기능을 추가한다.
- [x] [6단계] 주석을 검색하여 안내사항에 맞게 클라이언트 코드를 수정한다.
    - 힌트
        - ***< 예약 목록 검색 >***
        - `어드민 > 예약 관리 페이지`에서 검색 조건을 선택하고 적용을 누르면,
          reservation-with-member.js의 applyFilter() 함수가 실행 된다.
            - 입력한 themeId, memberId, dateFrom, dateTo 값을 사용해 검색 기능을 완성하세요.
        - ***< HandlerInterceptor >***
            - 컨트롤러에 진입하기 전에 Cookie 값을 확인하여 role를 확인한다.
            - return 값에 따라 처리되는 방식을 확인한다.
      ``` java
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
    
