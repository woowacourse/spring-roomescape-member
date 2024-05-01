# 미션 2 기능 요구 사항

## API 명세

##### 🔧 테마 조회

```
- 요청

GET /themes HTTP/1.1
```

<br>

```
- 응답

HTTP/1.1 200 
Content-Type: application/json
{
    "id": 1,
    "name": "레벨2 탈출",
    "description": "우테코 레벨2를 탈출하는 내용입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}
```

<br>

##### 🔧 테마 추가

```
- 요청

POST /themes HTTP/1.1
content-type: application/json

{
    "name": "레벨2 탈출",
    "description": "우테코 레벨2를 탈출하는 내용입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}
```

<br>

```
- 응답

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

<br>

##### 🔧 테마 삭제

```
- 요청

DELETE /themes/1 HTTP/1.1
```

<br>

```
- 응답

HTTP/1.1 204
```

<br>

## 예외 처리

- [x] 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때
- [x] 날짜 생성 시 날짜에 유효하지 않은 값이 입력되었을 때
- [x] 예약자 명에 null, 빈 문자열, 공백만 입력되었을 때
- [x] 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때
- [x] 지나간 날짜와 시간에 대한 예약 생성은 불가능하다.
- [x] 중복 예약은 불가능하다.
    - ex. 이미 4월 1일 10시에 예약이 되어있다면, 4월 1일 10시에 대한 예약을 생성할 수 없다.
- [x] 사용자의 잘못된 요청은 500이 아닌 400 코드로 응답한다.

