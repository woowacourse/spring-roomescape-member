## 예외 처리 목록
- 도메인 필드에 null, blank 를 검즘한다.
  - [x] Reservation
  - [x] Reservation Time
  - [x] Theme
- ReservationService
  - [x] 지나간 날짜/시간에 대한 예악은 불가능하다.
  - [x] 해당 날짜/시간에 이미 예약이 존재한다면 예약을 생성할 수 없다.
  - [x] 존재하지 않는 id의 예약 삭제는 불가능하다.
- ReservationTimeService
  - [x] 이미 존재하는 예약 시간이 있다면 생성할 수 없다.
  - [x] 존재하지 않는 id의 예약 시간 삭제는 불가능하다.
  - [x] 예약 시간 삭제 시 해당 시간을 사용하는 예약이 존재한다면 삭제할 수 없다.
- ThemeService
  - [x] 존재하지 않는 id의 테마 삭제는 불가능하다.
  - [x] 동일한 이름의 테마는 생성할 수 없다.
  - [x] 테마 삭제 시 해당 테마를 사용하는 예약이 존재한다면 삭제할 수 없다.

## API 명세서

### 예약

<details>
<summary>POST <code>/reservations</code> 예약 추가 API</summary>

#### Request

```http
POST /reservations HTTP/1.1
content-type: application/json

{
    "name": "브라운",
    "date": "2023-08-05",
    "timeId": 1,
    "themeId": 1
}
```

#### Response

```http
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
        "name": "테마1",
        "thumbnail": "https://example.com/thumbnail.jpg"
    }
}
```

</details>

<details>
<summary>GET <code>/reservations</code> 전체 예약 조회 API</summary>

#### Request

```http
GET /reservations HTTP/1.1
```

#### Response

```http
HTTP/1.1 200
Content-Type: application/json

[
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
        "name": "테마1",
        "thumbnail": "https://example.com/thumbnail.jpg"
    }
  }
]
```

</details>

<details>
<summary>DELETE <code>/reservations/{id}</code> 예약 삭제 API</summary>

#### Request

```http
DELETE /reservations/1 HTTP/1.1
```

#### Response

```http
HTTP/1.1 204
```

</details>

### 예약 시간

<details>
<summary>POST <code>/times</code> 예약 시간 추가 API</summary>

#### Request

```http
POST /times HTTP/1.1
content-type: application/json

{
    "startAt": "10:00"
}
```

#### Response

```http
HTTP/1.1 201
Content-Type: application/json

{
    "id": 1,
    "startAt": "10:00"
}
```

</details>

<details>
<summary>GET <code>/times</code> 전체 예약 시간 조회 API</summary>

#### Request

```http
GET /times HTTP/1.1
```

#### Response

```http
HTTP/1.1 200 
Content-Type: application/json

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

</details>

<details>
<summary>DELETE <code>/times/{id}</code> 예약 시간 삭제 API</summary>

#### Request

```http
DELETE /times/1 HTTP/1.1
```

#### Response

```http
HTTP/1.1 204
```

</details>

<details>
<summary>GET <code>/times/available</code> 이용 가능한 시간 조회 API</summary>

#### Request

```http
GET /times/available?date={date}&themeId={themeId} HTTP/1.1

date: (필수) 예약 가능한 시간을 조회할 날짜 (형식: YYYY-MM-DD)
themeId: (필수) 조회할 테마의 ID
```

#### Response

```http
HTTP/1.1 200

[
    {
        "timeId": 1,
        "startAt": "10:00"
        "alreadyBooked": false
    },
    {
        "timeId": 2,
        "startAt": "10:40"
        "alreadyBooked": true
    }
]
```

</details>

### 테마

<details>
<summary>POST <code>/themes</code> 테마 추가 API</summary>

#### Request

```http
POST /themes HTTP/1.1
content-type: application/json

{
    "name": "테마1",
    "description": "테마1 설명",
    "thumbnail": "https://example.com/thumbnail.jpg"
}
```

#### Response

```http
HTTP/1.1 201
Content-Type: application/json

{
    "id": 1,
    "name": "테마1",
    "description": "테마1 설명",
    "thumbnail": "https://example.com/thumbnail.jpg"
}
```

</details>

<details>
<summary>GET <code>/themes</code> 전체 테마 조회 API</summary>

#### Request

```http
GET /themes HTTP/1.1
```

#### Response

```http
HTTP/1.1 200
Content-Type: application/json

[
    {
        "id": 1,
        "name": "테마1",
        "description": "테마1 설명",
        "thumbnail": "https://example.com/thumbnail.jpg"
    },
    {
        "id": 2,
        "name": "테마2",
        "description": "테마2 설명",
        "thumbnail": "https://example.com/thumbnail-2.jpg"
    }
]
```

</details>

<details>
<summary>DELETE <code>/themes/{id}</code> 테마 삭제 API</summary>

#### Request

```http
DELETE /themes/1 HTTP/1.1
```

#### Response

```http
HTTP/1.1 204
```

</details>

<details>
<summary>GET <code>/themes/popular</code> 인기 테마 조회 API</summary>

#### Request

```http
GET /themes/popular?startDate={startDate}&endDate={endDate}&limit={limit} HTTP/1.1
```

시작날짜와 끝날짜를 입력받아 해당 기간 동안 예약이 가장 많은 테마 10개를 조회한다.

startDate: (required=false) 시작 날짜 (형식: YYYY-MM-DD)
endDate: (required=false) 끝 날짜 (형식: YYYY-MM-DD)
limit: (default=10) 조회할 테마의 개수
#### Response

```http
HTTP/1.1 200
Content-Type: application/json

[
    {
        "id": 1,
        "name": "테마1",
        "description": "테마1 설명",
        "thumbnail": "https://example.com/thumbnail.jpg"
    },
    {
        "id": 2,
        "name": "테마2",
        "description": "테마2 설명",
        "thumbnail": "https://example.com/thumbnail-2.jpg"
    }
]
```

</details>
