package roomescape.global.exception;

public class WrongPasswordException extends IllegalRequestException {

    public WrongPasswordException(String message) {
        super(message);
    }

    public WrongPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
