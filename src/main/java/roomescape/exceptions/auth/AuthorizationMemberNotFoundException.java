package roomescape.exceptions.auth;

public class AuthorizationMemberNotFoundException extends RuntimeException {

    public AuthorizationMemberNotFoundException(String message) {
        super("[ERROR] " + message);
    }
}
