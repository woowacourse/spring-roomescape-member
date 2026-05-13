package roomescape.exception.exception;

public class DuplicatedResourceException extends BaseCustomException {
    public DuplicatedResourceException(ErrorCode errorCode) {
        super(errorCode);
    }
}
