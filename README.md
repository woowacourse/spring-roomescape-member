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

| 파라미터명     | 필수 여부   | 타입     | 설명        | 예시         |
|-----------|---------|--------|-----------|------------|
| startDate | 필수      | Date   | 조회 시작 날짜  | 2026-05-03 |
| endDate   | 필수      | Date   | 조회 종료 날짜  | 2026-05-10 |
| limit     | 필수      | BigInt | 조회할 테마 개수 | 10         |

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