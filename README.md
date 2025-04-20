# 방탈출 예약 관리

## 버전 관리
- JDK 21
- Gradle 8.13
- Spring Boot 3.4.4

##  화면 명세서
###  1. 홈 화면
- [x] `localhost:8080/admin` 요청 시 어드민 메인 페이지 응답

### 2. 예약 조회
- [x] `/admin/reservation` 요청 시 예약 관리 페이지 응답

## API 명세서
### 예약 조회 API
- [x] 예약 조회 API 구현
#### Request
```
GET /reservations HTTP/1.1
```

#### Response
```
HTTP/1.1 200 
Content-Type: application/json

[
    {
        "id": 1,
        "name": "브라운",
        "date": "2023-01-01",
        "time": "10:00"
    },
    {
        "id": 2,
        "name": "브라운",
        "date": "2023-01-02",
        "time": "11:00"
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
    "time": "15:40"
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
    "time": "15:40"
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