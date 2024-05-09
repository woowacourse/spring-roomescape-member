## 기능 요구 사항

### 서비스 정책
- 사용자 서비스 정책
  - 사용자 이름은 숫자로만 이루어질 수 없고, 10자 이하이다.
  - 사용자 이메일은 고유한 Id로, 중복될 수 없다.
- 예약 서비스 정책
  - 예약 시간은 10분 단위이다.
  - 예약 시간은 예약과 독립적으로 존재하고, 생성된 예약 시간으로만 예약할 수 있다.
  - 테마는 예약과 독립적으로 존재하고, 생성된 테마로만 예약할 수 있다.
  - 이전 날짜 혹은 당일 예약은 할 수 없다.
  - 중복되는 예약은 할 수 없다. 동일한 날짜, 시간, 테마에 최대 1팀까지 예약이 가능하다.
  - 예약이 존재하는 예약 시간은 삭제할 수 없다.

### 관리자 페이지

- [x] 메인 페이지: `/admin` 요청 시 응답한다.
- [x] 예약 관리 페이지: `/admin/reservation` 요청 시 응답한다.
- [x] 예약 시간 관리 페이지: `/admin/time` 요청 시 응답한다.
- [x] 테마 관리 페이지: `/admin/theme` 요청 시 응답한다.

### 사용자 페이지
- [x] 메인 페이지: `/` 요청 시 인기 테마 페이지를 응답한다.
- [x] 예약 페이지: `/reservation` 요청 시 응답한다.
- [x] 로그인 페이지: `/login` 요청 시 로그인 폼 페이지를 응답한다.

### 예약 API

- [x] 조회 <br>
    - Request
      ```  
      GET /reservations HTTP/1.1
      ```
    - Response
      ```  
      HTTP/1.1 200
      Content-Type: application/json
      
      [
         {
            "id": 1,
            "name": "브라운",
            "date": "2023-01-01",
            "time": {
                "id": 1,
                "startAt": "15:00"
            },
            "theme": {
                "id": 1,
                "name": "레벨2 탈출"
            }
         },
         {
            "id": 2,
            "name": "브라운",
            "date": "2023-01-02",
            "time": {
                "id": 1,
                "startAt": "15:00"
            },
            "theme": {
                "id": 1,
                "name": "레벨2 탈출"
            }
         }
      ]
      ```
- [x] 추가
    - Request
      ``` 
      POST /reservations HTTP/1.1
      content-type: application/json
      cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI

      {
          "date": "2023-08-05",
          "timeId": 1,
          "themeId": 1
      }
      ```
    - Response
      ```  
      HTTP/1.1 201
      Content-Type: application/json
    
      {
         "id": 1,
         "name": "브라운",
         "date": "2023-08-05",
         "time": {
              "id": 1,
              "startAt": "15:00"
         },
         "theme": {
              "id": 1,
              "name": "레벨2 탈출"
         }
      }
      ```
- [x] 삭제
    - Request
      ``` 
      DELETE /reservations/1 HTTP/1.1
      ```
    - Response
      ```  
      HTTP/1.1 204
      ```
### 관리자 예약 API
- [x] 추가
    - Request
      ``` 
      POST /admin/reservations HTTP/1.1
      content-type: application/json
      cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI

      {
          "date": "2023-08-05",
          "timeId": 1,
          "themeId": 1,
          "memberId": 1
      }
      ```
    - Response
      ```  
      HTTP/1.1 201
      Content-Type: application/json
    
      {
         "id": 1,
         "name": "브라운",
         "date": "2023-08-05",
         "time": {
              "id": 1,
              "startAt": "15:00"
         },
         "theme": {
              "id": 1,
              "name": "레벨2 탈출"
         }
      }
      ```

### 예약 시간 API
- [x] 조회 <br>
    - Request
      ```  
      GET /times HTTP/1.1
      ```
    - Response
      ```  
      HTTP/1.1 200
      Content-Type: application/json
      
      [
         {
            "id": 1,
            "startAt": "15:00"
         },
         {
            "id": 2,
            "startAt": "16:00"
         }
      ]
      ```
