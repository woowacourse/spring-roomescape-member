package roomescape.exception;

public class ConflictException extends CustomBusinessException {
    public ConflictException(ErrorCode errorCode) {
        super(errorCode);
    }
}
