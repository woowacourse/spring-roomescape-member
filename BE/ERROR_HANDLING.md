# 방탈출 예약 시스템 에러 핸들링 가이드 (Error Handling)

이 문서는 서버에서 발생할 수 있는 주요 예외 상황과 그에 대한 에러 코드(`ErrorCode`), HTTP 상태 코드, 그리고 클라이언트에게 반환되는 에러 메시지를 정리한 문서입니다. `ErrorCode.java`를 기준으로 작성되었습니다.

## 1. 400 Bad Request (잘못된 요청)

클라이언트의 요청 형식이 잘못되었거나 필수 값이 누락되었을 때 발생합니다.

| Error Code | Message | Description |
| :--- | :--- | :--- |
| `INVALID_REQUEST_FORMAT` | 요청 정보가 올바르지 않습니다. 입력 내용을 다시 확인해주세요. | 일반적인 요청 형식 오류 |
| `INVALID_HTTP_MESSAGE` | 요청 정보가 올바르지 않습니다. 입력 내용을 다시 확인해주세요. | JSON 파싱 오류 등 역직렬화 실패 |
| `REQUEST_NAME_EMPTY` | 이름이 비어있습니다. 이름을 입력해주세요 | 요청 시 이름 파라미터 누락/빈 값 |
| `RESERVATION_NAME_EMPTY` | 예약자 성함이 누락되었습니다. 이름을 입력해주세요. | 예약 생성/수정 시 예약자 이름 누락 |
| `RESERVATION_DATE_NULL` | 예약 날짜가 선택되지 않았습니다. 날짜를 선택해주세요. | 예약 생성/수정 시 날짜 누락 |
| `RESERVATION_ID_NULL` | 예약 번호를 확인할 수 없습니다. 다시 시도해 주세요. | 요청 시 예약 ID 누락 |
| `RESERVATION_TIME_NULL` | 예약 시간이 선택되지 않았습니다. 시간을 선택해주세요. | 예약 생성/수정 시 시간 ID 누락 |
| `THEME_NAME_EMPTY` | 테마 이름이 비어있습니다. 이름을 입력해주세요. | 테마 생성/수정 시 이름 누락 |
| `THEME_DESCRIPTION_EMPTY` | 테마 설명이 비어있습니다. 설명을 입력해주세요. | 테마 생성/수정 시 설명 누락 |
| `THEME_THUMBNAIL_EMPTY` | 테마 썸네일 주소가 비어있습니다. 주소를 입력해주세요. | 테마 생성/수정 시 썸네일 주소 누락 |
| `RESERVATION_UPDATE_REQUEST_EMPTY` | 수정할 내용이 없습니다. 변경할 정보를 입력해주세요 | 예약 수정 시 변경 내용 없음 |

## 2. 403 Forbidden (권한 없음)

요청을 수행할 수 있는 권한이 없을 때 발생합니다. (예: 관리자 권한 필요)

| Error Code | Message | Description |
| :--- | :--- | :--- |
| `FORBIDDEN` | 요청을 수행하기 위해 필요한 권한이 없습니다 | 인가 실패 |

## 3. 404 Not Found (리소스를 찾을 수 없음)

요청한 리소스(예약, 시간, 테마 등)가 데이터베이스에 존재하지 않을 때 발생합니다.

| Error Code | Message | Description |
| :--- | :--- | :--- |
| `RESERVATION_NOT_FOUND_BY_ID` | 해당 번호의 예약 정보를 찾을 수 없습니다. | ID로 예약 조회 실패 |
| `RESERVATION_NOT_FOUND_BY_NAME` | 입력하신 성함의 예약 정보를 찾을 수 없습니다. | 이름으로 예약 조회 실패 |
| `RESERVATION_TIME_NOT_FOUND` | 선택하신 예약 시간 정보가 존재하지 않습니다. | 시간 ID로 시간 조회 실패 |
| `THEME_NOT_FOUND` | 선택하신 테마 정보가 존재하지 않습니다. | 테마 ID로 테마 조회 실패 |

## 4. 405 Method Not Allowed (허용되지 않은 메서드)

해당 API 엔드포인트에서 지원하지 않는 HTTP 메서드(GET, POST 등)로 요청했을 때 발생합니다.

| Error Code | Message | Description |
| :--- | :--- | :--- |
| `METHOD_NOT_ALLOWED` | 지원하지 않는 요청 방법입니다. 주소 혹은 요청 방식을 확인해주세요. | 잘못된 HTTP Method 요청 |

## 5. 409 Conflict (충돌 발생)

서버의 현재 상태와 요청이 충돌할 때 발생합니다. 주로 중복 생성이나 참조 무결성 제약 조건 위반 시 사용됩니다.

| Error Code | Message | Description |
| :--- | :--- | :--- |
| `RESERVATION_DUPLICATED` | 동일한 날짜, 시간, 테마의 예약이 이미 존재합니다. 다른 조건을 선택해주세요. | 동일 조건 예약 중복 |
| `RESERVATION_TIME_ALREADY_USED` | 사용 중인 예약 시간은 삭제할 수 없습니다. 관련 예약을 먼저 삭제해주세요. | 예약이 존재하는 시간을 삭제 시도 |
| `THEME_ALREADY_USED` | 사용 중인 테마는 삭제할 수 없습니다. 관련 예약을 먼저 삭제해주세요. | 예약이 존재하는 테마를 삭제 시도 |

## 6. 422 Unprocessable Entity (처리할 수 없는 엔티티)

요청 데이터의 문법은 올바르지만, 비즈니스 로직(의미론적 제약 조건)을 위반하여 처리할 수 없을 때 발생합니다.

| Error Code | Message | Description |
| :--- | :--- | :--- |
| `ILLEGAL_PAST_DATE` | 지나간 날짜나 시간으로는 예약을 생성하거나 삭제할 수 없습니다. 현재 이후의 시간을 선택해주세요. | 과거 날짜/시간에 대한 조작 시도 |
