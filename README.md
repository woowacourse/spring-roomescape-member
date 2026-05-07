# 방탈출 사용자 예약 시스템 (Room Escape User Reservation System)

## 프로젝트 소개

방탈출 사용자 예약 시스템은 **Spring Boot 기반의 REST API 서버**로, 방탈출 게임 예약 서비스를 관리하는 웹 애플리케이션입니다.

## 기술 스택

- **프레임워크**: Spring Boot 3.4.4
- **언어**: Java 21
- **데이터베이스**: H2 (In-memory)
- **빌드 도구**: Gradle
- **테스트**: JUnit 5, REST Assured


## 프로젝트 구조

```
src/main/java/roomescape/
├── controller/
│   ├── ReservationController.java               
│   ├── ReservationThemeController.java          
│   └── ReservationTimeController.java           
├── service/
│   ├── RoomReservationService.java              
│   ├── ReservationThemeService.java             
│   └── ReservationTimeService.java              
├── dao/
│   ├── ReservationDao.java
│   └── ReservationTimeDao.java
├── repository/
│   ├── reservation/
│   │   ├── ReservationRepository.java
│   │   └── JdbcReservationRepository.java
│   ├── reservationTime/
│   │   ├── ReservationTimeRepository.java
│   │   └── JdbcReservationTimeRepository.java
│   └── ReservationTheme/                     
│       ├── ReservationThemeRepository.java
│       └── JdbcReservationThemeRepository.java
├── domain/
│   ├── Reservation/
│   │   ├── Reservation.java
│   │   └── ReservationCommand.java              
│   ├── ReservationTime/
│   │   ├── ReservationTime.java
│   │   ├── ReservationTimeCommand.java
│   │   ├── ReservationTimeCondition.java        
│   │   └── ReservationTimeWithAvailable.java    
│   └── ReservationTheme/                        
│       ├── ReservationTheme.java                
│       ├── ReservationThemeCommand.java         
│       ├── ReservationThemeWithCount.java       
│       └── PopularThemeCondition.java           
├── dto/
│   ├── Reservation/
│   │   ├── AddReservationRequest.java
│   │   ├── ReservationResponse.java
│   │   └── ReservationCondition.java            
│   ├── ReservationTime/
│   │   ├── AddReservationTimeRequest.java
│   │   ├── ReservationTimeResponse.java
│   │   └── AvailableReservationTimeResponse.java 
│   └── theme/                                   
│       ├── AddThemeRequest.java                 
│       ├── ReservationThemeResponse.java        
│       ├── PopularConditionRequest.java         
│       └── PopularReservationThemeResponse.java 
├── exception/
│   ├── CustomException.java
│   ├── BaseCustomException.java
│   ├── ReservationCommandException.java
│   ├── ReservationTimeConditionException.java
│   ├── DuplicatedReservationRequestException.java
│   ├── NotFoundResourceException.java
│   ├── DataReferencedException.java
│   ├── ErrorMessage.java
│   ├── HttpErrorMapping.java
│   └── handler/
│       └── GlobalExceptionHandler.java
└── RoomescapeApplication.java
```

## 주요 기능

### 1. 예약 관리
- **예약 조회**: 현재 예약된 모든 정보 조회
- **예약 생성**: 특정 날짜/시간/테마로 방탈출 게임 예약
- **예약 삭제**: 예약된 게임 삭제

### 2. 예약 시간 관리
- **예약 시간 조회**: 모든 예약 시간 목록 조회
- **예약 시간 추가**: 새로운 예약 시간대 생성
- **예약 시간 삭제**: 기존 예약 시간대 삭제
- **예약 가능 시간 조회**: 특정 날짜와 테마 기준으로 예약 가능 여부 포함한 시간 목록 조회

### 3. 테마 관리
- **테마 조회**: 모든 방탈출 테마 목록 조회
- **테마 추가**: 새로운 방탈출 테마 생성
- **테마 삭제**: 기존 방탈출 테마 삭제
- **인기 테마 조회**: 특정 기간 내 예약 횟수 기준 인기 테마 조회


## 데이터 모델

### Reservation (예약)
| 필드 | 타입 | 설명 |
|------|------|------|
| id | long | 예약 고유 ID |
| name | String | 예약자 이름 |
| date | String | 예약 날짜 |
| time | ReservationTime | 예약 시간 객체 |
| reservationTheme | ReservationTheme | 예약 테마 객체 |

