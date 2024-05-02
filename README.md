## 기능 요구 사항

### 구현 기능 목록
- 시간 관리, 예약 관리 API 명세 변경
- 예외 처리
  - 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때
    - 예약 시간은 10분 단위이다.
  - 예약 생성 시 날짜에 유효하지 않은 값이 입력되었을 때
    - 이전 날짜 혹은 당일 예약은 할 수 없다.
  - 예약 생성 시 예약자 이름에 유효하지 않은 값이 입력되었을 때
    - 예약자 이름은 숫자이거나 비어있을 수 없다.
  - 중복되는 예약을 할 때
    - 동일한 날짜, 시간, 테마에 최대 1팀까지 예약이 가능하다.
  - 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때

### 관리자 페이지

- [x] 메인 페이지: `/admin` 요청 시 응답한다.
- [x] 예약 관리 페이지: `/admin/reservation` 요청 시 응답한다.
- [x] 예약 시간 관리 페이지: `/admin/time` 요청 시 응답한다.
- [x] 테마 관리 페이지: `/admin/theme` 요청 시 응답한다.

### 사용자 페이지
- [x] 예약 페이지: `/reservation` 요청 시 응답한다.


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
            }
         },
         {
            "id": 2,
            "name": "브라운",
            "date": "2023-01-02",
            "time": {
                "id": 1,
                "startAt": "15:00"
            }
         }
      ]
      ```
- [x] 추가
    - Request
      ``` 
      POST /reservations HTTP/1.1
      content-type: application/json
    
      {
          "date": "2023-08-05",
          "name": "브라운",
          "timeId": 1
      }
      ```
    - Response
      ```  
      HTTP/1.1 200
      Content-Type: application/json
    
      {
         "id": 1,
         "name": "브라운",
         "date": "2023-08-05",
         "time": {
              "id": 1,
              "startAt": "15:00"
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
      HTTP/1.1 200
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
      HTTP/1.1 200
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
      HTTP/1.1 200
      ```
