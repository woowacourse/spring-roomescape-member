package roomescape.global.exception;

public enum ErrorCode {

    // Not Found - 404
    RESERVATION_NOT_FOUND_BY_ID("예약을 찾을 수 없습니다. 예약 ID를 확인해주세요."),
    RESERVATION_NOT_FOUND_BY_NAME("예약을 찾을 수 없습니다. 예약자 성함을 확인해주세요."),
    RESERVATION_TIME_NOT_FOUND("예약 시간을 찾을 수 없습니다. 예약 시간 ID를 확인해주세요."),
    THEME_NOT_FOUND("테마를 찾을 수 없습니다. 테마 ID를 확인해주세요."),

    // Bad Request - 400
    REQUEST_NAME_EMPTY("요청 이름이 비어있습니다. 이름을 입력해주세요"),
    RESERVATION_NAME_EMPTY("예약자의 이름이 비어있습니다. 이름을 입력해주세요."),
    RESERVATION_DATE_NULL("예약 날짜가 비어있습니다. 날짜를 선택해주세요."),
    RESERVATION_ID_NULL("예약 목록 ID가 비어있습니다. 요청 형식을 확인해주세요."),
    RESERVATION_TIME_NULL("예약 시간이 비어있습니다. 시간을 선택해주세요."),
    THEME_NAME_EMPTY("테마 이름이 비어있습니다. 이름을 입력해주세요."),
    THEME_DESCRIPTION_EMPTY("테마 설명이 비어있습니다. 설명을 입력해주세요."),
    THEME_THUMBNAIL_EMPTY("테마 썸네일이 비어있습니다. 썸네일 URL을 입력해주세요."),

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
