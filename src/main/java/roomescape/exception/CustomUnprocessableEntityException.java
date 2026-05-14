package roomescape.exception;

public class CustomUnprocessableEntityException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomUnprocessableEntityException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
