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

- 사용자의 테마 조회
    - Http Method: GET
    - URL: /themes
    - Response
        - 정상적으로 조회된 경우: `Http Status: 200 OK`
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

- 관리자의 테마 조회
    - Http Method: GET
    - URL: /admin/themes
    - Response
        - 정상적으로 조회된 경우: `Http Status: 200 OK`
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
        - 정상적으로 추가된 경우: `Http Status: 201 Created`
  ```text
  {
      "id": 1,
      "name": "피즈의 모험",
      "description": "피즈가 모험을 떠나는 이야기입니다.",
      "thumbnail": "http://localhost:8080/images/fizz.jpg"
  }
  ```

- 관리자의 테마 삭제
    - Http Method: GET
    - URL: /admin/themes/{id}
    - Response
        - 정상적으로 삭제된 경우: `Http Status: 204 No Content`

    - 사용자의 예약 추가
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
            - 정상적으로 추가된 경우: `Http Status: 201 Created`
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
        - 날짜, 시간, 테마가 동일한 예약일 경우: `Http Status: 400 Bad Request`
        ```text
      {
          "status": "BAD_REQUEST",
          "message": "[ERROR] 중복된 예약이 이미 존재합니다."
      }
      ```

- 관리자의 예약 추가
    - Http Method: POST
    - URL: /admin/reservation
    - Request
  ```text
  {
        "name": "fizz",
        "date": "2026-05-02",
        "timeId": 1
  }
  ```
    - Response
        - 정상적으로 추가된 경우: `Http Status: 201 Created`
  ```text
  {
        "id": 1,
        "name": "fizz",
        "date": "2026-05-02",
        "time": {
            "id": 1,
            "startAt": "10:00:00"
        }
  }
  ```
    - 이름, 날짜, 시간이 동일한 예약일 경우: `Http Status: 400 Bad Request`
  ```text
  {
      "status": "BAD_REQUEST",
      "message": "[ERROR] 중복된 예약이 이미 존재합니다."
  }
  ```

- 예약 가능 시간 조회
    - Http Method: GET
    - URL: /times?date={date}&themeId={id} / date 형식: yyyy-mm-dd
    - Response
        - 정상적으로 조회된 경우: `Http Status: 200 OK`
  ```text
  {
      {
        "id": 1,
        "startAt": "10:00"
      },
      {
        "id": 2,
        "startAt": "11:00"
      }
  }
  ```

- 인기 테마 조회
    - Http Method: GET
    - URL: /themes/ranking?start-date={start-date}&end-date={end-date} / date 형식: yyyy-mm-dd
    - Response
        - 정상적으로 조회된 경우: `Http Status: 200 OK`
  ```text
  {
      {
        "theme": {
          "id": 1,
          "name": "피즈의 모험",
          "description": "피즈가 모험을 떠나는 이야기입니다.",
          "thumbnail": "http://localhost:8080/images/fizz.jpg"
        }
      },
      {
        "theme": {
          "id": 2,
          "name": "나무의 일대기",
          "date": "나무가 살아온 인생을 보여주는 이야기입니다.",
          "thumbnail": "http://localhost:8080/images/tree.jpg"
        }
      },
  }
  ```
