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
├── controller/              # REST API 엔드포인트
│   ├── ReservationController.java          # 예약 관련 API
│   └── ReservationTimeController.java      # 예약 가능 시간 관련 API
├── service/                 # 비즈니스 로직
│   ├── RoomReservationService.java         # 예약 서비스 (메인)
│   └── ReservationTimeService.java         # 예약 시간 서비스
├── dao/                     # 데이터 접근 계층
│   ├── ReservationDao.java                 # 예약 데이터 접근
│   └── ReservationTimeDao.java             # 예약 시간 데이터 접근
├── repository/              # Repository 패턴 구현
├── domain/                  # 도메인 모델 (비즈니스 엔티티)
│   ├── Reservation.java                    # 예약 정보 (id, name, date, time)
│   ├── ReservationTime.java                # 예약 시간 정보 (id, startAt)
│   ├── ReservationCommand.java             # 예약 생성 요청
│   └── ReservationTimeCommand.java         # 예약 시간 생성 요청
├── dto/                     # 요청/응답 데이터 전송 객체
│   ├── AddReservationRequest.java          # 예약 추가 요청
│   └── AddReservationTimeRequest.java      # 예약 시간 추가 요청
├── exception/               # 커스텀 예외 클래스
└── RoomescapeApplication.java               # 애플리케이션 진입점
```

## 주요 기능

### 1. 예약 시간 관리
- **예약 가능한 시간대 조회**: 모든 예약 시간 목록 조회
- **예약 시간 추가**: 새로운 예약 시간대 생성
- **예약 시간 삭제**: 기존 예약 시간대 삭제

### 2. 예약 관리
- **예약 조회**: 현재 예약된 모든 정보 조회
- **예약 생성**: 특정 날짜/시간에 방탈출 게임 예약
- **예약 삭제**: 예약된 게임 삭제


## 데이터 모델

### Reservation (예약)
| 필드 | 설명 |
|------|------|
| id | 예약 고유 ID |
| name | 예약자 이름 |
| date | 예약 날짜 |
| time | 예약 시간 (ReservationTime 객체) |

### ReservationTime (예약 시간)
| 필드 | 설명 |
|------|------|
| id | 예약 시간 고유 ID |
| startAt | 시작 시간 |


## API 엔드포인트

### 예약 시간 관련
- `GET /times` - 모든 예약 가능한 시간 조회
- `POST /times` - 새로운 예약 시간 추가
- `DELETE /times/[id]` - 예약 시간 삭제

### 예약 관련
- `GET /reservations` - 모든 예약 조회
- `POST /reservations` - 새로운 예약 생성
- `DELETE /reservations/[id]` - 예약 삭제

## 데이터베이스

프로젝트는 **H2 In-Memory 데이터베이스**를 사용하며, 초기 스키마는 다음 파일에 정의됩니다:
- `src/main/resources/schema.sql` - 테이블 정의
