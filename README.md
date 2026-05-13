# 구현 기능 목록

## 1단계 - 테마 도메인 추가

- 방탈출 게임에 '테마' 정보를 추가한다.
- 테마는 이름, 설명, 썸네일 이미지 URL을 가진다.
- 모든 테마의 시작 시간과 소요 시간은 동일하다고 가정한다.
    - [x] 테마 테이블을 만든다.
    - [x] 테마 도메인 클래스를 만든다.
    - [x] 관련 DTO 클래스를 만든다.
- 예약에 테마 정보를 포함하도록 기존 코드를 변경한다.
    - [x] 예약 테이블과 도메인 클래스에 테마ID 속성을 추가한다.
- [x] 사용자의 테마 조회 API를 구현한다.
- [x] 관리자의 테마 조회 API를 구현한다.
- 관리자가 테마를 추가·삭제할 수 있다.
    - [x] 관리자의 테마 추가 API를 구현한다.
    - [x] 관리자의 테마 삭제 API를 구현한다.

## 2단계 - 사용자 예약

- 사용자가 날짜와 테마를 선택하면 예약 가능한 시간 목록이 표시된다.
- 예약 가능한 시간이란, 관리자가 등록한 시간 중 해당 날짜+테마에 아직 예약이 없는 시간이다.
    - [x] 전체 예약에서 지정된 날짜와 테마에 해당하는 예약들의 시간을 조회한다.
    - [x] 전체 시간 목록에서 조회된 시간들을 제외한다.

- 테마와 날짜를 쿼리 파라미터로 사용
    - 같은 날짜와 시간이라도 테마가 다르면 예약이 가능해야 하므로, 테마 ID를 조건으로 포함하여 조회하도록 설계했습니다.

- 사용자가 예약 가능한 시간을 선택하여 본인의 이름으로 예약한다.
    - 이전 미션에서 구현한 예약 생성 API를 사용한다.
- 같은 날짜·시간이라도 테마가 다르면 각각 예약 가능하다.
    - [x] 같은 날짜, 시간, 테마인 예약이 이미 존재하면 예외를 발생시킨다.

## 3단계 - 인기 테마 조회

- 최근 1주 동안 예약이 많았던 테마 상위 10개를 조회한다.
- 예: 오늘이 5월 8일이면, 게임 날짜가 5월 1일~5월 7일인 예약을 집계해 인기 순서대로 10개를 응답한다.
- [x] 전체 테마 목록을 가져오면서, 예약 정보 테이블에서 각 테마당 예약 횟수의 총합도 가져온다.

## 4단계 - 서비스 정책 적용

- 지나간 날짜·시간에 대한 예약 생성은 불가능하다.
    - [x] 지나간 날짜, 시간에 예약을 요청하면 422 에러를 발생시킨다.
- 같은 날짜+시간+테마에 이미 예약이 있으면 중복 예약을 거부한다.
    - [x] 중복 예약을 요청하면 409 에러를 발생시킨다.
- 예약이 존재하는 시간을 삭제할 수 없다.
    - [ ] 예약이 존재하는 시간을 삭제 요청하면 409 에러를 발생시킨다.
- 유효하지 않은 입력값(빈 이름, 잘못된 날짜 형식 등)을 거부한다.
    - [ ] 유효하지 않은 입력값이 들어오면 400 에러를 발생시킨다.
- 랭킹 조회 기간이 유효하지 않으면 거부한다.
    - [ ] 종료 날짜와 시작 날짜 모두 미래일 수 없다.
    - [ ] 조회 종료 날짜가 시작 날짜보다 뒤여야 한다.
    - [ ] 조회 기간은 1년 이내여야 한다.
- 중복된 예약 시간 추가를 거부한다.
    - [ ] 중복된 예약 시간 추가를 요청하면 409 에러를 발생시킨다.

## 5단계 - 에러 응답 설계

