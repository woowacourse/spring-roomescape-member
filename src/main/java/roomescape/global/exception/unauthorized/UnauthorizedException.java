package roomescape.global.exception.unauthorized;

public class UnauthorizedException extends IllegalArgumentException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
