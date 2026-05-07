package roomescape.domain.global.exception;

public enum ErrorCode {

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
