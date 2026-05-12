# 방탈출 사용자 예약 설계

## 싸이클 1 

### 1 단계 - 테마 도메인 추가

- 규칙 1. 테마는 리소스인가?
    - 맞다.
    - 왜?
    - 예약에 종속적인 도메인 모델이지만,
      독립적인 멘탈 모델이니까
- 규칙 2.
- 규칙 3.
- 규칙 4. 테마의 API 명세는 어떻게 작성해야?
    - 추가 PUT /themes
    - 삭제 DELETE /themes
    - 조회 GET /themes

#### API 설계의 근거

> 사용자가 테마, 날짜에 따른 예약 가능한 시간을 조회할 때, 주 자원이 되는 시간을 중심으로 설계하였습니다.

테마는 독립적인 리소스이기에 단독 엔드포인트로,  
API 설계 기본 원칙에 따라 행위를 HTTP 메서드로 표현

#### 테마 도메인

- 이름
- 설명
- 썸네일 이미지 URL
- 시작 시간과 소요 시간은 동일하다고 가정한다.

#### 기능 목록

> 방탈출 게임에 '테마' 정보를 추가한다.  
> 테마는 이름, 설명, 썸네일 이미지 URL을 가진다.  
> 모든 테마의 시작 시간과 소요 시간은 동일하다고 가정한다.

-[x] record Theme
    -[x] String name
    -[x] String description
    -[x] String thumbnailUrl
    -[x] void validate
        -[x] Theme transientOf

> 예약에 테마 정보를 포함하도록 기존 코드를 변경한다.

-[x] class ThemeController
    -[x] ThemeRequest
    -[x] ThemeResponse

> 관리자가 테마를 추가·삭제할 수 있다.

-[x] class ThemeService
    - [x] Theme saveTime
    - [x] void removeTime
-[x] class ThemeRepository
-[x] class ThemeDao
-[x] record Reservation
    -[x] Theme theme

### 관리자가 테마를 추가, 삭제, 조회

### 추가 API

POST /themes

### 조회 API

GET /themes

### 삭제

DELETE /themes/{themeID}

---

## 추가 단계 - 사용자 클라이언트 화면 제작

[마이찬](https://github.com/Uechann)의 실험 기록을 참조해,  
정리된 API 명세를 기반으로 Claude 를 통해 React.js 클라이언트 제작

[클라이언트 저장소](https://github.com/Uechann/react-roomscape)

---

## 2단계 - 사용자 예약

### 1. 테마들의 모든 정보 조회 API

```json
GET /themes HTTP/1.1 
```

### 2. 사용자가 (날짜, 테마)를 통해 예약 가능 시간 조회 API

```json
GET /times?date=2026-05-04&themeId=1HTTP/1.1 
```

### 3. 사용자가 날짜, 테마, 예약 시간을 통해 예약 등록 API

```json
POST /reservations HTTP/1.1

{
  "themeId": 1,
  "date": "2026-05-03",
  "timeId": 1,
  "name": "브라운"
}
```

#### 요청과 응답은 1단계의 진행 과정을 통해 구체화

## 3단계 - 인기 테마 조회

### 1. 최근 1주 동안 예약이 많았던 테마 상위 10개를 조회한다.

- 반환값은 테마 10개.

```json
GET /themes?topCount=10&during=7 HTTP / 1.1 
```

---

## 싸이클 2

### 1단계 - 서비스 정책 적용
다음 정책을 만족하지 않는 요청은 거부한다.

공통 규칙
- 유효하지 않은 입력값(빈 이름, 잘못된 날짜 형식 등)을 거부한다. 400 

예약 규칙
- 예약이 존재하지 않는 경우 404 Not Found
- 자신의 예약이 아닌 예약에 접근하는 경우 403 Forbidden
- 이미 취소된 예약을 취소하는 경우 409 Conflict
- 이미 완료된 예약을 취소하는 경우 409 Conflict
- 지나간 날짜·시간에 대한 예약 변경, 취소가 불가능하다. 422 Unprocessable Entity
- 존재하지 않는 시간 ID에 대한 예약 변경일 경우 404 Not Found
- 같은 날짜+시간+테마에 이미 예약이 있으면 중복 예약을 거부한다. 409 Conflict
- 예약이 존재하는 시간을 삭제할 수 없다. 422 Unprocessable Entity

### 2단계 - 에러 응답 설계
서비스 정책 위반, 유효하지 않은 입력, 존재하지 않는 리소스 등에 대해 의도된 에러 응답을 반환한다.
500(서버 에러)이 사용자에게 노출되지 않도록 한다.

에러 응답에 담을 데이터

- HTTP 상태 코드
- 에러 메시지
- 에러 발생 시각
- API 경로
- 추적 ID

### 3단계 - 내 예약 조회/변경/취소
사용자가 자신의 이름으로 본인의 예약 목록을 조회할 수 있다.
사용자가 본인의 예약을 취소할 수 있다.
사용자가 본인의 예약의 날짜·시간을 변경할 수 있다.
변경·취소 시 발생하는 에러 케이스(이미 지난 예약을 취소, 변경하려는 시간이 이미 차 있음 등)도 2단계의 규칙에 맞춰 처리한다.

#### 사용자가 자신의 예약 취소 API

사용자의 예약의 상태를 취소 상태로 변경한다.

이때 그러면 예약에 상태라는 필드가 추가되어야 한다.

예약 상태 (대기, 확정, 완료, 취소)

요청

```http request
PATCH http://localhost:8080/reservations/{reservationId}
{
	"status": "CANCELLED"
}
```

```http request
PATCH http://localhost:8080/reservations/{reservationId}/cancel
```

#### 예약 날짜·시간 변경 API

예약의 날짜, 시간을 변경한다.

존재하는 필드에 대해서만 변경한다.

요청

```http request
PATCH http://localhost:8080/reservations/{reservationId}
{
	"date": "2026-05-15",
	"timeId": 1
}
```

---

## API 명세서


