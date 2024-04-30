# 요구사항 문서

- [x] localhost:8080/admin 요청 시 어드민 메인 페이지가 응답할 수 있도록 구현한다.
- [x] 어드민 메인 페이지는 templates/admin/index.html 파일을 이용한다.
- [x] localhost:8080/admin/reservation 요청 시 아래 화면과 같이 예약 관리 페이지가 응답할 수 있도록 구현한다.
- [x] 페이지는 templates/admin/reservation-legacy.html 파일을 이용한다.
- [x] 예약 조회 API 명세를 따라 예약 관리 페이지 로드 시 호출되는 예약 목록 조회 API를 구현한다.
- [x] API 명세를 따라 예약 추가 API 와 삭제 API를 구현한다.
- [x] 예약 추가와 취소가 잘 동작한다.
- [x] 이상의 요구 사항을 데이터베이스와 연동하도록 한다.

- [x] 방탈출 시간표에 따라 방탈출 예약 시 시간을 선택하는 방식으로 수정한다.
- [x] API 명세를 따라 시간 관리 API를 구현한다.
- [x] 페이지는 templates/admin/time.html 파일을 이용한다.

- [x] 기존에 구현한 예약 기능에서 시간을 시간 테이블에 저장된 값만 선택할 수 있도록 수정한다.
- [x] templates/admin/reservation.html 을 사용한다.

- [x] 레이어드 아키텍처를 적용하여 레이어별 책임과 역할에 따라 클래스를 분리한다.
- [x] 분리한 클래스는 매번 새로 생성하지 않고 스프링 빈으로 등록한다.

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