- 서비스 정책 위반, 유효하지 않은 입력, 존재하지 않는 리소스 등에 대해 의도된 에러 응답을 반환한다.
    - [ ] (우선순위) 상태 코드 선택 순서:
        1) 리소스가 존재하지 않는가? → 404
        2) 요청 형식·필수값이 잘못됐는가? → 400
        3) DB 제약조건이 걸린(걸려야 하는) 규칙 위반인가? → 409
           (중복 예약, 외래 키 참조 중인 리소스 삭제 등)
        4) 나머지 비즈니스 규칙 위반 → 422
           (과거 날짜 예약, 운영 시간 외 예약 등)
- 500(서버 에러)이 사용자에게 노출되지 않도록 한다.
    - [ ] 요청 url이 잘못된 경우 400 에러를 발생시킨다.
- 에러 응답 본문에 어떤 정보를 담을지 결정한다.
    - [ ] 응답 코드 + 메시지를 담는다.
- 브라우저에서 에러 발생 시 사용자에게 의미 있는 메시지가 표시되어야 한다.
    - [ ] 클라이언트가 행동을 바꿔 재시도 가능한 상황이면 구체적인 다음 행동을 제시한다.
        - ex) 이미 예약이 존재하는 시간에 예약을 요청할 때: "[ERROR] 해당 시간에 예약이 이미 존재합니다. 다른 시간을 선택해 주세요."

## 6단계 - 내 예약 조회/변경/취소

- 사용자가 자신의 이름으로 본인의 예약 목록을 조회할 수 있다.
    - [ ] 예약 목록이 없다면 빈 리스트를 반환한다.
- 사용자가 본인의 예약을 취소할 수 있다.
    - [ ] 날짜, 시간이 지난 예약을 취소 요청할 경우 예외를 발생시킨다.
- 사용자가 본인의 예약의 날짜·시간을 변경할 수 있다.
    - [ ] 날짜, 시간 이외의 것들의 변경을 요청할 경우 예외를 발생시킨다.
    - [ ] 변경하려는 날짜, 시간이 이미 지난 경우 예외를 발생시킨다.
    - [ ] 변경하려는 날짜, 시간에 예약이 이미 존재할 경우 예외를 발생시킨다.
- 변경·취소 시 발생하는 에러 케이스(이미 지난 예약을 취소, 변경하려는 시간이 이미 차 있음 등)도 2단계의 규칙에 맞춰 처리한다.

## API URL

### 테마 관리 및 조회 API (/admin/themes, /themes)

- 관리자와 사용자의 테마 추가/삭제/조회 분리
    - 관리자만 수행할 수 있는 기능이므로 명확한 역할 분리를 위해 엔드포인트를 나누었습니다.
    - 추후 '테마 비활성화' 기능 도입 시, 관리자는 모든 테마를 봐야 하지만 사용자는 활성 테마만 봐야 합니다.  
      필터링으로 처리할 경우 클라이언트 코드 노출 등의 위험이 있어 서버 레벨에서 미리 분리했습니다.

### 예약 가능한 시간 조회 API (/times/available?date=yyyy-mm-dd&themeId={id})

- /times 경로에 쿼리 파라미터만 추가하는 방식 대신 서브 패스(/available) 분리
    - 단순히 /times에 파라미터를 추가할 경우, 전체 시간 목록을 반환하는 기존 응답과 필터링된 예약 가능 목록을 반환하는 응답의 형식이 달라져 API의 일관성을 해칠 수 있습니다.
    - 따라서 리소스의 성격을 명확히 구분하고 클라이언트의 혼선을 방지하기 위해 /available 서브 패스를 추가했습니다.

### 예약 생성 API (/reservations)

- 사용자 예약과 관리자 예약 분리 X
    - 사용자 예약은 테마의 최대 인원과 예약 요청 인원을 비교하는 검증이 필수적이지만, 관리자 예약은 이러한 검증이 필요 없을 수도 있어 분리를 고려했습니다.  
      다만 현재 단계에서는 관리자 예약에 대한 특별한 요구사항이 없었기 때문에 분리하지 않았습니다.

### 인기 테마 조회 API (/themes/ranking?start-date=...&end-date=...)

