## API 명세서

### 어드민 메인 페이지
- http method: GET
- uri: /admin
- file path: templates/admin/index.html

### 어드민 예약 페이지 접근
- http method: GET
- uri: /admin/reservation
- file path: templates/admin/reservation-new.html

### 어드민 시간 페이지 접근
- http method: GET
- uri: /admin/time
- file path: templates/admin/time.html

### 어드민 테마 페이지 접근
- http method: GET
- uri: /admin/theme
- file path: templates/admin/theme.html

### 사용자 예약 페이지 접근
- http method: GET
- uri: /reservation
- file path: templates/reservation.html

### 사용자 기본 페이지 접근
- http method: GET
- uri: /
- file path: templates/index.html

### 모든 예약 조회
- http method: GET
- uri: /reservations
- response
  ```
  HTTP/1.1 200 
  Content-Type: application/json
  
  [
      {
          "id": 1,
          "name": "브라운",
          "date": "2023-01-01",
          "time": {
            "id": 1.
            "startAt": "10:00"
          },
          "theme": {
            "id": 1,
            "name": "레벨2 탈출",
            "description": "우테코 레벨2를 탈출하는 내용입니다.",
            "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
          }
      }
  ]
  ```

### 예약 추가
- http method: POST
- uri: /reservations
- request
  ```
  POST /reservations HTTP/1.1
  content-type: application/json
  
  {
      "date": "2023-08-05",
      "name": "브라운",
      "timeId": 1,
      "themeId": 1
  }
  ```
- response
  - 추가 성공
    ```
    HTTP/1.1 201 
    Location: /reservations/1
    Content-Type: application/json
  
    {
        "id": 1,
        "name": "브라운",
        "date": "2023-08-05",
        "time" : {
            "id": 1.
            "startAt": "10:00"
        }
        "theme": {
            "id": 1,
            "name": "레벨2 탈출",
            "description": "우테코 레벨2를 탈출하는 내용입니다.",
            "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
          }
    }
    ```
  - 추가 실패: 중복 예약 불가능 오류
    ```
    HTTP/1.1 400
  
    {
      "message": "선택하신 테마와 일정은 이미 예약이 존재합니다."
    }
    ```
  - 추가 실패 : 이름 길이 오류
    ```
    HTTP/1.1 400 
    Content-Type: application/json
  
    {
      "message": "이름은 1자 이상, 5자 이하여야 합니다."
    }  
    ```
  - 추가 실패 : 일정 오류
    ```
    HTTP/1.1 400 
    Content-Type: application/json
  
    {
      "message": "현재보다 이전으로 일정을 설정할 수 없습니다."
    }
    ```
  - 추가 실패 : 날짜 오류
    ```
    HTTP/1.1 400
  
    {
      "message": "올바르지 않은 날짜입니다."
    }
    ```
  - 추가 실패 : 존재하지 않는 시간 오류
    ```
    HTTP/1.1 400
    Content-Type: application/json

    {
    "message": "더이상 존재하지 않는 시간입니다."
    }
    ```
  - 추가 실패 : 존재하지 않는 테마 오류
    ```
    HTTP/1.1 400

    {
    "message": "더이상 존재하지 않는 테마입니다."
    }
    ```

### 예약 삭제
- http method: DELETE
- uri: /reservations/{id}
  - path variable
    - id: 예약 정보 식별자
- response
  - 존재하는 id로 삭제 요청
    ```
    HTTP/1.1 204
    ```
  
### 시간 추가
- http method: POST
- uri: /times
- request
  ```
  POST /times HTTP/1.1
  Content-Type: application/json
   
  {
      "startAt": "10:00"
  }
  ```
- response
  - 추가 성공
    ```
    HTTP/1.1 201 
    Location: /times/1
    Content-Type: application/json
  
    {
        "id": 1,
        "startAt": "10:00"
    }
    ```
  - 추가 실패 : 시간 오류
    ```
    HTTP/1.1 400
  
    {
      "message": "올바르지 않은 시간입니다."
    }
    ```
  - 추가 실패: 중복 시간 오류
    ```
    HTTP/1.1 400
  
    {
      "message": "이미 같은 시간이 존재합니다."
    }
    ```

