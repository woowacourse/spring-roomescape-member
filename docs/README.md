# 요구사항

> 사이클1 - 관리자 여부(인가)는 판단하지 않는다. 인가는 구현하지 않는다.

> 사이클2 - 변경·취소 API 명세, 에러 응답의 상태 코드와 본문 형식은 직접 결정한다.

---

## 1. Reservation

`status` : `RESERVED` | `CANCELED`

### 관리자

**예약을 한다.**
- 날짜와 테마를 선택하고 예약 가능한 시간을 선택하면 예약할 수 있다.
- `timeId`, `themeId`, `name`, `date`를 입력받는다.
- 예약의 기본 상태는 `RESERVED`이다.

**예약을 취소한다.**
- 해당 API를 사용한다는 게, 관리자임을 보장한다고 가정한다. 즉, 관리자인지 별도로 검증하지 않는다.
- 예약 상태를 `CANCELED`로 변경한다.

**예약을 조회한다.**
- 전체 예약을 조회한다.

### 사용자

**예약을 조회한다.**
- `name`(예약자 성함)을 Query Parameter로 받아 해당 예약자의 예약 목록을 조회한다.
- 예약 날짜 및 시간 기준 오름차순으로 정렬한다.

**예약을 한다.**
- 날짜와 테마를 선택하고 예약 가능한 시간을 선택하면 예약할 수 있다.
- `timeId`, `themeId`, `name`, `date`를 입력받는다.
- 예약의 기본 상태는 `RESERVED`이다.

**예약을 변경한다.**
- 사용자가 본인의 예약의 날짜·시간을 변경할 수 있다.
- 변경하려는 날짜·시간+테마가 이미 예약되어 있으면 거부한다.
- 이미 지난 예약은 변경할 수 없다.
- 변경하려는 날짜가 휴무일이면 거부한다.
- 변경하려는 날짜·시간이 과거이면 거부한다.

**예약을 취소한다.**
- 예약 상태를 `CANCELED`로 변경한다.
- 이미 지난 예약은 취소할 수 없다.

---

## 2. ClosedDate & AvailableDate

### 관리자

**휴무일을 생성한다.**
- `date`를 보내 생성한다.
- 이미 존재하는 `date`라면 예외가 발생한다.

**휴무일을 조회한다.**
- `id`, `date`를 포함한 `List<ClosedDateDetailDto>`를 반환한다.

**휴무일을 삭제한다.**
- hard-delete 방식으로 삭제한다.

### 사용자

**예약 가능한 날짜를 조회한다.**
- 오늘부터 30일치 날짜 중 휴무일을 제외한 날짜 목록을 반환한다.

---

## 3. ReservationTime

### 관리자

**예약 가능한 시간을 생성한다.**
- `startAt`을 보내 생성한다.
- 이미 존재하는 `startAt`이라면 예외가 발생한다.

**예약 가능한 시간을 삭제한다.**
- hard-delete 방식으로 삭제한다.
- `RESERVED` 상태의 예약이 존재하는 시간은 삭제할 수 없다.

### 사용자

**예약 가능한 시간을 조회한다.**
- `date`와 `themeId`를 받아 해당 날짜+테마에 `RESERVED` 예약이 없는 시간만 반환한다.

---

## 4. Theme

`isActive` : `true` | `false`

### 관리자

**테마를 생성한다.**
- `name`, `description`, `thumbnailUrl`을 받아 생성한다.
- 기본 상태는 비활성화(`isActive: false`)이다.

**모든 테마를 조회한다.**
- 활성화/비활성화 여부 포함 전체 목록을 반환한다.

**테마의 상태를 변경한다.**
- 활성화/비활성화를 전환한다.

### 사용자

**인기 테마 Top10을 조회한다. (추천용)**
- 최근 7일 기준, 취소를 포함하지 않은(`RESERVED`) 예약 수 Top 10 테마를 조회한다.
- 활성화된 테마만 포함한다.
- 순위만 반환한다. (예약 수 X)

**활성화된 테마 목록을 조회한다.**
- 가나다순 정렬하여 반환한다.

---

## 에러 응답 명세

### 응답 형식

```json
{
  "status": 400,
  "message": "예약자 이름은 필수 항목입니다.",
  "timestamp": "2026-05-14T12:00:00"
}
```

### 상태 코드별 케이스

| 상태 코드 | 케이스 |
|---|---|
| 400 Bad Request | 유효하지 않은 입력값 (빈 이름, 잘못된 날짜 형식 등) |
| 400 Bad Request | 과거 날짜/시간으로 예약 생성 또는 변경 시도 |
| 400 Bad Request | 예약 불가능한 날짜(휴무일)로 예약 시도 |
| 400 Bad Request | 이미 지난 예약 취소/변경 시도 |
| 404 Not Found | 존재하지 않는 리소스 요청 |
| 409 Conflict | 같은 날짜/시간/테마에 이미 예약 존재 |
| 409 Conflict | 이미 존재하는 예약 시간 또는 휴무일 등록 시도 |
| 409 Conflict | 예약이 존재하는 시간 삭제 시도 |
| 500 Internal Server Error | 예상치 못한 서버 오류 |

---

## ERD

```mermaid
erDiagram
    THEME ||--o{ RESERVATION : "하나의 테마는 여러 예약을 가짐"
    RESERVATION_TIME ||--o{ RESERVATION : "특정 시간에 여러 예약 가능"

    RESERVATION {
        Long id PK
        String name
        Date date
        Time start_at
        Long theme_id FK "theme(id) 참조"
        Enum status "RESERVED | CANCELED"
    }

    THEME {
        Long id PK
        String name
        String description
        String thumbnail_url
        Boolean is_active
    }

    CLOSED_DATE {
        Long id PK
        Date date UNIQUE
    }

    RESERVATION_TIME {
        Long id PK
        Time start_at UNIQUE
    }
```
