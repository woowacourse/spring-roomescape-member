package roomescape.exceptions;

public class LoginFailException extends RuntimeException {

    public LoginFailException(String message) {
        super(message);
    }
}
