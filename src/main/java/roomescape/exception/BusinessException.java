package roomescape.exception;

public class BusinessException extends RuntimeException {

    private final ErrorCode exceptionCode;

    public BusinessException(ErrorCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public ErrorCode getExceptionCode() {
        return exceptionCode;
    }
}
