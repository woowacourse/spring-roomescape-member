package roomescape.exception;

public class BusinessConflictException extends BusinessException {

    public BusinessConflictException(ErrorCode errorCode) {
        super(errorCode);
    }
}
