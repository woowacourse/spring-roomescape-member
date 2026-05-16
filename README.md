# Spring Roomescape Member API

## 공통 규칙

- Base URL: 미정 (서버 호스트/포트를 사용하세요)
- Content-Type: JSON 요청/응답은 application/json
- 날짜 형식: yyyy-MM-dd (예: 2026-05-01)
- 시간 형식: HH:mm:ss (예: 10:00:00)
- 화면 확인 링크
    - http://localhost:8080/
    - http://localhost:8080/admin.html

## 오류 응답

### 공통 응답 본문

```json
{
  "message": "에러 메시지",
  "timestamp": "2026-05-01T12:34:56.789"
}
```

### GlobalExceptionHandler 기준 공통 상태 코드

- 400 Bad Request: 검증 실패, 예약/시간/테마 중복, 예약 제약 위반
- 404 Not Found: 리소스를 찾을 수 없음
- 409 Conflict: 의존성이 있어 삭제 불가
- 500 Internal Server Error: 서버 내부 예외

## API  명세서

### 테마

#### GET /themes

- 모든 테마 목록을 조회합니다.
- 응답: 200 OK

```json
[
  {
    "id": 1,
    "name": "Roomescape A",
    "description": "...",
    "thumbnail": "https://example.com/a.png"
  }
]
```

#### GET /themes/rank

- 인기 테마 순위를 조회합니다.
- 쿼리 파라미터:
    - sort (기본값: reservationCount)
    - order (기본값: DESC)
    - startDate (선택, 형식 yyyy-MM-dd)
    - endDate (선택, 형식 yyyy-MM-dd)
    - limit (기본값: 10)
- 서버 기본값:
    - endDate: 오늘 날짜
    - startDate: endDate - 7 days
- 응답: 200 OK

#### POST /admin/themes

- 새로운 테마를 추가합니다.
- 요청 본문

    ```json
    {
      "name": "Roomescape A",
      "description": "...",
      "thumbnail": "https://example.com/a.png"
    }
    ```

- 응답: 201 Created

#### DELETE /admin/themes/{id}

- 테마를 삭제합니다.
- 응답: 204 No Content

### 예약 시간

#### GET /times

- 등록된 모든 예약 시간 목록을 조회합니다.
- 응답: 200 OK

```json
[
  {
    "id": 1,
    "startAt": "10:00:00"
  }
]
```

#### POST /admin/times

- 새로운 예약 시간을 추가합니다.
- 요청 본문:

    ```json
    {
      "startAt": "10:00:00"
    }
    ```

- 응답: 201 Created

#### DELETE /admin/times/{id}

- 예약 시간을 삭제합니다.
- 제약 사항:
    - 삭제하려는 예약 시간을 참조하고 있는 예약 데이터가 하나라도 존재할 경우 삭제가 불가능합니다.
- 응답:
    - 204 No Content: 삭제 성공
    - 404 Not Found: 존재하지 않는 시간 ID인 경우
    - 409 Conflict: 예약된 내역이 있어 삭제할 수 없는 경우

### 스케줄

#### GET /times/{themeId}

- 특정 테마와 날짜의 예약 가능 스케줄을 조회합니다.
- 쿼리 파라미터:
    - date (필수, 형식 yyyy-MM-dd)
