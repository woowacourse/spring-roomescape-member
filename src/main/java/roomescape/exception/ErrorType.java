package roomescape.exception;

public enum ErrorType {
    UNEXPECTED_SERVER_ERROR("서버 관리자에게 문의하세요."),
    ;

    private final String message;

    ErrorType(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
