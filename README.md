## 기능 요구 사항

### 어드민 페이지

- [x] 메인 페이지: `/admin` 요청 시 응답한다.
- [x] 예약 관리 페이지: `/reservation` 요청 시 응답한다.
- [x] 예약 시간 관리 페이지: `/time` 요청 시 응답한다.

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

### 추가된 비즈니스 기능
- 예약자 이름은 숫자이거나 비어있을 수 없다.
- 예약 날짜는 현재 날짜 이전이거나 비어있을 수 없다.
- 예약 시간은 10분 단위이며, 존재하는 예약 시간 목록 중 하나를 선택한다.
- 동일한 시간에 최대 4팀까지 예약이 가능하다.
- 동일한 시간에 동일한 예약자가 예약할 수 없다.
