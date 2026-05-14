package roomescape.global.exception;

public enum ErrorCode {

    // Not Found - 404
    RESERVATION_NOT_FOUND_BY_ID("해당 번호의 예약 정보를 찾을 수 없습니다."),
    RESERVATION_NOT_FOUND_BY_NAME("입력하신 성함의 예약 정보를 찾을 수 없습니다."),
    RESERVATION_TIME_NOT_FOUND("선택하신 예약 시간 정보가 존재하지 않습니다."),
    THEME_NOT_FOUND("선택하신 테마 정보가 존재하지 않습니다."),

    // Bad Request - 400
    INVALID_REQUEST_FORMAT("요청 정보가 올바르지 않습니다. 입력 내용을 다시 확인해주세요."),
    REQUEST_NAME_EMPTY("이름이 비어있습니다. 이름을 입력해주세요"),
    RESERVATION_NAME_EMPTY("예약자 성함이 누락되었습니다. 이름을 입력해주세요."),
    RESERVATION_DATE_NULL("예약 날짜가 선택되지 않았습니다. 날짜를 선택해주세요."),
    RESERVATION_ID_NULL("예약 번호를 확인할 수 없습니다. 다시 시도해 주세요."),
    RESERVATION_TIME_NULL("예약 시간이 선택되지 않았습니다. 시간을 선택해주세요."),
    THEME_NAME_EMPTY("테마 이름이 비어있습니다. 이름을 입력해주세요."),
    THEME_DESCRIPTION_EMPTY("테마 설명이 비어있습니다. 설명을 입력해주세요."),
    THEME_THUMBNAIL_EMPTY("테마 썸네일 주소가 비어있습니다. 주소를 입력해주세요."),
    RESERVATION_UPDATE_REQUEST_EMPTY("수정할 내용이 없습니다. 변경할 정보를 입력해주세요"),

    // Bad Request - 400 (Generic)
    INVALID_HTTP_MESSAGE("요청 정보가 올바르지 않습니다. 입력 내용을 다시 확인해주세요."),

    // Method Not Allowed - 405
    METHOD_NOT_ALLOWED("지원하지 않는 요청 방법입니다. 주소 혹은 요청 방식을 확인해주세요."),

    // Conflict - 409
    RESERVATION_DUPLICATED("동일한 날짜, 시간, 테마의 예약이 이미 존재합니다. 다른 조건을 선택해주세요."),
    RESERVATION_TIME_ALREADY_USED("사용 중인 예약 시간은 삭제할 수 없습니다. 관련 예약을 먼저 삭제해주세요."),
    THEME_ALREADY_USED("사용 중인 테마는 삭제할 수 없습니다. 관련 예약을 먼저 삭제해주세요."),

    // Unprocessable Entity - 422
    ILLEGAL_PAST_DATE("지나간 날짜나 시간으로는 예약을 생성하거나 삭제할 수 없습니다. 현재 이후의 시간을 선택해주세요.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
