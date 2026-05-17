package roomescape.exception;

public enum ErrorCode {

    // 테마
    THEME_DUPLICATED("이미 존재하는 테마입니다."),
    THEME_NOT_FOUND("존재하지 않는 테마입니다."),
    THEME_HAS_RESERVATIONS("예약이 존재하는 테마는 삭제할 수 없습니다."),
    THEME_NAME_BLANK("테마 이름은 빈값일 수 없습니다. "),
    THEME_NAME_LENGTH_INVALID("테마 이름은 20자 이하입니다."),
    THEME_DESCRIPTION_BLANK("테마 설명은 빈값일 수 없습니다."),
    THEME_DESCRIPTION_LENGTH_INVALID("테마 설명 길이는 1000자 이하입니다."),
    THEME_URL_BLANK("테마 URL은 빈값일 수 없습니다."),

    // 예약 시간
    TIME_NOT_ON_THE_HOUR("예약 시작 시간은 정각이어야 합니다."),
    TIME_DUPLICATED("이미 존재하는 예약 시간입니다."),
    TIME_NOT_FOUND("존재하지 않는 예약 시간입니다."),
    TIME_HAS_RESERVATIONS("예약이 존재하는 시간은 삭제할 수 없습니다."),
    TIME_NULL("예약 시작 시간은 필수입니다."),
    // 예약
    RESERVATION_DUPLICATED("이미 존재하는 예약입니다."),
    RESERVATION_NOT_FOUND("존재하지 않는 예약입니다."),
    RESERVATION_NOT_OWNER("본인의 예약만 변경/취소할 수 있습니다."),
    RESERVATION_PAST_DATE("이미 지난 날짜·시간으로는 예약할 수 없습니다."),
    RESERVATION_PAST_UPDATE("이미 지난 예약은 변경/취소할 수 없습니다."),
    RESERVATION_TIME_ALREADY_BOOKED("변경하려는 날짜·시간에 이미 예약이 존재합니다."),
    RESERVATION_NAME_BLANK("예약자 이름은 빈값일 수 없습니다."),
    RESERVATION_NAME_LENGTH_INVALID("예약자 이름은 20자 이하여야 합니다."),
    RESERVATION_DATE_NULL("예약 날짜는 필수입니다."),
    RESERVATION_TIME_NULL("예약 시간은 필수입니다."),
    RESERVATION_THEME_NULL("예약 테마는 필수입니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getCode() {
        return name();
    }

    public String getMessage() {
        return message;
    }

}