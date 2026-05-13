package roomescape.exception.exception;

public class InvalidRequestException extends BaseCustomException {
    public InvalidRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
