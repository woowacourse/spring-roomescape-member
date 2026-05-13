<p align="center">
    <img src="https://github.com/user-attachments/assets/eb5a8839-ed5b-453b-a764-85ffcc9d0219"/>
</p>

<h1 align="middle">방탈출 예약 서비스</h1>

![Static Badge](https://img.shields.io/badge/Java-21-green)
![Static Badge](https://img.shields.io/badge/Version-1.0.0-red)

우아한테크코스 BE 레벨 2 방탈출 예약 서비스를 구현한 저장소입니다.

# 목차

- [API 명세서](#api-명세서)
    - [Reservation](#reservation)
    - [Time](#time)
    - [Theme](#theme)
- [클라이언트 기능](#클라이언트-기능)
    - [공통(사용자)](#공통사용자)
        - [인기 테마 조회 기능](#인기-테마-조회-기능)
        - [전체 테마 조회 기능](#전체-테마-조회-기능)
        - [예약 생성 기능](#예약-생성-기능)
    - [관리자](#관리자)
        - [예약 추가 기능](#예약-추가-기능)
        - [예약 관리 기능](#예약-관리-기능)
        - [테마 관리 기능](#테마-관리-기능)
        - [시간 추가 및 관리 기능](#시간-추가-및-관리-기능)

# API 명세서

## Reservation

### `POST /api/reservations` 예약 생성 API

<details>
<summary>세부 내용</summary>

### API 정보

```text
예약을 생성합니다.
```

### 상황별 HTTP Code 및 에러 메시지

| 상황                                | HTTP Code                 | 에러 메시지                     | 문제 필드   |
|-----------------------------------|---------------------------|----------------------------|---------|
| 요청 처리 성공                          | 201 Created               | 없음                         | 없음      |
| 필드의 자료형이 올바르지 않는 경우               | 400 Bad Request           | {필드명}의 형식이 유효하지 않습니다.      | 필드명     |
| 필드의 자료형이 유효하지 않은 경우               | 400 Bad Request           | {필드명}의 값이 유효하지 않습니다.       | 필드명     |
| name의 길이가 0이하, 20 초과인 경우          | 400 Bad Request           | 예약자의 이름의 길이는 1이상 20이하 입니다. | name    |
| date가 현재 시간보다 과거인 경우              | 400 Bad Request           | 예약 날짜가 현재보다 과거입니다.         | date    |
| timeId가 존재하지 않는 경우                | 404 Not Found             | 존재 하지 않는 시간대입니다.           | timeId  |
| themeId가 존재하지 않는 경우               | 404 Not Found             | 존재 하지 않는 테마입니다.            | themeId |
| timeId, themeId에 해당하는 예약이 존재하는 경우 | 409 Conflict              | 이미 예약된 시간, 테마입니다.          | 없음      |
| 서버 오류인 경우                         | 500 Interval Server Error | 서버 내부 오류가 발생했습니다.          | 없음      |

### Request Body

| 필드      | 타입     | 필수 여부 | 설명       | 예시         |
|---------|--------|-------|----------|------------|
| name    | String | 필수    | 예약자 이름   | 브라운        |
| date    | Date   | 필수    | 예약 날짜    | 2026-05-12 |
| timeId  | Number | 필수    | 예약 시간 ID | 1          |
| themeId | Number | 필수    | 예약 테마 ID | 1          |

#### 예시

```json
{
  "name": "브라운",
  "date": "2026-05-12",
  "timeId": 1,
  "themeId": 1
}
```

### Response Body

#### 성공 예시

```json
{
  "id": 1,
  "name": "브라운",
  "date": "2026-05-12",
  "timeId": 1,
  "themeId": 1
}
```

#### 실패 예시

```json
{
  "message": "유효하지 않은 입력값입니다.",
  "errors": [
    {
      "field": "date",
      "message": "예약 날짜가 현재보다 과거입니다."
    }
  ]
}
```

</details>

### `GET /api/admin/reservations` 관리자 예약 목록 조회 API

<details>
<summary>세부 내용</summary>

### API 정보

```text
등록된 전체 예약 목록을 조회합니다.
```

### 상황별 HTTP Code 및 에러 메시지

| 상황       | HTTP Code | 에러 메시지 | 문제 필드 |
|----------|-----------|--------|-------|
| 요청 처리 성공 | 200 OK    | 없음     | 없음    |

### Request Body

없음

### Response Body

#### 성공 예시

```json
[
  {
    "id": 1,
    "name": "브라운",
    "date": "2026-05-04",
    "time": {
      "id": 2,
      "startAt": "12:00"
    },
    "theme": {
      "id": 1,
      "name": "피온피온",
      "description": "설명",
      "imageUrl": "https://roomescape.com/images/themes/ring-banner.png"
    }
  }
]
```

#### 실패 예시

```json
{
  "message": "유효하지 않은 입력값입니다.",
  "errors": [
    {
      "field": "date",
      "message": "예약 날짜가 현재보다 과거입니다."
    }
  ]
}
```

</details>

### `DELETE /api/admin/reservations/{id}` 관리자 예약 삭제 API

<details>
<summary>세부 내용</summary>

### API 정보

```text
예약 ID로 예약을 삭제합니다.
```

### 상황별 HTTP Code 및 에러 메시지

| 상황                  | HTTP Code       | 에러 메시지             | 문제 필드 |
|---------------------|-----------------|--------------------|-------|
| 요청 처리 성공            | 200 OK          | 없음                 | 없음    |
| id의 자료형이 올바르지 않은 경우 | 400 Bad Request | id의 형식이 유효하지 않습니다. | id    |
| id가 존재하지 않는 경우      | 404 Not Found   | 존재 하지 않는 예약입니다.    | id    |

### Path Variables

| 이름 | 타입     | 필수 여부 | 설명        | 예시 |
|----|--------|-------|-----------|----|
| id | Number | 필수    | 삭제할 예약 ID | 1  |

### Request Body

없음

### Response Body

#### 성공 예시

```text
응답 본문 없음
```

#### 실패 예시

```json
{
  "code": "RESERVATION404",
  "errorMessage": "존재 하지 않는 예약입니다."
}
```

</details>

## Time

### `GET /api/times?date={date}&themeId={themeId}` 예약 가능 시간 조회 API

<details>
<summary>세부 내용</summary>

### API 정보

```text
특정 날짜와 테마 기준으로 예약 가능한 시간을 조회합니다.
```

### 상황별 HTTP Code 및 에러 메시지

| 상황                   | HTTP Code       | 에러 메시지                | 문제 필드   |
|----------------------|-----------------|-----------------------|---------|
| 요청 처리 성공             | 200 OK          | 없음                    | 없음      |
| 필드의 자료형이 올바르지 않는 경우  | 400 Bad Request | {필드명}의 형식이 유효하지 않습니다. | 필드명     |
| date가 현재 시간보다 과거인 경우 | 400 Bad Request | 예약 날짜가 현재보다 과거입니다.    | date    |
| themeId가 존재하지 않는 경우  | 404 Not Found   | 존재 하지 않는 테마입니다.       | themeId |

### Query Parameters

| 이름      | 타입     | 필수 여부 | 설명       | 예시         |
|---------|--------|-------|----------|------------|
| date    | Date   | 필수    | 예약 날짜    | 2026-05-12 |
| themeId | Number | 필수    | 예약 테마 ID | 1          |

### Request Body

없음

### Response Body

#### 성공 예시

```json
[
  {
    "id": 1,
    "startAt": "20:30"
  },
  {
    "id": 2,
    "startAt": "22:30"
  }
]
```

#### 실패 예시

```json
{
  "message": "유효하지 않은 입력값입니다.",
  "errors": [
    {
      "field": "date",
      "message": "예약 날짜가 현재보다 과거입니다."
    }
  ]
}
```

</details>

### `GET /api/admin/times` 관리자 예약 시간 목록 조회 API

<details>
<summary>세부 내용</summary>

### API 정보

```text
등록된 전체 예약 시간을 조회합니다.
```

### 상황별 HTTP Code 및 에러 메시지

| 상황       | HTTP Code | 에러 메시지 | 문제 필드 |
|----------|-----------|--------|-------|
| 요청 처리 성공 | 200 OK    | 없음     | 없음    |

### Request Body

없음

### Response Body

#### 성공 예시

```json
[
  {
    "id": 1,
    "startAt": "10:00"
  },
  {
    "id": 2,
    "startAt": "11:00"
  }
]
```

#### 실패 예시

```json
{
  "code": "ERROR_CODE",
  "errorMessage": "에러 메시지"
}
```

</details>

### `POST /api/admin/times` 관리자 예약 시간 생성 API

<details>
<summary>세부 내용</summary>

### API 정보

```text
예약 시간을 생성합니다.
```

### 상황별 HTTP Code 및 에러 메시지

| 상황                      | HTTP Code       | 에러 메시지                | 문제 필드   |
|-------------------------|-----------------|-----------------------|---------|
| 요청 처리 성공                | 201 Created     | 없음                    | 없음      |
| 필드의 자료형이 올바르지 않는 경우     | 400 Bad Request | {필드명}의 형식이 유효하지 않습니다. | 필드명     |
| startAt의 형식이 유효하지 않은 경우 | 400 Bad Request | 예약 시간의 형식이 유효하지 않습니다. | startAt |

### Request Body

| 필드      | 타입   | 필수 여부 | 설명       | 예시    |
|---------|------|-------|----------|-------|
| startAt | Time | 필수    | 예약 시작 시간 | 20:30 |

#### 예시

```json
{
  "startAt": "20:30"
}
```

### Response Body

#### 성공 예시

```json
{
  "id": 1,
  "startAt": "20:30"
}
```

#### 실패 예시

```json
{
  "code": "TIME400",
  "errorMessage": "예약 시간의 형식이 유효하지 않습니다."
}
```

</details>

### `DELETE /api/admin/times/{id}` 관리자 예약 시간 삭제 API

<details>
<summary>세부 내용</summary>

### API 정보

```text
예약 시간 ID로 예약 시간을 삭제합니다.
```

### 상황별 HTTP Code 및 에러 메시지

| 상황                  | HTTP Code       | 에러 메시지             | 문제 필드 |
|---------------------|-----------------|--------------------|-------|
| 요청 처리 성공            | 200 OK          | 없음                 | 없음    |
| id의 자료형이 올바르지 않은 경우 | 400 Bad Request | id의 형식이 유효하지 않습니다. | id    |
| id가 존재하지 않는 경우      | 404 Not Found   | 존재 하지 않는 시간대입니다.   | id    |

### Path Variables

| 이름 | 타입     | 필수 여부 | 설명           | 예시 |
|----|--------|-------|--------------|----|
| id | Number | 필수    | 삭제할 예약 시간 ID | 1  |

### Request Body

없음

### Response Body

#### 성공 예시

```text
응답 본문 없음
```

#### 실패 예시

```json
{
  "code": "TIME404",
  "errorMessage": "존재 하지 않는 시간대입니다."
}
```

</details>

## Theme

### `GET /api/themes` 테마 목록 조회 API

<details>
<summary>세부 내용</summary>

### API 정보

```text
등록된 전체 테마를 조회합니다.
```

### 상황별 HTTP Code 및 에러 메시지

| 상황       | HTTP Code | 에러 메시지 | 문제 필드 |
|----------|-----------|--------|-------|
| 요청 처리 성공 | 200 OK    | 없음     | 없음    |

### Request Body

없음

### Response Body

#### 성공 예시

```json
[
  {
    "id": 1,
    "name": "링",
    "description": "이것은 링 방탈출 설명입니다.",
    "imageUrl": "https://roomescape.com/images/themes/ring.png"
  },
  {
    "id": 2,
    "name": "감옥",
    "description": "이것은 감옥 방탈출 설명입니다.",
    "imageUrl": "https://roomescape.com/images/themes/prison-room.png"
  }
]
```

#### 실패 예시

```json
{
  "code": "ERROR_CODE",
  "errorMessage": "에러 메시지"
}
```

</details>

### `GET /api/themes/rankings/last-7-days` 최근 7일 인기 테마 조회 API

<details>
<summary>세부 내용</summary>

### API 정보

```text
오늘을 제외한 직전 7일 동안 예약 수가 많은 테마를 조회합니다.
```

### 상황별 HTTP Code 및 에러 메시지

| 상황       | HTTP Code | 에러 메시지 | 문제 필드 |
|----------|-----------|--------|-------|
| 요청 처리 성공 | 200 OK    | 없음     | 없음    |

### Request Body

없음

### Response Body

#### 성공 예시

```json
[
  {
    "id": 1,
    "name": "링",
    "description": "이것은 링 방탈출 설명입니다.",
    "imageUrl": "https://roomescape.com/images/themes/ring.png"
  },
  {
    "id": 2,
    "name": "감옥",
    "description": "이것은 감옥 방탈출 설명입니다.",
    "imageUrl": "https://roomescape.com/images/themes/prison-room.png"
  }
]
```

#### 실패 예시

```json
{
  "code": "ERROR_CODE",
  "errorMessage": "에러 메시지"
}
```

</details>

### `POST /api/admin/themes` 관리자 테마 생성 API

<details>
<summary>세부 내용</summary>

### API 정보

```text
테마를 생성합니다.
```

### 상황별 HTTP Code 및 에러 메시지

| 상황                       | HTTP Code       | 에러 메시지                   | 문제 필드       |
|--------------------------|-----------------|--------------------------|-------------|
| 요청 처리 성공                 | 201 Created     | 없음                       | 없음          |
| 필드의 자료형이 올바르지 않는 경우      | 400 Bad Request | {필드명}의 형식이 유효하지 않습니다.    | 필드명         |
| name의 길이가 0이하, 20 초과인 경우 | 400 Bad Request | 테마 이름의 길이는 1이상 20이하 입니다. | name        |
| description이 비어있는 경우     | 400 Bad Request | 테마 설명은 필수입니다.            | description |
| imageUrl이 비어있는 경우        | 400 Bad Request | 테마 이미지 URL은 필수입니다.       | imageUrl    |

### Request Body

| 필드          | 타입     | 필수 여부 | 설명         | 예시                                                   |
|-------------|--------|-------|------------|------------------------------------------------------|
| name        | String | 필수    | 테마 이름      | 브라운                                                  |
| description | String | 필수    | 테마 설명      | 테마 설명                                                |
| imageUrl    | String | 필수    | 테마 이미지 URL | https://roomescape.com/images/themes/prison-room.png |

#### 예시

```json
{
  "name": "브라운",
  "description": "테마 설명",
  "imageUrl": "https://roomescape.com/images/themes/prison-room.png"
}
```

### Response Body

#### 성공 예시

```json
{
  "id": 1,
  "name": "브라운",
  "description": "테마 설명",
  "imageUrl": "https://roomescape.com/images/themes/prison-room.png"
}
```

#### 실패 예시

```json
{
  "code": "THEME400",
  "errorMessage": "테마 이름의 길이는 1이상 20이하 입니다."
}
```

</details>

### `DELETE /api/admin/themes/{id}` 관리자 테마 삭제 API

<details>
<summary>세부 내용</summary>

### API 정보

```text
테마 ID로 테마를 삭제합니다.
```

### 상황별 HTTP Code 및 에러 메시지

| 상황                  | HTTP Code       | 에러 메시지             | 문제 필드 |
|---------------------|-----------------|--------------------|-------|
| 요청 처리 성공            | 200 OK          | 없음                 | 없음    |
| id의 자료형이 올바르지 않은 경우 | 400 Bad Request | id의 형식이 유효하지 않습니다. | id    |
| id가 존재하지 않는 경우      | 404 Not Found   | 존재 하지 않는 테마입니다.    | id    |

### Path Variables

| 이름 | 타입     | 필수 여부 | 설명        | 예시 |
|----|--------|-------|-----------|----|
| id | Number | 필수    | 삭제할 테마 ID | 1  |

### Request Body

없음

### Response Body

#### 성공 예시

```text
응답 본문 없음
```

#### 실패 예시

```json
{
  "code": "THEME404",
  "errorMessage": "존재 하지 않는 테마입니다."
}
```

</details>

## 클라이언트 기능

### 공통(사용자)

#### 인기 테마 조회 기능

지난 1주간 등록된 예약 중 가장 많이 등록된 대상 테마를 조회합니다.
(예시: 2026년 5월 5일에 조회 -> 검색 범위는 2026년 4월 29일부터 2026년 5월 4일)

<img alt="인기 테마 조회" src="https://github.com/user-attachments/assets/da440d2b-318b-48ff-85fd-4b1b03cb1866" />

#### 전체 테마 조회 기능

등록된 모든 테마를 조회합니다.

<img alt="전체 테마 조회" src="https://github.com/user-attachments/assets/5a55dddc-7d70-4e64-9d01-0cca9b59d602" />

#### 예약 생성 기능

테마를 선택하고, 정해진 예약 폼에 맞춰 예약합니다.

<img alt="예약 생성" src="https://github.com/user-attachments/assets/18155750-0c47-4979-9aec-481afeab4b06" />

### 관리자

#### 예약 추가 기능

직접 예약을 추가합니다.

<img alt="관리자 예약 생성" src="https://github.com/user-attachments/assets/2b318338-bccd-4524-a1af-3dde857db0f4" />

#### 예약 관리 기능

전체 예약 내역을 관리합니다. (필터링, 정렬 기준 제공)

<img alt="관리자 예약 관리" src="https://github.com/user-attachments/assets/741f5147-0abe-4a9a-bf4c-81cd507d98bd" />

#### 테마 추가 기능

테마를 추가합니다.

<img alt="관리자 테마 추가" src="https://github.com/user-attachments/assets/f28fc0bf-a9bb-49ae-923f-c345504b0673" />

#### 테마 관리 기능

등록된 전체 테마를 관리합니다. (ID 순으로 정렬)

<img alt="관리자 테마 관리" src="https://github.com/user-attachments/assets/15da0dc8-03aa-452d-994f-3a50f7c26de4" />

#### 시간 추가 및 관리 기능

시간을 등록하고, 등록된 전체 시간을 관리합니다. (ID 순으로 정렬)

<img alt="관리자 시간 등록 및 관리" src="https://github.com/user-attachments/assets/68bf0fc6-ddf7-4661-a849-95b9f368ccaa" />
