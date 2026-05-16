package roomescape.exception;

public class ForbiddenException extends CustomBusinessException {
    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
