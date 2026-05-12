# 에러 응답 설계 문서

## 에러 응답 형식 (토론 그룹에서 정한 형식)

```json
{
  "code": "에러_코드",
  "path": "/요청_URI",
  "message": "사용자에게 보여줄 메시지",
  "action": "해결 방법 안내 (없으면 null)"
}
```

---

## 예약 (Reservation)

### POST /reservations

|          상황           | 상태 코드 |             code              |         message         | action                          |
|:---------------------:|:-----:|:-----------------------------:|:-----------------------:|---------------------------------|
|   필수 값 누락 (name 없음)   |  400  |    RESERVATION_BLANK_NAME     |     name이 누락되었습니다.      | requestBody에 name 값을 추가해주세요.    |
|   필수 값 누락 (date 없음)   |  400  |    RESERVATION_BLANK_DATE     |     date가 누락되었습니다.      | requestBody에 date 값을 추가해주세요.    |
|  필수 값 누락 (timeId 없음)  |  400  |   RESERVATION_BLANK_TIMEID    |    timeId가 누락되었습니다.     | requestBody에 timeId 값을 추가해주세요.  |
| 필수 값 누락 (themeId 없음)  |  400  |   RESERVATION_BLANK_THEMEID   |    themeId가 누락되었습니다.    | requestBody에 themeId 값을 추가해주세요. |
|   이름 길이 위반 (2~20자)    |  400  |    RESERVATION_WRONG_NAME     |  이름은 2자 이상 20자 이하 입니다.  | -                               |
|       잘못된 날짜 형식       |  400  |   RESERVATION_INVALID_DATE    |     날짜 형식이 잘못되었습니다.     | 날짜 형식은 2026-05-11 형태여야 합니다.     |
|     이미 지난 날짜로 예약      |  400  |    RESERVATION_WRONG_DATE     | 이미 지난 날짜로는 예약이 불가능합니다.  | -                               |
| 오늘 날짜인데 이미 지난 시간으로 예약 |  400  |    RESERVATION_WRONG_TIME     | 이미 지난 시간으로는 예약이 불가능합니다. | -                               |
|    존재하지 않는 timeId     |  404  | RESERVATION_TIMEID_NOT_FOUND  |    해당 시간은 존재하지 않습니다.    | -                               |
|    존재하지 않는 themeId    |  404  | RESERVATION_THEMEID_NOT_FOUND |    해당 테마는 존재하지 않습니다.    | -                               |
|   같은 날짜+시간+테마 중복 예약   |  409  |     RESERVATION_DUPLICATE     |     중복된 예약이 존재합니다.      | 다른 날짜 혹은 시간, 테마로 예약해 주세요.       |

### DELETE /reservations/{id}

| 상황            | 상태 코드 | code                  | message                     | action          |
|---------------|-------|-----------------------|-----------------------------|-----------------|
| 존재하지 않는 예약 삭제 | 404   | RESERVATION_NOT_FOUND | 해당 예약이 존재하지 않아서 삭제할 수 없습니다. | 예약 아이디를 확인해주세요. |

---

## 시간 (Time)

### POST /times

| 상황                   | 상태 코드 | code               | message              | action                          |
|----------------------|-------|--------------------|----------------------|---------------------------------|
| 필수 값 누락 (startAt 없음) | 400   | TIME_BLANK_STARTAT | startAt이 누락되었습니다.    | requestBody에 startAt 값을 추가해주세요. |
| 잘못된 시간 형식            | 400   | TIME_WRONG_STARTAT | startAt의 값이 잘못되었습니다. | TIME의 형태에 맞게 작성해주세요. ex) 08:00  |
| 이미 존재하는 시간           | 409   | TIME_DUPLICATE     | 이미 존재하는 시간이 있습니다.    | 다른 startAt을 입력해주세요.             |

### DELETE /times/{id}

| 상황             | 상태 코드 | code               | message                      | action                         |
|----------------|-------|--------------------|------------------------------|--------------------------------|
| 존재하지 않는 시간 삭제  | 404   | TIME_NOT_FOUND     | 해당 시간이 존재하지 않아서 삭제할 수 없습니다.  | 시간 아이디를 확인해주세요.                |
| 예약이 존재하는 시간 삭제 | 409   | TIME_CANNOT_DELETE | 해당 시간의 예약이 존재하기에 삭제할 수 없습니다. | 해당 시간의 모든 예약을 삭제한 후, 재시도 해주세요. |

---

## 테마 (Theme)

### POST /themes

