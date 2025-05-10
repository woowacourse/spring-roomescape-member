package roomescape.exceptions.auth;

public class AuthorizationException extends IllegalArgumentException {

    public AuthorizationException(String message) {
        super("[ERROR] " + message);
    }
}
