## 방탈출 예약 관리

## 페이지 반환

- [x] API 경로는 `GET /`으로 구현한다. 
  - 인기 테마를 나열하는 페이지 
  - [x] 메인 페이지 파일은 `templates/index.html`을 사용한다.
  - [x] 응답 성공시, `200 OK`를 반환한다.

- [x] API 경로는 `GET /reservation`으로 구현한다.
  - 날짜, 테마, 시간을 선택해 예약을 진행하는 예약페이지
  - [x] 메인 페이지 파일은 `templates/reservation.html`을 사용한다.
  - [x] 응답 성공시, `200 OK`를 반환한다.

- [x] API 경로는 `GET /admin` 으로 구현한다.
  - 관리자 페이지 
  - [x] 메인 페이지 파일은 `templates/admin/index.html`를 사용한다.
  - [x] 응답 성공시, `200 OK`를 반환한다.

- [x] API 경로는 `GET /admin/reservation` 으로 구현한다.
  - 현재 등록된 모든 예약들을 관리할 수 있는 페이지 
  - [x] 메인 페이지 파일은 `templates/admin/reservation-new.html`를 사용한다.
  - [x] 응답 성공시, `200 OK`를 반환한다.
  
- [x] API 경로는 `GET /admin/time` 으로 구현한다.
  - 현재 등록된 모든 방탈출 시간들을 관리할 수 있는 페이지 
  - [x] 메인 페이지 파일은 `templates/admin/time.html`를 사용한다.
  - [x] 응답 성공시, `200 OK`를 반환한다.
  
- [x] API 경로는 `GET /admin/theme` 으로 구현한다.
  - 현재 등록된 모든 방탈출 테마들을 관리할 수 있는 페이지
  - [x] 메인 페이지 파일은 `templates/admin/theme.html`를 사용한다.
  - [x] 응답 성공시, `200 OK`를 반환한다.

- [x] API 경로는 `GET /login` 으로 구현한다.
  - 로그인 페이지
  - [x] 메인 페이지 파일은 `templates/login.html`를 사용한다.
  - [x] 응답 성공시, `200 OK`를 반환한다.

## 데이터(JSON) 반환

### POST '/login'
- id/pw로 로그인 요청
- 응답: `200 OK`

```
HTTP/1.1 200 OK
Content-Type: application/json
Keep-Alive: timeout=60
Set-Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI; Path=/; HttpOnly
```

### GET '/login/check'
- 로그인 검증 API
- 응답: `200 OK`
```json
{"name":"관리자"}
```

### GET `/reservations`
- 전체 예약 목록 조회
- 응답: `200 OK`
```json
[
  {
    "id": 1,
    "name": "김민준",
    "date": "2025-04-30",
    "time": {
      "timeId": 1,
      "startAt": "15:40:00"
    },
    "theme": {
      "themeId": 8,
      "name": "레벨 8탈출",
      "description": "우테코 레벨8를 탈출하는 내용입니다.",
      "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
    }
  },
  {
    "id": 2,
    "name": "이서연",
    "date": "2025-04-30",
    "time": {
      "timeId": 2,
      "startAt": "16:30:00"
    },
    "theme": {
      "themeId": 3,
      "name": "레벨 3탈출",
      "description": "우테코 레벨3를 탈출하는 내용입니다.",
      "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
    }
  }
]
```

---

### POST `/reservations`
- 예약 추가
- 요청
```json
{
  "date": "2025-05-06",
  "memberId": 1,
  "themeId": 1,
  "timeId": 1
}
```
- 응답: `201 Created`

```json
{
  "id": 21,
  "date": "2025-05-06",
  "member": {
    "memberId": 1,
    "name": "제프리"
  },
  "memberId": 1,
  "time": {
    "timeId": 1,
    "startAt": "15:40:00"
  },
  "theme": {
    "themeId": 1,
    "name": "레벨 1탈출",
    "description": "우테코 레벨1를 탈출하는 내용입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
  }
}
```

---

### DELETE `/reservations/{id}`
- 예약 삭제
- PathVariable
  - `id`: 삭제할 예약의 ID
- 응답: `204 No Content`

---

### GET `/themes`
- 전체 테마 목록 조회
- 응답: `200 OK`
```json
[
  {
    "themeId": 1,
    "name": "레벨 1탈출",
    "description": "우테코 레벨1를 탈출하는 내용입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
  },
  {
    "themeId": 2,
    "name": "레벨 2탈출",
    "description": "우테코 레벨2를 탈출하는 내용입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
  }
]
```

---

### GET `/themes/ranking`
- 인기 테마 랭킹 조회
- 응답: `200 OK`  
```json
[
  {
    "themeId": 12,
    "name": "레벨 12탈출",
    "description": "우테코 레벨12를 탈출하는 내용입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
  },
  {
    "themeId": 11,
    "name": "레벨 11탈출",
    "description": "우테코 레벨11를 탈출하는 내용입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
  }
]
```

---

### POST `/themes`
- 테마 추가
- 요청:
```json
{
  "name": "방탈출1",
  "description": "방탈출1 설명",
  "thumbnail": "방탈출1 썸네일"
}
```
- 응답: `201 Created`
```json
{
  "themeId": 13,
  "name": "방탈출1",
  "description": "방탈출1 설명",
  "thumbnail": "방탈출1 썸네일"
}
```

---

### DELETE `/themes/{id}`
- 테마 삭제
- PathVariable
  - `id`: 삭제할 테마의 ID
- 응답: `204 No Content`

---

### GET `/times`
- 예약 가능한 시간 목록 조회
- 응답: `200 OK`
```json
[
  {
    "timeId": 1,
    "startAt": "15:40:00"
  },
  {
    "timeId": 2,
    "startAt": "16:30:00"
  },
  {
    "timeId": 3,
    "startAt": "17:50:00"
  }
]
```

---

### POST `/times`
- 예약 시간 추가
- 요청:
```json
{
  "startAt": "20:50"
}
```
- 응답: `201 Created`
```json
{
  "timeId": 4,
  "startAt": "20:50:00"
}
```

---

### DELETE `/times/{id}`
- 예약 시간 삭제
- PathVariable
  - `id`: 삭제할 시간의 ID
- 응답: `204 No Content`
