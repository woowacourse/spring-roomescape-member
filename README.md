# 기능 목록

## 1단계 :

- [ ] 예약에 테마 정보를 포함하도록 한다.
- [ ] 테마는 이름, 설명, 썸네일 이미지 URL을 가진다.
- [ ] 각 테마의 시작 시간은 10시이고, 종료 시간은 20시이다.
- [ ] 각 테마의 소요 시간은 2시간이다.
- [ ] 관리자가 테마를 추가, 삭제할 수 있다.

## 2단계 :

- [ ] 사용자가 날짜와 테마를 선택하면 예약 가능한 시간 목록이 표시된다.
- [ ] 사용자가 예약 가능한 시간을 선택하여 본인의 이름으로 예약한다. (동명이인은 어떡하지?)
- [ ] 같은 날짜, 시간이라도 테마가 다르면 각각 예약 가능한다.

## 3단계 :

- [ ] 최근 1주 동안 예약이 많았던 테마 상위 10개를 조회한다.
  - 예: 오늘이 5월 8일이면, 게임 날짜가 5월 1일 ~ 5월 7일인 예약을 집계해 인기 순서대로 10개를 응답한다.


# API 명세서

## 관리자 API 명세서

### 1. 테마 추가
| 항목 | 상세 내용 |
| :--- | :--- |
| **URL** | `/admin/themes` |
| **HTTP Method** | `POST` |
| **Request (Body)** | `{ "name": "테마명" }` |
| **Response** | `{ "id": 1, "name": "테마명" }` |
| **StatusCode** | `201 Created` |

### 2. 테마 삭제
| 항목 | 상세 내용 |
| :--- | :--- |
| **URL** | `/admin/themes/{id}` |
| **HTTP Method** | `DELETE` |
| **Request (Path)** | `id` (테마 ID) |
| **Response** | 없음 |
| **StatusCode** | `204 No Content` |

### 3. 전체 예약 조회
| 항목 | 상세 내용 |
| :--- | :--- |
| **URL** | `/admin/reservations` |
| **HTTP Method** | `GET` |
| **Request** | 없음 |
| **Response** | `[ { "reservationId": 1, "userId": 1, "userName": "루크", "themeId": 1, "themeName": "테마명", "date": "2026-05-10", "time": "14:00" }, ... ]` |
| **StatusCode** | `200 OK` |

### 4. 예약 삭제
| 항목 | 상세 내용 |
| :--- | :--- |
| **URL** | `/admin/reservations/{id}` |
| **HTTP Method** | `DELETE` |
| **Request (Path)** | `id` (예약 ID) |
| **Response** | 없음 |
| **StatusCode** | `204 No Content` |

## 사용자 API 명세서

### 1. 테마 목록 조회

| 항목              | 상세 내용                                                                                    |
|:----------------|:-----------------------------------------------------------------------------------------|
| **URL**         | `/themes`                                                                                |
| **HTTP Method** | `GET`                                                                                    |
| **Request**     | 없음                                                                                       |
| **Response**    | `[ { "id": 1, "name": "테마명", "description": "공포", "image_url": "https://techcourse.woowahan.com/" }, ... ]` |
| **StatusCode**  | `200 OK`                                                                                 |

### 2. 예약 가능한 시간 조회

| 항목 | 상세 내용                                                                                             |
| :--- |:--------------------------------------------------------------------------------------------------|
| **URL** | `/schedules?startAt={startAt}&themeId={themeId}`                                                  |
| **HTTP Method** | `GET`                                                                                             |
| **Request (Query)** | `startAt` , `themeId`                                                                             |
| **Response** | `[ { "id": 1, "themeName": "...", "startAt": "...", "endAt": "...", "isAvailable": true }, ... ]` |
| **StatusCode**  | `200 OK`                                                                                          |

### 3. 예약 생성

| 항목 | 상세 내용                                 |
| :--- |:--------------------------------------|
| **URL** | `/reservations`                       |
| **HTTP Method** | `POST`                                |
| **Request (Body)** | `{ "scheduleId": 1, "userId": 1 }`    |
| **Response** | `{ "id": 101 }`                       |
| **StatusCode**  | `201 Created`                         |

### 4. 인기 테마 통계 조회

| 항목 | 상세 내용                                                                                       |
| :--- |:--------------------------------------------------------------------------------------------|
| **URL** | `/admin/themes?sort=reservations&limit=10&days=7`                                           |
| **HTTP Method** | `GET`                                                                                       |
| **Request (Query)** | `sort`: 정렬 기준 (예: `reservations`) <br> `limit`: 조회 개수 (예: `10`) <br> `days`: 통계 기간 (예: `7`) |
| **Response** | `[ { "themeName": "테마명", "reservationCount": 25 }, ... ]`                                   |
| **StatusCode**  | `200 OK`                                                                                    | 
