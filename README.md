# 방탈출 사용자 예약 설계

## 1 단계 - 테마 도메인 추가

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

### API 설계의 근거

테마는 독립적인 리소스이기에 단독 엔드포인트로,  
API 설계 기본 원칙에 따라 행위를 HTTP 메서드로 표현

### 테마 도메인

- 이름
- 설명
- 썸네일 이미지 URL
- 시작 시간과 소요 시간은 동일하다고 가정한다.

### 기능 목록

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

[마이찬]()의 실험 기록을 참조해,  
정리된 API 명세를 기반으로 Claude 를 통해 React.js 클라이언트 제작


---

## 2단계 - 사용자 예약

### 1. 테마들의 모든 정보 조회 API

```java
GET /
themes HTTP/1.1 
```

### 2. 사용자가 (날짜, 테마)를 통해 예약 가능 시간 조회 API

```java
GET /times?date=2026-05-04&themeId=1HTTP/1.1 
```

### 3. 사용자가 날짜, 테마, 예약 시간을 통해 예약 등록 API

```java
POST /
reservations HTTP/1.1

        {
        "themeId":1,
        "date":"2026-05-03",
        "timeId"=1,
        "name":"브라운"
        }
```

#### 요청과 응답은 1단계의 진행 과정을 통해 구체화

## 3단계 - 인기 테마 조회

### 1. 최근 1주 동안 예약이 많았던 테마 상위 10개를 조회한다.

- 반환값은 테마 10개.

```jsx
GET / theme / popular
HTTP / 1.1
GET / theme ? top = 10 & during = 7 HTTP / 1.1 
```
