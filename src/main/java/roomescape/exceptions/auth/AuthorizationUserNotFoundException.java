package roomescape.exceptions.auth;

public class AuthorizationUserNotFoundException extends RuntimeException {

    public AuthorizationUserNotFoundException(String message) {
        super("[ERROR] " + message);
    }
}
