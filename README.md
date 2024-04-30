## 기능 요구 사항

### 1단계 기능 요구사항

- [x] 예약관리 API가 적절한 응답을 하도록 한다.
- [x] 발생 가능한 예외사항을 처리한다
    - [x] 시간 생성 시 : 시작 시간에 유효하지 않은 값이 입력되었을 때
    - [x] 예약 생성 시 : 예약자명, 날짜, 시간에 유효하지 않은 값이 입력 되었을 때
        - [x] 예약 날짜는 오늘 혹은 오늘 이후여야 한다.
        - [x] 예약 시간
            - [x] 예약 날짜가 오늘이라면 현재 시간 이후여야 한다.
            - [x] 예약 날짜가 오늘이 아니라면, 모든 시간에 예약이 가능하다.
    - [x] 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때

- [x] 새로운 서비스 정책을 반영한다
    - 예약 생성 시
        - [x] 지나간 날짜와 시간에 대한 예약 생성은 불가능하다.
        - [x] 중복 예약은 불가능하다.
        - ```
      ex. 이미 4월 1일 10시에 예약이 되어있다면, 4월 1일 10시에 대한 예약을 생성할 수 없다.
      ```

### 2단계 기능 요구사항

- [ ] 사용자 예약 시 원하는 테마를 선택할 수 있도록 테마 도메인을 추가한다.
    - [ ] 모든 테마는 시작 시간과 소요 시간이 동일하다고 가정합니다.
- [x] 관리자가 테마를 관리할 수 있도록 기능을 추가합니다.
    - [ ] 관리자가 방탈출 예약 시, 테마 정보를 포함할 수 있도록 기능을 변경합니다.

---

## API 명세

### 테마 조회 API

request

```json
GET /themes HTTP/1.1
```

response

```json
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

### 테마 추가 API

request

```json
POST /themes HTTP/1.1
content-type: application/json

{
"name": "레벨2 탈출",
"description": "우테코 레벨2를 탈출하는 내용입니다.",
"thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}
```

response

```json
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

### 테마 삭제 API

request

```json
DELETE /themes/1 HTTP/1.1
```

response

```json
HTTP/1.1 204
```
