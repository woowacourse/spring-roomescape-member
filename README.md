# 방탈출 예약 관리 프로그램

## 📌 페이지 응답

**어드민 페이지**

- `/admin`요청 시 어드민 메인 페이지를 응답한다.
    - 어드민 메인 페이지는 `templates/admin/index.html` 파일을 이용한다.
- `/admin/time` 요청 시 시간 관리 페이지를 응답한다.
    - 시간 관리 페이지는 `templates/admin/time.html` 파일을 이용한다.
- `/admin/theme`요청 시 테마 관리 페이지를 응답한다.
    - 테마 관리 페이지는 `templates/admin/theme.html`파일을 이용한다.
- `/admin/reservation`요청 시 예약 관리 페이지를 응답한다.
    - 예약 관리 페이지는 `templates/admin/reservation-new.html`파일을 이용한다.

**사용자 페이지**

- `/login` 요청 시 로그인 폼 페이지를 응답한다.
    - 로그인 폼 페이지는 `templates/login.html` 파일을 이용한다.
- `/`요청 시 인기 테마 페이지를 응답한다.
    - 인기 테마 페이지는`templates/index.html`파일을 이용한다.
- `/reservation`요청 시 사용자 예약 페이지를 응답한다.
    - 사용자 예약 페이지는`templates/reservation.html`파일을 이용한다.

## 📌 기능

**사용자 방탈출 예약**

- 사용자는 날짜와 테마를 선택하면 예약 가능한 시간을 확인할 수 있다.
- 사용자는 예약 가능한 시간을 확인하고, 원하는 시간에 예약을 할 수 있다.
    - 시간을 선택하고 예약자 명을 기입한 후 예약 버튼을 누르면 예약이 완료된다.

**인기 테마**

- 최근 일주일을 기준으로 하여 해당 기간 내에 방문하는 예약이 많은 테마 10개를 보여준다.
    - 예약 이력이 없는 테마는 보여 주지 않는다.
- 현재 날짜로부터 7일 전 날짜를 시작으로 하루 전 날짜까지 예약 건수가 많은 순서대로 테마를 정렬한다.

## 📌 예외 처리와 응답

- 예외 처리 시, CustomException 클래스를 작성하여 사용한다.
    - BusinessRuleViolationException(`422 Unprocessable Content`): 도메인 규칙 위반(ex. 과거 날짜)
    - ExistedDuplicateValueException(`409 Conflict`): 이미 존재하는 값을 중복 입력
    - NotExistedValueException(`404 Not Found`): 존재하지 않는 값
    - InvalidInputException(`400 Bad Request`): 빈 값 / null / 불가능한 값
    - InUseException(`409 Conflict`): 사용 중인 상태를 삭제


- 테마 명이 같다면 동일한 테마로 간주한다.
- 같은 테마에서 같은 날짜와 시간의 예약이 존재하는 경우, 예약이 중복되었다고 판단한다.
