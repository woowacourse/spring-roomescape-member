# 방탈출 예약 관리 프로그램

## 1단계

- [x] localhost:8080/admin 요청 시 어드민 메인 페이지(templates/admin/index.htm)를 응답한다.

## 2단계

- [x] /admin/reservation 요청 시 예약 관리 페이지(templates/admin/reservation-legacy.html)가 응답한다.
- [x]  API 명세에 따라 아래 기능 구현
- [x] 예약 확인 API 구현
    - [x] Request
    ```
    GET /reservations HTTP/1.1
    ```

    - [x] Response
    ```
    HTTP/1.1 200
    Content-Type: application/json
  
    [
    {
    "id": 1,
    "name": "브라운",
    "date": "2023-01-01",
    "time": "10:00"
    },
    {
    "id": 2,
    "name": "브라운",
    "date": "2023-01-02",
    "time": "11:00"
    }
    ]
    ```

## 3단계

- [x] API 명세에 따라 아래 기능 구현
    - [x] 예약 추가 API 구현
        - [x] Request
        ```
        POST /reservations HTTP/1.1
        content-type: application/json
      
        {
        "name": "브라운",
        "date": "2023-08-05",
        "time": "15:40"
        }
        ```
        - [x] 이름, 날짜, 시간 중 하나라도 없으면 400 Bad Request
        - [x] 이름, 날짜, 시간 중 잘못된 형식이면 400 Bad Request

        - [x] Response
        ```
        HTTP/1.1 200
        Content-Type: application/json
      
        {
        "id": 1,
        "name": "브라운",
        "date": "2023-08-05",
        "time": "15:40"
        }
        ```

    - [x] 예약 삭제 API 구현
        - Request
        ```
        DELETE /reservations/1 HTTP/1.1
        Response
        HTTP/1.1 200
        ```
        - [ ] 존재하지 않는 Id를 삭제하면 204 NO_CONTENT

## 4 ~ 6단계

- [x] 데이터를 저장하기 위해 h2 데이터베이스 연동
- [x] DB로부터 예약 조회
- [x] DB로부터 예약 추가
- [x] DB로부터 예약 삭제

## 7단계

- [x] /admin/time 요청 시 시간 관리 페이지를 응답한다.

- [x] API 명세에 따라 아래 기능 구현
    - [x] 시간 추가 API 구현
        - Request
        ```
        POST /times HTTP/1.1
        content-type: application/json
        
        {
        "startAt": "10:00"
        }
        ```
      
        - Response
        ```
        HTTP/1.1 200
        Content-Type: application/json
        
        {
        "id": 1,
        "startAt": "10:00"
        }
        ```
    - [x] 시간 조회 API 구현
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
              "startAt": "10:00"
          }
      ]
      ```

    - [x] 시간 삭제 API 구현
      - Request
      ```
      DELETE /times/1 HTTP/1.1
      ```

      - Response
      ```
      HTTP/1.1 200
      ```

## 8단계

- [x] 방탈출 예약 시 시간 테이블에 저장된 시간만 선택할 수 있도록 수정

- [x] 예약 추가/조회 API 명세 변경
  - [x] 예약 추가 API
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
        "time" : {
            "id": 1,
            "startAt" : "10:00"
        }
    }
    ```

  - [x] 예약 조회 API
    - Request
    ```
    GET /reservations HTTP/1.1
    ```
    
    - Response
    ```
    [
        {
            "id": 1,
            "name": "브라운",
            "date": "2023-08-05",
            "time": {
                "id": 1,
                "startAt": "10:00"
            }
        }
    ]
    ```

## 9단계

- [x] 레이어드 아키텍쳐 적용
