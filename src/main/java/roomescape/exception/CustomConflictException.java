package roomescape.exception;

public class CustomConflictException extends CustomException {

    public CustomConflictException(ErrorCode errorCode) {
        super(errorCode);
    }
}
