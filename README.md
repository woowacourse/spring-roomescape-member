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
      - [x] 존재하지 않는 Id를 삭제하면 204 NO_CONTENT

      - Request
        ```
        DELETE /reservations/1 HTTP/1.1
        ```

      - Response
        ```
        HTTP/1.1 200
        ```

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
      HTTP/1.1 200
      Content-Type: application/json
      
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
- [x] 이미 예약된 날짜와 시간에 대한 예약 생성은 불가능하다.

## 2단계

- [x] 사용자 예약 시 원하는 테마를 선택할 수 있도록 테마 도메인을 추가합니다.
  - 테마는 아래의 정보를 가집니다.
    - name: 테마 이름
    - description: 테마 설명
    - thumbnail: 테마 이미지
- [x] 모든 테마는 시작 시간과 소요 시간이 동일하다고 가정합니다.

관리자가 테마를 관리할 수 있도록 기능을 추가합니다.
- [x] API 명세에 따라 아래 기능 구현
    - [x] 테마 조회 API 구현
      - Request
        ```
        GET /themes HTTP/1.1
        ```
        
      - Response
        ```
        GET /themes HTTP/1.1 200 
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

    - [x] 테마 추가 API 구현
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
        Location: /themes/1
        Content-Type: application/json
        
        {
            "id": 1,
            "name": "레벨2 탈출",
            "description": "우테코 레벨2를 탈출하는 내용입니다.",
            "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        }
        ```

    - [x] 테마 삭제 API 구현
      - Request
        ```
        DELETE /themes/1 HTTP/1.1
        ```
        
      - Response
        ```
        HTTP/1.1 204
        ```

- [x] 관리자가 방탈출 예약 시, 테마 정보를 포함할 수 있도록 기능을 변경합니다.
  - [x] 예약 목록 조회 API 호출 후 렌더링 response 명세에 맞춰 값 설정

관리자 기능 (테마 관리, 예약 관리)
- [x] /admin/theme 요청 시 테마 관리 페이지를 응답합니다.
  - [x] 페이지는 templates/admin/theme.html 파일을 이용합니다.
  - [x] 어드민에서 방탈출 예약 시, 테마 정보를 포함할 수 있도록 신규 페이지 파일을 사용합니다.
    - AS-IS: templates/admin/reservation.html
    - TO-BE: templates/admin/reservation-new.html

## 3단계

사용자 예약
- [x] 사용자 예약 가능 시간 조회 API 구현
  - 사용자는 날짜와 테마를 선택하면 예약 가능한 시간을 확인할 수 있습니다.
  - Request
    ```
    GET /availableTimes?date="2025-05-01"&themeId=1 HTTP/1.1
    ```
    
  - Response
    ```
    HTTP/1.1 200 
    Content-Type: application/json
    
    [
        {
            "id": 1,
            "startAt": "10:00"
            "alreadyBooked": true
        }
    ]
    ```

- [x] /reservation 요청 시 사용자 예약 페이지를 응답
  - 페이지는 templates/reservation.html 파일을 이용

인기 테마
- [ ] 인기 테마 조회 API 구현
  - 최근 일주일을 기준으로 해당 기간 내에 방문하는 예약이 많은 테마 10개 응답
  - 예시) 오늘이 4월 8일인 경우, 4월 1일부터 4월 7일까지 예약 건수가 많은 순서대로 10개의 테마를 조회
  - Request
    ```
    GET /themes/popular?startDate="2025-05-01"&endDate="2025-05-07"&limit=10 HTTP/1.1
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

- [x] "/" 요청 시 인기 테마 페이지를 응답
  - 페이지는 templates/index.html 파일을 이용
