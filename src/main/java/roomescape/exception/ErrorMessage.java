package roomescape.exception;

public enum ErrorMessage {
    INVALID_NAME_BLANK( "이름은 필수입니다."),
    INVALID_NAME_LENGTH("이름은 20자를 초과할 수 없습니다."),
    INVALID_DATE_NULL("날짜는 필수입니다."),
    INVALID_DATE_FORMAT("유효하지 않은 날짜입니다."),
    INVALID_TIME_ID_FORMAT("시간 ID는 0보다 커야 합니다."),

    INVALID_THEME_ID_FORMAT("테마 ID는 0보다 커야 합니다."),
    INVALID_THEME_ID("유효하지 않은 테마 id입니다."),

    INVALID_START_TIME_NULL("시작 시간은 필수입니다."),
    INVALID_START_TIME_FORMAT("유효하지 않은 시간입니다."),

    INVALID_RESERVATION_TIME_ID("유효하지 않은 시간 id입니다."),

    CANNOT_DELETE_RESERVATION_TIME_IN_USE("해당 시간을 참조하는 예약 데이터가 존재하기 때문에 삭제할 수 없습니다."),
    CANNOT_DELETE_THEME_IN_USE("해당 테마를 참조하는 예약 데이터가 존재하기 때문에 삭제할 수 없습니다."),

    INTEGRITY_VIOLATION_ON_DELETE("데이터 무결성 위반으로 삭제에 실패했습니다."),

    DUPLICATED_RESERVATION_REQUEST("해당 날짜, 시간, 테마의 예약이 존재하여 예약할 수 없습니다."),

    UNAUTHORIZED_DELETE_RESERVATION_REQUEST("해당 예약을 삭제할 권한이 없습니다."),
    ;

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
