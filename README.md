# 🚪방탈출 사용자 예약 애플리케이션

## 요구사항 분석

### 1. 예외 처리와 응답

- [ ] 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때
  - [ ] null이 들어오면 예외가 발생한다.
  - [ ] `HH:mm` 형태가 아닌 경우 예외가 발생한다.
  - [ ] 이미 존재하는 시간은 등록할 수 없다.
- [ ] 예약 생성 시 예약자명, 날짜, 시간에 유효하지 않은 값이 입력 되었을 때
  - [ ] 값중에 하나라도 null이 들어오면 예외가 발생한다.
  - [ ] 날짜의 형태가 `yyyy-MM-dd`이 아닌 경우 예외가 발생한다.
  - [ ] 등록되지 않은 시간인 경우 예외가 발생한다.
- [ ] 아래와 같은 서비스 정책을 반영
  - [ ] 지나간 날짜와 시간에 대한 예약 생성은 불가능하다.
  - [ ] 중복 예약은 불가능하다.
  - [ ] 특정 시간에 대한 예약이 존재하는데 그 시간은 삭제가 불가능하다.

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
