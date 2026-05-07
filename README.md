## 기능 명세

### 사용자

- [ ] 전체 테마 목록을 조회 가능하다.

#### 화면 작성

- [x] 웰컴 페이지에서 전체 테마 목록을 보여준다.
- [x] 테마 목록 중 하나를 선택하면 해당 테마를 예약할 수 있는 상세 페이지로 이동한다.
- [x] 예약 시에는 이름과 날짜, 예약 시간대를 선택해야 한다.
- [x] 같은 테마에서 이미 예약된 시간대는 선택이 불가능하다.

<br>

### 관리자

- [ ] 모든 예약 조회 가능하다.
- [ ] 특정 사용자의 예약을 조회 가능하다.
- [x] 테마를 추가할 수 있다.
- [x] 테마를 삭제할 수 있다.

## API 명세

### 1. 관리자 (Admin)

#### 모든 예약 조회
```json
GET /admin/reservations

[
  {
    "id": 1,
    "name": "루드비코",
    "date": "2024-05-10",
    "time": {
      "id": 1,
      "startAt": "13:00"
    },
    "theme": {
      "id": 1,
      "name": "베네치아의 유령",
      "description": "공포/스릴러 테마입니다.",
      "thumbnailUrl": "https://example.com/image.png"
    }
  }
]
```


#### 예약 시간 추가
```json
POST /admin/times

{
  "startAt": "15:40"
}

// Response: 201 Created (Location: /times/{id})
```


#### 예약 시간 삭제
```json
DELETE /admin/times/{id}

// Response: 200 OK
```


#### 테마 추가
```json
POST /admin/themes

{
  "name": "우테코 탈출",
  "description": "제한 시간 내에 미션을 완료하세요.",
  "thumbnailUrl": "https://example.com/image.png"
}

// Response: 201 Created (Location: /theme/{id})
```


#### 테마 삭제
```json
DELETE /admin/themes/{id}

// Response: 204 No Content
```


<br>

### 2. 테마 (Theme)

#### 전체 테마 목록 조회
```json
GET /themes

[
  {
    "id": 1,
    "name": "우테코 탈출",
    "description": "제한 시간 내에 미션을 완료하세요.",
    "thumbnailUrl": "https://example.com/image.png"
  }
]
```


#### 인기 테마 목록 조회
```json
GET /themes/popular

[
  {
    "id": 1,
    "name": "우테코 탈출",
    "description": "가장 인기 있는 테마입니다.",
    "thumbnailUrl": "https://example.com/image.png"
  }
]
```


#### 특정 테마 상세 조회
```json
GET /themes/{id}

{
  "id": 1,
  "name": "우테코 탈출",
  "description": "제한 시간 내에 미션을 완료하세요.",
  "thumbnailUrl": "https://example.com/image.png"
}
```


<br>

### 3. 예약 (Reservation)

#### 예약 생성
```json
POST /reservations

{
  "date": "2024-05-15",
  "timeId": 1,
  "themeId": 1,
  "name": "루드비코"
}

// Response: 201 Created (Location: /reservations/{id})
```


#### 예약 취소
```json
DELETE /reservations/{id}

// Response: 200 OK
```


<br>

### 4. 예약 시간 (Time)

#### 전체 예약 시간 목록 조회
```json
GET /times

[
  {
    "id": 1,
    "startAt": "13:00"
  },
  {
    "id": 2,
    "startAt": "15:00"
  }
]
```


#### 예약된 시간 목록 조회
```json
GET /times/reserved?themeId=1&selectedDate=2024-05-15

[
  {
    "id": 1,
    "startAt": "13:00"
  }
]
```


<br>

### 5. 화면 (View)

#### 예약 페이지
```json
GET /reservations

// Response: reservation.html 렌더링
```
