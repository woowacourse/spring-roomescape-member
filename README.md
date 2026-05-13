# 방탈출 예약 관리

## 🙋기능 요구사항

### 관리자 예약 관리
- 관리자는 예약을 예약자 이름, 예약 날짜, 예약 시간, 테마 정보를 입력해 예약을 추가할 수 있다
- 관리자는 전체 예약 목록을 조회할 수 있다
- 관리자는 특정 예약을 삭제할 수 있어야 한다.

### 관리자 예약 시간 관리
- 관리자는 시간을 입력해서 예약 시간을 등록할 수 있다.
- 관리자는 전체 예약 시간 목록을 조회할 수 있다.
- 관리자는 예약 시간을 삭제할 수 있다.

### 관리자 테마 관리
- 관리자는 테마의 이름, 설명, 썸네일 이미지 URL를 입력하여 등록할 수 있다.
- 관리자는 전체 테마 목록을 조회할 수 있다.
- 관리자는 특정 테마를 삭제할 수 있다.

### 사용자 예약
- 사용자는 특정 날짜 및 테마의 예약 가능한 시간을 조회할 수 있다.
- 사용지는 예약자 이름, 예약 날짜, 예약 시간, 테마 정보를 입력해서 예약을 할 수 있다.

### 예약 정책
- 지나간 날짜와 시간에 대한 예약은 생성할 수 없다.
- 같은 날짜, 시간, 테마에 이미 예약이 존재하면 예약을 생성할 수 없다.
- 예약이 존재하는 예약 시간은 삭제할 수 없다.
- 빈 이름, 잘못된 날짜/시간 형식, 필수값 누락 등 유효하지 않은 입력값은 거부한다.

### 인기 테마
- 사용자는 최근 1주일 동안 예약이 많았던 테마 상위 10개를 확인할 수 있어야 한다.

## 🌐 API 명세

### 공통 에러 응답

**Response**

```
Content-Type: application/json

{
  "message": "에러 메시지"
}
```

| statusCode | 상황 |
| --- | --- |
| 400 Bad Request | 요청 본문, 쿼리 파라미터, path variable의 형식이 잘못된 경우, 필수값이 누락된 경우, 빈 문자열처럼 유효하지 않은 값이 전달된 경우, 지나간 날짜와 시간으로 예약을 요청한 경우 |
| 404 Not Found | 요청에서 조회, 삭제, 참조한 예약, 예약 시간 또는 테마가 존재하지 않는 경우 |
| 409 Conflict | 이미 존재하는 자원을 생성하려는 경우, 같은 날짜+시간+테마에 예약이 이미 존재하는 경우, 예약이 존재하는 시간 또는 테마를 삭제하려는 경우 |
| 500 Internal Server Error | 서버 내부 오류가 발생한 경우 |

### 예약 API

#### 예약 추가 API

**Request**

```
POST /reservations HTTP/1.1
content-type: application/json

{
    "date": "2023-08-05",
    "name": "브라운",
    "timeId": 1,
    "themeId": 1
}

```

**Response**

```
HTTP/1.1 201
Content-Type: application/json

{
    "id": 1,
    "name": "브라운",
    "date": "2023-08-05",
    "time" : {
        "id": 1,
        "startAt" : "10:00"
    },
    "theme" : {
        "id": 1,
        "name": "레벨2 탈출",
        "description": "우테코 레벨2를 탈출하는 내용입니다.",
        "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
    }
}

```

**Status Code**

| statusCode | 상황 |
| --- | --- |
| 201 Created | 예약 생성 성공 |
| 400 Bad Request | 예약자 이름이 비어 있거나 255자를 초과한 경우, 날짜 형식이 `yyyy-MM-dd`가 아닌 경우, 필수값이 누락된 경우, 지나간 날짜와 시간에 대한 예약인 경우 |
| 404 Not Found | `timeId` 또는 `themeId`에 해당하는 예약 시간 또는 테마가 존재하지 않는 경우 |
| 409 Conflict | 같은 날짜, 시간, 테마에 이미 예약이 존재하는 경우 |

#### 예약 조회 API

**Request**

```
GET /admin/reservations HTTP/1.1
```

**Response**

```
{
  "reservations": [
    {
      "id": 1,
      "name": "브라운",
      "date": "2023-08-05",
      "time": {
        "id": 1,
        "startAt": "10:00"
      },
      "theme": {
        "id": 1,
        "name": "레벨2 탈출",
        "description": "우테코 레벨2를 탈출하는 내용입니다.",
        "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
      }
    }, ...
  ]
}
```

**Status Code**

| statusCode | 상황 |
| --- | --- |
| 200 OK | 예약 목록 조회 성공 |

#### 예약 삭제 API

**Request**

```
DELETE /admin/reservations/1 HTTP/1.1
```

**Response**

```
HTTP/1.1 204
```

**Status Code**

| statusCode | 상황 |
| --- | --- |
| 204 No Content | 예약 삭제 성공 |
| 404 Not Found | 삭제하려는 예약이 존재하지 않는 경우 |

### 시간 API

#### 시간 추가 API

**Request**

```
POST /admin/times HTTP/1.1
content-type: application/json

{
    "startAt": "10:00"
}
```

