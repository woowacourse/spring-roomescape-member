# 요구사항 문서

- [x] API 명세를 현재 프론트엔드 코드가 잘 동작할 수 있도록 수정
- [x] 예약 시간에 대한 제약 조건 추가
    - [x] 중복된 예약 시간 생성 요청 시 에러
    - [x] ISO 8601 표준에 따른 hh:mm 포맷에 해당하지 않는 요청 시 에러
    - [x] 예약이 있는 예약 시간을 삭제 요청 시 에러
- [x] 예약에 대한 제약 조건 추가
    - [x] 동일한 날짜와 시간, 테마에 예약 생성 요청 시 에러
    - [x] 존재하지 않는 시간에 예약 생성 요청 시 에러
    - [x] ISO 8601 표준에 따른 YYYY-MM-dd 포맷에 해당하지 않는 날짜가 포함된 예약 생성 요청 시 에러
    - [x] 지나간 날짜와 시간의 예약 요청 시 에러
    - [x] 이름이 비어있는 예약 요청 시 에러
    - [x] 존재하지 않는 테마 예약 생성 요청시 에러
    - [x] 테마 값이 비어있는 예약 요청 시 에러

- [x] 테마에 대한 제약 조건 추가
    - [x] 테마 이름, 설명, 썸네일 이미자가 비어 있을 경우 에러
    - [x] 중복된 이름의 테마 생성 요청시 에러
    - [x] 예약이 있는 테마를 삭제 요청시 에러

- [x] 사용자 예약 기능 추가
- [x] 인기 테마 기능 추가

# API 명세

## 예약 조회 API

### Request

> GET /reservations HTTP/1.1

### Response

> HTTP/1.1 200
>
> Content-Type: application/json

``` JSON 
[
    {
        "id": 1,
        "name": "브라운",
        "date": "2023-08-05",
        "time": {
            "id": 1,
            "startAt": "10:00"
        }
        "theme" : {
            "id": 1,
            "name": "이름",
            "description": "설명",
            "thumbnail": "썸네일"
        }
    }
]
```

## 예약 추가 API

### Request

> POST /reservations HTTP/1.1
>
> content-type: application/json

```JSON
{
  "date": "2023-08-05",
  "name": "브라운",
  "timeId": 1,
  "themeId": 1
}
```

### Response

> HTTP/1.1 201
>
> Content-Type: application/json
> Location: /reservations/{id}

```JSON
{
  "id": 1,
  "name": "브라운",
  "date": "2023-08-05",
  "time": {
    "id": 1,
    "startAt": "10:00"
  },
  "theme": {
    "id": 1,
    "name": "이름",
    "description": "설명",
    "thumbnail": "썸네일"
  }
}
```

## 예약 취소 API

### Request

> DELETE /reservations/1 HTTP/1.1

### Response

> HTTP/1.1 204

## 시간 추가 API

### request

> POST /times HTTP/1.1
> content-type: application/json

```JSON
{
  "startAt": "10:00"
}
```

### response

> HTTP/1.1 201
> Content-Type: application/json
> Location: /times/{id}

```JSON
{
  "id": 1,
  "startAt": "10:00"
}
```

## 시간 조회 API

### request

> GET /times HTTP/1.1

### response

> HTTP/1.1 200
> Content-Type: application/json

```JSON
[
  {
    "id": 1,
    "startAt": "10:00"
  }
]
```

## 시간 삭제 API

### request

> DELETE /times/1 HTTP/1.1

### response

> HTTP/1.1 204

## 테마 조회 API

### request

> GET /themes HTTP/1.1

### response

> HTTP/1.1 200
> Content-Type: application/json

```json
[
  {
    "id": 1,
    "name": "레벨2 탈출",
    "description": "우테코 레벨2를 탈출하는 내용입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
  }
]
```

## 테마 추가 API

### request

> POST /themes HTTP/1.1
> content-type: application/json

```json
{
  "name": "레벨2 탈출",
  "description": "우테코 레벨2를 탈출하는 내용입니다.",
  "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}
```

### response

> HTTP/1.1 201
> Location: /themes/1
> Content-Type: application/json

```json
{
  "id": 1,
  "name": "레벨2 탈출",
  "description": "우테코 레벨2를 탈출하는 내용입니다.",
  "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}
```

## 테마 삭제 API

### request

> DELETE /themes/1 HTTP/1.1

### response

> HTTP/1.1 204

## 예약 가능 시간 조회 API

### Request

> GET /times/book-able?date=${date}&themeId=${themeId}

### response

> HTTP/1.1 200
> Content-Type: application/json

```json
[
  {
    "id": 0,
    "startAt": "02:53",
    "isBooked": false
  }
]
```