- 명시적인 기간(Start/End Date) 설정
    - 상대적 표현(week)은 해당 날짜 기준 일주일 전인지 후인지 모호하지만, 시작과 끝 날짜를 명시하면 설명하기 훨씬 명확합니다.
    - 특정 단위(week, month)로 고정하면 기간 변경 시 URL을 새로 작성해야 하지만, 날짜 범위를 받으면 다양한 기간 요구사항에 유연하게 대응할 수 있습니다.
    - /themes 하위 경로를 사용해 테마 정보를 반환한다는 목적을 직관적으로 드러냈습니다.

## API 명세서

### 공통

- [ ] 요청 형식이 잘못된 경우 (검증 예외, 잘못된 url 등): `Http Status: 400 Bad Request`

    ```text
    {
        "status": "BAD_REQUEST",
        "message": "[ERROR] (~ 이유). 다시 시도해 주세요."
    }
    ```

### Theme

- 관리자의 테마 추가
    - Http Method: POST
    - URL: /admin/themes
    - Request
        ```text
        {
            "name": "피즈의 모험",
            "description": "피즈가 모험을 떠나는 이야기입니다.",
            "thumbnail": "http://localhost:8080/images/fizz.jpg"
        }
        ```
    - Response
        - [x] 정상적으로 추가된 경우: `Http Status: 201 Created`
        ```text
        {
            "id": 1,
            "name": "피즈의 모험",
            "description": "피즈가 모험을 떠나는 이야기입니다.",
            "thumbnail": "http://localhost:8080/images/fizz.jpg"
        }
        ```

- 관리자의 테마 삭제
    - Http Method: DELETE
    - URL: /admin/themes/{id}
    - Response
        - [x] 정상적으로 삭제된 경우: `Http Status: 204 No Content`

        - [x] 해당 테마를 사용하는 예약이 존재할 경우: `Http Status: 409 Conflict`
        ```text
        {
            "status": "CONFLICT",
            "message": "[ERROR] 현재 해당 테마를 사용하는 예약이 존재합니다. 연관된 예약을 삭제한 후 다시 시도해 주세요."
        }
        ```

- 전체 테마 조회
    - Http Method: GET
    - URL: /themes
    - Response
        - [x] 정상적으로 조회된 경우: `Http Status: 200 OK`
        ```text
        [
            {
                "id": 1,
                "name": "피즈의 모험",
                "description": "피즈가 모험을 떠나는 이야기입니다.",
                "thumbnail": "http://localhost:8080/images/fizz.jpg"
            },
            {
                "id": 2,
                "name": "나무의 일대기",
                "date": "나무가 살아온 인생을 보여주는 이야기입니다.",
                "thumbnail": "http://localhost:8080/images/tree.jpg"
            }
        ]
        ```

- 인기 테마 조회
    - Http Method: GET
    - URL: /themes/ranking?start-date={start-date}&end-date={end-date} / date 형식: yyyy-mm-dd
    - Response
        - [x] 정상적으로 조회된 경우: `Http Status: 200 OK`
        ```text
        [
            {
                "id": 1,
                "name": "잃어버린 왕국",
                "description": "사라진 고대 왕국의 비밀을 추적하는 모험 테마",
                "thumbnailUrl": "https://example.com/images/lost-kingdom.jpg"
            },
            {
                "id": 2,
                "name": "심야의 연구소",
                "description": "한밤중 폐쇄된 연구소에서 탈출 단서를 찾는 스릴러 테마",
                "thumbnailUrl": "https://example.com/images/midnight-lab.jpg"
            },
            {
                "id": 3,
                "name": "해적선의 저주",
                "description": "저주받은 해적선에서 보물을 찾아 탈출하는 테마",
                "thumbnailUrl": "https://example.com/images/pirate-curse.jpg"
            }
        ]
        ```

### Reservation

