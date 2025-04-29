# 방탈출 예약 관리

## 버전 관리
- JDK 21
- Gradle 8.13
- Spring Boot 3.4.4

## 요구사항 명세서
- [ ] 예약 시간 검증
    - [x] 시작 시간은 LocalTime 형식을 만족시켜야 한다.
    - [x] 예약이 이미 존재하는 시간은 삭제할 수 없다.
    - [ ] 중복된 시간 추가는 불가능하다.

- [ ] 예약 검증
    - [ ] 예약자명이 존재하지 않거나, 255자를 초과할 수 없다. 
    - [ ] 날짜는 LocalDate 형식을 만족시켜야 한다.
    - [ ] 지나간 날짜와 시간에 대한 예약 생성은 불가능하다.
    - [ ] 중복된 일시의 예약은 불가능하다. 
    - [x] 존재하지 않는 예약 시간 ID를 이용하여 예약할 수 없다.

##  화면 명세서
###  1. 홈 화면
- [x] `localhost:8080/admin` 요청 시 어드민 메인 페이지 응답

### 2. 예약 조회
- [x] `/admin/reservation` 요청 시 예약 관리 페이지 응답

## API 명세서
## 예약 API
### 예약 조회 API
- [x] 예약 조회 API 구현
#### Request
```
GET /reservations HTTP/1.1
```

#### Response
```
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
- [x] 예약 추가 API 구현
#### Request
```
POST /reservations HTTP/1.1
content-type: application/json

{
    "date": "2023-08-05",
    "name": "브라운",
    "timeId": 1
}
```

#### Response
```
HTTP/1.1 200
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

### 예약 취소 API
- [x] 예약 취소 API 구현
#### Request
```
DELETE /reservations/1 HTTP/1.1
```
#### Response
```
HTTP/1.1 200
```

## 예약 시간 API
### 예약 시간 추가 API
- [x] 예약 시간 추가 API 구현
#### Request
```
POST /times HTTP/1.1
content-type: application/json

{
    "startAt": "10:00"
}

```

#### Response
```
HTTP/1.1 200
Content-Type: application/json

{
    "id": 1,
    "startAt": "10:00"
}
```

### 예약 시간 조회 API
- [x] 예약 시간 조회 API 구현
#### Request
```
GET /times HTTP/1.1
```

#### Response
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

### 예약 시간 삭제 API
- [x] 예약 시간 삭제 API 구현
#### Request
```
DELETE /times/1 HTTP/1.1
```

#### Response
```
HTTP/1.1 200
```