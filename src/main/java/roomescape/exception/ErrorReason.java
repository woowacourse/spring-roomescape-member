package roomescape.exception;

public enum ErrorReason {
    RESERVATION_NOT_FOUND("예약을 찾을 수 없습니다."),
    RESERVATION_TIME_NOT_FOUND("예약 시간을 찾을 수 없습니다."),
    RESERVATION_ALREADY_CANCELED("이미 취소된 예약입니다."),
    RESERVATION_DUPLICATE("동일한 날짜, 시간, 테마에 예약이 등록되어 있습니다."),
    RESERVATION_TIME_IN_USE("해당 시간에 이미 예약이 있습니다."),
    RESERVATION_NOT_OWNER("다른 이용자의 예약은 변경할 수 없습니다."),
    THEME_NOT_FOUND("테마를 찾을 수 없습니다."),
    PAST_RESERVATION("지난 시간의 예약은 변경할 수 없습니다.");

    private final String message;

    ErrorReason(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
