# 요구사항 문서

- [x] API 명세를 현재 프론트엔드 코드가 잘 동작할 수 있도록 수정
- [ ] 예약 시간에 대한 제약 조건 추가
    - [x] 중복된 예약 시간 생성 요청 시 에러
    - [x] ISO 8601 표준에 따른 hh:mm 포맷에 해당하지 않는 요청 시 에러
    - [x] 예약이 있는 예약 시간을 삭제 요청 시 에러
    - [ ] 존재하지 않는 시간을 삭제 요청 시 에러
- [ ] 예약에 대한 제약 조건 추가
    - [ ] 동일한 날짜와 시간에 예약 생성 요청 시 에러
    - [x] 존재하지 않는 시간에 예약 생성 요청 시 에러
    - [ ] ISO 8601 표준에 따른 YYYY-MM-dd 포맷에 해당하지 않는 날짜가 포함된 예약 생성 요청 시 에러
    - [ ] 존재하지 않는 예약을 삭제 요청 시 에러
    - [ ] 지나간 날짜와 시간의 예약 요청 시 에러

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
  "timeId": 1
}
```

### Response

> HTTP/1.1 200
>
> Content-Type: application/json

```JSON
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

## 예약 취소 API

### Request

> DELETE /reservations/1 HTTP/1.1

### Response

> HTTP/1.1 200

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

> HTTP/1.1 200
> Content-Type: application/json

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

> HTTP/1.1 200
