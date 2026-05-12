# 방탈출 사용자 예약 미션

## API 명세서

`Resrevation`


| 기능 | Http/url | 헤더 | 요청본문 | 응답 |
|---|---|---|---|---|
| 사용자 예약 생성 | `POST` `/reservations` | - | `{name, date, timeId}` | `{id, name, date, time}` |
| 사용자 이름별 예약 조회 | `GET` `/reservations?name={name}` | - | - | `[{id, name, date, time}, ...]` |
| 사용자 예약 변경 | `PATCH` `/reservations/{id}` | `Authorization: {name}` | `{date, timeId}` | - |
| 사용자 예약 삭제 | `DELETE` `/reservations/{id}` | `Authorization: {name}` | - | - |
| 관리자 예약 조회 | `GET` `/admin/reservations` | - | - | `[{id, name, date, time}, ...]` |
| 관리자 예약 삭제 | `DELETE` `/admin/reservations/{id}` | - | - | - |


`ReservationTime`

| 기능 | Http/url | 헤더 | 요청본문 | 응답 |
|---|---|---|---|---|
| 관리자 테마 시간 조회 | `GET` `/admin/themes/{themeId}/times` | - | - | `[{id, startAt, theme}, ...]` |
| 관리자 테마 시간 생성 | `POST` `/admin/themes/{themeId}/times` | - | `{startAt}` | `{id, startAt, theme}` |
| 관리자 테마 시간 삭제 | `DELETE` `/admin/themes/times/{timeId}` | - | - | - |
| 예약 가능 시간 조회 | `GET` `/themes/{themeId}/available-times?date={yyyy-MM-dd}` | - | - | `[{id, startAt, theme}, ...]` |


`Theme`

| 기능 | Http/url | 헤더 | 요청본문 | 응답 |
|---|---|---|---|---|
| 관리자 테마 조회 | `GET` `/admin/themes` | - | - | `[{id, name, description, thumbnailUrl}, ...]` |
| 관리자 테마 추가 | `POST` `/admin/themes` | - | `{name, description, thumbnailUrl}` | `{id, name, description, thumbnailUrl}` |
| 관리자 테마 삭제 | `DELETE` `/admin/themes/{id}` | - | - | - |
| 인기 테마 조회 | `GET` `/themes/popular?period={period}&limit={limit}` | - | - | `[{id, name, description, thumbnailUrl}, ...]` |

## 예외 명세서

### 예외 응답 형식

| 상황 | HTTP Status | 응답본문 |
|---|---|---|
| 단일 오류 | 각 예외별 상태 코드 | `{message, validationErrors: null}` |
| 검증 오류 | `400 Bad Request` | `{message: "잘못된 요청입니다", validationErrors: ["오류 메시지", ...]}` |

### 공통 예외

| 상황 | HTTP Status | message | validationErrors |
|---|---|---|---|
| 잘못된 경로 | `404 Not Found` | `잘못된 경로입니다.` | `null` |
| 요청 파라미터 타입 불일치 | `400 Bad Request` | `잘못된 요청입니다` | `null` |
| 데이터 무결성 위반 | `400 Bad Request` | `잘못된 요청입니다` | `null` |
| 예상하지 못한 서버 오류 | `500 Internal Server Error` | `서버 내부 오류가 발생했습니다.` | `null` |
| 인증 헤더 누락 또는 공백 | `400 Bad Request` | `잘못된 요청입니다` | `["인증에 실패했습니다."]` |

### Reservation 예외

| 상황 | HTTP Status       | message | validationErrors |
|---|-------------------|---|---|
| 예약자 이름 누락 또는 공백 | `400 Bad Request` | `잘못된 요청입니다` | `["예약자 이름은 비어있을 수 없습니다."]` |
| 예약자 이름 길이 초과 | `400 Bad Request` | `잘못된 요청입니다` | `["예약자 이름은 최대 10자까지 입력할 수 있습니다."]` |
| 예약 날짜 누락 | `400 Bad Request` | `잘못된 요청입니다` | `["예약 날짜는 비어있을 수 없습니다."]` |
| 예약 시간 정보 누락 | `400 Bad Request` | `잘못된 요청입니다` | `["예약 시간 정보가 없습니다."]` |
| 과거 날짜/시간 예약 생성 또는 변경 | `400 Bad Request` | `예약 날짜는 미래여야 합니다.` | `null` |
| 예약자 이름 불일치 | `400 Bad Request` | `예약자 이름이 일치하지 않습니다.` | `null` |
| 예약 없음 | `404 Not Found` | `찾는 예약이 없습니다.` | `null` |
| 예약 중복 | `409 Conflict`    | `예약은 중복 생성이 불가능합니다.` | `null` |

### Theme 예외

| 상황             | HTTP Status       | message                    | validationErrors |
|----------------|-------------------|----------------------------|---|
| 테마 이름 누락 또는 공백 | `400 Bad Request` | `잘못된 요청입니다`                | `["테마의 이름은 비어있울 수 없습니다."]` |
| 테마 중복          | `409 Conflict`    | `테마는 중복 생성이 불가능합니다.`       | `null` |
| 테마 없음          | `404 Not Found`   | `찾는 테마가 없습니다.`             | `null` |
| 예약이 존재하는 테마 삭제 | `409 Conflict` | `예약이 존재하는 테마는 삭제할 수 없습니다.` | `null` |

### ReservationTime 예외

| 상황 | HTTP Status | message | validationErrors |
|---|---|---|---|
| 예약 시간 누락 | `400 Bad Request` | `잘못된 요청입니다` | `["찾는 예약 시간이 없습니다."]` |
| 예약 시간 없음 | `404 Not Found` | `찾는 예약 시간이 없습니다.` | `null` |
| 예약 시간 중복 | `409 Conflict` | `예약 시간은 중복 생성이 불가능합니다.` | `null` |
| 예약이 존재하는 시간 삭제 | `409 Conflict` | `예약이 존재하는 시간은 삭제할 수 없습니다.` | `null` |

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

* 클라이언트 관점에서 필요한 핵심 데이터를 기준으로 리소스를 식별한다.


2. 우리 그룹의 "좋은 API" 정의 

* 클라이언트 관점에서 이해하기 쉬워야 하며 예측이 가능해야한다.
* 일관성이 있고 서버에 필요한 정보를 제공해야한다. 
* 사용자가 명확하다. (/api/admin/**, /api/** )

------------------------------------------------------------------------------------------------------------

1. 에러 응답의 수신자를 구분한다

* message는 사용자(또는 API 소비자) 를 위한 정보로 제공한다. 
* code, http status는 프론트엔드/클라이언트가 에러를 식별하고 분기 처리하기 위한 정보로 사용한다. 



2. 비즈니스 규칙 위반 시의 응답 기준

*  비즈니스 예외 응답 방식은 팀 내 합의된 기준을 우선한다. 
*  프론트엔드와 협의하여 UX/UI 관점에서 필요한 정보 수준을 결정한다. 
*  사용자가 수행할 수 없는 행동이라면 그 이유를 명확히 전달한다. 
*  예: 이미 지난 예약은 취소할 수 없으므로 → 취소 불가 응답을 반환한다. 



3. 응답 본문에 "다음 행동"을 담는 기준

아래 조건을 모두 고려했을 때 필요하면 포함한다.

*  사용자의 요청/입력으로 인해 발생한 문제인가? 
*  사용자가 다음에 무엇을 해야 할지 응답만으로 알기 어려운가? 
*  제공하는 정보가 실제로 사용자의 다음 행동 결정에 도움이 되는가? 


```
