package roomescape.exception;

public class InUseEntityException extends CodeException {

    public InUseEntityException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
