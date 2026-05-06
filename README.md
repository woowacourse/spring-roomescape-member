# Spring Roomescape Member API

## 공통 규칙
- Base URL: 미정 (서버 호스트/포트를 사용하세요).
- Content-Type: JSON 요청/응답은 application/json.
- 날짜 형식: yyyy-MM-dd (예: 2026-05-01).
- 시간 형식: HH:mm:ss (예: 10:00:00).

## 오류 응답
처리된 오류는 아래 형태로 반환됩니다.
```
JSON
{
"message": "string",
"timestamp": "2026-05-01T12:34:56.789"
}
```
### GlobalExceptionHandler 기준 공통 상태 코드:  
- 400 Bad Request: 검증 실패, 예약/시간/테마 중복, 예약 제약 위반.  
- 404 Not Found: 리소스를 찾을 수 없음.  
- 409 Conflict: 의존성이 있어 삭제 불가.  
- 500 Internal Server Error: 서버 내부 예외.  

## 테마
### GET /themes
모든 테마 목록을 조회합니다.

응답: 200 OK

```JSON
[
{
"id": 1,
"name": "Roomescape A",
"description": "...",
"thumbnail": "https://example.com/a.png"
}
]
```
### GET /themes/rank
인기 테마 순위를 조회합니다.

쿼리 파라미터:
- sort (기본값: reservationCount)
- order (기본값: DESC)
- startDate (선택, 형식 yyyy-MM-dd)
- endDate (선택, 형식 yyyy-MM-dd)
- limit (기본값: 10)
서버 기본값:
- endDate: 오늘 날짜
- startDate: endDate - 7 days

- 응답: 200 OK

### POST /admin/themes
새로운 테마를 추가합니다.

요청 본문:
``` JSON
{
"name": "Roomescape A",
"description": "...",
"thumbnail": "https://example.com/a.png"
}
```

응답: 201 Created

### DELETE /admin/themes/{id}
테마를 삭제합니다.

응답: 204 No Content

## 예약 시간
### GET /times
등록된 모든 예약 시간 목록을 조회합니다.

응답: 200 OK
```JSON
[
{
"id": 1,
"startAt": "10:00:00"
}
]
```
### POST /admin/times
새로운 예약 시간을 추가합니다.

요청 본문:

```JSON
{
"startAt": "10:00:00"
}
```
응답: 201 Created

### DELETE /admin/times/{id}
예약 시간을 삭제합니다.

응답: 204 No Content

## 스케줄
### GET /times/{themeId}
특정 테마와 날짜의 예약 가능 스케줄을 조회합니다.

쿼리 파라미터:
- date (필수, 형식 yyyy-MM-dd)

응답: 200 OK

``` JSON
{
"themeId": 1,
"date": "2026-05-01",
"schedules": [
{
"timeId": 1,
"startAt": "10:00:00",
"isAvailable": true
}
]
}
```
## 예약
### GET /reservations
본인의 예약 목록을 조회합니다.

응답: 200 OK

``` JSON
[
{
"id": 1,
"themeResponse": {
"id": 2,
"name": "Roomescape A",
"description": "...",
"thumbnail": "https://example.com/a.png"
},
"name": "Brown",
"date": "2026-05-01",
"time": {
"id": 1,
"startAt": "10:00:00"
}
}
]
```

### POST /reservations
새로운 예약을 생성합니다.

요청 본문:

``` JSON
{
"themeId": 2,
"name": "Brown",
"date": "2026-05-01",
"timeId": 1
}
```
응답: 201 Created

### DELETE /reservations/{id}
본인의 예약을 취소합니다. (본인 확인을 위해 이름을 포함합니다)

요청 본문 (JSON):

``` JSON
{
"name": "Brown"
}
```
응답: 204 No Content

## 관리자 예약
### POST /admin/reservations
관리자 권한으로 예약을 강제 생성합니다.

요청 본문:

``` JSON
{
"themeId": 2,
"name": "Brown",
"date": "2026-05-01",
"timeId": 1
}
```
응답: 201 Created

### DELETE /admin/reservations/{id}
관리자 권한으로 예약을 삭제합니다.

응답: 204 No Content