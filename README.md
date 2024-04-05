# 요구사항 문서

- [x] localhost:8080/admin 요청 시 어드민 메인 페이지가 응답할 수 있도록 구현한다.
- [x] 어드민 메인 페이지는 templates/admin/index.html 파일을 이용한다.
- [x] localhost:8080/admin/reservation 요청 시 아래 화면과 같이 예약 관리 페이지가 응답할 수 있도록 구현한다.
- [x] 페이지는 templates/admin/reservation-legacy.html 파일을 이용한다.
- [x] 예약 조회 API 명세를 따라 예약 관리 페이지 로드 시 호출되는 예약 목록 조회 API를 구현한다.
- [x] API 명세를 따라 예약 추가 API 와 삭제 API를 구현한다.
- [x] 예약 추가와 취소가 잘 동작한다.
- [x] 시간 추가, 삭제, 조회 API를 구현한다.

### 데이터베이스

- [x] h2 데이터베이스에 데이터를 저장하기 위해 연동한다.
- [x] 예약 시간 데이터베이스를 구축한다.

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

## 예약 추가 API

### Request

> POST /reservations HTTP/1.1
>
> content-type: application/json

```JSON
{
  "date": "2023-08-05",
  "name": "브라운",
  "time": "15:40"
}
```

### Response

> HTTP/1.1 201
>
> Content-Type: application/json

```JSON
{
  "id": 1,
  "name": "브라운",
  "date": "2023-08-05",
  "time": "15:40"
}
```

## 예약 취소 API

### Request

> DELETE /reservations/1 HTTP/1.1

### Response

> HTTP/1.1 204

<br>

## 시간 추가 API

### Request

> POST /times HTTP/1.1
>
>content-type: application/json

```java
{
        "startAt":"10:00"
        }
```

### Response

> HTTP/1.1 201
>
> Content-Type: application/json

```java
{
        "id":1,
        "startAt":"10:00"
        }
```

## 시간 조회 API

### Request

> GET /times HTTP/1.1

### Response

> HTTP/1.1 200
>
> Content-Type: application/json

```java
[
        {
        "id":1,
        "startAt":"10:00"
        }
        ]
```

## 시간 삭제 API

### Request

> DELETE /times/1 HTTP/1.1

### Response

> HTTP/1.1 204

## 데이터베이스

### 예약

```mysql
CREATE TABLE reservation
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    date VARCHAR(255) NOT NULL,
    time VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);
```

### 예약 시간

```mysql
CREATE TABLE reservation_time
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    start_at VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);
```
