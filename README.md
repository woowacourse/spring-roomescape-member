# Mission 2) 방탈출 사용자 예약

- 테마별 예외처리를 함께 고려하기 위하여 2단계 → 1단계 → 3단계 순으로 진행한다.

## 0단계: 기본 코드 준비

Mission 1의 코드 파일을 복사해서 준비한다.

- 기반 코드: 플린트

## 1단계: 예외 처리와 응답

- 예외 처리 시, CustomException 클래스를 작성하여 사용한다.
    - `PharmaceuticalViolationException`
        - **도메인 규칙 위반(ex. 과거 날짜, 존재하지 않는 ID)** `422 Unprocessable Content`
    - `ExistedDuplicateValueException`
        - **이미 존재하는 값을 중복 입력, 사용 중인 상태를 삭제** `409 Conflict`
    - `NotExistedValueException`
        - **존재하지 않는 값** `404 Not Found`
    - `InvalidInputException`
        - **필드 누락 / 빈 값 / null** `400 Bad Request`

### 📌시간만 고려했을 때 예외사항 (시간)

- 동일한 시간이 추가 입력되는 경우 `409 Conflict`
- 유효하지 않는 시간 값(null, 시간 형식)을 입력하는 경우 `400 BAD REQEUST`
- 유효하지 않는 시간을 삭제하려고 할 때 (즉, id 값이 옳지 않을 때) `404 Not Found`
- 예약에서 사용중인 시간을 삭제하려고 할 때 `422 Unprocessable Content`

### 📌테마만 고려했을 때 예외사항 (테마 이름, 테마 설명, 테마 썸네일)

- 동일한 테마가 입력되는 경우 (테마 명을 기준으로 확인) `409 Conflict`
- 유효하지 않는 테마 값(null, 테마 형식)을 입력하는 경우 `400 BAD REQEUST`
- 유효하지 않는 테마를 삭제하려고 할 때 (즉, id 값이 옳지 않을 때) `404 Not Found`
- 예약에서 사용중인 테마를 삭제하려고 할 때 `422 Unprocessable Content`

### 📌예약, 날짜, 시간, 테마를 고려했을 때 예외사항

- 같은 테마에서 같은 날짜와 시간의 예약이 존재할 경우 `409 Conflict`
- 날짜와 시간을 고려해서 과거의 날짜나 시간인 경우 `422 Unprocessable Content`
- 예약 생성 시, 예약자명, 날짜, 시간, 테마 입력이 빈값이거나 null인 경우 `400 BAD REQEUST`

## 2단계: 테마 추가

- `/admin/theme`요청 시 테마 관리 페이지를 응답한다.
    - 페이지는`templates/admin/theme.html`파일을 이용한다.
- 어드민에서 방탈출 예약 시, 테마 정보를 포함할 수 있도록 신규 페이지 파일을 사용한다.
    - AS-IS:**`templates/admin/reservation.html`**
    - TO-BE:**`templates/admin/reservation-new.html`**

## 3단계: 사용자 기능

### 사용자 예약

- 사용자는 날짜와 테마를 선택하면 예약 가능한 시간을 확인할 수 있다.
- 사용자는 예약 가능한 시간을 확인하고, 원하는 시간에 예약을 할 수 있다.
    - 시간을 선택하고 예약자 명을 기입한 후 예약 버튼을 누르면 예약이 완료된다.
- `/reservation`요청 시 사용자 예약 페이지를 응답한다.
    - 페이지는`templates/reservation.html`파일을 이용한다.

### 인기 테마

- 최근 일주일을 기준으로 하여 해당 기간 내에 방문하는 예약이 많은 테마 10개를 보여준다.
    - 예약 이력이 없는 테마는 보여주지 않는다.
- 현재 날짜로부터 7일 전 날짜를 시작으로 하루 전 날짜까지 예약 건수가 많은 순서대로 테마를 정렬한다.
- `/`요청 시 인기 테마 페이지를 응답한다.
    - 페이지는`templates/index.html`파일을 이용한다.