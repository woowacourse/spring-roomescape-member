<p align="center">
    <img alt="프로젝트 썸네일 이미지" src="https://github.com/user-attachments/assets/eb5a8839-ed5b-453b-a764-85ffcc9d0219"/>
</p>

<h1 align="middle">방탈출 예약 서비스</h1>

![Static Badge](https://img.shields.io/badge/Java-21-green)
![Static Badge](https://img.shields.io/badge/Version-1.0.0-red)

우아한테크코스 BE 레벨 2 방탈출 예약 서비스를 구현한 저장소입니다.

## 목차

- [API 명세서](#api-명세서)
    - [관리자 예약 생성](#관리자-예약-생성)
    - [관리자 예약 조회](#관리자-예약-조회)
    - [관리자 예약 삭제](#관리자-예약-삭제)
    - [관리자 시간 조회](#관리자-시간-조회)
    - [관리자 시간 생성](#관리자-시간-생성)
    - [관리자 시간 삭제](#관리자-시간-삭제)
    - [관리자 테마 추가](#관리자-테마-추가)
    - [관리자 테마 삭제](#관리자-테마-삭제)
    - [사용자 테마 조회](#사용자-테마-조회)
    - [사용자 예약 조회](#사용자-예약-조회)
    - [사용자 예약 생성](#사용자-예약-생성)
    - [사용자 예약 수정](#사용자-예약-수정)
    - [사용자 예약 취소](#사용자-예약-취소)
    - [사용자 예약 가능한 시간 조회](#사용자-예약-가능한-시간-조회)
    - [사용자 인기 테마 조회](#사용자-인기-테마-조회)
- [클라이언트 기능](#클라이언트-기능)
    - [공통(사용자)](#공통\(사용자\))
        - [인기 테마 조회 기능](#인기-테마-조회-기능)
        - [전체 테마 조회 기능](#전체-테마-조회-기능)
        - [예약 생성 기능](#예약-생성-기능)
    - [관리자](#관리자)
        - [예약 추가 기능](#예약-추가-기능)
        - [예약 관리 기능](#예약-관리-기능)
        - [테마 관리 기능](#테마-관리-기능)
        - [시간 추가 및 관리 기능](#시간-추가-및-관리-기능)

## API 명세서

### 관리자 예약 생성

#### URL

```http
POST /api/admin/reservations
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

#### Response Body - Success

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

#### Response Body - Failure

##### Example

```json
{
  "timestamp": "2026-05-11T18:39:29",
  "message": "요청 형식이 잘못되었습니다.",
  "errors": [
    {
      "field": "id",
      "value": "a",
      "message": "id는 정수여야 합니다."
    }
  ]
}
```

##### Error Responses

| 상태 코드 | 메시지                     | 설명                                              |
|:-----:|:------------------------|:------------------------------------------------|
| `400` | 요청 형식이 잘못되었습니다.         | 데이터 형식이 틀리거나 필수 파라미터가 누락된 경우, 참조 id가 존재하지 않는 경우 |
| `409` | 이미 존재하는 예약입니다.          | 같은 날짜, 시간 및 테마를 가진 예약이 존재하는 경우                  |
| `422` | 지난 날짜 및 시간에 예약을 시도했습니다. | 지난 날짜 및 시간에 예약을 시도하는 경우                         |

### 관리자 예약 조회

#### URL

```http
GET /api/admin/reservations
```

#### Query Parameters

없음

#### Request Body

없음

#### Response Body - Success

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

None

#### Request Body

None

#### Response Body - Success

##### 204 No Content

None

#### Error Responses

| 상태 코드 | 메시지                | 설명                           |  
|:-----:|:-------------------|:-----------------------------|  
| `400` | 요청 형식이 잘못되었습니다.    | 데이터 형식이 틀리거나 필수 파라미터가 누락된 경우 |  
| `404` | 요청한 예약을 찾을 수 없습니다. | 존재하지 않는 리소스 삭제를 시도하는 경우      |  

### 관리자 시간 조회

#### URL

```http
GET /api/admin/times
```

#### Query Parameters

없음

#### Request Body

없음

#### Response Body - Success

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

#### Response Body - Success

##### 201 Created

```json
{
  "id": 1,
  "startAt": "20:30"
}
```

#### Response Body - Failure

| 상태 코드 | 메시지             | 설명                           |  
|:-----:|:----------------|:-----------------------------|  
| `400` | 요청 형식이 잘못되었습니다. | 데이터 형식이 틀리거나 필수 파라미터가 누락된 경우 |  
| `409` | 이미 존재하는 시간입니다.  | 이미 저장된 시간인 경우                |  

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

#### Response Body - Success

##### 204 No Content

없음

#### Error Responses

| 상태 코드 | 메시지                     | 설명                           |  
|:-----:|:------------------------|:-----------------------------|  
| `400` | 요청 형식이 잘못되었습니다.         | 데이터 형식이 틀리거나 필수 파라미터가 누락된 경우 |
| `404` | 요청한 시간을 찾을 수 없습니다.      | 요청한 시간이 존재하지 않는 경우           |  
| `409` | 요청한 시간을 참조하는 예약이 존재합니다. | 요청한 시간을 다른 예약이 참조하고 있는 경우    |  

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
  "name": "테마명",
  "description": "테마 설명",
  "imageUrl": "https://roomescape.com/images/themes/prison-room.png"
}
```

#### Response Body - Success

##### 201 Created

```json
{
  "id": 1,
  "name": "테마명",
  "description": "테마 설명",
  "imageUrl": "https://roomescape.com/images/themes/prison-room.png"
}
```

#### Response Body - Failure

| 상태 코드 | 메시지             | 설명                           |  
|:-----:|:----------------|:-----------------------------|  
| `400` | 요청 형식이 잘못되었습니다. | 데이터 형식이 틀리거나 필수 파라미터가 누락된 경우 |  
| `409` | 이미 존재하는 테마입니다.  | 같은 이름을 가진 테마가 존재하는 경우        |  

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

#### Response Body - Success

##### 204 No Content

없음

#### Response Body - Failure

| 상태 코드 | 메시지                     | 설명                           |  
|:-----:|:------------------------|:-----------------------------|  
| `400` | 요청 형식이 잘못되었습니다.         | 데이터 형식이 틀리거나 필수 파라미터가 누락된 경우 |  
| `404` | 요청한 테마를 찾을 수 없습니다.      | 요청한 테마가 존재하지 않는 경우           |  
| `409` | 요청한 테마를 참조하는 예약이 존재합니다. | 요청한 테마를 다른 예약이 참조하고 있는 경우    |

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

### 사용자 예약 조회

#### URL

```http
GET /api/reservations
```

#### Headers

|        헤더명         | 타입     | 설명         | 필수 여부 |
|:------------------:|:-------|:-----------|:------|
| `Reservation-Name` | String | 요청한 사용자 이름 | O     |

#### Query Parameters

없음

#### Request Body

없음

#### Response Body - Success

##### 200 OK

```json
[
  {
    "id": 1,
    "name": "브라운",
    "date": "2026-05-12",
    "time": {
      "id": 1,
      "startAt": "20:30"
    },
    "theme": {
      "id": 1,
      "name": "링",
      "description": "이것은 링 방탈출 설명입니다.",
      "imageUrl": "https://roomescape.com/images/themes/ring.png"
    }
  },
  {
    "id": 2,
    "name": "브라운",
    "date": "2026-05-13",
    "time": {
      "id": 2,
      "startAt": "22:30"
    },
    "theme": {
      "id": 2,
      "name": "감옥",
      "description": "이것은 감옥 방탈출 설명입니다.",
      "imageUrl": "https://roomescape.com/images/themes/prison-room.png"
    }
  }
]
```

#### Error Responses

| 상태 코드 | 메시지             | 설명                           |
|:-----:|:----------------|:-----------------------------|
| `400` | 요청 형식이 잘못되었습니다. | 데이터 형식이 틀리거나 필수 파라미터가 누락된 경우 |

### 사용자 예약 생성

#### URL

```http
POST /api/reservations
```

#### Query Parameters

없음

#### Request

- Body

```json
{
  "name": "브라운",
  "date": "2026-05-12",
  "timeId": 1,
  "themeId": 1
}
```

#### Response Body - Success

##### 201 Created

```json
{
  "id": 1,
  "name": "브라운",
  "date": "2026-05-12",
  "timeId": 1,
  "themeId": 1
}
```

#### Error Responses

| 상태 코드 | 메시지                        | 설명                                              |
|:-----:|:---------------------------|:------------------------------------------------|
| `400` | 요청 형식이 잘못되었습니다.            | 데이터 형식이 틀리거나 필수 파라미터가 누락된 경우, 참조 id가 존재하지 않는 경우 |
| `409` | 이미 존재하는 예약입니다.             | 같은 날짜, 시간 및 테마를 가진 예약이 존재하는 경우                  |
| `422` | 지난 날짜 및 시간에는 예약을 할 수 없습니다. | 지난 날짜 및 시간에 예약을 시도하는 경우                         |

### 사용자 예약 수정

#### URL

```http
PATCH /api/reservations/{id}
```

#### Headers

|        헤더명         | 타입     | 설명         | 필수 여부 |
|:------------------:|:-------|:-----------|:------|
| `Reservation-Name` | String | 요청한 사용자 이름 | O     |

#### Query Parameters

없음

#### Request Body

```json
{
  "date": "2026-05-12",
  "timeId": 1
}
```

#### Response Body - Success

##### 204 No Content

None

#### Error Responses

| 상태 코드 | 메시지                   | 설명                                              |
|:-----:|:----------------------|:------------------------------------------------|
| `400` | 요청 형식이 잘못되었습니다.       | 데이터 형식이 틀리거나 필수 파라미터가 누락된 경우, 참조 id가 존재하지 않는 경우 |
| `403` | 요청한 예약에 접근할 권한이 없습니다. | 다른 사용자가 예약한 데이터에 접근하는 경우                        |
| `404` | 요청한 예약이 존재하지 않습니다.    | 요청한 예약이 아직 존재하지 않는 경우                           |
| `409` | 이미 존재하는 예약입니다.        | 변경하려는 날짜 또는 시간에 이미 예약이 존재하는 경우                  |
| `422` | 지난 예약은 변경할 수 없습니다.    | 과거의 예약 변경을 시도하는 경우                              |
| `422` | 지난 시점으로는 변경할 수 없습니다.  | 과거 날짜 및 시간으로 변경하려는 경우                           |

### 사용자 예약 취소

#### URL

```http
DELETE /api/reservations/{id}
```

#### Headers

|        헤더명         | 타입     | 설명         | 필수 여부 |
|:------------------:|:-------|:-----------|:------|
| `Reservation-Name` | String | 요청한 사용자 이름 | O     |

#### Query Parameters

없음

#### Request Body

None

#### Response Body - Success

##### 204 No Content

None

#### Error Responses

| 상태 코드 | 메시지                   | 설명                                              |
|:-----:|:----------------------|:------------------------------------------------|
| `400` | 요청 형식이 잘못되었습니다.       | 데이터 형식이 틀리거나 필수 파라미터가 누락된 경우, 참조 id가 존재하지 않는 경우 |
| `403` | 요청한 예약에 접근할 권한이 없습니다. | 다른 사용자가 예약한 데이터에 접근하는 경우                        |
| `404` | 요청한 예약이 존재하지 않습니다.    | 요청한 예약이 아직 존재하지 않는 경우                           |

### 사용자 예약 가능한 시간 조회

#### URL

```http
GET /api/times?date={date}&themeId={themeId}
```

#### Query Parameters

| 파라미터명   | 필수 여부 | 타입     | 설명        | 예시         |
|---------|-------|--------|-----------|------------|
| date    | O     | Date   | 예약할 날짜    | 2026-05-12 |
| themeId | O     | BigInt | 예약할 테마 ID | 1          |

#### Request Body

없음

#### Response Body - Success

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

#### Error Responses

| 상태 코드 | 메시지                         | 설명                                              |  
|:-----:|:----------------------------|:------------------------------------------------|
| `400` | 요청 형식이 잘못되었습니다.             | 데이터 형식이 틀리거나 필수 파라미터가 누락된 경우, 테마 id가 존재하지 않는 경우 |  
| `422` | 비즈니스 상의 이유로 요청을 수행할 수 없습니다. | 이전 날짜로 조회하려고 하는 경우                              |  

### 사용자 인기 테마 조회

#### URL

```http
GET /api/themes/popular?startDate={startDate}&endDate={endDate}&limit={limit}
```

#### Query Parameters

| 파라미터명     | 필수 여부 | 타입     | 설명        | 예시         |
|-----------|-------|--------|-----------|------------|
| startDate | O     | Date   | 조회 시작 날짜  | 2026-05-03 |
| endDate   | O     | Date   | 조회 종료 날짜  | 2026-05-10 |
| limit     | O     | BigInt | 조회할 테마 개수 | 10         |

#### Request Body

없음

#### Response Body - Success

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

#### Error Responses

| 상태 코드 | 메시지                         | 설명                           |  
|:-----:|:----------------------------|:-----------------------------|  
| `400` | 요청 형식이 잘못되었습니다.             | 데이터 형식이 틀리거나 필수 파라미터가 누락된 경우 |
| `422` | 비즈니스 상의 이유로 요청을 수행할 수 없습니다. | 시작 일자가 종료 일자보다 미래인 경우        |

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