**Response**

```
HTTP/1.1 201
Content-Type: application/json

{
    "id": 1,
    "startAt": "10:00"
}
```

**Status Code**

| statusCode | 상황 |
| --- | --- |
| 201 Created | 예약 시간 생성 성공 |
| 400 Bad Request | 예약 시간이 비어 있거나 시간 형식이 `HH:mm`이 아닌 경우 |
| 409 Conflict | 같은 시작 시간의 예약 시간이 이미 존재하는 경우 |

#### 시간 조회 API

**Request**

```
GET /times HTTP/1.1
```

**Response**

```
HTTP/1.1 200 
Content-Type: application/json

{
  "times": [
    {
      "id": 1,
      "startAt": "10:00"
    }
  ]
}
```

**Status Code**

| statusCode | 상황 |
| --- | --- |
| 200 OK | 예약 시간 목록 조회 성공 |

#### 시간 삭제 API

**Request**

```
DELETE /admin/times/1 HTTP/1.1
```

**Response**

```
HTTP/1.1 204
```

**Status Code**

| statusCode | 상황 |
| --- | --- |
| 204 No Content | 예약 시간 삭제 성공 |
| 404 Not Found | 삭제하려는 예약 시간이 존재하지 않는 경우 |
| 409 Conflict | 해당 예약 시간에 연결된 예약이 존재하는 경우 |

#### 예약 가능한 시간 조회 API

**Request**
```
GET /times/availability?date=2026-05-04&themeId=1 HTTP/1.1
```

**Response**

```
HTTP/1.1 200
Content-Type: application/json

{
  "availableTimes": [
    {
      "id": 1,
      "startAt": "10:00",
      "isAvailable" : true
    }
  ]
}
```

**Status Code**

| statusCode | 상황 |
| --- | --- |
| 200 OK | 예약 가능 시간 조회 성공 |
| 400 Bad Request | `date` 형식이 `yyyy-MM-dd`가 아니거나 필수 쿼리 파라미터가 누락된 경우 |
| 404 Not Found | `themeId`에 해당하는 테마가 존재하지 않는 경우 |

### 테마 API

#### 테마 추가

**Request**

```
POST /admin/themes HTTP/1.1
content-type: application/json

{
    "name": "공포",
    "description" : "무서움",
    "thumbnail" : "url"
}
```

**Response**

```
HTTP/1.1 201
Content-Type: application/json

{
    "id": 1,
    "name": "공포",
    "description" : "무서움",
    "thumbnail" : "url"
}
```

**Status Code**

| statusCode | 상황 |
| --- | --- |
| 201 Created | 테마 생성 성공 |
| 400 Bad Request | 이름, 설명, 썸네일이 비어 있거나 길이 제한을 초과한 경우 |
| 409 Conflict | 같은 이름의 테마가 이미 존재하는 경우 |

#### 테마 목록 조회

**Request**

```
GET /themes HTTP/1.1
```

**Response**

```
HTTP/1.1 200
Content-Type: application/json

{
  "themes": [
    {
    "id": 1,
    "name": "공포",
    "description" : "무서움",
    "thumbnail" : "url"
	}
  ]
}
```

**Status Code**

| statusCode | 상황 |
| --- | --- |
| 200 OK | 테마 목록 조회 성공 |

#### 테마 삭제

**Request**

```
DELETE /admin/themes/1 HTTP/1.1
```

**Response**

```
HTTP/1.1 204
```

**Status Code**

| statusCode | 상황 |
| --- | --- |
| 204 No Content | 테마 삭제 성공 |
| 404 Not Found | 삭제하려는 테마가 존재하지 않는 경우 |
| 409 Conflict | 해당 테마에 연결된 예약이 존재하는 경우 |

#### 인기 테마 조회

**Request**

```
GET /themes/popularity?days=7&size=10 HTTP/1.1
```

**Response**

```
HTTP/1.1 200
Content-Type: application/json

{
  "themes": [
    {
    "id": 1,
    "name": "공포",
    "description" : "무서움",
    "thumbnail" : "url"
	}
  ]
}
```

**Status Code**

| statusCode | 상황 |
| --- | --- |
| 200 OK | 인기 테마 조회 성공 |
| 400 Bad Request | `days` 또는 `size`가 양수가 아니거나 필수 쿼리 파라미터가 누락된 경우 |

## 🖥️ 프론트엔드 페이지

### 사용자 예약 페이지

- 경로: `http://localhost:8080`
- 사용자는 인기 테마를 확인하고, 날짜와 테마를 선택해 예약 가능한 시간을 조회할 수 있다.
- 사용자는 예약 가능한 시간을 선택한 뒤 이름을 입력해 예약할 수 있다.

### 관리자 페이지

- 경로: `http://localhost:8080/admin`
- 관리자는 사용자 예약 화면과 같은 흐름으로 날짜, 테마, 예약 가능한 시간, 이름을 입력해 예약을 등록할 수 있다.
- 관리자는 예약 목록을 조회하고 예약을 삭제할 수 있다.
- 관리자는 예약 시간과 테마를 등록하거나 삭제할 수 있다.
