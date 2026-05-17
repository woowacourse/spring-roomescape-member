package roomescape.exception;

public class NotFoundException extends CustomBusinessException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
