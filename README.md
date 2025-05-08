# View

### [홈화면 View (Admin)]

- 어드민 홈화면을 보여줍니다.

```text
### 요청 예시

GET localhost:8080/admin

### 응답 예시

templates/admin/index.html (홈화면 뷰)
```

### [예약 전체 조회 View (Admin)]

- 어드민 예약 전체 조회 화면을 보여줍니다.
- 각 예약의 다음과 같은 정보를 볼 수 있습니다.
    - 예약번호
    - 예약자
    - 날짜
    - 시간
- 다음과 같은 작업을 수행할 수 있습니다.
    - 개별 예약 삭제
    - 예약 추가

```text
### 요청 예시

GET localhost:8080/admin/reservation

### 응답 예시

templates/admin/reservation-legacy.html (전체 조회 뷰)
```

### [테마 관리 View (Admin)]

- 어드민 테마 관리 화면을 보여줍니다.
- 각 테마는 다음과 같은 정보를 볼 수 있습니다.
  - 순서
  - 제목
  - 설명
  - 썸네일 URL
- 다음과 같은 작업을 수행할 수 있습니다.
  - 테마 추가
  - 테마 삭제

```text
### 요청 예시
GET localhost:8080/admin/theme

### 응답 예시
templates/admin/theme.html (테마 관리 뷰)
```

### [사용자 예약 페이지 (Normal)]

- 일반 사용자는 이 곳에서 예약이 가능합니다.
- 날짜 선택, 테마 선택, 시간 선택이 필요합니다.
  - 이전 정보를 선택해야 다음 정보 선택이 가능합니다.
- 예약자명을 입력할 수 있습니다.

````text
### 요청 예시
GET localhost:8080/reservation

### 응답 예시
templates/reservation.html (사용자 예약 페이지)
````

### [인기 테마 페이지 (Normal)]

- 인기 테마들을 보여줍니다.
- 테마들은 예약 수에 따라 내림차순으로 정렬됩니다.
- 각 테마는 제목, 설명, 썸네일 이미지를 보여줍니다.

````text
### 요청 예시
GET localhost:8080/

### 응답 예시
templates/index.html (인기 테마 페이지)
````
---
# API

## Reservation

### [예약 추가 API]

- 예약을 추가할 수 있다.
- 다음과 같은 정보를 전달해야 한다.
    - name : 예약자명
    - date : 예약 날짜 (yyyy-MM-dd)
    - time : 예약 시간 (HH:mm)
- 다음과 같은 정보가 반환된다.
    - id : 예약 번호
    - name : 예약자명
    - date : 예약 날짜
    - time : 예약 시간

```text
### 요청 예시

POST localhost:8080/reservations
Content-Type: application/json

{
  "name": "dompoo",
  "date": "2024-12-05",
  "time": "20:00"
}

### 응답 예시

HTTP/1.1 201
Content-Type: application/json

{
  "id": 1,
  "name": "dompoo",
  "date": "2024-12-05",
  "time": "20:00:00"
}
```

### [예약 전체 조회 API]

- 전체 예약 조회를 할 수 있다.
- 다음과 같은 정보가 반환된다.
    - id : 예약 번호
    - name : 예약자명
    - date : 예약 날짜
    - time : 예약 시간

```text
### 요청 예시

GET localhost:8080/reservations

### 응답 예시

HTTP/1.1 200
Content-Type: application/json

[
  {
    "id": 1,
    "name": "dompoo",
    "date": "2024-12-05",
    "time": "20:00:00"
  },
  {
    "id": 2,
    "name": "wade",
    "date": "2024-12-05",
    "time": "18:30:00"
  }
]
```

### [예약 삭제 API]

- 예약을 삭제할 수 있다.
- 다음과 같은 정보를 포함해야 한다.
    - id : 예약 번호

```text
### 요청 예시

DELETE localhost:8080/reservations/{id}

### 응답 예시

HTTP/1.1 204
Content-Length: 0
```

## Time

### [시간 추가 API]

```text
### 요청 예시

POST /times HTTP/1.1
content-type: application/json

{
    "startAt": "10:00"
}

### 응답 예시

HTTP/1.1 201
Content-Type: application/json

{
    "id": 1,
    "startAt": "10:00"
}
```

### [시간 조회 API]

```text
### 요청 예시

GET /times HTTP/1.1

### 응답 예시

HTTP/1.1 200 
Content-Type: application/json

[
   {
        "id": 1,
        "startAt": "10:00"
    }
]
```

### [시간 삭제 API]

```text
### 요청 예시

DELETE /times/1 HTTP/1.1

### 응답 예시

HTTP/1.1 204

```

## Theme

### [테마 조회 API]

```text
### 요청 예시

GET /themes HTTP/1.1

### 응답 예시
HTTP/1.1 200 
Content-Type: application/json

[
   {
        "id": 1,
        "name": "레벨2 탈출",
        "description": "우테코 레벨2를 탈출하는 내용입니다.",
        "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
    }
]
```
### [테마 추가 API]

```text
### 요청 예시
POST /themes HTTP/1.1
content-type: application/json

{
"name": "레벨2 탈출",
"description": "우테코 레벨2를 탈출하는 내용입니다.",
"thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}

### 응답 예시
HTTP/1.1 201
Location: /themes/1
Content-Type: application/json

{
"id": 1,
"name": "레벨2 탈출",
"description": "우테코 레벨2를 탈출하는 내용입니다.",
"thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}
```
### [테마 삭제 API]

```text
### 요청 예시
DELETE /themes/1 HTTP/1.1

### 응답 예시
HTTP/1.1 204
```

### [인기 테마 조회 API]

- 예약 수 내림차순으로 테마들을 반환합니다.

```text
### 요청 예시
GET /themes/popular

### 응답 예시
HTTP/1.1 200
Location: /themes/popular
Content-Type: application/json
[
    {
        "id": 3,
        "name": "우테코 학교",
        "description": "잃어버린 DDD를 찾아라",
        "thumbnail": "/image/school.png"
    },
    {
        "id": 1,
        "name": "공포의 우테코",
        "description": "우테코에서 벌어지는 미스터리를 풀어라",
        "thumbnail": "/image/horror.png"
    },
    {
        "id": 2,
        "name": "시간 도둑",
        "description": "스릴 넘치는 우테코 미션",
        "thumbnail": "/image/time.png"
    }
]
```
