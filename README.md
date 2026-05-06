<p align="center">
    <img src="https://github.com/user-attachments/assets/eb5a8839-ed5b-453b-a764-85ffcc9d0219"/>
</p>

<h1 align="middle">방탈출 예약 서비스</h1>

![Static Badge](https://img.shields.io/badge/Java-21-green)
![Static Badge](https://img.shields.io/badge/Version-1.0.0-red)

우아한테크코스 BE 레벨 2 방탈출 예약 서비스를 구현한 저장소입니다.

## 목차

- [API 명세서](#api-명세서)
    - [예약 생성](#예약-생성)
    - [관리자 예약 조회](#관리자-예약-조회)
    - [관리자 예약 삭제](#관리자-예약-삭제)
    - [관리자 시간 조회](#관리자-시간-조회)
    - [관리자 시간 생성](#관리자-시간-생성)
    - [관리자 시간 삭제](#관리자-시간-삭제)
    - [관리자 테마 추가](#관리자-테마-추가)
    - [관리자 테마 삭제](#관리자-테마-삭제)
    - [사용자 테마 조회](#사용자-테마-조회)
    - [사용자 예약 가능한 시간 조회](#사용자-예약-가능한-시간-조회)
    - [사용자 예약 생성](#사용자-예약-생성)
    - [사용자 인기 테마 조회](#사용자-인기-테마-조회)
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

## API 명세서

### 예약 생성

#### URL

```http
POST /api/reservations
```

#### Query Parameters

없음

#### Request Body

```json
{
  "name": "서여",
  "date": "2026-05-04",
  "timeId": 1,
  "themeId": 1
}
```

#### Response Body

##### 201 Created

```json
{
  "id": 1,
  "name": "서여",
  "date": "2026-05-04",
  "timeId": 1,
  "themeId": 1
}
```

### 관리자 예약 조회

#### URL

```http
GET /api/admin/reservations
```

#### Query Parameters

없음

#### Request Body

없음

#### Response Body

##### 200 OK

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
  },
  {
    "id": 2,
    "name": "네오",
    "date": "2026-05-06",
    "time": {
      "id": 2,
      "startAt": "12:00"
    },
    "theme": {
      "id": 1,
      "name": "피온피온",
      "description": "설명",
      "imageUrl": "https://roomescape.com/images/themes/prison-banner.png"
    }
  }
]
```

### 관리자 예약 삭제

#### URL

```http
DELETE /api/admin/reservations/{id}
```

#### Path Variables

| 파라미터명 | 필수 여부 | 타입     | 설명        | 예시 |
|-------|-------|--------|-----------|----|
| id    | 필수    | Number | 삭제할 예약 ID | 1  |

#### Query Parameters

없음

#### Request Body

없음

#### Response Body

##### 200 OK

없음

### 관리자 시간 조회

#### URL

```http
GET /api/admin/times
```

#### Query Parameters

없음

#### Request Body

없음

#### Response Body

##### 200 OK

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

### 관리자 시간 생성

#### URL

```http
POST /api/admin/times
```

#### Query Parameters

없음

#### Request Body

```json
{
  "startAt": "20:30"
}
```

#### Response Body

##### 201 Created

```json
{
  "id": 1,
  "startAt": "20:30"
}
```

### 관리자 시간 삭제

#### URL

```http
DELETE /api/admin/times/{id}
```

#### Path Variables

| 파라미터명 | 필수 여부 | 타입     | 설명        | 예시 |
|-------|-------|--------|-----------|----|
| id    | 필수    | Number | 삭제할 시간 ID | 1  |

#### Query Parameters

없음

#### Request Body

없음

#### Response Body

##### 200 OK

없음

### 관리자 테마 추가

#### URL

```http
POST /api/admin/themes
```

#### Query Parameters

없음

#### Request Body

```json
{
  "name": "브라운",
  "description": "테마 설명",
  "imageUrl": "https://roomescape.com/images/themes/prison-room.png"
}
```

#### Response Body

##### 201 Created

```json
{
  "id": 1,
  "name": "브라운",
  "description": "테마 설명",
  "imageUrl": "https://roomescape.com/images/themes/prison-room.png"
}
```

### 관리자 테마 삭제

#### URL

```http
DELETE /api/admin/themes/{id}
```

#### Path Variables

| 파라미터명 | 필수 여부 | 타입     | 설명        | 예시 |
|-------|-------|--------|-----------|----|
| id    | 필수    | Number | 삭제할 테마 ID | 1  |

#### Query Parameters

없음

#### Request Body

없음

#### Response Body

##### 200 OK

없음

### 사용자 테마 조회

#### URL

```http
GET /api/themes
```

#### Query Parameters

없음

#### Request Body

없음

#### Response Body

##### 200 OK

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

### 사용자 예약 가능한 시간 조회

#### URL

```http
GET /api/times?date={date}&themeId={themeId}
```

#### Query Parameters

| 파라미터명   | 필수 여부 | 타입     | 설명        | 예시         |
|---------|-------|--------|-----------|------------|
| date    | 필수    | Date   | 예약할 날짜    | 2026-05-12 |
| themeId | 필수    | BigInt | 예약할 테마 ID | 1          |

#### Request Body

없음

#### Response Body

##### 200 OK

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

### 사용자 예약 생성

#### URL

```http
POST /api/reservations
```

#### Query Parameters

없음

#### Request Body

```json
{
  "name": "브라운",
  "date": "2026-05-12",
  "timeId": 1,
  "themeId": 1
}
```

#### Response Body

##### 200 OK

```json
{
  "id": 1,
  "name": "브라운",
  "date": "2026-05-12",
  "timeId": 1,
  "themeId": 1
}
```

### 사용자 인기 테마 조회

#### URL

```http
GET /themes/popular?startDate={startDate}&endDate={endDate}&limit={limit}
```

#### Query Parameters

| 파라미터명     | 필수 여부 | 타입     | 설명        | 예시         |
|-----------|-------|--------|-----------|------------|
| startDate | 필수    | Date   | 조회 시작 날짜  | 2026-05-03 |
| endDate   | 필수    | Date   | 조회 종료 날짜  | 2026-05-10 |
| limit     | 필수    | BigInt | 조회할 테마 개수 | 10         |

#### Request Body

없음

#### Response Body

##### 200 OK

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
