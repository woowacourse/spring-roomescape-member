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

# 방탈출 사용 예약

## 1단계

어드민 페이지 기능이 정상적으로 동작하도록 보완합니다.
- [x] 시간 관리 API, 예약 관리 API가 적절한 응답을 하도록 변경

발생할 수 있는 예외 상황에 대한 처리를 하여, 사용자에게 적절한 응답을 합니다.
- [ ] 예약 생성 시 예약자명, 날짜, 시간에 유효하지 않은 값이 입력 되었을 때
- [x] 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때
- [ ] 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때

서비스 정책을 반영합니다.
- [x] 지나간 날짜와 시간에 대한 예약 생성은 불가능하다.
- [ ] 중복 예약은 불가능하다.
