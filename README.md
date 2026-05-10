## 구현할 기능 목록

### 테마
```
class Theme
- id
- name  
- description
- thumbnail
```

### 방탈출 API 명세
| 기능              | 메서드 / URL                               | 요청                                  | 응답                                          | 상태 코드 |
|-----------------|-----------------------------------------|-------------------------------------|---------------------------------------------| ----- |
| 예약 조회           | GET `/reservations`                     | —                                   | `[{id, name, date, time, theme}, ...]`      | 200   |
| 예약 등록           | POST `/reservations`                    | `{name, date, timeId, themeId}`     | `{id, name, date, time, theme}`             | 201   |
| 예약 삭제           | DELETE `/reservations/{id}`             | —                                   |                                             | 204   |
| 시간 조회           | GET `/times`                            | —                                   | `[{id, startAt}, ...]`                      | 200   |
| 시간 등록           | POST `/times`                           | `{startAt}`                         | `{id, startAt}`                             | 201   |
| 시간 삭제           | DELETE `/times/{id}`                    | —                                   |                                             | 204   |
| 테마 조회           | GET `/themes`                           | —                                   | `[{id, name, description, thumbnail}, ...]` | 200   |
| 테마 등록           | POST `/themes`                          | `{name, description, thumbnail}`    | `{id, name, description, thumbnail}`        | 201   |
| 테마 삭제           | DELETE `/themes/{id}`                   | —                                   |                                             | 204   |
| 예약 가능 시간 조회     | GET `/themes/{id}/times?date=2026-05-08` | —                                   | `[{time, available}, ...]`                  | 200   |
| 인기 테마 상위 10개 조회 | GET `/themes/weekly-top`                | —                                   | `[{id, name, description, thumbnail}, ...]` | 200   |

### 1단계 - 테마 도메인 추가
- [X] 테마 테이블 스키마 추가
- [X] 초기 테마 데이터 삽입 쿼리문 추가
- [X] Theme 클래스 추가
    - [X] 검증 추가
- [X] 예약에 테마 필드 추가
- [X] reservation 스키마 수정(테마를 참조하도록)
- [X] reservation 테마 필드 null 검증 추가
- [X] 테마 조회, 등록, 삭제 API 구현

### 2단계 - 사용자 예약
- [X] 예약 가능한 시간 목록 조회 API 구현
- [X] 클라이언트 화면 추가
  - [X] 메인 페이지 추가
  - [X] 예약 페이지 추가

### 3단계 - 인기 테마 조회
- [X] 인기 테마 상위 10개 조회 기능 추가

### 예약 더미 데이터
| 순위 | theme_id | 테마명 | 예약 횟수 |
|------|----------|--------|----------|
| 1위 | 12 | 한밤중의 도서관 | 14회 |
| 2위 | 7 | 9회말 2사 만루 | 12회 |
| 3위 | 4 | 녹화된 마지막 하루 | 11회 |
| 4위 | 1 | 시간조작자 연구소 | 9회 |
| 5위 | 9 | 잊혀진 기억의 숲 | 8회 |
| 6위 | 2 | 사라진 개발자 | 7회 |
| 7위 | 8 | 새벽 2시의 증류소 | 7회 |
| 8위 | 3 | 404호의 비밀 | 6회 |
| 9위 | 6 | 버그 추적자: 죽음의 디버깅 | 6회 |
| 10위 | 11 | 인공지능의 반란 | 6회 |
| 11위 | 5 | VIP 전용 금고 | 4회 |
| 12위 | 10 | 명탐정의 마지막 조각 | 3회 |
