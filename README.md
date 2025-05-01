## 🚀 1단계 - 예외 처리와 응답

### 요구사항

- [x] 시간 관리, 예약 관리 API가 적절한 응답을 하도록 생성에 대한 API를 HttpStatus 201로 변경

- 예외 처리
    - [x] 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때
        - [x] HH:mm 형식이 아닐 때 - LocalTime에서 걸림
        - [x] 방탈출 운영 시간이 아닐 때 (12:00 ~ 23:00 -> 진행 시간 1시간이라 가정하고 22시까지 가능) - domain
    - [x] 예약 생성 시 예약자명, 날짜, 시간에 유효하지 않은 값이 입력 되었을 때
        - [x] 예약자명 글자 수 제한 (2 ~ 5글자 사이) - domain
        - [x] 지난 날짜에 대한 예약 시 예외 - domain
        - [x] 당일 날짜이면서 지난 시간이나 현재 시간에 대한 예약 시 예외. - domain
            - [x] 시작 시간 10분 전까지 가능
        - [x] 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때 - Service
    - [x] 날짜와 시간이 중복되는 예약은 불가능하다. - Service
        - ex. 이미 4월 1일 10시에 예약이 되어있다면, 4월 1일 10시에 대한 예약을 생성할 수 없다.
- [x] 테마 관리 기능 추가
    - [x] 테마 전체 조회
    - [x] 테마 생성
    - [x] 테마 삭제
        - [x] 특정 테마에 대한 예약이 존재하는데, 그 테마를 삭제하려 할 때 예외 처리- Service
    - [x] 인기 테마 조회
    - [x] 특정 테마의 특정 날짜 예약 가능 시간 조회

---

### API 명세

- [GET] /themes
  
  테마 조회
  **응답 예시**

```json
[
  {
    "id": 1,
    "name": "레벨2 탈출",
    "description": "우테코 레벨2를 탈출하는 내용입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
  }
]
```

**id:** 식별자

**name:** 이름

**description:** 설명

**thumbnail:** 썸네일 이미지 경로

---
- [POST] /themes
  
  테마 생성
  **요청 예시**

```json
{
  "name": "레벨2 탈출",
  "description": "우테코 레벨2를 탈출하는 내용입니다.",
  "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}
```

**name:** 이름

**description:** 설명

**thumbnail:** 썸네일 이미지 경로

**응답 예시**

```json
{
  "id": 1,
  "name": "레벨2 탈출",
  "description": "우테코 레벨2를 탈출하는 내용입니다.",
  "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}
```

**id:** 식별자

**name:** 이름

**description:** 설명

**thumbnail:** 썸네일 이미지 경로

---
- [DELETE] /themes/{id}
  
  테마 삭제
---
- [GET] /times/available
  
- 예약 가능 시간 조회
  - Query Parameter
    - themeId: 테마의 식별자
    - date: 에약 날짜

**응답 예시**

```json
[
  {
    "timeId": 1,
    "startAt": "12:00:00",
    "booked": true
  },
  {
    "timeId": 2,
    "startAt": "13:00:00",
    "booked": false
  }
]
```

**timeId:** 예약 시간의 식별자

**startAt:** 예약 시작 시간

**booked:** 예약 여부

---
- [GET] /themes/rank
  
  일주일 간, 인기 테마 10개 내림차순 조회


**응답 예시**

```json
[
  {
    "id": 7,
    "name": "The Lost City",
    "description": "Discover the secrets of the lost city and find your way out.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
  },
  {
    "id": 11,
    "name": "The Bank Heist",
    "description": "Plan and execute the perfect bank heist to escape.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
  },
  {
    "id": 20,
    "name": "The Wild West",
    "description": "Escape the wild west town before the showdown.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
  },
  ...
]
```