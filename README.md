## 🚀 1단계 - 예외 처리와 응답

### 요구사항

- [x] 시간 관리, 예약 관리 API가 적절한 응답을 하도록 생성에 대한 API를 HttpStatus 201로 변경

- 예외 처리
    - [x] 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때
        - [x] HH:mm 형식이 아닐 때
        - [x] 방탈출 운영 시간이 아닐 때 (12:00 ~ 23:00 -> 진행 시간 1시간이라 가정하고 22시까지 가능)
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

- [DELETE] /themes/{id}
  테마 삭제

### 고민한 점

- 현재 도메인에서 IllegalArgumentException을 사용해 예외처리를 하고 있는데 이 예외를 ControllerAdvice에서 잡아도 될까?
    - IllegalArgumentException이 java가 지원하는 예외이기 떄문에 우리가 예상치 못한 곳에서 IllegalArgumentException이 터질 가능성이 있기 떄문에 이 예외까지 잡아도
      되는지 잘 모르겠음
    - 해결방법: 도메인만의 커스텀 예외를 만든다. 하지만 이렇게 할 경우 controller 패키지의 ControllerAdvice가 도메인 커스텀 예외를 의존하게 되어 controller가 domain
      패키지를 의존하게 된다.
- `특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때`를 구현하려고 ON DELETE RESTRICT를 추가하여 해결했다.
    - 이는 외래키 제약 조건이 있을 때만 사용 가능하다. 이전 학습을 통해 외래키를 안쓰는 경우도 있다는 것을 알게 되어 없는 경우를 고려
    - 외래키를 사용한다해도 테스트에서 외래키를 끄고 테스트를 하는 경우도 고려
    - 외래키 제약 조건을 걸고, 아래 예외를 잡아 처리하면, 예외 메시지가 그대로 노출될 위험이 있다.
    - 그리고 JdbcSQLIntegrityConstraintViolationException을 잡아 예외를 처리한다 해도 JDBC에 너무 의존적인 구현이 된다.
    - 결론은 서비스 단에서 비즈니스 로직으로 처리한다. 그래서, 다른 Repository 의존성을 추가했다.
        - 그런데, 만약 reservation_time이나, theme을 참조하는 테이블이 많아지면, 의존성이 너무 많아지지 않을까? 걱정된다.

```text
org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException: Referential integrity constraint violation: "CONSTRAINT_23: PUBLIC.RESERVATION FOREIGN KEY(TIME_ID) REFERENCES PUBLIC.RESERVATION_TIME(ID) (CAST(1 AS BIGINT))"; SQL statement:
DELETE FROM reservation_time WHERE id = ?
```

- LocalDate.now(), LocalTime.now()를 사용할 지, Clock을 Mocking할 지