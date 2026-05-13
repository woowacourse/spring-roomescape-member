package roomescape.exception.exception;

public class NotFoundResourceException extends BaseCustomException {
    public NotFoundResourceException(ErrorCode errorCode) {
        super(errorCode);
    }
}