- 예약 추가
    - Http Method: POST
    - URL: /reservation
    - Request
        ```text
        {
            "name": "fizz",
            "date": "2026-05-02",
            "timeId": 1,
            "themeId": 1
        }
        ```
    - Response
        - [x] 정상적으로 추가된 경우: `Http Status: 201 Created`
        ```text
        {
            "id": 1,
            "name": "fizz",
            "date": "2026-05-02",
            "time": {
                "id": 1,
                "startAt": "10:00:00"
            }
            "theme": {
                "id": 1,
                "name": "피즈의 모험",
                "description": "피즈가 모험하는 이야기",
                "thumbnailUrl": "1.jpg"
            }
        }
        ```

        - [x] 존재하는 예약과 날짜, 시간, 테마가 동일한 예약일 경우: `Http Status: 409 Conflict`
        ```text
        {
            "status": "CONFLICT",
            "message": "[ERROR] 해당 시간에 예약이 이미 존재합니다. 예약 가능한 시간으로 다시 시도해 주세요."
        }
        ```

        - [x] 지나간 날짜, 시간일 경우: `Http Status: 422 Unprocessable Entity`
        ```text
        {
            "status": "UNPROCESSABLE_ENTITY",
            "message": "[ERROR] 지나간 시간에는 예약할 수 없습니다. 예약 시간을 변경해 주세요."
        }
        ```

- 사용자 이름으로 예약 조회
    - Http Method: GET
    - URL: /reservations
    - Response
        - [ ] 정상적으로 조회된 경우: `Http Status: 200 OK`
        ```text
        [
            {
                "id": 1,
                "name": "예약자01",
                "date": "2026-05-01",
                "time": {
                    "id": 1,
                    "startAt": "10:00:00"
                },
                "theme": {
                    "id": 1,
                    "name": "잃어버린 왕국",
                    "description": "사라진 고대 왕국의 비밀을 추적하는 모험 테마",
                    "thumbnailUrl": "https://example.com/images/lost-kingdom.jpg"
                }
            },
            {
                "id": 2,
                "name": "예약자01",
                "date": "2026-05-02",
                "time": {
                    "id": 2,
                    "startAt": "11:00:00"
                },
                "theme": {
                    "id": 1,
                    "name": "잃어버린 왕국",
                    "description": "사라진 고대 왕국의 비밀을 추적하는 모험 테마",
                    "thumbnailUrl": "https://example.com/images/lost-kingdom.jpg"
                }
            }
        ]
        ```

- 전체 예약 조회
    - Http Method: GET
    - URL: /reservations
    - Response
        - [x] 정상적으로 조회된 경우: `Http Status: 200 OK`
        ```text
        [
            {
                "id": 1,
                "name": "예약자01",
                "date": "2026-05-01",
                "time": {
                    "id": 1,
                    "startAt": "10:00:00"
                },
                "theme": {
                    "id": 1,
                    "name": "잃어버린 왕국",
                    "description": "사라진 고대 왕국의 비밀을 추적하는 모험 테마",
                    "thumbnailUrl": "https://example.com/images/lost-kingdom.jpg"
                }
            },
            {
                "id": 2,
                "name": "예약자02",
                "date": "2026-05-02",
                "time": {
                    "id": 2,
                    "startAt": "11:00:00"
                },
                "theme": {
                    "id": 1,
                    "name": "잃어버린 왕국",
                    "description": "사라진 고대 왕국의 비밀을 추적하는 모험 테마",
                    "thumbnailUrl": "https://example.com/images/lost-kingdom.jpg"
                }
            }
        ]
        ```

- 예약 변경
    - Http Method: PATCH
    - URL: /reservations/{id}
    - Request
        ```text
        {
            "date": "2026-05-03",
            "timeId": 2
        }
        ```
    - Response
        - [x] 정상적으로 변경된 경우: `Http Status: 200 OK`
        ```text
        {
            "name": "fizz",
            "date": "2026-05-03",
            "timeId": 2,
            "themeId": 1
        }
        ```

        - [ ] 이미 지나간 날짜, 시간으로 예약을 변경할 경우: `Http Status: 422 Unprocessable Entity`
        ```text
        {
            "status": "UNPROCESSABLE_ENTITY",
            "message": "[ERROR] 지나간 시간으로 예약을 변경할 수 없습니다."
        }
        ```

        - [ ] 예약이 이미 존재하는 시간으로 변경할 경우: `Http Status: 409 Conflict`
        ```text
        {
            "status": "CONFLICT",
            "message": "[ERROR] 동일한 예약 시간이 이미 존재합니다. 시간을 변경해 다시 시도해 주세요."
        }
        ```

        - [ ] 날짜, 시간이 아닌 변경을 요청할 경우: `Http Status: 422 Unprocessable Entity`
        ```text
        {
            "status": "UNPROCESSABLE_ENTITY",
            "message": "[ERROR] 날짜, 시간만 변경할 수 있습니다. 변경할 수 없는 값을 제외하고 다시 시도해 주세요."
        }
        ```

