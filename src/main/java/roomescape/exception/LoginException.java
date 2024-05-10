package roomescape.exception;

public class LoginException extends BadRequestException {
    public LoginException(String message) {
        super(message);
    }
}
