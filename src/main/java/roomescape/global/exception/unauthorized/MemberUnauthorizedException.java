package roomescape.global.exception.unauthorized;

public class MemberUnauthorizedException extends UnauthorizedException {
    public MemberUnauthorizedException() {
        super("유저");
    }
}
