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
  - null: InvalidInputException
  - 생성: TimeDoesNotExistException, NotCorrectDateTimeException -> ReservationC
- [x] 중복, 삭제할 때의 예외는 Conflict로 응답한다.
  - 중복: DuplicateTimeException, DuplicateReservationException

### 3. 테마 선택 기능

- [x] `/admin/theme` 요청 시 테마 관리 페이지를 응답
- [x] 테마 조회 기능 응답
- [x] 테마 추가 기능 응답
- [x] 테마 삭제 기능 응답

### 4. 사용자 예약 기능

- [x] 사용자가 예약 가능한 시간을 조회하고 예약할 수 있다.
  - [x] 사용자는 날짜와 테마를 선택하면 예약 가능한 시간을 조회
  - [x] 예약 가능한 시간을 확인하고, 원하는 시간에 예약 추가
  - [x] `/reservation` 요청 시 사용자 예약 페이지 응답
- [ ] 인기 테마 조회할 수 있다.
  - [ ] 최근 일주일을 기준으로 해당 기간 내에 방문하는 예약이 많은 테마 10개를 확인
  - [ ] 오늘 날짜 이전으로 일주일 (4월 8일인 경우, 게임 날짜가 4월 1일부터 4월 7일까지)
  - [ ] `/` 요청 시 인기 테마 페이지를 응답

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

### 테마 추가

* Request
    ```
    POST /themes HTTP/1.1
    content-type: application/json

    {
      "name": "레벨2 탈출",
      "description": "우테코 레벨2를 탈출하는 내용입니다.",
      "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
    }
    ```
* Response
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

### 테마 조회

* Request
    ```
    GET /themes HTTP/1.1
    ```
* Response
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

* Request
    ```
    DELETE /themes/1 HTTP/1.1
    ```
* Response
    ```
    HTTP/1.1 204
    ```