| 상황                | 상태 코드 | code             | message               | action                       |
|-------------------|-------|------------------|-----------------------|------------------------------|
| 이름 빈 값            | 400   | THEME_BLANK_NAME | name이 누락되었습니다.        | requestBody에 name 값을 추가해주세요. |
| 이름 길이 초과 (20자 초과) | 400   | THEME_WRONG_NAME | 이름은 2자 이상 20자 이하 입니다. | -                            |
| url 누락/빈 값        | 400   | THEME_BLANK_URL  | url이 누락되었습니다.         | requestBody에 url 값을 추가해주세요.  |

### DELETE /themes/{id}

| 상황            | 상태 코드 | code            | message                     | action          |
|---------------|-------|-----------------|-----------------------------|-----------------|
| 존재하지 않는 테마 삭제 | 404   | THEME_NOT_FOUND | 해당 테마가 존재하지 않아서 삭제할 수 없습니다. | 테마 아이디를 확인해주세요. |

### GET /themes/ranks

| 상황                  | 상태 코드 | code                     | message                | action                       |
|---------------------|-------|--------------------------|------------------------|------------------------------|
| limit 누락            | 400   | THEME_RANK_BLANK_LIMIT   | limit 값이 누락되었습니다.      | queryString에 limit값을 추가해주세요. |
| limit 음수 또는 0       | 400   | THEME_RANK_INVALID_LIMIT | limit는 0 이하일 수 없습니다.   | -                            |
| limit 범위 초과 (30 초과) | 400   | THEME_RANK_INVALID_LIMIT | limit는 30 초과 일 수 없습니다. | -                            |

---

## 공통 (예상치 못한 서버 에러)

| 상황         | 상태 코드 | code           | message            | action |
|------------|-------|----------------|--------------------|--------|
| 처리되지 않은 예외 | 500   | INTERNAL_ERROR | 요청 처리에 문제가 발생했습니다. | -      |

---

## 설계 판단 메모

여기에 설계하면서 고민한 점이나 판단 근거를 기록한다.

- action을 받아서 사용할 사람은 "사용자"가 아니라 "동료(개발자)" 이다.
    - 프론트엔드를 담당한 동료가 이 action과 code를 기반으로 적절한 UI를 구성해 줄 것을 기대한다.

---

## 구현 체크리스트

### POST /reservations

- [x] RESERVATION_BLANK_NAME — 필수 값 누락 (name 없음)
- [x] RESERVATION_BLANK_DATE — 필수 값 누락 (date 없음)
- [x] RESERVATION_BLANK_TIMEID — 필수 값 누락 (timeId 없음)
- [x] RESERVATION_BLANK_THEMEID — 필수 값 누락 (themeId 없음)
- [x] RESERVATION_WRONG_NAME — 이름 길이 위반 (2~20자)
- [x] RESERVATION_INVALID_DATE — 잘못된 날짜 형식
- [ ] RESERVATION_WRONG_DATE — 이미 지난 날짜로 예약
- [ ] RESERVATION_WRONG_TIME — 오늘 날짜인데 이미 지난 시간으로 예약
- [x] RESERVATION_TIMEID_NOT_FOUND — 존재하지 않는 timeId
- [x] RESERVATION_THEMEID_NOT_FOUND — 존재하지 않는 themeId
- [x] RESERVATION_DUPLICATE — 같은 날짜+시간+테마 중복 예약

### DELETE /reservations/{id}

- [x] RESERVATION_NOT_FOUND — 존재하지 않는 예약 삭제

### POST /times

- [x] TIME_BLANK_STARTAT — 필수 값 누락 (startAt 없음)
- [x] RESERVATION_INVALID_DATE — 잘못된 날짜 형식
- [x] TIME_DUPLICATE — 이미 존재하는 시간

### DELETE /times/{id}

- [x] TIME_NOT_FOUND — 존재하지 않는 시간 삭제
- [x] TIME_CANNOT_DELETE — 예약이 존재하는 시간 삭제

### POST /themes

- [x] THEME_BLANK_NAME — 이름 빈 값
- [x] THEME_WRONG_NAME — 이름 길이 초과
- [x] THEME_BLANK_URL — url 누락/빈 값

### DELETE /themes/{id}

- [x] THEME_NOT_FOUND — 존재하지 않는 테마 삭제

### GET /themes/ranks

- [ ] THEME_RANK_BLANK_LIMIT — limit 누락
- [x] THEME_RANK_INVALID_LIMIT — limit 음수 또는 0
- [x] THEME_RANK_INVALID_LIMIT — limit 범위 초과 (30 초과)

### 공통

- [x] INTERNAL_ERROR — 처리되지 않은 예외