# 방탈출 예약 관리
## 기능 목록
<details>
<summary>STEP1에서의 기능 목록</summary>
<div markdown="1">

- [x] `localhost:8080/admin` 요청 시 어드민 메인 페이지가 응답할 수 있게 한다.<br>
  - [x] 어드민 메인 페이지는 `templates/admin/index.html` 파일을 이용한다.<br>
- [x] 필요한 의존성을 찾아서 `build.gradle`에 추가한다.<br>
- [x] `/admin/reservation` 요청 시 예약 관리 페이지가 응답할 수 있게 한다.<br>
  - [x] 페이지는 `templates/admin/reservation-legacy.html` 파일을 이용한다.<br>
- [x] API 명세를 따라 예약 관리 페이지 로드 시 호출되는 예약 목록 조회 API도 구현한다.<br>
- [x] API 명세를 따라 예약 추가 API를 구현한다.<br>
- [x] API 명세를 따라 예약 삭제 API를 구현한다.<br>
  - Spring MVC가 제공하는 Annotation을 활용한다.<br>
- [x] 예약 정보의 식별자를 생성할 때 AtomicLong을 활용한다. <br>
- [x] h2 데이터베이스를 연동한다.<br>
- [x] 데이터베이스의 예약 스키마를 추가한다.<br>
- [x] 예약 조회를 구현한다.<br>
- [x] 예약 추가를 구현한다.<br>
- [x] 예약 삭제를 구현한다.<br>
- [x] 시간 추가를 구현한다.<br>
- [x] 시간 조회를 구현한다.<br>
- [x] 시간 삭제를 구현한다.<br>
- [x] 시간 관리 페이지를 구현한다.<br>
- [x] 예약 페이지 파일 수정한다.<br>
- [x] 외래키 지정을 통해 예약 테이블과 예약 시간 테이블의 관계를 설정한다.<br>
</div>
</details>

- [x] 바탕 코드는 spring-roomescape-admin 미션의 결과물을 사용한다.
- [x] 어드민 페이지에서 기능이 정상 동작하도록 API들을 수정한다.
- [x] 발생할 수 있는 예외 상황에 대한 처리를 한다.
  - [x] 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때
  - [x] 예약 생성 시 예약자명, 날짜, 시간에 유효하지 않은 값이 입력되었을 때
  - [x] 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려고 할 때
  - [x] 지나간 날짜와 시간에 대한 예약 생성을 하려고 할 때
  - [x] 중복 예약을 하려고 할 때
- [x] 테마 도메인을 추가한다.
  - [x] 해당 도메인에 맞게 예약 도메인 또한 수정한다.
  - [x] 어드민에서 방탈출 예약 시, 테마 정보를 포함할 수 있도록 신규 페이지 파일을 사용한다.
- [x]  `/admin/theme` 요청 시 테마 관리 페이지를 응답한다.
  - 페이지는 `templates/admin/theme.html` 파일을 이용한다.
- [x] 테마 조회를 구현한다.
- [x] 테마 추가를 구현한다.
- [x] 테마 삭제를 구현한다.
- [x] 테마 관련 예외를 구현한다.
- [x] 사용자가 예약 가능한 시간을 조회할 수 있다.
  - [x] 날짜와 테마를 선택하면 예약 가능한 시간을 확인할 수 있다.
  - [x] 예약 시 사용자 구분은 어드민과 동일하게 사용자의 이름으로 한다.
- [x] 사용자 예약 추가 기능을 구현한다. 
- [x] `/reservation` 요청 시 사용자 예약 페이지를 응답한다.
  - [x] 페이지는 `templates/reservation.html` 파일을 이용한다.
- [x] 인기 테마 조회 기능을 구현한다.
  - [x] 최근 일주일을 기준으로, 해당 기간 내에 방문하는 예약이 많은 테마 10개를 확인한다.
- [x] `/` 요청 시 인기 테마 페이지를 응답한다.
  - [x]  페이지는 t`emplates/index.html` 파일을 이용한다.




## API 명세

### 예약 목록 조회 API

- Request

```
GET /reservations HTTP/1.1

```

- Response

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

- Request

```
POST /reservations HTTP/1.1
content-type: application/json

{
    "date": "2023-08-05",
    "name": "브라운",
    "timeId": 1
}
```

- Response

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

- Request

```
DELETE /reservations/1 HTTP/1.1
```

- Response

```
HTTP/1.1 200
```

### 시간 추가 API

- Request

```
POST /times HTTP/1.1
content-type: application/json

{
    "startAt": "10:00"
}
```

- Response

```
HTTP/1.1 200
Content-Type: application/json

{
    "id": 1,
    "startAt": "10:00"
}
```

### 시간 조회 API

- Request

```
GET /times HTTP/1.1
```

- Response

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

### 시간 삭제 API

- Request

```
DELETE /times/1 HTTP/1.1
```

- Response

```
HTTP/1.1 200
```
