# 🚀 방탈출 사용자 예약 설계

### 📄 방탈출 통합 API 명세

| 도메인             | Method   | Endpoint             | Path Variable | Query Parameter                        | Request Body                                            | Response                    | Description                            |
|:----------------|:---------|:---------------------|:--------------|:---------------------------------------|:--------------------------------------------------------|:----------------------------|:---------------------------------------|
| **Reservation** | `GET`    | `/reservations`      | -             | -                                      | -                                                       | `List<ReservationResponse>` | 모든 예약 목록 조회                            |
| **Reservation** | `POST`   | `/reservations`      | -             | -                                      | `ReservationRequest`<br>*(name, date, timeId, themeId)* | `ReservationResponse`       | 새로운 예약 생성                              |
| **Reservation** | `DELETE` | `/reservations/{id}` | `id` (Long)   | -                                      | -                                                       | `200 OK` (Void)             | 식별자를 통한 예약 삭제                          |
| **Theme**       | `GET`    | `/themes`            | -             | -                                      | -                                                       | `List<ThemeResponse>`       | 모든 테마 목록 조회                            |
| **Theme**       | `GET`    | `/themes`            | -             | `topCount` (Long)<br>`during` (Long)   | -                                                       | `List<ThemeResponse>`       | 기간(`during`) 내 상위(`topCount`) 인기 테마 조회 |
| **Theme**       | `POST`   | `/themes`            | -             | -                                      | `ThemeRequest`<br>*(name, description, thumbnailUrl)*   | `ThemeResponse`             | 새로운 테마 생성                              |
| **Theme**       | `DELETE` | `/themes/{id}`       | `id` (Long)   | -                                      | -                                                       | `200 OK` (Void)             | 식별자를 통한 테마 삭제                          |
| **Time**        | `GET`    | `/timeSlots`         | -             | -                                      | -                                                       | `List<TimeResponse>`        | 모든 예약 시간 목록 조회                         |
| **Time**        | `GET`    | `/timeSlots`         | -             | `themeId` (long)<br>`date` (LocalDate) | -                                                       | `List<TimeResponse>`        | 특정 날짜, 테마의 예약 가능 시간 조회                 |
| **Time**        | `POST`   | `/timeSlots`         | -             | -                                      | `TimeRequest`<br>*(startAt)*                            | `TimeResponse`              | 새로운 예약 시간 생성                           |
| **Time**        | `DELETE` | `/timeSlots/{id}`    | `id` (Long)   | -                                      | -                                                       | `200 OK` (Void)             | 식별자를 통한 예약 시간 삭제                       |

