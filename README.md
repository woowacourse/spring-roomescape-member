## 방탈출 사용자 예약

### 1단계 - 예외 처리와 응답

- 아래 상황에 대한 예외 처리를 한다
    - 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때
    - 예약 생성 시 예약자명, 날짜, 시간에 유효하지 않은 값이 입력 되었을 때
    - 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때
    - 존재하지 않는 시간에 대한 예약을 생성하려고 할 때

- 아래 서비스 정책을 적용한다
    - 지나간 날짜와 시간에 대한 예약 생성은 불가능하다
    - 중복 예약은 불가능하다

### 2단계 - 테마 추가

- 관리자가 테마를 관리할 수 있도록 기능을 추가합니다.
- 관리자가 방탈출 예약 시, 테마 정보를 포함할 수 있도록 기능을 변경합니다.
- /admin/theme 요청 시 테마 관리 페이지를 응답 합니다

API 명세서

테마 조회 API

request
```
GET /themes HTTP/1.1
```

response
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

테마 추가 API

request
```
POST /themes HTTP/1.1
content-type: application/json

{
  "name": "레벨2 탈출",
  "description": "우테코 레벨2를 탈출하는 내용입니다.",
  "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}
```

response
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

테마 삭제 API

request
```
DELETE /themes/1 HTTP/1.1
```
response
```
HTTP/1.1 204
```

### 3단계 - 사용자 기능
- (관리자가 아닌) 사용자가 예약 가능한 시간을 조회하고, 예약할 수 있도록 기능을 추가/변경 합니다.
  - 사용자는 날짜와 테마를 선택하면 예약 가능한 시간을 확인할 수 있습니다.
  - 사용자는 예약 가능한 시간을 확인하고, 원하는 시간에 예약을 할 수 있습니다.
  - 예약 시 사용자 구분은 어드민과 동일하게 사용자의 이름으로 합니다.
- 인기 테마 조회 기능을 추가합니다.

### 4단계 - 사용자 로그인
- /login 요청 시 로그인 폼이 있는 페이지를 응답합니다
- 로그인 기능을 사용할 수 있습니다.
- 로그인 후 사용자의 정보를 조회할 수 있습니다.
