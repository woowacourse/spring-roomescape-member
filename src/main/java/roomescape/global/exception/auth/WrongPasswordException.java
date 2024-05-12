package roomescape.global.exception.auth;

import roomescape.global.exception.IllegalRequestException;

public class WrongPasswordException extends IllegalRequestException {

    public WrongPasswordException(String message) {
        super(message);
    }

    public WrongPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
