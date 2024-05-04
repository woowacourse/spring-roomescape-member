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
    "name": "레벨2 탈출",
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

##### 🔧 최근 1주일 기준 인기 테마 조회

```
- 요청

GET /themes/popular HTTP/1.1
```

<br>

```
- 응답

HTTP/1.1 200 
Content-Type: application/json
{
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

#### reservation (예약)
- [x] 예약자 명에 null, 빈 문자열, 공백만 입력되었을 때
- [x] 예약자 명은 4글자까지 입력 가능하다.
- [x] 예약자 명은 한글로만 입력 가능하다.
- [ ] 이미 예약한 예약자를 추가할 수 없다.
- [x] 날짜 생성 시 날짜에 유효하지 않은 값이 입력되었을 때
- [x] 지나간 날짜로 예약 했을 때
- [x] 중복 예약은 불가능하다.
  - ex. 이미 4월 1일 10시에 예약이 되어있다면, 4월 1일 10시에 대한 예약을 생성할 수 없다.

#### theme (테마)
- [x] 테마 명에 null, 빈 문자열, 공백만 입력되었을 때
- [x] 테마 명은 10글자까지 입력 가능하다.
- [x] 테마 명은 공백 포함 한글, 영어, 숫자만 입력가능하다.
- [ ] 이미 예약한 테마명은 추가할 수 없다.
- [ ] 설명에 null, 빈 문자열, 공백만 입력되었을 때
- [ ] 설명은 50자까지 입력 가능하다.
- [ ] 설명은 한글로만 입력가능하다.
- [ ] 썸네일 주소는 올바른 URL 형식으로 이뤄져있어야 한다.

#### reservationTime (예약시간)
- [x] 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때
- [x] 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때
