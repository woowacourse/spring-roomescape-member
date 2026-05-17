# 상세 API 명세서

README의 API 요약 표에서 연결되는 상세 명세입니다.

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

| 상태 코드 | 메시지                | 설명                                              |
|:-----:|:-------------------|:------------------------------------------------|
| `400` | 요청 형식이 잘못되었습니다.    | 데이터 형식이 틀리거나 필수 파라미터가 누락된 경우, 참조 id가 존재하지 않는 경우 |
| `409` | 이미 존재하는 예약입니다.     | 같은 날짜, 시간 및 테마를 가진 예약이 존재하는 경우                  |
| `422` | 지난 예약은 접근할 수 없습니다. | 지난 날짜 및 시간에 예약을 시도하는 경우                         |

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

| 상태 코드 | 메시지                | 설명                                              |
|:-----:|:-------------------|:------------------------------------------------|
| `400` | 요청 형식이 잘못되었습니다.    | 데이터 형식이 틀리거나 필수 파라미터가 누락된 경우, 참조 id가 존재하지 않는 경우 |
| `409` | 이미 존재하는 예약입니다.     | 같은 날짜, 시간 및 테마를 가진 예약이 존재하는 경우                  |
| `422` | 지난 예약은 접근할 수 없습니다. | 지난 날짜 및 시간에 예약을 시도하는 경우                         |

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
| `400` | 요청 본문의 형식이 잘못되었습니다.   | 요청 본문 중 누락된 필드가 존재하는 경우, 참조 id가 존재하지 않는 경우      |
| `403` | 요청한 예약에 접근할 권한이 없습니다. | 다른 사용자가 예약한 데이터에 접근하는 경우                        |
| `404` | 요청한 예약을 찾을 수 없습니다.    | 요청한 예약이 아직 존재하지 않는 경우                           |
| `409` | 이미 존재하는 예약입니다.        | 변경하려는 날짜 또는 시간에 이미 예약이 존재하는 경우                  |
| `422` | 지난 예약은 접근할 수 없습니다.    | 과거의 예약 변경을 시도하는 경우                              |
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
| `404` | 요청한 예약을 찾을 수 없습니다.    | 요청한 예약이 아직 존재하지 않는 경우                           |
| `422` | 지난 예약은 접근할 수 없습니다.    | 지난 예약을 취소하려고 하는 경우                              |

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

| 상태 코드 | 메시지                   | 설명                                              |  
|:-----:|:----------------------|:------------------------------------------------|
| `400` | 요청 형식이 잘못되었습니다.       | 데이터 형식이 틀리거나 필수 파라미터가 누락된 경우, 테마 id가 존재하지 않는 경우 |  
| `422` | 지난 날짜로 시간 조회를 시도했습니다. | 이전 날짜로 조회하려고 하는 경우                              |  

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

| 상태 코드 | 메시지                        | 설명                           |  
|:-----:|:---------------------------|:-----------------------------|  
| `400` | 요청 형식이 잘못되었습니다.            | 데이터 형식이 틀리거나 필수 파라미터가 누락된 경우 |
| `400` | limit은 0 또는 양수여야 합니다.      | limit이 음수인 경우                |
| `422` | 시작 일자는 종료 일자 이전 시점이어야 합니다. | 시작 일자가 종료 일자보다 미래인 경우        |
