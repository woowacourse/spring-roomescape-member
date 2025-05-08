package roomescape.exception.unauthorized;

public class UserUnauthorizedException extends UnauthorizedException {
    public UserUnauthorizedException() {
        super("유저");
    }
}
