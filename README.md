# 기능 구현

## 1단계

- [x] 바뀐 API 스펙에 맞게 응답 코드를 수정한다.
- [ ] 시간 추가에 대한 예외를 처리한다.
    - [ ] 시작 시간에 유효하지 않은 값이 입력되었을 때
        - [ ] 빈 값 혹은 형식 다름
        - [ ] 중복된 시간 (해당 시간이 이미 존재)
- [x] 시간 삭제에 대한 예외를 처리한다.
    - [x] 특정 시간에 대한 예약이 존재
    - [x] 존재 하지 않는 시간
- [x] 예약 추가에 대한 예외를 처리한다.
    - [x] 예약자명, 날짜, 시간에 유효하지 않은 값이 입력 되었을 때
        - [x] 빈 값 혹은 형식 다름
        - [x] 중복된 예약 (해당 시간대에 이미 예약이 존재)
        - [ ] 지나간 날짜와 시간에 대한 예약
- [ ] 예약 삭제에 대한 예외를 처리한다.
    - [ ] 존재 하지 않는 예약

# API 명세

## 예약

### 예약 목록 조회 API

Request

```
GET /reservations HTTP/1.1
```

Response

```
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

### 예약 추가 API

Request

```
POST /reservations HTTP/1.1
content-type: application/json

{
    "date": "2023-08-05",
    "name": "브라운",
    "timeId": 1
}
```

Response

```
HTTP/1.1 201
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

### 예약 삭제 API

Request

```
DELETE /reservations/1 HTTP/1.1
```

Response

```
HTTP/1.1 204
```

## 시간

### 시간 목록 조회 API

Request

```
GET /times HTTP/1.1
```

Response

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

### 시간 추가 API

Request

```
POST /times HTTP/1.1
content-type: application/json

{
    "startAt": "10:00"
}
```

Response

```
HTTP/1.1 201
Content-Type: application/json

{
    "id": 1,
    "startAt": "10:00"
}
```

### 시간 삭제 API

Request

```
DELETE /times/1 HTTP/1.1
```

Response

```
HTTP/1.1 204
```
