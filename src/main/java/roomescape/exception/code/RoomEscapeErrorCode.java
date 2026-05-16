package roomescape.exception.code;

public enum RoomEscapeErrorCode {
    RESERVATION_DATE_IN_PAST("RESERVATION_DATE_IN_PAST", "과거의 날짜는 예약할 수 없습니다."),
    RESERVATION_TIME_IN_PAST("RESERVATION_TIME_IN_PAST", "과거의 시간은 예약할 수 없습니다."),
    RESERVATION_ALREADY_RESERVED("RESERVATION_ALREADY_RESERVED", "누군가 이미 예약한 시간입니다."),
    RESERVATION_NOT_FOUND("RESERVATION_NOT_FOUND", "예약을 찾을 수 없습니다."),
    RESERVATION_NOT_CHANGED("RESERVATION_NOT_CHANGED", "변경할 예약 정보가 없습니다."),

    RESERVATION_TIME_NOT_FOUND("RESERVATION_TIME_NOT_FOUND", "예약 시간을 찾을 수 없습니다."),
    RESERVATION_TIME_ALREADY_USED("RESERVATION_TIME_ALREADY_USED", "이미 사용된 예약 시간입니다."),
    RESERVATION_TIME_ALEADY_EXISTS("RESERVATION_TIME_ALREADY_EXISTS", "이미 존재하는 예약 시간입니다."),

    THEME_NOT_FOUND("THEME_NOT_FOUND", "존재하지 않는 테마입니다.");

    private String code;
    private String message;

    RoomEscapeErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
