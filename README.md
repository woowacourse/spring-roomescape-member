# 방탈출 예약 미션

## 접속 방법

1. 어플리케이션 실행
2. http://localhost:8080/ 로 접속

## 🗒️ 용어 정의 및 모델링

### 예약

| 용어          | 설명                                |
|-------------|-----------------------------------|
| reservation | 방탈출 예약 정보. 예약자 이름과 방문 날짜, 시각을 포함. |
| name        | 예약자 이름. (10자 이내)                  |
| date        | 예약 날짜.                            |
| time        | 예약 시각. 시간은 슬롯으로 관리된다.             |
| theme       | 방탈출 테마. 시작시간과 소요시간이 동일하다.         |

### 예약 시간

| 용어               | 설명                       |
|------------------|--------------------------|
| reservation time | 예약 시간                    |
| start at         | 시간을 슬롯으로 관리할 때 슬롯의 시작 시간 |

### 테마

| 용어                  | 설명      |
|---------------------|---------|
| theme               | 방탈출 테마. |
| theme name          | 테마 이름   |
| theme description   | 테마 설명   |
| theme thumbnail url | 테마 썸네일  |
| theme runtime       | 테마 소요시간 |

# 💻 기능 요구 사항 (member)

## 테마 + 사용자 예약 단계 - 지난 미션까지는 관리자가 전화·현장 예약을 받아 시스템에 직접 등록했다. 이번 사이클부터는 사용자가 브라우저에서 직접 예약하는 서비스로 발전시킨다. 별도의 로그인은 없으며, 사용자는 이름으로 식별된다.

### 단계 1 - 테마 도메인 추가

- [x] 방탈출 게임에 theme 정보를 추가한다.
    - [x] theme는 theme name, theme description, theme thumbnail url을 가진다.
    - [x] 모든 theme의 start at과 runtime은 동일하다고 가정한다.
- [x] reservation에 theme 정보를 포함하도록 기존 코드를 변경한다.
    - [x] 기존 테스트코드를 변경된 api에 맞게 수정한다.
- [x] admin이 theme를 추가·삭제할 수 있다.

### 단계 2 - 사용자 예약

- [x] 사용자가 date와 theme를 선택하면 예약 가능한 시간 목록이 표시된다.
    - [x] 예약 가능한 시간이란, admin이 등록한 reservation time 중 해당 date + theme에 아직 reservation이 없는 time이다.
- [x] 사용자가 예약 가능한 시간을 선택하여 본인의 name으로 예약한다.
    - [x] 예약 가능한 시간이 아니라면 에러를 반환한다.
- [x] 같은 date·time이라도 theme가 다르면 각각 예약 가능하다.

### 단계 3 - 인기 테마 조회

- [x] 최근 1주 동안 reservation이 많았던 theme 상위 10개를 조회한다.
    - [x] 오늘 5월 8일이면, date가 5월 1일~5월 7일인 reservation을 집계해 인기 순서대로 10개를 응답한다.

# 💻 기능 요구 사항 (admin)

## 방탈출 예약 관리 - 방탈출 카페 관리자가 전화·현장 예약을 직접 등록·관리하는 상황에 필요한 예약 관리 서비스를 만든다.

### 단계 1 - 웹 요청,응답

#### 방탈출 카페 관리자가 전화·현장 예약을 직접 등록·관리하는 상황에 필요한 예약 관리 API를 만든다.

- [x] 별도의 데이터베이스 없이 **메모리(List + AtomicLong)**로 예약 상태를 관리한다.
- [x] 서버를 재시작하면 데이터는 모두 사라진다.

- [x] 브라우저 화면은 만들지 않는다.
- [x] API 동작 확인 방법(테스트, HTTP 클라이언트 등)은 스스로 찾는다.

- [x] 예약 CRUD API를 구현한다.

| 기능    | 메서드 / URL                   | 요청 본문                | 응답                              |
|-------|-----------------------------|----------------------|---------------------------------|
| 예약 조회 | `GET /reservations`         |                      | `[{id, name, date, time}, ...]` |
| 예약 추가 | `POST /reservations`        | `{name, date, time}` | `{id, name, date, time}`        |
| 예약 삭제 | `DELETE /reservations/{id}` |                      | `200 OK`                        |

<details>

<summary>예약 추가 요청·응답 예시</summary>

```
POST /reservations HTTP/1.1
Content-Type: application/json

{
"name": "브라운",
"date": "2023-08-05",
"time": "15:40"
}
```

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

</details>

### 단계2 - 데이터베이스 연동

#### 1단계 메모리 저장은 서버 재시작 시 예약 데이터가 모두 사라지는 상황이다. 이를 해결하기 위해 예약 CRUD를 H2 데이터베이스로 전환한다.

- [x] 레벨1에서 학습했던 JUnit만 활용한 단위 테스트에 집중한다.
    - [x] 새로운 테스트 도구나 기법(Spring Boot Test, Mock, RestAssured 추가 활용 등)을 도입하지 않는다.
    - [x] 요구사항에서 RestAssured가 주어진 경우 그대로 사용하되, 그 위에 새 테스트 기법을 쌓지 않는다.

