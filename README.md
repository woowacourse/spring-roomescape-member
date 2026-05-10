## 기능목록

### 테마 도메인

* [x] 테마 도메인 추가
    * [x] 테마는 이름, 설명, 썸네일 이미지 url을 가진다
    * 모든 테마의 시작 시간과 소요 시간은 동일하다고 가정한다
* [x] 예약에 테마 정보가 포함 되도록 수정
* [x] 관리자의 테마 추가, 삭제

### 사용자 예약

* [x] 사용자의 날짜, 테마 선택 -> 예약 가능한 시간 표시
    * [x] 예약 가능한 시간이란 관리자가 등록한 시간 중 해당 날짜+테마에 아직 예약이 없는 시간이다
* [x] 사용자가 본인의 이름으로 예약 가능한 시간으로 예약
* [x] 같은 날짜, 시간이라도 테마가 다르면 각각 예약 가능

### 인기 테마 조회

* [x] 최근 1주일동안 예약이 많았던 테마 상위 10개 조회
    * [x] 최근 1주일을 이전 날 기준으로 조회한다

### 추가 요구사항

* [x] `data.sql`로 테스트용 데이터 넣어서 인기 테마 조회 결과 검증

### 화면

* [x] 사용자가 보는 화면 출력
    * [x] 브라우저에서 정상 동작 확인까지만 한다

---

## API 명세

### 예약 API

```http request
### 조회 요청
GET /reservations HTTP/1.1
Content-Type: application/json

### 조회 응답
HTTP/1.1 200 OK
Content-Type: application/json

[
{
   "id": 1,
   "name": "브라운",
   "date": "2026-05-03",
   "time": {
      "id": 1,
      "startAt": "10:00"
   },
   "theme": { 
      "id": 1,
      "name": "테마이름",
      "description": "설명",
      "thumbnailImageUrl": "썸네일이미지url"
   }
}
]

---

### 생성 요청
POST /reservations HTTP/1.1
Content-Type: application/json

{
   "name": "브라운",
   "date": "2026-05-03",
   "timeId": 1,
   "themeId": 1
}

### 생성 응답
HTTP/1.1 201 Created
Content-Type: application/json

[
{
   "id": 1,
   "name": "브라운",
   "date": "2026-05-03",
   "time": {
      "id": 1,
      "startAt": "10:00"
   },
   "theme": { 
      "id": 1,
      "name": "테마이름",
      "description": "설명",
      "thumbnailImageUrl": "썸네일이미지url"
   }
}
]

---

### 삭제 요청
DELETE /reservations/1 HTTP/1.1

### 삭제 응답
HTTP/1.1 204 No Content

---

```

### 예약 시간 API

```http request
### 조회 요청
GET /times HTTP/1.1
Content-Type: application/json

### 조회 응답
HTTP/1.1 200 OK
Content-Type: application/json

[
    {
        "id": 1,
        "startAt": "15:30"
    },
    {
        "id": 2,
        "startAt": "16:30"
    }
]

---

### 생성 요청
POST /times HTTP/1.1
Content-Type: application/json

{
    "startAt": "15:30"
}

### 생성 응답
HTTP/1.1 201 Created
Content-Type: application/json

{
    "id": 1,
    "startAt": "15:30"
}

---

### 삭제 요청
DELETE /times/1 HTTP/1.1

### 삭제 응답
HTTP/1.1 204 No Content

---

### 사용자가 날짜·테마를 골라 예약 가능한 시간을 본다. -> 요청
GET /times?date=2026-05-03&themeId=1 HTTP/1.1

### 응답
HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "id": 1,
    "startAt": 15:30",
    "reserved": false
  },
  {
    "id": 2,
    "startAt": 15:30",
    "reserved": true
  }
]
```

### 테마 API

```http request
### 조회 요청
GET /themes HTTP/1.1
Content-Type: application/json

### 조회 응답
HTTP/1.1 200 OK
Content-Type: application/json

[
   {
      "id": 1,
      "name": "테마이름",
      "description": "설명",
      "thumbnailImageUrl": "썸네일이미지url"
   }
]

---

### 생성 요청
POST /themes HTTP/1.1
Content-Type: application/json

{
   "name": "테마이름",
   "description": "설명",
   "thumbnailImageUrl": "썸네일이미지url"
}

### 생성 응답
HTTP/1.1 201 Created
Content-Type: application/json

{
   "id": 1,
   "name": "테마이름",
   "description": "설명",
   "thumbnailImageUrl": "썸네일이미지url"
}

---

### 삭제 요청
DELETE /themes/1 HTTP/1.1

### 삭제 응답
HTTP/1.1 204 No Content

---

### 최근 1주 동안 예약이 많은 테마 상위 10개 -> 요청
GET /themes?days=7 HTTP/1.1

### 응답
HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "id": 1,
    "name": "방탈출 제목",
    "description": "테마 설명",
    "thumbnail": "https://example.com/theme1.png"
  },
  {
    "id": 2,
    "name": "공포의 방",
    "description": "공포 테마 설명",
    "thumbnail": "https://example.com/theme2.png"
  }
]

```