- 예약 삭제
    - Http Method: DELETE
    - URL: /reservations/{id}
    - Response
        - [x] 정상적으로 삭제된 경우: `Http Status: 204 No Content`

        - [ ] 이미 지나간 시간의 예약을 삭제할 경우: `Http Status: 422 Unprocessable Entity`
        ```text
        {
            "status": "UNPROCESSABLE_ENTITY",
            "message": "[ERROR] 지나간 시간의 예약은 삭제할 수 없습니다."
        }
        ```

### Reservation Time

- 예약 시간 추가
    - Http Method: POST
    - URL: /admin/themes
    - Request
        ```text
        {
            "startAt": "10:00:00"
        }
        ```
    - Response
        - [x] 정상적으로 추가된 경우: `Http Status: 201 Created`
        ```text
        {
            "id": 1,
            "startAt": "10:00:00"
        }
        ```

        - [ ] 존재하는 예약 시간과 동일한 시간일 경우: `Http Status: 409 Conflict`
        ```text
        {
            "status": "CONFLICT",
            "message": "[ERROR] 동일한 예약 시간이 이미 존재합니다. 시간을 변경해 다시 시도해 주세요."
        }
        ```

- 전체 예약 시간 조회
    - Http Method: GET
    - URL: /times
    - Response
        - [x] 정상적으로 조회된 경우: `Http Status: 200 OK`
        ```text
        [
            {
                "id": 1,
                "startAt": "10:00:00"
            },
            {
                "id": 2,
                "startAt": "11:00:00"
            },
            {
                "id": 3,
                "startAt": "12:00:00"
            }
        ]
        ```

- 예약 가능 여부 조회
    - Http Method: GET
    - URL: /times/available?date={date}&themeId={id} / date 형식: yyyy-mm-dd
    - Response
        - [x] 정상적으로 조회된 경우: `Http Status: 200 OK`
        ```text
        [
            {
                "time": {
                    "id": 1,
                    "startAt": "10:00:00"
                },
                "available": true
            },
            {
                "time": {
                    "id": 2,
                    "startAt": "11:00:00"
                },
                "available": false
            }
        ]
        ```

        - [ ] 시작 날짜 또는 종료 날짜가 미래인 경우: `Http Status: 422 Unprocessable Entity`
        ```
        {
            "status": "UNPROCESSABLE_ENTITY",
            "message": "[ERROR] 요청에 미래 날짜가 존재합니다. 현재보다 이전 날짜로 다시 요청해 주세요."
        }
        ```

        - [ ] 종료 날짜가 시작 날짜보다 먼저 올 경우: `Http Status: 422 Unprocessable Entity`
        ```
        {
            "status": "UNPROCESSABLE_ENTITY",
            "message": "[ERROR] 종료 날짜가 시작 날짜보다 빠릅니다. 종료 날짜가 시작 날짜보다 뒤에 오도록 요청해 주세요."
        }
        ```

        - [ ] 조회 기간이 1년을 초과할 경우: `Http Status: 422 Unprocessable Entity`
        ```
        {
            "status": "UNPROCESSABLE_ENTITY",
            "message": "[ERROR] 조회 기간이 최대 기간을 초과했습니다. 기간이 1년 이내가 되도록 다시 요청해 주세요."
        }
        ```

- 예약 시간 삭제
    - Http Method: DELETE
    - URL: /times/{id}
    - Response
        - [x] 정상적으로 삭제된 경우: `Http Status: 204 No Content`

        - [ ] 해당 예약 시간을 사용하는 예약이 존재할 경우: `Http Status: 409 Conflict`
        ```text
        {
            "status": "CONFLICT",
            "message": "[ERROR] 현재 해당 예약 시간을 사용하는 예약이 존재합니다. 연관된 예약을 삭제한 후 다시 시도해 주세요."
        }
        ```
