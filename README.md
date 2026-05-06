# 방탈출 예약 애플리케이션

## API

### 예약 (`/reservations`)

| 메서드 | 경로 | 설명 |
|--------|------|------|
| `GET` | `/reservations` | 예약 목록 조회 |
| `POST` | `/reservations` | 예약 생성 |
| `DELETE` | `/reservations/{id}` | 예약 삭제 |

### 예약 시간 (`/times`)

| 메서드 | 경로                       | 설명                      |
|--------|--------------------------|-------------------------|
| `GET` | `/times`                 | 등록된 예약 시간 전체 조회      |
| `POST` | `/times`                 | 예약 시간 추가             |
| `GET` | `/times/available-times` | 예약 가능한 시간 조회 |
| `DELETE` | `/times/{id}`            | 예약 시간 삭제             |

### 테마 (`/themes`)

| 메서드 | 경로 | 설명 |
|--------|------|------|
| `GET` | `/themes` | 테마 목록 조회 |
| `POST` | `/themes` | 테마 등록 |
| `DELETE` | `/themes/{id}` | 테마 삭제 |
| `GET` | `/themes/most-reserved-themes` | 인기 테마 조회 |

---

## 웹 프론트엔드

서버를 실행한 후 `http://localhost:8080/` 경로에 접속해 서비스를 이용할 수 있습니다.
