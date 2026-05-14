package roomescape.exception;

public class CustomConflictException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomConflictException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
