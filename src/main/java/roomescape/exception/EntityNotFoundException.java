package roomescape.exception;

public class EntityNotFoundException extends CodeException {

    public EntityNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