- [x] 1단계에서 만든 조회·추가·삭제 API를 모두 JdbcTemplate기반으로 전환한다
    - [x] 기존의 List<Reservation>, AtomicLong은 제거한다
    - [x] 예약 추가 시 DB가 생성한 id를 응답에 담는다

### 단계3 - 시간 관리

#### 관리자가 매번 예약 시간을 텍스트로 직접 입력해 번거롭고 실수가 나는 상황이다. 정해진 시간 슬롯을 관리자가 선택해서 쓸 수 있도록 시간 관리 기능을 추가하고 예약과 시간을 연결한다.

- [x] 레벨1에서 학습했던 JUnit만 활용한 단위 테스트에 집중한다.
    - [x] 새로운 테스트 도구나 기법(Spring Boot Test, Mock, RestAssured 추가 활용 등)을 도입하지 않는다.
    - [x] 요구사항에서 RestAssured가 주어진 경우 그대로 사용하되, 그 위에 새 테스트 기법을 쌓지 않는다.

- [x] 시간 관리 API

| 기능    | 메서드 / URL            | 요청 본문       | 응답                     |
|-------|----------------------|-------------|------------------------|
| 시간 추가 | `POST /times`        | `{startAt}` | `{id, startAt}`        |
| 시간 조회 | `GET /times`         |             | `[{id, startAt}, ...]` |
| 시간 삭제 | `DELETE /times/{id}` |             | `200 OK`               |

- [x] Reservation 클래스의 time 필드를 String → ReservationTime 객체로 변경
    - [x] 예약 추가 요청 본문: time → timeId
    - [x] 예약 조회 응답: time을 객체로 ({id, startAt})

<details>

<summary>예약 추가 요청·응답 예시</summary>

```
POST /reservations HTTP/1.1
Content-Type: application/json

{
    "date": "2023-08-05",
    "name": "브라운",
    "timeId": 1
}
```

```
HTTP/1.1 200
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

</details>

### 단계4 - 계층 분리

#### ReservationController에 웹 요청 처리·비즈니스 로직·DB 접근이 모두 몰려 응집도는 낮고 결합도는 높은 상황이다. 레이어드 아키텍처로 레이어별 책임에 따라 코드를 분리한다.

- [x] 레이어별 책임과 역할에 따라 클래스를 분리하고, 분리한 클래스를 Spring Bean으로 등록한다.
    - [x] ReservationController에 JdbcTemplate 필드가 남아있지 않아야 한다

# 📝API 명세

| 기능           | 메서드 / URL                       | 요청 본문                                | 응답 본문                                                                                                 |
|--------------|---------------------------------|--------------------------------------|-------------------------------------------------------------------------------------------------------|
| (어드민)        |                                 |
| 시간 추가        | `POST /admin/times`             | `{startAt}`                          | `{id, startAt}`                                                                                       |
| 시간 삭제        | `DELETE /admin/times/{id}`      |                                      |                                                                                                       |
| 테마 추가        | `POST /admin/themes`            | `{name, description, thumbnail_url}` | `{theme_id, name, description, thumbnail_url, rumtime}`                                               |
| 테마 삭제        | `DELETE /admin/themes/{id}`     |                                      |                                                                                                       |
| (유저)         |                                 |                                      |
| 예약 추가        | `POST /reservations`            | `{name, date, time_id, theme_id}`    | `{id, name, date, {time_id, start_at}, {theme_id, name, description, thumbnail_url, rumtime}}`        |
| 예약 삭제        | `DELETE /reservations/{id}`     |                                      |                                                                                                       |
| 예약 조회        | `GET /reservations`             |                                      | `[{id, name, date, {time_id, start_at}, {theme_id, name, description, thumbnail_url, rumtime}}, ...]` |
| 시간 조회        | `GET /times`                    |                                      | `[{id, startAt}, ...]`                                                                                |
| 예약 가능한 시간 조회 | `GET /times?date={}&themeId={}` |                                      | `[{id, startAt}, ...]`                                                                                |
| 테마 조회        | `GET /themes`                   |                                      | `[{theme_id, name, description, thumbnail_url, rumtime}, ...]`                                        |
| 인기 있는 테마 조회  | `GET /themes?days={}&limits={}` |                                      | `[{theme_id, name, description, thumbnail_url, rumtime}, ...]`                                        |

# 응답 코드

| 응답 코드                       | 상황                 |
|-----------------------------|--------------------|
| `200 Ok`                    | 정상적으로 조회됨          |
| `201 Created`               | 정상적으로 생성됨          |
| `204 No Content`            | 반환값이 없음            |
| `400 Bad Request`           | 클라이언트 요청값이 올바르지 않음 |
| `404 Not Found`             | 없는 자원에 대한 접근       |
| `409 Conflict`              | 서버의 현재 상태와 충돌      |
| `500 Internal Server Error` | 서버 내부 오류           |
