package roomescape.exception;

public class BadRequestException extends CustomBusinessException {
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
