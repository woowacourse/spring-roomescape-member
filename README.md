# 방 탈출 예약 시스템

## 기능 목록

### 테마

- [x] 관리자가 테마를 추가/삭제할 수 있다.
- [x] 테마 내역은 이름, 설명, 썸네일 이미지 URL을 갖는다.
- [x] 예약 내역에는 테마 정보가 포함된다.

        공포:   11:00 12:00 13:00 … 17:00
        코미디: 11:00 12:00 13:00 … 17:00
        로맨스: 11:00 12:00            -> X 모두 같은 시간을 공유해야 함

### 사용자 예약

- [x] 사용자가 날짜와 테마를 선택하면 예약 가능한 시간 목록이 표시된다.
    - [x] 예약 가능한 시간이란 이전 예약 리스트에 동일한 날짜 + 테마의 예약이 없는 시간이다.
    - [x] 테마 예약이 더 이상 불가능해도 선택할 수 있다.
- [x] 예약할 때 이름을 입력받아야 하고, 예약 기록에 이름을 포함한다.
- [x] 테마가 다르다면 동일한 시간에 동일한 이름을 가진 예약을 할 수 있다.

### 인기 테마 조회

- [x] 최근 1주 동안 예약이 많았던 테마 상위 10개를 조회할 수 있다.
- [x] 조회 기준은 오늘을 제외한 이전 7일을 기준으로 한다.

### 구현 관련

- [x] `data.sql`에 기본 시간을 추가해둔다.

---

## API 명세

### 사용자 API

| 기능       | 메서드 / URL                   | 요청 본문                           | 쿼리 파라미터 (전체 optional)                              | 응답                                                                          |
|----------|-----------------------------|---------------------------------|----------------------------------------------------|-----------------------------------------------------------------------------|
| 예약 목록 조회 | `GET /reservations`         | -                               | -                                                  | `200 OK` <br> `[{reservationId, name, date, theme, time}, ...]`             |
| 예약 단건 조회 | `GET /reservations/{id}`    | -                               | -                                                  | `200 OK` <br> `{reservationId, name, date, theme, time}`                    |
| 예약 추가    | `POST /reservations`        | `{name, themeId, date, timeId}` | -                                                  | `201 Created` <br> `{reservationId, name, theme: {...}, date, time: {...}}` |
| 예약 삭제    | `DELETE /reservations/{id}` | -                               | -                                                  | `200 OK`                                                                    |
| 시간 조회    | `GET /times`                | -                               | -                                                  | `200 OK` <br> `[{timeId, startAt}, ...]`                                    |
| 시간 조회    | `GET /times/available`      | -                               | `date`, `themeId`                                  | `200 OK` <br> `[{timeId, startAt}, ...]`                                    |
| 테마 목록 조회 | `GET /themes`               | -                               | -                                                  | `200 OK` <br> `[{themeId, name, description, url}, ...]`                    |
| 테마 단건 조회 | `GET /themes/{id}`          | -                               | -                                                  | `200 OK` <br> `{themeId, name, description, url}`                           |
| 인기 테마 조회 | GET /themes/famous          | -                               | days - optional, date - optional, limit - optional | `200 OK` <br> `[{themeId, name, description, url}, ...]`                    |

### 어드민 API

| 기능    | 메서드 / URL                   | 요청 본문                      | 쿼리 파라미터 | 응답                                                     |
|-------|-----------------------------|----------------------------|---------|--------------------------------------------------------|
| 시간 추가 | `POST /admin/times`         | `{startAt}`                | -       | `201 Created` <br> `{timeId, startAt}`                 |
| 시간 삭제 | `DELETE /admin/times/{id}`  | -                          | -       | `200 OK`                                               |
| 테마 생성 | `POST /admin/themes`        | `{name, description, url}` | -       | `201 Created` <br> `{themeId, name, description, url}` |
| 테마 수정 | `PUT /admin/themes/{id}`    | `{name, description, url}` | -       | `200 OK` <br> `{themeId, name, description, url}`      |
| 테마 삭제 | `DELETE /admin/themes/{id}` | -                          | -       | `200 OK`                                               |