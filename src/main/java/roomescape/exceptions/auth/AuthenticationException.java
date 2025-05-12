package roomescape.exceptions.auth;

public class AuthenticationException extends IllegalArgumentException {

    public AuthenticationException(String message) {
        super("[ERROR] " + message);
    }
}
