# 방탈출 예약 관리

## 홈 화면

### 요청

- 메서드 : GET
- 요청 URL : /admin
- 설명 : 어드민 페이지를 응답한다.

`GET /admin HTTP/1.1`

## 예약 목록 조회 페이지

- 페이지 URL : /admin/reservation

### 요청

- 메서드 : GET
- 요청 URL : /reservations
- 설명 : 예약 목록을 조회한다.

`GET /reservations HTTP/1.1`

### 응답

```json
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

## 예약 추가

### 요청

- 메서드 : POST
- 요청 URL : /reservations
- 설명 : 예약을 추가한다.
- 조건
    - 이름
        - 이름은 null이 될 수 없다.
        - 이름은 10글자를 넘을 수 없다.
    - dateTime (날짜 & 시간)
        - 날짜와 시간은 null이 될 수 없다.
        - 과거 날짜&시간으로 예약할 수 없다.

```json
POST /reservations HTTP/1.1
content-type: application/json

{
"date": "2023-08-05",
"name": "브라운",
"timeId": 1
}
```

### 응답

```json
HTTP/1.1 201
Content-Type: application/json

{
"id": 1,
"name": "브라운",
"date": "2023-08-05",
"time": {
"id": 1,
"startAt": "10:00"
}
}
```

## 예약 취소

### 요청

- 메서드 : DELETE
- 요청 URL : /reservations
- 설명 : 예약을 삭제한다.

`DELETE /reservations/1 HTTP/1.1`

### 응답

```json
HTTP/1.1 204
Content-Type: application/json
```

## 시간 추가

### 요청

- 메서드 : POST
- 요청 URL : /times
- 설명 : 시간을 추가한다.

```json
POST /times HTTP/1.1
content-type: application/json

{
"startAt": "10:00"
}
```

### 응답

```json
HTTP/1.1 201
Content-Type: application/json

{
"id": 1,
"startAt": "10:00"
}
```

## 시간 조회

### 요청

- 메서드 : GET
- 요청 URL : /times
- 설명 : 시간을 조회한다.

```json
GET /times HTTP/1.1
```

### 응답

```json
HTTP/1.1 200
Content-Type: application/json

[
{
"id": 1,
"startAt": "10:00"
}
]
```

## 시간 삭제

### 요청

- 메서드 : DELETE
- 요청 URL : /times/{id}
- 설명 : 시간을 삭제한다.

```json
DELETE /times/1 HTTP/1.1
```

### 응답

```json
HTTP/1.1 204
```

## 예약 가능 시간 조회

### 요청

- 메서드 : GET
- 요청 URL : /times/available
- 설명 : 가능한 시간을 조회한다.

```json
GET /times/available?date=2020-05-01&themeId=1 HTTP/1.1
```

### 응답

```json
HTTP/1.1 200
Content-Type: application/json

[
{
"timeId": 1,
"startAt": "10:00",
"alreadyBooked": "true"
}
{
"timeId": 2,
"startAt": "11:00",
"alreadyBooked": "false"
}
]
```

## 테마 조회

### 요청

- 메서드 : GET
- 요청 URL : /themes
- 설명 : 가능한 테마들을 조회한다.

```json
GET /themes HTTP/1.1
```

### 응답

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

## 테마 조회

### 요청

- 메서드 : GET
- 요청 URL : /themes/popular
- 설명 : 지난 일주일간(오늘 날짜 -7 ~ 오늘 날짜-1) 예약 건수가 많은 순서대로 10개의 테마를 조회한다.

```json
GET /themes/popular HTTP/1.1
```

### 응답

```json
HTTP/1.1 200
Content-Type: application/json

[
{
"id": 1,
"name": "추리",
"description": "셜록 with Danny",
"thumbnail": "image/thumbnail.png"
},
{
"id": 2,
"name": "공포",
"description": "어둠 속의 비명",
"thumbnail": "image/thumbnail.png"
},
{
"id": 3,
"name": "모험",
"description": "잃어버린 도시",
"thumbnail": "image/thumbnail.png"
},
{
"id": 4,
"name": "SF",
"description": "우주 탈출 미션",
"thumbnail": "image/thumbnail.png"
},
{
"id": 5,
"name": "감성",
"description": "시간을 걷는 집",
"thumbnail": "image/thumbnail.png"
},
{
"id": 12,
"name": "논리",
"description": "퍼즐 마스터",
"thumbnail": "image/thumbnail.png"
},
{
"id": 11,
"name": "로맨스",
"description": "잃어버린 편지",
"thumbnail": "image/thumbnail.png"
},
{
"id": 8,
"name": "범죄",
"description": "은행 강도 사건",
"thumbnail": "image/thumbnail.png"
},
{
"id": 9,
"name": "스릴러",
"description": "잠입 작전",
"thumbnail": "image/thumbnail.png"
},
{
"id": 7,
"name": "역사",
"description": "고려 왕실의 비밀",
"thumbnail": "image/thumbnail.png"
}
]
```

## 테마 추가

### 요청

- 메서드 : POST 
- 요청 URL : /themes
- 설명 : 테마를 추가한다.

```json
POST /themes HTTP/1.1
content-type: application/json

{
"name": "레벨2 탈출",
"description": "우테코 레벨2를 탈출하는 내용입니다.",
"thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}
```

### 응답

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

## 테마 삭제

### 요청

- 메서드 : DELETE
- 요청 URL : /themes/{id}
- 설명 : 테마를 삭제한다.

```json
DELETE /themes/1 HTTP/1.1
```

### 응답

```json
HTTP/1.1 204
```

## 예외

### 시간

- [x] 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때
    - HH:mm 형식이 아닌 경우
- [x] 예약 생성 시 예약자명, 날짜, 시간에 유효하지 않은 값이 입력 되었을 때
    - 예약자명 : 10글자 이내
    - 날짜 : yyyy-MM-dd 형식
- [x] 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때
- [x] 지나간 날짜와 시간에 대한 예약 생성은 불가능하다.
- [x] 중복 예약은 불가능하다.
    - ex. 이미 4월 1일 10시에 예약이 되어있다면, 4월 1일 10시에 대한 예약을 생성할 수 없다.
