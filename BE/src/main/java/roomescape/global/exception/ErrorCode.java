package roomescape.global.exception;

public enum ErrorCode {

    // Not Found - 404
    RESERVATION_NOT_FOUND("예약을 찾을 수 없습니다."),
    RESERVATION_TIME_NOT_FOUND("예약 시간을 찾을 수 없습니다."),
    THEME_NOT_FOUND("테마를 찾을 수 없습니다."),

    // Bad Request - 400
    RESERVATION_NAME_EMPTY("예약자의 이름이 비어있습니다."),
    RESERVATION_DATE_NULL("예약 날짜가 비어있습니다."),
    RESERVATION_ID_NULL("예약 목록 ID가 비어있습니다."),
    RESERVATION_TIME_NULL("예약 시간이 비어있습니다."),
    THEME_NAME_EMPTY("테마 이름은 null 혹은 빈 값일 수 없습니다."),
    THEME_DESCRIPTION_EMPTY("테마 설명은 null 혹은 빈 값일 수 없습니다."),
    THEME_THUMBNAIL_EMPTY("테마 썸네일은 null 혹은 빈 값일 수 없습니다."),

    // Conflict - 409
    RESERVATION_DUPLICATED("동일한 날짜, 시간, 테마의 예약이 이미 존재합니다."),
    RESERVATION_TIME_ALREADY_USED("참조하고 있는 예약 시간이어서 삭제할 수 없습니다."),
    THEME_ALREADY_USED("참조하고 있는 테마이어서 삭제할 수 없습니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