[`React.js 클라이언트`](https://github.com/Uechann/react-roomscape)

<details><summary><h4>📜 그룹 규칙 상세</h4></summary>

1. 리소스 식별 기준

URL을 정할 때 무엇을 리소스로 보고, 무엇을 필터·표현 조건으로 보는가

Rule 1. 도메인 분석을 통해, 실행 흐름에 나타나는 명사를 리소스로 식별한다.
리소스 식별 규칙 자체는 추상적으로 설정하되,
API 명세 설계에 대한 각자의 관점에 맞춰 판단하고

* 서버/도메인 모델 관점
* 클라이언트/멘탈 모델 관점

이후 피드백 활동에서 각자의 경험을 공유해 규칙을 구체화한다.

2. 서버/클라이언트 책임 기준

어떤 판단을 서버가 책임지고, 어떤 판단을 클라이언트가 책임지는가

* (If-Then) 만약 판단에 필요한 원천 데이터가 서버에만 존재한다면 → 해당 로직은 서버가 책임지고 결과값을 내려준다.
    * 예: "예약 가능 여부(available)"는 서버가 기존 예약 현황을 계산하여 응답에 포함한다.

* (If-Then) 만약 클라이언트가 받은 데이터를 가공 없이 화면에 그리는 역할이라면 → 서버는 클라이언트가 바로 사용할 수 있는 형태(UI friendly)로 응답을 설계한다.

* (우선순위) 책임 판단 순서:

    1. 데이터의 무결성을 보장해야 하는 곳이 어디인가? (보통 서버)
    2. 클라이언트가 중복된 계산 로직을 가질 필요가 있는가?

* (금지) 클라이언트에게 비즈니스 로직 판단을 떠넘기지 않는다.
    * 이유: 데이터 불일치 문제를 방지하고 클라이언트의 복잡도를 낮추기 위함이다.


3. 관리자/사용자 API 분리 기준

같은 자원을 다루는 관리자와 사용자 API를 분리하는가, 합치는가

* (If-Then) 만약 리소스의 도메인 모델과 생성 행위가 동일하다면 → 관리자와 사용자 구분 없이 같은 엔드포인트를 사용한다.
    * 예: POST /reservations (누가 생성하든 '예약 생성'이라는 행위는 동일함)
* (If-Then) 만약 사용자의 권한에 따라 응답 필드의 구성이나 노출 데이터가 현저히 다르다면 → 도메인 목적에 따라 API를 분리하거나 권한 로직으로 분기한다.
* (우선순위) 결정 순서:

    1. 리소스의 식별자가 동일한가?
    2. 조회/수정의 목적과 Context가 동일한가?


4. 우리 그룹의 "좋은 API" 정의

동작하는 것을 넘어, 어떤 조건을 만족해야 좋은 API라고 부를 것인가

좋은 API

* 정의: 동작을 완벽히 보장하는 것은 물론, 뛰어난 가독성을 바탕으로 명세만 보아도 요청과 행동을 직관적으로 식별할 수 있는 API.
* 특징:
    * 직관성: URL(리소스 명사)과 HTTP 메서드(행위 동사)의 조합만으로 해당 API가 어떤 자원에 대해 무슨 작업을 수행하는지 누구나 명확히 추론할 수 있습니다.
    * 명확성: 리소스의 정의, 메서드 시그니처 등에 대해 팀이 합의한 명확한 기준과 규칙을 엄격하게 따릅니다.
    * 문서화의 역할: 잘 짜인 API 명세 자체가 훌륭한 설명서 역할을 하여, 개발자 간의 소통 비용과 유지보수 비용을 크게 낮춥니다.

</details>

---

## ✅ 1 단계 - 테마 도메인 추가

| 도메인       | Method   | Endpoint       | Path Variable | Query Parameter                      | Request Body                                          | Response              | Description                            |
|:----------|:---------|:---------------|:--------------|:-------------------------------------|:------------------------------------------------------|:----------------------|:---------------------------------------|
| **Theme** | `GET`    | `/themes`      | -             | -                                    | -                                                     | `List<ThemeResponse>` | 모든 테마 목록 조회                            |
| **Theme** | `GET`    | `/themes`      | -             | `topCount` (Long)<br>`during` (Long) | -                                                     | `List<ThemeResponse>` | 기간(`during`) 내 상위(`topCount`) 인기 테마 조회 |
| **Theme** | `POST`   | `/themes`      | -             | -                                    | `ThemeRequest`<br>*(name, description, thumbnailUrl)* | `ThemeResponse`       | 새로운 테마 생성                              |
| **Theme** | `DELETE` | `/themes/{id}` | `id` (Long)   | -                                    | -                                                     | `200 OK` (Void)       | 식별자를 통한 테마 삭제                          |

### API 설계의 근거

- 테마는 `리소스` 라 판단, 단독 엔드포인트로 정의
    - 현재 예약의 필드로만 사용되는 종속적 `도메인` 이지만,
    - 클라이언트의 입장에서 독립된 멘탈 모델, 즉 `리소스` 이다.
- API 설계 원칙에 따라 행위를 HTTP 메서드로 표현

<details><summary><h4>설계 과정 상세</h4></summary>

### 그룹 규칙 기반

- 규칙 1. 테마는 리소스인가?
    - 맞다.
    - 왜?
    - 예약에 종속적인 도메인 모델이지만,
      독립적인 멘탈 모델이니까
- 규칙 2. 테마의 상태를 검증할 주체는?
    - 프론트가 요청할 때, 값의 존재를 검증
    - 백엔드가 생성할 때, 값의 유효를 검증
- 규칙 3. 테마에 접근할 주체는?
    - 관리자가 테마를 추가/수정/삭제하고
    - 사용자는 테마를 사용한다
    - 테마는 관리자가 조작(Command)하고, 사용자가 조회(Query)한다
- 규칙 4. 테마의 API 명세는 어떻게 작성해야?
    - 조회는 동일하다.
    - 조회 GET /themes
    - 추가/수정/삭제를 사용자가 사용하지 않는다 가정한다면  
      별도의 API 로 분리할 필요가 없다.
    - 추가 PUT /themes
    - 삭제 DELETE /themes

### 테마 도메인

`record Theme`

- 이름 `String name`
- 설명 `String discription`
- 썸네일 이미지 URL `String thumbnailUrl`
- 시작 시간과 소요 시간은 동일하다고 가정한다.

### 기능 목록

> 방탈출 게임에 '테마' 정보를 추가한다.  
> 테마는 이름, 설명, 썸네일 이미지 URL을 가진다.  
> 모든 테마의 시작 시간과 소요 시간은 동일하다고 가정한다.

-[x] record Theme
    -[x] String name
    -[x] String description
    -[x] String thumbnailUrl
    -[x] void validate
        -[x] Theme transientOf

> 예약에 테마 정보를 포함하도록 기존 코드를 변경한다.

-[x] class ThemeController
    -[x] ThemeRequest
    -[x] ThemeResponse

> 관리자가 테마를 추가·삭제할 수 있다.

-[x] class ThemeService
    - [x] Theme saveTime
    - [x] void removeTime
-[x] class ThemeRepository
-[x] class ThemeDao
-[x] record Reservation
    -[x] Theme theme

### 관리자가 테마를 추가, 삭제, 조회

### 추가 API

POST /themes

### 조회 API

GET /themes

### 삭제

DELETE /themes/{themeID}

</details>

---

## 추가 단계 - 사용자 클라이언트 화면 제작

[`React.js 클라이언트`](https://github.com/Uechann/react-roomscape)

- [마이찬](https://github.com/Uechann) 실험 기록
- API 명세 기반
- Claude 활용

---

## ✅ 2단계 - 사용자 예약

| 도메인             | Method | Endpoint        | Path Variable | Query Parameter                        | Request Body                                            | Response              | Description            |
|:----------------|:-------|:----------------|:--------------|:---------------------------------------|:--------------------------------------------------------|:----------------------|:-----------------------|
| **Time**        | `GET`  | `/timeSlots`    | -             | `themeId` (long)<br>`date` (LocalDate) | -                                                       | `List<TimeResponse>`  | 특정 날짜, 테마의 예약 가능 시간 조회 |
| **Reservation** | `POST` | `/reservations` | -             | -                                      | `ReservationRequest`<br>*(name, date, timeId, themeId)* | `ReservationResponse` | 새로운 예약 생성              |

### API 설계의 근거

> 사용자가 `날짜와 테마를 선택`하면 예약 가능한 시간 목록이 표시된다

- 사용자가 `선택`한 `날짜`, `테마`를 기반으로 `예약 가능한 시간` 목록을 표시한다
    - 요청 - 날짜, 테마
    - Request Parameter - date, theme(id)
    - 응답 - 시간(예약 가능한)
    - Response Body - list<timeSlot>

> 사용자가 예약 가능한 시간을 선택하여 본인의 이름으로 예약한다 -> POST /reservation

- 사용자가 `선택`한 `날짜`, `테마`, `시간`을 기반으로 예약을 진행한다
    - 요청 - 날짜, 테마, 시간
    - Request Parameter - date, theme(id), timeSlot(id)
    - 응답 - 예약 결과
    - Response Body - reservation

- 테마는 `리소스` 라 판단, 단독 엔드포인트로 정의
    - 현재 예약의 필드로만 사용되는 종속적 `도메인` 이지만,
    - 클라이언트의 입장에서 독립된 멘탈 모델, 즉 `리소스` 이다.
- API 설계 원칙에 따라 행위를 HTTP 메서드로 표현

<details><summary><h4>설계 과정 상세</h4></summary>

### 1. 테마들의 모든 정보 조회 API

```json
GET /themes HTTP/1.1 
```

### 2. 사용자가 (날짜, 테마)를 통해 예약 가능 시간 조회 API

```json
GET /timeSlots?date=2026-05-04&themeId=1HTTP/1.1 
```

### 3. 사용자가 날짜, 테마, 예약 시간을 통해 예약 등록 API

```json
POST /reservations HTTP/1.1

{
  "themeId": 1,
  "date": "2026-05-03",
  "timeId": 1,
  "name": "브라운"
}
```

</details>

## ✅ 3단계 - 인기 테마 조회

| 도메인       | Method | Endpoint  | Path Variable | Query Parameter                      | Request Body | Response              | Description                            |
|:----------|:-------|:----------|:--------------|:-------------------------------------|:-------------|:----------------------|:---------------------------------------|
| **Theme** | `GET`  | `/themes` | -             | `topCount` (Long)<br>`during` (Long) | -            | `List<ThemeResponse>` | 기간(`during`) 내 상위(`topCount`) 인기 테마 조회 |

### API 설계의 근거

- 특정 `스코프` 를 기준으로 `리소스` 인 테마를 조회.
    - 테마를 조회한다는 `목적` 과 `횅위` 가 동일하기에 동일한 엔드포인트로
    - 클라이언트의 입장에서 검색 조건인 `스코프` 로 행위를 추론

<details><summary><h4>설계 과정 상세</h4></summary>

### 1. 최근 1주 동안 예약이 많았던 테마 상위 10개를 조회한다.

> 테마 ~~상위 10개~~를 조회

- 응답은 테마 `record Theme`

> ~~테마~~ 상위 10개~~를 조회~~

- 조건은 10개 `LIMIT 10`

> 최근 1주 동안

- 조건은 최근 1주 `WHERE DATE BETWEEN NOW()-7 AND NOW()`

```json
GET /themes?topCount=10&during=7 HTTP / 1.1 
```

</details>
