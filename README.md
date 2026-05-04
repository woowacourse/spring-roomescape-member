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

### 인기 테마
- 사용자는 최근 1주일 동안 예약이 많았던 테마 상위 10개를 확인할 수 있어야 한다.

## 🌐 API 명세

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
Location: /reservations/1
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

#### 예약 조회 API

**Request**

```
GET /reservations HTTP/1.1
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

#### 예약 취소 API

**Request**

```
DELETE /reservations/1 HTTP/1.1
```

**Response**

```
HTTP/1.1 204
```

### 시간 API

#### 시간 추가 API

**Request**

```
POST /times HTTP/1.1
content-type: application/json

{
    "startAt": "10:00"
}
```

**Response**

```
HTTP/1.1 201
Location: /times/1
Content-Type: application/json

{
    "id": 1,
    "startAt": "10:00"
}
```

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

#### 시간 삭제 API

**Request**

```
DELETE /times/1 HTTP/1.1
```

**Response**

```
HTTP/1.1 204
```

#### 예약 가능한 시간 조회 API

**Request**
```
GET /times/availabilty?date="2026-05-04"&themeId=1 HTTP/1.1
```

**Response**

```
HTTP/1.1 200
Content-Type: application/json

{
  "availableTimes": [
    {
      "id": 1,
      "startAt": "10:00"
      "isAvailable" : true
    }
  ]
}
```

### 테마 API

#### 테마 추가

**Request**

```
POST /themes HTTP/1.1
content-type: application/json

{
    "name": "공포"
    "description" : "무서움"
    "thumbnail" : "url"
}
```

**Response**

```
HTTP/1.1 201
Content-Type: application/json

{
    "id": 1,
    "name": "공포"
    "description" : "무서움"
    "thumbnail" : "url"
}
```

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
    "name": "공포"
    "description" : "무서움"
    "thumbnail" : "url"
		}
  ]
}
```

#### 테마 삭제

**Request**

```
DELETE /themes/1 HTTP/1.1
```

**Response**

```
HTTP/1.1 204
```

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
  "popularThemes": [
    {
    "id": 1,
    "name": "공포"
    "description" : "무서움"
    "thumbnail" : "url"
		}
  ]
}
```

