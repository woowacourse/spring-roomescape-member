# 방탈출 사용자 예약 미션

## 기능 요구 사항

- [x] 1단계 - 테마 도메인 추가
  - [x] 방탈출 게임의 `테마` 정보를 추가한다.
    - [x] 모든 테마의 시작 시간과 소요 시간은 동일하다고 가정한다.
    - [x] 테마는 이름, 설명, 썸네일 이미지 URL을 가진다.
  - [x] 예약에 테마 정보를 포함하도록 기존 코드를 변경한다.
  - [x] 관리자가 테마를 추가, 삭제할 수 있다.

- 2단계 - 사용자 예약
  - [x] 사용자가 날짜와 테마를 선택하면 예약 가능한 시간 목록이 표시된다.
    - [x] 예약 가능한 시간이란, 관리자가 등록한 시간 중 해당 날짜+테마에 아직 예약이 없는 시간이다.
  - [x] 사용자가 예약 가능한 시간을 선택하여 본인의 이름으로 예약한다.
  - [x] 같은 날짜·시간이라도 테마가 다르면 각각 예약 가능하다.

- 3단계 - 인기 테마 조회
  - [x] 최근 1주 동안 예약이 많았던 테마 상위 10개를 조회한다.
  - [x] 예: 오늘이 5월 8일이면, 게임 날짜가 5월 1일~5월 7일인 예약을 집계해 인기 순서대로 10개를 응답한다.

## API 명세서

`Resrevation`

| 기능          | Http/url                                                          | 요청본문                   | 응답                              |
|-------------|-------------------------------------------------------------------|------------------------|---------------------------------|
| 예약 생성       | `POST` `/reservations`                                            | `{name, date, timeId}` | `{id, name, date, time}`        |
| 예약 삭제       | `DELETE` `/reservations/{reservationId}`                          | -                      | -                               |
| 예약 조회       | `GET` `/reservations`                                             | -                      | `[{id, name, date, time}, ...]` |
`Theme`

| 기능 | Http/url | 요청본문 | 응답 |
|--------|------------------------------------|-----------------------------------------|-----------------------------------------------------|
| 관리자 테마 추가 | `POST` `/admin/themes` | `{name, description, thumbnailUrl}` | `{id, name, description, thumbnailUrl}` |
| 관리자 테마 삭제 | `DELETE` `/admin/themes/{themeId}` | - | - |
| 관리자 테마 조회  | `GET` `/admin/themes` | - | `[{id, name, description, thumbnailUrl}, ...]` |
| 인기 테마 조회    | `GET` `/theme/popular?period={period}&limit={limit}` | -       | `[{id, name, description, thumbnailUrl}, ...]`                                |

`ReservationTime`

| 기능 | Http/url | 요청본문 | 응답 |
|--------------|-----------------------------------------|-------------|-----------------------------------------------------|
| 관리자 테마 시간 생성 | `POST` `/admin/themes/{themeId}/times` | `{startAt}` | `{id, startAt, theme}` |
| 관리자 테마 시간 삭제 | `DELETE` `/admin/themes/times/{timeId}` | - | - |
| 관리자 테마 시간 조회 | `GET` `/admin/themes/{themeId}/times` | - | `[{id, startAt, theme}, ...]` |
| 예약 가능 시간 조회 | `GET` `/theme/{themeId}/available-times?date={yyyy-MM-dd}`    | -                      | `[{id, startAt, theme}, ...]`   |

## 테이블 설계

`Reservation`
```sql
CREATE TABLE reservation
(
  id      BIGINT       NOT NULL AUTO_INCREMENT,
  name    VARCHAR(255) NOT NULL,
  date    DATE         NOT NULL,
  time_id BIGINT       NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (time_id) REFERENCES reservation_time (id),
  UNIQUE (date, time_id)
);
```

`Reservation_time`
```sql
CREATE TABLE reservation_time
(
  id       BIGINT NOT NULL AUTO_INCREMENT,
  start_at TIME   NOT NULL,
  theme_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (theme_id) REFERENCES theme (id),
  UNIQUE (start_at, theme_id)
);
```

`themes`
```sql
CREATE TABLE theme
(
  id       BIGINT NOT NULL AUTO_INCREMENT,
  name    VARCHAR(255) NOT NULL,
  description     VARCHAR(10000), //설명은 null이여도 된다. h2가 아니라면 TEXT
  thumbnail_url     VARCHAR(2048), //이미지는 null이여도 된다. 단 디폴트 이미지를 써야한다.
    PRIMARY KEY (id),
  UNIQUE (name)
  );
```

## API 설계 룰

```angular2html
1. 리소스 식별 기준
리소스는 비즈니스 도메인에서 독립적으로 식별 가능한 핵심 개체를 기준으로 정의한다.
 일반적으로 다른 객체에 의존하지 않고, 자체적인 생명주기를 가지는 상위 개념을 리소스로 본다.
 
2. 관리자/사용자 API 분리 기준
관리자 API와 사용자 API는 분리하는 것이 일반적이다.
두 API는 동일한 도메인을 다루더라도 목적과 책임이 다르며, 요구하는 데이터의 범위와 응답 형태도 다르기 때문이다.
 따라서 권한과 사용 시나리오에 따라 인터페이스를 분리하여 책임을 명확히 하는 것이 바람직하다.
 
관리자 API와 사용자 API는 책임과 권한이 명확히 다르기 때문에 컨트롤러뿐 아니라 URL도 분리하는 것이 일반적이다.
/api/admin/**, /api/**와 같이 prefix를 분리하면 가독성이 높아지고 권한 제어도 명확해져 유지보수성과 보안성이 향상된다. 

 3. 우리 그룹의 "좋은 API" 정의 

* 핵심 리소스를 정확히 파악할 수 있다.
* 오해의 소지가 없고 일관되어야 한다.
* 사용자가 명확하다. (/api/admin/**, /api/** )

-------------------------------------------------------------------------------------------------- 

4. 서버 클라이언트 책임 기준
클라이언트는 사용자 요구사항/UX&UI적으로 필요한 정보만 책임을 가진다. 
서버는 클라이언트가 필요한 정보를 제공하는 것에 책임을 진다.

```