### 시간 조회
- http method: GET
- uri: /times
- response
   ```
  HTTP/1.1 200 
  Content-Type: application/json
  [
   {
        "id": 1,
        "startAt": "10:00"
    }
  ]
  ```

### 예약 가능한 시간 조회
- http method: GET
- uri: /times/available?date=2023-01-01&themeId=1
- request parameter
  - date: 날짜: 필수
  - themeId: 테마 식별자: 필수
- response
   ```
  HTTP/1.1 200 
  Content-Type: application/json
  [
   {
        "id": 1,
        "startAt": "10:00"
    }
  ]
  ```

### 시간 삭제
- http method: DELETE
- uri: /times/{id}
  - path variable
    - id: 시간 정보 식별자
- response
  - 성공: 존재하는 id로 삭제 요청
    ```
    HTTP/1.1 204
    ```
  - 삭제 실패: 이미 예약이 존재하는 시간 삭제 시도 오류
    ```
    HTTP/1.1 400

    {
      "message": "해당 시간에 예약이 존재해서 삭제할 수 없습니다."
    }
    ```

### 테마 추가
- http method: POST
- uri: /themes
- request
  ```
  POST /themes HTTP/1.1
  content-type: application/json
  
  {
      "name": "레벨2 탈출",
      "description": "우테코 레벨2를 탈출하는 내용입니다.",
      "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
  }
  ```
- response
  - 추가 성공
    ```
    HTTP/1.1 201
    Location: /themes/1
    Content-Type: application/json

    {
    "id": 1,
    "name": "레벨2 탈출",
    "description": "우테코 레벨2를 탈출하는 내용입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
    }
    ```
  - 추가 실패 : 이름 오류
    ```
    HTTP/1.1 400

    {
      "message": "이름은 빈칸(공백)일 수 없습니다."
    }
    ```
  - 추가 실패: 중복 이름 오류
    ```
    HTTP/1.1 400

    {
      "message": "이미 존재하는 테마 이름입니다."
    }
    ```
  - 추가 실패: 썸네일 형식 오류
    ```
    HTTP/1.1 400

    {
      "message": "올바르지 않은 썸네일 형식입니다."
    }
    ```

### 테마 조회
- http method: GET
- uri: /themes
- response
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
  
### 인기 테마 조회
- http method: GET
- uri: /themes/popular
- description: 최근 일주일 기준 예약이 많은 테마 10개 조회 (today: 4/8 -> 조회 기간: 4/1~4/7)
- response
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

### 테마 삭제
- http method: DELETE
- uri: /themes/{id}
  - path variable
    - id: 테마 정보 식별자
- response
  - 성공: 존재하는 id로 삭제 요청
    ```
    HTTP/1.1 204
    ```
  - 삭제 실패: 이미 예약이 존재하는 테마 삭제 시도 오류
    ```
    HTTP/1.1 400

    {
      "message": "해당 테마로 예약이 존재해서 삭제할 수 없습니다."
    }
    ```

## 기능 명세서

### 예약
- [x] 예약 정보는 식별자, 이름, 일정으로 이뤄져있다.
  - [x] 이름은 1자 이상, 5자 이하여야 한다.

### 일정
- [x] 일정은 날짜, 예약 시간으로 이뤄져있다.
  - [x] 일정은 현재 이후여야 한다.
  - [x] 날짜는 올바른 형식으로 주어져야 한다.
  - [x] 예약 시간은 이미 존재하는 시간들 중 하나이어야 한다.
  - [x] 테마는 이미 존재하는 테마들 중 하나이어야 한다.

### 시간
- [x] 시간 정보는 식별자, 시작하는 시간으로 이뤄져있다.

### 테마
- [x] 테마는 식별자, 이름, 설명, 썸네일로 이뤄져있다.
  - [x] 이름은 중복될 수 없다.
  - [x] 이름은 빈칸이나 공백일 수 없다.
  - [x] 이름은 1자 이상, 10자 이하여야 한다.
  - [x] 설명은 1자 이상, 125자 이하여야 한다.
  - [x] 썸네일은 1자 이상, 250자 이하여야 한다.