### ReservationTime (예약 시간)
| 필드 | 타입 | 설명 |
|------|------|------|
| id | long | 예약 시간 고유 ID |
| startAt | String | 시작 시간 |

### ReservationTheme (예약 테마)
| 필드 | 타입 | 설명 |
|------|------|------|
| id | long | 테마 고유 ID |
| name | String | 테마 이름 |
| description | String | 테마 설명 |
| imageUrl | String | 썸네일 이미지 URL |


## API 엔드포인트

### 예약 관련

| 메서드 | URL | 설명 | 상태 코드 |
|--------|-----|------|-----------|
| GET | `/reservations` | 모든 예약 조회 | 200 |
| POST | `/reservations` | 새로운 예약 생성 | 200 |
| DELETE | `/reservations/{id}` | 예약 삭제 | 204 |

**POST /reservations - Request Body**
| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| name | String | Y | 예약자 이름 |
| date | String | Y | 예약 날짜 (YYYY-MM-DD) |
| timeId | long | Y | 예약 시간 ID |
| themeId | long | Y | 예약 테마 ID |

**GET /reservations, POST /reservations - Response**
| 필드 | 타입 | 설명 |
|------|------|------|
| id | long | 예약 고유 ID |
| name | String | 예약자 이름 |
| date | String | 예약 날짜 |
| time | ReservationTimeResponse | 예약 시간 객체 |
| reservationTheme | ReservationThemeResponse | 예약 테마 객체 |

---

### 예약 시간 관련

| 메서드 | URL | 설명 | 상태 코드 |
|--------|-----|------|-----------|
| GET | `/times` | 모든 예약 시간 조회 | 200 |
| POST | `/times` | 새로운 예약 시간 추가 | 200 |
| GET |	`/times/availability` |	날짜·테마 기준 예약 가능 시간 조회 |	200 |
| DELETE | `/times/{id}` | 예약 시간 삭제 | 204 |

**POST /times - Request Body**
| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| startAt | String | Y | 시작 시간 (HH:mm) |

**GET /times, POST /times - Response**
| 필드 | 타입 | 설명 |
|------|------|------|
| id | long | 예약 시간 고유 ID |
| startAt | String | 시작 시간 |

**GET /times/availability - Query Parameters**
| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| date | String | Y | 조회 날짜 (YYYY-MM-DD) |
| themeId | long | Y | 테마 ID |

**GET /times/availability - Response**
| 필드 | 타입 | 설명 |
|------|------|------|
| id | long | 예약 시간 고유 ID |
| startAt | String | 시작 시간 (HH:mm) |
| isAvailable | boolean | 예약 가능 여부 |

---

### 테마 관련

| 메서드 | URL | 설명 | 상태 코드 |
|--------|-----|------|-----------|
| GET | `/themes` | 모든 테마 조회 | 200 |
| POST | `/themes` | 새로운 테마 추가 | 201 |
| DELETE | `/themes/{id}` | 테마 삭제 | 204 |
| GET | `/themes/popular` | 인기 테마 조회 | 200 |

**POST /themes - Request Body**
| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| name | String | Y | 테마 이름 |
| description | String | Y | 테마 설명 |
| imageUrl | String | Y | 썸네일 이미지 URL |

**GET /themes, POST /themes - Response**
| 필드 | 타입 | 설명 |
|------|------|------|
| id | long | 테마 고유 ID |
| name | String | 테마 이름 |
| description | String | 테마 설명 |
| imageUrl | String | 썸네일 이미지 URL |

**GET /themes/popular - Query Parameters**
| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| start_date | String | Y | 조회 시작 날짜 (YYYY-MM-DD) |
| end_date | String | Y | 조회 종료 날짜 (YYYY-MM-DD) |
| size | long | Y | 조회할 테마 개수 |

**GET /themes/popular - Response**
| 필드 | 타입 | 설명 |
|------|------|------|
| id | long | 테마 고유 ID |
| name | String | 테마 이름 |
| description | String | 테마 설명 |
| imageUrl | String | 썸네일 이미지 URL |
| count | long | 기간 내 예약 횟수 |


## 데이터베이스

프로젝트는 **H2 In-Memory 데이터베이스**를 사용하며, 초기 스키마와 초기 데이터 저장 쿼리는 다음 파일에 정의됩니다:
- `src/main/resources/schema.sql` - 테이블 정의
- `src/main/resources/data.sql` - 초기 데이터 저장
