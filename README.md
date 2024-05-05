# 🗝️방탈출 사용자 예약🗝️

## 기능 명세서

### 주간 인기 테마 페이지

- `/` 으로 접속할 수 있다.
- 지난 한 주 동안 가장 많이 예약된 순서로 테마를 최대 10개 보여준다.

### 관리자 메인 페이지

- `/admin` 으로 접속할 수 있다.
- 관리자 페이지를 볼 수 있다.
- 네비게이션 바의 Reservation을 누르면 관리자 예약 페이지로 이동한다.

### 예약 관리 페이지

- `/admin/reservation` 으로 접속할 수 있다.
- 예약 목록을 볼 수 있다.
    - 예약 번호, 이름, 날짜, 시간을 볼 수 있다.
- 예약을 추가할 수 있다.
    - 이름, 날짜, 시간을 입력하여 추가한다.
    - 빈 값이 있으면 예외를 던진다.
    - 같은 날짜, 시간, 테마에 예약하면 예외를 던진다.
    - 지나간 날짜와 시간에 예약하면 예외를 던진다.
- 예약을 삭제할 수 있다.

### 시간 관리 페이지

- `/admin/time` 으로 접속할 수 있다.
- 시간 목록을 볼 수 있다.
    - 순서, 시간을 볼 수 있다.
- 시간을 추가할 수 있다.
    - 시간을 입력하여 추가한다.
    - 빈 값이 있으면 예외를 던진다.
- 시간을 삭제할 수 있다.
    - 해당 시간에 예약한 내역이 존재하면 예외를 던진다.

### 테마 관리 페이지

- `/admin/theme` 으로 접속할 수 있다.
- 테마 목록을 볼 수 있다.
    - 순서, 테마 이름, 설명을 볼 수 있다.
- 테마를 추가할 수 있다.
    - 테마 이름, 설명, 섬네일을 입력하여 추가한다.
- 테마를 삭제할 수 있다.
    - 해당 테마를 예약한 내역이 존재하면 예외를 던진다.

### 사용자 예약 페이지

- `/reservation` 으로 접속할 수 있다.
- 날짜, 테마를 선택하면 예약 가능한 시간을 볼 수 있다.
- 날짜, 테마, 시간을 선택하고 이름을 입력하여 예약할 수 있다.

## API 명세서

| HTTP Method | URI                                    | Description    | Request                                           | Response                                              |
|-------------|----------------------------------------|----------------|---------------------------------------------------|-------------------------------------------------------|
| GET         | `/`                                    | 주간 인기 테마 페이지   |                                                   | `templates/index.html`                                |
| GET         | `/admin`                               | 관리자 메인 페이지     |                                                   | `templates/admin/index.html`                          |
| GET         | `/admin/reservation`                   | 예약 관리 페이지      |                                                   | `templates/admin/reservation-new.html`                |
| GET         | `/admin/time`                          | 시간 관리 페이지      |                                                   | `templates/admin/time.html`                           |
| GET         | `/admin/theme`                         | 테마 관리 페이지      |                                                   | `templates/admin/theme.html`                          |
| GET         | `/reservation`                         | 사용자 예약 페이지     |                                                   | `templates/reservation.html`                          |
| GET         | `/reservations`                        | 예약 목록 조회       |                                                   | [ReservationsResponse](#ReservationsResponse)         |
| POST        | `/reservations`                        | 예약 추가          | [ReservationRequest](#ReservationRequest)         | [ReservationResponse](#ReservationResponse)           |
| DELETE      | `/reservations/{id}`                   | 예약 삭제          |                                                   | `HTTP/1.1 204`                                        |
| GET         | `/times`                               | 시간 목록 조회       |                                                   | [ReservationTimesResponse](#ReservationTimesResponse) |
| GET         | `/times?date={date}&themeId={themeId}` | 예약 가능 시간 목록 조회 |                                                   | [BookedTimesResponse](#BookedTimesResponse)           |
| POST        | `/times`                               | 시간 추가          | [ReservationTimeRequest](#ReservationTimeRequest) | [ReservationTimeResponse](#ReservationTimeResponse)   |
| DELETE      | `/times/{id}`                          | 시간 삭제          |                                                   | `HTTP/1.1 204`                                        |
| GET         | `/themes`                              | 테마 목록 조회       |                                                   | [ThemesResponse](#ThemesResponse)                     |
| GET         | `/themes/popular`                      | 주간 인기 테마 목록 조회 |                                                   | [ThemesResponse](#ThemesResponse)                     |
| POST        | `/themes`                              | 테마 추가          | [ThemeRequest](#ThemeRequest)                     | [ThemeResponse](#ThemeResponse)                       |
| DELETE      | `/themes/{id}`                         | 테마 삭제          |                                                   | `HTTP/1.1 204`                                        |

### ReservationRequest

```json
{
  "name": "홍길동",
  "date": "2021-07-01",
  "timeId": 1,
  "themeId": 1
}
```

### ReservationResponse

```
HTTP/1.1 201 Created
```

```json
{
  "id": 1,
  "name": "홍길동",
  "date": "2021-07-01",
  "time": {
    "id": 1,
    "time": "10:00"
  },
  "theme": {
    "id": 1,
    "name": "테마1",
    "description": "테마1 설명",
    "thumbnail": "테마1 섬네일"
  }
}
```

### ReservationsResponse

```
HTTP/1.1 200 OK
```

```json
{
  "reservations": [
    {
      "id": 1,
      "name": "홍길동",
      "date": "2021-07-01",
      "time": {
        "id": 1,
        "time": "10:00"
      },
      "theme": {
        "id": 1,
        "name": "테마1",
        "description": "테마1 설명",
        "thumbnail": "테마1 섬네일"
      }
    }
  ]
}
```

### ReservationTimeRequest

```json
{
  "time": "10:00"
}
```

### ReservationTimeResponse

```
HTTP/1.1 201 Created
```

```json
{
  "id": 1,
  "time": "10:00"
}
```

### ReservationTimesResponse

```
HTTP/1.1 200 OK
```

```json
{
  "times": [
    {
      "id": 1,
      "time": "10:00"
    }
  ]
}
```

### BookedTimesResponse

```
HTTP/1.1 200 OK
```

```json
{
  "times": [
    {
      "id": 1,
      "time": "10:00"
    }
  ]
}
```

### ThemeRequest

```json
{
  "name": "테마1",
  "description": "테마1 설명",
  "thumbnail": "테마1 섬네일"
}
```

### ThemeResponse

```
HTTP/1.1 201 Created
```

```json
{
  "id": 1,
  "name": "테마1",
  "description": "테마1 설명",
  "thumbnail": "테마1 섬네일"
}
```

### ThemesResponse

```
HTTP/1.1 200 OK
```

```json
{
  "themes": [
    {
      "id": 1,
      "name": "테마1",
      "description": "테마1 설명",
      "thumbnail": "테마1 섬네일"
    }
  ]
}
```