package roomescape.exception.business;

public class BusinessConflictException extends BusinessException {

    public BusinessConflictException(ErrorCode errorCode) {
        super(errorCode);
    }
}
