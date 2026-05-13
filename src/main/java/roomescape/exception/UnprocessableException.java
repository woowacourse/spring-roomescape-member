package roomescape.exception;

public class UnprocessableException extends BaseException {

    public UnprocessableException(ErrorCode errorCode) {
        super(errorCode);
    }
}
