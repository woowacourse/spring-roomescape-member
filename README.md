# View

### [홈화면 View (Admin)]

- 어드민 홈화면을 보여줍니다.

```text
### 요청 예시

GET localhost:8080/admin

### 응답

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

### 응답

templates/admin/reservation-legacy.html (전체 조회 뷰)
```
---
# API

## Reservation

### [예약 추가 API (Admin)]

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

---

## 응답

### 정상 요청인 경우

HTTP/1.1 201
Content-Type: application/json

{
  "id": 1,
  "name": "dompoo",
  "date": "2024-12-05",
  "time": "20:00:00"
}

### name을 입력하지 않았을 경우

HTTP/1.1 400
Content-Length: 0

### name에 빈 값을 입력한 경우

HTTP/1.1 400
Content-Length: 0

### 날짜를 입력하지 않았을 경우

HTTP/1.1 400
Content-Length: 0

### 시간을 입력하지 않았을 경우

HTTP/1.1 400
Content-Length: 0
```

### [예약 전체 조회 API (Admin)]

- 전체 예약 조회를 할 수 있다.
- 다음과 같은 정보가 반환된다.
    - id : 예약 번호
    - name : 예약자명
    - date : 예약 날짜
    - time : 예약 시간

```text
### 요청 예시

GET localhost:8080/reservations

---

### 응답

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

### [예약 삭제 API (Admin)]

- 예약을 삭제할 수 있다.
- 다음과 같은 정보를 포함해야 한다.
    - id : 예약 번호

```text
### 요청 예시

DELETE localhost:8080/reservations/{id}

---

## 응답

### 정상 요청인 경우

HTTP/1.1 204
Content-Length: 0

### id에 해당하는 예약이 없는 경우

HTTP/1.1 400
Content-Length: 0
```

## Time

### [시간 추가 API(Admin)]

```text
### 요청 예시

POST /times HTTP/1.1
content-type: application/json

{
    "startAt": "10:00"
}
---

## 응답

### 정상 요청인 경우

HTTP/1.1 201
Content-Type: application/json

{
    "id": 1,
    "startAt": "10:00"
}
```

### [시간 조회 API(Admin)]

```text
### 요청 예시

GET /times HTTP/1.1
---

## 응답

### 정상 요청인 경우

HTTP/1.1 200 
Content-Type: application/json

[
   {
        "id": 1,
        "startAt": "10:00"
    }
]
```

### [시간 삭제 API(Admin)]

```text
### 요청 예시

DELETE /times/1 HTTP/1.1
---

## 응답

### 정상 요청인 경우

HTTP/1.1 204

```
