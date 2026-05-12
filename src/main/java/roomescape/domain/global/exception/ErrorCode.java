package roomescape.domain.global.exception;

public enum ErrorCode {

    RESERVATION_INVALID_REQUEST("요청 형식이 잘못되었습니다."),
    RESERVATION_DUPLICATE("이미 존재하는 예약입니다."),
    RESERVATION_INVALID_DATETIME("지난 날짜 및 시간에 예약을 시도했습니다."),

    TIME_NOT_FOUND("요청한 시간을 찾을 수 없습니다."),

    THEME_NOT_FOUND("요청한 테마를 찾을 수 없습니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
