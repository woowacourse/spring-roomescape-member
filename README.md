# 방탈출 예약 관리
방탈출 예약과 예약 시간을 관리하는 미션입니다.

## 기능 목록

### 예약 시간 관리
- 예약 시간 생성
- 예약 시간 목록 조회
- 예약 시간 삭제
- 예약 시간은 `HH:mm` 형식으로 요청
- 예약에서 참조 중인 예약 시간은 삭제할 수 없도록 검증

### 예약 관리
- 예약 생성
- 예약 목록 조회
- 예약 삭제
- 예약 생성 시 예약자 이름, 예약 날짜, 예약 시간 ID 입력
- 예약 시간 ID를 통해 예약과 예약 시간을 연결
- 존재하지 않는 예약 시간 ID로 예약 생성 시 예외 발생

### 예외 처리
- 예약 요청 값이 비어 있으면 `400 Bad Request` 반환
- 예약 시간 요청 값이 비어 있으면 `400 Bad Request` 반환
- 존재하지 않는 예약 시간으로 예약하면 `404 Not Found` 반환
- 예약에서 참조 중인 예약 시간을 삭제하면 `400 Bad Request` 반환
- 처리하지 못한 서버 오류는 `500 Internal Server Error` 반환

## 추가한 의존성
| 의존성 | 역할 |
| --- | --- |
| `spring-boot-starter-jdbc` | JDBC Template을 사용한 데이터베이스 접근 |
| `h2` | 개발 및 테스트용 인메모리 데이터베이스 |

## 실행 방법

백엔드 패키지로 이동해서 실행합니다.

```bash
cd BE
./gradlew bootRun
```

테스트도 `BE` 패키지에서 실행합니다.

```bash
cd BE
./gradlew test
```

## API

### 예약 시간

#### 예약 시간 생성

```http
POST /times
Content-Type: application/json

{
  "startAt": "10:00"
}
```

응답: `201 Created`

응답 헤더:

```http
Location: /times/{id}
```

#### 예약 시간 목록 조회

```http
GET /times
```

응답: `200 OK`

#### 예약 시간 삭제

```http
DELETE /times/{id}
```

응답: `204 No Content`

참조 중인 예약 시간이면 `400 Bad Request`를 반환합니다.

### 예약

#### 예약 생성

```http
POST /reservations
Content-Type: application/json

{
  "name": "브라운",
  "date": "2026-04-29",
  "timeId": 1
}
```

응답: `201 Created`

응답 헤더:

```http
Location: /reservations/{id}
```

존재하지 않는 예약 시간 ID로 요청하면 `404 Not Found`를 반환합니다.

#### 예약 목록 조회

```http
GET /reservations
```

응답: `200 OK`

#### 예약 삭제

```http
DELETE /reservations/{id}
```

응답: `204 No Content`
