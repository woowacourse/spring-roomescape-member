package roomescape.global.exception;

public abstract class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String title;

    protected CustomException(ErrorCode errorCode, String title) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.title = title;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getTitle() {
        return title;
    }
}
