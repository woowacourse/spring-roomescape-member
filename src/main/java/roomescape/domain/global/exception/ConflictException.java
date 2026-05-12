package roomescape.domain.global.exception;

public class ConflictException extends RuntimeException implements BaseException {

    private final ErrorCode errorCode;

    public ConflictException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