- 응답: 200 OK

    ```json
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


### 예약

#### GET admin/reservations

- 모든 예약 목록을 조회합니다.
- `GET /admin/reservations`와 같이 정상적으로 요청한 경우
    - 응답: 200 OK

        ```json
        [
            {
                "id": 1,
                "name": "User1",
                "date": "2026-05-01",
                "time": {
                    "id": 1,
                    "startAt": "10:00:00"
                },
                "theme": {
                    "id": 1,
                    "name": "Theme A",
                    "description": "Desc A",
                    "thumbnail": "https://picsum.photos/id/1011/200/300"
                }
            },
            {
                "id": 2,
                "name": "User2",
                "date": "2026-05-02",
                "time": {
                    "id": 2,
                    "startAt": "11:00:00"
                },
                "theme": {
                    "id": 1,
                    "name": "Theme A",
                    "description": "Desc A",
                    "thumbnail": "https://picsum.photos/id/1011/200/300"
                }
            },
            ...
        ]
        ```


#### **GET /reservations**

- 본인(이름)의 예약 목록을 조회합니다.
- 쿼리 파라미터:
    - `name` (필수, 문자열): 조회하고자 하는 예약자의 이름 (예: `?name=User1`)
- `GET /reservations?name=User1`와 같이 정상적으로 요청한 경우
    - 응답: 200 OK

        ```json
        [
            {
                "id": 1,
                "name": "User1",
                "date": "2026-05-01",
                "time": {
                    "id": 1,
                    "startAt": "10:00:00"
                },
                "theme": {
                    "id": 1,
                    "name": "Theme A",
                    "description": "Desc A",
                    "thumbnail": "https://picsum.photos/id/1011/200/300"
                }
            }
        ]
        ```

- `GET /reservations?name=`와 같이 이름을 입력하지 않은 경우
    - 응답: 400 Bad Request

        ```json
        {
            "message": "조회할 예약자 이름은 필수입니다.",
            "timestamp": "2026-05-16T14:22:11.690"
        }
        ```

- `GET /reservations?na`와 같이 필수 파라미터를 입력하지 않은 경우
    - 응답: 400 Bad Request

        ```json
        {
            "message": "필수 요청 파라미터(name)가 누락되었습니다.",
            "timestamp": "2026-05-16T14:24:25.929"
        }
        ```


#### POST /reservations

- 새로운 예약을 생성합니다.
- 요청 본문:

    ```json
    {
      "name": "Brown",
      "date": "2026-05-01",
      "timeId": 1,
      "themeId": 2
    }
    ```

- 제약 사항:
    - `date`와 `timeId`에 해당하는 시작 시간이 현재 시간보다 이전일 경우 예약을 생성할 수 없습니다.
- 응답:
    - 201 Created: 예약 성공
    - 400 Bad Request:
        - 이미 예약된 시간인 경우
        - 지난 날짜나 시간으로 예약을 시도하는 경우
        - 필수 값이 누락된 경우

#### DELETE /reservations/{id}

- 본인의 예약을 취소합니다. (본인 확인을 위해 이름을 포함합니다)
- 정상적인 요청 본문과 `DELETE /reservations/40`를 요청한 경우

    ```json
    {
        "name": "Brown"
    }
    ```

    - 응답: 204 No Content
- 정상적인 요청 본문과 `DELETE /reservations/40`를 한 번 더 요청한 경우

    ```json
    {
        "name": "Brown"
    }
    ```

    - 응답: 404 Not Found

        ```json
        {
            "message": "해당 예약을 찾을 수 없습니다. id: 40",
            "timestamp": "2026-05-16T18:47:00.150"
        }
        ```

- 지난 예약을 삭제하려고 한 경우 (`DELETE /reservations/1`)

    ```json
    {
        "name": "User1"
    }
    ```

    - 응답: 400 Bad Request

        ```json
        {
            "message": "지난 예약은 삭제할 수 없습니다.",
            "timestamp": "2026-05-16T18:42:11.818"
        }
        ```


#### PATCH /reservations/{id}

- 본인의 예약의 날짜와 시간을 변경합니다.
- 정상적인 요청 본문과 `PATCH /reservations/1`를 요청한 경우

    ```json
    {
        "name": "User1",
        "date": "2026-05-17",
        "timeId": 5
    }
    ```

    - 응답: 200 OK

        ```json
        {
            "id": 1,
            "name": "User1",
            "date": "2026-05-17",
            "time": {
                "id": 5,
                "startAt": "14:00:00"
            },
            "theme": {
                "id": 1,
                "name": "Theme A",
                "description": "Desc A",
                "thumbnail": "https://picsum.photos/id/1011/200/300"
            }
        }
        ```

- 정상적인 요청 본문과 `PATCH /reservations/1`를 한 번 더 요청한 경우

    ```json
    {
        "name": "User1",
        "date": "2026-05-17",
        "timeId": 5
    }
    ```

    - 응답: 400 Bad Request

        ```json
        {
            "message": "해당 날짜와 시간, 테마는 이미 예약이 완료되었습니다.",
            "timestamp": "2026-05-16T18:03:23.633"
        }
        ```

- 잘못된 요청 본문(본인이 아닌 다른 예약자 이름)과 `PATCH /reservations/1`를 요청한 경우

    ```json
    {
        "name": "User3",
        "date": "2026-05-17",
        "timeId": 5
    }
    ```

    - 응답: 401 Unauthorized

        ```json
        {
            "message": "예약자 이름이 일치하지 않아 수정할 수 없습니다.",
            "timestamp": "2026-05-16T17:29:18.843"
        }
        ```

- 잘못된 요청 본문(없는 예약 시간)과 `PATCH /reservations/1`를 요청한 경우

    ```json
    {
        "name": "User1",
        "date": "2026-05-17",
        "timeId": 50
    }
    ```

    - 응답: 404 Not Found

        ```json
        {
            "message": "예약 시간을 찾을 수 없습니다.",
            "timestamp": "2026-05-16T18:04:32.273"
        }
        ```

- 잘못된 요청 본문(오늘 이전 날짜)과 `PATCH /reservations/1`를 요청한 경우

    ```json
    {
        "name": "User1",
        "date": "2026-05-01",
        "timeId": 5
    }
    ```

    - 응답: 400 Bad Request

        ```json
        {
            "message": "예약 날짜는 오늘 이후여야 합니다.",
            "timestamp": "2026-05-16T18:05:27.028"
        }
        ```

- 잘못된 요청 본문(오늘이지만 지난 시간)과 `PATCH /reservations/1`를 요청한 경우

    ```json
    {
        "name": "User1",
        "date": "2026-05-16",
        "timeId": 5
    }
    ```

    - 응답: 400 Bad Request

        ```json
        {
            "message": "예약 시간은 현재 시간 이후여야 합니다.",
            "timestamp": "2026-05-16T18:06:48.896"
        }
        ```

- 정상적인 요청 본문과 `PATCH /reservations/999`를 요청한 경우

    ```json
    {
        "name": "User1",
        "date": "2026-05-17",
        "timeId": 5
    }
    ```

    - 응답: 404 Not Found

        ```json
        {
            "message": "해당 예약을 찾을 수 없습니다. id: 999",
            "timestamp": "2026-05-16T17:29:51.671"
        }
        ```


### 관리자 예약

#### POST /admin/reservations

- 관리자 권한으로 예약을 강제 생성합니다.
- 요청 본문

    ```json
    {
      "name": "Brown",
      "date": "2026-05-01",
      "timeId": 1,
      "themeId": 2
    }
    ```

- 응답: 201 Created

#### DELETE /admin/reservations/{id}

- 관리자 권한으로 예약을 삭제합니다.
- 응답: 204 No Content