- [x] 추가
    - Request
      ``` 
      POST /times HTTP/1.1
      content-type: application/json
    
      {
          "startAt": "15:00"
      }
      ```
    - Response
      ```  
      HTTP/1.1 201
      Content-Type: application/json
    
      {
         "id": 1,
         "startAt": "15:00"
      }
      ```
- [x] 삭제
    - Request
      ``` 
      DELETE /times/1 HTTP/1.1
      ```
    - Response
      ```  
      HTTP/1.1 204
      ```
### 테마 API
- [x] 조회 <br>
    - Request
      ```  
      GET /themes HTTP/1.1
      ```
    - Response
      ```  
      HTTP/1.1 200
      Content-Type: application/json
      
      [
         {
            "id": 1,
            "name": "레벨2 탈출",
            "description": "우테코 레벨2를 탈출하는 내용입니다.",
            "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
         }
      ]
      ```
- [x] 추가
    - Request
      ``` 
      POST /themes HTTP/1.1
      content-type: application/json
    
      {
          "name": "레벨2 탈출",
          "description": "우테코 레벨2를 탈출하는 내용입니다.",
          "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
      }
      ```
      
    - Response
      ```  
      HTTP/1.1 201
      Content-Type: application/json
    
      {
         "id": 1,
         "name": "레벨2 탈출",
         "description": "우테코 레벨2를 탈출하는 내용입니다.",
         "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
      }
      ```
- [x] 삭제
    - Request
      ``` 
      DELETE /themes/1 HTTP/1.1
      ```
    - Response
      ```  
      HTTP/1.1 204
      ```
- [x] 인기 테마 조회 

  최근 일주일을 기준으로 하여 해당 기간 내에 방문하는 예약이 많은 테마 10개를 조회한다.

    - Request
      ``` 
      GET /themes/popular HTTP/1.1
      ```
    - Response
      ```  
      HTTP/1.1 200
      Content-Type: application/json
      
      [
         {
            "id": 1,
            "name": "레벨2 탈출",
            "description": "우테코 레벨2를 탈출하는 내용입니다.",
            "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
         },
         {
            "id": 2,
            "name": "호러",
            "description": "매우 무섭습니다.",
            "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
         }
      ]
      ```
### 인증 API
- [x] 로그인
  - Request
      ```
      POST /login HTTP/1.1
      content-type: application/json
    
      {
        "password": "password",
        "email": "admin@email.com"
      }
      ```
  - Response
      ```
      HTTP/1.1 200
      Content-Type: application/json
      Set-Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI; Path=/; HttpOnly
      ```
- [x] 인증 정보 조회
    - Request
        ```
        GET /login/check HTTP/1.1
        cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
        ```
    - Response
        ```
        HTTP/1.1 200 
        Content-Type: application/json
      
        {
          "name": "어드민"
        }
        ```
### 사용자 API
- [x] 일반 사용자 회원가입
    - Request
        ```
        POST /members/join HTTP/1.1
        content-type: application/json
      
        {
          "password": "password",
          "email": "admin@email.com",
          "name": "브라운"
        }
        ```
  - Response
      ```
      HTTP/1.1 201
      Content-Type: application/json
      {
          "id": 1,
          "email": "admin@email.com",
          "name": "브라운"
      }
      ```
- [x] 관리자 회원가입
    - 위와 동일하나, 엔드포인트만 `/members/join/admin`으로 변경됨
- [x] 목록 조회
    - Request
        ```
        GET /members HTTP/1.1
        content-type: application/json
        ```
    - Response
      ```  
      HTTP/1.1 200
      Content-Type: application/json
      
      [
         {
            "id": 1,
            "name": "브라운",
            "email": "brown@gamil.com"
         }
      ]
      ```
