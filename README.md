# spring-roomescape-member

> 사용자 방탈출 예약 미션 저장소

페어 : [스타크](https://github.com/MODUGGAGI), [카야](https://github.com/choiyoung69)

---

## 👥 페어 프로그래밍 규칙

네비게이터, 드라이버 역할 교환은 **15분**에 한번씩

|          **네비게이터**          |            **드라이버**            |
|:---------------------------:|:------------------------------:|
| 드라이버에게 지시와 동시에 서로의 대화 내용 기록 | 자신이 작성하려는 코드의 의도를 항상 명확히 말로 전달 |

---

## 기능 요구 사항

### **1단계 - 테마 도메인 추가**

- [x]  관리자의 테마 추가
    - 테마 이름, 테마 설명, 테마 썸네일 이미지 URL을 RequestBody로 보낸다.
- [x]  관리자의 테마 삭제
- [ ]  `Reservation` 클래스에 Theme 필드 추가

### **2단계 - 사용자 예약**

- [ ]  날짜를 선택하면 해당 날짜의 테마 조회 기능 추가
- [ ]  날짜와 테마를 선택하면 예약 가능한 시간 목록 조회 기능 추가
    - 예약 불가능한 시간과 가능한 시간 전부 조회 가능
    - 불가능한 시간은 선택이 불가능하게 처리
- [ ]  사용자의 방탈출 예약 기능 추가
    - Request Body로
        1. 본인 이름
        2. 선택 날짜
        3. 테마 (테마 ID)
        4. 시간 (시간 ID)

      4개의 필드 포함

    - 이미 예약되어있는 날짜, 테마, 시간에 방탈출 예약 불가

### **3단계 - 인기 테마 조회**

- [ ]  최근 1주 동안 예약이 많았던 테마 상위 10개 조회 기능 추가
    - 최근 1주는 오늘 제외하고 어제 기준으로 1주 동안의 기간

---

## API 명세서

### 기존 관리자 API 명세서

#### 예약 (`/reservations`)

| HTTP Method | 상태 코드 | Endpoint | 기능 | 요청 본문 | 응답 본문 |
|:-----------:|:---------:|----------|------|-----------|-----------|
| GET | 200 | `/reservations` | 전체 예약 조회 | - | `[{ id, name, date, time: { id, time } }]` |
| POST | 201 | `/reservations` | 예약 생성 | `{ name, date, timeId }` | `{ id, name, date, time: { id, time } }` |
| DELETE | 204 | `/reservations/{id}` | 예약 삭제 | - | - |

#### 예약 시간 (`/times`)

| HTTP Method | 상태 코드 | Endpoint | 기능 | 요청 본문 | 응답 본문 |
|:-----------:|:---------:|----------|------|-----------|-----------|
| GET | 200 | `/times` | 전체 예약 시간 조회 | - | `[{ id, time }]` |
| POST | 201 | `/times` | 예약 시간 생성 | `{ startAt }` | `{ id, time }` |
| DELETE | 204 | `/times/{id}` | 예약 시간 삭제 | - | - |

> - `date` : `yyyy-MM-dd` 형식 (예: `2024-05-01`)
> - `startAt` / `time` : `HH:mm` 형식 (예: `10:00`)
> - `timeId` : 예약 시간의 id (Long)


### 페어가 추가한 API 명세서

#### 테마 (`/themes`)

| HTTP Method | 상태 코드 | Endpoint | 기능 | 요청 본문 | 응답 본문 |
|:-----------:|:---------:|----------|------|-----------|-----------|
| GET | 200 | `/themes` | 전체 테마 조회 | - | `[{ id, name, description, thumbnailImgUrl }]` |
| POST | 201 | `/themes` | 테마 생성 | `{ name, description, thumbnailImgUrl }` | `{ id, name, description, thumbnailImgUrl }` |
| DELETE | 204 | `/themes/{id}` | 테마 삭제 | - | - |
| DELETE | 404 | `/themes/{id}` | 테마 삭제 (존재하지 않는 경우) | - | - |
