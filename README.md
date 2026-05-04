# 방탈출 사용자 예약 설계

## 1 단계 - 테마 도메인 추가

- 규칙 1. 테마는 리소스인가?
    - 맞다.
    - 왜?
    - 독립적인 멘탈 모델이니까
- 규칙 2.
- 규칙 3.
- 규칙 4. 테마의 API 명세는 어떻게 작성해야?
    - 추가 PUT /themes
    - 삭제 DELETE /themes
    - 조회 GET /themes

## 2단계 - 사용자 예약

### 1. 테마들의 모든 정보 조회 API

```java
GET /themes HTTP/1.1 
```

### 2. 사용자가 (날짜, 테마)를 통해 예약 가능 시간 조회 API

```java
GET /times?date=2026-05-04&themeId=1 HTTP/1.1 
```

### 3. 사용자가 날짜, 테마, 예약 시간을 통해 예약 등록 API

```java
POST /reservations HTTP/1.1 

{
	"themeId": 1,
	"date": "2026-05-03",
	"timeId" = 1,
	"name": "브라운"
}
```

#### 요청과 응답은 1단계의 진행 과정을 통해 구체화

## 3단계 - 인기 테마 조회

### 1. 최근 1주 동안 예약이 많았던 테마 상위 10개를 조회한다.

- 반환값은 테마 10개.

```jsx
GET /theme/popular HTTP/1.1 
GET /theme?top=10&during=7 HTTP/1.1 
```
