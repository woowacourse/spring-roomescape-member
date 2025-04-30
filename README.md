# 방탈출 사용자 예약

## 요구사항

- [X] 시간 관리, 예약 관리 API가 적절한 응답을 하도록 변경
- [X] 어드민의 시간 관리 페이지, 방탈출 예약 페이지에서 모든 기능이 정상적으로 동작하는지 확인합니다.

## 유효성 검증

### 시간

- [X] 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때
  - null, 공백
  - "HH:mm" 형식에 올바르지 않은 경우
- [X] 특정 시간에 대한 현예약이 존재하는데, 그 시간을 삭제하려 할 때
- [X] 동일한 시간은 생성할 수 없다.

### 날짜

- [X] 지나간 날짜와 시간에 대한 예약 생성은 불가능하다.
  - 지난 날짜인 경우 불가능
  - 같은 날짜이면서 시간이 지난 경우 불가능
  - 미래 날짜인데 시간이 현재보다 과거인 경우는 가능

### 예약

- [X] 예약 생성 시 예약자명, 날짜, 시간에 유효하지 않은 값이 입력 되었을 때
- [X] 중복 예약은 불가능하다.
  - ex. 이미 4월 1일 10시에 예약이 되어있다면, 4월 1일 10시에 대한 예약을 생성할 수 없다.

---

## API 명세

### 테마 조회

- [X] 테마 조회 API를 구현한다.
  - Request
    ```
    GET /themes HTTP/1.1
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

### 테마 추가

- [X] 테마 추가 API를 구현한다.
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

### 테마 삭제

- [ ] 테마 삭제 API를 구현한다.
  - Request
    ```
    DELETE /themes/1 HTTP/1.1
    ```

  - Response
    ```
    HTTP/1.1 204
    ```
