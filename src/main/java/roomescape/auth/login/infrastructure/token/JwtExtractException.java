package roomescape.auth.login.infrastructure.token;

public class JwtExtractException extends RuntimeException {

    public JwtExtractException(String message) {
        super(message);
    }
}
