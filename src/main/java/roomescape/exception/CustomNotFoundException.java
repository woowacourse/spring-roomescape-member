package roomescape.exception;

public class CustomNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
