# 사용 예시

## 접근 방법

애플리케이션 실행 후 아래의 링크로 접속할 수 있습니다.

- 관리자 페이지: [localhost:8080/admin](http://localhost:8080/admin)
    ```plaintext
      테스트 관리자 계정
      email: sooyang@woowa.com
      password: 1234
    ```

- 사용자 페이지: [localhost:8080/](http://localhost:8080/)
    ```plaintext
  테스트 사용자 계정
  email: user@woowa.com
  password: user1234
   ```

## 명세

### 사용자

| 기능       | Method | URL            | 파라미터 / Path Variable | Body                        |
|----------|--------|----------------|----------------------|-----------------------------|
| 로그인      | POST   | `/login`       | -                    | `email`, `password`         |
| 로그인 확인   | GET    | `/login/check` | -                    | -                           |
| 로그아웃     | POST   | `/logout`      | -                    | -                           |
| 회원 전체 조회 | GET    | `/members`     | -                    | -                           |
| 회원 가입    | POST   | `/members`     | -                    | `email`, `password`, `name` |

### 예약

| 기능       | Method | URL                             | 파라미터 / Path Variable | Body                        |
|----------|--------|---------------------------------|----------------------|-----------------------------|
| 예약 전체 조회 | GET    | `/reservations`                 | -                    | -                           |
| 예약 상세 조회 | GET    | `/reservations/{reservationId}` | `reservationId`      | -                           |
| 예약 생성    | POST   | `/reservations`                 | -                    | `themeId`, `timeId`, `date` |
| 예약 삭제    | DELETE | `/reservations/{reservationId}` | `reservationId`      | -                           |
| 조건부 예약 조회   | GET    | `/admin/reservations` | `themeId`, `memberId`, `dateFrom`, `dateTo` | -                                       |
| 예약 생성 (관리자) | POST   | `/admin/reservations` | -                                           | `themeId`, `memberId`, `date`, `timeId` |

### 예약 시간

| 기능          | Method | URL                  | 파라미터 / Path Variable | Body                   |
|-------------|--------|----------------------|----------------------|------------------------|
| 예약 시간 전체 조회 | GET    | `/times`             | -                    | -                      |
| 예약 가능 시간 조회 | GET    | `/times/reservation` | `date`, `themeId`    | -                      |
| 특정 예약 시간 조회 | GET    | `/times/{timeId}`    | `timeId`             | -                      |
| 예약 시간 등록    | POST   | `/times`             | -                    | `startTime`, `endTime` |
| 예약 시간 삭제    | DELETE | `/times/{timeId}`    | `timeId`             | -                      |

### 테마

| 기능       | Method | URL                 | 파라미터 / Path Variable | Body                  |
|----------|--------|---------------------|----------------------|-----------------------|
| 테마 전체 조회 | GET    | `/themes`           | -                    | -                     |
| 인기 테마 조회 | GET    | `/themes/ranks`     | -                    | -                     |
| 테마 등록    | POST   | `/themes`           | -                    | `name`, `description` |
| 테마 삭제    | DELETE | `/themes/{themeId}` | `themeId`            | -                     |

# 구현할 기능 목록

- [x] 0단계 준비
- 발생할 수 있는 예외 상황에 대한 처리 구현
    - [x] 유효하지 않은 값이 입력되는 경우
        - [x] null
    - [x] 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제할 수 없다.
    - [x] 과거일시로 예약할 수 없다.
    - [x] 중복된 일시와 테마로 예약할 수 없다.
- 방탈출 테마 추가
    - [x] 테마 테이블 및 예약 테이블 외래키 추가
    - 관리자 기능 구현
        - [x] 테마 관련 화면 추가
        - [x] 전체 조회
        - [x] 테마 추가
            - [x] 동일한 이름의 테마를 추가할 수 없다.
        - [x] 테마 삭제
            - [x] 특정 테마에 대한 예약이 존재하는데, 그 테마를 삭제할 수 없다.
- 사용자 기능 구현
    - [x] 사용자 예약 화면 추가
    - [x] 사용자가 예약 가능한 시간 조회
        - [x] 이미 예약된 시간은 선택할 수 없다.
    - [x] 사용자 예약
    - 인기 테마 조회
        - [x] 인기 테마 화면 추가
        - [x] 최근 일주일 기준 방문하는 예약이 많은 테마 10개
    - 로그인 기능 구현
        - [x] 로그인 화면 추가
        - [x] 회원가입 화면 추가
        - [x] 사용자 도메인 추가
        - [x] 로그인 기능 구현
        - [x] Cookie로 사용자 정보 조회
    - 관리자 기능 구현
        - [x] 관리자 페이지 접근 제한 기능 추가
        - [x] 예약 목록 검색 기능 추가
