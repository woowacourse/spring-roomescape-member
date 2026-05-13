# 방탈출 예약 시스템 (Room Escape Reservation System)

## 프로젝트 소개

방탈출 예약 시스템은 **Spring Boot 기반의 REST API 서버**로, 방탈출 게임 예약을 관리하는 웹 애플리케이션입니다.

사용자는 이 시스템을 통해 **원하는 날짜와 시간에 방탈출 게임을 예약**할 수 있습니다.

## 기술 스택

- **프레임워크**: Spring Boot 3.4.4
- **언어**: Java 21
- **데이터베이스**: H2 (In-memory)
- **빌드 도구**: Gradle
- **테스트**: JUnit 5, REST Assured

## 프로젝트 구조

```
src/main/java/roomescape/
src/main/java/roomescape/
├── controller/              # REST API 엔드포인트 (Reservation, Time, Theme)
├── service/                 # 비즈니스 로직 및 예외 처리
├── repository/              # 데이터 접근 계층 (JdbcTemplate 기반)
├── domain/                  # 도메인 모델 및 Value Object (Command 포함)
├── dto/                     # 데이터 전송 객체 (Request/Response)
├── exception/               # 커스텀 예외 (Conflict, NotFound, Unauthorized 등)
└── config/                  # 설정 클래스 (CORS, WebMvc)
└── RoomescapeApplication.java               # 애플리케이션 진입점
```

## 주요 기능

### 1. 예약 시간 관리
- **예약 가능한 시간대 조회**: 모든 예약 시간 목록 조회
- **예약 시간 추가**: 새로운 예약 시간대 생성
- **예약 시간 삭제**: 기존 예약 시간대 삭제

### 2. 테마 관리
- **테마 목록 조회**: 현재 운영 중인 모든 방탈출 테마 조회
- **테마 추가**: 새로운 테마(이름, 설명, 이미지 URL) 생성
- **테마 삭제**: 테마 삭제 (단, 해당 테마에 예약이 존재하는 경우 삭제 불가)
- **인기 테마 조회**: 예약 건수를 기반으로 한 인기 테마 랭킹 조회

### 3. 예약 관리
- **예약 조회**: 현재 예약된 모든 정보 조회
- **예약 생성**: 특정 날짜/시간에 방탈출 게임 예약
- **예약 삭제**: 예약된 게임 삭제
- **예약 수정**: 기존 예약의 정보를 변경 (날짜, 시간, 테마 등)


## 데이터 모델

### Reservation (예약)
| 필드       | 설명                         |
|----------|----------------------------|
| id       | 예약 고유 ID                   |
| name     | 예약자 이름                     |
| date     | 예약 날짜                      |
| time_id  | 예약 시간 (ReservationTime 객체) |
| theme_id | 테마 (Theme 객체)              |


### ReservationTime (예약 시간)
| 필드 | 설명 |
|------|------|
| id | 예약 시간 고유 ID |
| startAt | 시작 시간 |

### Theme (테마)
| 필드          | 설명                             |
|-------------|--------------------------------|
| id          | 테마 고유 ID                       |
| name        | 테마 이름                          |
| description | 테마 설명                          |
| image_url   | 테마 대표 이미지 |



## API 엔드포인트

### 예약 시간 관련
- `GET /times` - 모든 예약 가능한 시간 조회
- `POST /times` - 새로운 예약 시간 추가
- `DELETE /times/[id]` - 예약 시간 삭제

### 테마 관련
- `GET /themes` - 모든 테마 조회
- `POST /themes` - 새로운 테마 추가
- `DELETE /themes/{id}` - 테마 삭제

### 예약 관련
- `GET /reservations` - 모든 예약 조회
- `POST /reservations` - 새로운 예약 생성
- `DELETE /reservations/[id]` - 예약 삭제
- `POST /reservations/{id}` - 기존 예약 정보 수정

## 데이터베이스

프로젝트는 **H2 In-Memory 데이터베이스**를 사용하며, 초기 스키마는 다음 파일에 정의됩니다:
- `src/main/resources/schema.sql` - 테이블 정의
