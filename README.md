# 🚪방탈출 사용자 예약 애플리케이션

## 요구사항 분석

### 1. 예외 처리

- [x] 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때
  - [x] null이 들어오면 예외가 발생한다.
  - [x] 이미 존재하는 시간은 등록할 수 없다.
- [x] 예약 생성 시 예약자명, 날짜, 시간에 유효하지 않은 값이 입력 되었을 때
  - [x] 값중에 하나라도 null이 들어오면 예외가 발생한다.
  - [x] 등록되지 않은 시간인 경우 예외가 발생한다.
- [x] 아래와 같은 서비스 정책을 반영
  - [x] 지나간 날짜와 시간에 대한 예약 생성은 불가능하다.
  - [x] 중복 예약은 불가능하다.
  - [x] 특정 시간에 대한 예약이 존재하면 그 시간은 삭제가 불가능하다.

### 2. 예외 응답

- [x] null, 생성할 때의 예외는 BadRequest로 응답한다.
  - null InvalidInputException
  - 생성 TimeDoesNotExistException, NotCorrectDateTimeException
- [x] 중복, 삭제할 때의 예외는 Conflict로 응답한다.
  - 중복 DuplicateTimeException, DuplicateReservationException

---

## CRUD API 명세

### 예약 목록 조회

* Request
    ```
    GET /reservations HTTP/1.1
    ```
* Response
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
            "startAt": "10:00"
        }
      }
    ]
    ```

### 예약 추가

* Request
    ```
    POST /reservations HTTP/1.1
    content-type: application/json

    {
      "date": "2023-08-05",
      "name": "브라운",
      "timeId": 1
    }
    ```
* Response
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

### 예약 취소

* Request
    ```
    DELETE /reservations/1 HTTP/1.1
    ```
* Response
    ```
    HTTP/1.1 200
    ```

### 시간 추가

* Request
    ```
    POST /times HTTP/1.1
    content-type: application/json

    {
      "startAt": "10:00"
    }
    ```
* Response
    ```
    HTTP/1.1 200
    Content-Type: application/json

    {
       "id": 1,
       "startAt": "10:00"
    }
    ```

### 시간 조회

* Request
    ```
    GET /times HTTP/1.1
    ```
* Response
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

* Request
    ```
    DELETE /times/1 HTTP/1.1
    ```
* Response
    ```
    HTTP/1.1 200
    ```
