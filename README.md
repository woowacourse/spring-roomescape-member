# 방탈출 사용자 예약
### 요구사항
- [x] 요청에 대한 적절한 상태코드를 반환하도록 수정
  - 생성 시 201, 삭제 시 204

- [ ] 시간에서 발생할 수 있는 예외 사항 처리
  - [ ] 유효하지 않은 시작 시간
  - [ ] 중복되는 시작 시간
  - [ ] 초 단위 시간은 무시하도록 처리
  - [ ] 없는 id를 삭제하는 경우
  - [ ] 예약이 존재하는 시간을 삭제하는 경우
  - [ ] 같은 날짜, 같은 시간

- [ ] 예약에서 발생할 수 있는 예외 사항 처리
  - [ ] 시간 id가 존재하지 않는 경우
  - [ ] 이름 제약조건 (길이 등)
  - [ ] 과거 시간을 예약하는 경우
  - [ ] 같은 날짜에 같은 시간을 예약하는 경우 중복 예약

# 방탈출 API 명세

## 예약 조회

### Request

- GET /reservations

### Response

- 200 OK
- content-ype: application/json

``` json
[
    {
        "id": 1,
        "name": "브라운",
        "date": "2023-08-05",
        "time": {
            "id": 1,
            "startAt": "10:00"
        }
    }
]
```

---

## 예약 추가

### Request

- POST /reservations
- content-type: application/json

```json
{
  "date": "2023-08-05",
  "name": "브라운",
  "timeId": 1
}
```

### Response

- 201 Created
- content-type: application/json

```json
{
  "id": 1,
  "name": "브라운",
  "date": "2023-08-05",
  "time": {
    "id": 1,
    "startAt": "10:00"
  }
}
```

---

## 예약 삭제

### Request

- DELETE /reservations/{reservationId}
- Path Variables : Long

### Response

- 204 No Content

---

## 시간 추가

### Request

- POST /times
- content-type: application/json

```json
{
  "startAt": "10:00"
}
```

### Response

- 201 Created
- content-type: application/json

```json
{
  "id": 1,
  "startAt": "10:00"
}
```

---

## 시간 조회

### Request

- GET /times

### Response

- 200 OK
- content-type: application/json

```json
[
  {
    "id": 1,
    "startAt": "10:00"
  }
]
```

---

## 시간 삭제

### Request

- DELETE /times/{timeId}
- PathVariable : Long

### Response

- 200 OK
