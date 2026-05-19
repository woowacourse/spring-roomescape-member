package roomescape.exception;

public class InvalidDomainStateException extends CodeException {

    public InvalidDomainStateException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
