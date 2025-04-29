# 🚪방탈출 예약 관리 애플리케이션

방탈출 예약을 위해 가능한 시간대를 추가하고, 이를 기반으로 방탕출을 예약할 수 있는 서비스입니다.
* http://localhost:8080/admin/time 페이지에서 시간대를 추가할 수 있습니다.
* http://localhost:8080/admin/reservation 페이지에서 방탈출에 대한 예약을 진행할 수 있습니다.

## 요구사항 분석

### 1. 홈 화면

- [x] `/admin`으로 요청시 메인 페이지 응답

### 2. 예약 조회

- [x] `/admin/reservation` 요청 시 예약 관리 페이지 응답
- [x] 예약 관리 페이지 로드 시 호출되는 예약 목록 조회
- [x] 예약 조회 시, H2 데이터베이스 활용

### 3. 예약 추가 / 취소

- [x] 예약 관리 페이지 내에서 예약 추가
- [x] 예약 관리 페이지 내에서 예약 삭제
- [x] 예약 추가/취소 시, H2 데이터베이스 활용
- [x] 예약 시간은 추가된 시간만 선택 가능

### 4. 시간 관리 기능

- [x] `/admin/time` 요청 시 시간 관리 페이지 응답
- [x] 시간 관리 페이지 내에서 시간 추가
- [x] 시간 관리 페이지 내에서 시간 조회
- [x] 시간 관리 페이지 내에서 시간 삭제

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
