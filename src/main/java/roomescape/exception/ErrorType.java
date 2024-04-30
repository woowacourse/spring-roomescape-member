package roomescape.exception;

public enum ErrorType {
    UNEXPECTED_SERVER_ERROR("서버 관리자에게 문의하세요."),
    ;

    ErrorType(final String message) {
        this.message = message;
    }

    private final String message;

    public String getMessage() {
        return message;
    }
}
