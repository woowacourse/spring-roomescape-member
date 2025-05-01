# 방탈출 예약 관리

## 1단계 - 예외 처리와 응답

- [x] 시간 관리, 예약 관리 API 구현
- [x] 예외 처리
    - [x] 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때 예외가 발생한다.
    - [x] 예약 생성 시 예약자명, 날짜, 시간에 유효하지 않은 값이 입력 되었을 때 예외가 발생한다.
    - [x] 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때 예외가 발생한다.
- [x] 비즈니스 로직 추가
    - [x] 지나간 날짜와 시간에 대한 예약 생성은 불가능하다.
    - [x] 중복 예약은 불가능하다.

## 2단계 - 테마 추가

- [x] 테마를 추가할 수 있다.
    - [ ] 예외 처리
        - [x] 테마 추가 시 빈 값이 들어오면 예외가 발생한다.
        - [x] 테마 추가 시 중복된 테마 이름이 들어오면 예외가 발생한다.
        - [ ] 테마 추가 시 설명에 20자 이하 100자 이상의 문자열이 들어오면 예외가 발생한다. - 나중
        - [ ] 테마 추가 시 썸네일에 이미지 형식이 들어오지 않으면 예외가 발생한다. - 나중
- [x] 테마를 조회할 수 있다.
- [x] 테마를 삭제할 수 있다.

## 3단계 - 사용자 기능

- [x] 사용자가 예약을 생성한다.
    - [x] 사용자는 날짜와 테마를 선택하면 예약 가능한 시간을 확인할 수 있습니다.
    - [x] 사용자는 예약 가능한 시간을 확인하고, 원하는 시간에 예약을 할 수 있습니다.
    - [x] /reservation 요청 시 사용자 예약 페이지를 응답합니다.
- [x] 인기 테마를 조회한다.
    - [x] 최근 일주일을 기준으로 하여 해당 기간 내에 방문하는 예약이 많은 테마 10개를 확인하려 합니다.
    - [x] 예를 들어 오늘이 4월 8일인 경우, 게임 날짜가 4월 1일부터 4월 7일까지인 예약 건수가 많은 순서대로 10개의 테마를 조회할 수 있어야 합니다.
    - [x] / 요청 시 인기 테마 페이지를 응답합니다

# 화면

- 어드민 메인 페이지: localhost:8080/admin
- 예약 관리 페이지: localhost:8080/admin/reservation
- 시간 관리 페이지: localhost:8080/admin/time
- 사용자 예약 페이지: localhost:8080/reservation
- 인기 테마 페이지: localhost:8080

# API 명세

### 예약 목록 조회

```
Request
GET /reservations HTTP/1.1

Response
HTTP/1.1 200 
Content-Type: application/json
[
    {
        "id": "Long",
        "name": String,
        "date": LocalDate (YYYY-MM-DD),
        "reservationTime": {
            "id": Long,
            "startAt" : LocalTime (HH:mm)
        },
        "themeName": String
    }
]
```

### 예약 추가

```
Request
Content-Type: application/json
POST /reservations
{
    "name": String,
    "date": LocalDate (YYYY-MM-DD),
    "timeId": Long,
    "themeId": Long
}

Response
Content-Type: application/json
HTTP/1.1 200 
{
    "id": Long,
    "name": String,
    "date": LocalDate (YYYY-MM-DD),
    "reservationTime": {
        "id": Long,
        "startAt" : LocalTime (HH:mm)
    },
    "themeName" : String
}

```

### 예약 취소

```
Request
DELETE /reservations/1 HTTP/1.1

Response
HTTP/1.1 200
```

### 시간 추가

```
Request
POST /times HTTP/1.1
content-type: application/json

{
    "startAt": LocalTime (HH:mm)
}

Response
HTTP/1.1 200
Content-Type: application/json

{
    "id": Long,
    "startAt": LocalTime (HH:mm)
}
```

### 시간 조회

```
Request
GET /times HTTP/1.1

Response
HTTP/1.1 200 
Content-Type: application/json

[
   {
        "id": Long,
        "startAt": LocalTime (HH:mm)
    }
]
```

### 시간 삭제

```
Request
DELETE /times/1 HTTP/1.1

Response
HTTP/1.1 200
```

### 테마 목록 조회

```
Request
GET /themes HTTP/1.1

Response
HTTP/1.1 200 
Content-Type: application/json

[
   {
        "id": 1,
        "name": "레벨2 탈출",
        "description": "우테코 레벨2를 탈출하는 내용입니다.",
        "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
    }
]
```

### 테마 추가

```
Request
POST /themes HTTP/1.1
content-type: application/json

{
    "name": "레벨2 탈출",
    "description": "우테코 레벨2를 탈출하는 내용입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}

Response
HTTP/1.1 201
Location: /themes/1
Content-Type: application/json

{
    "id": 1,
    "name": "레벨2 탈출",
    "description": "우테코 레벨2를 탈출하는 내용입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}
```

### 테마 삭제

```
Request
DELETE /themes/1 HTTP/1.1

Response
HTTP/1.1 204
```

### 예약 가능한 시간 조회

```
Request
GET /times/available?date={}&themeId={} HTTP/1.1

Response
HTTP/1.1 200 
Content-Type: application/json

[
   {
        "id": Long,
        "startAt": LocalTime (HH:mm)
        "alreadyBooked": boolean
    }
]
```

### 인기 테마 조회

```
Request
GET /theme/top10

Response
HTTP/1.1 200 
Content-Type: application/json

[
   {
        "id": Number
        "name": String
        "description": String
        "thumbnail": String
    }
]
```